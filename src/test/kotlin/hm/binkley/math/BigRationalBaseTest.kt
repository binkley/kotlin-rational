@file:Suppress("NonAsciiCharacters", "JUnit5MalformedNestedClass")

package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.TEN
import hm.binkley.math.TestBigRational.Companion.TWO
import hm.binkley.math.TestBigRational.Companion.ZERO
import hm.binkley.math.TestBigRational.Companion.cantorSpiral
import hm.binkley.math.TestBigRational.Companion.valueOf
import hm.binkley.math.floating.toBigRational
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.math.RoundingMode
import java.math.RoundingMode.CEILING

@Suppress("RedundantInnerClassModifier")
internal class BigRationalBaseTest {
    @Test
    fun `should forbid division by zero`() {
        shouldThrow<ArithmeticException> {
            1 over 0
        }
    }

    @Test
    fun `should equate`() {
        val rational = (1 over 2)
        @Suppress("ReplaceCallWithBinaryOperator") // JaCoCo
        rational.equals(rational).shouldBeTrue()
        rational.equals(this).shouldBeFalse()
        (1 over 2) shouldBe (1 over 2)
        0 shouldNotBe ZERO
        (2 over 5) shouldNotBe (2 over 3)
    }

    @Test
    fun `should hash sensibly`() {
        (1 over 2).hashCode() shouldBe (1 over 2).hashCode()
        TEN.hashCode() shouldNotBe (1 over 2).hashCode()
    }

    @Test
    fun `should pretty print`() {
        ZERO.toString() shouldBe "0"
        TEN.toString() shouldBe "10"
        (1 over 2).toString() shouldBe "1⁄2"
        (-1 over 2).toString() shouldBe "-1⁄2"
    }

    @Nested
    inner class Factories {
        @Test
        fun `should use constant 0`() {
            (0 over 1) shouldBeSameInstanceAs ZERO
            (0 over 2) shouldBeSameInstanceAs ZERO
        }

        @Test
        fun `should use constant 1`() {
            (1 over 1) shouldBeSameInstanceAs ONE
            (2 over 2) shouldBeSameInstanceAs ONE
        }

        @Test
        fun `should use constant 2`() {
            (2 over 1) shouldBeSameInstanceAs TWO
            (4 over 2) shouldBeSameInstanceAs TWO
        }

        @Test
        fun `should use constant 10`() {
            (10 over 1) shouldBeSameInstanceAs TEN
            (20 over 2) shouldBeSameInstanceAs TEN
        }

        @Test
        fun `should simplify fractions`() {
            (4 over 2) shouldBe (2 over 1)
            (4 over 8L) shouldBe (1 over 2)
            (4L over 8).denominator shouldBe 2.big
        }

        @Test
        fun `should maintain positive denominator`() {
            (4 over (-4L)).denominator shouldBe 1.big
            ((-4) over 4L).denominator shouldBe 1.big
            ((-4) over (-4)).denominator shouldBe 1.big
        }

        @Test
        fun `should value big decimal`() {
            valueOf(1.0.big) shouldBe ONE
        }

        @Test
        fun `should value double`() {
            valueOf(1.0) shouldBe ONE
        }

        @Test
        fun `should value float`() {
            valueOf(1.0f) shouldBe ONE
        }

        @Test
        fun `should value big integer`() {
            valueOf(1.big) shouldBe ONE
        }

        @Test
        fun `should value long`() {
            valueOf(1L) shouldBe ONE
        }

        @Test
        fun `should value int`() {
            valueOf(1) shouldBe ONE
        }
    }

    @Nested
    inner class Conversions {
        @Test
        fun `should convert to big decimal`() {
            // This adheres to the API for conversion to BigDecimal from double
            ONE.toBigDecimal() shouldBe BFloating("1.0")
        }

        @Test
        fun `should fail to convert to big decimal for repeating decimals`() {
            shouldThrow<ArithmeticException> {
                (1 over 3).toBigDecimal()
            }
        }

        @Test
        fun `should convert to double`() {
            (11 over 10).toDouble() shouldBe 1.1
        }

        @Test
        fun `should convert floating point for extreme finite values`() {
            Double.MAX_VALUE.toBigRational().toDouble() shouldBe
                Double.MAX_VALUE
            Double.MIN_VALUE.toBigRational().toDouble() shouldBe
                Double.MIN_VALUE
            Float.MAX_VALUE.toBigRational().toFloat() shouldBe
                Float.MAX_VALUE
            Float.MIN_VALUE.toBigRational().toFloat() shouldBe
                Float.MIN_VALUE
        }

        @Test
        fun `should convert to float`() {
            (11 over 10).toFloat() shouldBe 1.1f
        }

        @Test
        fun `should convert to big integer`() {
            ONE.toBigInteger() shouldBe BFixed.ONE
            (1 over 2).toBigInteger() shouldBe BFixed.ZERO
            (3 over 2).toBigInteger() shouldBe BFixed.ONE
        }

        @Test
        fun `should convert to long`() {
            (11 over 10).toLong() shouldBe 1L
            (valueOf(Long.MAX_VALUE) + 1).toLong() shouldBe
                Long.MIN_VALUE
        }

        @Test
        fun `should convert to int`() {
            (11 over 10).toInt() shouldBe 1
            (valueOf(Int.MAX_VALUE) + 1).toInt() shouldBe
                Int.MIN_VALUE
        }

        @Test
        fun `should convert to short`() {
            (11 over 10).toShort() shouldBe 1.toShort()
            (valueOf(Short.MAX_VALUE.toInt()) + 1).toShort() shouldBe
                Short.MIN_VALUE
        }

        @Test
        fun `should convert to byte`() {
            (11 over 10).toByte() shouldBe 1.toByte()
            (valueOf(Byte.MAX_VALUE.toInt()) + 1).toByte() shouldBe
                Byte.MIN_VALUE
        }

        @Test
        fun `should not convert to character`() {
            shouldThrow<UnsupportedOperationException> {
                ONE.toChar()
            }
        }
    }

