package dev.milinktr.regionfix;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Exact-match overrides plus runtime dictionary from the user's active overlays. */
final class Translations {
    private static final Map<String,String> MANUAL;
    private static volatile Map<String,String> runtime = Collections.emptyMap();

    static {
        HashMap<String,String> m = new HashMap<>();
        m.put("Interconnectivity", "Cihazlar arası bağlantı");
        m.put("Cast", "Ekran yansıtma");
        m.put("Xiaomi Share", "Xiaomi Share");
        m.put("Home screen+", "Ana ekran+");
        m.put("Apps", "Uygulamalar");
        m.put("Notifications", "Bildirimler");
        m.put("Call sync", "Arama senkronizasyonu");
        m.put("Shared connectivity", "Paylaşılan bağlantı");
        m.put("From this account", "Bu hesaptan");
        m.put("On", "Açık");
        m.put("Off", "Kapalı");
        m.put("Clipboard", "Pano");
        m.put("Cross-device camera", "Cihazlar arası kamera");
        m.put("Cross-device files", "Cihazlar arası dosyalar");
        m.put("Privacy", "Gizlilik");
        m.put("Connected devices", "Bağlı cihazlar");
        m.put("Turn on", "Aç");
        m.put("Turn off", "Kapat");
        m.put("Connect", "Bağlan");
        m.put("Disconnect", "Bağlantıyı kes");
        m.put("Allow Home screen+ on these devices",
                "Bu cihazlarda Ana ekran+ kullanımına izin ver");
        m.put("View and control this device and use its apps from nearby phones, tablets, computers, or vehicles signed in with the same account",
                "Aynı hesapla oturum açılmış yakındaki telefon, tablet, bilgisayar veya araçlardan bu cihazı görüntüleyip kontrol edin ve uygulamalarını kullanın");
        m.put("Use your phone's network on tablets or computers",
                "Telefonunuzun internet bağlantısını tabletlerde veya bilgisayarlarda kullanın");
        m.put("Control this device from your phone, tablet, computer, or vehicle",
                "Bu cihazı telefonunuzdan, tabletinizden, bilgisayarınızdan veya aracınızdan kontrol edin");
        m.put("Continue tasks in the same app on another device",
                "Görevlere aynı uygulamada başka bir cihazdan devam edin");
        m.put("Share notifications with other devices",
                "Bildirimleri diğer cihazlarla paylaş");
        m.put("Sync calls across devices",
                "Aramaları cihazlar arasında eşitle");
        // The clickable "Learn more" span must remain clickable.
        MANUAL = Collections.unmodifiableMap(m);
    }

    private Translations() {}

    static void replaceRuntimeDictionary(Map<String,String> map) {
        runtime = Collections.unmodifiableMap(new HashMap<>(map));
    }

    static CharSequence translate(CharSequence original) {
        if (original == null) return original;
        if (original instanceof Spanned) return translateSpanned((Spanned) original);

        String text = original.toString();
        String replacement = MANUAL.get(text);
        if (replacement == null) replacement = runtime.get(text);
        if (replacement != null && !replacement.equals(text)) return replacement;

        String trimmed = text.trim();
        if (!trimmed.equals(text)) {
            replacement = MANUAL.get(trimmed);
            if (replacement == null) replacement = runtime.get(trimmed);
            if (replacement != null) return text.replace(trimmed, replacement);
        }
        return original;
    }

    /** Preserve spans, URLs and ClickableSpan objects on the main summary. */
    private static CharSequence translateSpanned(Spanned source) {
        String text = source.toString();
        if (!text.contains("Automatically discover nearby devices signed in")) return source;
        SpannableStringBuilder copy = new SpannableStringBuilder(source);
        replace(copy,
                "Automatically discover nearby devices signed in to your Xiaomi Account and connect them to each other to perform a variety of tasks across multiple devices.",
                "Xiaomi Hesabınızda oturum açmış yakındaki cihazları otomatik olarak keşfedin ve birden fazla cihazda görevler gerçekleştirmek için birbirine bağlayın.");
        replace(copy, "Turn on WLAN and Bluetooth before use.",
                "Kullanmadan önce WLAN ve Bluetooth'u açın.");
        replace(copy, "Learn more", "Daha fazla bilgi");
        return copy.toString().equals(text) ? source : copy;
    }

    private static void replace(SpannableStringBuilder b, String from, String to) {
        int at = b.toString().indexOf(from);
        if (at >= 0) b.replace(at, at + from.length(), to);
    }
}
