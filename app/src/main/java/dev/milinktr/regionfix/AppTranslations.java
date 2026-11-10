package dev.milinktr.regionfix;

import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.InflaterInputStream;

/**
 * APK English-source -> Turkish dictionary. Unlike locale switching this
 * does not request tr-TR, and therefore does not trigger the CN ROM's
 * Arabic fallback. The dictionary is scoped per package, not global.
 * Only exact English text is replaced; no user-created text is modified.
 */
final class AppTranslations {
    private static final String TAG = "HyperOSTR";
    private static final ConcurrentHashMap<String, Map<String, String>> CACHE = new ConcurrentHashMap<>();
    private AppTranslations() {}

    static boolean isApp(String pkg) {
        return "com.miui.gallery".equals(pkg)
                || "com.miui.mediaeditor".equals(pkg)
                || "com.miui.weather2".equals(pkg)
                || "com.hyper.gallery.plugin".equals(pkg);
    }

    static CharSequence translate(String pkg, CharSequence source) {
        if (source == null || !isApp(pkg) || source instanceof Spanned)
            return source;
        Map<String, String> dict = CACHE.computeIfAbsent(pkg, AppTranslations::load);
        if (dict.isEmpty()) return source;
        String key = source.toString();
        String value = dict.get(key);
        if (value == null) {
            String compact = key.replace('\u00a0', ' ').trim();
            if (!compact.equals(key)) value = dict.get(compact);
        }
        return value != null && !value.equals(key) ? value : source;
    }

    private static Map<String, String> load(String pkg) {
        final String b64 = AppTranslationData.forPackage(pkg);
        if (b64 == null) return Collections.emptyMap();
        HashMap<String, String> dict = new HashMap<>();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(
                new InflaterInputStream(new ByteArrayInputStream(
                        Base64.decode(b64, Base64.DEFAULT))), StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) {
                int pos = line.indexOf('\t');
                if (pos <= 0 || pos == line.length()-1) continue;
                dict.putIfAbsent(line.substring(0, pos), line.substring(pos+1));
            }
            Log.i(TAG, "English->Turkish dictionary ready for " + pkg
                    + ": " + dict.size() + " entries");
            return Collections.unmodifiableMap(dict);
        } catch (Throwable failure) {
            Log.e(TAG, "Dictionary invalid for " + pkg, failure);
            return Collections.emptyMap();
        }
    }
}
