# Sunmi PayLib (optional — real device)

For direct Payment SDK integration on Sunmi hardware:

1. Download PayLib AAR from https://developer.sunmi.com/en-US/
2. Copy to this folder, e.g. `PayLib-release-1.4.48.aar`
3. Uncomment in `app/build.gradle.kts`:

```kotlin
implementation(files("libs/PayLib-release-1.4.48.aar"))
```

4. Wire SDK callbacks in `SunmiPaymentCapture.kt`

Until AAR is added, payment capture uses Sunmi broadcast intents on real devices and **Simulate Payment** on emulator.
