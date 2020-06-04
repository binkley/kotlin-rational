package hm.binkley.math.finite

import hm.binkley.math.BInt
import hm.binkley.math.finite.FixedBigRational.Companion.ONE
import hm.binkley.math.finite.FixedImaginary.Companion.I
import hm.binkley.math.unaryMinus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FixedComplexTest {
    @Test
    fun `should destructure`() {
        val z = ONE + I
        val (real, imag) = z
        assertEquals(z.real, real)
        assertEquals(z.imag, imag)
    }

    @Test
    fun `should display`() {
        assertEquals("1+1i", (ONE + I).toString())
        assertEquals("1-1i", (ONE - I).toString())
    }

    @Test
    fun `should add`() {
        assertEquals(ONE + I, I + ONE)
        assertEquals(ONE + I, BInt.ONE + I)
        assertEquals(ONE + I, I + BInt.ONE)
        assertEquals(ONE + I, 1L + I)
        assertEquals(ONE + I, I + 1L)
        assertEquals(ONE + I, 1 + I)
        assertEquals(ONE + I, I + 1)
    }

    @Test
    fun `should subtract`() {
        assertEquals(-ONE + I, I - ONE)
        assertEquals(ONE - I, BInt.ONE - I)
        assertEquals(-ONE + I, I - BInt.ONE)
        assertEquals(ONE - I, 1L - I)
        assertEquals(-ONE + I, I - 1L)
        assertEquals(ONE - I, 1 - I)
        assertEquals(-ONE + I, I - 1)
    }
}
