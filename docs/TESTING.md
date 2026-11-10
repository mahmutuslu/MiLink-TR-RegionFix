# MiLink TR + Region Fix alpha3 — test kontrolü

Bu bir **deneysel** sürümdür. Alpha1'de ana sayfanın çevirisi gerçek cihazda görülmüştür; alpha3 henüz test edilmemiştir.

1. LSPosed kapsamı: `com.milink.service` ve `com.miui.mishare.connectivity`, modern API 102.
2. MiLink uygulama dilini Türkçe olarak ayarla:
   \`\`\`powershell
   & "C:\platform-tools\adb.exe" shell "cmd locale set-app-locales com.milink.service --user 0 --locales tr-TR"
   \`\`\`
3. MiLink işlemini yeniden başlat:
   \`\`\`powershell
   & "C:\platform-tools\adb.exe" shell "am force-stop com.milink.service"
   \`\`\`
4. Ana menü ve tüm alt menüleri incele; özellikle linklerin tıklanabilirliğini kontrol et.
5. \`& "C:\platform-tools\adb.exe" logcat -d -v time -s MiLinkTR:V\` ile sözlük sayısı ve yerel PC hook loglarını incele.

Beklenen günlükler:
- \`TextView#setText installed for all MiLink UI pages\`
- \`Translation catalog: classes=... scanned=... pairs=...\`
- \`Local trusted-device region compatibility hook active\` (enum biliniyorsa)

Yanlış çeviri veya bağlantı sorunu yaşanırsa LSPosed'de modülü kapatıp \`com.milink.service\` uygulamasını yeniden başlat. Uygulama verilerini silme.

**Önemli:** Bölge uyum hook'u yalnızca güvenilir uzak cihazların yerel cihaz-bölge karşılaştırmasını etkiler; Çin Xiaomi hesabının sunucu doğrulaması için çözüm olarak düşünülmemelidir.

Global telefon/tablet denemelerinde cihazın önce MiLink güvenilir cihaz listesinde bulunduğunu doğrulayın. Bu sürüm eşleşmemiş cihazın keşif veya hesap yetkilendirme engelini otomatik kaldırmaz.

## Xiaomi Share son ekran testi (alpha5)

1. LSPosed'de `com.miui.mishare.connectivity` için kapsamı **ayrıca** etkinleştirin.
2. MiLink ve Xiaomi Share uygulamalarını yeniden başlatın (veya telefonu yeniden başlatın).
3. Xiaomi Share ekranında `Dosya almak için Xiaomi Share'i açın`, `Yakındaki cihazlar tarafından bulunabilir`, `Dokundurarak paylaş`, `Cihaz adı`, `Desteklenen cihazlar`, `Gizlilik` yazılarını doğrulayın.
4. Log: `adb logcat -d -v time -s MiLinkTR:V` — yeni scope yüklendi mi kontrol edin.
5. Hâlâ İngilizceyse ekran açıkken `adb shell dumpsys activity activities` ile etkin Activity paketini doğrulayın; sistem verilerini veya Xiaomi Share uygulama verilerini silmeyin.
