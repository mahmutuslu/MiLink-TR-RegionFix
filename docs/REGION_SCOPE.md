# Yerel bölge uyumluluğu ve Çin hesabı sınırları

İki ayrı kısıtlama vardır:

1. **Cihaz bölge eşleştirmesi:** Aynı Xiaomi hesaplarına sahip, ancak farklı ülke kodu bildiren Windows PC'ler, cihaz keşfi/clipboard/file özelliklerinde yerel \`PropBuildRegion\` eşleştirmesine takılabilir. Bu sürümde yalnızca karşı tarafın \`getDeviceType()\` değeri PC=4 veya OTHER_PC=21 ise, MiLink'in yerel eşleştirme yanıtında telefonun kendi bölgesi kullanılır.
2. **Xiaomi hesabının ülke ve sunucu yetkisi:** Xiaomi sunucusunun Çin hesabı, sunucu etkinleştirmesi veya sertifika/cihaz onayı şartı LSPosed tarafından otomatik kaldırılmaz. Modül bu kontrolleri değiştirmez.

MiLink APK üzerinde aşağıdaki imzalar doğrulandı:

- \`com.xiaomi.continuity.networking.NetworkingManager.getStringProperty(String,int):String\`
- \`getTrustedDeviceInfo(String):TrustedDeviceInfo\`
- \`getLocalDeviceInfo():TrustedDeviceInfo\`
- \`TrustedDeviceInfo.getDeviceType():int\`
- \`TrustedDeviceInfo.getDeviceId():String\`

Modül \`PropertyType.PropBuildRegion\` enum sabitini çalışma anında çözümlemeyi dener; yoksa **bölge değişimi yapmaz** (fail closed).

Bu yöntem cihaz keşfinin \`com.xiaomi.mirror\` tarafında ayrı region check varsa onu otomatik geçmez. Çalışan özellikleri bozmamak için kaynağı doğrulanmamış obfuscation hook'ları eklenmemiştir.

Aşağıdaki işlemler bilinçli olarak **yapılmaz**: hesap kimliğini veya ülkesini sahteleştirme, giriş belirteçlerine müdahale, sunucu yanıtlarını değiştirme, cihaz güvenilirliği atlatma veya oturum güvenliği değişiklikleri.

Referans: https://github.com/luobi125/Get-Out-Xiaomi-Interconnection-Regional-Lock (yerel PC bölge uyumu analizi); bu repo kaynak kodunu kopyalamaz, doğrulanan imzaları kullanır.
