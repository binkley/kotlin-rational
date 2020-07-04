@file:Suppress("NonAsciiCharacters", "RedundantInnerClassModifier")

package hm.binkley.math.floating

import hm.binkley.math.BDouble
import hm.binkley.math.BInt
import hm.binkley.math.`**` // ktlint-disable no-wildcard-imports
import hm.binkley.math.backAgain
import hm.binkley.math.ceil
import hm.binkley.math.compareTo
import hm.binkley.math.div
import hm.binkley.math.divideAndRemainder
import hm.binkley.math.downTo
import hm.binkley.math.fixed.FixedBigRational
import hm.binkley.math.floating.FloatingBigRational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational.Companion.POSITIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.TEN
import hm.binkley.math.floating.FloatingBigRational.Companion.TWO
import hm.binkley.math.floating.FloatingBigRational.Companion.ZERO
import hm.binkley.math.floor
import hm.binkley.math.gcd
import hm.binkley.math.isDenominatorEven
import hm.binkley.math.isZero
import hm.binkley.math.lcm
import hm.binkley.math.minus
import hm.binkley.math.plus
import hm.binkley.math.rangeTo
import hm.binkley.math.rem
import hm.binkley.math.sqrt
import hm.binkley.math.step
import hm.binkley.math.times
import hm.binkley.math.truncate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private typealias BigRationalAssertion = (FloatingBigRational) -> Unit

/**
 * NB -- the tests use a mixture of constructors while testing functionality.
 * This is intentional, and raises coverage.
 */
