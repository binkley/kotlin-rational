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
import java.math.BigDecimal
import java.math.BigInteger
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
        fun `should convert to big decimal for repeating decimals`() {
            valueOf(1.big, 3.big).toBigDecimal(2) shouldBe BDouble("0.33")
            valueOf(1.big, 3.big).toBigDecimal(0) shouldBe BDouble("0")
            valueOf(1.big, 2.big).toBigDecimal(2) shouldBe BDouble("0.50")
            valueOf(1.big, 2.big).toBigDecimal(0) shouldBe BDouble("0")
            valueOf(33.big, 2.big).toBigDecimal(2) shouldBe BDouble("16.50")
            valueOf(33.big, 2.big).toBigDecimal(0) shouldBe BDouble("16")
            valueOf(340.big, 11.big).toBigDecimal(2) shouldBe BDouble("30.90")
            valueOf(340.big, 11.big).toBigDecimal(0) shouldBe BDouble("30")
        }

        @Test
        fun `should convert to big decimal for repeating decimals with given rounding`() {
            valueOf(1.big, 3.big).toBigDecimal(2, CEILING) shouldBe BDouble("0.34")
            valueOf(1.big, 3.big).toBigDecimal(0, CEILING) shouldBe BDouble("1")
            valueOf(1.big, 2.big).toBigDecimal(2, CEILING) shouldBe BDouble("0.50")
            valueOf(1.big, 2.big).toBigDecimal(0, CEILING) shouldBe BDouble("1")
            valueOf(33.big, 2.big).toBigDecimal(2, CEILING) shouldBe BDouble("16.50")
            valueOf(33.big, 2.big).toBigDecimal(0, CEILING) shouldBe BDouble("17")
            valueOf(340.big, 11.big).toBigDecimal(2, CEILING) shouldBe BDouble("30.91")
            valueOf(340.big, 11.big).toBigDecimal(0, CEILING) shouldBe BDouble("31")
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
