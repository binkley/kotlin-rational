@file:Suppress("NonAsciiCharacters", "RedundantInnerClassModifier")

package hm.binkley.math.floating

import hm.binkley.math.BInt
import hm.binkley.math.`**`
import hm.binkley.math.backAgain
import hm.binkley.math.big
import hm.binkley.math.ceil
import hm.binkley.math.compareTo
import hm.binkley.math.div
import hm.binkley.math.divideAndRemainder
import hm.binkley.math.fixed.FixedBigRational
import hm.binkley.math.floating.FloatingBigRational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational.Companion.POSITIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.TEN
import hm.binkley.math.floating.FloatingBigRational.Companion.TWO
import hm.binkley.math.floating.FloatingBigRational.Companion.ZERO
import hm.binkley.math.floating.FloatingBigRational.Companion.cantorSpiral
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
import hm.binkley.math.round
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

private typealias BigRationalAssertion = (FloatingBigRational) -> Unit

/**
 * NB -- the tests use a mixture of constructors while testing functionality.
 * This is intentional, and raises coverage.
 */
internal class FloatingBigRationalTest {
    @Nested
    inner class ConstructionTests {
        @Test
        fun `should use constant NaN`() {
            (0 over 0) shouldBeSameInstanceAs NaN
        }

        @Test
        fun `should use constant Infinity`() {
            (Long.MAX_VALUE over 0L) shouldBeSameInstanceAs POSITIVE_INFINITY
        }

        @Test
        fun `should use constant -Infinity`() {
            (Long.MIN_VALUE over 0.big) shouldBeSameInstanceAs
                NEGATIVE_INFINITY
        }

        @Test
        fun `should use constant 0`() {
            (0L over Long.MIN_VALUE) shouldBeSameInstanceAs ZERO
        }

        @Test
        fun `should use constant 1`() {
            (1.big over 1) shouldBeSameInstanceAs ONE
            (-1 over -1) shouldBeSameInstanceAs ONE
        }

        @Test
        fun `should use constant 2`() {
            (2.big over 1) shouldBeSameInstanceAs TWO
            (-2 over -1) shouldBeSameInstanceAs TWO
        }

        @Test
        fun `should use constant 10`() {
            (10.big over 1) shouldBeSameInstanceAs TEN
            (-10 over -1) shouldBeSameInstanceAs TEN
        }

        @Test
        fun `should be a number`() {
            (11 over 10).toByte() shouldBe 1L.toByte()
            (11 over 10).toShort() shouldBe 1L.toShort()
            (11 over 10).toInt() shouldBe 1L.toInt()
            (11 over 10).toLong() shouldBe 1L
            (11 over 10).toFloat() shouldBe 1.1.toFloat()
            (11 over 10).toDouble() shouldBe 1.1
            NaN.toDouble() shouldBe Double.NaN
            POSITIVE_INFINITY.toDouble() shouldBe Double.POSITIVE_INFINITY
            NEGATIVE_INFINITY.toDouble() shouldBe Double.NEGATIVE_INFINITY
            POSITIVE_INFINITY.toLong() shouldBe Long.MAX_VALUE
            NEGATIVE_INFINITY.toLong() shouldBe Long.MIN_VALUE
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
            ((4L over 8).denominator) shouldBe 2.big
        }

        @Test
        fun `should maintain positive denominator`() {
            +(4 over -4L) shouldBe -ONE
            +(-4 over 4L) shouldBe -ONE
            (-4 over -4) shouldBe ONE
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
        -(-(1 over 2)) shouldBe (1 over 2)
        0 shouldNotBe ZERO
        (2 over 5) shouldNotBe (2 over 3)
    }

    @Test
    fun `should hash separately`() {
        (1 over 3).hashCode() shouldNotBe (1 over 2).hashCode()
        NEGATIVE_INFINITY.hashCode() shouldNotBe POSITIVE_INFINITY.hashCode()
    }

    @Test
    fun `should not be a floating big rational`() {
        FixedBigRational.valueOf(1.big, 1.big).hashCode() shouldNotBe
            (1 over 1).hashCode()
        (FixedBigRational.ONE..FixedBigRational.TWO) shouldNotBe ONE..TWO
        (FixedBigRational.ONE..FixedBigRational.TWO).hashCode() shouldNotBe
            (ONE..TWO).hashCode()
    }

    @Test
    fun `should pretty print`() {
        (ZERO.toString()) shouldBe "0"
        (2 over 1).toString() shouldBe "2"
        (1 over 2).toString() shouldBe "1⁄2"
        (1 over -2).toString() shouldBe "-1⁄2"
        (POSITIVE_INFINITY.toString()) shouldBe "Infinity"
        (NEGATIVE_INFINITY.toString()) shouldBe "-Infinity"
        (NaN.toString()) shouldBe "NaN"
    }

    @Test
    fun `should be ℚ-ish`() {
        val twoThirds = 2 over 3
        val threeHalves = 3 over 2
        val fiveSevenths = 5 over 7

        // Associativity
        ((twoThirds + threeHalves) + fiveSevenths) shouldBe
            twoThirds + (threeHalves + fiveSevenths)
        ((twoThirds * threeHalves) * fiveSevenths) shouldBe
            twoThirds * (threeHalves * fiveSevenths)

        // Commutativity
        (threeHalves + twoThirds) shouldBe twoThirds + threeHalves
        (threeHalves * twoThirds) shouldBe twoThirds * threeHalves

        // Identities
        (twoThirds + ZERO) shouldBe twoThirds
        (twoThirds * ONE) shouldBe twoThirds

        // Inverses
        (twoThirds + -twoThirds) shouldBe ZERO
        (twoThirds * twoThirds.unaryDiv()) shouldBe ONE

        // Distributive
        ((twoThirds + threeHalves) * fiveSevenths) shouldBe
            twoThirds * fiveSevenths + threeHalves * fiveSevenths

        (ONE + -ONE) shouldBe ZERO
    }

    @Test
    fun `should divide with remainder`() {
        (13 over 2).divideAndRemainder(3 over 1) shouldBe
            ((2 over 1) to (1 over 2))
        (-13 over 2).divideAndRemainder(-3 over 1) shouldBe
            ((2 over 1) to (-1 over 2))
        (-13 over 2).divideAndRemainder(3 over 1) shouldBe
            ((-2 over 1) to (-1 over 2))
        (13 over 2).divideAndRemainder(-3 over 1) shouldBe
            ((-2 over 1) to (1 over 2))

        fun nonFiniteCheck(
            dividend: FloatingBigRational,
            divisor: FloatingBigRational,
            assertion: BigRationalAssertion,
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
            val rightSideUp = 2 over 3
            val noChange = +rightSideUp

            noChange.numerator shouldBe rightSideUp.numerator
            noChange.denominator shouldBe rightSideUp.denominator
        }

        @Test
        fun `should invert arithmetically`() {
            val rightSideUp = 2 over 3
            val upsideDown = -rightSideUp

            upsideDown.numerator shouldBe rightSideUp.numerator.negate()
            upsideDown.denominator shouldBe rightSideUp.denominator
        }

        @Test
        fun `should invert multiplicatively`() {
            val rightSideUp = 2 over 3
            val upsideDown = rightSideUp.unaryDiv()

            upsideDown.numerator shouldBe rightSideUp.denominator
            upsideDown.denominator shouldBe rightSideUp.numerator
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
            (ONE + POSITIVE_INFINITY).isPositiveInfinity() shouldBe true
            (POSITIVE_INFINITY + POSITIVE_INFINITY)
                .isPositiveInfinity() shouldBe true
            (POSITIVE_INFINITY + NEGATIVE_INFINITY).isNaN() shouldBe true
            (ONE + NEGATIVE_INFINITY).isNegativeInfinity() shouldBe true
            (NEGATIVE_INFINITY + NEGATIVE_INFINITY)
                .isNegativeInfinity() shouldBe true
            (NEGATIVE_INFINITY + POSITIVE_INFINITY).isNaN() shouldBe true
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
            (POSITIVE_INFINITY - ONE).isPositiveInfinity() shouldBe true
            (POSITIVE_INFINITY - POSITIVE_INFINITY).isNaN() shouldBe true
            (POSITIVE_INFINITY - NEGATIVE_INFINITY)
                .isPositiveInfinity() shouldBe true
            (NEGATIVE_INFINITY - ONE).isNegativeInfinity() shouldBe true
            (NEGATIVE_INFINITY - NEGATIVE_INFINITY).isNaN() shouldBe true
            (NEGATIVE_INFINITY - POSITIVE_INFINITY)
                .isNegativeInfinity() shouldBe true
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
            (ONE * POSITIVE_INFINITY).isPositiveInfinity() shouldBe true
            (ONE * NEGATIVE_INFINITY).isNegativeInfinity() shouldBe true
            (ZERO * POSITIVE_INFINITY).isNaN() shouldBe true
            (POSITIVE_INFINITY * ZERO).isNaN() shouldBe true
            (ZERO * NEGATIVE_INFINITY).isNaN() shouldBe true
            (NEGATIVE_INFINITY * ZERO).isNaN() shouldBe true
            (POSITIVE_INFINITY * POSITIVE_INFINITY)
                .isPositiveInfinity() shouldBe true
        }

        @Test
        fun `should divide`() {
            (ONE / NaN).isNaN() shouldBe true
            (ZERO / ZERO).isNaN() shouldBe true
            (ZERO / POSITIVE_INFINITY) shouldBe ZERO
            (ONE / ZERO).isPositiveInfinity() shouldBe true
            (POSITIVE_INFINITY / POSITIVE_INFINITY).isNaN() shouldBe true
            (ZERO / NEGATIVE_INFINITY) shouldBe ZERO
            (-ONE / ZERO).isNegativeInfinity() shouldBe true
            (NEGATIVE_INFINITY / NEGATIVE_INFINITY).isNaN() shouldBe true
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
            (ONE % NaN).isNaN() shouldBe true
            (ONE % POSITIVE_INFINITY) shouldBe ZERO
            (ONE % NEGATIVE_INFINITY) shouldBe ZERO

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
            var nonFinite = NaN
            (++nonFinite).isNaN() shouldBe true
            nonFinite = POSITIVE_INFINITY
            (++nonFinite).isPositiveInfinity() shouldBe true
            nonFinite = NEGATIVE_INFINITY
            (++nonFinite).isNegativeInfinity() shouldBe true
        }

        @Test
        fun `should decrement`() {
            var a = ONE
            --a shouldBe ZERO
            var nonFinite = NaN
            (--nonFinite).isNaN() shouldBe true
            nonFinite = POSITIVE_INFINITY
            (--nonFinite).isPositiveInfinity() shouldBe true
            nonFinite = NEGATIVE_INFINITY
            (--nonFinite).isNegativeInfinity() shouldBe true
        }
    }

    @Nested
    inner class RoundingTests {
        @Test
        fun `should round towards ceiling`() {
            NaN.ceil().isNaN() shouldBe true
            POSITIVE_INFINITY.ceil().isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.ceil().isNegativeInfinity() shouldBe true
            ZERO.ceil() shouldBe ZERO
            ONE.ceil() shouldBe ONE
            (-ONE).ceil() shouldBe -ONE
            (3 over 2).ceil() shouldBe TWO
            (1 over 2).ceil() shouldBe ONE
            (-1 over 2).ceil() shouldBe ZERO
            (-3 over 2).ceil() shouldBe -ONE
        }

        @Test
        fun `should round towards floor`() {
            NaN.floor().isNaN() shouldBe true
            POSITIVE_INFINITY.floor().isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.floor().isNegativeInfinity() shouldBe true
            ZERO.floor() shouldBe ZERO
            ONE.floor() shouldBe ONE
            (-ONE).floor() shouldBe -ONE
            (3 over 2).floor() shouldBe ONE
            (1 over 2).floor() shouldBe ZERO
            (-1 over 2).floor() shouldBe -ONE
            (-3 over 2).floor() shouldBe -TWO
        }

        @Test
        fun `should round towards nearest even whole number`() {
            ZERO.round() shouldBe ZERO
            ONE.round() shouldBe ONE
            (-ONE).round() shouldBe -ONE
            (3 over 2).round() shouldBe TWO
            (1 over 2).round() shouldBe ZERO
            (-1 over 2).round() shouldBe ZERO
            (-3 over 2).round() shouldBe -TWO
        }

        @Test
        fun `should truncate and fractionate`() {
            listOf(
                ZERO,
                ONE,
                -ONE,
                3 over 2,
                1 over 2,
                -1 over 2,
                -3 over 2,
            ).forEach {
                val (truncation, fraction) = it.truncateAndFraction()
                (truncation + fraction) shouldBe it
            }
        }

        @Test
        fun `should round towards 0`() {
            NaN.truncate().isNaN() shouldBe true
            POSITIVE_INFINITY.truncate().isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.truncate().isNegativeInfinity() shouldBe true
            ZERO.truncate() shouldBe ZERO
            ONE.truncate() shouldBe ONE
            (-ONE).truncate() shouldBe -ONE
            (3 over 2).truncate() shouldBe ONE
            (1 over 2).truncate() shouldBe ZERO
            (-1 over 2).truncate() shouldBe ZERO
            (-3 over 2).truncate() shouldBe -ONE
        }

        @Test
        fun `should fractionate`() {
            NaN.fraction().isNaN() shouldBe true
            POSITIVE_INFINITY.fraction().isNaN() shouldBe true
            NEGATIVE_INFINITY.fraction().isNaN() shouldBe true
            ZERO.fraction() shouldBe ZERO
            ONE.fraction() shouldBe ZERO
            (-ONE).fraction() shouldBe ZERO
            (3 over 2).fraction() shouldBe (1 over 2)
            (1 over 2).fraction() shouldBe (1 over 2)
            (-1 over 2).fraction() shouldBe (-1 over 2)
            (-3 over 2).fraction() shouldBe (-1 over 2)
        }
    }

    @Nested
    inner class SpecialCasesTests {
        @Test
        fun `should round trip NaN and infinities`() {
            NaN.toDouble().toBigRational().isNaN() shouldBe true
            POSITIVE_INFINITY.toDouble().toBigRational()
                .isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.toDouble().toBigRational()
                .isNegativeInfinity() shouldBe true

            NaN.toFloat().toBigRational().isNaN() shouldBe true
            POSITIVE_INFINITY.toFloat().toBigRational()
                .isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.toFloat().toBigRational()
                .isNegativeInfinity() shouldBe true
        }

        @Test
        fun `should round trip as double precision`() {
            Double.NaN.toBigRational().toDouble() shouldBe Double.NaN
            Double.MAX_VALUE.toBigRational().toDouble() shouldBe
                Double.MAX_VALUE
            Double.MIN_VALUE.toBigRational().toDouble() shouldBe
                Double.MIN_VALUE
        }

        @Test
        fun `should round trip as single precision`() {
            Float.NaN.toBigRational().toFloat() shouldBe Float.NaN
            Float.MAX_VALUE.toBigRational().toFloat() shouldBe
                Float.MAX_VALUE
            Float.MIN_VALUE.toBigRational().toFloat() shouldBe
                Float.MIN_VALUE
        }

        @Test
        fun `should be infinity`() {
            (2 over 0).isPositiveInfinity() shouldBe true
            (-2 over 0).isNegativeInfinity() shouldBe true
        }

        @Test
        fun `should check finitude`() {
            ZERO.isFinite() shouldBe true
            POSITIVE_INFINITY.isFinite() shouldBe false
            NEGATIVE_INFINITY.isFinite() shouldBe false
            NaN.isFinite() shouldBe false
        }

        @Test
        fun `should check infinitude`() {
            ZERO.isInfinite() shouldBe false
            POSITIVE_INFINITY.isInfinite() shouldBe true
            NEGATIVE_INFINITY.isInfinite() shouldBe true
            NaN.isInfinite() shouldBe false
        }

        @Test
        fun `should propagate NaN`() {
            (ZERO + NaN).isNaN() shouldBe true
            (NaN + NaN).isNaN() shouldBe true
            (NaN + ONE).isNaN() shouldBe true
            (NaN - ZERO).isNaN() shouldBe true
            (NaN - NaN).isNaN() shouldBe true
            (ZERO - NaN).isNaN() shouldBe true
            (ONE * NaN).isNaN() shouldBe true
            (NaN * NaN).isNaN() shouldBe true
            (NaN * ONE).isNaN() shouldBe true
            (NaN / ONE).isNaN() shouldBe true
            (NaN / NaN).isNaN() shouldBe true
            (ONE / NaN).isNaN() shouldBe true
        }

        @Test
        fun `should propagate infinities`() {
            (-NEGATIVE_INFINITY).isPositiveInfinity() shouldBe true
            (ONE + POSITIVE_INFINITY).isPositiveInfinity() shouldBe true
            (NEGATIVE_INFINITY - ONE).isNegativeInfinity() shouldBe true
            (POSITIVE_INFINITY + NEGATIVE_INFINITY).isNaN() shouldBe true
            (POSITIVE_INFINITY * POSITIVE_INFINITY)
                .isPositiveInfinity() shouldBe true
            (POSITIVE_INFINITY * NEGATIVE_INFINITY)
                .isNegativeInfinity() shouldBe true
            (NEGATIVE_INFINITY * NEGATIVE_INFINITY)
                .isPositiveInfinity() shouldBe true
            (POSITIVE_INFINITY / POSITIVE_INFINITY).isNaN() shouldBe true
            (POSITIVE_INFINITY / NEGATIVE_INFINITY).isNaN() shouldBe true
            (NEGATIVE_INFINITY / NEGATIVE_INFINITY).isNaN() shouldBe true
        }

        @Test
        fun `should invert infinities incorrectly`() {
            (ONE / POSITIVE_INFINITY) shouldBe ZERO
            (ONE / NEGATIVE_INFINITY) shouldBe ZERO
        }

        @Test
        fun `should cope with various infinities`() {
            (ZERO * POSITIVE_INFINITY).isNaN() shouldBe true
            (ZERO / POSITIVE_INFINITY) shouldBe ZERO
            (POSITIVE_INFINITY / ZERO).isPositiveInfinity() shouldBe true
            (ZERO * NEGATIVE_INFINITY).isNaN() shouldBe true
            (ZERO / NEGATIVE_INFINITY) shouldBe ZERO
            (NEGATIVE_INFINITY / ZERO).isNegativeInfinity() shouldBe true
            (POSITIVE_INFINITY * NEGATIVE_INFINITY)
                .isNegativeInfinity() shouldBe true
            (POSITIVE_INFINITY / NEGATIVE_INFINITY).isNaN() shouldBe true
        }

        @Test
        fun `should understand equalities of non-finite values`() {
            POSITIVE_INFINITY shouldBe POSITIVE_INFINITY
            NEGATIVE_INFINITY shouldBe NEGATIVE_INFINITY
            // Cannot use shouldNotBe: It short-circuits for === objects
            (NaN == NaN) shouldBe false
        }
    }

    @Test
    fun `should note integer rationals`() {
        (1 over 2).isInteger() shouldBe false
        (2 over 1).isInteger() shouldBe true
        ZERO.isInteger() shouldBe true
        POSITIVE_INFINITY.isInteger() shouldBe false
        NEGATIVE_INFINITY.isInteger() shouldBe false
        NaN.isInteger() shouldBe false
    }

    @Test
    fun `should note dyadic rationals`() {
        (1 over 2).isDyadic() shouldBe true
        (2 over 1).isDyadic() shouldBe true
        ZERO.isDyadic() shouldBe true
        (2 over 3).isDyadic() shouldBe false
        POSITIVE_INFINITY.isDyadic() shouldBe false
        NEGATIVE_INFINITY.isDyadic() shouldBe false
        NaN.isDyadic() shouldBe false
    }

    @Test
    fun `should note p-adic rationals`() {
        (1 over 3).isPAdic(3) shouldBe true
        (2 over 1).isPAdic(3) shouldBe true
        ZERO.isPAdic(3) shouldBe true
        (2 over 5).isPAdic(3) shouldBe false
        POSITIVE_INFINITY.isPAdic(3) shouldBe false
        NEGATIVE_INFINITY.isPAdic(3) shouldBe false
        NaN.isPAdic(3) shouldBe false
    }

    @Test
    fun `should note even denominators`() {
        (1 over 2).isDenominatorEven() shouldBe true
        (1 over 3).isDenominatorEven() shouldBe false
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
            Double.NaN.toBigRational().isNaN() shouldBe true
            Double.NEGATIVE_INFINITY.toBigRational()
                .isNegativeInfinity() shouldBe true
            Double.POSITIVE_INFINITY.toBigRational()
                .isPositiveInfinity() shouldBe true
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
            Float.NaN.toBigRational().isNaN() shouldBe true
            Float.NEGATIVE_INFINITY.toBigRational()
                .isNegativeInfinity() shouldBe true
            Float.POSITIVE_INFINITY.toBigRational()
                .isPositiveInfinity() shouldBe true
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
            NaN.toByte() shouldBe 0b0
        }

        @Test
        fun `should wrap conversion to Byte`() {
            ((Byte.MAX_VALUE + 1) over 1).toByte() shouldBe (-128).toByte()
            NaN.toByte() shouldBe 0b0
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

            (sorted.map { it.toDouble() }) shouldBe doubleSorted
        }

        @Test
        fun `should compare other number types`() {
            (1.0.big > ZERO) shouldBe true
            (ONE > 0.0.big) shouldBe true
            (Double.POSITIVE_INFINITY > ZERO) shouldBe true
            (ZERO > Double.NEGATIVE_INFINITY) shouldBe true
            (Float.NaN > ZERO) shouldBe true
            (NaN > Float.MAX_VALUE) shouldBe true
            (0.big < ONE) shouldBe true
            (ZERO < 1.big) shouldBe true
            (0L < ONE) shouldBe true
            (ZERO < 1L) shouldBe true
            (0 < ONE) shouldBe true
            (ZERO < 1) shouldBe true
        }

        @Test
        fun `should not order NaN values`() {
            (NaN == NaN) shouldBe false
            (NaN > NaN) shouldBe false
            (NaN < NaN) shouldBe false
        }

        @Test
        fun `should reciprocate`() {
            (-5 over 3).unaryDiv() shouldBe (-3 over 5)
            ZERO.unaryDiv().isPositiveInfinity() shouldBe true
            POSITIVE_INFINITY.unaryDiv() shouldBe ZERO
            NEGATIVE_INFINITY.unaryDiv() shouldBe ZERO
            NaN.unaryDiv().isNaN() shouldBe true
        }

        @Test
        fun `should absolute`() {
            (ZERO.absoluteValue) shouldBe ZERO
            ((3 over 5).absoluteValue) shouldBe (3 over 5)
            ((-3 over 5).absoluteValue) shouldBe (3 over 5)
            NaN.absoluteValue.isNaN() shouldBe true
            POSITIVE_INFINITY.absoluteValue
                .isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.absoluteValue
                .isPositiveInfinity() shouldBe true
        }

        @Test
        fun `should signum`() {
            ((3 over 5).sign) shouldBe ONE
            ((0 over 5).sign) shouldBe ZERO
            ((-3 over 5).sign) shouldBe -ONE
            (NEGATIVE_INFINITY.sign) shouldBe -ONE
            (POSITIVE_INFINITY.sign) shouldBe ONE
            NaN.sign.isNaN() shouldBe true
        }

        @Test
        fun `should raise`() {
            ((3 over 5) `**` 2) shouldBe (9 over 25)
            (3 over 5).pow(0) shouldBe ONE
            (3 over 5).pow(-2) shouldBe (25 over 9)
            POSITIVE_INFINITY.pow(2) shouldBe POSITIVE_INFINITY
            POSITIVE_INFINITY.pow(-1) shouldBe ZERO
            NEGATIVE_INFINITY.pow(3) shouldBe NEGATIVE_INFINITY
            NEGATIVE_INFINITY.pow(2) shouldBe POSITIVE_INFINITY
            NEGATIVE_INFINITY.pow(-1) shouldBe ZERO
            NaN.pow(2).isNaN() shouldBe true
            POSITIVE_INFINITY.pow(0).isNaN() shouldBe true
            NEGATIVE_INFINITY.pow(0).isNaN() shouldBe true
        }

        @Test
        fun `should square root`() {
            (9 over 25).sqrt() shouldBe (3 over 5)
            (9 over 25).sqrtApproximated() shouldBe (3 over 5)
            (8 over 25).sqrtApproximated() shouldBe
                (282_842_712_474_619 over 500_000_000_000_000)
            (9 over 26).sqrtApproximated() shouldBe
                (5_883_484_054_145_521 over 10_000_000_000_000_000)

            shouldThrow<ArithmeticException> { (8 over 25).sqrt() }
            shouldThrow<ArithmeticException> { (9 over 26).sqrt() }

            NaN.sqrt().isNaN() shouldBe true
            NaN.sqrtApproximated().isNaN() shouldBe true
            POSITIVE_INFINITY.sqrt() shouldBe POSITIVE_INFINITY
            POSITIVE_INFINITY.sqrtApproximated() shouldBe POSITIVE_INFINITY
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
            NaN.mediant(NaN).isNaN() shouldBe true
            NaN.mediant(POSITIVE_INFINITY).isNaN() shouldBe true
            NaN.mediant(NEGATIVE_INFINITY).isNaN() shouldBe true
            POSITIVE_INFINITY.mediant(POSITIVE_INFINITY)
                .isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.mediant(NEGATIVE_INFINITY)
                .isNegativeInfinity() shouldBe true
            NaN.mediant(ZERO).isNaN() shouldBe true
            ZERO.mediant(NaN).isNaN() shouldBe true
            POSITIVE_INFINITY.mediant(NaN).isNaN() shouldBe true
            NEGATIVE_INFINITY.mediant(NaN).isNaN() shouldBe true
            ZERO.mediant(ZERO) shouldBe ZERO
            POSITIVE_INFINITY.mediant(NEGATIVE_INFINITY) shouldBe ZERO
            NEGATIVE_INFINITY.mediant(POSITIVE_INFINITY) shouldBe ZERO
            POSITIVE_INFINITY.mediant(ZERO) shouldBe ONE
            ZERO.mediant(POSITIVE_INFINITY) shouldBe ONE
            ZERO.mediant(NEGATIVE_INFINITY) shouldBe -ONE
            NEGATIVE_INFINITY.mediant(ZERO) shouldBe -ONE
            ONE.mediant(TWO) shouldBe (3 over 2)
        }

        @Test
        fun `should find continued fraction`() {
            val cfA = (3245 over 1000).toContinuedFraction()
            cfA shouldBe listOf(3 over 1, 4 over 1, 12 over 1, 4 over 1)
            cfA.isFinite() shouldBe true
            cfA.backAgain() shouldBe (3245 over 1000)
            val negCfA = (-3245 over 1000).toContinuedFraction()
            negCfA shouldBe
                listOf(-4 over 1, ONE, 3 over 1, 12 over 1, 4 over 1)
            negCfA.isFinite() shouldBe true
            negCfA.backAgain() shouldBe (-3245 over 1000)
            ZERO.toContinuedFraction() shouldBe listOf(ZERO)
            ONE.toContinuedFraction() shouldBe listOf(ONE)
            (1 over 3).toContinuedFraction() shouldBe listOf(ZERO, 3 over 1)

            val cfNaN = NaN.toContinuedFraction()
            cfNaN.isFinite() shouldBe false
            cfNaN.backAgain().isNaN() shouldBe true
            cfNaN.integerPart.isNaN() shouldBe true
            val cfPosInf = POSITIVE_INFINITY.toContinuedFraction()
            cfPosInf.isFinite() shouldBe false
            cfPosInf.backAgain().isNaN() shouldBe true
            cfPosInf.integerPart.isNaN() shouldBe true
            val cfNegInf = NEGATIVE_INFINITY.toContinuedFraction()
            cfNegInf.isFinite() shouldBe false
            cfNegInf.backAgain().isNaN() shouldBe true
            cfNegInf.integerPart.isNaN() shouldBe true
        }
    }

    @Nested
    inner class CantorSpiral {
        @Test
        fun `should find Cantor spiral`() {
            cantorSpiral().take(10).toList() shouldBe (
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
                )
        }
    }
}
