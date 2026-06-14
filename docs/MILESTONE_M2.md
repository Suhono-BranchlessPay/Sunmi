# Milestone M2 — Transaction Capture

**Target:** 1–2 weeks · **Status:** ✅ IN PROGRESS (core complete)  
**Repo:** https://github.com/Suhono-BranchlessPay/Sunmi/tree/dev

---

## Deliverables

| Item | Status |
|------|--------|
| Payment event model (`PaymentEvent`) | ✅ |
| Sunmi payment broadcast listener | ✅ |
| Debug payment simulator (emulator) | ✅ |
| `sunmi_payment` BP payload | ✅ |
| SQLite offline queue | ✅ |
| Max retry 3x | ✅ |
| Foreground `BpAuditService` | ✅ |
| Boot auto-start | ✅ |
| Simulate Payment UI button | ✅ |
| Flush queue button | ✅ |
| Unit tests (10+) | ✅ |

---

## Architecture

```
Payment (Sunmi broadcast / debug simulate)
    → BpAuditService
    → AnchorProcessor
        → online: POST /api/v1/anchor
        → offline/fail: SQLite queue
    → flush on reconnect / manual flush
```

## BP payload (M2)

```json
{
  "event_type": "sunmi_payment",
  "reference_id": "PAY-XXXXXXXX",
  "amount": 25000,
  "currency": "IDR",
  "metadata": {
    "erp": "sunmi_pos",
    "payment_method": "card",
    "device_model": "V2 Pro",
    "device_sn": "..."
  }
}
```

---

## Test on emulator

1. Settings → save BP license key
2. Tap **Simulate Payment (M2)**
3. Expect verify URL or queued offline message
4. Tap **Flush Offline Queue** if pending

```powershell
adb shell am startservice -n com.branchlesspay.auditshield/.BpAuditService -a com.branchlesspay.auditshield.SIMULATE_PAYMENT
```

---

## Sunmi hardware (real device)

On Sunmi devices (`Build.MANUFACTURER = SUNMI`):
- `SunmiPaymentCapture` listens for payment success broadcasts
- Drop PayLib AAR into `app/libs/` for direct SDK (see `app/libs/README.md`)

---

## Pending for M2 sign-off

- [ ] Real payment on Sunmi P2/V2 hardware
- [ ] Verify URL from live `sunmi_payment`
- [ ] Screenshot Simulate Payment → HTTP 202 on emulator

---

## Next: M3

Transaction history, WebView verify page, QR print via Sunmi printer SDK.
