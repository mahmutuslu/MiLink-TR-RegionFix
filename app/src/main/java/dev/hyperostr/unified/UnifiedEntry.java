package dev.hyperostr.unified;

import android.util.Log;
import dev.milinktr.regionfix.MiLinkModule;
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam;
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam;
import io.github.libxposed.api.XposedModuleInterface.PackageReadyParam;

/**
 * A unique LSPosed API 102 entry for the unified APK.
 * Keeps the verified MiLink behavior in the existing base implementation.
 */
public final class UnifiedEntry extends MiLinkModule {
    private static final String TAG = "HyperOSTR";

    static {
        Log.i(TAG, "UnifiedEntry class initialized");
    }

    @Override
    public void onModuleLoaded(ModuleLoadedParam param) {
        Log.i(TAG, "UnifiedEntry API102 attached to process " + param.getProcessName());
        super.onModuleLoaded(param);
    }

    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        Log.i(TAG, "UnifiedEntry package loaded: " + param.getPackageName());
        super.onPackageLoaded(param);
    }

    @Override
    public synchronized void onPackageReady(PackageReadyParam param) {
        Log.i(TAG, "UnifiedEntry package ready: " + param.getPackageName());
        super.onPackageReady(param);
    }
}
