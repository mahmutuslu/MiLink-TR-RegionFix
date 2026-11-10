# Flutter AOT v0.1 — same-byte-length string proof, 2026-11-10

**Status:** Offline patch and structural verification **complete**. Device/UI test **not performed**; no claim of working Turkish translation yet.

The supplied `HyperOS_TR_APKs.zip` was used as the baseline; Gallery 5.4.3.6-1015-cn and Weather 18.0.1.4-R. These vendor apps use Flutter AOT, not just Android resource strings.

## Proofs

| App | AOT data file | Isolate-data offset | Original bytes | Experimental bytes | SHA256 baseline |
| --- | --- | --- | --- | --- | --- |
| Gallery | `lib/arm64-v8a/libapp.so` | `0x128efd` | `Favorites` | `Favoriler` | `e45eaf2defd28a27eebd25d24ffb1b3d75c99435f91690edd71952b16a51db6d` |
| Weather | `lib/arm64-v8a/libapp.so` | `0x8d8ee` | `Today` | `Bugun` | `4419550083cf6b2d793431c8adfe387a3bfa40f1aed662da6a338fc408ad0182` |

Both occurrences are within the **Dart isolate snapshot data**, starting at file offset `0x4ac0`. Replacements preserve exact UTF-8 byte length (9→9; 5→5) and library size (22,807,568 and 10,486,704 bytes respectively). Structural checks confirmed:
- Four-byte snapshot magic `f5f5dcdc` unchanged at VM offset `0x340` and isolate offset `0x4ac0`
- Snapshot header bytes unchanged
- Only bytes inside the selected original exact string range modified (Gallery 2 changed bytes, Weather 5)
- ELF header and layout untouched
- Both patched libraries SHA256-verified

**IMPORTANT:** The selected literal may or may not be used by the visible UI. A same-length swap is NOT proof of valid AOT object hashes or app startup. `Bugun` is deliberately temporary ASCII, not final Turkish `Bugün` (which would require a 6-byte UTF-8 representation). Rewriting arbitrary occurrences or app signatures is unsafe.

Generated local proof bundle (not checked into GitHub because it contains Xiaomi proprietary binary blobs):
`HyperOS_Flutter_Turkce_AOT_PoC_v0.1.zip`, consisting of a reproducible Python builder, SHA manifest, guarded Windows PowerShell 5.1 diagnostic and reversible test scripts, and two research-only patched `libapp.so` files.

## Runtime test caveats

`/data/app/.../lib/arm64/libapp.so` may be absent or inaccessible depending on native extraction. The guarded test script refuses to proceed if paths, package versions, or source SHA-256 values differ. Any temporary bind-mount is rollback-only, no autostart, and may not propagate to the app mount namespace; check that separately. App crash or unchanged UI is possible. Never install these as a normal KernelSU ZIP or overwrite the original APK. PowerShell script only stages a new file and temporarily bind-mounts after explicit confirmation, with automatic unmount when the user finishes.

Once it is proven in a live app which Dart string object drives the UI, consider a separate update-safe, version-gated native loader and Unicode-aware patching. Until then, keep existing system overlay and the verified MiLink/Xiaomi Share LSPosed work unchanged.