internal class FloatingBigRationalTest {
    @Nested
    inner class ConstructionTests {
        @Test
        fun `should construct NaN`() {
            assertSame(
                NaN,
                0 over 0
            )
        }

        @Test
        fun `should construct Infinity`() {
            assertSame(
                POSITIVE_INFINITY,
                Long.MAX_VALUE over 0L
            )
        }

        @Test
        fun `should construct -Infinity`() {
            assertSame(
                NEGATIVE_INFINITY,
                Long.MIN_VALUE over BInt.ZERO
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
                BInt.ONE over 1
            )
            assertSame(
                ONE,
                -1 over -1
            )
        }

        @Test
        fun `should construct 2`() {
            assertSame(
                TWO,
                BInt.TWO over 1
            )
            assertSame(
                TWO,
                -2 over -1
            )
        }

        @Test
        fun `should construct 10`() {
            assertSame(
                TEN,
                BInt.TEN over 1
            )
            assertSame(
                TEN,
                -10 over -1
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
            assertEquals(
                Double.POSITIVE_INFINITY,
                POSITIVE_INFINITY.toDouble()
            )
            assertEquals(
                Double.NEGATIVE_INFINITY,
                NEGATIVE_INFINITY.toDouble()
            )
            assertEquals(
                Long.MAX_VALUE,
                POSITIVE_INFINITY.toLong()
            )
            assertEquals(
                Long.MIN_VALUE,
                NEGATIVE_INFINITY.toLong()
            )
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
                2 over 1,
                BInt.valueOf(4) over BInt.TWO
            )
        }

        @Test
        fun `should simplify fractions`() {
            assertEquals(
                1 over BInt.TWO,
                BInt.valueOf(4) over 8L
            )
            assertEquals(
                BInt.valueOf(2),
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
    }

    @Suppress("ReplaceCallWithBinaryOperator")
    @Test
    fun `should be itself`() {
        assertTrue(1 over 2 == 1 over 2)
        assertTrue(1 over 2 == +(1 over 2))
        assertTrue(1 over 2 == -(-(1 over 2)))
        assertTrue(ZERO == ZERO)
        assertTrue(ONE == ONE)
        assertFalse(ZERO.equals(0))
        assertFalse((2 over 3) == (2 over 5))
    }

    @Test
    fun `should hash separately`() {
        assertNotEquals((1 over 2).hashCode(), (1 over 3).hashCode())
        assertNotEquals(
            POSITIVE_INFINITY.hashCode(), NEGATIVE_INFINITY.hashCode()
        )
    }

    @Test
    fun `should not be a floating big rational`() {
        assertFalse(
            (1 over 1).hashCode() == FixedBigRational.valueOf(
                BInt.ONE,
                BInt.ONE
            ).hashCode()
        )
        assertFalse(
            ONE..TWO == FixedBigRational.ONE..FixedBigRational.TWO
        )
        assertFalse(
            (ONE..TWO).hashCode() ==
                (FixedBigRational.ONE..FixedBigRational.TWO).hashCode()
        )
    }

    @Test
    fun `should pretty print`() {
        assertEquals(
            "0",
            ZERO.toString()
        )
        assertEquals(
            "2",
            (2 over 1).toString()
        )
        assertEquals(
            "1⁄2",
            (1 over 2).toString()
        )
        assertEquals(
            "-1⁄2",
            (1 over -2).toString()
        )
        assertEquals(
            "Infinity",
            POSITIVE_INFINITY.toString()
        )
        assertEquals(
            "-Infinity",
            NEGATIVE_INFINITY.toString()
        )
        assertEquals(
            "NaN",
            NaN.toString()
        )
    }

    @Test
    fun `should be ℚ-ish`() {
        val twoThirds = 2 over 3
        val threeHalves = 3 over 2
        val fiveSevenths = 5 over 7

        // Associativity
        assertEquals(
            twoThirds + (threeHalves + fiveSevenths),
            (twoThirds + threeHalves) + fiveSevenths
        )
        assertEquals(
            twoThirds * (threeHalves * fiveSevenths),
            (twoThirds * threeHalves) * fiveSevenths
        )

        // Commutativity
        assertEquals(
            twoThirds + threeHalves,
            threeHalves + twoThirds
        )
        assertEquals(
            twoThirds * threeHalves,
            threeHalves * twoThirds
        )

        // Identities
        assertEquals(
            twoThirds,
            twoThirds + ZERO
        )
        assertEquals(
            twoThirds,
            twoThirds * ONE
        )

        // Inverses
        assertEquals(
            ZERO,
            twoThirds + -twoThirds
        )
        assertEquals(
            ONE,
            twoThirds * twoThirds.unaryDiv()
        )

        // Distributive
        assertEquals(
            twoThirds * fiveSevenths + threeHalves * fiveSevenths,
            (twoThirds + threeHalves) * fiveSevenths
        )

        assertEquals(
            ZERO,
            ONE + -ONE
        )
    }

    @Test
    fun `should divide with remainder`() {
        assertEquals(
            (2 over 1) to (1 over 2),
            (13 over 2).divideAndRemainder(3 over 1)
        )
        assertEquals(
            (2 over 1) to (-1 over 2),
            (-13 over 2).divideAndRemainder(-3 over 1)
        )
        assertEquals(
            (-2 over 1) to (-1 over 2),
            (-13 over 2).divideAndRemainder(3 over 1)
        )
        assertEquals(
            (-2 over 1) to (1 over 2),
            (13 over 2).divideAndRemainder(-3 over 1)
        )

        fun nonFiniteCheck(
            dividend: FloatingBigRational,
            divisor: FloatingBigRational,
            assertion: BigRationalAssertion
        ) {
            val (quotient, remainder) = dividend.divideAndRemainder(divisor)
            assertion(quotient)
            assertion(remainder)
        }

        nonFiniteCheck((13 over 2), NaN) {
            it.isNaN()
        }
        nonFiniteCheck(NaN, (3 over 1)) {
            it.isNaN()
        }
        nonFiniteCheck(NaN, NaN) {
            it.isNaN()
        }
        nonFiniteCheck((13 over 2), POSITIVE_INFINITY) {
            it.isPositiveInfinity()
        }
        nonFiniteCheck(POSITIVE_INFINITY, (3 over 1)) {
            it.isPositiveInfinity()
        }
        nonFiniteCheck(POSITIVE_INFINITY, POSITIVE_INFINITY) {
            it.isPositiveInfinity()
        }
        nonFiniteCheck((13 over 2), NEGATIVE_INFINITY) {
            it.isNegativeInfinity()
        }
        nonFiniteCheck(NEGATIVE_INFINITY, (3 over 1)) {
            it.isNegativeInfinity()
        }
        nonFiniteCheck(NEGATIVE_INFINITY, NEGATIVE_INFINITY) {
            it.isPositiveInfinity()
        }
    }

    @Nested
    inner class OperatorTests {
        @Test
        fun `should do nothing arithmetically`() {
            val rightsideUp = 2 over 3
            val noChange = +rightsideUp

            assertEquals(
                rightsideUp.numerator,
                noChange.numerator
            )
            assertEquals(
                rightsideUp.denominator,
                noChange.denominator
            )
        }

        @Test
        fun `should invert arithmetically`() {
            val rightsideUp = 2 over 3
            val upsideDown = -rightsideUp

            assertEquals(
                rightsideUp.numerator.negate(),
                upsideDown.numerator
            )
            assertEquals(
                rightsideUp.denominator,
                upsideDown.denominator
            )
        }

        @Test
        fun `should invert multiplicatively`() {
            val rightsideUp = 2 over 3
            val upsideDown = rightsideUp.unaryDiv()

            assertEquals(
                rightsideUp.denominator,
                upsideDown.numerator
            )
            assertEquals(
                rightsideUp.numerator,
                upsideDown.denominator
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
                BDouble.ONE + ONE
            )
            assertEquals(
                11 over 1,
                ONE + BDouble.TEN
            )
            assertEquals(
                2 over 1,
                1.0 + ONE
            )
            assertEquals(
                2 over 1,
                ONE + 1.0
            )
            assertEquals(
                2 over 1,
                1.0f + ONE
            )
            assertEquals(
                2 over 1,
                ONE + 1.0f
            )
            assertEquals(
                2 over 1,
                BInt.ONE + ONE
            )
            assertEquals(
                2 over 1,
                ONE + BInt.ONE
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
            assertTrue((ONE + POSITIVE_INFINITY).isPositiveInfinity())
            assertTrue(
                (POSITIVE_INFINITY + POSITIVE_INFINITY).isPositiveInfinity()
            )
            assertTrue((POSITIVE_INFINITY + NEGATIVE_INFINITY).isNaN())
            assertTrue((ONE + NEGATIVE_INFINITY).isNegativeInfinity())
            assertTrue(
                (NEGATIVE_INFINITY + NEGATIVE_INFINITY).isNegativeInfinity()
            )
            assertTrue((NEGATIVE_INFINITY + POSITIVE_INFINITY).isNaN())
        }

        @Test
        fun `should subtract`() {
            assertEquals(
                -1 over 15,
                (3 over 5) - (2 over 3)
            )
            assertEquals(
                ZERO,
                BDouble.ONE - ONE
            )
            assertEquals(
                ZERO,
                ONE - BDouble.ONE
            )
            assertEquals(
                ZERO,
                1.0 - ONE
            )
            assertEquals(
                ZERO,
                ONE - 1.0
            )
            assertEquals(
                ZERO,
                1.0f - ONE
            )
            assertEquals(
                ZERO,
                ONE - 1.0f
            )
            assertEquals(
                ZERO,
                BInt.ONE - ONE
            )
            assertEquals(
                ZERO,
                ONE - BInt.ONE
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
            assertTrue((POSITIVE_INFINITY - ONE).isPositiveInfinity())
            assertTrue((POSITIVE_INFINITY - POSITIVE_INFINITY).isNaN())
            assertTrue(
                (POSITIVE_INFINITY - NEGATIVE_INFINITY).isPositiveInfinity()
            )
            assertTrue((NEGATIVE_INFINITY - ONE).isNegativeInfinity())
            assertTrue((NEGATIVE_INFINITY - NEGATIVE_INFINITY).isNaN())
            assertTrue(
                (NEGATIVE_INFINITY - POSITIVE_INFINITY).isNegativeInfinity()
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
                BDouble.ONE * ONE
            )
            assertEquals(
                ONE,
                ONE * BDouble.ONE
            )
            assertEquals(
                ONE,
                1.0 * ONE
            )
            assertEquals(
                ONE,
                ONE * 1.0
            )
            assertEquals(
                ONE,
                1.0f * ONE
            )
            assertEquals(
                ONE,
                ONE * 1.0f
            )
            assertEquals(
                ONE,
                BInt.ONE * ONE
            )
            assertEquals(
                ONE,
                ONE * BInt.ONE
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
            assertTrue((ONE * POSITIVE_INFINITY).isPositiveInfinity())
            assertTrue((ONE * NEGATIVE_INFINITY).isNegativeInfinity())
            assertTrue((ZERO * POSITIVE_INFINITY).isNaN())
            assertTrue((POSITIVE_INFINITY * ZERO).isNaN())
            assertTrue((ZERO * NEGATIVE_INFINITY).isNaN())
            assertTrue((NEGATIVE_INFINITY * ZERO).isNaN())
            assertTrue(
                (POSITIVE_INFINITY * POSITIVE_INFINITY).isPositiveInfinity()
            )
        }

        @Test
        fun `should divide`() {
            assertTrue((ONE / NaN).isNaN())
            assertTrue((ZERO / ZERO).isNaN())
            assertEquals(
                ZERO,
                ZERO / POSITIVE_INFINITY
            )
            assertTrue((ONE / ZERO).isPositiveInfinity())
            assertTrue((POSITIVE_INFINITY / POSITIVE_INFINITY).isNaN())
            assertEquals(
                ZERO,
                ZERO / NEGATIVE_INFINITY
            )
            assertTrue((-ONE / ZERO).isNegativeInfinity())
            assertTrue((NEGATIVE_INFINITY / NEGATIVE_INFINITY).isNaN())
            assertEquals(
                9 over 10,
                (3 over 5) / (2 over 3)
            )
            assertEquals(
                ONE,
                BDouble.ONE / ONE
            )
            assertEquals(
                ONE,
                ONE / BDouble.ONE
            )
            assertEquals(
                ONE,
                1.0 / ONE
            )
            assertEquals(
                ONE,
                ONE / 1.0
            )
            assertEquals(
                ONE,
                1.0f / ONE
            )
            assertEquals(
                ONE,
                ONE / 1.0f
            )
            assertEquals(
                ONE,
                BInt.ONE / ONE
            )
            assertEquals(
                ONE,
                ONE / BInt.ONE
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
        fun `should find remainder`() {
            assertTrue((ONE % NaN).isNaN())
            assertEquals(
                ZERO,
                ONE % POSITIVE_INFINITY
            )
            assertEquals(
                ZERO,
                ONE % NEGATIVE_INFINITY
            )

            assertEquals(
                ZERO,
                (3 over 5) % (2 over 3)
            )
            assertEquals(
                ZERO,
                BDouble.ONE % ONE
            )
            assertEquals(
                ZERO,
                ONE % BDouble.ONE
            )
            assertEquals(
                ZERO,
                1.0 % ONE
            )
            assertEquals(
                ZERO,
                ONE % 1.0
            )
            assertEquals(
                ZERO,
                1.0f % ONE
            )
            assertEquals(
                ZERO,
                ONE % 1.0f
            )
            assertEquals(
                ZERO,
                BInt.ONE % ONE
            )
            assertEquals(
                ZERO,
                ONE % BInt.ONE
            )
            assertEquals(
                ZERO,
                1L % ONE
            )
            assertEquals(
                ZERO,
                ONE % 1L
            )
            assertEquals(
                ZERO,
                1 % ONE
            )
            assertEquals(
                ZERO,
                ONE % 1
            )
        }

        @Test
        fun `should increment`() {
            var a = 1L.toBigRational()
            assertEquals(
                2 over 1,
                ++a
            )
            var nonFinite = NaN
            assertTrue((++nonFinite).isNaN())
            nonFinite = POSITIVE_INFINITY
            assertTrue((++nonFinite).isPositiveInfinity())
            nonFinite = NEGATIVE_INFINITY
            assertTrue((++nonFinite).isNegativeInfinity())
        }

        @Test
        fun `should decrement`() {
            var a = ONE
            assertEquals(
                ZERO,
                --a
            )
            var nonFinite = NaN
            assertTrue((--nonFinite).isNaN())
            nonFinite = POSITIVE_INFINITY
            assertTrue((--nonFinite).isPositiveInfinity())
            nonFinite = NEGATIVE_INFINITY
            assertTrue((--nonFinite).isNegativeInfinity())
        }
    }

    @Nested
    inner class SpecialCasesTests {
        @Test
        fun `should round trip as double precision`() {
            assertEquals(
                0.0,
                0.0.toBigRational().toDouble()
            )
            assertEquals(
                1.0,
                1.0.toBigRational().toDouble()
            )
            assertEquals(
                0.1,
                0.1.toBigRational().toDouble()
            )
            assertEquals(
                Double.NaN,
                Double.NaN.toBigRational().toDouble()
            )
            assertEquals(
                Double.MAX_VALUE,
                Double.MAX_VALUE.toBigRational().toDouble()
            )
            assertEquals(
                Double.MIN_VALUE,
                Double.MIN_VALUE.toBigRational().toDouble()
            )
        }

        @Test
        fun `should round trip as single precision`() {
            assertEquals(
                Float.NaN,
                Float.NaN.toBigRational().toFloat()
            )
            assertEquals(
                Float.MAX_VALUE,
                Float.MAX_VALUE.toBigRational().toFloat()
            )
            assertEquals(
                Float.MIN_VALUE,
                Float.MIN_VALUE.toBigRational().toFloat()
            )
            assertEquals(
                0.0f,
                0.0f.toBigRational().toFloat()
            )
            assertEquals(
                1.0f,
                1.0f.toBigRational().toFloat()
            )
            assertEquals(
                0.1f,
                0.1f.toBigRational().toFloat()
            )
        }

        @Test
        fun `should be infinity`() {
            assertTrue((2 over 0).isPositiveInfinity())
            assertTrue((-2 over 0).isNegativeInfinity())
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
            assertTrue(
                (POSITIVE_INFINITY * POSITIVE_INFINITY).isPositiveInfinity()
            )
            assertTrue(
                (POSITIVE_INFINITY * NEGATIVE_INFINITY).isNegativeInfinity()
            )
            assertTrue(
                (NEGATIVE_INFINITY * NEGATIVE_INFINITY).isPositiveInfinity()
            )
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
        fun `should cope with various infinities`() {
            assertTrue((ZERO * POSITIVE_INFINITY).isNaN())
            assertEquals(ZERO, ZERO / POSITIVE_INFINITY)
            assertTrue((POSITIVE_INFINITY / ZERO).isPositiveInfinity())
            assertTrue((ZERO * NEGATIVE_INFINITY).isNaN())
            assertEquals(ZERO, ZERO / NEGATIVE_INFINITY)
            assertTrue((NEGATIVE_INFINITY / ZERO).isNegativeInfinity())
            assertTrue(
                (POSITIVE_INFINITY * NEGATIVE_INFINITY).isNegativeInfinity()
            )
            assertTrue((POSITIVE_INFINITY / NEGATIVE_INFINITY).isNaN())
        }

        @Test
        fun `should understand equalities of non-finite values`() {
            assertEquals(POSITIVE_INFINITY, POSITIVE_INFINITY)
            assertEquals(NEGATIVE_INFINITY, NEGATIVE_INFINITY)
            assertNotEquals(NaN, NaN)
        }
    }

    @Test
    fun `should note integer rationals`() {
        assertFalse((1 over 2).isInteger())
        assertTrue((2 over 1).isInteger())
        assertTrue(ZERO.isInteger())
        assertFalse(POSITIVE_INFINITY.isInteger())
        assertFalse(NEGATIVE_INFINITY.isInteger())
        assertFalse(NaN.isInteger())
    }

    @Test
    fun `should note dyadic rationals`() {
        assertTrue((1 over 2).isDyadic())
        assertTrue((2 over 1).isDyadic())
        assertTrue(ZERO.isDyadic())
        assertFalse((2 over 3).isDyadic())
        assertFalse(POSITIVE_INFINITY.isDyadic())
        assertFalse(NEGATIVE_INFINITY.isDyadic())
        assertFalse(NaN.isDyadic())
    }

    @Test
    fun `should note p-adic rationals`() {
        assertTrue((1 over 3).isPAdic(3))
        assertTrue((2 over 1).isPAdic(3))
        assertTrue(ZERO.isPAdic(3))
        assertFalse((2 over 5).isPAdic(3))
        assertFalse(POSITIVE_INFINITY.isPAdic(3))
        assertFalse(NEGATIVE_INFINITY.isPAdic(3))
        assertFalse(NaN.isPAdic(3))
    }

    @Test
    fun `should note even denominators`() {
        assertTrue((1 over 2).isDenominatorEven())
        assertFalse((1 over 3).isDenominatorEven())
    }

    @Nested
    inner class RoundingTests {
        @Test
        fun `should round down`() {
            assertEquals(ZERO, ZERO.floor())
            assertTrue(NaN.floor().isNaN())
            assertTrue(POSITIVE_INFINITY.floor().isPositiveInfinity())
            assertTrue(NEGATIVE_INFINITY.floor().isNegativeInfinity())
            assertEquals(ONE, (ONE).floor())
            assertEquals(-ONE, (-ONE).floor())
            assertEquals(ZERO, (1 over 2).floor())
            assertEquals(-ONE, (-1 over 2).floor())
        }

        @Test
        fun `should round up`() {
            assertEquals(ZERO, ZERO.ceil())
            assertTrue(NaN.ceil().isNaN())
            assertTrue(POSITIVE_INFINITY.ceil().isPositiveInfinity())
            assertTrue(NEGATIVE_INFINITY.ceil().isNegativeInfinity())
            assertEquals(ONE, (ONE).ceil())
            assertEquals(-ONE, (-ONE).ceil())
            assertEquals(ONE, (1 over 2).ceil())
            assertEquals(ZERO, (-1 over 2).ceil())
        }

        @Test
        fun `should round towards 0`() {
            assertEquals(ZERO, ZERO.truncate())
            assertTrue(NaN.truncate().isNaN())
            assertTrue(POSITIVE_INFINITY.truncate().isPositiveInfinity())
            assertTrue(NEGATIVE_INFINITY.truncate().isNegativeInfinity())
            assertEquals(ONE, (ONE).truncate())
            assertEquals(-ONE, (-ONE).truncate())
            assertEquals(ZERO, (1 over 2).truncate())
            assertEquals(ZERO, (-1 over 2).truncate())
        }
    }

    @Nested
    inner class ConversionTests {
        @Test
        fun `should convert BigDecimal in infix constructor`() {
            assertEquals(ZERO, BDouble.ZERO.toBigRational())
            assertEquals(30 over 1, BDouble.valueOf(30L).toBigRational())
            assertEquals(3 over 1, BDouble.valueOf(3).toBigRational())
            assertEquals(3 over 10, BDouble("0.3").toBigRational())
            assertEquals(77 over 10, BDouble("7.70").toBigRational())
            assertEquals(ONE, BDouble.ONE over BDouble.ONE)
            assertEquals(ONE, BInt.ONE over BDouble.ONE)
            assertEquals(ONE, 1L over BDouble.ONE)
            assertEquals(ONE, 1 over BDouble.ONE)
            assertEquals(ONE, 1.0 over BDouble.ONE)
            assertEquals(ONE, 1.0f over BDouble.ONE)

            assertEquals(ONE, BDouble.ONE over 1L)
            assertEquals(ONE, BDouble.ONE over 1)
        }

        @Test
        fun `should convert BigInteger in infix constructor`() {
            assertEquals(ZERO, BInt.ZERO.toBigRational())
            assertEquals(30 over 1, BInt.valueOf(30L).toBigRational())
            assertEquals(3 over 1, BInt.valueOf(3).toBigRational())
            assertEquals(ONE, BInt.ONE over BInt.ONE)
            assertEquals(ONE, BDouble.ONE over BInt.ONE)
            assertEquals(ONE, 1L over BInt.ONE)
            assertEquals(ONE, 1 over BInt.ONE)
            assertEquals(ONE, 1.0 over BInt.ONE)
            assertEquals(ONE, 1.0f over BInt.ONE)

            assertEquals(ONE, BInt.ONE over 1L)
            assertEquals(ONE, BInt.ONE over 1)
        }

        @Test
        fun `should convert double in infix constructor`() {
            assertEquals(ONE, BDouble.ONE over 1.0)
            assertEquals(ONE, BInt.ONE over 1.0)
            assertEquals(ONE, 1L over 1.0)
            assertEquals(ONE, 1 over 1.0)
            assertEquals(ONE, 1.0 over 1.0)
            assertEquals(ONE, 1.0f over 1.0)

            assertEquals(ONE, 1.0 over BInt.ONE)
            assertEquals(ONE, 1.0 over 1L)
            assertEquals(ONE, 1.0 over 1)
        }

        @Test
        fun `should convert float in infix constructor`() {
            assertEquals(ONE, BDouble.ONE over 1.0f)
            assertEquals(ONE, BInt.ONE over 1.0f)
            assertEquals(ONE, 1L over 1.0f)
            assertEquals(ONE, 1 over 1.0f)
            assertEquals(ONE, 1.0 over 1.0f)
            assertEquals(ONE, 1.0f over 1.0f)

            assertEquals(ONE, 1.0f over BInt.ONE)
            assertEquals(ONE, 1.0f over 1L)
            assertEquals(ONE, 1.0f over 1)
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
                4.0,
                123.456
            )
            val rationals = listOf(
                -4 over 1,
                -3 over 1,
                -2 over 1,
                -1 over 1,
                -1 over 2,
                -3 over 10,
                -1 over 10,
                ZERO,
                1 over 10,
                3 over 10,
                1 over 2,
                ONE,
                2 over 1,
                3 over 1,
                4 over 1,
                15432 over 125
            )
            assertTrue(Double.NaN.toBigRational().isNaN())
            assertTrue(
                Double.NEGATIVE_INFINITY.toBigRational().isNegativeInfinity()
            )
            assertTrue(
                Double.POSITIVE_INFINITY.toBigRational().isPositiveInfinity()
            )
            assertEquals(
                rationals,
                doubles.map {
                    it.toBigRational()
                }
            )
            assertEquals(
                doubles,
                rationals.map {
                    it.toDouble()
                }
            )
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
                4.0f,
                123.456f
            )
            val rationals = listOf(
                -4 over 1,
                -3 over 1,
                -2 over 1,
                -1 over 1,
                -1 over 2,
                -3 over 10,
                -1 over 10,
                ZERO,
                1 over 10,
                3 over 10,
                1 over 2,
                ONE,
                2 over 1,
                3 over 1,
                4 over 1,
                15432 over 125
            )
            assertTrue(Float.NaN.toBigRational().isNaN())
            assertTrue(
                Float.NEGATIVE_INFINITY.toBigRational().isNegativeInfinity()
            )
            assertTrue(
                Float.POSITIVE_INFINITY.toBigRational().isPositiveInfinity()
            )
            assertEquals(
                rationals,
                floats.map {
                    it.toBigRational()
                }
            )
            assertEquals(
                floats,
                rationals.map {
                    it.toFloat()
                }
            )
        }

        @Test
        fun `should wrap conversion to Long`() {
            assertEquals(
                (-128).toByte(),
                ((Byte.MAX_VALUE + 1) over 1).toByte()
            )
            assertEquals(0b0, NaN.toByte())
        }

        @Test
        fun `should wrap conversion to Byte`() {
            assertEquals(
                (-128).toByte(),
                ((Byte.MAX_VALUE + 1) over 1).toByte()
            )
            assertEquals(0b0, NaN.toByte())
        }
    }

    @Nested
    inner class ProgressionTests {
        @Suppress("ReplaceCallWithBinaryOperator")
        @Test
        fun `should be itself`() {
            val zeroToOne = ZERO..ONE
            assertEquals(zeroToOne, zeroToOne)
            assertEquals(
                zeroToOne,
                ZERO..ONE
            )
            assertFalse(zeroToOne.equals(ZERO))
            assertEquals(
                zeroToOne,
                zeroToOne step ONE
            )
            assertNotEquals(
                zeroToOne step ONE,
                zeroToOne step TWO
            )
            assertNotEquals(
                zeroToOne,
                ZERO..TWO
            )
            assertEquals(
                (zeroToOne step (1 over 2)).hashCode(),
                (zeroToOne step (1 over 2)).hashCode()
            )
        }

        @Test
        fun `should pretty print`() {
            assertEquals(
                "0..1 step 1",
                "${(ZERO..ONE)}"
            )
            assertEquals(
                "0..1 step 2",
                "${(ZERO..ONE step 2)}"
            )
            assertEquals(
                "1 downTo 0 step -1",
                "${(ONE downTo ZERO)}"
            )
            assertEquals(
                "1 downTo 0 step -2",
                "${(1 downTo ZERO step -TWO)}"
            )
        }

        @Test
        fun `should progress`() {
            assertTrue(((ZERO..(-ONE)).isEmpty()))
            assertTrue((ZERO..TWO).contains(ONE))
            assertEquals(
                listOf(ONE, (2 over 1)),
                ((1 over 1)..(5 over 2)).toList()
            )
            val three = 3 over 1
            assertEquals(
                listOf(ONE, three),
                (BDouble.ONE..three step 2).toList()
            )
            assertEquals(
                listOf(ONE, three),
                (ONE..BDouble.valueOf(3) step 2).toList()
            )
            assertEquals(
                listOf(ONE, three),
                (1.0..three step 2).toList()
            )
            assertEquals(
                listOf(ONE, three),
                (ONE..3.0 step 2).toList()
            )
            assertEquals(
                listOf(ONE, three),
                (1.0f..three step 2).toList()
            )
            assertEquals(
                listOf(ONE, three),
                (ONE..3.0f step 2).toList()
            )
            assertEquals(
                listOf(ONE, three),
                (BInt.ONE..three step 2).toList()
            )
            assertEquals(
                listOf(ONE, three),
                (ONE..BInt.valueOf(3) step 2).toList()
            )
            assertEquals(
                listOf(ONE, three),
                (1L..three step 2L).toList()
            )
            assertEquals(
                listOf(ONE, three),
                (ONE..3L step 2L).toList()
            )
            assertEquals(
                listOf(ONE, three),
                (1..three step (2 over 1)).toList()
            )
            assertEquals(
                listOf(ONE, three),
                (ONE..3 step (2 over 1)).toList()
            )
            assertEquals(
                listOf((2 over 1), ONE),
                ((2 over 1) downTo (1 over 2) step -BInt.ONE).toList()
            )
        }

        @Suppress("ControlFlowWithEmptyBody")
        @Test
        fun `should not progress`() {
            val noop = { }

            assertThrows<IllegalStateException> {
                for (r in ZERO..NaN) noop()
            }
            assertThrows<IllegalStateException> {
                for (r in NaN..ZERO) noop()
            }
            assertThrows<IllegalStateException> {
                for (r in ZERO..ZERO step NaN) noop()
            }
            assertThrows<IllegalStateException> {
                for (r in ZERO..ONE step -1) noop()
            }
            assertThrows<IllegalStateException> {
                for (r in BInt.ONE downTo ZERO step 1); noop()
            }
            assertThrows<IllegalStateException> {
                for (r in 0L downTo ONE step ZERO); noop()
            }
        }
    }

    @Nested
    inner class FunctionTests {
        @Test
        fun `should sort like double`() {
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
            val doubleSorted = listOf(
                Double.POSITIVE_INFINITY,
                Double.NaN,
                0.0,
                Double.POSITIVE_INFINITY,
                Double.NaN,
                Double.NEGATIVE_INFINITY,
                0.0,
                Double.NEGATIVE_INFINITY
            ).sorted()

            assertEquals(doubleSorted, sorted.map { it.toDouble() })
        }

        @Test
        fun `should compare other number types`() {
            assertTrue(BDouble.ONE > ZERO)
            assertTrue(ONE > BDouble.ZERO)
            assertTrue(Double.POSITIVE_INFINITY > ZERO)
            assertTrue(ZERO > Double.NEGATIVE_INFINITY)
            assertTrue(Float.NaN > ZERO)
            assertTrue(NaN > Float.MAX_VALUE)
            assertTrue(BInt.ZERO < ONE)
            assertTrue(ZERO < BInt.ONE)
            assertTrue(0L < ONE)
            assertTrue(ZERO < 1L)
            assertTrue(0 < ONE)
            assertTrue(ZERO < 1)
        }

        @Test
        fun `should not order NaN values`() {
            assertFalse(NaN == NaN)
            assertFalse(NaN > NaN)
            assertFalse(NaN < NaN)
        }

        @Test
        fun `should reciprocate`() {
            assertEquals(
                -3 over 5,
                (-5 over 3).unaryDiv()
            )
            assertTrue(ZERO.unaryDiv().isPositiveInfinity())
            assertEquals(
                ZERO,
                POSITIVE_INFINITY.unaryDiv()
            )
            assertEquals(
                ZERO,
                NEGATIVE_INFINITY.unaryDiv()
            )
            assertTrue(NaN.unaryDiv().isNaN())
        }

        @Test
        fun `should absolute`() {
            assertEquals(
                ZERO,
                ZERO.absoluteValue
            )
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
                (3 over 5) `**` 2
            )
            assertEquals(
                ONE,
                (3 over 5) `**` 0
            )
            assertEquals(
                25 over 9,
                (3 over 5) `**` -2
            )
        }

        @Test
        fun `should root`() {
            assertEquals(
                3 over 5,
                (9 over 25).sqrt()
            )
            assertThrows<ArithmeticException> { (8 over 25).sqrt() }
            assertThrows<ArithmeticException> { (9 over 26).sqrt() }
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
            assertEquals(
                ZERO,
                ZERO.gcd(ZERO)
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
            assertEquals(
                ZERO,
                ZERO.lcm(ZERO)
            )
        }

        @Test
        fun `should find between`() {
            assertTrue(NaN.mediant(NaN).isNaN())
            assertTrue(NaN.mediant(POSITIVE_INFINITY).isNaN())
            assertTrue(
                POSITIVE_INFINITY.mediant(POSITIVE_INFINITY)
                    .isPositiveInfinity()
            )
            assertTrue(NaN.mediant(NEGATIVE_INFINITY).isNaN())
            assertTrue(
                NEGATIVE_INFINITY.mediant(NEGATIVE_INFINITY)
                    .isNegativeInfinity()
            )
            assertTrue(NaN.mediant(ZERO).isNaN())
            assertTrue(ZERO.mediant(NaN).isNaN())
            assertTrue(POSITIVE_INFINITY.mediant(NaN).isNaN())
            assertTrue(NEGATIVE_INFINITY.mediant(NaN).isNaN())
            assertTrue(ZERO.mediant(ZERO).isZero())
            assertEquals(
                ZERO,
                POSITIVE_INFINITY.mediant(NEGATIVE_INFINITY)
            )
            assertEquals(
                ZERO,
                NEGATIVE_INFINITY.mediant(POSITIVE_INFINITY)
            )
            assertEquals(
                ONE,
                POSITIVE_INFINITY.mediant(ZERO)
            )
            assertEquals(
                ONE,
                ZERO.mediant(POSITIVE_INFINITY)
            )
            assertEquals(
                -ONE,
                ZERO.mediant(NEGATIVE_INFINITY)
            )
            assertEquals(
                -ONE,
                NEGATIVE_INFINITY.mediant(ZERO)
            )
            assertEquals(
                3 over 2,
                ONE.mediant(TWO)
            )
        }

        @Test
        fun `should find continued fraction`() {
            val cfA = (3245 over 1000).toContinuedFraction()
            assertEquals(
                listOf(3 over 1, 4 over 1, 12 over 1, 4 over 1),
                cfA
            )
            assertTrue(cfA.isFinite())
            assertEquals((3245 over 1000), cfA.backAgain())
            val negCfA = (-3245 over 1000).toContinuedFraction()
            assertEquals(
                listOf(-4 over 1, ONE, 3 over 1, 12 over 1, 4 over 1),
                negCfA
            )
            assertTrue(negCfA.isFinite())
            assertEquals((-3245 over 1000), negCfA.backAgain())
            assertEquals(
                listOf(ZERO),
                ZERO.toContinuedFraction()
            )
            assertEquals(
                listOf(ONE),
                ONE.toContinuedFraction()
            )
            assertEquals(
                listOf(ZERO, 3 over 1),
                (1 over 3).toContinuedFraction()
            )

            val cfNaN = NaN.toContinuedFraction()
            assertFalse(cfNaN.isFinite())
            assertTrue(cfNaN.backAgain().isNaN())
            assertTrue(cfNaN.integerPart.isNaN())
            val cfPosInf = POSITIVE_INFINITY.toContinuedFraction()
            assertFalse(cfPosInf.isFinite())
            assertTrue(cfPosInf.backAgain().isNaN())
            assertTrue(cfPosInf.integerPart.isNaN())
            val cfNegInf = NEGATIVE_INFINITY.toContinuedFraction()
            assertFalse(cfNegInf.isFinite())
            assertTrue(cfNegInf.backAgain().isNaN())
            assertTrue(cfNegInf.integerPart.isNaN())
        }
    }

    @Nested
    inner class CantorSpiral {
        @Test
        fun `should find Cantor spiral`() {
            assertEquals(
                listOf(
                    ZERO,
                    ONE,
                    -ONE,
                    -1 over 2,
                    1 over 2,
                    TWO,
                    -TWO,
                    -2 over 3,
                    -1 over 3,
                    1 over 3
                ),
                FloatingBigRational.cantorSpiral().take(10).toList()
            )
        }
    }
}
