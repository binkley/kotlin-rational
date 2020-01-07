@file:Suppress("NonAsciiCharacters")

package hm.binkley.math

import hm.binkley.math.Rational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.Rational.Companion.NaN
import hm.binkley.math.Rational.Companion.ONE
import hm.binkley.math.Rational.Companion.POSITIVE_INFINITY
import hm.binkley.math.Rational.Companion.TWO
import hm.binkley.math.Rational.Companion.ZERO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigInteger

/**
 * NB -- the tests use a mixture of constructors while testing functionality.
 * This is intentional, and raises coverage.
 */
internal class RationalTest {
    @Test
    fun `should construct NaN`() {
        assertSame(
            NaN,
            0 over 0
        )
    }

    @Test
    fun `should construct +∞`() {
        assertSame(
            POSITIVE_INFINITY,
            Long.MAX_VALUE over 0L
        )
    }

    @Test
    fun `should construct -∞`() {
        assertSame(
            NEGATIVE_INFINITY,
            Long.MIN_VALUE over BigInteger.ZERO
        )
    }

    @Test
    fun `should construct 0`() {
        assertSame(
            ZERO,
            0L over Long.MIN_VALUE
        )
    }

    @Test
    fun `should construct 1`() {
        assertSame(
            ONE,
            BigInteger.ONE over 1
        )
    }

    @Test
    fun `should construct 2`() {
        assertSame(
            TWO,
            BigInteger.TWO over 1
        )
    }

    @Test
    fun `should be a number`() {
        assertEquals(1L.toByte(), (11 over 10).toByte())
        assertEquals(1L.toShort(), (11 over 10).toShort())
        assertEquals(1L.toInt(), (11 over 10).toInt())
        assertEquals(1L, (11 over 10).toLong())
        assertEquals(1.1.toFloat(), (11 over 10).toFloat())
        assertEquals(1.1, (11 over 10).toDouble())
        assertEquals(Double.NaN, NaN.toDouble())
        assertEquals(Double.POSITIVE_INFINITY, POSITIVE_INFINITY.toDouble())
        assertEquals(Double.NEGATIVE_INFINITY, NEGATIVE_INFINITY.toDouble())
    }

    @Test
    fun `should not be a character`() {
        assertThrows<IllegalStateException> {
            ONE.toChar()
        }
    }

    @Test
    fun `should reduce fractions`() {
        assertEquals(
            Rational.new(BigInteger.TWO),
            BigInteger.valueOf(4) over BigInteger.TWO
        )
    }

    @Test
    fun `should simplify fractions`() {
        assertEquals(
            1 over BigInteger.TWO,
            BigInteger.valueOf(4) over 8L
        )
        assertEquals(
            BigInteger.valueOf(2),
            (4L over 8).denominator
        )
    }

    @Test
    fun `should maintain positive denominator`() {
        assertEquals(
            -ONE,
            +(4 over -4L)
        )
        assertEquals(
            -ONE,
            +(-4 over 4L)
        )
        assertEquals(
            ONE,
            (-4 over -4)
        )
    }

    @Test
    fun `should be infinity`() {
        assertTrue((2 over 0).isPositiveInfinity())
        assertTrue((-2 over 0).isNegativeInfinity())
    }

    @Test
    fun `should provide properties`() {
        assertEquals(BigInteger.TWO, (2 over 3).numerator)
        assertEquals(BigInteger.valueOf(3), (2 over 3).denominator)
    }

    @Test
    fun `should be itself`() {
        assertTrue(1 over 2 == 1 over 2)
        assertTrue(ZERO == ZERO)
        assertTrue(ONE == ONE)
    }

    @Test
    fun `should not be itself`() {
        assertFalse(NaN == NaN)
        assertFalse(POSITIVE_INFINITY == POSITIVE_INFINITY)
        assertFalse(NEGATIVE_INFINITY == NEGATIVE_INFINITY)
    }

    @Test
    fun `should hash separately`() {
        assertFalse((1 over 2).hashCode() == (1 over 3).hashCode())
    }

    @Test
    fun `should pretty print`() {
        assertEquals(
            "NaN",
            NaN.toString()
        )
        assertEquals(
            "+∞",
            POSITIVE_INFINITY.toString()
        )
        assertEquals(
            "-∞",
            NEGATIVE_INFINITY.toString()
        )
        assertEquals(
            "0",
            ZERO.toString()
        )
        assertEquals(
            "1/2",
            (1 over 2).toString()
        )
    }