    @Nested
    internal class BigDecimalConversions {
        data class Conversion(
            val numerator: Int,
            val denominator: Int,
            val limitPlaces: Int,
            val roundingMode: RoundingMode?,
            val result: String,
        )

        @Suppress("unused")
        fun testData() = listOf(
            // Non-repeating
            Conversion(1, 2, 0, null, "0"),
            Conversion(1, 2, 1, null, "0.5"),
            Conversion(1, 2, 2, null, "0.50"),
            // Repeating
            Conversion(1, 3, 0, null, "0"),
            Conversion(1, 3, 1, null, "0.3"),
            Conversion(1, 3, 2, null, "0.33"),
            // 2-digit characteristic
            Conversion(33, 2, 0, null, "16"),
            Conversion(33, 2, 1, null, "16.5"),
            Conversion(33, 2, 2, null, "16.50"),
            // 2-digit mantissa
            Conversion(340, 11, 0, null, "30"),
            Conversion(340, 11, 1, null, "30.9"),
            Conversion(340, 11, 2, null, "30.90"),
            // Non-repeating
            Conversion(1, 2, 0, CEILING, "1"),
            Conversion(1, 2, 1, CEILING, "0.5"),
            Conversion(1, 2, 2, CEILING, "0.50"),
            // Repeating
            Conversion(1, 3, 0, CEILING, "1"),
            Conversion(1, 3, 1, CEILING, "0.4"),
            Conversion(1, 3, 2, CEILING, "0.34"),
            // 2-digit characteristic
            Conversion(33, 2, 0, CEILING, "17"),
            Conversion(33, 2, 1, CEILING, "16.5"),
            Conversion(33, 2, 2, CEILING, "16.50"),
            // 2-digit mantissa
            Conversion(340, 11, 0, CEILING, "31"),
            Conversion(340, 11, 1, CEILING, "31.0"),
            Conversion(340, 11, 2, CEILING, "30.91"),
        )

        @ParameterizedTest
        @MethodSource("testData")
        fun `should convert to big decimal`(c: Conversion) {
            val rat = c.numerator over c.denominator
            val actual =
                if (null == c.roundingMode) rat.toBigDecimal(c.limitPlaces)
                else rat.toBigDecimal(c.limitPlaces, c.roundingMode)
            val expected = BFloating(c.result)

            actual shouldBe expected
        }
    }

    @Nested
    inner class Properties {
        @Test
        fun `should absolve`() {
            ZERO.absoluteValue shouldBe ZERO
            (3 over 5).absoluteValue shouldBe (3 over 5)
            (-3 over 5).absoluteValue shouldBe (3 over 5)
        }

        @Test
        fun `should reciprocate`() {
            (3 over 2).reciprocal shouldBe (2 over 3)
        }

        @Test
        fun `should signum`() {
            (3 over 5).signum() shouldBe 1
            (0 over 5).signum() shouldBe 0
            (-3 over 5).signum() shouldBe -1
        }

        @Test
        fun `should sign`() {
            (3 over 5).sign shouldBe ONE
            (0 over 5).sign shouldBe ZERO
            (-3 over 5).sign shouldBe -ONE
        }
    }

    @Nested
    inner class Predicates {
        @Test
        fun `should be zero`() {
            ZERO.isZero().shouldBeTrue()
            ONE.isZero().shouldBeFalse()
        }

        @Test
        fun `should be one`() {
            ONE.isUnit().shouldBeTrue()
            ZERO.isUnit().shouldBeFalse()
        }

        @Test
        fun `should be positive`() {
            ONE.isPositive().shouldBeTrue()
            ZERO.isPositive().shouldBeFalse()
            (-ONE).isPositive().shouldBeFalse()
        }

        @Test
        fun `should be negative`() {
            ONE.isNegative().shouldBeFalse()
            ZERO.isNegative().shouldBeFalse()
            (-ONE).isNegative().shouldBeTrue()
        }

        @Test
        fun `should be integral`() {
            ZERO.isWhole().shouldBeTrue()
            (2 over 1).isWhole().shouldBeTrue()
            (1 over 2).isWhole().shouldBeFalse()
        }

        @Test
        fun `should be p-adic`() {
            ZERO.isPAdic(3).shouldBeTrue()
            (1 over 3).isPAdic(3).shouldBeTrue()
            (2 over 1).isPAdic(3).shouldBeTrue()
            (2 over 5).isPAdic(3).shouldBeFalse()
        }

        @Test
        fun `should be dyadic`() {
            ZERO.isDyadic().shouldBeTrue()
            (1 over 2).isDyadic().shouldBeTrue()
            (2 over 1).isDyadic().shouldBeTrue()
            (2 over 3).isDyadic().shouldBeFalse()
        }

        @Test
        fun `should be evenly denominated`() {
            ZERO.isDenominatorEven().shouldBeFalse()
            (1 over 2).isDenominatorEven().shouldBeTrue()
            (1 over 3).isDenominatorEven().shouldBeFalse()
        }
    }

    @Test
    fun `should find absolute difference`() {
        ONE.diff(ZERO) shouldBe ONE
        ZERO.diff(ONE) shouldBe ONE
    }

    @Test
    fun `should have Cantor spiral`() {
        cantorSpiral() shouldNotBe null
    }
}
