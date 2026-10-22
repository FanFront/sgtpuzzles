package name.boyle.chris.sgtpuzzles

/**
 * Semantic colour tokens for every native puzzle, applied at the JNI -> Canvas boundary.
 *
 * The native game engines continue to supply their original logical palette indices.
 * This layer changes only the colours associated with those indices; it never changes
 * game logic, state, save formats, hit targets, drawing coordinates or animation timing.
 *
 * Colour names below mirror the C COL_* enums (in declaration order). Keep these in
 * sync with app/src/main/jni/<game>.c when updating upstream puzzle engines.
 * Alias constants and computed colour ranges are deliberately omitted; any index
 * beyond the supplied names retains its original colour.
 */
internal object ModernBoardPalette {
    private fun names(s: String) = s.trim().split(Regex("\\s+"))

    private val nativeColourNames = mapOf(
        "blackbox" to names("BACKGROUND COVER LOCK TEXT FLASHTEXT HIGHLIGHT LOWLIGHT GRID BALL BALL_REVEALED WRONG BUTTON CURSOR"),
        "bridges" to names("BACKGROUND FOREGROUND HIGHLIGHT LOWLIGHT SELECTED MARK HINT GRID WARNING CURSOR"),
        "cube" to names("BACKGROUND BORDER BLUE"),
        "dominosa" to names("BACKGROUND TEXT DOMINO DOMINOCLASH DOMINOTEXT EDGE CURSOR DOMINOCURSOR HIGHLIGHT_1 HIGHLIGHT_2"),
        "fifteen" to names("BACKGROUND TEXT HIGHLIGHT LOWLIGHT TILE"),
        "filling" to names("BACKGROUND GRID HIGHLIGHT CORRECT ERROR USER CURSOR"),
        "flip" to names("BACKGROUND WRONG RIGHT GRID DIAG HINT CURSOR"),
        "flood" to names("BACKGROUND SEPARATOR 1 2 3 4 5 6 7 8 9 10 HIGHLIGHT LOWLIGHT"),
        "galaxies" to names("BACKGROUND WHITEBG BLACKBG WHITEDOT BLACKDOT GRID EDGE ARROW CURSOR"),
        "guess" to names("BACKGROUND FRAME CURSOR FLASH HOLD EMPTY 1 2 3 4 5 6 7 8 9 10 CORRECTPLACE CORRECTCOLOUR"),
        "inertia" to names("BACKGROUND OUTLINE HIGHLIGHT LOWLIGHT PLAYER DEAD_PLAYER MINE GEM WALL HINT"),
        "keen" to names("BACKGROUND GRID BLOCK_BORDER USER HIGHLIGHT ERROR PENCIL"),
        "lightup" to names("BACKGROUND GRID BLACK LIGHT LIT ERROR CURSOR"),
        "loopy" to names("BACKGROUND FOREGROUND LINEUNKNOWN HIGHLIGHT MISTAKE SATISFIED CURSOR FAINT"),
        "magnets" to names("BACKGROUND HIGHLIGHT LOWLIGHT TEXT ERROR CURSOR DONE NEUTRAL NEGATIVE POSITIVE NOT"),
        "map" to names("BACKGROUND GRID 0 1 2 3 ERROR ERRTEXT TEXT_ON_FILLED"),
        "mines" to names("BACKGROUND BACKGROUND2 1 2 3 4 5 6 7 8 MINE BANG CROSS FLAG FLAGBASE QUERY HIGHLIGHT LOWLIGHT WRONGNUMBER CURSOR"),
        // TEXT_DARK/TEXT_LIGHT are aliases for MARKED/BLANK and not extra indices.
        "mosaic" to names("BACKGROUND UNMARKED GRID MARKED BLANK TEXT_SOLVED ERROR CURSOR"),
        "net" to names("BACKGROUND LOCKED BORDER WIRE ENDPOINT POWERED BARRIER ERR"),
        "netslide" to names("BACKGROUND FLASHING BORDER WIRE ENDPOINT POWERED BARRIER LOWLIGHT TEXT"),
        // CLUE and LINE_YES are aliases of GRID.
        "palisade" to names("BACKGROUND FLASH GRID LINE_MAYBE LINE_NO ERROR"),
        "pattern" to names("BACKGROUND EMPTY FULL TEXT UNKNOWN GRID CURSOR ERROR CURSOR_GUIDE"),
        // CURSOR_BACKGROUND is an alias of LOWLIGHT.
        "pearl" to names("BACKGROUND HIGHLIGHT LOWLIGHT BLACK WHITE ERROR GRID FLASH DRAGON DRAGOFF"),
        "pegs" to names("BACKGROUND HIGHLIGHT LOWLIGHT PEG CURSOR"),
        // BLACK/TEXT/USER alias GRID, CURSOR aliases LOWLIGHT.
        "range" to names("BACKGROUND GRID ERROR LOWLIGHT"),
        "rect" to names("BACKGROUND CORRECT LINE TEXT GRID DRAG DRAGERASE CURSOR"),
        "samegame" to names("BACKGROUND 1 2 3 4 5 6 7 8 9 IMPOSSIBLE SEL HIGHLIGHT LOWLIGHT"),
        // The B0/M0/D0/X0 ranges are computed colours; leave them unmodified.
        "signpost" to names("BACKGROUND HIGHLIGHT LOWLIGHT GRID CURSOR ERROR DRAG_ORIGIN ARROW ARROW_BG_DIM EMPTY NUMBER0 NUMBER NUMBER_SET NUMBER_SET_MID"),
        "singles" to names("BACKGROUND UNUSED1 LOWLIGHT BLACK WHITE BLACKNUM GRID CURSOR ERROR"),
        "sixteen" to names("BACKGROUND TEXT HIGHLIGHT LOWLIGHT TILE"),
        "slant" to names("BACKGROUND GRID INK SLANT1 SLANT2 ERROR CURSOR FILLEDSQUARE GROUNDED"),
        "solo" to names("BACKGROUND XDIAGONALS GRID GRID_BLOCK CLUE USER HIGHLIGHT ERROR PENCIL KILLER"),
        "tents" to names("BACKGROUND GRID GRASS TREETRUNK TREELEAF TENT ERROR ERRTEXT ERRTRUNK"),
        "towers" to names("BACKGROUND GRID USER HIGHLIGHT ERROR PENCIL DONE"),
        "tracks" to names("BACKGROUND TRACK_BACKGROUND GRID CLUE CURSOR TRACK TRACK_CLUE SLEEPER DRAGON DRAGOFF ERROR FLASH ERROR_BACKGROUND"),
        "twiddle" to names("BACKGROUND TEXT HIGHLIGHT HIGHLIGHT_GENTLE LOWLIGHT LOWLIGHT_GENTLE HIGHCURSOR LOWCURSOR TILE"),
        "undead" to names("BACKGROUND GRID TEXT ERROR HIGHLIGHT FLASH MONSTER_OUTLINE MONSTER_EYES GHOST ZOMBIE VAMPIRE DONE"),
        // SPENT is an alias for LOWLIGHT.
        "unequal" to names("BACKGROUND GRID TEXT GUESS ERROR PENCIL HIGHLIGHT LOWLIGHT"),
        // n_HIGHLIGHT and n_LOWLIGHT belong to their respective tile states.
        "unruly" to names("BACKGROUND GRID EMPTY 0 0_HIGHLIGHT 0_LOWLIGHT 1 1_HIGHLIGHT 1_LOWLIGHT CURSOR ERROR"),
        // The outer background is distinct from the playable surface.
        "untangle" to names("SYSBACKGROUND BACKGROUND LINE CROSSEDLINE OUTLINE POINT DRAGPOINT CURSORPOINT NEIGHBOUR FLASH1 FLASH2")
    )

