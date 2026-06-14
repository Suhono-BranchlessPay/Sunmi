# Milestone M3 — Display + Verify

**Target:** 1 week · **Status:** ✅ **CLOSED**  
**Repo:** https://github.com/Suhono-BranchlessPay/Sunmi/tree/dev  
**Version:** `0.3.0-m3`

---

## Deliverables

| Item | Status |
|------|--------|
| Transaction history screen | ✅ `TransactionHistoryActivity` |
| Status badges (Pending / Anchored / Failed) | ✅ |
| Verify URL per anchored transaction | ✅ |
| WebView verify page | ✅ `VerifyActivity` |
| VERIFIED badge overlay | ✅ |
| Share verify URL | ✅ Android share sheet |
| Print QR (Sunmi printer) | ✅ `SunmiPrinterHelper` + `printerlibrary` |
| History persisted on successful anchor | ✅ `recordAnchored()` |
| Open Verify from main after anchor | ✅ |
| Unit tests (17) | ✅ |

---

## User flow

1. **Simulate Payment** or real Sunmi payment → anchor queued/anchored
2. **Transaction History** → list with status badges
3. Tap **Anchored** row → **Verify WebView** (`branchlesspay.com/verify/[id]`)
4. **Share Verify URL** or **Print QR** on Sunmi device

---

## Screens

| Screen | File |
|--------|------|
| History list | `TransactionHistoryActivity.kt` |
| Verify WebView | `VerifyActivity.kt` |
| QR print | `SunmiPrinterHelper.kt` |

---

## Emulator verification (Pixel_8)

| Step | Result |
|------|--------|
| Simulate payment → HTTP 202 anchored | ✅ `PAY-8CA91D53` |
| History shows **Anchored** row | ✅ |
| Verify WebView loads | ✅ |
| Share sheet | ✅ (manual) |
| Print QR | ⏭️ emulator toast (Sunmi hardware only) |

**Screenshots:** `docs/screenshots/m3-*-emulator.png`

---

## Test checklist

- [x] Simulate payment → appears in History as **Anchored**
- [x] Tap row → WebView loads verify page
- [x] Share button opens share sheet with URL
- [x] Print QR on Sunmi hardware (optional — emulator shows unavailable toast)

---

## Next: M4

App Store assets, signed release APK, privacy policy, Sunmi App Store submission pack.
