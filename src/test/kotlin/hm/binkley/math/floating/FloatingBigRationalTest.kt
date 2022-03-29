@file:Suppress("NonAsciiCharacters", "RedundantInnerClassModifier")

package hm.binkley.math.floating

import hm.binkley.math.BFixed
import hm.binkley.math.BFloating
import hm.binkley.math.`^`
import hm.binkley.math.big
import hm.binkley.math.ceil
import hm.binkley.math.compareTo
import hm.binkley.math.dec
import hm.binkley.math.divideAndRemainder
import hm.binkley.math.fixed.FixedBigRational
import hm.binkley.math.floating.FloatingBigRational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational.Companion.POSITIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.TEN
import hm.binkley.math.floating.FloatingBigRational.Companion.TWO
import hm.binkley.math.floating.FloatingBigRational.Companion.ZERO
import hm.binkley.math.floor
import hm.binkley.math.inc
import hm.binkley.math.isDyadic
import hm.binkley.math.rangeTo
import hm.binkley.math.sqrt
import hm.binkley.math.sqrtApproximated
import hm.binkley.math.toBigInteger
import hm.binkley.math.truncate
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
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
        fun `should use constant +∞`() {
            (Long.MAX_VALUE over 0L) shouldBeSameInstanceAs POSITIVE_INFINITY
        }

        @Test
        fun `should use constant -∞`() {
            (Long.MIN_VALUE over 0.big) shouldBeSameInstanceAs NEGATIVE_INFINITY
        }

        @Test
        fun `should provide over as a convenience`() {
            (10L over 1) shouldBe TEN
            (10 over 1L) shouldBe TEN
        }
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
        POSITIVE_INFINITY.toString() shouldBe "Infinity"
        NEGATIVE_INFINITY.toString() shouldBe "-Infinity"
        NaN.toString() shouldBe "NaN"
    }

    @Test
    fun `should divide with remainder`() {
        fun nonFiniteCheck(
            dividend: FloatingBigRational,
            divisor: FloatingBigRational,
            assertion: BigRationalAssertion,
        ) {
            val (quotient, remainder) = dividend.divideAndRemainder(divisor)
            assertion(quotient)
            assertion(remainder)
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
        nonFiniteCheck((13 over 2), NaN) {
            it.isNaN()
        }
        nonFiniteCheck(NaN, (3 over 1)) {
            it.isNaN()
        }
        nonFiniteCheck(NaN, NaN) {
            it.isNaN()
        }
    }

    @Nested
    inner class OperatorTests {
        @Test
        fun `should add`() {
            (ONE + POSITIVE_INFINITY).shouldBePositiveInfinity()
            (POSITIVE_INFINITY + POSITIVE_INFINITY).shouldBePositiveInfinity()
            (POSITIVE_INFINITY + NEGATIVE_INFINITY).shouldBeNaN()
            (ONE + NEGATIVE_INFINITY).shouldBeNegativeInfinity()
            (NEGATIVE_INFINITY + NEGATIVE_INFINITY).shouldBeNegativeInfinity()
            (NEGATIVE_INFINITY + POSITIVE_INFINITY).shouldBeNaN()
        }

        @Test
        fun `should subtract`() {
            (POSITIVE_INFINITY - ONE).shouldBePositiveInfinity()
            (POSITIVE_INFINITY - POSITIVE_INFINITY).shouldBeNaN()
            (POSITIVE_INFINITY - NEGATIVE_INFINITY).shouldBePositiveInfinity()
            (NEGATIVE_INFINITY - ONE).shouldBeNegativeInfinity()
            (NEGATIVE_INFINITY - NEGATIVE_INFINITY).shouldBeNaN()
            (NEGATIVE_INFINITY - POSITIVE_INFINITY).shouldBeNegativeInfinity()
        }

        @Test
        fun `should multiply`() {
            (ONE * POSITIVE_INFINITY).shouldBePositiveInfinity()
            (ONE * NEGATIVE_INFINITY).shouldBeNegativeInfinity()
            (ZERO * POSITIVE_INFINITY).shouldBeNaN()
            (POSITIVE_INFINITY * ZERO).shouldBeNaN()
            (ZERO * NEGATIVE_INFINITY).shouldBeNaN()
            (NEGATIVE_INFINITY * ZERO).shouldBeNaN()
            (POSITIVE_INFINITY * POSITIVE_INFINITY).shouldBePositiveInfinity()
        }

        @Test
        fun `should divide`() {
            (ZERO / POSITIVE_INFINITY) shouldBe ZERO
            (ONE / ZERO).shouldBePositiveInfinity()
            (ZERO / NEGATIVE_INFINITY) shouldBe ZERO
            (-ONE / ZERO).shouldBeNegativeInfinity()
            (NEGATIVE_INFINITY / NEGATIVE_INFINITY).shouldBeNaN()
            (POSITIVE_INFINITY / POSITIVE_INFINITY).shouldBeNaN()
            (ONE / NaN).shouldBeNaN()
            (NaN / ONE).shouldBeNaN()
            (ZERO / ZERO).shouldBeNaN()
        }

        @Test
        fun `should find remainder`() {
            (ONE % POSITIVE_INFINITY) shouldBe ZERO
            (POSITIVE_INFINITY % ONE) shouldBe ZERO
            (ONE % NEGATIVE_INFINITY) shouldBe ZERO
            (NEGATIVE_INFINITY % ONE) shouldBe ZERO
            (ONE % NaN).shouldBeNaN()
            (NaN % ONE).shouldBeNaN()
            (ZERO % ZERO).shouldBeNaN()
        }

        @Test
        fun `should increment`() {
            var nonFinite = POSITIVE_INFINITY
            (++nonFinite).shouldBePositiveInfinity()
            nonFinite = NEGATIVE_INFINITY
            (++nonFinite).shouldBeNegativeInfinity()
            nonFinite = NaN
            (++nonFinite).shouldBeNaN()
        }

        @Test
        fun `should decrement`() {
            var nonFinite = POSITIVE_INFINITY
            (--nonFinite).shouldBePositiveInfinity()
            nonFinite = NEGATIVE_INFINITY
            (--nonFinite).shouldBeNegativeInfinity()
            nonFinite = NaN
            (--nonFinite).shouldBeNaN()
        }
    }

    @Nested
    inner class RoundingTests {
        @Test
        fun `should round towards ceiling`() {
            POSITIVE_INFINITY.ceil().shouldBePositiveInfinity()
            NEGATIVE_INFINITY.ceil().shouldBeNegativeInfinity()
            NaN.ceil().shouldBeNaN()
        }

        @Test
        fun `should round towards floor`() {
            POSITIVE_INFINITY.floor().shouldBePositiveInfinity()
            NEGATIVE_INFINITY.floor().shouldBeNegativeInfinity()
            NaN.floor().shouldBeNaN()
        }

        @Test
        fun `should round towards 0`() {
            POSITIVE_INFINITY.truncate().shouldBePositiveInfinity()
            NEGATIVE_INFINITY.truncate().shouldBeNegativeInfinity()
            NaN.truncate().shouldBeNaN()
        }
    }

    @Nested
    inner class SpecialCasesTests {
        @Test
        fun `should round trip NaN and infinities`() {
            POSITIVE_INFINITY.toDouble().toBigRational() shouldBe
                POSITIVE_INFINITY
            NEGATIVE_INFINITY.toDouble().toBigRational() shouldBe
                NEGATIVE_INFINITY
            NaN.toDouble().toBigRational() shouldBe NaN

            POSITIVE_INFINITY.toFloat().toBigRational() shouldBe
                POSITIVE_INFINITY
            NEGATIVE_INFINITY.toFloat().toBigRational() shouldBe
                NEGATIVE_INFINITY
            NaN.toFloat().toBigRational() shouldBe NaN
        }

        @Test
        fun `should be infinity`() {
            (2 over 0).shouldBePositiveInfinity()
            (-2 over 0).shouldBeNegativeInfinity()
        }

        @Test
        fun `should check finitude`() {
            ZERO.isFinite().shouldBeTrue()
            POSITIVE_INFINITY.isFinite().shouldBeFalse()
            NEGATIVE_INFINITY.isFinite().shouldBeFalse()
            NaN.isFinite().shouldBeFalse()
        }

        @Test
        fun `should check infinitude`() {
            ZERO.isInfinite().shouldBeFalse()
            POSITIVE_INFINITY.isInfinite().shouldBeTrue()
            NEGATIVE_INFINITY.isInfinite().shouldBeTrue()
            NaN.isInfinite().shouldBeFalse()
        }

        @Test
        fun `should propagate NaN`() {
            (ZERO + NaN).shouldBeNaN()
            (NaN + NaN).shouldBeNaN()
            (NaN + ONE).shouldBeNaN()
            (NaN - ZERO).shouldBeNaN()
            (NaN - NaN).shouldBeNaN()
            (ZERO - NaN).shouldBeNaN()
            (ONE * NaN).shouldBeNaN()
            (NaN * NaN).shouldBeNaN()
            (NaN * ONE).shouldBeNaN()
            (NaN / ONE).shouldBeNaN()
            (NaN / NaN).shouldBeNaN()
            (ONE / NaN).shouldBeNaN()
            (NaN % ONE).shouldBeNaN()
            (NaN % NaN).shouldBeNaN()
            (ONE % NaN).shouldBeNaN()
        }

        @Test
        fun `should propagate infinities`() {
            (-NEGATIVE_INFINITY).shouldBePositiveInfinity()
            (ONE + POSITIVE_INFINITY).shouldBePositiveInfinity()
            (NEGATIVE_INFINITY - ONE).shouldBeNegativeInfinity()
            (POSITIVE_INFINITY + NEGATIVE_INFINITY).shouldBeNaN()
            (POSITIVE_INFINITY * POSITIVE_INFINITY).shouldBePositiveInfinity()
            (POSITIVE_INFINITY * NEGATIVE_INFINITY).shouldBeNegativeInfinity()
            (NEGATIVE_INFINITY * NEGATIVE_INFINITY).shouldBePositiveInfinity()
            (POSITIVE_INFINITY / POSITIVE_INFINITY).shouldBeNaN()
            (POSITIVE_INFINITY / NEGATIVE_INFINITY).shouldBeNaN()
            (NEGATIVE_INFINITY / NEGATIVE_INFINITY).shouldBeNaN()
        }

        @Test
        fun `should invert infinities incorrectly`() {
            (ONE / POSITIVE_INFINITY) shouldBe ZERO
            (ONE / NEGATIVE_INFINITY) shouldBe ZERO
        }

        @Test
        fun `should cope with various infinities`() {
            (ZERO * POSITIVE_INFINITY).shouldBeNaN()
            (ZERO / POSITIVE_INFINITY) shouldBe ZERO
            (POSITIVE_INFINITY / ZERO).shouldBePositiveInfinity()
            (ZERO * NEGATIVE_INFINITY).shouldBeNaN()
            (ZERO / NEGATIVE_INFINITY) shouldBe ZERO
            (NEGATIVE_INFINITY / ZERO).shouldBeNegativeInfinity()
            (POSITIVE_INFINITY * NEGATIVE_INFINITY).shouldBeNegativeInfinity()
            (POSITIVE_INFINITY / NEGATIVE_INFINITY).shouldBeNaN()
        }

        @Test
        fun `should understand equalities of non-finite values`() {
            POSITIVE_INFINITY shouldBe POSITIVE_INFINITY
            NEGATIVE_INFINITY shouldBe NEGATIVE_INFINITY
            // Cannot use shouldNotBe: It short-circuits for === objects
            @Suppress("KotlinConstantConditions")
            (NaN == NaN).shouldBeFalse()
        }
    }

    @Test
    fun `should note integer rationals`() {
        POSITIVE_INFINITY.isWhole().shouldBeFalse()
        NEGATIVE_INFINITY.isWhole().shouldBeFalse()
        NaN.isWhole().shouldBeFalse()
    }

    @Test
    fun `should note p-adic rationals`() {
        (1 over 3).isPAdic(3).shouldBeTrue()
        (2 over 5).isPAdic(3).shouldBeFalse()
        POSITIVE_INFINITY.isPAdic(3).shouldBeFalse()
        NEGATIVE_INFINITY.isPAdic(3).shouldBeFalse()
        NaN.isPAdic(3).shouldBeFalse()
    }

    @Test
    fun `should note dyadic rationals`() {
        (1 over 2).isDyadic().shouldBeTrue()
        POSITIVE_INFINITY.isDyadic().shouldBeFalse()
        NEGATIVE_INFINITY.isDyadic().shouldBeFalse()
        NaN.isDyadic().shouldBeFalse()
    }

    @Nested
    inner class ProgressionTests {
        @Test
        fun `should equate`() {
            (ONE..TEN).equals(this).shouldBeFalse()
            (ONE..TEN) shouldNotBe
                FixedBigRational.ONE..FixedBigRational.TEN
            (ONE..TEN).hashCode() shouldNotBe
                (FixedBigRational.ONE..FixedBigRational.TEN).hashCode()
        }
    }

    @Nested
    inner class ConversionTests {
        @Test
        fun `should be a number`() {
            ONE.toDouble() shouldBe 1.0
            POSITIVE_INFINITY.toDouble() shouldBe Double.POSITIVE_INFINITY
            NEGATIVE_INFINITY.toDouble() shouldBe Double.NEGATIVE_INFINITY
            NaN.toDouble() shouldBe Double.NaN

            ONE.toFloat() shouldBe 1.0f
            POSITIVE_INFINITY.toFloat() shouldBe Float.POSITIVE_INFINITY
            NEGATIVE_INFINITY.toFloat() shouldBe Float.NEGATIVE_INFINITY
            NaN.toFloat() shouldBe Float.NaN
        }

        @Test
        fun `should not convert extrema to big decimal`() {
            // Note JDK rules for BigDecimal->Double->BigDecimal, and handling
            // of string _vs_ double/long inputs
            ONE.toBigDecimal() shouldBe BFloating("1.0")
            ONE.toBigDecimal(1) shouldBe BFloating("1.0")

            shouldThrow<ArithmeticException> {
                POSITIVE_INFINITY.toBigDecimal()
            }
            shouldThrow<ArithmeticException> {
                POSITIVE_INFINITY.toBigDecimal(1)
            }
            shouldThrow<ArithmeticException> {
                NEGATIVE_INFINITY.toBigDecimal()
            }
            shouldThrow<ArithmeticException> {
                NEGATIVE_INFINITY.toBigDecimal(1)
            }
            shouldThrow<ArithmeticException> {
                NaN.toBigDecimal()
            }
            shouldThrow<ArithmeticException> {
                NaN.toBigDecimal(1)
            }
        }

        @Test
        fun `should not convert extrema to big integer`() {
            ONE.toBigInteger() shouldBe BFixed.ONE

            shouldThrow<ArithmeticException> {
                POSITIVE_INFINITY.toBigInteger()
            }
            shouldThrow<ArithmeticException> {
                NEGATIVE_INFINITY.toBigInteger()
            }
            shouldThrow<ArithmeticException> {
                NaN.toBigInteger()
            }
        }

        @Test
        fun `should not convert extrema to long`() {
            shouldThrow<ArithmeticException> {
                POSITIVE_INFINITY.toLong()
            }
            shouldThrow<ArithmeticException> {
                NEGATIVE_INFINITY.toLong()
            }
            shouldThrow<ArithmeticException> {
                NaN.toLong()
            }
        }

        @Test
        fun `should not convert extrema to int`() {
            shouldThrow<ArithmeticException> {
                POSITIVE_INFINITY.toInt()
            }
            shouldThrow<ArithmeticException> {
                NEGATIVE_INFINITY.toInt()
            }
            shouldThrow<ArithmeticException> {
                NaN.toInt()
            }
        }

        @Test
        fun `should convert big decimal in infix constructor`() {
            0.0.big.toBigRational() shouldBe ZERO
            30.0.big.toBigRational() shouldBe (30 over 1)
            3.0.big.toBigRational() shouldBe (3 over 1)
            BFloating("0.3").toBigRational() shouldBe (3 over 10)
            BFloating("7.70").toBigRational() shouldBe (77 over 10)
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
        fun `should convert big integer in infix constructor`() {
            0.big.toBigRational() shouldBe ZERO
            BFixed.valueOf(30L).toBigRational() shouldBe (30 over 1)
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
                10.0,
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
                TWO,
                3 over 1,
                4 over 1,
                TEN,
                15432 over 125
            )

            Double.POSITIVE_INFINITY.toBigRational().shouldBePositiveInfinity()
            Double.NEGATIVE_INFINITY.toBigRational().shouldBeNegativeInfinity()
            Double.NaN.toBigRational().shouldBeNaN()
            (-0.0).toBigRational() shouldBe ZERO

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
                10.0f,
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
                TWO,
                3 over 1,
                4 over 1,
                TEN,
                15432 over 125
            )

            Float.POSITIVE_INFINITY.toBigRational().shouldBePositiveInfinity()
            Float.NEGATIVE_INFINITY.toBigRational().shouldBeNegativeInfinity()
            Float.NaN.toBigRational().shouldBeNaN()
            (-0.0f).toBigRational() shouldBe ZERO

            floats.map {
                it.toBigRational()
            } shouldBe rationals

            rationals.map {
                it.toFloat()
            } shouldBe floats
        }

        @Test
        fun `should convert to fixed equivalent or complain`() {
            ONE.toFixedBigRational() shouldBe FixedBigRational.ONE

            shouldThrow<ArithmeticException> {
                NaN.toFixedBigRational()
            }
            shouldThrow<ArithmeticException> {
                POSITIVE_INFINITY.toFixedBigRational()
            }
            shouldThrow<ArithmeticException> {
                NEGATIVE_INFINITY.toFixedBigRational()
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

            (sorted.map { it.toDouble() }) shouldBe doubleSorted
        }

        @Test
        fun `should compare other number types`() {
            (Double.POSITIVE_INFINITY > ZERO).shouldBeTrue()
            (ZERO > Double.NEGATIVE_INFINITY).shouldBeTrue()
            (Float.NaN > ZERO).shouldBeTrue()
            (NaN > Float.MAX_VALUE).shouldBeTrue()
        }

        @Test
        fun `should not order NaN values`() {
            @Suppress("KotlinConstantConditions")
            (NaN == NaN).shouldBeFalse()
            (NaN > NaN).shouldBeFalse()
            (NaN < NaN).shouldBeFalse()
        }

        @Test
        fun `should reciprocate`() {
            POSITIVE_INFINITY.unaryDiv() shouldBe ZERO
            NEGATIVE_INFINITY.unaryDiv() shouldBe ZERO
            NaN.unaryDiv().shouldBeNaN()
        }

        @Test
        fun `should absolute`() {
            NaN.absoluteValue.shouldBeNaN()
            POSITIVE_INFINITY.absoluteValue
                .shouldBePositiveInfinity()
            NEGATIVE_INFINITY.absoluteValue
                .shouldBePositiveInfinity()
        }

        @Test
        fun `should signum`() {
            ZERO.sign shouldBe ZERO
            NEGATIVE_INFINITY.sign shouldBe -ONE
            POSITIVE_INFINITY.sign shouldBe ONE
            NaN.sign.shouldBeNaN()
        }

        @Test
        fun `should raise`() {
            POSITIVE_INFINITY `^` 2 shouldBe POSITIVE_INFINITY
            POSITIVE_INFINITY `^` -1 shouldBe ZERO

            NEGATIVE_INFINITY `^` 3 shouldBe NEGATIVE_INFINITY
            NEGATIVE_INFINITY `^` 2 shouldBe POSITIVE_INFINITY
            NEGATIVE_INFINITY `^` -1 shouldBe ZERO

            (NaN `^` 2).shouldBeNaN()
            (POSITIVE_INFINITY `^` 0).shouldBeNaN()
            (NEGATIVE_INFINITY `^` 0).shouldBeNaN()
        }

        @Test
        fun `should square root`() {
            NaN.sqrt().shouldBeNaN()
            POSITIVE_INFINITY.sqrt() shouldBe POSITIVE_INFINITY
        }

        @Test
        fun `should square root approximately`() {
            NaN.sqrtApproximated().shouldBeNaN()
            POSITIVE_INFINITY.sqrtApproximated() shouldBe POSITIVE_INFINITY
        }

        @Test
        fun `should find between`() {
            NaN.mediant(NaN).shouldBeNaN()
            NaN.mediant(POSITIVE_INFINITY).shouldBeNaN()
            NaN.mediant(NEGATIVE_INFINITY).shouldBeNaN()
            POSITIVE_INFINITY.mediant(POSITIVE_INFINITY)
                .shouldBePositiveInfinity()
            NEGATIVE_INFINITY.mediant(NEGATIVE_INFINITY)
                .shouldBeNegativeInfinity()
            NaN.mediant(ZERO).shouldBeNaN()
            ZERO.mediant(NaN).shouldBeNaN()
            POSITIVE_INFINITY.mediant(NaN).shouldBeNaN()
            NEGATIVE_INFINITY.mediant(NaN).shouldBeNaN()
            POSITIVE_INFINITY.mediant(NEGATIVE_INFINITY) shouldBe ZERO
            NEGATIVE_INFINITY.mediant(POSITIVE_INFINITY) shouldBe ZERO
            POSITIVE_INFINITY.mediant(ZERO) shouldBe ONE
            ZERO.mediant(POSITIVE_INFINITY) shouldBe ONE
            ZERO.mediant(NEGATIVE_INFINITY) shouldBe -ONE
            NEGATIVE_INFINITY.mediant(ZERO) shouldBe -ONE
        }

        @Test
        fun `should find continued fraction`() {
            val cfA = (3245 over 1000).toContinuedFraction()
            cfA.isFinite().shouldBeTrue()
            val negCfA = (-3245 over 1000).toContinuedFraction()
            negCfA.isFinite().shouldBeTrue()

            val cfNaN = NaN.toContinuedFraction()
            cfNaN.isFinite().shouldBeFalse()
            cfNaN.toBigRational().shouldBeNaN()
            cfNaN.integerPart.shouldBeNaN()
            val cfPosInf = POSITIVE_INFINITY.toContinuedFraction()
            cfPosInf.isFinite().shouldBeFalse()
            cfPosInf.toBigRational().shouldBeNaN()
            cfPosInf.integerPart.shouldBeNaN()
            val cfNegInf = NEGATIVE_INFINITY.toContinuedFraction()
            cfNegInf.isFinite().shouldBeFalse()
            cfNegInf.toBigRational().shouldBeNaN()
            cfNegInf.integerPart.shouldBeNaN()
        }
    }
}

private fun FloatingBigRational.shouldBePositiveInfinity() =
    isPositiveInfinity().shouldBeTrue()

private fun FloatingBigRational.shouldBeNegativeInfinity() =
    isNegativeInfinity().shouldBeTrue()

private fun FloatingBigRational.shouldBeNaN() = isNaN().shouldBeTrue()
