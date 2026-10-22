# Modern styling of native puzzle boards

The in-game visuals are now themed through `ModernBoardPalette.kt`, **not**
just through Android Activity backgrounds. Each of the 40 native puzzle
engines has an explicit table of its C `COL_*` indices. The frontend maps
those semantic indices into modern light/dark palettes when loading a
puzzle's colours. Native game logic, drawing coordinates and save files do
not change.

## Visible changes

- Clear pale / navy board surfaces, structured grid and border contrast
- Cohesive ink, user-entered digits, annotations, error, selection and success
  colours instead of the old black/red/green primary palette
- Updated special pieces: path wires and rails, tent/tree accents, nodes and
  cursors, filled cells and binary puzzle states
- Ten distinct, coordinated logical hues for Flood, Guess, Same Game, Mines;
  Map region colours have dedicated contrast-preserving variants
- Light and dark native-game boards (respect Android night-mode selection)
- Softer antialiased bitmap zoom, rounded joins and thick connecting-line caps,
  and a readable medium-weight variable-width number font
- Fifteen, Sixteen and Twiddle have separately drawn light/dark tile surfaces
  instead of using the old plain-grey board colour

## Keep puzzle rules intact

The palette changes **only RGB drawing values**. It does not alter keyboard,
gestures, hit targets, game states, rules, presets, seeds, timings, or on-disk
saves. Colours without an explicit safe semantic mapping remain exactly as the
C engine provides them (especially ghost/monster types and dynamic Signpost
arrow ranges). Thin 1px grid lines intentionally keep square line ends so that
adjacent cells remain seamlessly connected.

## Upstream maintenance

If a C engine's colour enum changes, update the respective list in
`ModernBoardPalette.kt` and add a regression assertion for that game. The
map is index-based because Android's JNI receives RGB triplets without the
symbolic C names.

## Verification

CI compiles debug and release APKs and runs unit tests for 40-game coverage,
light/dark contrast, unique numbered colours, distinct clue/selection/error
states, map immutability and tile contrast. A real-device visual review is
still advisable for puzzle-specific animations and accessibility (including
colour-blind readability). In particular, test Solo, Loopy, Flood, Mines,
Light Up, Bridges, Map, Fifteen, Twiddle, Net, Untangle, and Pattern.
