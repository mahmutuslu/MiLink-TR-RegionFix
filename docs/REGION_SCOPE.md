# Bölge / Xiaomi hesabı — hangi kısıtı düzeltiyoruz?

Kullanıcı hedefi: **Yalnızca aynı Xiaomi hesabında oturum açmış cihazlar haberleşsin; hesap/cihaz ülkesinin Çin (CN) olması zorunlu olmasın.**

## 1. Güven modeli korunan bileşenler

- Aynı Xiaomi hesabında oturum açılması: **zorunlu, dokunulmuyor**.
- Cihazın güvenilir olarak kaydedilmesi ve eşleşmesi: **zorunlu, dokunulmuyor**.
- Xiaomi giriş tokenları / hesap kimliği / imza, kimlik doğrulama ve sunucu yetkileri: **değiştirilmiyor**.
- MiLink / Xiaomi Share menülerinin Türkçeleştirilmesi: **korunuyor**.

## 2. Yerel cihaz-bölge karşılaştırması (alpha3'ten beri)

\`NetworkingManager.getStringProperty(String, PropBuildRegion)\` yalnızca \`getTrustedDeviceInfo(deviceId)\` tarafından gerçekten döndürülmüş, kimliği eşleşen güvenilir cihazlar için yerel bölge değerine uyarlanır. Global telefon/tablet/PC cihaz tipi açısından ayrıcalık yoktur. Bilinmeyen, yeni keşfedilmiş veya eşleşmemiş cihazlar bu koldan otomatik geçirilmez.

## 3. Yerel CN hesap-bölgesi keşif engeli (alpha6 yeni)

**Cihazdaki MiLink APK'sında imzalar doğrulandı**:

- \`com.xiaomi.dist.notification.discover.NotificationTransDeviceManager.startDiscovery(IDeviceListener):void\`
- \`com.xiaomi.dist.notification.common.cloud.AccountManager.getRegion():String\`
- \`com.xiaomi.dist.notification.common.cloud.AccountManager.getXiaomiAccount():android.accounts.Account\`

Yeni hook yalnızca \`startDiscovery(...)\` çağrısı sırasında \`getRegion()\` sorgusu yapıldığında, gerçek Xiaomi hesabı varsa ve bölge değeri boş değilse onu \`cn\` olarak yorumlar. Bu değişiklik **yalnızca o eşzamanlı çağrı zincirinde** etkilidir; kullanıcı hesabının gerçek bölgesi hiçbir yere yazılmaz.

Dikkat: \`startDiscovery\` daha sonra farklı bir iş parçacığında \`getRegion\` çağırırsa bu yöntem etki etmez. Bu durumda körlemesine kapsam genişletmek yerine cihaz loglarıyla çağrı yolunu yeniden doğrulamak gerekir.

## 4. Dokunulmayan kısıtlar

- \`com.xiaomi.mirror\` içindeki bağımsız cihaz-keşif bölge süzgeci
- Xiaomi Share'in başka servislerde uyguladığı farklı ülke kontrolleri
- Xiaomi sunucusunun hesap/cihaz uygunluk veya Çin hesabı gereksinimi

Bu yüzden **tüm Çin bölge kilitleri açıldı** iddiası yoktur. Modül güvenli biçimde bilinen ve doğrulanmış iki yerel kontrolü hedefler.

Kaynak çalışması: https://github.com/Leekin2/XApps (XNotify discovery analizi) ve https://github.com/luobi125/Get-Out-Xiaomi-Interconnection-Regional-Lock (yerel PC/region analizi). Kod bağımsız ve dar kapsamlı uygulanmıştır.
