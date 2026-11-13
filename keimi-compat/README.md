# KeiMi / HyperOS 4 compatibility investigation

**Status: investigation only — not a working compatibility fix.**

This work is isolated on a feature branch; it does not change the MiLink-TR-RegionFix main branch or any phone/system application.

## Target environment
- Xiaomi 15 Ultra (xuanyuan), HyperOS 4.0.0.18.XOACNXM, Android API 37
- LSPosed API 102, KeiMi 2.6.0 (`os.kei.keimi`)
- Active Mirror `com.xiaomi.mirror` version `18.03.01-26.10.27` (versionCode 180301)
- Factory Mirror `18.01.07-26.08.13` (versionCode 180107)
- Active MiLink `com.milink.service` version `18.2.4.0.2611031737` (versionCode 180020400)
- Factory MiLink `18.2.0.12.2608172246`
- The Xiaomi PC Manager phone connection already works and MUST remain working.

## User-visible failures
- PC discovery / desktop launch: Analysis 0/3, Hook 0/3
- Xiaomi Smart Hub display: Analysis 0/6, Hook 0/6
- KeiMi UI states current versions are outside verified compatibility range.
- KeiMi's verified Mirror: 18.01.07; verified MiLink: 18.2.1.0 / 18.2.2.0.

## Evidence and next engineering gate
- LSPosed module log reported `Computer fusion unavailable for this Mirror build`.
- An independent MiLink/iPhone fusion OEM warning must not be conflated with the Smart Hub failure.
- Version mismatch alone does NOT prove which class/method needs changing.
- Before implementing hooks: extract exact resolved descriptors from KeiMi and validate matching method prototypes, call sites, class loaders and process names in BOTH Mirror and MiLink APKs.
- A counters-only patch or unconditional return-true version bypass is NOT accepted.
- No uninstall/downgrade of system APKs; no writes to /product or /data/app.
- No release until build passes, injection verified, feature behavior tested on-device, and PC Manager regression checked.

## Reproducible local inventory
Run from the directory containing the provided APKs:

```sh
python3 keimi-compat/tools/apk_inventory.py "KeiMi_2.6.0+89f8bc77.apk" MirrorOS4.apk Mirror_18.03.01.apk MiLink_18.2.4.0.apk
```

Only metadata and evidence are committed. **Do not upload proprietary Xiaomi or third-party APKs/logs containing personal data into a public repository.**
