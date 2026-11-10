package dev.milinktr.regionfix;

import android.content.Context;
import android.content.res.Configuration;
import android.os.LocaleList;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Build a translation dictionary from the *already installed* MiLink resources.
 *
 * Some MiLink pages use an English Resources context despite system language tr.
 * We independently resolve matching resource IDs under EN and TR configurations.
 * TR includes the active KernelSU/LightOS overlay, when available.
 *
 * We do not modify ROMs, resource tables, account settings or locale globally.
 */
final class TranslationCatalog {
    private static final String TAG = "MiLinkTR";
    private static final AtomicBoolean started = new AtomicBoolean();
    private static final String[] RESOURCE_CLASSES = {
            "com.milink.service.R$string",
            "com.miui.circulate.world.R$string",
            "com.miui.circulateplus.world.R$string",
            "com.miui.circulate.device.service.R$string",
            "com.miui.circulate.log.R$string",
            "com.milink.R$string",
            "com.xiaomi.dist.file.service.R$string",
            "com.xiaomi.dist.universalclipboardservice.R$string",
            "com.xiaomi.mirror.opensdk.R$string",
            "com.miui.circulate.nfc.relay.R$string"
    };

    private TranslationCatalog() {}

    static void ensureLoaded(Context context, ClassLoader loader, Runnable onFinished) {
        if (!started.compareAndSet(false, true)) return;
        final Context appContext = context.getApplicationContext();
        new Thread(() -> {
            int scanned = 0, classes = 0, overlaps = 0;
            try {
                Configuration english = new Configuration(appContext.getResources().getConfiguration());
                english.setLocales(LocaleList.forLanguageTags("en-US"));
                Configuration turkish = new Configuration(appContext.getResources().getConfiguration());
                turkish.setLocales(LocaleList.forLanguageTags("tr-TR"));
                Context enContext = appContext.createConfigurationContext(english);
                Context trContext = appContext.createConfigurationContext(turkish);

                Map<String,String> pairs = new HashMap<>();
                Set<String> ambiguous = new HashSet<>();
                Set<Integer> seenResourceIds = new HashSet<>();
                for (String className : RESOURCE_CLASSES) {
                    Class<?> owner;
                    try {
                        owner = Class.forName(className, false, loader);
                    } catch (Throwable notFound) {
                        continue;
                    }
                    classes++;
                    for (Field field : owner.getFields()) {
                        if (!Modifier.isStatic(field.getModifiers()) || field.getType() != int.class)
                            continue;
                        try {
                            int id = field.getInt(null);
                            if (!seenResourceIds.add(id)) continue;
                            if (!"string".equals(enContext.getResources().getResourceTypeName(id)))
                                continue;
                            String en = enContext.getString(id);
                            String tr = trContext.getString(id);
                            scanned++;
                            if (!usable(en, tr)) continue;
                            String existing = pairs.get(en);
                            if (existing != null && !existing.equals(tr)) {
                                ambiguous.add(en);
                                overlaps++;
                            } else if (existing == null) {
                                pairs.put(en, tr);
                            }
                        } catch (Throwable unavailableResource) {
                            // Each individual resource may be removed in another ROM.
                        }
                    }
                }
                for (String s : ambiguous) pairs.remove(s);
                Translations.replaceRuntimeDictionary(pairs);
                Log.i(TAG, "Translation catalog: classes=" + classes
                        + " scanned=" + scanned + " pairs=" + pairs.size()
                        + " ambiguous=" + overlaps);
            } catch (Throwable t) {
                Log.e(TAG, "Dynamic catalog failed; manual translations remain active", t);
            } finally {
                if (onFinished != null) {
                    try { onFinished.run(); } catch (Throwable ignored) {}
                }
            }
        }, "MiLinkTR-resource-catalog").start();
    }

    private static boolean usable(String en, String tr) {
        if (en == null || tr == null || en.equals(tr) ||
                en.isEmpty() || tr.isEmpty() || en.length() > 450 || tr.length() > 600)
            return false;
        if (en.indexOf('%') >= 0 || tr.indexOf('%') >= 0 ||
                en.indexOf('<') >= 0 || tr.indexOf('<') >= 0 ||
                en.indexOf('\\') >= 0 || tr.indexOf('\\') >= 0)
            return false;
        // Keep hard-coded technical strings / URLs / non-UI identifiers unchanged.
        if (en.startsWith("http:") || en.startsWith("https:") || en.contains("://"))
            return false;
        if (en.indexOf(' ') < 0 && en.indexOf('_') >= 0) return false;
        return true;
    }
}
