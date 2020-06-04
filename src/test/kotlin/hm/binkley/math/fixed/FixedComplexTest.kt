package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.fixed.FixedBigRational.Companion.ONE
import hm.binkley.math.fixed.FixedBigRational.Companion.TWO
import hm.binkley.math.fixed.FixedBigRational.Companion.ZERO
import hm.binkley.math.fixed.FixedImaginary.Companion.I
import hm.binkley.math.unaryMinus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private val ONE_PLUS_I = ONE + I
private val ONE_MINUS_I = ONE - I
private val NEG_ONE_PLUS_I = -ONE + I
private val NEG_ONE_MINUS_I = -ONE - I

internal class FixedComplexTest {
    @Test
    fun `should destructure`() {
        val (real, imag) = ONE_PLUS_I
        assertEquals(ONE_PLUS_I.real, real)
        assertEquals(ONE_PLUS_I.imag, imag)
    }

    @Test
    fun `should display`() {
        assertEquals("1+1i", ONE_PLUS_I.toString())
        assertEquals("1-1i", ONE_MINUS_I.toString())
    }

    @Test
    fun `should posite`() {
        assertEquals(ONE_PLUS_I, +ONE_PLUS_I)
    }

    @Test
    fun `should negate`() {
        assertEquals(NEG_ONE_MINUS_I, -ONE_PLUS_I)
    }

    @Test
    fun `should add`() {
        assertEquals(ONE_PLUS_I, I + ONE)
        assertEquals(ONE_PLUS_I, BInt.ONE + I)
        assertEquals(ONE_PLUS_I, I + BInt.ONE)
        assertEquals(ONE_PLUS_I, 1L + I)
        assertEquals(ONE_PLUS_I, I + 1L)
        assertEquals(ONE_PLUS_I, 1 + I)
        assertEquals(ONE_PLUS_I, I + 1)
        assertEquals(TWO + 2.i, ONE_PLUS_I + ONE_PLUS_I)
        assertEquals(TWO + I, ONE_PLUS_I + ONE)
        assertEquals(TWO + I, ONE + ONE_PLUS_I)
        assertEquals(TWO + I, ONE_PLUS_I + BInt.ONE)
        assertEquals(TWO + I, BInt.ONE + ONE_PLUS_I)
        assertEquals(TWO + I, ONE_PLUS_I + 1L)
        assertEquals(TWO + I, 1L + ONE_PLUS_I)
        assertEquals(TWO + I, ONE_PLUS_I + 1)
        assertEquals(TWO + I, 1 + ONE_PLUS_I)
        assertEquals(ONE + 2.i, ONE_PLUS_I + I)
        assertEquals(ONE + 2.i, I + ONE_PLUS_I)
    }

    @Test
    fun `should subtract`() {
        assertEquals(-ONE + I, I - ONE)
        assertEquals(ONE_MINUS_I, BInt.ONE - I)
        assertEquals(NEG_ONE_PLUS_I, I - BInt.ONE)
        assertEquals(ONE_MINUS_I, 1L - I)
        assertEquals(NEG_ONE_PLUS_I, I - 1L)
        assertEquals(ONE_MINUS_I, 1 - I)
        assertEquals(NEG_ONE_PLUS_I, I - 1)
        assertEquals(ZERO + 0.i, ONE_PLUS_I - ONE_PLUS_I)
        assertEquals(ZERO + I, ONE_PLUS_I - ONE)
        assertEquals(ONE_MINUS_I, TWO - ONE_PLUS_I)
        assertEquals(0 + I, ONE_PLUS_I - BInt.ONE)
        assertEquals(0 + I, BInt.ONE - ONE_MINUS_I)
        assertEquals(0 + I, ONE_PLUS_I - 1L)
        assertEquals(0 + I, 1L - ONE_MINUS_I)
        assertEquals(0 + I, ONE_PLUS_I - 1)
        assertEquals(0 + I, 1 - ONE_MINUS_I)
        assertEquals(1 + 0.i, ONE_PLUS_I - I)
        assertEquals(-1 + 0.i, I - ONE_PLUS_I)
    }
}
