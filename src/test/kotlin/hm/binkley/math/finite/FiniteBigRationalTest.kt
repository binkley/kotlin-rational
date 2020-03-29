@file:Suppress("NonAsciiCharacters")

package hm.binkley.math.finite

import hm.binkley.math.finite.FiniteBigRational.Companion.ONE
import hm.binkley.math.finite.FiniteBigRational.Companion.TEN
import hm.binkley.math.finite.FiniteBigRational.Companion.TWO
import hm.binkley.math.finite.FiniteBigRational.Companion.ZERO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.math.BigInteger

private typealias BInt = BigInteger
private typealias BDouble = BigDecimal

/**
 * NB -- the tests use a mixture of constructors while testing functionality.
 * This is intentional, and raises coverage.
 */
internal class FiniteBigRationalTest {
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
            twoThirds * twoThirds.reciprocal
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
            val upsideDown = rightsideUp.reciprocal

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
                2 over 1,
                ONE + BDouble.ONE
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
            var a = 1L.toFiniteBigRational()
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
            assertEquals(ZERO, ZERO.round())
            assertEquals(ONE, (ONE).round())
            assertEquals(-ONE, (-ONE).round())
            assertEquals(ZERO, (1 over 2).round())
            assertEquals(ZERO, (-1 over 2).round())
        }
    }

    @Nested
    inner class ConversionTests {
        @Test
        fun `should convert BigDecimal in infix constructor`() {
            assertEquals(ZERO, BDouble.ZERO.toFiniteBigRational())
            assertEquals(
                30 over 1,
                BDouble.valueOf(30L).toFiniteBigRational()
            )
            assertEquals(3 over 1, BDouble.valueOf(3).toFiniteBigRational())
            assertEquals(3 over 10, BDouble("0.3").toFiniteBigRational())
            assertEquals(77 over 10, BDouble("7.70").toFiniteBigRational())
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
            assertEquals(ZERO, BInt.ZERO.toFiniteBigRational())
            assertEquals(30 over 1, BInt.valueOf(30L).toFiniteBigRational())
            assertEquals(3 over 1, BInt.valueOf(3).toFiniteBigRational())
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
                -5404319552844595 over 18014398509481984,
                -3602879701896397 over 36028797018963968,
                ZERO,
                3602879701896397 over 36028797018963968,
                5404319552844595 over 18014398509481984,
                1 over 2,
                ONE,
                2 over 1,
                3 over 1,
                4 over 1,
                8687443681197687 over 70368744177664
            )
            assertEquals(
                rationals,
                doubles.map {
                    it.toFiniteBigRational()
                })
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
                -5033165 over 16777216,
                -13421773 over 134217728,
                ZERO,
                13421773 over 134217728,
                5033165 over 16777216,
                1 over 2,
                ONE,
                2 over 1,
                3 over 1,
                4 over 1,
                16181625 over 131072
            )
            assertEquals(
                rationals,
                floats.map {
                    it.toFiniteBigRational()
                })
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
    inner class ProgressionTests {
        @Suppress("ReplaceCallWithBinaryOperator")
        @Test
        fun `should be itself`() {
            val zeroToOne = ZERO..ONE
            assertEquals(zeroToOne, zeroToOne)
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
                "${(ONE downTo ZERO step -TWO)}"
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
                for (r in ZERO..ONE step -1) noop()
            }
            assertThrows<IllegalStateException> {
                for (r in ONE downTo ZERO step 1); noop()
            }
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
        fun `should reciprocate`() {
            assertEquals(
                -3 over 5,
                (-5 over 3).reciprocal
            )
            assertThrows<ArithmeticException> {
                ZERO.reciprocal
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
            assertThrows<ArithmeticException> {
                ZERO.between(ZERO)
            }
            assertEquals(
                3 over 2,
                ONE.between(TWO)
            )
        }

        @Test
        fun `should find continued fraction`() {
            val cfA = (3245 over 1000).toContinuedFraction()
            assertEquals(
                listOf(3 over 1, 4 over 1, 12 over 1, 4 over 1),
                cfA
            )
            assertEquals((3245 over 1000), cfA.toFiniteBigRational())
            val negCfA = (-3245 over 1000).toContinuedFraction()
            assertEquals(
                listOf(-4 over 1, ONE, 3 over 1, 12 over 1, 4 over 1),
                negCfA
            )
            assertEquals((-3245 over 1000), negCfA.toFiniteBigRational())
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
}
