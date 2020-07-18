@file:Suppress("NonAsciiCharacters")

package hm.binkley.math.fixed

import hm.binkley.math.BDouble
import hm.binkley.math.BInt
import hm.binkley.math.`**` // ktlint-disable no-wildcard-imports
import hm.binkley.math.ceil
import hm.binkley.math.compareTo
import hm.binkley.math.div
import hm.binkley.math.divideAndRemainder
import hm.binkley.math.fixed.FixedBigRational.Companion.ONE
import hm.binkley.math.fixed.FixedBigRational.Companion.TEN
import hm.binkley.math.fixed.FixedBigRational.Companion.TWO
import hm.binkley.math.fixed.FixedBigRational.Companion.ZERO
import hm.binkley.math.fixed.FixedBigRational.Companion.cantorSpiral
import hm.binkley.math.floating.FloatingBigRational
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
import hm.binkley.math.sqrtApproximated
import hm.binkley.math.times
import hm.binkley.math.truncate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * NB -- the tests use a mixture of constructors while testing functionality.
 * This is intentional, and raises coverage.
 */
internal class FixedBigRationalTest {
    @Nested
    inner class ConstructionTests {
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
        assertFalse((1 over 2).hashCode() == (1 over 3).hashCode())
    }

    @Test
    fun `should not be a floating big rational`() {
        assertFalse(
            (1 over 1).hashCode() == FloatingBigRational.valueOf(
                BInt.ONE,
                BInt.ONE
            ).hashCode()
        )
        assertFalse(
            ONE..TWO == FloatingBigRational.ONE..FloatingBigRational.TWO
        )
        assertFalse(
            (ONE..TWO).hashCode() ==
                (FloatingBigRational.ONE..FloatingBigRational.TWO)
                    .hashCode()
        )
    }

    @Test
    fun `should pretty print`() {
        assertEquals(
            "0",
            ZERO.toString()
        )
        assertEquals(
            "1⁄2",
            (1 over 2).toString()
        )
        assertEquals(
            "-1⁄2",
            (1 over -2).toString()
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
    }

    @Nested
    inner class OperatorTests {
        @Test
        fun `should do nothing arithmetically`() {
            val rightSideUp = 2 over 3
            val noChange = +rightSideUp

            assertEquals(
                rightSideUp.numerator,
                noChange.numerator
            )
            assertEquals(
                rightSideUp.denominator,
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
        }

        @Test
        fun `should divide`() {
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
        }

        @Test
        fun `should decrement`() {
            var a = ONE
            assertEquals(
                ZERO,
                --a
            )
        }
    }

    @Test
    fun `should note integer rationals`() {
        assertFalse((1 over 2).isInteger())
        assertTrue((2 over 1).isInteger())
        assertTrue(ZERO.isInteger())
    }

    @Test
    fun `should note dyadic rationals`() {
        assertTrue((1 over 2).isDyadic())
        assertTrue((2 over 1).isDyadic())
        assertTrue(ZERO.isDyadic())
        assertFalse((2 over 3).isDyadic())
    }

    @Test
    fun `should note p-adic rationals`() {
        assertTrue((1 over 3).isPAdic(3))
        assertTrue((2 over 1).isPAdic(3))
        assertTrue(ZERO.isPAdic(3))
        assertFalse((2 over 5).isPAdic(3))
    }

    @Test
    fun `should not even denominators`() {
        assertTrue((1 over 2).isDenominatorEven())
        assertFalse((1 over 3).isDenominatorEven())
    }

    @Nested
    inner class RoundingTests {
        @Test
        fun `should round down`() {
            assertEquals(ZERO, ZERO.floor())
            assertEquals(ONE, (ONE).floor())
            assertEquals(-ONE, (-ONE).floor())
            assertEquals(ZERO, (1 over 2).floor())
            assertEquals(-ONE, (-1 over 2).floor())
        }

        @Test
        fun `should round up`() {
            assertEquals(ZERO, ZERO.ceil())
            assertEquals(ONE, (ONE).ceil())
            assertEquals(-ONE, (-ONE).ceil())
            assertEquals(ONE, (1 over 2).ceil())
            assertEquals(ZERO, (-1 over 2).ceil())
        }

        @Test
        fun `should round towards 0`() {
            assertEquals(ZERO, ZERO.truncate())
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
            assertEquals(
                30 over 1,
                BDouble.valueOf(30L).toBigRational()
            )
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
        }

        @Test
        fun `should wrap conversion to Byte`() {
            assertEquals(
                (-128).toByte(),
                ((Byte.MAX_VALUE + 1) over 1).toByte()
            )
        }
    }

    @Nested
    inner class FunctionTests {
        @Test
        fun `should sort`() {
            val sorted = listOf(
                ZERO,
                ZERO
            ).sorted()
            assertEquals(ZERO, sorted[0])
            assertEquals(ZERO, sorted[1])
        }

        @Test
        fun `should compare other number types`() {
            assertTrue(BDouble.ONE > ZERO)
            assertTrue(ONE > BDouble.ZERO)
            assertTrue(1.0 > ZERO)
            assertTrue(ZERO < 1.0)
            assertThrows<ArithmeticException> {
                Double.POSITIVE_INFINITY > ZERO
            }
            assertThrows<ArithmeticException> {
                ZERO < Double.POSITIVE_INFINITY
            }
            assertThrows<ArithmeticException> {
                ZERO > Double.NEGATIVE_INFINITY
            }
            assertThrows<ArithmeticException> {
                Double.NEGATIVE_INFINITY < ZERO
            }
            assertTrue(1.0f > ZERO)
            assertTrue(ZERO < 1.0f)
            assertThrows<ArithmeticException> {
                Float.NaN > ZERO
            }
            assertThrows<ArithmeticException> {
                ZERO < Float.NaN
            }
            assertTrue(BInt.ZERO < ONE)
            assertTrue(ZERO < BInt.ONE)
            assertTrue(0L < ONE)
            assertTrue(ZERO < 1L)
            assertTrue(0 < ONE)
            assertTrue(ZERO < 1)
        }

        @Test
        fun `should multiplicatively invert`() {
            assertEquals(
                -3 over 5,
                (-5 over 3).unaryDiv()
            )
            assertThrows<ArithmeticException> {
                ZERO.unaryDiv()
            }
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
        fun `should square root`() {
            assertEquals(3 over 5, (9 over 25).sqrt())
            assertEquals(3 over 5, (9 over 25).sqrtApproximated())

            assertEquals(
                282_842_712_474_619 over 500_000_000_000_000,
                (8 over 25).sqrtApproximated()
            )
            assertEquals(
                5_883_484_054_145_521 over 10_000_000_000_000_000,
                (9 over 26).sqrtApproximated()
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
            assertTrue(ZERO.mediant(ZERO).isZero())
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
            assertEquals((3245 over 1000), cfA.toBigRational())
            val negCfA = (-3245 over 1000).toContinuedFraction()
            assertEquals(
                listOf(-4 over 1, ONE, 3 over 1, 12 over 1, 4 over 1),
                negCfA
            )
            assertEquals((-3245 over 1000), negCfA.toBigRational())
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
                cantorSpiral().take(10).toList()
            )
        }
    }
}
