# Sunmi App Store — Submission Pack

**App:** BP Audit Shield  
**Package:** `com.branchlesspay.auditshield`  
**Version:** 1.0.0 (versionCode 4)  
**Portal:** https://partner.sunmi.com

---

## Assets checklist

| Asset | Spec | File |
|-------|------|------|
| App icon | 512×512 PNG | `store/icon-512.png` |
| Screenshot 1 | 1280×800 PNG | `store/screenshot-01-main.png` |
| Screenshot 2 | 1280×800 PNG | `store/screenshot-02-history.png` |
| Screenshot 3 | 1280×800 PNG | `store/screenshot-03-verify.png` |
| Feature graphic | 1024×500 PNG | `store/feature-graphic-1024x500.png` |
| Release APK | Signed | `app/build/outputs/apk/release/app-release.apk` |

Regenerate store PNGs from emulator captures:

```powershell
pip install pillow
python scripts/generate_store_assets.py
```

---

## Listing fields

| Field | Value |
|-------|--------|
| Short description | See `store/LISTING.md` (≤80 chars) |
| Full description | See `store/LISTING.md` (≤4000 chars) |
| Privacy policy URL | https://branchlesspay.com/privacy |
| Category | Business / Finance / POS utility |

---

## Release APK build

1. Copy `local.properties.example` → `local.properties`
2. Generate keystore (once):

   ```powershell
   powershell -ExecutionPolicy Bypass -File scripts\generate_release_keystore.ps1
   ```

3. Add keystore passwords to `local.properties` (gitignored)
4. Build:

   ```powershell
   powershell -ExecutionPolicy Bypass -File scripts\build_release_apk.ps1
   ```

Output: `app/build/outputs/apk/release/app-release.apk`

---

## Pre-submission test (Sunmi device or emulator)

- [ ] Install release APK (not debug)
- [ ] Settings → Save license key → Test Connection → HTTP 202
- [ ] Simulate payment → anchored + verify URL
- [ ] Transaction History → Verify WebView → VERIFIED badge
- [ ] Share verify URL
- [ ] Print QR on Sunmi hardware (optional)
- [ ] Reboot device → service auto-starts

---

## Submit on Sunmi Partner Portal

1. Log in at https://partner.sunmi.com
2. Create / update app listing for **BP Audit Shield**
3. Upload icon, feature graphic, and 3+ screenshots
4. Paste short + full description from `store/LISTING.md`
5. Set privacy policy URL: `https://branchlesspay.com/privacy`
6. Upload signed release APK
7. Submit for review

---

## Contact

suhono@branchlesspay.com
