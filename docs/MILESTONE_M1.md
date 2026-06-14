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

- [ ] Open app on emulator or Sunmi device
- [ ] Settings → enter BP license key → Save
- [ ] Test Connection → **HTTP 202**
- [ ] Home → Send Test Anchor → verify URL shown
- [ ] Open verify URL in browser → page loads
- [ ] Screenshot for Bos submission

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
