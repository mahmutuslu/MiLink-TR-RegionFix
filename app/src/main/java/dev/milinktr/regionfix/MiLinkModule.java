package dev.milinktr.regionfix;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
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
 * Native libxposed API 102 test module.
 *
 * Scope is com.milink.service only. This module NEVER changes Xiaomi account
 * authentication, server responses, network state, or device authorization.
 * The regional investigation is intentionally read-only in alpha1.
 */
public final class MiLinkModule extends XposedModule {
    private static final String TAG = "MiLinkTR";
    private static final String PACKAGE = "com.milink.service";
    private static final String TARGET_ACTIVITY =
            "com.miui.circulate.world.ui.connectivitysettings.ConnectivityActivity";

    private final AtomicInteger translatedLogCount = new AtomicInteger();
    private boolean installed;

    @Override
    public void onModuleLoaded(ModuleLoadedParam param) {
        Log.i(TAG, "alpha1 API102 module loaded, process=" + param.getProcessName());
    }

    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        if (PACKAGE.equals(param.getPackageName())) {
            Log.i(TAG, "MiLink package loaded; waiting for ready callback");
        }
    }

    @Override
    public synchronized void onPackageReady(PackageReadyParam param) {
        if (!PACKAGE.equals(param.getPackageName()) || installed) return;
        installed = true;

        RegionDiagnostics.report();
        installTextHook();
        installFocusRefreshHook();
    }

    private void installTextHook() {
        try {
            Method method = TextView.class.getDeclaredMethod(
                    "setText", CharSequence.class, TextView.BufferType.class);
            hook(method).setId("milink-tr-textview-alpha1").intercept(chain -> {
                Object receiver = chain.getThisObject();
                if (!(receiver instanceof TextView)) return chain.proceed();

                TextView view = (TextView) receiver;
                if (!isConnectivityContext(view.getContext())) return chain.proceed();

                Object arg = chain.getArg(0);
                if (!(arg instanceof CharSequence)) return chain.proceed();
                CharSequence original = (CharSequence) arg;
                CharSequence replacement = Translations.translate(original);
                if (replacement == original) return chain.proceed();

                if (translatedLogCount.getAndIncrement() < 40) {
                    Log.i(TAG, "translated UI label: " + safePreview(original));
                }
                return chain.proceed(new Object[]{replacement, chain.getArg(1)});
            });
            Log.i(TAG, "TextView#setText hook installed");
        } catch (Throwable error) {
            Log.e(TAG, "TextView hook unavailable", error);
        }
    }

    private void installFocusRefreshHook() {
        // An optional second pass for labels populated before the text hook.
        // It changes only TextViews inside the known MiLink Activity.
        try {
            Method method = Activity.class.getDeclaredMethod(
                    "onWindowFocusChanged", boolean.class);
            hook(method).setId("milink-tr-focus-alpha1").intercept(chain -> {
                Object result = chain.proceed();
                Object receiver = chain.getThisObject();
                if (receiver instanceof Activity && Boolean.TRUE.equals(chain.getArg(0))) {
                    Activity activity = (Activity) receiver;
                    if (TARGET_ACTIVITY.equals(activity.getClass().getName())
                            && activity.getWindow() != null) {
                        rewriteTree(activity.getWindow().getDecorView());
                    }
                }
                return result;
            });
            Log.i(TAG, "Activity focus refresh installed");
        } catch (Throwable error) {
            Log.w(TAG, "Focus refresh unavailable (text hook may still work): " + error);
        }
    }

    private static boolean isConnectivityContext(Context context) {
        for (int i = 0; context != null && i < 12; ++i) {
            if (context instanceof Activity &&
                    TARGET_ACTIVITY.equals(context.getClass().getName())) return true;
            if (context instanceof ContextWrapper) {
                Context next = ((ContextWrapper) context).getBaseContext();
                if (next == context) break;
                context = next;
            } else {
                break;
            }
        }
        return false;
    }

    private static void rewriteTree(View view) {
        if (view == null) return;
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            CharSequence original = textView.getText();
            CharSequence translated = Translations.translate(original);
            if (translated != original) {
                textView.setText(translated);
            }
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                rewriteTree(group.getChildAt(i));
            }
        }
    }

    private static String safePreview(CharSequence text) {
        if (text == null) return "<null>";
        String s = text.toString().replace('\n', ' ');
        return s.length() > 80 ? s.substring(0, 80) : s;
    }
}
