package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.fixed.FixedBigRational.Companion.ONE
import hm.binkley.math.fixed.FixedBigRational.Companion.TWO
import hm.binkley.math.fixed.FixedImaginary.Companion.I
import hm.binkley.math.unaryMinus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class FixedImaginaryTest {
    @Test
    fun `should have value`() {
        assertEquals(ONE, I.value)
    }

    @Test
    fun `should display`() {
        assertEquals("1i", (+I).toString())
        assertEquals("-1i", (-I).toString())
        assertEquals("1i", (ONE.i).toString())
        assertEquals("-1i", ((-ONE).i).toString())
        assertEquals("1i", (BInt.ONE.i).toString())
        assertEquals("-1i", ((-BInt.ONE).i).toString())
        assertEquals("1i", (1L.i).toString())
        assertEquals("-1i", ((-1L).i).toString())
        assertEquals("1i", (1.i).toString())
        assertEquals("-1i", ((-1).i).toString())
    }

    @Test
    fun `should posite`() {
        assertEquals(1.i, +I)
    }

    @Test
    fun `should negate`() {
        assertEquals((-1).i, -I)
    }

    @Test
    fun `should add`() {
        assertEquals(2.i, 1.i + 1.i)
    }

    @Test
    fun `should subtract`() {
        assertEquals(0.i, 1.i - 1.i)
    }

    @Test
    fun `should multiply`() {
        assertEquals(-TWO, 2.i * I)
        assertEquals(2.i, TWO * I)
        assertEquals(2.i, BInt.TWO * I)
        assertEquals(2.i, 2L * I)
        assertEquals(2.i, 2 * I)
    }

    @Test
    fun `should compare`() {
        assertTrue(I < 2.i)
        assertTrue(I <= 2.i)
        assertTrue(I <= I)
        assertTrue(I == I)
        assertTrue(I >= I)
        assertTrue(I >= 0.i)
        assertTrue(I > 0.i)
    }
}
