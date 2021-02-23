@file:Suppress("NonAsciiCharacters")

package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.TEN
import hm.binkley.math.TestBigRational.Companion.TWO
import hm.binkley.math.TestBigRational.Companion.ZERO
import hm.binkley.math.TestBigRational.Companion.valueOf
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
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.math.RoundingMode.CEILING

internal class BigRationalBaseTest {
    @Test
    fun `should be itself`() {
        valueOf(1.big, 2.big) shouldBe valueOf(1.big, 2.big)
        0 shouldNotBe ZERO
        valueOf(2.big, 5.big) shouldNotBe valueOf(2.big, 3.big)
    }

    @Test
    fun `should hash separately`() {
        TEN.hashCode() shouldNotBe valueOf(1.big, 2.big).hashCode()
    }

    @Test
    fun `should pretty print`() {
        ZERO.toString() shouldBe "0"
        TEN.toString() shouldBe "10"
        valueOf(1.big, 2.big).toString() shouldBe "1⁄2"
        valueOf((-1).big, 2.big).toString() shouldBe "-1⁄2"
    }

    @Nested
    inner class Factories {
        @Test
        fun `should use constant 0`() {
            valueOf(0.big, 1.big) shouldBeSameInstanceAs ZERO
            valueOf(0.big, 2.big) shouldBeSameInstanceAs ZERO
        }

        @Test
        fun `should use constant 1`() {
            valueOf(1.big, 1.big) shouldBeSameInstanceAs ONE
            valueOf(2.big, 2.big) shouldBeSameInstanceAs ONE
        }

        @Test
        fun `should use constant 2`() {
            valueOf(2.big, 1.big) shouldBeSameInstanceAs TWO
            valueOf(4.big, 2.big) shouldBeSameInstanceAs TWO
        }

        @Test
        fun `should use constant 10`() {
            valueOf(10.big, 1.big) shouldBeSameInstanceAs TEN
            valueOf(20.big, 2.big) shouldBeSameInstanceAs TEN
        }

        @Test
        fun `should simplify fractions`() {
            valueOf(4.big, 2.big) shouldBe valueOf(2.big, 1.big)
            valueOf(4.big, 8L.big) shouldBe valueOf(1.big, 2.big)
            valueOf(4L.big, 8.big).denominator shouldBe 2.big
        }

        @Test
        fun `should maintain positive denominator`() {
            valueOf(4.big, (-4L).big).denominator shouldBe 1.big
            valueOf((-4).big, 4L.big).denominator shouldBe 1.big
            valueOf((-4).big, (-4).big).denominator shouldBe 1.big
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
            ONE.toBigDecimal() shouldBe BigDecimal("1.0")
        }

        @Test
        fun `should fail to convert to big decimal for repeating decimals`() {
            shouldThrow<ArithmeticException> {
                valueOf(1.big, 3.big).toBigDecimal()
            }
        }

        @Test
        fun `should convert to double`() {
            valueOf(11.big, 10.big).toDouble() shouldBe 1.1
        }

        @Test
        fun `should convert to float`() {
            valueOf(11.big, 10.big).toFloat() shouldBe 1.1f
        }

        @Test
        fun `should convert to big integer`() {
            ONE.toBigInteger() shouldBe BigInteger.ONE
        }

        @Test
        fun `should convert to long`() {
            valueOf(11.big, 10.big).toLong() shouldBe 1L
            (valueOf(Long.MAX_VALUE) + 1).toLong() shouldBe
                Long.MIN_VALUE
        }

        @Test
        fun `should convert to int`() {
            valueOf(11.big, 10.big).toInt() shouldBe 1
            (valueOf(Int.MAX_VALUE) + 1).toInt() shouldBe
                Int.MIN_VALUE
        }

        @Test
        fun `should convert to short`() {
            valueOf(11.big, 10.big).toShort() shouldBe 1.toShort()
            (valueOf(Short.MAX_VALUE.toInt()) + 1).toShort() shouldBe
                Short.MIN_VALUE
        }

        @Test
        fun `should convert to byte`() {
            valueOf(11.big, 10.big).toByte() shouldBe 1.toByte()
            (valueOf(Byte.MAX_VALUE.toInt()) + 1).toByte() shouldBe
                Byte.MIN_VALUE
        }

        @Test
        fun `should not be a character`() {
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
            val rat = valueOf(c.numerator.big, c.denominator.big)
            val actual =
                if (null == c.roundingMode) rat.toBigDecimal(c.limitPlaces)
                else rat.toBigDecimal(c.limitPlaces, c.roundingMode)
            val expected = c.result.big

            actual shouldBe expected
        }
    }

    @Nested
    inner class Properties {
        @Test
        fun `should absolve`() {
            ZERO.absoluteValue shouldBe ZERO
            valueOf(3.big, 5.big).absoluteValue shouldBe valueOf(3.big, 5.big)
            valueOf((-3).big, 5.big).absoluteValue shouldBe
                valueOf(3.big, 5.big)
        }

        @Test
        fun `should reciprocate`() {
            valueOf(3.big, 2.big).reciprocal shouldBe valueOf(2.big, 3.big)
        }

        @Test
        fun `should sign`() {
            valueOf(3.big, 5.big).sign shouldBe ONE
            valueOf(0.big, 5.big).sign shouldBe ZERO
            valueOf((-3).big, 5.big).sign shouldBe -ONE
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
            ONE.isOne().shouldBeTrue()
            ZERO.isOne().shouldBeFalse()
        }

        @Test
        fun `should be integral`() {
            ZERO.isInteger().shouldBeTrue()
            valueOf(2.big, 1.big).isInteger().shouldBeTrue()
            valueOf(1.big, 2.big).isInteger().shouldBeFalse()
        }

        @Test
        fun `should be dyadic`() {
            ZERO.isDyadic().shouldBeTrue()
            valueOf(1.big, 2.big).isDyadic().shouldBeTrue()
            valueOf(2.big, 1.big).isDyadic().shouldBeTrue()
            valueOf(2.big, 3.big).isDyadic().shouldBeFalse()
        }

        @Test
        fun `should be p-adic`() {
            ZERO.isPAdic(3).shouldBeTrue()
            valueOf(1.big, 3.big).isPAdic(3).shouldBeTrue()
            valueOf(2.big, 1.big).isPAdic(3).shouldBeTrue()
            valueOf(2.big, 5.big).isPAdic(3).shouldBeFalse()
        }

        @Test
        fun `should be evenly denominated`() {
            ZERO.isDenominatorEven().shouldBeFalse()
            valueOf(1.big, 2.big).isDenominatorEven().shouldBeTrue()
            valueOf(1.big, 3.big).isDenominatorEven().shouldBeFalse()
        }
    }
}