    internal val supportedGames: Set<String> get() = nativeColourNames.keys

    // Opaque colours only. Alpha should always remain 255 for native blitter
    // compositing, state snapshots and zooming.
    private fun rgb(hex: Int) = 0xff000000.toInt() or hex

    private data class Theme(
        val canvas: Int,
        val outer: Int,
        val subduedSurface: Int,
        val surface: Int,
        val ink: Int,
        val mutedInk: Int,
        val grid: Int,
        val boldGrid: Int,
        val border: Int,
        val accent: Int,
        val accentSoft: Int,
        val cyan: Int,
        val success: Int,
        val successSoft: Int,
        val warning: Int,
        val error: Int,
        val errorSoft: Int,
        val highlight: Int,
        val lowlight: Int,
        val filled: Int,
        val paleFill: Int
    )

    private val light = Theme(
        canvas = rgb(0xF7F9FD), outer = rgb(0xEDF2F9),
        subduedSurface = rgb(0xEEF3FA), surface = rgb(0xFFFFFF),
        ink = rgb(0x26364F), mutedInk = rgb(0x62758F),
        grid = rgb(0x93A3B7), boldGrid = rgb(0x526783), border = rgb(0x405571),
        accent = rgb(0x316FD8), accentSoft = rgb(0xDBE9FF),
        cyan = rgb(0x168CB9), success = rgb(0x138B76), successSoft = rgb(0xD8F6EB),
        warning = rgb(0xB7791F), error = rgb(0xD4415A), errorSoft = rgb(0xFDE6E8),
        highlight = rgb(0xFFFFFF), lowlight = rgb(0xC0CFDF),
        filled = rgb(0x24344B), paleFill = rgb(0xE8EFF8)
    )
    private val dark = Theme(
        canvas = rgb(0x18263A), outer = rgb(0x101A2A),
        subduedSurface = rgb(0x223249), surface = rgb(0x25384E),
        ink = rgb(0xEEF5FF), mutedInk = rgb(0xB2C3D9),
        grid = rgb(0x8297B1), boldGrid = rgb(0xC2D0E3), border = rgb(0xAEC2DB),
        accent = rgb(0x91BBFF), accentSoft = rgb(0x29496C),
        cyan = rgb(0x69D6EF), success = rgb(0x65DEC1), successSoft = rgb(0x225546),
        warning = rgb(0xFFD16E), error = rgb(0xFF8092), errorSoft = rgb(0x683846),
        highlight = rgb(0x405A78), lowlight = rgb(0x293F56),
        filled = rgb(0x0E1B2C), paleFill = rgb(0x344B63)
    )

