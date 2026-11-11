# MiLink TR + Region Fix — v1.0.0 (LSPosed API 102)

Xiaomi HyperOS 4 MiLink (Interconnectivity) ve Xiaomi Share için Türkçe arayüz ve dar kapsamlı yerel bölge uyumluluğu modülü.

**Tek APK, iki LSPosed kapsamı:** `com.milink.service` ve `com.miui.mishare.connectivity`.

## İçerik

- MiLink ana ekranı ve alt menülerinde doğrulanmış Türkçe metinler, etkin Türkçe overlay'den dinamik kaynak eşleştirme
- Xiaomi Share ekranları için Türkçe metinler
- Sadece önceden güvenilir olduğu doğrulanan uzak cihazların yerel MiLink bölge karşılaştırması
- Gerçek Xiaomi hesabı açıkken, yalnızca bildirim keşfi çağrısında sınırlı CN bölgesi uyumu
- Hesap kimliği, tokenlar, aynı-hesap doğrulaması ve sunucu yetkilendirmesi **değiştirilmez**

**Bu sürümde Galeri, Medya Düzenleyici, Galeri eklentisi, Hava Durumu ve Flutter AOT yamaları yoktur.** Uygulama verilerini temizlemez, ROM sistem dosyalarını değiştirmez.

## İndir

[GitHub Releases](https://github.com/mahmutuslu/MiLink-TR-RegionFix/releases/latest) sayfasından `MiLink-TR-RegionFix-v1.0.0.apk` dosyasını yükle.

**İmza bilgisi:** GitHub Actions, bu sürümün APK'sını varsayılan Android debug anahtarıyla imzalar. Farklı CI çalışmaları aynı imzayı garantilemez; daha sonraki sürümü yüklerken eski modülü kaldırmak gerekebilir. İleride kalıcı imzalı güncellemeler için özel yayın anahtarı gereklidir.

## Kurulum

1. APK'yi Xiaomi 15 Ultra / HyperOS 4 cihazına yükle.
2. LSPosed (API 102) içinde modülü aç.
3. Kapsamdan **yalnızca** `com.milink.service` ve `com.miui.mishare.connectivity` paketlerini etkinleştir.
4. Telefonu yeniden başlat veya her iki işlemi yeniden başlat.
5. MiLink ana/alt sayfaları, Xiaomi Share, bağlı cihaz keşfi ve bildirim bölümünü denetle.

Günlük: `adb logcat -d -v time -s MiLinkTR:V HyperOSTR:V`.

## Doğrulama sınırları

Önceki saha denemelerinde MiLink Türkçe ekranları ve dar kapsamlı bağlanırlık hook'larının yüklendiği görüldü. **Tam bölge kilidinin kalktığı, tüm Global cihazların bulunduğu, sunucu hesabının kabul ettiği veya her Flutter ekranının çevrildiği iddia edilmez.** [Bölge uyumluluk açıklaması](docs/REGION_SCOPE.md) ve [test planı](docs/TESTING.md).

Sorun durumunda modülü LSPosed'den kapat, işlemleri yeniden başlat. Xiaomi hesap/veri sıfırlaması gerekmez.

## Kaynak ve yapı

Java 17 / Gradle 8.9 / Android compileSdk 35. Tek APK `dev.hyperostr.unified` paket kimliğini korur; önceki unified alfa paketleriyle aynı uygulamadır (imza uyumuna tabidir). Kaynaklar yalnızca modüle aittir; Xiaomi'ye ait sistem APK veya `.so` dosyaları dağıtılmaz.
