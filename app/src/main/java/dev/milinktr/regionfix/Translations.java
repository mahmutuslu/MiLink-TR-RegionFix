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
        // Verified from user screenshots: Home screen+, Cast, Notifications and Share subpages.
        m.put("Cast mobile device screen to nearby TVs and other external monitors",
                "Mobil cihazınızın ekranını yakındaki TV'lere ve diğer harici ekranlara yansıtın");
        m.put("View the list of available devices for casting",
                "Yansıtma için kullanılabilir cihazların listesini görüntüleyin");
        m.put("Hide private items", "Özel ögeleri gizle");
        m.put("Don't display floating notifications and incoming calls",
                "Kayan bildirimleri ve gelen aramaları gösterme");
        m.put("Help", "Yardım");
        m.put("Permissions details", "İzin ayrıntıları");
        m.put("Receive", "Al");
        m.put("Sync", "Eşitle");
        m.put("Notifications you get while this device is locked can be shown on phones, tablets, computers, TVs, or connected watches signed in to the same account",
                "Bu cihaz kilitliyken gelen bildirimler, aynı hesapla oturum açılmış telefon, tablet, bilgisayar, TV veya bağlı saatlerde gösterilebilir");
        m.put("Other notifications", "Diğer bildirimler");
        m.put("When this device is connected to the network, notifications from another locked Xiaomi phone signed in to the same account can be viewed on this device.",
                "Bu cihaz ağa bağlıyken, aynı hesapla oturum açılmış kilitli bir Xiaomi telefondan gelen bildirimleri bu cihazda görüntüleyebilirsiniz.");
        m.put("When devices signed in to the same account are nearby and have Bluetooth and WLAN turned on, notifications from other devices can be replied to directly on this device.",
                "Aynı hesapta oturum açılmış cihazlar yakındaysa ve Bluetooth ile WLAN açıksa, diğer cihazlardan gelen bildirimlere doğrudan bu cihazdan yanıt verebilirsiniz.");
        m.put("Available devices", "Kullanılabilir cihazlar");
        m.put("Refresh", "Yenile");
        m.put("Couldn't find available devices", "Kullanılabilir cihaz bulunamadı");
        m.put("Make sure both devices are signed in to the same account and are connected to the network, or have WLAN turned on",
                "Her iki cihazın da aynı hesapta oturum açtığından ve ağa bağlı olduğundan veya WLAN'ın açık olduğundan emin olun");
        m.put("Turn on Xiaomi Share to receive files",
                "Dosya almak için Xiaomi Share'i açın");
        m.put("You can turn on Xiaomi Share in Control center",
                "Xiaomi Share'i Kontrol Merkezi'nden açabilirsiniz");
        m.put("Can be found by nearby devices", "Yakındaki cihazlar tarafından bulunabilir");
        m.put("Before setting your receiving status to \"From this account\", make sure the other device's system version is updated to Xiaomi HyperOS or later, and Xiaomi Share is updated to version 3.0.0 or later.",
                "Alma durumunu \"Bu hesaptan\" yapmadan önce diğer cihazın Xiaomi HyperOS veya üzeri bir sürüme, Xiaomi Share'in de 3.0.0 veya üzeri sürüme güncellendiğinden emin olun.");
        m.put("Touch to share", "Dokundurarak paylaş");
        m.put("Touch the backs of two devices together to share items",
                "Ögeleri paylaşmak için iki cihazın arka yüzlerini birbirine dokundurun");
        m.put("Device name", "Cihaz adı");
        m.put("Supported devices", "Desteklenen cihazlar");
        // More common secondary-page labels.
        m.put("Permissions", "İzinler");
        m.put("Privacy Policy", "Gizlilik Politikası");
        m.put("About", "Hakkında");
        m.put("Cancel", "İptal");
        m.put("Done", "Bitti");
        m.put("Search again", "Tekrar ara");
        m.put("No devices found", "Cihaz bulunamadı");
        m.put("Select devices", "Cihazları seçin");
        m.put("Settings", "Ayarlar");
        m.put("Learn more", "Daha fazla bilgi");
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

        String raw = original.toString();
        String translated = lookup(raw);
        if (translated != null && !translated.equals(raw)) return translated;

        // UI wraps sometimes insert newlines or NBSPs without changing the content.
        String normalized = raw.replace('\u00a0', ' ').replaceAll("\\s+", " ").trim();
        if (!normalized.equals(raw)) {
            translated = lookup(normalized);
            if (translated != null && !translated.equals(raw)) return translated;
        }

        // The "Receive" explanation contains two bullet paragraphs in a single
        // TextView. Replace only the two explicitly approved full sentences,
        // preserving the original bullet markers and paragraph boundaries.
        String result = raw;
        String[] sentences = {
            "When this device is connected to the network, notifications from another locked Xiaomi phone signed in to the same account can be viewed on this device.",
            "When devices signed in to the same account are nearby and have Bluetooth and WLAN turned on, notifications from other devices can be replied to directly on this device."
        };
        for (String sentence : sentences) {
            String value = MANUAL.get(sentence);
            if (value != null && result.contains(sentence)) result = result.replace(sentence, value);
        }
        return result.equals(raw) ? original : result;
    }

    private static String lookup(String value) {
        String translated = MANUAL.get(value);
        if (translated == null) translated = runtime.get(value);
        return translated;
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