    // Distinct puzzle colours, not a single monochrome filter. Adjacent
    // Flood / Guess / Same Game tiles and Mines' numbered clues must remain
    // distinguishable at a glance.
    private val lightPlayColours = intArrayOf(
        0xDF5868, 0xE0AF37, 0x159B79, 0x3B78D0, 0xEB8738,
        0x9673D8, 0xA5725D, 0x31A6BB, 0x8FAD37, 0xD370B0
    ).map(::rgb).toIntArray()
    private val darkPlayColours = intArrayOf(
        0xFF7D8B, 0xFFDB70, 0x54D5A8, 0x84B4FF, 0xFFC080,
        0xC6A0FF, 0xDEA795, 0x65D7EC, 0xB6DB74, 0xFFA8D4
    ).map(::rgb).toIntArray()
    private val indexedColourGames = setOf("flood", "guess", "samegame", "mines", "map")
    private val softHighlightGames = setOf(
        "solo", "keen", "towers", "unequal", "loopy",
        "filling", "slant", "undead", "tracks", "bridges"
    )

    /**
     * Returns a copy of the native colour array. Unrecognised backends or
     * palette indices are kept unchanged for forwards compatibility.
     */
    fun apply(game: String, original: IntArray, isNight: Boolean): IntArray {
        val names = nativeColourNames[game] ?: return original.copyOf()
        val palette = if (isNight) dark else light
        val numbered = if (isNight) darkPlayColours else lightPlayColours
        return original.copyOf().also { result ->
            names.take(result.size).forEachIndexed { index, name ->
                val mapped: Int? = when {
                    game in indexedColourGames && name.toIntOrNull() != null -> {
                        val number = name.toInt()
                        val slot = if (game == "map") number + 2 else number - 1
                        numbered.getOrNull(slot)
                    }
                    else -> semanticColour(game, name, palette)
                }
                if (mapped != null) result[index] = mapped
            }
        }
    }

