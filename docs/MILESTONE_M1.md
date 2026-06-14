# M1 — OFFICIALLY CLOSED ✅

**Closed:** June 2026  
**Repo:** https://github.com/Suhono-BranchlessPay/Sunmi/tree/dev

---

## Deliverables — ALL COMPLETE

| Item | Status |
|------|--------|
| Kotlin Android app (minSdk 21, `com.branchlesspay.auditshield`) | ✅ |
| Settings + Test Connection | ✅ |
| Test anchor (`sunmi_transaction`) | ✅ |
| BP API HTTP 202 live | ✅ |
| Gradle wrapper + debug APK | ✅ |
| Emulator install (Pixel_8) | ✅ |
| Unit tests | ✅ 3/3 (M1 baseline) |

## Live verification

- API test: `scripts/test_anchor.py` → HTTP **202**
- APK: `app/build/outputs/apk/debug/app-debug.apk`
- Emulator screenshot: `docs/screenshots/m1-main-emulator.png`

---

## Handoff to M2

M1 scope complete. M2 adds Payment SDK capture, SQLite offline queue, and `sunmi_payment` auto-anchor.

See `docs/MILESTONE_M2.md`
