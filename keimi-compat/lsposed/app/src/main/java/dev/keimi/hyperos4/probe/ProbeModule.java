package dev.keimi.hyperos4.probe;

import java.lang.reflect.Method;
import android.util.Log;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface.PackageReadyParam;

/**
 * Diagnostic only. No arguments, return values, or OEM feature flags are modified.
 * Scope is strictly Mirror + MiLink; does not touch KeiMi or system_server.
 */
public final class ProbeModule extends XposedModule {
    @Override
    public void onPackageReady(PackageReadyParam param) {
        final String pkg = param.getPackageName();
        if (!"com.xiaomi.mirror".equals(pkg) && !"com.milink.service".equals(pkg)) {
            return;
        }
        report("KeiMiProbe: attached package=" + pkg + " API=" + getApiVersion());
        if (!"com.milink.service".equals(pkg)) {
            report("KeiMiProbe: Mirror instrumentation intentionally disabled pending exact hook targets.");
            return;
        }
        probe(param.getClassLoader(),
              "com.xiaomi.miplay.mylibrary.utils.SmartHubUIUtil", "enterSmartHubUI");
        probe(param.getClassLoader(),
              "com.xiaomi.miplay.mylibrary.utils.SmartHubUIUtil", "quitSmartHubUI");
        probe(param.getClassLoader(),
              "com.xiaomi.miplay.audioclient.sdk.MiPlayClient", "enterSmartHubUI");
        probe(param.getClassLoader(),
              "com.xiaomi.miplay.audioclient.sdk.MiPlayClient", "quitSmartHubUI");
    }

    private void report(String message) {
        log(Log.INFO, "KeiMiProbe", message);
    }

    private void probe(ClassLoader loader, String className, String name) {
        try {
            Class<?> type = Class.forName(className, false, loader);
            int installed = 0;
            for (Method method : type.getDeclaredMethods()) {
                if (!name.equals(method.getName()) || method.getParameterCount() != 1
                    || method.getParameterTypes()[0] != int.class) {
                    continue;
                }
                hook(method).intercept(chain -> {
                    report("KeiMiProbe: call " + className + "." + name);
                    return chain.proceed();
                });
                installed++;
            }
            report("KeiMiProbe: " + className + "." + name + " hooks=" + installed);
        } catch (ClassNotFoundException e) {
            report("KeiMiProbe: not loaded " + className);
        } catch (Throwable e) {
            report("KeiMiProbe: install error " + className + "." + name
                + " type=" + e.getClass().getName());
        }
    }
}
