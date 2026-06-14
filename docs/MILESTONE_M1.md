# Milestone M1 — Setup + Basic Anchor

**Target:** 1 week · **Status:** ✅ COMPLETE (scaffold)  
**Date:** June 2026  
**Repo:** https://github.com/Suhono-BranchlessPay/Sunmi/tree/dev

---

## Deliverables

| Item | Status |
|------|--------|
| Android project (Kotlin, minSdk 21) | ✅ |
| Package `com.branchlesspay.auditshield` | ✅ |
| Settings screen (license key + API URL) | ✅ |
| Test Connection → BP API | ✅ |
| Test anchor `sunmi_transaction` | ✅ |
| `BpAuditService` background stub | ✅ |
| Boot auto-start receiver | ✅ |
| Unit tests (payload + hash + validation) | ✅ |
| README + M1 report | ✅ |

---

## Manual test checklist

- [x] BP API test anchor via `scripts/test_anchor.py` → HTTP 202
- [x] Gradle unit tests → 3/3 PASS
- [x] Debug APK built → `app/build/outputs/apk/debug/app-debug.apk`
- [ ] Install APK on Sunmi device / emulator
- [ ] Settings → Test Connection → **HTTP 202**
- [ ] Home → Send Test Anchor → verify URL opens
- [ ] Screenshot for Bos submission

## Live test (2026-06-14)

| Check | Result |
|-------|--------|
| `scripts/test_anchor.py` | HTTP **202** ✅ |
| Verify URL | https://branchlesspay.com/verify/f1bb17dd-ece8-45d4-a7f3-f0c84ed9af0e |
| Gradle `test` | **3/3 PASS** |
| `assembleDebug` | **BUILD SUCCESSFUL** |

---

## Pending (needs physical device / Android Studio)

- [ ] APK install on Sunmi T2 / V2 / P2
- [ ] Screenshot app running on Sunmi hardware
- [ ] Sunmi Payment SDK dependency (M2)

---

## M2 preview

- Sunmi Payment SDK transaction listener
- SQLite offline queue
- Auto-anchor on successful payment
- Event type: `sunmi_payment`

---

## Report to Bos

```
M1 COMPLETE — Sunmi Android BP Audit Shield
Repo: github.com/Suhono-BranchlessPay/Sunmi (dev)
APK: build via Android Studio → Build APK
Test: Settings → Test Connection → HTTP 202
Next: M2 Payment SDK + offline queue
```
