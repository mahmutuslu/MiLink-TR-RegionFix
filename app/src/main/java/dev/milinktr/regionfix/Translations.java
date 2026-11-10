package dev.milinktr.regionfix;

import android.text.Spanned;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Exact, UI-safe translations for text observed in ConnectivityActivity.
 * We do not replace Spanned text because it may contain clickable hyperlinks.
 */
final class Translations {
    private static final Map<String, String> LABELS;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("Interconnectivity", "Cihazlar arası bağlantı");
        map.put("Cast", "Ekran yansıtma");
        map.put("Home screen+", "Ana ekran+");
        map.put("Apps", "Uygulamalar");
        map.put("Notifications", "Bildirimler");
        map.put("Call sync", "Arama senkronizasyonu");
        map.put("Shared connectivity", "Paylaşılan bağlantı");
        map.put("From this account", "Bu hesaptan");
        map.put("On", "Açık");
        map.put("Off", "Kapalı");
        map.put("Connect", "Bağlan");
        map.put("Disconnect", "Bağlantıyı kes");
        map.put("Turn on", "Aç");
        map.put("Turn off", "Kapat");
        map.put("Control this device from your phone, tablet, computer, or vehicle",
                "Bu cihazı telefonunuzdan, tabletinizden, bilgisayarınızdan veya aracınızdan kontrol edin");
        map.put("Continue tasks in the same app on another device",
                "Görevlere aynı uygulamada başka bir cihazdan devam edin");
        map.put("Share notifications with other devices",
                "Bildirimleri diğer cihazlarla paylaş");
        map.put("Sync calls across devices",
                "Aramaları cihazlar arasında eşitle");
        // Brand names must not be altered.
        LABELS = Collections.unmodifiableMap(map);
    }

    private Translations() {}

    static CharSequence translate(CharSequence text) {
        if (text == null || text instanceof Spanned) return text;
        String value = text.toString();
        String translated = LABELS.get(value.trim());
        return translated == null ? text : translated;
    }
}
