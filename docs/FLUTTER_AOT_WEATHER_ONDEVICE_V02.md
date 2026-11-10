# Flutter AOT Weather — verified on-device milestone and Unicode follow-up

Date: 2026-11-10. Device: Xiaomi 15 Ultra `xuanyuan`, HyperOS 4, Android API 37, KernelSU Next.

## v0.1.2: ON-DEVICE SUCCESS, safely reverted

User launched the guarded temporary bind-mount tool against installed Weather `com.miui.weather2`, version `18.0.1.4-R`. Before patch, installed extracted `libapp.so` matched the expected SHA256:

`4419550083cf6b2d793431c8adfe387a3bfa40f1aed662da6a338fc408ad0182`.

The original Dart isolate snapshot-data literal `Today` at file offset `0x8d8ee` was replaced by the same-byte-length ASCII `Bugun`. User screenshot visibly showed **Bugun** on the main Weather daily forecast list (neighboring row still **Tomorrow**), proving the selected Dart string is consumed by the Flutter UI. Script output showed:

```
Temporary bind mount visible in root shell. Starting app...
Bind mount is visible in app process mount namespace.
UMOUNT: completed
ORIGINAL RESTORED: 4419550083cf6b2d793431c8adfe387a3bfa40f1aed662da6a338fc408ad0182
EXPERIMENT COMPLETED; ORIGINAL RESTORED: 4419550083cf6b2d793431c8adfe387a3bfa40f1aed662da6a338fc408ad0182
```

No permanent app, ROM, or KernelSU module modification occurred. The installed Gallery is `5.4.3.10-1030-cn`, not our original export `5.4.3.6-1015-cn`. Gallery is deliberately excluded from subsequent test until its installed native library is available.

## v0.2: offline-only Latin-1 proof (NOT yet tested on device)

Based on the verified Weather literal, try the correct Turkish **Bugün**, encoded as a five-code-unit **Latin-1** Dart `OneByteString` (`42 75 67 FC 6E`). The original ASCII `Today` was `54 6F 64 61 79`; both occupy 5 bytes. The preceding byte at offset `0x8d8ed` is `8A`, compatible with a compact length-5 OneByteString encoding, but this by itself does not guarantee the app will display `ü` correctly.

`HyperOS_Flutter_Turkce_AOT_PoC_v0.2.zip` contains only the Weather patch, a SHA/version-guarded reversible PowerShell 5.1 tester, reproducible offline builder, and manifest. It is a manual test, **not** an installable KernelSU module. The patched SHA256 is:

`89749ba44f8b1aad38ca05c009e326e6f5c2077ef8aaf05c6ec455432d8514af`

The proof verified that only the target five bytes changed, ELF size did not change, and both Dart snapshot magic headers remained intact. Actual correct Unicode rendering and app stability are still unverified. If device test fails, use the script's rollback or reboot.

### Scope limits
- This approach does not yet handle variable-length translations (`Tomorrow → Yarın`), Turkish dotless `ı` or `ğ` (which are not representable in Latin-1), nor all other Flutter UI strings.
- A long-term comprehensive translation needs Dart AOT object-aware string relocation / rewriting (and update-safe version detection), not repeated ad-hoc byte replacements.
- Do not distribute Xiaomi proprietary `.so` blobs in a public repository. Keep GitHub docs and scripts source-only; test bundles are for the user's own extracted APKs.
