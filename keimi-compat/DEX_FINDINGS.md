# DEX findings (2026-11-13)

Source artifacts privately supplied by the device owner. APKs and private logs are **not** redistributed in this repository.

## Verified method references from MiLink 18.2.4.0

- `Lcom/xiaomi/miplay/mylibrary/utils/SmartHubUIUtil;->enterSmartHubUI(I)V`
- `Lcom/xiaomi/miplay/mylibrary/utils/SmartHubUIUtil;->quitSmartHubUI(I)V`
- `Lcom/xiaomi/miplay/audioclient/sdk/MiPlayClient;->enterSmartHubUI(I)I`
- `Lcom/xiaomi/miplay/audioclient/sdk/MiPlayClient;->quitSmartHubUI(I)I`

The signatures come from the APK's DEX method ID tables; they may include references rather than implementations. They are NOT yet verified hook targets for KeiMi. Do not hook them blindly, force successful status counters, or claim functionality.

## Mirror comparison

Mirror 18.01.07 and 18.03.01 were both parsed. They share device-discovery protocol references, but the exact 3 KeiMi computer-fusion hook targets have not been established. The earlier informal claim that `FusionUtils` is implemented in the two Mirror APKs is **not supported by these method reference tables**.

## Remaining blockers

1. Identify the exact KeiMi 2.6.0 3/3 and 6/6 compatibility descriptors from the DEX instructions and runtime evidence, not just strings.
2. Locate the actual implementing classes and invoke sites in Mirror and MiLink.
3. Implement explicitly version-guarded LSPosed changes with rollback and non-invasive logging.
4. Verify on the device without changing working Xiaomi PC Manager installation.

**Status: code-analysis tooling committed; no verified working APK or compatibility hook fix.**
