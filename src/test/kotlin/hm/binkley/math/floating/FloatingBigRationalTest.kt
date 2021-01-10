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
import hm.binkley.math.floating.FloatingBigRational.Companion.cantorSpiral
import hm.binkley.math.floor
import hm.binkley.math.fraction
import hm.binkley.math.rangeTo
import hm.binkley.math.sqrt
import hm.binkley.math.sqrtApproximated
import hm.binkley.math.truncate
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
        POSITIVE_INFINITY.equals(POSITIVE_INFINITY) shouldBe true
        NEGATIVE_INFINITY.equals(NEGATIVE_INFINITY) shouldBe true
        NaN.equals(NaN) shouldBe false
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
            (ZERO / POSITIVE_INFINITY) shouldBe ZERO
            (ONE / ZERO).isPositiveInfinity() shouldBe true
            (ZERO / NEGATIVE_INFINITY) shouldBe ZERO
            (-ONE / ZERO).isNegativeInfinity() shouldBe true
            (NEGATIVE_INFINITY / NEGATIVE_INFINITY).isNaN() shouldBe true
            (POSITIVE_INFINITY / POSITIVE_INFINITY).isNaN() shouldBe true
            (ONE / NaN).isNaN() shouldBe true
            (ZERO / ZERO).isNaN() shouldBe true
        }

        @Test
        fun `should find remainder`() {
            (ONE % POSITIVE_INFINITY) shouldBe ZERO
            (ONE % NEGATIVE_INFINITY) shouldBe ZERO
            (ONE % NaN).isNaN() shouldBe true
        }

        @Test
        fun `should increment`() {
            var nonFinite = POSITIVE_INFINITY
            (++nonFinite).isPositiveInfinity() shouldBe true
            nonFinite = NEGATIVE_INFINITY
            (++nonFinite).isNegativeInfinity() shouldBe true
            nonFinite = NaN
            (++nonFinite).isNaN() shouldBe true
        }

        @Test
        fun `should decrement`() {
            var nonFinite = POSITIVE_INFINITY
            (--nonFinite).isPositiveInfinity() shouldBe true
            nonFinite = NEGATIVE_INFINITY
            (--nonFinite).isNegativeInfinity() shouldBe true
            nonFinite = NaN
            (--nonFinite).isNaN() shouldBe true
        }
    }

    @Nested
    inner class RoundingTests {
        @Test
        fun `should round towards ceiling`() {
            POSITIVE_INFINITY.ceil().isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.ceil().isNegativeInfinity() shouldBe true
            NaN.ceil().isNaN() shouldBe true
        }

        @Test
        fun `should round towards floor`() {
            POSITIVE_INFINITY.floor().isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.floor().isNegativeInfinity() shouldBe true
            NaN.floor().isNaN() shouldBe true
        }

        @Test
        fun `should round towards 0`() {
            POSITIVE_INFINITY.truncate().isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.truncate().isNegativeInfinity() shouldBe true
            NaN.truncate().isNaN() shouldBe true
        }

        @Test
        fun `should fractionate`() {
            POSITIVE_INFINITY.fraction().isNaN() shouldBe true
            NEGATIVE_INFINITY.fraction().isNaN() shouldBe true
            NaN.fraction().isNaN() shouldBe true
        }
    }

    @Nested
    inner class SpecialCasesTests {
        @Test
        fun `should round trip NaN and infinities`() {
            POSITIVE_INFINITY.toDouble().toBigRational()
                .isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.toDouble().toBigRational()
                .isNegativeInfinity() shouldBe true
            NaN.toDouble().toBigRational().isNaN() shouldBe true

            POSITIVE_INFINITY.toFloat().toBigRational()
                .isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.toFloat().toBigRational()
                .isNegativeInfinity() shouldBe true
            NaN.toFloat().toBigRational().isNaN() shouldBe true
        }

        @Test
        fun `should round trip as double precision`() {
            Double.MAX_VALUE.toBigRational().toDouble() shouldBe
                Double.MAX_VALUE
            Double.MIN_VALUE.toBigRational().toDouble() shouldBe
                Double.MIN_VALUE
            Double.NaN.toBigRational().toDouble() shouldBe Double.NaN
        }

        @Test
        fun `should round trip as single precision`() {
            Float.MAX_VALUE.toBigRational().toFloat() shouldBe
                Float.MAX_VALUE
            Float.MIN_VALUE.toBigRational().toFloat() shouldBe
                Float.MIN_VALUE
            Float.NaN.toBigRational().toFloat() shouldBe Float.NaN
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
        POSITIVE_INFINITY.isInteger() shouldBe false
        NEGATIVE_INFINITY.isInteger() shouldBe false
        NaN.isInteger() shouldBe false
    }

    @Test
    fun `should note dyadic rationals`() {
        POSITIVE_INFINITY.isDyadic() shouldBe false
        NEGATIVE_INFINITY.isDyadic() shouldBe false
        NaN.isDyadic() shouldBe false
    }

    @Test
    fun `should note p-adic rationals`() {
        POSITIVE_INFINITY.isPAdic(3) shouldBe false
        NEGATIVE_INFINITY.isPAdic(3) shouldBe false
        NaN.isPAdic(3) shouldBe false
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

            Double.NEGATIVE_INFINITY.toBigRational()
                .isNegativeInfinity() shouldBe true
            Double.POSITIVE_INFINITY.toBigRational()
                .isPositiveInfinity() shouldBe true
            Double.NaN.toBigRational().isNaN() shouldBe true

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
                .isNegativeInfinity() shouldBe true
            Float.POSITIVE_INFINITY.toBigRational()
                .isPositiveInfinity() shouldBe true
            Float.NaN.toBigRational().isNaN() shouldBe true

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
            (Double.POSITIVE_INFINITY > ZERO) shouldBe true
            (ZERO > Double.NEGATIVE_INFINITY) shouldBe true
            (Float.NaN > ZERO) shouldBe true
            (NaN > Float.MAX_VALUE) shouldBe true
        }

        @Test
        fun `should not order NaN values`() {
            (NaN == NaN) shouldBe false
            (NaN > NaN) shouldBe false
            (NaN < NaN) shouldBe false
        }

        @Test
        fun `should reciprocate`() {
            POSITIVE_INFINITY.unaryDiv() shouldBe ZERO
            NEGATIVE_INFINITY.unaryDiv() shouldBe ZERO
            NaN.unaryDiv().isNaN() shouldBe true
        }

        @Test
        fun `should absolute`() {
            NaN.absoluteValue.isNaN() shouldBe true
            POSITIVE_INFINITY.absoluteValue
                .isPositiveInfinity() shouldBe true
            NEGATIVE_INFINITY.absoluteValue
                .isPositiveInfinity() shouldBe true
        }

        @Test
        fun `should signum`() {
            (NEGATIVE_INFINITY.sign) shouldBe -ONE
            (POSITIVE_INFINITY.sign) shouldBe ONE
            NaN.sign.isNaN() shouldBe true
        }

        @Test
        fun `should exponentiate`() {
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
            NaN.sqrt().isNaN() shouldBe true
            POSITIVE_INFINITY.sqrt() shouldBe POSITIVE_INFINITY
        }

        @Test
        fun `should square root approximately`() {
            NaN.sqrtApproximated().isNaN() shouldBe true
            POSITIVE_INFINITY.sqrtApproximated() shouldBe POSITIVE_INFINITY
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
            cfA.isFinite() shouldBe true
            val negCfA = (-3245 over 1000).toContinuedFraction()
            negCfA.isFinite() shouldBe true

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

    @Test
    fun `should have Cantor spiral`() {
        cantorSpiral() shouldNotBe null
    }
}
