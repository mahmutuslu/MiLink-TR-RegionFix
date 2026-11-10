# MiLink TR + Region Fix (LSPosed API 102)

Xiaomi HyperOS 4 / MiLink (`com.milink.service`) için açık kaynak, deneysel LSPosed modülü.

> **Durum: İlk kaynak kodu / test sürümü.** Henüz Xiaomi 15 Ultra üzerinde doğrulanmış bir APK sürümü yok. Hesap-bölge kısıtlaması kaldırıldı iddiası yoktur.

## Amaç

- **Türkçe arayüz:** MiLink'in Interconnectivity (Cihazlar arası bağlantı) ekranında görülen İngilizce metinleri çalışma anında, yalnızca hedef Activity'de Türkçeye çevirmeyi denemek.
- **Bölge / hesap incelemesi:** Xiaomi hesabının ülke bilgisi, telefonun bölgesi ve uzak sunucu yetkilendirmesinin birbirinden farklı kontrol noktaları olduğunu ayırt etmek. Bu ilk sürüm **herhangi bir bölge kapısını atlatmaz**.
- **Tek LSPosed APK'sı:** İleride doğrulanmış, yerel uyumluluk düzeltmelerini aynı modüle eklemek.

## Hedef cihaz

- Xiaomi 15 Ultra (`xuanyuan`), HyperOS `OS4.0.0.18.XOACNXM`, Android 17 / API 37
- KernelSU Next + modern LSPosed (API 102)
- Kapsam: **yalnızca** `com.milink.service`

## Neden kaynak-hook yaklaşımı?

Kullanıcı testlerinde `cmd overlay lookup` doğru Türkçe metni döndürdüğü hâlde `ConnectivityActivity` İngilizce gösteriyor. `tr-TR` uygulama dili de sonucu değiştirmedi. Almanca kaynakları seçildiğinde MiLink Almancaya geçti. Bu repo, önce uygulamanın ekranındaki `TextView` yazılarını hedefleyerek sorunu teşhis eder.

## İlk sürümdeki davranış

`TextView.setText(CharSequence, BufferType)` çağrılarını MiLink sürecinde izler; **yalnızca `ConnectivityActivity` bağlamındaki**, önceden tanımlanmış birebir metinleri Türkçe karşılıklarıyla değiştirir. Bağlantı, Xiaomi hesabı, bildirim, Wi-Fi veya Bluetooth davranışını değiştirmez. Tıklanabilir biçimlendirmeyi korumak için zengin/metin-spannable içerikler değiştirilmez.

## APK derleme

GitHub'da **Actions → Build debug APK → Run workflow** yoluyla derleme başlatılabilir; başarılı derlemenin `MiLink-TR-RegionFix-debug-apk` artifact'ından APK indirilir. Alternatif olarak Java 17 ve Android SDK ile:

```bash
gradle :app:assembleDebug
```

Android Studio gerekli değildir. APK'nin yalnızca *derlenmesi* onun telefonda test edildiği anlamına gelmez.

## Kurulum ve test

1. Debug APK'yi yükleyin.
2. LSPosed API 102 içinde modülü etkinleştirin ve kapsamdan **`com.milink.service`** paketini seçin.
3. MiLink işlemini yeniden başlatın / telefonu yeniden başlatın.
4. Interconnectivity ekranındaki metinleri ve `adb logcat -s MiLinkTR:V` çıktısını kontrol edin.

Ayrıntılar: [TESTING.md](docs/TESTING.md) ve [REGION_SCOPE.md](docs/REGION_SCOPE.md).

## Sınırlamalar

Çeviri listesi ilk ekran metinleriyle sınırlıdır; değişen sürüm/metinlerde eşleşmeyebilir. Android'in kişisel hesap kimlik doğrulamasını veya Xiaomi sunucu yetkilerini değiştirmez. Kararlı sürüm yayımlanmadan önce cihaz testleri gereklidir.
