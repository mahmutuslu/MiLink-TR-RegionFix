package dev.milinktr.regionfix;

import android.text.Spanned;
import android.util.Log;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * English-source, per-application translation.
 * Does not change system or application locale, resource IDs, account or app data.
 * Unknown text is preserved verbatim after upstream updates.
 */
final class UnifiedTranslations {
    private static final String TAG = "HyperOSTR";
    private static final String GALLERY = "com.miui.gallery";
    private static final String EDITOR = "com.miui.mediaeditor";
    private static final String WEATHER = "com.miui.weather2";
    private static final String PLUGIN = "com.hyper.gallery.plugin";
    private static final Map<String, Map<String,String>> DICTIONARIES;

    static {
        Map<String, Map<String,String>> all = new HashMap<>();
        add(all, GALLERY, """
Albums|Albümler
All photos|Tüm ögeler
Photos|Fotoğraflar
Videos|Videolar
Gallery|Galeri
Camera|Kamera
Favorites|Favoriler
Memories|Anılar
Screenshots|Ekran görüntüleri
Screenshots and screen recordings|Ekran görüntüleri ve ekran kayıtları
Downloads|İndirilenler
Documents|Belgeler
Recently deleted|Son silinenler
Deleted photos|Silinen fotoğraflar
Private album|Özel albüm
Search|Ara
Search photos|Fotoğraf ara
Select|Seç
Select all|Tümünü seç
Unselect all|Tüm seçimleri kaldır
Edit|Düzenle
Share|Paylaş
Delete|Sil
Move|Taşı
Copy|Kopyala
Add|Ekle
Remove|Kaldır
Save|Kaydet
Cancel|İptal
Done|Bitti
Back|Geri
Settings|Ayarlar
About|Hakkında
Details|Ayrıntılar
More|Daha fazla
Sort|Sırala
Rename|Yeniden adlandır
New album|Yeni albüm
Create album|Albüm oluştur
View photos|Fotoğrafları görüntüle
All items|Tüm ögeler
Show in albums|Albümlerde göster
Hide|Gizle
Restore|Geri yükle
Permanently delete|Kalıcı olarak sil
Cleaner|Temizleyici
Back up photos|Fotoğrafları yedekle
Download automatically|Otomatik indir
Download quality|İndirme kalitesi
High quality|Yüksek kalite
High definition|Yüksek çözünürlük
Day|Gün
Month|Ay
Year|Yıl
Today|Bugün
Yesterday|Dün
This month|Bu ay
Last month|Geçen ay
Manage|Yönet
Albums|Albümler
Journeys|Seyahatler
Burst|Seri çekim
Clones|Klonlar
Couldn\'t create|Oluşturulamadı
Couldn\'t delete|Silinemedi
Couldn\'t rename|Yeniden adlandırılamadı
""");
        add(all, WEATHER, """
Weather|Hava durumu
Weather forecast|Hava tahmini
Current weather|Güncel hava durumu
Hourly forecast|Saatlik tahmin
Daily forecast|Günlük tahmin
Forecast|Tahmin
Today|Bugün
Tomorrow|Yarın
Yesterday|Dün
Now|Şimdi
Real feel|Hissedilen
Feels like|Hissedilen
Humidity|Nem
Wind|Rüzgâr
Wind speed|Rüzgâr hızı
Wind direction|Rüzgâr yönü
Visibility|Görüş mesafesi
Pressure|Basınç
Precipitation|Yağış
Chance of rain|Yağmur olasılığı
Rainfall|Yağış miktarı
Sunrise|Gün doğumu
Sunset|Gün batımı
UV index|UV indeksi
Air quality|Hava kalitesi
Air quality index|Hava kalitesi indeksi
AQI|Hava kalitesi indeksi
Good|İyi
Moderate|Orta
Unhealthy|Sağlıksız
Very unhealthy|Çok sağlıksız
Hazardous|Tehlikeli
Clear|Açık
Sunny|Güneşli
Mostly sunny|Çoğunlukla güneşli
Cloudy|Bulutlu
Mostly cloudy|Çoğunlukla bulutlu
Partly cloudy|Parçalı bulutlu
Overcast|Kapalı
Rain|Yağmur
Light rain|Hafif yağmur
Heavy rain|Şiddetli yağmur
Showers|Sağanak yağış
Thunderstorm|Gök gürültülü fırtına
Snow|Kar
Light snow|Hafif kar
Fog|Sis
Haze|Pus
Windy|Rüzgârlı
Location|Konum
My location|Konumum
Add city|Şehir ekle
Manage cities|Şehirleri yönet
Search cities|Şehir ara
Settings|Ayarlar
About|Hakkında
Refresh|Yenile
Updated just now|Az önce güncellendi
Last updated|Son güncelleme
Weather alert|Hava durumu uyarısı
Weather alerts|Hava durumu uyarıları
Weather details|Hava durumu ayrıntıları
More|Daha fazla
See more|Daha fazla göster
High|En yüksek
Low|En düşük
Morning|Sabah
Afternoon|Öğleden sonra
Evening|Akşam
Night|Gece
AM|ÖÖ
PM|ÖS
Mon|Pzt
Tue|Sal
Wed|Çar
Thu|Per
Fri|Cum
Sat|Cmt
Sun|Paz
""");
        add(all, EDITOR, """
Edit|Düzenle
Photo editor|Fotoğraf düzenleyici
AI Editor|Yapay zekâ düzenleyici
AI Photo Editor|Yapay zekâ fotoğraf düzenleyici
AI Editor plug-in|Yapay zekâ düzenleyici eklentisi
AI Cutout|Yapay zekâ ile kes
AI ID Photo|Yapay zekâ kimlik fotoğrafı
AI Erase|Yapay zekâ ile sil
AI Erase Pro|Gelişmiş yapay zekâ silgisi
AI Enhance|Yapay zekâ ile iyileştir
AI Ultra HD|Yapay zekâ Ultra HD
AI Expansion|Yapay zekâ ile genişlet
AI portrait|Yapay zekâ portre
Crop|Kırp
Rotate|Döndür
Flip|Çevir
Straighten|Düzleştir
Filters|Filtreler
Filter|Filtre
Beauty|Güzelleştir
Beautify|Güzelleştir
Adjust|Ayarla
Enhance|İyileştir
Erase|Sil
Remove|Kaldır
Remove reflection|Yansımayı kaldır
Expand|Genişlet
Background|Arka plan
Background blur|Arka plan bulanıklığı
Background replacement|Arka plan değiştirme
Cutout|Dekupe
Sticker|Çıkartma
Stickers|Çıkartmalar
Text|Metin
Frame|Çerçeve
Frames|Çerçeveler
Watermark|Filigran
Leica watermark|Leica filigranı
Brightness|Parlaklık
Contrast|Kontrast
Exposure|Pozlama
Saturation|Doygunluk
Vibrance|Canlılık
Sharpness|Keskinlik
Clarity|Netlik
Highlights|Parlak alanlar
Shadows|Gölgeler
Warmth|Sıcaklık
Temperature|Sıcaklık
Tint|Renk tonu
Hue|Ton
Grain|Gren
Vignette|Vinyet
Noise|Gürültü
Fade|Soluklaştırma
White balance|Beyaz dengesi
Effects|Efektler
Details|Ayrıntılar
Intensity|Yoğunluk
Size|Boyut
Original|Orijinal
Compare|Karşılaştır
Undo|Geri al
Redo|Yinele
Reset|Sıfırla
Save|Kaydet
Save copy|Kopya kaydet
Cancel|İptal
Apply|Uygula
Done|Bitti
Back|Geri
Next|İleri
Retry|Yeniden dene
Download|İndir
Downloading|İndiriliyor
Face|Yüz
Portrait|Portre
Body|Vücut
Skin|Cilt
Skin tone|Cilt tonu
Smooth|Pürüzsüzleştir
Sky|Gökyüzü
Skyscaping|Gökyüzü değiştirme
Magic sky|Sihirli gökyüzü
Video|Video
Photo|Fotoğraf
Speed|Hız
Trim|Kırp
Music|Müzik
Sound|Ses
Volume|Ses düzeyi
Aspect ratio|En boy oranı
Landscape|Yatay
Square|Kare
Free|Serbest
Mirror|Yansıt
Draw|Çiz
Mosaic|Mozaik
Blur|Bulanıklaştır
More|Daha fazla
Select|Seç
Share|Paylaş
Move|Taşı
Copy|Kopyala
Delete|Sil
Loading…|Yükleniyor…
Processing…|İşleniyor…
Just a sec…|Bir saniye…
Expanding image…|Görüntü genişletiliyor…
Changing background…|Arka plan değiştiriliyor…
Beautifying…|Güzelleştiriliyor…
Erasing…|Siliniyor…
Enhancing…|İyileştiriliyor…
Applying…|Uygulanıyor…
Sign in|Oturum aç
Exit|Çıkış
This feature isn't available now|Bu özellik şu anda kullanılamıyor
""");
        add(all, PLUGIN, """
AI Editor|Yapay zekâ düzenleyici
AI Photo Editor|Yapay zekâ fotoğraf düzenleyici
AI Erase|Yapay zekâ ile sil
AI Erase Pro|Gelişmiş yapay zekâ silgisi
AI Expand|Yapay zekâ ile genişlet
AI Enhance|Yapay zekâ ile iyileştir
AI Cutout|Yapay zekâ ile kes
AI ID Photo|Yapay zekâ kimlik fotoğrafı
Cutout|Dekupe
Background|Arka plan
Background replacement|Arka plan değiştirme
Filter|Filtre
Filters|Filtreler
Erase|Sil
Enhance|İyileştir
Portrait|Portre
Photo|Fotoğraf
Video|Video
Edit|Düzenle
Save|Kaydet
Apply|Uygula
Cancel|İptal
Done|Bitti
Reset|Sıfırla
Retry|Yeniden dene
Download|İndir
Downloading|İndiriliyor
Loading…|Yükleniyor…
Processing…|İşleniyor…
Just a sec…|Bir saniye…
Expanding image…|Görüntü genişletiliyor…
Changing background…|Arka plan değiştiriliyor…
Beautifying…|Güzelleştiriliyor…
Erasing…|Siliniyor…
Enhancing…|İyileştiriliyor…
Applying…|Uygulanıyor…
Brightness|Parlaklık
Contrast|Kontrast
Exposure|Pozlama
Saturation|Doygunluk
Sharpness|Keskinlik
Clarity|Netlik
Highlights|Parlak alanlar
Shadows|Gölgeler
Warmth|Sıcaklık
Original|Orijinal
Compare|Karşılaştır
Undo|Geri al
Redo|Yinele
More|Daha fazla
Settings|Ayarlar
""");
        DICTIONARIES = Collections.unmodifiableMap(all);
    }

