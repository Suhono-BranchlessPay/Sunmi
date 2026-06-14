# BranchlessPay Audit Shield — Sunmi Android

Blockchain audit trail for Sunmi Android POS devices. Background service anchors every transaction to BranchlessPay / Monad.

| Item | Value |
|------|-------|
| Package | `com.branchlesspay.auditshield` |
| Version | **1.0.0** (versionCode 4) |
| minSdk | 21 (Android 5.0+) |
| Target | Sunmi T2 / V2 / P2 series |
| BP API | `POST https://branchlesspay.com/api/v1/anchor` |
| Privacy | https://branchlesspay.com/privacy |
| GitHub | https://github.com/Suhono-BranchlessPay/Sunmi |
| Branch | **`dev` only** |
| Timeline | M1–M4 · September 2026 |

---

## Milestones (complete)

| Milestone | Scope | Status |
|-----------|-------|--------|
| **M1** | Setup + basic anchor | ✅ **CLOSED** |
| **M2** | Payment capture + offline SQLite queue | ✅ **CLOSED** |
| **M3** | History, WebView verify, Share, QR print | ✅ **CLOSED** |
| **M4** | Signed release + Sunmi App Store pack | ✅ **CLOSED** |

See `docs/MILESTONE_M1.md` · `M2` · `M3` · `M4` · `docs/SUNMI_APPSTORE.md`

---

## Features (v1.0.0)

1. **Settings** — BP license key, API URL, test connection, privacy policy link
2. **Background service** — captures Sunmi payments (broadcast) or debug simulate on emulator
3. **Offline queue** — SQLite, max 3 retries, flush on reconnect
4. **Transaction history** — Pending / Anchored / Failed badges + verify URL
5. **Verify WebView** — `branchlesspay.com/verify/[id]` with VERIFIED badge
6. **Share + Print QR** — Sunmi printer SDK on hardware
7. **Boot auto-start** — `BootReceiver` starts foreground service

---

## Quick start (Android Studio)

1. Open folder `Sunmi/` in Android Studio (Ladybug or newer)
2. Copy `local.properties.example` → `local.properties` (SDK path + optional dev key)
3. Sync Gradle · SDK 35 required
4. Run on emulator (API 21+) or Sunmi device via USB
5. **Settings** → enter `BP_LICENSE_KEY` → **Save** → **Test Connection** → HTTP 202
6. **Simulate Payment** (emulator) or real payment on Sunmi hardware

```powershell
# API smoke test (uses .env — copy from .env.example)
python scripts/test_anchor.py

# Unit tests + debug APK
powershell -ExecutionPolicy Bypass -File scripts\run_tests_and_build.ps1

# Sunmi App Store assets (icon, screenshots, feature graphic)
pip install pillow
python scripts/generate_store_assets.py

# Signed release APK
powershell -ExecutionPolicy Bypass -File scripts\build_release_apk.ps1
```

| Build | Output |
|-------|--------|
| Debug | `app/build/outputs/apk/debug/app-debug.apk` |
| Release | `app/build/outputs/apk/release/app-release.apk` |

**Debug builds:** pre-fill license key via `local.properties` → `bp.license.key=...` (not in release APK).

---

## Sunmi App Store submission

| Asset | Path |
|-------|------|
| Icon 512×512 | `store/icon-512.png` |
| Screenshots 1280×800 | `store/screenshot-01-main.png` … `03-verify.png` |
| Feature graphic 1024×500 | `store/feature-graphic-1024x500.png` |
| Listing copy | `store/LISTING.md` |
| Checklist | `docs/SUNMI_APPSTORE.md` |

Portal: https://partner.sunmi.com

---

## BP payload (live payment)

```json
{
  "event_type": "sunmi_payment",
  "reference_id": "PAY-XXXXXXXX",
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

## Sunmi SDK

| SDK | Purpose | Docs |
|-----|---------|------|
| Payment SDK | Capture real transactions | https://developer.sunmi.com/en-US/ |
| Printer SDK | QR verify receipt | `com.sunmi:printerlibrary` |
| PayLib AAR | Direct payment callbacks | `app/libs/` (see README) |

Official docs: https://docs.sunmi.com/en-US/

---

## Project structure

```
app/src/main/java/com/branchlesspay/auditshield/
├── MainActivity.kt              Home + simulate / flush / history
├── SettingsActivity.kt          License key + privacy policy
├── TransactionHistoryActivity.kt
├── VerifyActivity.kt            WebView verify + share + print QR
├── BpApiClient.kt               BP anchor API + content_hash
├── BpAuditService.kt            Foreground service + payment listener
├── AnchorProcessor.kt           Online anchor + offline queue
├── SqliteAnchorQueue.kt
├── SunmiPaymentCapture.kt       Sunmi broadcast listener
└── SunmiPrinterHelper.kt        QR print
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
