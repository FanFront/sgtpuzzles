# Simplified Chinese localisation (work in progress)

The Android UI uses `app/src/main/res/values-zh-rCN/strings.xml`; strings not listed there fall back to English. The initial Chinese UI strings are adapted from [lishaoxia1985/sgtpuzzles](https://github.com/lishaoxia1985/sgtpuzzles) and supplemented for newer UI entries. Only strings with a Chinese translation were imported, to avoid stale English overrides. Keep format placeholders (`%s`, `%d`, `{0}`) unchanged.

The bundled offline help pages in `app/src/main/assets/zh/` are copied from the same fork. `HelpActivity` already checks `resources.configuration.locale.language` (which is `zh` for Simplified Chinese) and chooses a localized page when available, falling back to English otherwise. No online access is needed.

**Limitations:** This is a translation baseline, not complete localisation. Some imported pages still include English paragraphs and may describe older versions of a puzzle. Please review them against `app/src/main/assets/en/` before release. Puzzle engine messages, puzzle names, and certain game configuration options originate in C; `puzzles.h` currently defines `_(x)` as the identity macro, so Android resources alone do not translate all native strings. Native translation requires a separate safe C/JNI strategy.

If regenerating HTML assets, note that `app/src/main/assets-sources/Makefile.doc` currently generates HTML only from available `po/*.po` catalogs. There is not yet a Chinese Halibut/po4a translation source; keep the imported HTML assets until that source is prepared.
