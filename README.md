# MiLink TR + Region Fix — LSPosed API 102

Xiaomi HyperOS 4 ve MiLink (\`com.milink.service\`) için açık kaynak test modülü.

**Son sürüm: v0.2.0-alpha2 — cihazda henüz doğrulanmadı.** Önceki v0.1.0-alpha1 ana Interconnectivity sayfasında Türkçe metinleri başarıyla gösterdi.

## v0.2 alpha2 kapsamı

- **Tüm MiLink alt menüleri:** yalnızca başlangıç \`ConnectivityActivity\` yerine MiLink'e ait tüm \`Activity\` ekranlarında, eşleşen UI metinlerini Türkçeye çevirir.
- **Dinamik çeviri sözlüğü:** cihazda yüklü MiLink APK'sının \`R$string\` kaynaklarını İngilizce ve Türkçe yapılandırmada çözümler. Aktif LightOS/KernelSU Türkçe overlay'inden yararlanır. Çakışan metinleri güvenle atlar; çalışma anında sadece UI \`TextView\` değerlerini değiştirir. Ayrıca ilk ekranda doğrulanmış metinler için sabit karşılıklar vardır.
- **Zengin biçimli metinler:** Ana ekrandaki "Learn more" linkini koruyacak sınırlı bir dönüştürme içerir.
- **Yerel bilgisayar bölge uyumu:** \`NetworkingManager.getStringProperty(String,int)\` içindeki \`PropBuildRegion\` karşılaştırmasında, karşıdaki cihazın tipinin PC (4) veya OTHER_PC (21) olduğunu doğrularsa yerel bölgeyi kullanır. Diğer cihazlara dokunmaz.
- **Hesap doğrulama:** Xiaomi hesabı, giriş bilgisi, token'lar, sunucu doğrulaması, abonelik veya sunucu tarafı Çin hesabı gereksinimi **değiştirilmez / kaldırılamaz**. Bilgisayarın bölge uyumu ile hesap ülke kısıtı farklı konulardır.

## Hedef

- Xiaomi 15 Ultra \`xuanyuan\`
- HyperOS \`OS4.0.0.18.XOACNXM\`, Android 17, API 37
- KernelSU Next ve modern LSPosed API 102
- LSPosed kapsamı: **yalnızca \`com.milink.service\`**

## Kurulum

1. GitHub Actions → **Build debug APK** → son başarılı iş akışının APK artifact'ını indir.
2. APK'yi telefonuna yükle; önceki v0.1 alpha1 varsa üzerine kurulabilir (aynı paket, debug imzası aynı ortamda farklı olabilir; gerekirse önce eski test APK'sını kaldır).
3. LSPosed'de modülü etkinleştir; **\`com.milink.service\`** işaretle ve uygulamayı yeniden başlat.
4. MiLink uygulama dilini \`tr-TR\` olarak ayarla.
5. Interconnectivity, Ana ekran+, Bildirimler, Arama senkronizasyonu, Paylaşılan bağlantı, Pano, Kamera, Dosyalar ve Gizlilik menülerini kontrol et.
6. \`adb logcat -d -s MiLinkTR:V\` üzerinden sözlük ve yerel bölge uyumunun loglarını görebilirsin.

## Neden tek seferde alt menülere kapsam?

LightOS overlay'in çok sayıda Türkçe kaynak içermesine rağmen MiLink'in kendi ekranlarında bunları İngilizce bağlamla kullanabildiği gözlendi. Kaynak isimlerini elle tek tek sabitlemek yerine çalışma anında \`R$string\` alanlarını karşılaştırmak daha geniş sürüm uyumu sağlar.

## Güvenlik ve geri alma

Bu modül MiLink'teki UI yazılarını ve yalnızca PC tipindeki cihazlar için yerel bölge özelliğini etkiler. KernelSU çeviri ZIP'i ile bağımsızdır; Çin hesabı sunucu doğrulamasını atlatmaz. Sorun halinde LSPosed'de modülü devre dışı bırakıp MiLink'i yeniden başlat.

Tanınan sınıflar/sürüm eşleşmeleri ROM güncellemesiyle değişebilir. APK'nin GitHub'da derlenmiş olması her özelliğin telefonda doğrulandığı anlamına gelmez. Ayrıntılar: [TESTING.md](docs/TESTING.md), [REGION_SCOPE.md](docs/REGION_SCOPE.md).
