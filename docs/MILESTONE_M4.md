# Milestone M4 — Sunmi App Store Ready

**Target:** 1 week · **Status:** ✅ **CLOSED**  
**Repo:** https://github.com/Suhono-BranchlessPay/Sunmi/tree/dev  
**Version:** `1.0.0` (versionCode 4)

---

## Deliverables

| Item | Status |
|------|--------|
| Signed release APK | ✅ `app-release.apk` |
| Release keystore script | ✅ `scripts/generate_release_keystore.ps1` |
| App icon 512×512 | ✅ `store/icon-512.png` |
| Screenshots 1280×800 (×3) | ✅ `store/screenshot-0*.png` |
| Feature graphic 1024×500 | ✅ `store/feature-graphic-1024x500.png` |
| Store listing copy (EN) | ✅ `store/LISTING.md` |
| Privacy policy URL in app | ✅ Settings → Privacy Policy |
| App version in Settings | ✅ |
| Submission pack doc | ✅ `docs/SUNMI_APPSTORE.md` |
| README complete | ✅ |
| Unit tests pass | ✅ |

---

## Polish (M4)

- Removed internal milestone labels from UI strings (M2 tags)
- Settings shows app version + privacy policy link
- Release build excludes debug license key prefill

---

## Sunmi App Store requirements (from instruction doc)

| Requirement | Value |
|-------------|--------|
| Package | `com.branchlesspay.auditshield` |
| Privacy policy | https://branchlesspay.com/privacy |
| Short description | ≤80 chars — see `store/LISTING.md` |
| Full description | English — see `store/LISTING.md` |

---

## Build release APK

```powershell
powershell -ExecutionPolicy Bypass -File scripts\generate_release_keystore.ps1
# Add keystore passwords to local.properties
powershell -ExecutionPolicy Bypass -File scripts\build_release_apk.ps1
```

---

## Project complete (M1–M4)

All four milestones delivered. Ready for Sunmi Partner Portal submission.