    @Test
    fun `should add`() {
        assertEquals(
            19 over 15,
            (3 over 5) + (2 over 3)
        )
        assertEquals(
            2 over 1,
            BigInteger.ONE + ONE
        )
        assertEquals(
            2 over 1,
            ONE + BigInteger.ONE
        )
        assertEquals(
            2 over 1,
            1L + ONE
        )
        assertEquals(
            2 over 1,
            ONE + 1L
        )
        assertEquals(
            2 over 1,
            1 + ONE
        )
        assertEquals(
            2 over 1,
            ONE + 1
        )
    }

    @Test
    fun `should subtract`() {
        assertEquals(
            -1 over 15,
            (3 over 5) - (2 over 3)
        )
        assertEquals(
            ZERO,
            BigInteger.ONE - ONE
        )
        assertEquals(
            ZERO,
            ONE - BigInteger.ONE
        )
        assertEquals(
            ZERO,
            1L - ONE
        )
        assertEquals(
            ZERO,
            ONE - 1L
        )
        assertEquals(
            ZERO,
            1 - ONE
        )
        assertEquals(
            ZERO,
            ONE - 1
        )
    }

    @Test
    fun `should multiply`() {
        assertEquals(
            2 over 5,
            (3 over 5) * (2 over 3)
        )
        assertEquals(
            ONE,
            BigInteger.ONE * ONE
        )
        assertEquals(
            ONE,
            ONE * BigInteger.ONE
        )
        assertEquals(
            ONE,
            1L * ONE
        )
        assertEquals(
            ONE,
            ONE * 1L
        )
        assertEquals(
            ONE,
            1 * ONE
        )
        assertEquals(
            ONE,
            ONE * 1
        )
    }

    @Test
    fun `should divide`() {
        assertEquals(
            9 over 10,
            (3 over 5) / (2 over 3)
        )
        assertEquals(
            ONE,
            BigInteger.ONE / ONE
        )
        assertEquals(
            ONE,
            ONE / BigInteger.ONE
        )
        assertEquals(
            ONE,
            1L / ONE
        )
        assertEquals(
            ONE,
            ONE / 1L
        )
        assertEquals(
            ONE,
            1 / ONE
        )
        assertEquals(
            ONE,
            ONE / 1
        )
    }

    @Test
    fun `should increment`() {
        var a = Rational.new(1L)
        assertEquals(
            2 over 1,
            ++a
        )
    }

    @Test
    fun `should decrement`() {
        var a = ONE
        assertEquals(
            ZERO,
            --a
        )
    }

    @Test
    fun `should sort`() {
        val sorted = listOf(
            POSITIVE_INFINITY,
            NaN,
            ZERO,
            POSITIVE_INFINITY,
            NaN,
            NEGATIVE_INFINITY,
            ZERO,
            NEGATIVE_INFINITY
        ).sorted()
        assertTrue(sorted[0].isNegativeInfinity())
        assertTrue(sorted[1].isNegativeInfinity())
        assertEquals(ZERO, sorted[2])
        assertEquals(ZERO, sorted[3])
        assertTrue(sorted[4].isPositiveInfinity())
        assertTrue(sorted[5].isPositiveInfinity())
        assertTrue(sorted[6].isNaN())
        assertTrue(sorted[7].isNaN())
    }

    @Test
    fun `should reciprocate`() {
        assertEquals(
            -3 over 5,
            (-5 over 3).reciprocal
        )
        assertTrue(ZERO.reciprocal.isPositiveInfinity())
        assertEquals(
            ZERO,
            POSITIVE_INFINITY.reciprocal
        )
        assertEquals(
            ZERO,
            NEGATIVE_INFINITY.reciprocal
        )
        assertTrue(NaN.reciprocal.isNaN())
    }

    @Test
    fun `should absolute`() {
        assertEquals(
            3 over 5,
            (3 over 5).absoluteValue
        )
        assertEquals(
            3 over 5,
            (-3 over 5).absoluteValue
        )
        assertTrue(NaN.absoluteValue.isNaN())
        assertTrue(POSITIVE_INFINITY.absoluteValue.isPositiveInfinity())
        assertTrue(NEGATIVE_INFINITY.absoluteValue.isPositiveInfinity())
    }

