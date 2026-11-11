# MiLink TR + Region Fix v1.0.0 — kurulum ve kabul kontrolleri

Hedef: Xiaomi 15 Ultra `xuanyuan`, HyperOS 4, LSPosed API 102.

## CI doğrulaması
- Java 17 ile APK derlemesi
- 505 MiLink kaynak anahtarı, benzersizlik kontrolü
- LSPosed kapsamında yalnızca `com.milink.service` ve `com.miui.mishare.connectivity`
- Galeri/Hava Durumu/Flutter sözlük sınıfları derlemeye dahil değil

## Telefonda
1. Mevcut `dev.hyperostr.unified` imzası yeni APK ile uyuşmuyorsa önceki test sürümünü kaldır; LSPosed kapsamını yeniden aç.
2. LSPosed iki kapsamı etkinleştir ve telefonu yeniden başlat.
3. MiLink ana ve alt menülerindeki Türkçe metinleri, bağlantıları, Bildirimler > Al sayfasını kontrol et.
4. Xiaomi Share sayfası metinlerini ve dosya aktarımını kontrol et.
5. `adb logcat -d -v time -s MiLinkTR:V HyperOSTR:V` çıktısında hook kurulumlarını gözle.
6. Aynı Xiaomi hesabındaki önceden güvenilir cihaz keşfini ayrı doğrula; diğer hesabın yanlışlıkla görünmediğini kontrol et.
7. Uygulama çökmesi veya eşleşme sorunu olursa modülü devre dışı bırak, işlemleri yeniden başlat.

CI başarısı, telefondaki tüm bağlantı senaryolarının kanıtı değildir. Dün denenen MiLink/Xiaomi Share kapsamını korur; global keşif ve Xiaomi sunucu kısıtları garanti edilmez. Galeri ve Hava Durumu kapsam dışıdır.
