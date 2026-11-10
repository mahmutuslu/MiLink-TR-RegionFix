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


## 2026-11-10 — Türkiye/Global Xiaomi hesabıyla gerçek cihaz logları

Kullanıcı Xiaomi 15 Ultra, CN HyperOS 4 üzerinde **Türkiye/Global Xiaomi hesabı** ile giriş yapmış. v0.6 alpha6 modülü iki MiLink işleminde başarılı biçimde yükleniyor:

- \`Discovery-only CN account region compatibility installed\` — ilgili hook gerçekten kuruldu.
- \`Trusted-device local region compatibility hook active\` — güvenilir cihaz özelliği hook'u kuruldu.
- \`CN-only check relaxed for discovery\` — **görülmedi**, bu nedenle hesap bölgesinin çalışma anında değiştirildiği kanıtlanamaz.
- Yerel Lyra keşif logu: \`startDiscover() called IsConnectionServiceEnabled:true\`, \`startDiscovery tansBitSet:true\`, \`start discovery result: 0\`, \`is_same_account=1\`, \`env_satisfied=1\`. Cihaz keşfi başladı ve aynı hesap filtresi etkin.
- Bu loglar **Global cihaz bulundu**, **sunucunun CN kuralı kalktı** veya **eşleşme başarılı** anlamına gelmez. Henüz karşı cihaz keşfi kaydı yok.

### Gerçek APK DEX akışı

\`MiLink_current_base.apk\` dosyasının \`classes3.dex\` tanımlarında doğrulandı:

\`NotificationTransDeviceManager.startDiscovery(IDeviceListener):void\`

1. \`isConnectionServiceEnable()\` kontrol eder ve \`startDiscover() called IsConnectionServiceEnabled:\` loglar.
2. \`AccountManager.getRegion()\` çağırır.
3. \`equalsIgnoreCase("cn")\` ile test eder; başarısızsa \`startDiscover() called region is not cn, return\` diye loglayıp çıkar.
4. Başarılıysa \`LyraDiscover.startDiscovery()\` ve \`CacheDiscover.startDiscover()\` çağırır.

\`AccountManager.getRegion():String\` sınıfının DEX akışı:

1. \`getXiaomiAccount()\` çağırır.
2. Hesap varsa Xiaomi Passport \`getUserData(account, "acc_user_region")\` verisini döndürür.

Dolayısıyla hesap ilk kez Türkiye'de oluşturulmuş olsa bile, **ROM'daki yerel \`acc_user_region\` değerinin ne olduğu ölçülmeden** CN kapısının gerçekten devre dışı bırakıldığı söylenemez. Lyra keşfinin başlayabilmiş olması \`startDiscovery\` metodunun CN dalından geçmiş olduğunu gösterir; bu, gerçek alan değeri CN olduğu için de olabilir, hook sonucu CN döndürüldüğü için de olabilir. Modül sadece hesap bölgesi CN'den farklıysa ve Xiaomi oturumu bulunuyorsa bir kez logladığı için, **log satırının yokluğu tek başına hata kanıtı değildir**.

### İncelenecek bir sonraki dar kapı

İlk kez görülen Global cihazlar, \`getTrustedDeviceInfo(deviceId)\` kaydı olmadan önce yerel \`PropBuildRegion\` karşılaştırmasına takılabilir. v0.6 güvenlik gerekçesiyle yalnızca **önceden güvenilir cihazlar** için bu değeri uyarlıyor. Sonraki aşamada \`LyraDiscover.addOrUpdateDevice\` çağrısında yerel/uzak bölge ve native \`is_same_account=1\` filtresinin gerçekten hangi sırada çalıştığı **yalnızca tanı loglarıyla** doğrulanmalı; bilinmeyen veya farklı hesap cihazlarına genel geçiş eklenmemeli.

Not: \`medium_types=[["ble apple"]]\` Lyra taşıyıcı adıdır; tek başına Apple cihazı tarandığını veya Xiaomi taramasının dışlandığını kanıtlamaz.
