# Test plan — alpha1

## Prerequisites

- KernelSU Next and a working LSPosed installation supporting **modern API 102**.
- Install a debug APK built by GitHub Actions.
- Enable this module only for \`com.milink.service\`; do not select Android System.
- Keep a backup of the working KernelSU translation module; this APK is independent of it.

## Test procedure

1. Return MiLink's app locale to Turkish (if you previously tested German):

   \`\`\`powershell
   & "C:\platform-tools\adb.exe" shell "cmd locale set-app-locales com.milink.service --user 0 --locales tr-TR"
   \`\`\`

2. Restart MiLink to load LSPosed hooks:

   \`\`\`powershell
   & "C:\platform-tools\adb.exe" shell "am force-stop com.milink.service"
   \`\`\`

3. Open Settings → Interconnectivity and inspect the labels.
4. View the module's non-sensitive logs:

   \`\`\`powershell
   & "C:\platform-tools\adb.exe" logcat -d -v time -s MiLinkTR:V
   \`\`\`

5. Capture only the text of the UI for comparison:

   \`\`\`powershell
   & "C:\platform-tools\adb.exe" shell "uiautomator dump /sdcard/milink_ui.xml"
   \`\`\`

## Expected alpha1 behavior

- \`TextView#setText\` hook logs installed when MiLink starts.
- Recognized English labels under \`ConnectivityActivity\` may become Turkish.
- Xiaomi account, casting connectivity, calls, and notifications remain unaffected.
- Clickable / styled \`Spanned\` text is deliberately not modified.

## If English remains

Check LSPosed module activation, exact package scope, log lines, and actual \`ConnectivityActivity\` class name. If log says hook installed but UI is still English, collect sanitized class and UI text diagnostics. **Do not clear launcher data, reset the device, or modify Xiaomi Account data.**

## Uninstall

Disable this module in LSPosed and restart MiLink. No persistent application setting is written by the module.
