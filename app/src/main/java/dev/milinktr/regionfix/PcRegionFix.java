package dev.milinktr.regionfix;

import android.util.Log;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.HashSet;
import io.github.libxposed.api.XposedModule;

/**
 * Local cross-region interoperability for already-trusted devices, NOT a Xiaomi account bypass.
 * Intercepts only PropBuildRegion for devices already in MiLink's trusted device registry.
 * The remote server still decides account/device authorization.
 *
 * Based on a public analysis of NetworkingManager in Xiaomi Interconnection,
 * with the exact method signatures independently confirmed in the user's APK.
 */
final class PcRegionFix {
    private static final String TAG = "MiLinkTR";
    private final XposedModule module;
    private final ClassLoader loader;
    private final ThreadLocal<Boolean> nested = new ThreadLocal<>();
    private final Set<String> noted = new HashSet<>();

    PcRegionFix(XposedModule module, ClassLoader loader) {
        this.module = module;
        this.loader = loader;
    }

    void install() {
        try {
            Class<?> cls = Class.forName(
                    "com.xiaomi.continuity.networking.NetworkingManager", false, loader);
            Method method = cls.getDeclaredMethod(
                    "getStringProperty", String.class, int.class);
            final int regionProperty = resolveRegionProperty();
            if (regionProperty < 0) {
                Log.w(TAG, "Local region compatibility not installed: property enum unknown");
                return;
            }
            module.hook(method).setId("milink-trusted-device-region-alpha3").intercept(chain -> {
                Object actual = chain.proceed();
                if (Boolean.TRUE.equals(nested.get())) return actual;
                try {
                    Object idArg = chain.getArg(0);
                    Object propertyArg = chain.getArg(1);
                    if (!(idArg instanceof String) || !(propertyArg instanceof Integer)
                            || ((Integer) propertyArg).intValue() != regionProperty)
                        return actual;
                    String deviceId = (String) idArg;
                    Object manager = chain.getThisObject();
                    if (deviceId.isEmpty() || manager == null) return actual;
                    if (!isTrustedDevice(manager, deviceId)) return actual;

                    String ownId = localId(manager);
                    if (ownId == null || deviceId.equals(ownId)) return actual;

                    nested.set(true);
                    String localRegion;
                    try {
                        Object res = method.invoke(manager, ownId, regionProperty);
                        localRegion = (res instanceof String) ? (String) res : null;
                    } finally {
                        nested.remove();
                    }
                    if (localRegion == null || localRegion.isEmpty()) return actual;
                    if (localRegion.equals(actual)) return actual;

                    synchronized (noted) {
                        if (noted.add(deviceId)) {
                            Log.i(TAG, "Local region compatibility used for a trusted device");
                        }
                    }
                    return localRegion;
                } catch (Throwable exception) {
                    Log.w(TAG, "Local region compatibility skipped: " + exception.getClass().getSimpleName());
                    return actual;
                }
            });
            Log.i(TAG, "Trusted-device local region compatibility hook active");
        } catch (Throwable t) {
            Log.w(TAG, "Trusted-device local region compatibility unavailable", t);
        }
    }

    private int resolveRegionProperty() {
        try {
            Class<?> type = Class.forName(
                    "com.xiaomi.continuity.networking.PropertyType", false, loader);
            if (!type.isEnum()) return -1;
            Object[] constants = type.getEnumConstants();
            for (Object c : constants) {
                if ("PropBuildRegion".equals(((Enum<?>)c).name()))
                    return ((Enum<?>)c).ordinal();
            }
        } catch (Throwable ignored) {}
        return -1; // Fail closed. No guessed ordinal.
    }

    /**
     * Do not invent remote devices or allow unknown device IDs.
     * An entry must exist in the trusted-device registry and its reported
     * device ID must exactly match the requested ID.
     */
    private boolean isTrustedDevice(Object manager, String remoteId) {
        try {
            Object trusted = manager.getClass()
                    .getMethod("getTrustedDeviceInfo", String.class)
                    .invoke(manager, remoteId);
            if (trusted == null) return false;
            Object returnedId = trusted.getClass().getMethod("getDeviceId")
                    .invoke(trusted);
            return remoteId.equals(returnedId);
        } catch (Throwable unavailable) {
            return false;
        }
    }

    private String localId(Object manager) {
        try {
            Object local = manager.getClass().getMethod("getLocalDeviceInfo")
                    .invoke(manager);
            if (local == null) return null;
            Object id = local.getClass().getMethod("getDeviceId").invoke(local);
            return id instanceof String ? (String) id : null;
        } catch (Throwable unavailable) {
            return null;
        }
    }
}
