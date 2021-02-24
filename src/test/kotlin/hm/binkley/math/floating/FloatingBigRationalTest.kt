@file:Suppress("NonAsciiCharacters")

package hm.binkley.math.floating

import hm.binkley.math.BInt
import hm.binkley.math.backAgain
import hm.binkley.math.big
import hm.binkley.math.ceil
import hm.binkley.math.compareTo
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
import hm.binkley.math.fraction
import hm.binkley.math.rangeTo
import hm.binkley.math.sqrt
import hm.binkley.math.sqrtApproximated
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

    @Suppress("ReplaceCallWithBinaryOperator")
    @Test
    fun `should be itself`() {
        POSITIVE_INFINITY.equals(POSITIVE_INFINITY).shouldBeTrue()
        NEGATIVE_INFINITY.equals(NEGATIVE_INFINITY).shouldBeTrue()
        NaN.equals(NaN).shouldBeFalse()
    }

    @Test
    fun `should hash separately`() {
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
            (ONE + POSITIVE_INFINITY).isPositiveInfinity().shouldBeTrue()
            (POSITIVE_INFINITY + POSITIVE_INFINITY)
                .isPositiveInfinity().shouldBeTrue()
            (POSITIVE_INFINITY + NEGATIVE_INFINITY).isNaN().shouldBeTrue()
            (ONE + NEGATIVE_INFINITY).isNegativeInfinity().shouldBeTrue()
            (NEGATIVE_INFINITY + NEGATIVE_INFINITY)
                .isNegativeInfinity().shouldBeTrue()
            (NEGATIVE_INFINITY + POSITIVE_INFINITY).isNaN().shouldBeTrue()
        }

        @Test
        fun `should subtract`() {
            (POSITIVE_INFINITY - ONE).isPositiveInfinity().shouldBeTrue()
            (POSITIVE_INFINITY - POSITIVE_INFINITY).isNaN().shouldBeTrue()
            (POSITIVE_INFINITY - NEGATIVE_INFINITY)
                .isPositiveInfinity().shouldBeTrue()
            (NEGATIVE_INFINITY - ONE).isNegativeInfinity().shouldBeTrue()
            (NEGATIVE_INFINITY - NEGATIVE_INFINITY).isNaN().shouldBeTrue()
            (NEGATIVE_INFINITY - POSITIVE_INFINITY)
                .isNegativeInfinity().shouldBeTrue()
        }

        @Test
        fun `should multiply`() {
            (ONE * POSITIVE_INFINITY).isPositiveInfinity().shouldBeTrue()
            (ONE * NEGATIVE_INFINITY).isNegativeInfinity().shouldBeTrue()
            (ZERO * POSITIVE_INFINITY).isNaN().shouldBeTrue()
            (POSITIVE_INFINITY * ZERO).isNaN().shouldBeTrue()
            (ZERO * NEGATIVE_INFINITY).isNaN().shouldBeTrue()
            (NEGATIVE_INFINITY * ZERO).isNaN().shouldBeTrue()
            (POSITIVE_INFINITY * POSITIVE_INFINITY)
                .isPositiveInfinity().shouldBeTrue()
        }

        @Test
        fun `should divide`() {
            (ZERO / POSITIVE_INFINITY) shouldBe ZERO
            (ONE / ZERO).isPositiveInfinity().shouldBeTrue()
            (ZERO / NEGATIVE_INFINITY) shouldBe ZERO
            (-ONE / ZERO).isNegativeInfinity().shouldBeTrue()
            (NEGATIVE_INFINITY / NEGATIVE_INFINITY).isNaN().shouldBeTrue()
            (POSITIVE_INFINITY / POSITIVE_INFINITY).isNaN().shouldBeTrue()
            (ONE / NaN).isNaN().shouldBeTrue()
            (ZERO / ZERO).isNaN().shouldBeTrue()
        }

        @Test
        fun `should find remainder`() {
            (ONE % POSITIVE_INFINITY) shouldBe ZERO
            (ONE % NEGATIVE_INFINITY) shouldBe ZERO
            (ONE % NaN).isNaN().shouldBeTrue()
        }

        @Test
        fun `should increment`() {
            var nonFinite = POSITIVE_INFINITY
            (++nonFinite).isPositiveInfinity().shouldBeTrue()
            nonFinite = NEGATIVE_INFINITY
            (++nonFinite).isNegativeInfinity().shouldBeTrue()
            nonFinite = NaN
            (++nonFinite).isNaN().shouldBeTrue()
        }

        @Test
        fun `should decrement`() {
            var nonFinite = POSITIVE_INFINITY
            (--nonFinite).isPositiveInfinity().shouldBeTrue()
            nonFinite = NEGATIVE_INFINITY
            (--nonFinite).isNegativeInfinity().shouldBeTrue()
            nonFinite = NaN
            (--nonFinite).isNaN().shouldBeTrue()
        }
    }

    @Nested
    inner class RoundingTests {
        @Test
        fun `should round towards ceiling`() {
            POSITIVE_INFINITY.ceil().isPositiveInfinity().shouldBeTrue()
            NEGATIVE_INFINITY.ceil().isNegativeInfinity().shouldBeTrue()
            NaN.ceil().isNaN().shouldBeTrue()
        }

        @Test
        fun `should round towards floor`() {
            POSITIVE_INFINITY.floor().isPositiveInfinity().shouldBeTrue()
            NEGATIVE_INFINITY.floor().isNegativeInfinity().shouldBeTrue()
            NaN.floor().isNaN().shouldBeTrue()
        }

        @Test
        fun `should round towards 0`() {
            POSITIVE_INFINITY.truncate().isPositiveInfinity().shouldBeTrue()
            NEGATIVE_INFINITY.truncate().isNegativeInfinity().shouldBeTrue()
            NaN.truncate().isNaN().shouldBeTrue()
        }

        @Test
        fun `should fractionate`() {
            POSITIVE_INFINITY.fraction().isNaN().shouldBeTrue()
            NEGATIVE_INFINITY.fraction().isNaN().shouldBeTrue()
            NaN.fraction().isNaN().shouldBeTrue()
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
            (2 over 0).isPositiveInfinity().shouldBeTrue()
            (-2 over 0).isNegativeInfinity().shouldBeTrue()
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
            (ZERO + NaN).isNaN().shouldBeTrue()
            (NaN + NaN).isNaN().shouldBeTrue()
            (NaN + ONE).isNaN().shouldBeTrue()
            (NaN - ZERO).isNaN().shouldBeTrue()
            (NaN - NaN).isNaN().shouldBeTrue()
            (ZERO - NaN).isNaN().shouldBeTrue()
            (ONE * NaN).isNaN().shouldBeTrue()
            (NaN * NaN).isNaN().shouldBeTrue()
            (NaN * ONE).isNaN().shouldBeTrue()
            (NaN / ONE).isNaN().shouldBeTrue()
            (NaN / NaN).isNaN().shouldBeTrue()
            (ONE / NaN).isNaN().shouldBeTrue()
        }

        @Test
        fun `should propagate infinities`() {
            (-NEGATIVE_INFINITY).isPositiveInfinity().shouldBeTrue()
            (ONE + POSITIVE_INFINITY).isPositiveInfinity().shouldBeTrue()
            (NEGATIVE_INFINITY - ONE).isNegativeInfinity().shouldBeTrue()
            (POSITIVE_INFINITY + NEGATIVE_INFINITY).isNaN().shouldBeTrue()
            (POSITIVE_INFINITY * POSITIVE_INFINITY)
                .isPositiveInfinity().shouldBeTrue()
            (POSITIVE_INFINITY * NEGATIVE_INFINITY)
                .isNegativeInfinity().shouldBeTrue()
            (NEGATIVE_INFINITY * NEGATIVE_INFINITY)
                .isPositiveInfinity().shouldBeTrue()
            (POSITIVE_INFINITY / POSITIVE_INFINITY).isNaN().shouldBeTrue()
            (POSITIVE_INFINITY / NEGATIVE_INFINITY).isNaN().shouldBeTrue()
            (NEGATIVE_INFINITY / NEGATIVE_INFINITY).isNaN().shouldBeTrue()
        }

        @Test
        fun `should invert infinities incorrectly`() {
            (ONE / POSITIVE_INFINITY) shouldBe ZERO
            (ONE / NEGATIVE_INFINITY) shouldBe ZERO
        }

        @Test
        fun `should cope with various infinities`() {
            (ZERO * POSITIVE_INFINITY).isNaN().shouldBeTrue()
            (ZERO / POSITIVE_INFINITY) shouldBe ZERO
            (POSITIVE_INFINITY / ZERO).isPositiveInfinity().shouldBeTrue()
            (ZERO * NEGATIVE_INFINITY).isNaN().shouldBeTrue()
            (ZERO / NEGATIVE_INFINITY) shouldBe ZERO
            (NEGATIVE_INFINITY / ZERO).isNegativeInfinity().shouldBeTrue()
            (POSITIVE_INFINITY * NEGATIVE_INFINITY)
                .isNegativeInfinity().shouldBeTrue()
            (POSITIVE_INFINITY / NEGATIVE_INFINITY).isNaN().shouldBeTrue()
        }

        @Test
        fun `should understand equalities of non-finite values`() {
            POSITIVE_INFINITY shouldBe POSITIVE_INFINITY
            NEGATIVE_INFINITY shouldBe NEGATIVE_INFINITY
            // Cannot use shouldNotBe: It short-circuits for === objects
            (NaN == NaN).shouldBeFalse()
        }
    }

    @Test
    fun `should note integer rationals`() {
        POSITIVE_INFINITY.isInteger().shouldBeFalse()
        NEGATIVE_INFINITY.isInteger().shouldBeFalse()
        NaN.isInteger().shouldBeFalse()
    }

    @Test
    fun `should note dyadic rationals`() {
        POSITIVE_INFINITY.isDyadic().shouldBeFalse()
        NEGATIVE_INFINITY.isDyadic().shouldBeFalse()
        NaN.isDyadic().shouldBeFalse()
    }

    @Test
    fun `should note p-adic rationals`() {
        POSITIVE_INFINITY.isPAdic(3).shouldBeFalse()
        NEGATIVE_INFINITY.isPAdic(3).shouldBeFalse()
        NaN.isPAdic(3).shouldBeFalse()
    }

    @Test
    fun `should not be a fixed big rational range`() {
        (ONE..TEN) shouldNotBe
            FixedBigRational.ONE..FixedBigRational.TEN
        (ONE..TEN).hashCode() shouldNotBe
            (FixedBigRational.ONE..FixedBigRational.TEN).hashCode()
    }

    @Nested
    inner class ConversionTests {
        /** @todo Rationalize with following test */
        @Test
        fun `should be a number`() {
            POSITIVE_INFINITY.toDouble() shouldBe Double.POSITIVE_INFINITY
            NEGATIVE_INFINITY.toDouble() shouldBe Double.NEGATIVE_INFINITY
            NaN.toDouble() shouldBe Double.NaN

            POSITIVE_INFINITY.toFloat() shouldBe Float.POSITIVE_INFINITY
            NEGATIVE_INFINITY.toFloat() shouldBe Float.NEGATIVE_INFINITY
            NaN.toFloat() shouldBe Float.NaN
        }

        @Test
        fun `should not convert extrema to big decimal`() {
            // Note JDK rules for BigDecimal->Double->BigDecimal
            ONE.toBigDecimal() shouldBe "1.0".big
            ONE.toBigDecimal(1) shouldBe "1.0".big

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
            ONE.toBigInteger() shouldBe BInt.ONE

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
        fun `should convert big integer in infix constructor`() {
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

            Double.NEGATIVE_INFINITY.toBigRational()
                .isNegativeInfinity().shouldBeTrue()
            Double.POSITIVE_INFINITY.toBigRational()
                .isPositiveInfinity().shouldBeTrue()
            Double.NaN.toBigRational().isNaN().shouldBeTrue()

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

            Float.NEGATIVE_INFINITY.toBigRational()
                .isNegativeInfinity().shouldBeTrue()
            Float.POSITIVE_INFINITY.toBigRational()
                .isPositiveInfinity().shouldBeTrue()
            Float.NaN.toBigRational().isNaN().shouldBeTrue()

            floats.map {
                it.toBigRational()
            } shouldBe rationals

            rationals.map {
                it.toFloat()
            } shouldBe floats
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
            (NaN == NaN).shouldBeFalse()
            (NaN > NaN).shouldBeFalse()
            (NaN < NaN).shouldBeFalse()
        }

        @Test
        fun `should reciprocate`() {
            POSITIVE_INFINITY.unaryDiv() shouldBe ZERO
            NEGATIVE_INFINITY.unaryDiv() shouldBe ZERO
            NaN.unaryDiv().isNaN().shouldBeTrue()
        }

        @Test
        fun `should absolute`() {
            NaN.absoluteValue.isNaN().shouldBeTrue()
            POSITIVE_INFINITY.absoluteValue
                .isPositiveInfinity().shouldBeTrue()
            NEGATIVE_INFINITY.absoluteValue
                .isPositiveInfinity().shouldBeTrue()
        }

        @Test
        fun `should signum`() {
            (NEGATIVE_INFINITY.sign) shouldBe -ONE
            (POSITIVE_INFINITY.sign) shouldBe ONE
            NaN.sign.isNaN().shouldBeTrue()
        }

        @Test
        fun `should exponentiate`() {
            POSITIVE_INFINITY.pow(2) shouldBe POSITIVE_INFINITY
            POSITIVE_INFINITY.pow(-1) shouldBe ZERO
            NEGATIVE_INFINITY.pow(3) shouldBe NEGATIVE_INFINITY
            NEGATIVE_INFINITY.pow(2) shouldBe POSITIVE_INFINITY
            NEGATIVE_INFINITY.pow(-1) shouldBe ZERO
            NaN.pow(2).isNaN().shouldBeTrue()
            POSITIVE_INFINITY.pow(0).isNaN().shouldBeTrue()
            NEGATIVE_INFINITY.pow(0).isNaN().shouldBeTrue()
        }

        @Test
        fun `should square root`() {
            NaN.sqrt().isNaN().shouldBeTrue()
            POSITIVE_INFINITY.sqrt() shouldBe POSITIVE_INFINITY
        }

        @Test
        fun `should square root approximately`() {
            NaN.sqrtApproximated().isNaN().shouldBeTrue()
            POSITIVE_INFINITY.sqrtApproximated() shouldBe POSITIVE_INFINITY
        }

        @Test
        fun `should find between`() {
            NaN.mediant(NaN).isNaN().shouldBeTrue()
            NaN.mediant(POSITIVE_INFINITY).isNaN().shouldBeTrue()
            NaN.mediant(NEGATIVE_INFINITY).isNaN().shouldBeTrue()
            POSITIVE_INFINITY.mediant(POSITIVE_INFINITY)
                .isPositiveInfinity().shouldBeTrue()
            NEGATIVE_INFINITY.mediant(NEGATIVE_INFINITY)
                .isNegativeInfinity().shouldBeTrue()
            NaN.mediant(ZERO).isNaN().shouldBeTrue()
            ZERO.mediant(NaN).isNaN().shouldBeTrue()
            POSITIVE_INFINITY.mediant(NaN).isNaN().shouldBeTrue()
            NEGATIVE_INFINITY.mediant(NaN).isNaN().shouldBeTrue()
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
            cfNaN.backAgain().isNaN().shouldBeTrue()
            cfNaN.integerPart.isNaN().shouldBeTrue()
            val cfPosInf = POSITIVE_INFINITY.toContinuedFraction()
            cfPosInf.isFinite().shouldBeFalse()
            cfPosInf.backAgain().isNaN().shouldBeTrue()
            cfPosInf.integerPart.isNaN().shouldBeTrue()
            val cfNegInf = NEGATIVE_INFINITY.toContinuedFraction()
            cfNegInf.isFinite().shouldBeFalse()
            cfNegInf.backAgain().isNaN().shouldBeTrue()
            cfNegInf.integerPart.isNaN().shouldBeTrue()
        }
    }
}
