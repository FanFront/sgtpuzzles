# Modern visual system

The shared Android UI now uses a contemporary light/dark palette and Material
Components surfaces. This refresh applies to **all available puzzle games**
because they share the same game chooser, toolbar, status bar and Compose
keyboard. These improvements do not add daily challenges, hints, or statistics.

## Changes

- Responsive rounded game cards, a puzzle-themed icon, and a welcoming chooser.
- Readable descriptions, starred groups, clear current-game selection.
- Material light/dark colors, toolbar/icon contrast and system bar colors.
- Rounded, tactile shared on-screen keys with visible checked/pressed states.
- Spaced gameplay canvas frame and readable game status bar.
- Default native pale-grey (#CCC) puzzle backgrounds are rendered in the new
  light canvas color; all other native engine palette entries and all dark
  engine palette entries remain unchanged to preserve logical color cues.
- Unified responsive CSS for English, German and Chinese local offline help.
- New chooser copy in English and Simplified Chinese.

## Compatibility and scope

Native drawing coordinates, mechanics, gestures, puzzle logic, saved game
identifiers, filenames, and Android application ID have not changed. Puzzle
specific art and engine-drawn symbols are intentionally not replaced by the
design mockups: changing them requires independent work and visual regression
testing for each C engine. Undo/redo and other existing commands still work as
before.

Recommended manual checks: phone and tablet, light and dark mode, list and icon
chooser, starred current game, short/long press, digit/icon keyboard, game
orientation, native puzzle screenshots, English/Chinese help, TalkBack and
minimum 21 Android OS compatibility. CI compiles the debug and release APKs.
