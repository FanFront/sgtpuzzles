# Keyboard icon contrast fix

The game control keyboard's existing PNG icons were created as opaque white
shapes on transparent backgrounds, because the original keyboard was dark.
After the modern UI redesign introduced a white/light button surface, several
icons (direction arrows, backspace, mouse buttons, game control symbols, etc.)
became visually indistinguishable from their button background.

The shared Compose \`ResIcon\` now applies \`LocalContentColor.current\` to
**monochrome** key icons. The Material button supplies the correct enabled and
disabled foreground colours for light/dark mode.

The three coloured Undead monster icons (\`undead_sym_key_g\`,
\`undead_sym_key_v\`, and \`undead_sym_key_z\`) keep their original full
colour when enabled. When disabled they use the disabled foreground, just like
other controls. This fix does not change the artwork files, button dimensions,
keyboard navigation, accessibility descriptions or game logic.

Automated tests check monochrome keyboard icons, coloured exceptions and the
disabled state. For manual verification on an Android phone, open Solo, Net,
Loopy and Undead, check light/dark mode and enabled/disabled undo/redo arrows
and special buttons.