    @Test
    fun `should signum`() {
        assertEquals(
            ONE,
            (3 over 5).sign
        )
        assertEquals(
            ZERO,
            (0 over 5).sign
        )
        assertEquals(
            -ONE,
            (-3 over 5).sign
        )
        assertEquals(
            -ONE,
            NEGATIVE_INFINITY.sign
        )
        assertEquals(
            ONE,
            POSITIVE_INFINITY.sign
        )
        assertTrue(NaN.sign.isNaN())
    }

    @Test
    fun `should raise`() {
        assertEquals(
            9 over 25,
            (3 over 5).pow(2)
        )
        assertEquals(
            ONE,
            (3 over 5).pow(0)
        )
        assertEquals(
            25 over 9,
            (3 over 5).pow(-2)
        )
    }

    @Test
    fun `should progress`() {
        assertEquals(
            listOf(ONE, (2 over 1)),
            ((1 over 1)..(5 over 2)).toList()
        )
        assertEquals(
            listOf(ONE, (3 over 1)),
            (BigInteger.ONE..(3 over 1) step 2).toList()
        )
        assertEquals(
            listOf(ONE, (3 over 1)),
            (ONE..BigInteger.valueOf(3) step 2).toList()
        )
        assertEquals(
            listOf(ONE, (3 over 1)),
            (1L..(3 over 1) step 2L).toList()
        )
        assertEquals(
            listOf(ONE, (3 over 1)),
            (ONE..3L step 2L).toList()
        )
        assertEquals(
            listOf(ONE, (3 over 1)),
            (1..(3 over 1) step (2 over 1)).toList()
        )
        assertEquals(
            listOf(ONE, (3 over 1)),
            (ONE..3 step (2 over 1)).toList()
        )
        assertEquals(
            listOf((2 over 1), ONE),
            ((2 over 1) downTo (1 over 2) step -BigInteger.ONE).toList()
        )
    }

    @Suppress("ControlFlowWithEmptyBody")
    @Test
    fun `should not progress`() {
        assertThrows<IllegalStateException> {
            for (r in ZERO..NaN);
        }
        assertThrows<IllegalStateException> {
            for (r in NaN..ZERO);
        }
        assertThrows<IllegalStateException> {
            for (r in ZERO..ZERO step NaN);
        }
    }

    @Test
    fun `should check finitude`() {
        assertTrue(ZERO.isFinite())
        assertFalse(POSITIVE_INFINITY.isFinite())
        assertFalse(NEGATIVE_INFINITY.isFinite())
        assertFalse(NaN.isFinite())
    }

    @Test
    fun `should check infinitude`() {
        assertFalse(ZERO.isInfinite())
        assertTrue(POSITIVE_INFINITY.isInfinite())
        assertTrue(NEGATIVE_INFINITY.isInfinite())
        assertFalse(NaN.isInfinite())
    }

    @Test
    fun `should propagate NaN`() {
        assertTrue((ZERO + NaN).isNaN())
        assertTrue((NaN + NaN).isNaN())
        assertTrue((NaN + ONE).isNaN())
        assertTrue((NaN - ZERO).isNaN())
        assertTrue((NaN - NaN).isNaN())
        assertTrue((ZERO - NaN).isNaN())
        assertTrue((ONE * NaN).isNaN())
        assertTrue((NaN * NaN).isNaN())
        assertTrue((NaN * ONE).isNaN())
        assertTrue((NaN / ONE).isNaN())
        assertTrue((NaN / NaN).isNaN())
        assertTrue((ONE / NaN).isNaN())
    }

    @Test
    fun `should propagate infinities`() {
        assertTrue((-NEGATIVE_INFINITY).isPositiveInfinity())
        assertTrue((ONE + POSITIVE_INFINITY).isPositiveInfinity())
        assertTrue((NEGATIVE_INFINITY - ONE).isNegativeInfinity())
        assertTrue((POSITIVE_INFINITY + NEGATIVE_INFINITY).isNaN())
        assertTrue((POSITIVE_INFINITY * POSITIVE_INFINITY).isPositiveInfinity())
        assertTrue((POSITIVE_INFINITY * NEGATIVE_INFINITY).isNegativeInfinity())
        assertTrue((NEGATIVE_INFINITY * NEGATIVE_INFINITY).isPositiveInfinity())
        assertTrue((POSITIVE_INFINITY / POSITIVE_INFINITY).isNaN())
        assertTrue((POSITIVE_INFINITY / NEGATIVE_INFINITY).isNaN())
        assertTrue((NEGATIVE_INFINITY / NEGATIVE_INFINITY).isNaN())
    }

