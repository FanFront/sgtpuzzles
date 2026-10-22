# Publishing signed GitHub Releases

The [Publish Android release](../.github/workflows/release.yml) action publishes
an APK to GitHub Releases when a semantic version tag is pushed (for example,
`v1.0.0`). Only tags pointing to commits in `main` are accepted. The workflow
builds a release APK, runs JVM unit tests, signs the APK, verifies its signature
and package/version, and publishes the APK with SHA-256 checksums.

## Set up signing once

You **must use the same signing key for every subsequent release**. Back up your
keystore securely outside GitHub; never commit it, or its passwords, to this
repository. You can use an existing keystore if you already have APKs installed
that you want to update. To generate a **new** one on your own computer:

```sh
keytool -genkeypair -v -keystore sgtpuzzles-release.jks -alias sgtpuzzles \
  -keyalg RSA -keysize 3072 -validity 10000
```

Then add the following **repository Actions secrets** at
**Settings → Secrets and variables → Actions**:

| Secret | Value |
| --- | --- |
| `ANDROID_RELEASE_KEYSTORE_BASE64` | Single-line Base64 encoding of the .jks file |
| `ANDROID_RELEASE_STORE_PASSWORD` | Keystore password |
| `ANDROID_RELEASE_KEY_ALIAS` | Signing key alias (e.g. `sgtpuzzles`) |
| `ANDROID_RELEASE_KEY_PASSWORD` | Private key password |

On Linux, use `base64 -w 0 sgtpuzzles-release.jks` to encode the file;
on macOS, use `base64 -i sgtpuzzles-release.jks | tr -d '\n'`.
Save the resulting one-line value directly into the secret, **not** in the
source tree or a public issue. If you lose this key, you cannot sign an update
for existing installations signed by it.

## Publish a release

After the workflow is merged into `main` and the signing secrets exist,
choose a fresh version tag, create it on the latest `main` commit, and push:

```sh
git checkout main
git pull --ff-only
git tag v1.0.0
git push origin v1.0.0
```

The version name is `1.0.0` and version code is `1000000` for `v1.0.0`.
In general, `versionCode = major * 1000000 + minor * 1000 + patch`;
minor/patch are 0–999, and the code must exceed 149 and not exceed 2100000000.
Use a **greater** version than the last installed release. The workflow will
reject invalid tags and tags not reachable from `main`.

Find the signed `sgtpuzzles-v1.0.0.apk` at
[GitHub Releases](https://github.com/FanFront/sgtpuzzles/releases). You can also
download it from the Actions run's `sgtpuzzles-v1.0.0` artifact.

## Upgrade / overwrite an installed app (覆盖安装)

Android permits an in-place APK update *without uninstalling and losing app
data* only when all of the following hold:

1. The application ID matches: `name.boyle.chris.sgtpuzzles` (unchanged).
2. The signing certificate matches the installed app's certificate (or a
   valid signing-key rotation is used).
3. The new version code is **at least** the installed version's code.

For example, the same key can sign `v1.0.0`, then `v1.0.1`, and Android
allows the latter to update the former. With adb:

```sh
adb install -r sgtpuzzles-v1.0.1.apk
```

**Important:** A newly created release key cannot replace an APK signed with
the upstream Google Play app-signing key, nor an APK signed by an unrelated
Android Studio/GitHub Actions debug key. Android rejects that update with
`INSTALL_FAILED_UPDATE_INCOMPATIBLE`. The **Google Play upload key** is often
different from the key used to sign APKs delivered by Google Play.

If you don't control the installed app's signing key, your options are to
uninstall it first (back up exported game data beforehand) or to distribute
a separately identified app with a different application ID. The workflow
does not attempt to bypass Android signature checks.

The ordinary Android CI debug APKs are signed with runner-generated debug
keys; those keys can also differ between workflow runs. For reliable
in-place upgrades, install the **signed GitHub Release APKs** and keep
using the same release keystore for future tags.
