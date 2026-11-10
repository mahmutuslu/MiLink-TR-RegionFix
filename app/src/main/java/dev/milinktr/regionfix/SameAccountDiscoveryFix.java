package dev.milinktr.regionfix;

import android.accounts.Account;
import android.util.Log;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.libxposed.api.XposedModule;

/**
 * Prevent the hard-coded CN-account-region check from blocking local
 * notification device discovery on the user's CN ROM.
 *
 * Security properties:
 *  - Runs only within NotificationTransDeviceManager.startDiscovery().
 *  - Requires an existing Android Xiaomi Account on the account manager.
 *  - Does not change login/account identity, same-account matching, trust,
 *    access tokens, Xiaomi cloud region, or any remote server request.
 *  - Returns the real account region everywhere else.
 *
 * Signatures verified in the user's extracted MiLink Android DEX.
 */
final class SameAccountDiscoveryFix {
    private static final String TAG = "MiLinkTR";
    private static final String BASE = "com.xiaomi.dist.notification.";
    private final XposedModule module;
    private final ClassLoader loader;
    private final ThreadLocal<Boolean> inDiscovery = new ThreadLocal<>();
    private final AtomicBoolean noteOnce = new AtomicBoolean();

    SameAccountDiscoveryFix(XposedModule module, ClassLoader loader) {
        this.module = module;
        this.loader = loader;
    }

    void install() {
        try {
            Class<?> discovery = Class.forName(
                    BASE + "discover.NotificationTransDeviceManager", false, loader);
            Class<?> accounts = Class.forName(
                    BASE + "common.cloud.AccountManager", false, loader);

            Method start = null;
            for (Method candidate : discovery.getDeclaredMethods()) {
                if ("startDiscovery".equals(candidate.getName())
                        && candidate.getParameterCount() == 1
                        && candidate.getReturnType() == void.class) {
                    if (start != null) {
                        Log.w(TAG, "Discovery region hook skipped: ambiguous entry point");
                        return;
                    }
                    start = candidate;
                }
            }
            if (start == null) {
                Log.w(TAG, "Discovery region hook skipped: startDiscovery not found");
                return;
            }
            Method getRegion = accounts.getDeclaredMethod("getRegion");
            Method getXiaomiAccount = accounts.getDeclaredMethod("getXiaomiAccount");
            if (getRegion.getReturnType() != String.class
                    || !Account.class.isAssignableFrom(getXiaomiAccount.getReturnType())) {
                Log.w(TAG, "Discovery region hook skipped: unexpected account API");
                return;
            }
            start.setAccessible(true);
            getRegion.setAccessible(true);
            getXiaomiAccount.setAccessible(true);

            // This flag is thread-local and is always removed, including on exceptions.
            module.hook(start).setId("milink-discovery-start-alpha6").intercept(chain -> {
                Boolean previous = inDiscovery.get();
                inDiscovery.set(Boolean.TRUE);
                try {
                    return chain.proceed();
                } finally {
                    if (previous == null) inDiscovery.remove();
                    else inDiscovery.set(previous);
                }
            });

            module.hook(getRegion).setId("milink-discovery-region-alpha6").intercept(chain -> {
                Object original = chain.proceed();

                // Normal account/region behavior is untouched outside the
                // narrow synchronous device-discovery entry point.
                if (!Boolean.TRUE.equals(inDiscovery.get())
                        || !(original instanceof String)) return original;

                String actualRegion = ((String) original).trim();
                if (actualRegion.isEmpty() || "cn".equalsIgnoreCase(actualRegion))
                    return original;

                Object manager = chain.getThisObject();
                if (manager == null) return original;
                try {
                    // If no Xiaomi account is actually signed in, do not allow
                    // discovery by pretending an account exists.
                    Object signedIn = getXiaomiAccount.invoke(manager);
                    if (!(signedIn instanceof Account)) return original;
                } catch (Throwable unavailable) {
                    return original; // Fail closed.
                }

                if (noteOnce.compareAndSet(false, true)) {
                    Log.i(TAG, "CN-only check relaxed for discovery with a signed-in Xiaomi account;"
                            + " same-account verification stays unchanged");
                }
                return "cn";
            });
            Log.i(TAG, "Discovery-only CN account region compatibility installed");
        } catch (Throwable unavailable) {
            Log.w(TAG, "Discovery-only CN account region hook unavailable: "
                    + unavailable.getClass().getSimpleName());
        }
    }
}
