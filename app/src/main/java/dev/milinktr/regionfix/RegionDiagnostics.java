package dev.milinktr.regionfix;

import android.os.Build;
import android.util.Log;
import java.util.Locale;

/**
 * Read-only diagnostic stub. No account identifiers, credentials, or tokens
 * are collected or logged. No server / account checks are modified.
 */
final class RegionDiagnostics {
    private static final String TAG = "MiLinkTR";

    private RegionDiagnostics() {}

    static void report() {
        Locale locale = Locale.getDefault();
        Log.i(TAG, "Region diagnostics (non-invasive): locale="
                + locale.toLanguageTag()
                + " sdk=" + Build.VERSION.SDK_INT
                + " device=" + Build.DEVICE
                + "; region bypass is NOT enabled");
    }
}