    @Test
    fun `should invert infinities incorrectly`() {
        assertEquals(ZERO, ONE / POSITIVE_INFINITY)
        assertEquals(ZERO, ONE / NEGATIVE_INFINITY)
    }

    @Test
    fun `should find GCD (HCF)`() {
        assertEquals(
            2 over 63,
            (2 over 9).gcd(6 over 21)
        )
        assertEquals(
            2 over 63,
            (-2 over 9).gcd(6 over 21)
        )
        assertEquals(
            2 over 63,
            (2 over 9).gcd(-6 over 21)
        )
        assertEquals(
            2 over 63,
            (-2 over 9).gcd(-6 over 21)
        )
        assertEquals(
            (2 over 9),
            ZERO.gcd(2 over 9)
        )
    }

    @Test
    fun `should find LCM (LCD)`() {
        assertEquals(
            2 over 1,
            (2 over 9).lcm(6 over 21)
        )
        assertEquals(
            2 over 1,
            (-2 over 9).lcm(6 over 21)
        )
        assertEquals(
            2 over 1,
            (2 over 9).lcm(-6 over 21)
        )
        assertEquals(
            2 over 1,
            (-2 over 9).lcm(-6 over 21)
        )
        assertEquals(
            ZERO,
            ZERO.lcm(6 over 21)
        )
    }

    @Test
    fun `should note dyadic rationals`() {
        assertTrue((1 over 2).isDyadic())
        assertTrue((2 over 1).isDyadic())
        assertTrue(ZERO.isDyadic())
        assertFalse((2 over 3).isDyadic())
    }

    @Test
    fun `should convert from double`() {
        val doubles = listOf(
            -4.0,
            -3.0,
            -2.0,
            -1.0,
            -0.5,
            -0.3,
            -0.1,
            0.0,
            0.1,
            0.3,
            0.5,
            1.0,
            2.0,
            3.0,
            4.0
        )
        val rationals = listOf(
            -4 over 1,
            -3 over 1,
            -2 over 1,
            -1 over 1,
            -1 over 2,
            -5404319552844595 over 18014398509481984,
            -3602879701896397 over 36028797018963968,
            ZERO,
            3602879701896397 over 36028797018963968,
            5404319552844595 over 18014398509481984,
            1 over 2,
            ONE,
            2 over 1,
            3 over 1,
            4 over 1
        )
        assertTrue(Double.NaN.toRational().isNaN())
        assertTrue(Double.NEGATIVE_INFINITY.toRational().isNegativeInfinity())
        assertTrue(Double.POSITIVE_INFINITY.toRational().isPositiveInfinity())
        assertEquals(
            rationals,
            doubles.map {
                it.toRational()
            })
    }

    @Test
    fun `should convert from float`() {
        val floats = listOf(
            -4.0f,
            -3.0f,
            -2.0f,
            -1.0f,
            -0.5f,
            -0.3f,
            -0.1f,
            0.0f,
            0.1f,
            0.3f,
            0.5f,
            1.0f,
            2.0f,
            3.0f,
            4.0f
        )
        val rationals = listOf(
            -4 over 1,
            -3 over 1,
            -2 over 1,
            -1 over 1,
            -1 over 2,
            -5033165 over 16777216,
            -13421773 over 134217728,
            ZERO,
            13421773 over 134217728,
            5033165 over 16777216,
            1 over 2,
            ONE,
            2 over 1,
            3 over 1,
            4 over 1
        )
        assertTrue(Float.NaN.toRational().isNaN())
        assertTrue(Float.NEGATIVE_INFINITY.toRational().isNegativeInfinity())
        assertTrue(Float.POSITIVE_INFINITY.toRational().isPositiveInfinity())
        assertEquals(
            rationals,
            floats.map {
                it.toRational()
            })
    }
}
