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


## 2026-11-13: deeper local APK verification

The locally mounted original `KeiMi_2.6.0+89f8bc77.apk` was inspected again, using DEX method definition tables and read-only opcode examination, with specific code offsets:

| DEX defined method | Offset | Verified direct calls |
| --- | --- | --- |
| `Lyd0;->Il(Context,String,String,long,String,Ljz0;)Lwd0;` | `0x1543ec`, 70 code units | `Low4;->lI`, `Lyd0;->I`, `ConcurrentHashMap.computeIfAbsent`, `Lyd0;->O`, `Lyd0;->II` |
| `Lyd0;->II(Context,String,String,String,long,String,Ljz0;)Lwd0;` | `0x154494`, 287 code units | `Led0;->l`, `Ljz0;->I1`, `Low4;->II`, `Lyd0;->I0`; logs DexKit query / cache / JNI metrics |
| `Lyd0;->I0(Context,String,long,String,Lxd0;,Object,Lmd0;,long)Lwd0;` | `0x15427c`, 148 code units | Captures DexKit query metrics: target / schema / source / result hits / query failures / bridge count |
| `Lyd0;->O(Context,String,long)V` | `0x1547d4`, 154 code units | Verifies target APK fingerprint, invalidates cached entries when it changes |
| `Lyd0;->I(long,String,String)String` | `0x154774`, 24 code units | Creates a cache key containing version code and descriptor schema |

**Critical corrected interpretation:** The `mirror_computer_fusion_descriptors` Bundle is assembled from the `Lwd0;` result map after a versioned **DexKit search**, not a static pre-written list of all hook method signatures. Thus the mere presence of `0/3` and `0/6` does NOT establish nine hardcoded methods inside the APK. The resolver can yield no matches on changed OEM builds. The first missing datum is the actual query rules and the expected per-feature output keys, particularly the `Ljz0;` callback and the Mirror `Lz9;` case. Do not invent nine signatures.

The phone's working Xiaomi PC Manager connection remains out of scope for modification. No functional LSPosed patch has been validated.


## Mirror DexKit callback discriminator verified (raw DEX instructions)

Read-only examination of the user-provided `KeiMi_2.6.0+89f8bc77.apk`:

- In `classes2.dex`, `Ldn;->III(Context)Bundle` (code item `0xF72E0`) constructs `new Lz9;` and immediately supplies the constructor discriminator `const/16 v1, #+26`, then passes this object as the `Ljz0;` callback to `Lyd0;->Il(...)` with schema `mirror-computer-fusion-v1`.
- `Lz9;->I1(Object,Object)Object` (code item `0xD6D48`) dispatches through a genuine DEX packed-switch, 29 cases, first key 0. Switch key **26** branches to byte offset `0x72` from the start of that method's instructions. Therefore the next analysis should decode **case 26**, not every unrelated `Lz9;` case.
- Caution: `Lz9;` is a shared, heavily obfuscated lambda; other branches contain text like `FusionUtils`, `CommonPc`, or `mirror-desktop-open-display`. The presence of a keyword in the same *method* does not establish it belongs to the Mirror computer-fusion DexKit query. Confirm the case-26 control-flow range before treating these strings as query constraints.
- The confirmed callback route is `Ldn;->III` → `Lz9;(26)` → `Ljz0;->I1` executed from `Lyd0;->II` → `Lwd0;` map → `mirror_computer_fusion_descriptors` Bundle.

No 3/3 or 6/6 hook signatures are asserted without verified descriptor key/value outputs.


## 2026-11-13: Mirror case 26 opcode inspection

The actual packed-switch payload of `KeiMi classes2.dex Lz9;->I1` was parsed (payload code unit 2326, 29 cases, switch key starting at 0). Case 26 targets **code unit 57** (relative byte offset `0x72`) and its candidate linear body spans code units **57–149**.

Verified instructions in that region:

- `new LinkedHashMap` and `LII;->I1(Lpp0;Lpp0;)Llll;` iterator acquisition.
- `Llll;->hasNext()`, `Llll;->next()` enumerate descriptor candidate entries.
- `Enum.name()` and `Enum.ordinal()` extract keys from an enum; `Lof2;` is referenced as a type.
- `Ldd0;->IO(String,Lfz0;)Lee0;` and `Ldd0;->II(String,Lyc0;)Lee0;` create two query builder entries.
- `Map.put()` records results and `Ldz1;->l0O(Map)Map;` returns a transformed map.
- There are no direct `const-string` instructions within code units 57–149, so the actual class/method matching predicates reside in referenced objects/enum values, not literal targets in this branch.

This establishes a concrete query-construction path, **NOT** the 3+6 exact OEM hook signatures. The next dependency is inspecting `Lof2;`, `Lfz0;`, `Lyc0;`, and `Lz92;` along with how case 26 receives its `Lpp0;` inputs. Do not equate the MiLink `iphone-fusion-v3` chain with Smart Hub without evidence.

### Enum class resolved

`Lof2;` is present as a defined DEX class with seven enum-typed static fields (`I0`, `I1`, `II`, `IO`, `Il`, `Io`, `o`), an array field `l0`, and a `Lpp0;` field `l1`. These are the obfuscated descriptor-key candidates used by the case-26 iteration. **Seven enum constants are not equivalent to seven hook methods**; resolving constructor ordinal/name mapping, the `Lpp0;` input source and associated DexKit query builder objects is still required before defining OEM hook targets.


## Concrete enum keys recovered from KeiMi Mirror Case 26

Decoded `Lof2;-><clinit>()V` at `classes2.dex` code-item `0x244198` using the DEX `string_ids`, `method_ids`, `field_ids` and class-definition tables.

| Ordinal | Literal enum name | Obfuscated field |
| ---: | --- | --- |
| 0 | `MAC_ONLINE` | `Lof2;->o` |
| 1 | `REQUEST` | `Lof2;->I0` |
| 2 | `PACKET_CALLBACK` | `Lof2;->I1` |
| 3 | `CHANNEL_READY` | `Lof2;->Il` |
| 4 | `SCREEN_OPEN` | `Lof2;->II` |
| 5 | `DISPLAY_CLOSE` | `Lof2;->IO` |
| 6 | `DISPLAY_INSTANCE` | `Lof2;->Io` |

These are seven **descriptor key names**, *not* seven validated hook methods. The `Lz9;->I1` case-26 loop uses `Enum.name()` to generate the query key and `Enum.ordinal()` to branch to `Ldd0;->IO(String,Lfz0;)` versus `Ldd0;->II(String,Lyc0;)` builders. `Lz92;-><init>(B)V` is created in the case-26 branch and is an obfuscated callback/lambda carrier. Disambiguating its selected case and extracting its actual DexKit predicates is still necessary to identify corresponding classes in Mirror 18.03.01.

**Operational conclusion:** the previous estimate of exactly 3 Mirror + 6 MiLink target methods cannot be derived from UI's 0/3 and 0/6 status labels. A 7-key Mirror descriptor enum has now been extracted directly and independently of the UI counters. Do not present raw enum keys as validated OEM method signatures.

No phone state or PC Manager functionality was changed.
