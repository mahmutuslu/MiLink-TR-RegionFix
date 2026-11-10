# Gallery & Weather: Flutter localization root-cause investigation

Date: 2026-11-10. Device: Xiaomi 15 Ultra `xuanyuan`, OS4.0.0.18.XOACNXM, API37.

## Observed on device
- With app locale `tr-TR`, Gallery and Weather display Arabic (not Turkish).
- With `en-US`, both show English.
- KernelSU LightOS RROs for `com.miui.gallery`, `com.miui.weather2`, `com.hyper.gallery.plugin` report `STATE_ENABLED`.
- `cmd overlay lookup --user 0 com.miui.gallery com.miui.gallery:string/album_name_all_photos` returned `Tüm ögeler`.
- v1.7 (en) and v1.8 (en-US) experimental overlay locale rekeys **did not** translate the visible Gallery and Weather main screens.
- LSPosed native Zygisk libraries are mapped into Gallery and Weather; no unified Java entry callback was observed there. Java entry does run in Media Editor and Gallery Plugin.

## Original APK evidence
Inspected the user-supplied `HyperOS_TR_APKs.zip`, not third-party ROM sources.

**Gallery 5.4.3.6-1015-cn (`com.miui.gallery-0.apk`)**
- `assets/flutter_assets/` present; primary app APK contains no `classes.dex`.
- `lib/arm64-v8a/libapp.so` is 22,807,568 bytes and contains literal UTF-8 English UI text including `All items`, `Common`, `Favorites`, `Gallery`, `Albums`, `Memories`.
- `lib/arm64-v8a/libapp_gallery.so` is 39,887,904 bytes and also contains several of those English labels.
- Android `resources.arsc` only has 253 default string keys; the existing Gallery RRO defines 76 Turkish string keys. `album_name_all_photos`'s base string is `All photos`, but the user-visible Gallery screenshot says `All items`. These are **different strings**.

**Weather 18.0.1.4-R (`com.miui.weather2-0.apk`)**
- `assets/flutter_assets/` present; primary app APK contains no `classes.dex`.
- `lib/arm64-v8a/libapp.so` is 10,486,704 bytes and contains English `Today`, `Tomorrow`, `24-hour forecast`, `Clear`.
- `lib/arm64-v8a/libweather_app.so` is 7,117,040 bytes and includes `Today`, `Clear`.
- Existing Weather RRO defines 584 en-US strings, but only **237** key names overlap the original Weather APK's 375 default Android string keys. Thus 584 is not equal to 584 active Flutter translations.

## Interpretation
The normal Android RRO can be valid and return Turkish for a known `R.string` key **without changing Flutter-rendered text**. Flutter's AOT-compiled Dart code and its localized UI values may reside in shared native libraries rather than ordinary Android `strings.xml` resources. This directly explains why repeating en/en-US overlay qualifier changes had no visible effect. The exact Flutter localization loading mechanism still requires investigation before proposing a binary patch.

## Safety and release plan
1. **Stop** shipping more locale-only RRO experiments; v1.8 does not solve the user-facing translation.
2. Preserve the existing LightOS system translations and do not reset or clear Gallery/Weather application data.
3. Investigate the compiled Flutter localization tables/delegates and identify a reversible, narrowly targeted text replacement mechanism that survives updates, or explicitly identify when an updated app build is required.
4. The unified LSPosed module still applies to MiLink, Xiaomi Share, Media Editor and Gallery Plugin; the Gallery/Weather main Flutter UI requires separate work.
5. Avoid blindly modifying `libapp.so` or `libweather_app.so`: AOT snapshot offsets, app signatures, and native code changes are brittle and can break app startup or OTA updates.
6. Base future diagnostics on observed actual UI strings plus verified source locations, not guessed overlay language qualifiers.

No account checks, photo database, or application data need alteration for localization.
