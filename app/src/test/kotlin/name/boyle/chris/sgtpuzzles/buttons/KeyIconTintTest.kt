package name.boyle.chris.sgtpuzzles.buttons

import name.boyle.chris.sgtpuzzles.R
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KeyIconTintTest {
    @Test
    fun whiteKeyboardIconsUseThemeForegroundOnLightAndDarkButtons() {
        val monochromeIcons = intArrayOf(
            R.drawable.sym_key_north,
            R.drawable.sym_key_south,
            R.drawable.sym_key_east,
            R.drawable.sym_key_west,
            R.drawable.sym_key_backspace,
            R.drawable.sym_key_mouse_left,
            R.drawable.sym_key_mouse_right,
            R.drawable.square_empty,
            R.drawable.square_filled,
            R.drawable.line,
            R.drawable.lock,
            R.drawable.rotate_left_90,
            R.drawable.square_corner_123
        )
        for (icon in monochromeIcons) {
            assertTrue("Monochrome keyboard art must inherit button content tint",
                shouldTintKeyIcon(icon, enabled = true))
        }
    }

    @Test
    fun gameSpecificFullColourIllustrationsArePreserved() {
        for (icon in intArrayOf(
            R.drawable.undead_sym_key_g,
            R.drawable.undead_sym_key_v,
            R.drawable.undead_sym_key_z
        )) {
            assertFalse("Monster icon should keep its original colours",
                shouldTintKeyIcon(icon, enabled = true))
            assertTrue("Disabled monsters should use the disabled foreground",
                shouldTintKeyIcon(icon, enabled = false))
        }
    }

    @Test
    fun disabledKeyIconsUseDisabledForeground() {
        assertTrue(shouldTintKeyIcon(R.drawable.sym_key_north, enabled = false))
        assertTrue(shouldTintKeyIcon(R.drawable.square_empty, enabled = false))
    }
}
