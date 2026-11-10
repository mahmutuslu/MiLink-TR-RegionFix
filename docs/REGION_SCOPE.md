# Region fix: trusted-device compatibility

MiLink \`NetworkingManager.getStringProperty(String, PropBuildRegion)\` sorgusunun hedefi tüm **önceden güvenilir olarak kayıtlı uzak cihazlar** olabilir: Windows PC, global telefon, tablet, uyumlu diğer cihaz tipleri.

v0.3 alpha3'te:
- Yalnızca \`PropBuildRegion\` özelliğinin dönen değeri değiştirilir.
- Uzak cihazın \`getTrustedDeviceInfo(deviceId)\` kaydı bulunmalı; dönen nesnenin \`getDeviceId()\` değeri istenen cihaz kimliği ile eşleşmelidir.
- Bilinmeyen cihaz kimliği, boş veri, bilinmeyen enum veya yerel bölgenin okunamaması durumunda orijinal sonuç korunur.
- Hiçbir cihaz kaydı oluşturulmaz; eşleştirme, hesap oturumu, güvenlik kimliği, token ve sunucu yanıtları değiştirilmez.
- \`com.xiaomi.mirror\` içindeki keşif/bölge kontrolü ve sunucu tarafı Çin hesabı sınırlaması kapsam dışıdır.

Bu yüzden **yerel bölge karşılaştırması** için daha genel bir düzeltme sunar; bütün Xiaomi bölge kısıtlamalarını kaldırdığını garanti etmez.

Kaynak inceleme: https://github.com/luobi125/Get-Out-Xiaomi-Interconnection-Regional-Lock
