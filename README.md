# BranchlessPay Audit Shield — Sunmi Android

Blockchain audit trail for Sunmi Android POS devices. Background service anchors every transaction to BranchlessPay / Monad.

| Item | Value |
|------|-------|
| Package | `com.branchlesspay.auditshield` |
| minSdk | 21 (Android 5.0+) |
| Target | Sunmi T2 / V2 / P2 series |
| BP API | `POST https://branchlesspay.com/api/v1/anchor` |
| GitHub | https://github.com/Suhono-BranchlessPay/Sunmi |
| Branch | **`dev` only** |
| Timeline | M1–M4 · September 2026 |

---

## Milestones

| Milestone | Scope | Status |
|-----------|-------|--------|
| **M1** | Setup + basic anchor (Settings, test connection, test anchor) | ✅ |
| M2 | Transaction capture + offline queue + Payment SDK | ⏳ |
| M3 | History, WebView verify, QR print | ⏳ |
| M4 | Sunmi App Store assets + signed release | ⏳ |

See `docs/MILESTONE_M1.md` for M1 report.

---

## M1 — What works

1. **Settings** — save BP license key + API URL (SharedPreferences)
2. **Test Connection** — POST test anchor → expect HTTP **202**
3. **Send Test Anchor** — from home screen with verify URL
4. **BpAuditService** — background stub (M2 will add payment listener)
5. **BootReceiver** — auto-start service on device boot

---

## Quick start (Android Studio)

1. Open folder `Sunmi/` in Android Studio (Ladybug or newer)
2. Sync Gradle · SDK 34 required
3. Run on emulator (API 21+) or Sunmi device via USB
4. **Settings** → enter `BP_LICENSE_KEY` from Bos
5. **Test Connection** → HTTP 202 ✅
6. **Send Test Anchor** → copy verify URL

```powershell
# Optional: run JVM unit tests
cd Sunmi
.\gradlew.bat test
```

---

## Test anchor payload (M1)

```json
{
  "event_type": "sunmi_transaction",
  "reference_id": "TEST-XXXXXXXX",
  "amount": 10000,
  "currency": "IDR",
  "timestamp": "2026-06-14T...Z",
  "vendor": "sunmi",
  "metadata": {
    "erp": "sunmi_pos",
    "device_model": "V2 Pro",
    "device_sn": "..."
  }
}
```

---

## Sunmi SDK (M2+)

| SDK | Purpose | Docs |
|-----|---------|------|
| Payment SDK | Capture real transactions | https://developer.sunmi.com/en-US/ |
| Printer SDK | QR verify receipt (M3) | `com.sunmi:printerlibrary` |
| Push SDK | Notifications (optional) | developer portal |

Official docs: https://docs.sunmi.com/en-US/

---

## Project structure

```
app/src/main/java/com/branchlesspay/auditshield/
├── MainActivity.kt          Home + test anchor
├── SettingsActivity.kt      License key + test connection
├── BpApiClient.kt           BP anchor API + content_hash
├── BpAnchorPayload.kt       Payload builder
├── BpAuditService.kt        Background service (M1 stub)
├── BootReceiver.kt          Boot auto-start
├── DeviceInfo.kt            Model + serial
└── Prefs.kt                 Secure prefs storage
```

---

## Rules

1. English only (code, docs, comments)
2. No hardcoded credentials — use Settings / `.env.example`
3. Private GitHub · `dev` branch only
4. Report per milestone to Bos

---

## Contact

suhono@branchlesspay.com
