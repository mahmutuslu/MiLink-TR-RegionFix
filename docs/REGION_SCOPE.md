# Region Fix — design and boundaries

The term “China account required” can describe several distinct conditions:

1. **Local UI / product region gating:** App-side feature visibility checks and device locale/region flags.
2. **Account metadata:** Xiaomi Account's registration region and availability rules.
3. **Server-side authorization:** Remote eligibility checks, device attestation, and entitlement.

This project's **alpha1 does not bypass any of them**. It provides a place to collect privacy-preserving diagnostic observations and eventually patch narrowly confirmed *local compatibility gates*, provided that doing so is safe and does not impersonate server responses, forge credentials, or promise inaccessible entitlements.

Future changes require:
- Exact MiLink APK version and tested call sites.
- Reproducible symptoms (feature, region, account type) with credentials redacted.
- Baseline behavior before the hook and an explicit rollback.
- Confirmation that server-side restrictions remain respected.

No device-wide spoofing of Xiaomi Account, fingerprint, security attestation, or login tokens is included.
