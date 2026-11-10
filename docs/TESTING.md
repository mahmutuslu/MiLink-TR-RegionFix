# MiLink TR + Region Fix alpha2 — test kontrolü

Bu bir **deneysel** sürümdür. Alpha1'de ana sayfanın çevirisi gerçek cihazda görülmüştür; alpha2 henüz test edilmemiştir.

1. LSPosed kapsamı: \`com.milink.service\`, modern API 102.
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
- \`Local PC-only region compatibility hook active\` (enum biliniyorsa)

Yanlış çeviri veya bağlantı sorunu yaşanırsa LSPosed'de modülü kapatıp \`com.milink.service\` uygulamasını yeniden başlat. Uygulama verilerini silme.

**Önemli:** Bölge uyum hook'u yalnızca yerel cihaz-bölge karşılaştırmasını etkiler; Çin Xiaomi hesabının sunucu doğrulaması için çözüm olarak düşünülmemelidir.
