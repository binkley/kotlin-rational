package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.big
import hm.binkley.math.compareTo
import hm.binkley.math.fixed.FixedBigRational.Companion.ONE
import hm.binkley.math.fixed.FixedBigRational.Companion.TEN
import hm.binkley.math.fixed.FixedBigRational.Companion.TWO
import hm.binkley.math.fixed.FixedBigRational.Companion.ZERO
import hm.binkley.math.fixed.FixedBigRational.Companion.cantorSpiral
import hm.binkley.math.floating.FloatingBigRational
import hm.binkley.math.rangeTo
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

// TODO: Range syntax does not pick up the typealias for the 2nd argument
// private typealias BDouble = BigDecimal

/**
 * NB -- the tests use a mixture of constructors while testing functionality.
 * This is intentional, and raises coverage.
 */
internal class FixedBigRationalTest {
    @Test
    fun `should not be a floating big rational range`() {
        (ONE..TEN) shouldNotBe
            FloatingBigRational.ONE..FloatingBigRational.TEN
        (ONE..TEN).hashCode() shouldNotBe
            (FloatingBigRational.ONE..FloatingBigRational.TEN).hashCode()
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
        fun `should convert to and from big decimal`() {
            val decimals = listOf(
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
            ).map { it.toBigDecimal() }
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

            decimals.map {
                it.toBigRational()
            } shouldBe rationals
            rationals.map {
                it.toBigDecimal()
            } shouldBe decimals
        }

        @Test
        fun `should convert to and from double`() {
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
        fun `should convert to and from float`() {
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
    }

    @Nested
    inner class FunctionTests {
        @Test
        fun `should compare other number types`() {
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
            shouldThrow<ArithmeticException> {
                Float.NaN > ZERO
            }
            shouldThrow<ArithmeticException> {
                ZERO < Float.NaN
            }
        }

        @Test
        fun `should multiplicatively invert`() {
            shouldThrow<ArithmeticException> {
                ZERO.unaryDiv()
            }
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

            shouldThrow<UnsupportedOperationException> {
                cfA.toChar()
            }
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
