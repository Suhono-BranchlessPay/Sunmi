# Sunmi SDK Setup

## Official resources

| Resource | URL |
|----------|-----|
| Developer docs | https://docs.sunmi.com/en-US/ |
| Developer portal | https://developer.sunmi.com/en-US/ |
| Partner portal | https://partner.sunmi.com |

## SDKs (by milestone)

### M1 — No SDK required

Basic anchor uses standard Android + OkHttp only.

### M2 — Payment SDK

1. Register at https://developer.sunmi.com/en-US/
2. Download **Payment SDK** for P-series / V-series
3. Add AAR or Maven dependency to `app/build.gradle.kts`
4. Register transaction callback in `BpAuditService`

### M3 — Printer SDK

```kotlin
implementation("com.sunmi:printerlibrary:1.0.22")
```

Print QR code of verify URL on receipt.

## Emulator testing

Without Sunmi hardware:

- Android Studio emulator API 21+
- Test BP anchor flow (M1)
- Payment/print features require Sunmi device or Sunmi emulator image

## Device models for QA

| Model | Type |
|-------|------|
| T2 mini | Desktop POS |
| V2 Pro | Handheld |
| P2 | Payment terminal |

## Permissions (M2+)

```xml
<uses-permission android:name="com.sunmi.permission.PINPAD_SERVICE" />
<uses-permission android:name="com.sunmi.permission.SECURITY_SERVICE" />
<uses-permission android:name="com.sunmi.peripheral.printer.BIND_SERVICE" />
```
