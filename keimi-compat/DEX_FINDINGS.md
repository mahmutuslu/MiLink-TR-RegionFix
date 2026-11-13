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


## KeiMi DEX cross-references: verified from supplied KeiMi 2.6.0 APK

Source: `classes2.dex`, decoded with read-only DEX class/method definition tables plus const-string reference examination.

**Important correction:** `resolve_mirror_computer_fusion`, `mirror_computer_fusion_descriptors`, `milink_fusion_descriptors` and `milink_fusion_resolver_schema` are **string constants**, NOT Java method declarations.

| Class and method-id | DEX code offset | Observed operation |
| --- | --- | --- |
| `Ldn;->III` (method-id 4509) | `0xF72E0` | Builds a Bundle with `mirror_computer_fusion_descriptors` and `health_host_version_code`; uses `mirror-computer-fusion-v1` |
| `Lk92;->I11` (method-id 9966) | `0x22544C` | Calls `ContentResolver.call` with `resolve_mirror_computer_fusion`; reads descriptor Bundle, then uses reflection and method resolving |
| `Lhu0;->IIo` (method-id 7099) | `0x186F24` | Reads `milink_fusion_resolver_schema` and `milink_fusion_descriptors`; throws for unsupported MiLink version, missing schema or missing methods |
| `Lhu0;->IO0` (method-id 7101) | `0x1883E4` | Reports `MiLink fusion descriptor missing:` when a required descriptor is absent |

These are obfuscated KeiMi internal method names, not the 3+6 Xiaomi hook targets.
The caller resolves version-dependent descriptors dynamically and reflects over the target APK's types.
`Lk92;->I11` contains references to classes:
`com.xiaomi.continuity.networking.BusinessServiceInfo`,
`com.xiaomi.continuity.networking.TrustedDeviceInfo`,
`com.xiaomi.continuity.channel.Packet`,
`com.xiaomi.continuity.channel.PacketTransferProgress`,
`com.xiaomi.continuity.channel.Channel`,
`com.xiaomi.mirror.message.ScreenActionMessage`.
These are **class-name strings used during reflection**, not proof that six classes each correspond to a distinct hook.

**Remaining task:** Decode the body of the descriptor producing code (including version-gated and potentially obfuscated mapping construction) to recover each enum-key -> class/method/field signature tuple. Then compare actual definitions in both OEM APK versions. Until that is done, listing nine exact hook signatures would be fabricated and a patch must not ship.


## Additional DEX method-definition verification

The following signatures and code offsets were verified against the **defined-method table**, not only raw keyword matches, in `KeiMi_2.6.0+89f8bc77.apk/classes2.dex`:

- `Ldn;->III(Landroid/content/Context;)Landroid/os/Bundle;` — method-id 4509, code offset `0xF72E0`, 100 code units. Calls `Lfn;->I11(Context,String)Lx91;` (reads package metadata) and `Lyd0;->Il(Context,String,String,long,String,Ljz0;)Lwd0;` with package `com.xiaomi.mirror` and schema `mirror-computer-fusion-v1`. Then serializes map entries into `mirror_computer_fusion_descriptors` and returns `health_host_version_code`.
- `Lk92;->I11(Context,ClassLoader)Lh91;` — method-id 9966, code offset `0x22544C`, 325 code units. Calls `ContentResolver.call` with `resolve_mirror_computer_fusion`, checks returned host-version value, loads the descriptor Bundle, and resolves reflective methods.
- `Lhu0;->IIo(Application,ClassLoader,Bundle)Lef1;` — method-id 7099, code offset `0x186F24`, 974 code units. Checks MiLink package `versionCode`; requires `milink_fusion_resolver_schema` equal to `iphone-fusion-v3` and `milink_fusion_descriptors`. Resolves reflective methods/fields and a `COMPUTER_CONSTRUCTOR`. Error paths include `Unsupported MiLink versionCode=` and `MiLink fusion resolver schema missing/mismatched for versionCode=`.
- `Lhu0;->IO0(String,Bundle)String` — method-id 7101, code offset `0x1883E4`, 18 code units. Looks up required descriptor entries.

Additional follow-up into the descriptor producer:
- `Lfn;->I11(Context,String)Lx91;` — method-id 5619, code offset `0xFB3B8`, checks `PackageInfo.getLongVersionCode()`.
- `Lyd0;->Il(Context,String,String,long,String,Ljz0;)Lwd0;` — method-id 18936, code offset `0x1543EC`, the next dependency for deriving version-specific descriptor maps.
- `Lz9;->I1(Object,Object)Object` — method-id 19633, code offset `0xD6D48`, a large 2388-unit multi-case lambda containing `FusionUtils`/desktop-device strings. Its mere string presence does not prove these are the active hook targets.

**Correctness caveat:** The MiLink `iphone-fusion-v3` errors concern iPhone fusion and are not yet proven to be the Xiaomi Smart Hub 0/6 feature. Keep these two investigations separate. No nine-item exact target tuple list established, and no functional patch validated yet.
