@file:Suppress("NonAsciiCharacters", "RedundantInnerClassModifier")

package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.`**`
import hm.binkley.math.big
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
import hm.binkley.math.fraction
import hm.binkley.math.gcd
import hm.binkley.math.isDenominatorEven
import hm.binkley.math.isOne
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
import hm.binkley.math.truncateAndFraction
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

// TODO: Range syntax does not pick up the typealias for the 2nd argument
// private typealias BDouble = BigDecimal

/**
 * NB -- the tests use a mixture of constructors while testing functionality.
 * This is intentional, and raises coverage.
 */
internal class FixedBigRationalTest {
    @Nested
    inner class ConstructionTests {
        @Test
        fun `should use constant 0`() {
            (0L over Long.MIN_VALUE) shouldBeSameInstanceAs ZERO
        }

        @Test
        fun `should use constant 1`() {
            (1.big over 1) shouldBeSameInstanceAs ONE
            ((-1).big over -1) shouldBeSameInstanceAs ONE
        }

        @Test
        fun `should use constant 2`() {
            (2.big over 1) shouldBeSameInstanceAs TWO
            ((-2).big over -1) shouldBeSameInstanceAs TWO
        }

        @Test
        fun `should use constant 10`() {
            (10.big over 1) shouldBeSameInstanceAs TEN
            ((-10).big over -1) shouldBeSameInstanceAs TEN
        }

        @Test
        fun `should be a number`() {
            (11 over 10).toByte() shouldBe 1L.toByte()
            (11 over 10).toShort() shouldBe 1L.toShort()
            (11 over 10).toInt() shouldBe 1L.toInt()
            (11 over 10).toLong() shouldBe 1L
            (11 over 10).toFloat() shouldBe 1.1.toFloat()
            (11 over 10).toDouble() shouldBe 1.1
        }

        @Test
        fun `should not be a character`() {
            shouldThrow<IllegalStateException> {
                ONE.toChar()
            }
        }

        @Test
        fun `should simplify fractions`() {
            (4.big over 2.big) shouldBe (2 over 1)
            (4.big over 8L) shouldBe (1 over 2.big)
            (4L over 8).denominator shouldBe 2.big
        }

        @Test
        fun `should maintain positive denominator`() {
            +(4 over -4L) shouldBe -ONE
            +(-4 over 4L) shouldBe -ONE
            +(-4 over -4) shouldBe ONE
        }

        @Test
        fun `should know its own value`() {
            ZERO.isZero() shouldBe true
            ONE.isOne() shouldBe true
        }
    }

    @Suppress("ReplaceCallWithBinaryOperator")
    @Test
    fun `should be itself`() {
        (1 over 2) shouldBe (1 over 2)
        +(1 over 2) shouldBe (1 over 2)
        (-(-(1 over 2))) shouldBe (1 over 2)
        0 shouldNotBe ZERO
        (2 over 5) shouldNotBe (2 over 3)
    }

    @Test
    fun `should hash separately`() {
        (1 over 3).hashCode() shouldNotBe (1 over 2).hashCode()
    }

    @Test
    fun `should not be a floating big rational range`() {
        (ONE..TWO) shouldNotBe
            FloatingBigRational.ONE..FloatingBigRational.TWO
        (ONE..TWO).hashCode() shouldNotBe
            (FloatingBigRational.ONE..FloatingBigRational.TWO).hashCode()
    }

    @Test
    fun `should pretty print`() {
        "$ZERO" shouldBe "0"
        "${1 over 2}" shouldBe "1⁄2"
        "${1 over -2}" shouldBe "-1⁄2"
    }

