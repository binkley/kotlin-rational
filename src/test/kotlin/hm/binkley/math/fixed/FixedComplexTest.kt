package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.fixed.FixedBigRational.Companion.TWO
import hm.binkley.math.fixed.FixedComplex.Companion.ONE
import hm.binkley.math.fixed.FixedImaginary.Companion.I
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private val ONE_PLUS_I = BRat.ONE + I
private val ONE_MINUS_I = BRat.ONE - I
private val NEG_ONE_PLUS_I = -BRat.ONE + I
private val NEG_ONE_MINUS_I = -BRat.ONE - I

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
    fun `should conjugate`() {
        assertEquals(ONE_MINUS_I, ONE_PLUS_I.conjugate)
    }

    @Test
    fun `should determinate`() {
        assertEquals(8 over 1, (2 + 2.i).det)
    }

    @Test
    fun `should absolve`() {
        assertEquals(5 over 1, (3 + 4.i).absoluteValue)
    }

    @Test
    fun `should reciprocate`() {
        assertEquals((1 over 4) - (1 over 4).i, (2 + 2.i).unaryDiv())
        assertEquals((1 over 4) - (1 over 4).i, (2 + 2.i).reciprocal)
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
        assertEquals(ONE_PLUS_I, I + BRat.ONE)
        assertEquals(ONE_PLUS_I, BInt.ONE + I)
        assertEquals(ONE_PLUS_I, I + BInt.ONE)
        assertEquals(ONE_PLUS_I, 1L + I)
        assertEquals(ONE_PLUS_I, I + 1L)
        assertEquals(ONE_PLUS_I, 1 + I)
        assertEquals(ONE_PLUS_I, I + 1)
        assertEquals(2 + 2.i, ONE_PLUS_I + ONE_PLUS_I)
        assertEquals(2 + 1.i, ONE_PLUS_I + BRat.ONE)
        assertEquals(2 + 1.i, BRat.ONE + ONE_PLUS_I)
        assertEquals(2 + 1.i, ONE_PLUS_I + BInt.ONE)
        assertEquals(2 + 1.i, BInt.ONE + ONE_PLUS_I)
        assertEquals(2 + 1.i, ONE_PLUS_I + 1L)
        assertEquals(2 + 1.i, 1L + ONE_PLUS_I)
        assertEquals(2 + 1.i, ONE_PLUS_I + 1)
        assertEquals(2 + 1.i, 1 + ONE_PLUS_I)
        assertEquals(1 + 2.i, ONE_PLUS_I + I)
        assertEquals(1 + 2.i, I + ONE_PLUS_I)
    }

    @Test
    fun `should subtract`() {
        assertEquals(NEG_ONE_PLUS_I, I - BRat.ONE)
        assertEquals(ONE_MINUS_I, BInt.ONE - I)
        assertEquals(NEG_ONE_PLUS_I, I - BInt.ONE)
        assertEquals(ONE_MINUS_I, 1L - I)
        assertEquals(NEG_ONE_PLUS_I, I - 1L)
        assertEquals(ONE_MINUS_I, 1 - I)
        assertEquals(NEG_ONE_PLUS_I, I - 1)
        assertEquals(0 + 0.i, ONE_PLUS_I - ONE_PLUS_I)
        assertEquals(0 + I, ONE_PLUS_I - BRat.ONE)
        assertEquals(0 + I, BRat.ONE - ONE_MINUS_I)
        assertEquals(0 + I, ONE_PLUS_I - BInt.ONE)
        assertEquals(0 + I, BInt.ONE - ONE_MINUS_I)
        assertEquals(0 + I, ONE_PLUS_I - 1L)
        assertEquals(0 + I, 1L - ONE_MINUS_I)
        assertEquals(0 + I, ONE_PLUS_I - 1)
        assertEquals(0 + I, 1 - ONE_MINUS_I)
        assertEquals(1 + 0.i, ONE_PLUS_I - I)
        assertEquals(-1 + 0.i, I - ONE_PLUS_I)
    }

    @Test
    fun `should multiply`() {
        assertEquals(0 + 2.i, ONE_PLUS_I * ONE_PLUS_I)
        assertEquals(2 + 2.i, TWO * ONE_PLUS_I)
        assertEquals(2 + 2.i, ONE_PLUS_I * TWO)
        assertEquals(2 + 2.i, BInt.TWO * ONE_PLUS_I)
        assertEquals(2 + 2.i, ONE_PLUS_I * BInt.TWO)
        assertEquals(2 + 2.i, 2L * ONE_PLUS_I)
        assertEquals(2 + 2.i, ONE_PLUS_I * 2L)
        assertEquals(2 + 2.i, 2 * ONE_PLUS_I)
        assertEquals(2 + 2.i, ONE_PLUS_I * 2)
        assertEquals(-2 + 2.i, ONE_PLUS_I * 2.i)
        assertEquals(-2 + 2.i, 2.i * ONE_PLUS_I)
    }

    @Test
    fun `should divide`() {
        val half = 1 over 2
        assertEquals(1 + 0.i, ONE_PLUS_I / ONE_PLUS_I)
        assertEquals(half + half.i, TWO / ONE_PLUS_I)
        assertEquals(half + half.i, ONE_PLUS_I / TWO)
        assertEquals(half + half.i, BInt.TWO / ONE_PLUS_I)
        assertEquals(half + half.i, ONE_PLUS_I / BInt.TWO)
        assertEquals(half + half.i, 2L / ONE_PLUS_I)
        assertEquals(half + half.i, ONE_PLUS_I / 2L)
        assertEquals(half + half.i, 2 / ONE_PLUS_I)
        assertEquals(half + half.i, ONE_PLUS_I / 2)
        assertEquals(half - half.i, ONE_PLUS_I / 2.i)
        assertEquals(half - half.i, 2.i / ONE_PLUS_I)
    }

    @Test
    fun `should convert`() {
        val one = ONE + 0.i
        assertEquals(BRat.ONE, one.toBigRational())
        assertEquals(BInt.ONE, one.toBigInteger())
        assertEquals(1L, one.toLong())
        assertEquals(1, one.toInt())
        assertEquals(1.i, (0 + 1.i).toImaginary())
        // Sad paths
        assertThrows<ArithmeticException> { ONE_PLUS_I.toBigRational() }
        assertThrows<ArithmeticException> { ONE_PLUS_I.toBigInteger() }
        assertThrows<ArithmeticException> { ONE_PLUS_I.toLong() }
        assertThrows<ArithmeticException> { ONE_PLUS_I.toInt() }
        assertThrows<ArithmeticException> { ONE_PLUS_I.toImaginary() }
    }

    @Test
    fun `should raise`() {
        val half = 1 over 2
        assertEquals(1 + 0.i, ONE_PLUS_I.pow(0))
        assertEquals(ONE_PLUS_I, ONE_PLUS_I.pow(1))
        assertEquals(half - half.i, ONE_PLUS_I.pow(-1))
        assertEquals(0 + 2.i, ONE_PLUS_I.pow(2))
        assertEquals(0 - half.i, ONE_PLUS_I.pow(-2))
    }
}
