# MiLink TR + Region Fix v1.0.0

**MiLink ve Xiaomi Share odaklı tek APK (LSPosed API 102).** Galeri, Hava Durumu, Media Editor, Gallery Plugin ve Flutter AOT deneyleri bu yayından tamamen çıkarılmıştır.

### Dahil
- MiLink/Interconnectivity ana ve alt sayfalarında Türkçe UI eşleştirmesi
- Xiaomi Share Türkçe metinleri
- Önceden güvenilir cihazlar için dar kapsamlı yerel bölge karşılaştırma uyumu
- Xiaomi hesabı mevcutken yalnızca bildirim keşfindeki yerel CN kontrolü için koşullu iyileştirme

### Sınırlar
Bu modül Xiaomi hesap kimliğini, tokenları, same-account denetimini veya sunucu yetkisini değiştirmez. Tüm Global cihazların bulunması ya da tam bölge kilidi kaldırılması **garanti edilmez**. Flutter tabanlı Galeri/Hava Durumu çevirisi içermez.

### Kurulum
APK'yi yükle; LSPosed kapsamından **`com.milink.service`** ve **`com.miui.mishare.connectivity`** işaretle; telefonu/uygulamaları yeniden başlat. Sorun halinde LSPosed'den kapat ve işlemleri yeniden başlat.

**APK imzası:** GitHub Actions debug anahtarıyla imzalanmıştır. İleride farklı imza ile güncelleme halinde eski sürümü kaldırarak yeniden kurmak gerekebilir. Bu imza üretim amaçlı kalıcı güncelleme anahtarı değildir.

Kaynak: https://github.com/mahmutuslu/MiLink-TR-RegionFix