    @Test
    fun `should be ℚ-ish`() {
        val twoThirds = 2 over 3
        val threeHalves = 3 over 2
        val fiveSevenths = 5 over 7

        // Identity elements
        (twoThirds + ZERO) shouldBe twoThirds
        (twoThirds * ONE) shouldBe twoThirds
        (ONE + -ONE) shouldBe ZERO

        // Inverses
        (twoThirds + -twoThirds) shouldBe ZERO
        (twoThirds * twoThirds.unaryDiv()) shouldBe ONE

        // Associativity
        ((twoThirds + threeHalves) + fiveSevenths) shouldBe (
            twoThirds + (threeHalves + fiveSevenths)
            )
        ((twoThirds * threeHalves) * fiveSevenths) shouldBe (
            twoThirds * (threeHalves * fiveSevenths)
            )

        // Commutativity
        (twoThirds + threeHalves) shouldBe threeHalves + twoThirds
        (twoThirds * threeHalves) shouldBe threeHalves * twoThirds

        // Distributive
        ((twoThirds + threeHalves) * fiveSevenths) shouldBe (
            twoThirds * fiveSevenths + threeHalves * fiveSevenths
            )
    }

    @Test
    fun `should divide with remainder`() {
        (13 over 2).divideAndRemainder(3 over 1) shouldBe (
            (2 over 1) to (1 over 2)
            )
        (-13 over 2).divideAndRemainder(-3 over 1) shouldBe (
            (2 over 1) to (-1 over 2)
            )
        (-13 over 2).divideAndRemainder(3 over 1) shouldBe (
            (-2 over 1) to (-1 over 2)
            )
        (13 over 2).divideAndRemainder(-3 over 1) shouldBe (
            (-2 over 1) to (1 over 2)
            )
    }

    @Nested
    inner class OperatorTests {
        @Test
        fun `should do nothing arithmetically`() {
            (+(2 over 3)).numerator shouldBe (2 over 3).numerator
            (+(2 over 3)).denominator shouldBe (2 over 3).denominator
        }

        @Test
        fun `should invert arithmetically`() {
            (-(2 over 3)).numerator shouldBe (2 over 3).numerator.negate()
            (-(2 over 3)).denominator shouldBe (2 over 3).denominator
        }

        @Test
        fun `should invert multiplicatively`() {
            (2 over 3).unaryDiv().numerator shouldBe (2 over 3).denominator
            (2 over 3).unaryDiv().denominator shouldBe (2 over 3).numerator
        }

        @Test
        fun `should add`() {
            ((3 over 5) + (2 over 3)) shouldBe (19 over 15)
            (1.0.big + ONE) shouldBe (2 over 1)
            (ONE + 10.0.big) shouldBe (11 over 1)
            (1.0 + ONE) shouldBe (2 over 1)
            (ONE + 1.0) shouldBe (2 over 1)
            (1.0f + ONE) shouldBe (2 over 1)
            (ONE + 1.0f) shouldBe (2 over 1)
            (1.big + ONE) shouldBe (2 over 1)
            (ONE + 1.big) shouldBe (2 over 1)
            (1L + ONE) shouldBe (2 over 1)
            (ONE + 1L) shouldBe (2 over 1)
            (1 + ONE) shouldBe (2 over 1)
            (ONE + 1) shouldBe (2 over 1)
        }

        @Test
        fun `should subtract`() {
            ((3 over 5) - (2 over 3)) shouldBe (-1 over 15)
            (1.0.big - ONE) shouldBe ZERO
            (ONE - 1.0.big) shouldBe ZERO
            (1.0 - ONE) shouldBe ZERO
            (ONE - 1.0) shouldBe ZERO
            (1.0f - ONE) shouldBe ZERO
            (ONE - 1.0f) shouldBe ZERO
            (1.big - ONE) shouldBe ZERO
            (ONE - 1.big) shouldBe ZERO
            (1L - ONE) shouldBe ZERO
            (ONE - 1L) shouldBe ZERO
            (1 - ONE) shouldBe ZERO
            (ONE - 1) shouldBe ZERO
        }

        @Test
        fun `should multiply`() {
            ((3 over 5) * (2 over 3)) shouldBe (2 over 5)
            (1.0.big * ONE) shouldBe ONE
            (ONE * 1.0.big) shouldBe ONE
            (1.0 * ONE) shouldBe ONE
            (ONE * 1.0) shouldBe ONE
            (1.0f * ONE) shouldBe ONE
            (ONE * 1.0f) shouldBe ONE
            (1.big * ONE) shouldBe ONE
            (ONE * 1.big) shouldBe ONE
            (1L * ONE) shouldBe ONE
            (ONE * 1L) shouldBe ONE
            (1 * ONE) shouldBe ONE
            (ONE * 1) shouldBe ONE
        }

        @Test
        fun `should divide`() {
            ((3 over 5) / (2 over 3)) shouldBe (9 over 10)
            (1.0.big / ONE) shouldBe ONE
            (ONE / 1.0.big) shouldBe ONE
            (1.0 / ONE) shouldBe ONE
            (ONE / 1.0) shouldBe ONE
            (1.0f / ONE) shouldBe ONE
            (ONE / 1.0f) shouldBe ONE
            (1.big / ONE) shouldBe ONE
            (ONE / 1.big) shouldBe ONE
            (1L / ONE) shouldBe ONE
            (ONE / 1L) shouldBe ONE
            (1 / ONE) shouldBe ONE
            (ONE / 1) shouldBe ONE
        }

        @Test
        fun `should find remainder`() {
            ((3 over 5) % (2 over 3)) shouldBe ZERO
            (1.0.big % ONE) shouldBe ZERO
            (ONE % 1.0.big) shouldBe ZERO
            (1.0 % ONE) shouldBe ZERO
            (ONE % 1.0) shouldBe ZERO
            (1.0f % ONE) shouldBe ZERO
            (ONE % 1.0f) shouldBe ZERO
            (1.big % ONE) shouldBe ZERO
            (ONE % 1.big) shouldBe ZERO
            (1L % ONE) shouldBe ZERO
            (ONE % 1L) shouldBe ZERO
            (1 % ONE) shouldBe ZERO
            (ONE % 1) shouldBe ZERO
        }

        @Test
        fun `should increment`() {
            var a = 1L.toBigRational()
            ++a shouldBe (2 over 1)
        }

        @Test
        fun `should decrement`() {
            var a = ONE
            --a shouldBe ZERO
        }
    }

