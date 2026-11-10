package dev.milinktr.regionfix;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam;
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam;
import io.github.libxposed.api.XposedModuleInterface.PackageReadyParam;

/**
 * API 102. UI text hooks run in MiLink and Xiaomi Share only.
 * Region compatibility applies only to already-trusted remote device IDs;
 * Xiaomi Account and remote-server authorization are not altered.
 */
public final class MiLinkModule extends XposedModule {
    private static final String TAG = "MiLinkTR";
    private static final String MILINK = "com.milink.service";
    private static final String MISHARE = "com.miui.mishare.connectivity";
    private final AtomicInteger translatedLogs = new AtomicInteger();
    private boolean installed;
    private ClassLoader loader;

    @Override
    public void onModuleLoaded(ModuleLoadedParam param) {
        Log.i(TAG, "alpha6 API102 loaded in " + param.getProcessName());
    }

    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        if (isTranslatedPackage(param.getPackageName())) {
            Log.i(TAG, "Translation target loaded: " + param.getPackageName());
        }
    }

    @Override
    public synchronized void onPackageReady(PackageReadyParam param) {
        if (!isTranslatedPackage(param.getPackageName()) || installed) return;
        installed = true;
        loader = param.getClassLoader();
        if (MILINK.equals(param.getPackageName())) RegionDiagnostics.report();
        installTextHook();
        installFocusHook();
        // Local region compatibility must never touch the Xiaomi Share process.
        if (MILINK.equals(param.getPackageName())) {
            new PcRegionFix(this, loader).install();
            new SameAccountDiscoveryFix(this, loader).install();
        }
    }

    private void installTextHook() {
        try {
            Method m = TextView.class.getDeclaredMethod(
                    "setText", CharSequence.class, TextView.BufferType.class);
            hook(m).setId("milink-tr-textview-alpha6").intercept(chain -> {
                Object receiver = chain.getThisObject();
                if (!(receiver instanceof TextView)) return chain.proceed();
                TextView tv = (TextView) receiver;
                if (!isMiLinkView(tv)) return chain.proceed();

                Object original = chain.getArg(0);
                if (!(original instanceof CharSequence)) return chain.proceed();
                CharSequence value = (CharSequence) original;
                CharSequence translated = Translations.translate(value);
                if (translated == value) return chain.proceed();
                if (translatedLogs.getAndIncrement() < 40) {
                    Log.i(TAG, "UI label translated");
                }
                return chain.proceed(new Object[]{translated, chain.getArg(1)});
            });
            Log.i(TAG, "TextView#setText active for MiLink / Xiaomi Share");
        } catch (Throwable t) {
            Log.e(TAG, "TextView hook installation failed", t);
        }
    }

    private void installFocusHook() {
        try {
            Method m = Activity.class.getDeclaredMethod("onWindowFocusChanged", boolean.class);
            hook(m).setId("milink-tr-window-focus-alpha6").intercept(chain -> {
                Object result = chain.proceed();
                Object receiver = chain.getThisObject();
                if (receiver instanceof Activity && Boolean.TRUE.equals(chain.getArg(0))) {
                    Activity activity = (Activity) receiver;
                    if (isTranslatedPackage(activity.getPackageName())
                            && activity.getWindow() != null) {
                        if (MILINK.equals(activity.getPackageName())) {
                            TranslationCatalog.ensureLoaded(activity, loader, () -> {
                            if (!activity.isFinishing() && !activity.isDestroyed()) {
                                activity.runOnUiThread(() -> {
                                    if (activity.getWindow() != null) {
                                        rewriteTree(activity.getWindow().getDecorView());
                                    }
                                });
                            }
                            });
                        }
                        rewriteTree(activity.getWindow().getDecorView());
                    }
                }
                return result;
            });
            Log.i(TAG, "MiLink and Xiaomi Share Activity focus refresh installed");
        } catch (Throwable t) {
            Log.e(TAG, "Activity focus hook failed", t);
        }
    }

    private static boolean isTranslatedPackage(String packageName) {
        return MILINK.equals(packageName) || MISHARE.equals(packageName);
    }

    private static boolean isMiLinkView(TextView view) {
        try {
            Context ctx = view.getContext();
            return ctx != null && isTranslatedPackage(ctx.getPackageName());
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static void rewriteTree(View view) {
        if (view == null) return;
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if (isMiLinkView(tv)) {
                CharSequence before = tv.getText();
                CharSequence after = Translations.translate(before);
                if (after != before) tv.setText(after);
            }
        }
        if (view instanceof ViewGroup) {
            ViewGroup root = (ViewGroup) view;
            for (int i = 0; i < root.getChildCount(); ++i) {
                rewriteTree(root.getChildAt(i));
            }
        }
    }
}