    private fun semanticColour(game: String, name: String, p: Theme): Int? = when (name) {
        "BACKGROUND" -> p.canvas
        "SYSBACKGROUND" -> p.outer
        "BACKGROUND2", "TRACK_BACKGROUND", "WHITEBG" -> p.subduedSurface
        "BLACKBG" -> p.filled
        "EMPTY", "RIGHT", "BLANK", "WHITE", "LIGHT", "TILE" -> p.surface
        "MARKED", "FULL", "BLACK", "DOMINO", "WALL", "FILLEDSQUARE" -> p.filled
        "UNMARKED", "NEUTRAL", "GRASS" -> p.paleFill
        "0" -> if (game == "unruly") p.filled else null
        "1" -> if (game == "unruly") p.surface else null
        "0_HIGHLIGHT", "1_HIGHLIGHT" -> p.highlight
        "0_LOWLIGHT", "1_LOWLIGHT" -> p.lowlight
        "TREELEAF" -> p.success
        "TENT", "FLAG", "POSITIVE" -> p.error
        "NEGATIVE" -> p.ink
        "LIT" -> if (p == dark) rgb(0x886C25) else rgb(0xFFF0B0)

        "GRID", "SEPARATOR", "EDGE", "FAINT", "LINE_MAYBE", "DIAG",
        "XDIAGONALS", "SLEEPER", "LOCK", "COVER" -> p.grid
        "GRID_BLOCK", "BLOCK_BORDER", "OUTLINE", "BORDER", "FRAME",
        "TRACK_CLUE", "CLUE", "KILLER" -> p.boldGrid
        "TEXT", "INK", "FOREGROUND", "BLACKNUM", "TEXT_SOLVED", "NUMBER",
        "NUMBER0", "NUMBER_SET", "ERRTEXT", "TEXT_ON_FILLED",
        "ARROW", "LINE", "WIRE", "TREETRUNK" -> p.ink
        "PENCIL", "LINE_NO", "ARROW_BG_DIM", "NUMBER_SET_MID",
        "QUERY", "LOWLIGHT_GENTLE", "LOWLIGHT", "DONE" ->
            if (name == "DONE") p.success else if (name == "LOWLIGHT") p.lowlight else p.mutedInk
        "HIGHLIGHT", "HIGHLIGHT_GENTLE" ->
            if (game in softHighlightGames) p.accentSoft else p.highlight

        "CURSOR", "CURSORPOINT", "DOMINOCURSOR", "HIGHCURSOR",
        "LOWCURSOR", "SELECTED", "SEL", "DRAG", "DRAG_ORIGIN",
        "DRAGPOINT", "HOLD" -> p.accentSoft
        "CURSOR_GUIDE", "FLASH", "FLASH1", "FLASH2",
        "FLASHING", "FLASHTEXT", "POINT", "NEIGHBOUR" -> p.cyan
        "LINEUNKNOWN", "HINT", "MARK" -> p.warning
        "USER", "GUESS", "SLANT1", "TRACK", "BLUE" -> p.accent
        "SLANT2", "POWERED", "PLAYER", "CORRECT", "SATISFIED",
        "GROUNDED", "DRAGON", "DRAGOFF" ->
            if (name == "CORRECT") p.successSoft else p.success
        "ERROR", "WARNING", "MISTAKE", "DOMINOCLASH", "ERR",
        "CROSSEDLINE", "DEAD_PLAYER", "WRONGNUMBER" -> p.error
        "ERROR_BACKGROUND" -> p.errorSoft
        // Preserve puzzle-specific symbol colours (e.g. positive vs negative
        // magnets, ghosts vs vampires, track sleepers, object/ball values)
        // when no safe semantic replacement exists.
        else -> null
    }
}