    @Test
    fun `should note integer rationals`() {
        (1 over 2).isInteger() shouldBe false
        (2 over 1).isInteger() shouldBe true
        ZERO.isInteger() shouldBe true
    }

    @Test
    fun `should note dyadic rationals`() {
        (1 over 2).isDyadic() shouldBe true
        (2 over 1).isDyadic() shouldBe true
        ZERO.isDyadic() shouldBe true
        (2 over 3).isDyadic() shouldBe false
    }

    @Test
    fun `should note p-adic rationals`() {
        (1 over 3).isPAdic(3) shouldBe true
        (2 over 1).isPAdic(3) shouldBe true
        ZERO.isPAdic(3) shouldBe true
        (2 over 5).isPAdic(3) shouldBe false
    }

    @Test
    fun `should note even denominators`() {
        (1 over 2).isDenominatorEven() shouldBe true
        (1 over 3).isDenominatorEven() shouldBe false
    }

    @Nested
    inner class RoundingTests {
        @Test
        fun `should round down`() {
            ZERO.floor() shouldBe ZERO
            ONE.floor() shouldBe ONE
            (-ONE).floor() shouldBe -ONE
            (1 over 2).floor() shouldBe ZERO
            (-1 over 2).floor() shouldBe -ONE
        }

        @Test
        fun `should round up`() {
            ZERO.ceil() shouldBe ZERO
            ONE.ceil() shouldBe ONE
            (-ONE).ceil() shouldBe -ONE
            (1 over 2).ceil() shouldBe ONE
            (-1 over 2).ceil() shouldBe ZERO
        }

        @Test
        fun `should truncate and fractionate`() {
            listOf(ZERO, ONE, -ONE, 3 over 2, -3 over 2).forEach {
                val (truncation, fraction) = it.truncateAndFraction()
                (truncation + fraction) shouldBe it
            }
        }

        @Test
        fun `should round towards 0`() {
            ZERO.truncate() shouldBe ZERO
            ONE.truncate() shouldBe ONE
            (-ONE).truncate() shouldBe -ONE
            (1 over 2).truncate() shouldBe ZERO
            (-1 over 2).truncate() shouldBe ZERO
        }

        @Test
        fun `should fractionate`() {
            ZERO.fraction() shouldBe ZERO
            (3 over 2).fraction() shouldBe (1 over 2)
            (-3 over 2).fraction() shouldBe (-1 over 2)
        }
    }

