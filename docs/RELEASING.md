# GitHub Releases — no local development environment needed

Everything can be prepared and published in the **GitHub website**. Android
Studio, Java, Gradle, Git, and a local computer are not required.

The release APK keeps application ID `name.boyle.chris.sgtpuzzles`.
It uses a persistent signing key created once by GitHub Actions. Subsequent
releases reuse that exact signing key so Android can upgrade one of **our
GitHub Release APKs** over an earlier version without uninstalling it.

## One-time setup in GitHub

1. Open **Settings → Secrets and variables → Actions → New repository secret**.
   Create a secret named `ANDROID_RELEASE_SIGNING_PASSPHRASE`. Its value must
   be a **unique, randomly generated 24+ character password** (ideally 32+
   characters from a password manager). Keep a secure copy of that password.
   This secret encrypts the long-lived signing key and protects the key itself.
2. Merge [PR #2](https://github.com/FanFront/sgtpuzzles/pull/2) into `main`.
3. Go to **Actions → Initialize release signing key → Run workflow**.
   Select `main` and start it. It securely generates a PKCS12 Android signing
   keystore, encrypts it with AES-256 and PBKDF2, and saves **only the encrypted
   file** in a permanent branch named `android-release-signing`. The plaintext
   signing key and password are never committed or uploaded as artifacts.
   This setup refuses to overwrite an existing signing key.

If GitHub reports insufficient write permission when initializing, review
**Settings → Actions → General → Workflow permissions**. The workflow requests
`contents: write`, which the repository's policies must permit.

**Important:** Keep the `android-release-signing` branch and the secret;
both are needed to sign future updates. Never delete the branch, rotate
the password without re-encrypting the key, or publish the plaintext key.
You can also back up `release-key.p12.enc` using GitHub's web interface;
it is encrypted but still needs the original passphrase to unlock.

## Publish directly from the Actions tab

1. Open **Actions → Publish Android release → Run workflow**.
2. Choose the `main` branch, enter a version such as `v1.0.0`, and click
   **Run workflow**.
3. Wait for the build, tests, signing verification and publish steps to pass.
4. Download `sgtpuzzles-v1.0.0.apk` from
   [GitHub Releases](https://github.com/FanFront/sgtpuzzles/releases).

To upgrade an earlier GitHub Release APK, publish `v1.0.1` using the same
steps and install its APK normally. Android can retain existing application data
because the signing certificate and package ID stay the same.

The workflow also supports publishing when someone pushes a `v*` tag, but
the Actions **Run workflow** button means you never need a terminal or Git.

Versions must be `vMAJOR.MINOR.PATCH`. The build computes
`versionCode = major * 1000000 + minor * 1000 + patch`; minor/patch are
0–999, the version code must exceed 149, and Android's maximum is 2100000000.
Versions must increase for future upgrades.

## Important Android limitations

Android permits an in-place update only if the package ID matches, the
signing certificate matches (or there is a valid signing-key rotation), and
the new version code is compatible.

A newly generated key **cannot overwrite** the upstream Google Play app
or another app signed with a different private key. An old Android Studio or
GitHub Actions debug APK usually uses a different key too. There is no safe
way for GitHub Actions to bypass Android's signature checks.

For those existing installs, either obtain the original app signing key
(and reuse it, which this automated new-key setup does not support) or
uninstall the old app first, ideally after exporting your game data.
After installing your first signed GitHub Release, subsequent releases
from this setup can normally be installed on top of it.