    private UnifiedTranslations() {}

    private static void add(Map<String,Map<String,String>> all, String pkg, String input) {
        HashMap<String,String> dictionary = new HashMap<>();
        for (String line : input.split("\n")) {
            int split = line.indexOf('|');
            if (split <= 0 || split == line.length() - 1) continue;
            String original = line.substring(0, split);
            String translated = line.substring(split + 1);
            if (original.indexOf('%') != -1 || translated.indexOf('%') != -1) continue;
            dictionary.put(original, translated);
        }
        all.put(pkg, Collections.unmodifiableMap(dictionary));
        Log.i(TAG, "Loaded " + dictionary.size() + " English→Turkish labels for " + pkg);
    }

    static CharSequence translate(String packageName, CharSequence original) {
        if (original == null) return null;
        if (original instanceof Spanned) return original; // Preserve styled/clickable spans.
        Map<String,String> d = DICTIONARIES.get(packageName);
        if (d == null) return original;
        String raw = original.toString();
        String exact = d.get(raw);
        if (exact != null && !exact.equals(raw)) return exact;
        String normalized = raw.replace('\u00a0', ' ').replaceAll("\\s+", " ").trim();
        String fallback = d.get(normalized);
        return fallback != null && !fallback.equals(raw) ? fallback : original;
    }
}