    @Nested
    inner class ConversionTests {
        @Test
        fun `should convert BigDecimal in infix constructor`() {
            0.0.big.toBigRational() shouldBe ZERO
            30.0.big.toBigRational() shouldBe (30 over 1)
            3.0.big.toBigRational() shouldBe (3 over 1)
            "0.3".big.toBigRational() shouldBe (3 over 10)
            "7.70".big.toBigRational() shouldBe (77 over 10)
            (1.0.big over 1.0.big) shouldBe ONE
            (1.big over 1.0.big) shouldBe ONE
            (1L over 1.0.big) shouldBe ONE
            (1 over 1.0.big) shouldBe ONE
            (1.0 over 1.0.big) shouldBe ONE
            (1.0f over 1.0.big) shouldBe ONE
            (1.0.big over 1L) shouldBe ONE
            (1.0.big over 1) shouldBe ONE
        }

        @Test
        fun `should convert BigInteger in infix constructor`() {
            0.big.toBigRational() shouldBe ZERO
            BInt.valueOf(30L).toBigRational() shouldBe (30 over 1)
            3.big.toBigRational() shouldBe (3 over 1)
            (1.big over 1.big) shouldBe ONE
            (1.0.big over 1.big) shouldBe ONE
            (1L over 1.big) shouldBe ONE
            (1 over 1.big) shouldBe ONE
            (1.0 over 1.big) shouldBe ONE
            (1.0f over 1.big) shouldBe ONE
            (1.big over 1L) shouldBe ONE
            (1.big over 1) shouldBe ONE
        }

        @Test
        fun `should convert double in infix constructor`() {
            (1.0.big over 1.0) shouldBe ONE
            (1.big over 1.0) shouldBe ONE
            (1L over 1.0) shouldBe ONE
            (1 over 1.0) shouldBe ONE
            (1.0 over 1.0) shouldBe ONE
            (1.0f over 1.0) shouldBe ONE
            (1.0 over 1.big) shouldBe ONE
            (1.0 over 1L) shouldBe ONE
            (1.0 over 1) shouldBe ONE
        }

        @Test
        fun `should convert float in infix constructor`() {
            (1.0.big over 1.0f) shouldBe ONE
            (1.big over 1.0f) shouldBe ONE
            (1L over 1.0f) shouldBe ONE
            (1 over 1.0f) shouldBe ONE
            (1.0 over 1.0f) shouldBe ONE
            (1.0f over 1.0f) shouldBe ONE
            (1.0f over 1.big) shouldBe ONE
            (1.0f over 1L) shouldBe ONE
            (1.0f over 1) shouldBe ONE
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

            doubles.map {
                it.toBigRational()
            } shouldBe rationals
            rationals.map {
                it.toDouble()
            } shouldBe doubles
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

            floats.map {
                it.toBigRational()
            } shouldBe rationals
            rationals.map {
                it.toFloat()
            } shouldBe floats
        }

        @Test
        fun `should wrap conversion to Long`() {
            ((Byte.MAX_VALUE + 1) over 1).toByte() shouldBe (-128).toByte()
        }

        @Test
        fun `should wrap conversion to Byte`() {
            ((Byte.MAX_VALUE + 1) over 1).toByte() shouldBe (-128).toByte()
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

            sorted shouldBe listOf(ZERO, ZERO)
        }

        @Test
        fun `should compare other number types`() {
            (1.0.big > ZERO) shouldBe true
            (ONE > 0.0.big) shouldBe true
            (1.0 > ZERO) shouldBe true
            (ZERO < 1.0) shouldBe true
            shouldThrow<ArithmeticException> {
                Double.POSITIVE_INFINITY > ZERO
            }
            shouldThrow<ArithmeticException> {
                ZERO < Double.POSITIVE_INFINITY
            }
            shouldThrow<ArithmeticException> {
                ZERO > Double.NEGATIVE_INFINITY
            }
            shouldThrow<ArithmeticException> {
                Double.NEGATIVE_INFINITY < ZERO
            }
            (1.0f > ZERO) shouldBe true
            (ZERO < 1.0f) shouldBe true
            shouldThrow<ArithmeticException> {
                Float.NaN > ZERO
            }
            shouldThrow<ArithmeticException> {
                ZERO < Float.NaN
            }
            (0.big < ONE) shouldBe true
            (ZERO < 1.big) shouldBe true
            (0L < ONE) shouldBe true
            (ZERO < 1L) shouldBe true
            (0 < ONE) shouldBe true
            (ZERO < 1) shouldBe true
        }

        @Test
        fun `should multiplicatively invert`() {
            (-5 over 3).unaryDiv() shouldBe (-3 over 5)

            shouldThrow<ArithmeticException> {
                ZERO.unaryDiv()
            }
        }

        @Test
        fun `should absolute`() {
            ZERO.absoluteValue shouldBe ZERO
            (3 over 5).absoluteValue shouldBe (3 over 5)
            (-3 over 5).absoluteValue shouldBe (3 over 5)
        }

        @Test
        fun `should signum`() {
            (3 over 5).sign shouldBe ONE
            (0 over 5).sign shouldBe ZERO
            (-3 over 5).sign shouldBe -ONE
        }

        @Test
        fun `should raise`() {
            ((3 over 5) `**` 2) shouldBe (9 over 25)
            (3 over 5).pow(0) shouldBe ONE
            (3 over 5).pow(-2) shouldBe (25 over 9)
        }

        @Test
        fun `should square root`() {
            (9 over 25).sqrt() shouldBe (3 over 5)
            (9 over 25).sqrtApproximated() shouldBe (3 over 5)
            (8 over 25).sqrtApproximated() shouldBe (
                282_842_712_474_619 over 500_000_000_000_000
                )
            (9 over 26).sqrtApproximated() shouldBe (
                5_883_484_054_145_521 over 10_000_000_000_000_000
                )

            shouldThrow<ArithmeticException> { (8 over 25).sqrt() }
            shouldThrow<ArithmeticException> { (9 over 26).sqrt() }
        }

        @Test
        fun `should find GCD (HCF)`() {
            (2 over 9).gcd(6 over 21) shouldBe (2 over 63)
            (-2 over 9).gcd(6 over 21) shouldBe (2 over 63)
            (2 over 9).gcd(-6 over 21) shouldBe (2 over 63)
            (-2 over 9).gcd(-6 over 21) shouldBe (2 over 63)
            ZERO.gcd(2 over 9) shouldBe (2 over 9)
            ZERO.gcd(ZERO) shouldBe ZERO
        }

        @Test
        fun `should find LCM (LCD)`() {
            (2 over 9).lcm(6 over 21) shouldBe (2 over 1)
            (-2 over 9).lcm(6 over 21) shouldBe (2 over 1)
            (2 over 9).lcm(-6 over 21) shouldBe (2 over 1)
            (-2 over 9).lcm(-6 over 21) shouldBe (2 over 1)
            ZERO.lcm(6 over 21) shouldBe ZERO
            ZERO.lcm(ZERO) shouldBe ZERO
        }

        @Test
        fun `should find between`() {
            ZERO.mediant(ZERO) shouldBe ZERO
            ONE.mediant(TWO) shouldBe (3 over 2)
        }

        @Test
        fun `should find continued fraction`() {
            val cfA = (3245 over 1000).toContinuedFraction()
            cfA shouldBe listOf(3 over 1, 4 over 1, 12 over 1, 4 over 1)
            cfA.toBigRational() shouldBe (3245 over 1000)
            val negCfA = (-3245 over 1000).toContinuedFraction()
            negCfA shouldBe
                listOf(-4 over 1, ONE, 3 over 1, 12 over 1, 4 over 1)
            negCfA.toBigRational() shouldBe (-3245 over 1000)
            ZERO.toContinuedFraction() shouldBe listOf(ZERO)
            ONE.toContinuedFraction() shouldBe listOf(ONE)
            (1 over 3).toContinuedFraction() shouldBe listOf(ZERO, 3 over 1)
        }
    }

    @Nested
    inner class CantorSpiral {
        @Test
        fun `should find Cantor spiral`() {
            cantorSpiral().take(10).toList() shouldBe
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
                )
        }
    }
}
