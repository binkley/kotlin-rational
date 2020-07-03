package hm.binkley.math.algebra

import hm.binkley.math.algebra.Mod3Int.Companion.ONE
import hm.binkley.math.algebra.Mod3Int.Companion.TWO
import hm.binkley.math.algebra.Mod3Int.Companion.ZERO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

internal class Mod3IntTest {
    @Test
    fun `should have additive zero`() {
        assertSame(ZERO, ZERO.companion.valueOf(0))
        assertEquals(0, Mod3Int.valueOf(0).value)
    }

    @Test
    fun `should have multiplicative one`() {
        assertSame(ONE, ONE.companion.valueOf(1))
        assertEquals(1, Mod3Int.valueOf(1).value)
    }

    @Test
    fun `should handle negative values`() {
        assertEquals(TWO, Mod3Int.valueOf(-1))
    }

    @Test
    fun `should posite`() {
        assertEquals(ONE, +Mod3Int.valueOf(1))
    }

    @Test
    fun `should negate`() {
        assertEquals(TWO, -Mod3Int.valueOf(1))
    }

    @Test
    fun `should add`() {
        assertEquals(ONE, Mod3Int.valueOf(4) + Mod3Int.valueOf(3))
    }

    @Test
    fun `should subtract`() {
        assertEquals(ONE, Mod3Int.valueOf(4) - Mod3Int.valueOf(3))
    }

    @Test
    fun `should multiply`() {
        assertEquals(ZERO, Mod3Int.valueOf(4) * Mod3Int.valueOf(3))
    }

    @Test
    fun `should equal`() {
        assertEquals(ZERO, ZERO)
        assertNotEquals(ZERO, ONE)
        assertNotEquals(ZERO, TWO)
        assertEquals(ONE, ONE)
        assertNotEquals(ONE, TWO)
        assertEquals(TWO, TWO)
    }

    @Test
    fun `should hash`() {
        assertEquals(ZERO.hashCode(), ZERO.hashCode())
        assertNotEquals(ZERO.hashCode(), ONE.hashCode())
        assertNotEquals(ZERO.hashCode(), TWO.hashCode())
        assertEquals(ONE.hashCode(), ONE.hashCode())
        assertNotEquals(ONE.hashCode(), TWO.hashCode())
        assertEquals(TWO.hashCode(), TWO.hashCode())
    }

    @Test
    fun `should pretty print`() {
        assertEquals("0", "$ZERO")
    }
}
