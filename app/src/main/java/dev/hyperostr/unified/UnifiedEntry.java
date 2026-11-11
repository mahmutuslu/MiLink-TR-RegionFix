package dev.hyperostr.unified;

import android.util.Log;
import dev.milinktr.regionfix.MiLinkModule;
import io.github.libxposed.api.XposedModuleInterface.ModuleLoadedParam;
import io.github.libxposed.api.XposedModuleInterface.PackageLoadedParam;
import io.github.libxposed.api.XposedModuleInterface.PackageReadyParam;

/** Single API 102 entry. MiLink and Xiaomi Share only. */
public final class UnifiedEntry extends MiLinkModule {
    private static final String TAG = "HyperOSTR";

    static {
        Log.i(TAG, "MiLink TR + Region Fix v1.0.0 entry initialized");
    }

    @Override
    public void onModuleLoaded(ModuleLoadedParam param) {
        Log.i(TAG, "API102 attached to process " + param.getProcessName());
        super.onModuleLoaded(param);
    }

    @Override
    public void onPackageLoaded(PackageLoadedParam param) {
        super.onPackageLoaded(param);
    }

    @Override
    public synchronized void onPackageReady(PackageReadyParam param) {
        super.onPackageReady(param);
    }
}
