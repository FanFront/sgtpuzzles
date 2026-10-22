package name.boyle.chris.sgtpuzzles

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ModernBoardPaletteTest {
    @Test
    fun everyNativePuzzleHasModernColours() {
        assertEquals(40, ModernBoardPalette.supportedGames.size)
        for (game in ModernBoardPalette.supportedGames) {
            val original = IntArray(128) { 0xffcccccc.toInt() }
            val themed = ModernBoardPalette.apply(game, original, false)
            assertNotEquals("$game should have a themed canvas", original[0], themed[0])
            assertEquals("$game should keep native palette length", original.size, themed.size)
            assertTrue("$game palette should remain opaque", themed.all { it ushr 24 == 255 })
        }
    }

    @Test
    fun lightAndDarkThemesHaveDifferentSurfaceColours() {
        val input = IntArray(30) { 0xffcccccc.toInt() }
        val light = ModernBoardPalette.apply("solo", input, false)
        val dark = ModernBoardPalette.apply("solo", input, true)
        assertNotEquals(light[0], dark[0])
        assertNotEquals(light[2], dark[2]) // Grid lines
        assertNotEquals(light[5], dark[5]) // Entered numbers
    }

    @Test
    fun gameplayHighlightsAndErrorsRemainDistinct() {
        val input = IntArray(10) { 0xffcccccc.toInt() }
        val board = ModernBoardPalette.apply("solo", input, false)
        assertNotEquals(board[6], board[7]) // Selection vs mistake
        assertNotEquals(board[4], board[5]) // Clue vs user's numbers
    }

    @Test
    fun tenFloodColoursRemainDistinctInBothThemes() {
        val input = IntArray(14) { 0xff888888.toInt() }
        for (night in listOf(false, true)) {
            val themed = ModernBoardPalette.apply("flood", input, night)
            assertEquals(10, themed.slice(2..11).toSet().size)
        }
    }

    @Test
    fun visualUpdatesDoNotModifyEngineDataOrUnknownIndexes() {
        val original = IntArray(36) { (0xff000000L or it.toLong()).toInt() }
        val copy = original.clone()
        val result = ModernBoardPalette.apply("cube", original, false)
        assertArrayEquals(copy, original)
        assertEquals(original[20], result[20])
        assertArrayEquals(original, ModernBoardPalette.apply("futuregame", original, false))
    }

    @Test
    fun untangleKeepsOuterCanvasSeparateFromPlayArea() {
        val input = IntArray(12) { 0xff777777.toInt() }
        val board = ModernBoardPalette.apply("untangle", input, false)
        assertNotEquals(board[0], board[1])
        assertNotEquals(board[2], board[3])
    }

    @Test
    fun binaryPuzzleCellsStillHaveTwoDistinctStates() {
        val input = IntArray(15) { 0xffaaaaaa.toInt() }
        val result = ModernBoardPalette.apply("unruly", input, false)
        assertNotEquals(result[3], result[6])
        assertNotEquals(result[4], result[3])
        assertNotEquals(result[7], result[6])
    }
}
