package dev.milinktr.regionfix;

import android.util.Log;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.HashSet;
import io.github.libxposed.api.XposedModule;

/**
 * Local cross-region PC interoperability, NOT a China-account bypass.
 * Intercepts only PropBuildRegion of a positively identified PC/OTHER_PC.
 * The remote server still decides account/device authorization.
 *
 * Based on a public analysis of NetworkingManager in Xiaomi Interconnection,
 * with the exact method signatures independently confirmed in the user's APK.
 */
final class PcRegionFix {
    private static final String TAG = "MiLinkTR";
    private static final int PC = 4, OTHER_PC = 21;
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
                Log.w(TAG, "PC local region fix not installed: property enum unknown");
                return;
            }
            module.hook(method).setId("milink-pc-region-alpha2").intercept(chain -> {
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
                    if (!isComputer(manager, deviceId)) return actual;

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
                            Log.i(TAG, "Local PC region compatibility enabled for a trusted PC");
                        }
                    }
                    return localRegion;
                } catch (Throwable exception) {
                    Log.w(TAG, "PC region compatibility skipped: " + exception.getClass().getSimpleName());
                    return actual;
                }
            });
            Log.i(TAG, "Local PC-only region compatibility hook active");
        } catch (Throwable t) {
            Log.w(TAG, "Local PC region compatibility unavailable", t);
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

    private boolean isComputer(Object manager, String remoteId) {
        try {
            Object trusted = manager.getClass()
                    .getMethod("getTrustedDeviceInfo", String.class)
                    .invoke(manager, remoteId);
            if (trusted == null) return false;
            Object type = trusted.getClass().getMethod("getDeviceType").invoke(trusted);
            if (!(type instanceof Number)) return false;
            int number = ((Number) type).intValue();
            return number == PC || number == OTHER_PC;
        } catch (Throwable unavailable) {
            return false; // Do not bypass unknown device types.
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
