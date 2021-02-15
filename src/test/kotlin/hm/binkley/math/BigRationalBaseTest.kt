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
import java.math.RoundingMode.UNNECESSARY

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
            +valueOf(4.big, (-4L).big) shouldBe -ONE
            +valueOf((-4).big, 4L.big) shouldBe -ONE
            +valueOf((-4).big, (-4).big) shouldBe ONE
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
            // This is confusing, but adheres to the API for conversion to
            // BigDecimal from Double
            ONE.toBigDecimal() shouldBe BigDecimal("1.0")
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
    inner class Rounding {
        @Test
        fun `should round towards ceiling`() {
            ZERO.ceil() shouldBe ZERO
            ONE.ceil() shouldBe ONE
            (-ONE).ceil() shouldBe -ONE
            valueOf(3.big, 2.big).ceil() shouldBe TWO
            valueOf(1.big, 2.big).ceil() shouldBe ONE
            valueOf((-1).big, 2.big).ceil() shouldBe ZERO
            valueOf((-3).big, 2.big).ceil() shouldBe -ONE
        }

        @Test
        fun `should round towards floor`() {
            ZERO.floor() shouldBe ZERO
            ONE.floor() shouldBe ONE
            (-ONE).floor() shouldBe -ONE
            valueOf(3.big, 2.big).floor() shouldBe ONE
            valueOf(1.big, 2.big).floor() shouldBe ZERO
            valueOf((-1).big, 2.big).floor() shouldBe -ONE
            valueOf((-3).big, 2.big).floor() shouldBe -TWO
        }

        @Test
        fun `should round towards nearest even whole number`() {
            ZERO.round() shouldBe ZERO
            ONE.round() shouldBe ONE
            (-ONE).round() shouldBe -ONE
            valueOf(3.big, 2.big).round() shouldBe TWO
            valueOf(1.big, 2.big).round() shouldBe ZERO
            valueOf((-1).big, 2.big).round() shouldBe ZERO
            valueOf((-3).big, 2.big).round() shouldBe -TWO
        }

        @Test
        fun `should round towards 0`() {
            ZERO.truncate() shouldBe ZERO
            ONE.truncate() shouldBe ONE
            (-ONE).truncate() shouldBe -ONE
            valueOf(3.big, 2.big).truncate() shouldBe ONE
            valueOf(1.big, 2.big).truncate() shouldBe ZERO
            valueOf((-1).big, 2.big).truncate() shouldBe ZERO
            valueOf((-3).big, 2.big).truncate() shouldBe -ONE
        }

        @Test
        fun `should round as instructed`() {
            ZERO.round(UNNECESSARY) shouldBe ZERO

            shouldThrow<ArithmeticException> {
                valueOf(1.big, 2.big).round(UNNECESSARY) shouldBe ZERO
            }
        }

        @Test
        fun `should truncate and fractionate`() {
            listOf(
                ZERO,
                ONE,
                -ONE,
                valueOf(3.big, 2.big),
                valueOf(1.big, 2.big),
                valueOf((-1).big, 2.big),
                valueOf((-3).big, 2.big),
            ).forEach {
                val (truncation, fraction) = it.truncateAndFraction()
                (truncation + fraction) shouldBe it
            }
        }

        @Test
        fun `should fractionate`() {
            ZERO.fraction() shouldBe ZERO
            ONE.fraction() shouldBe ZERO
            (-ONE).fraction() shouldBe ZERO
            valueOf(3.big, 2.big).fraction() shouldBe valueOf(1.big, 2.big)
            valueOf(1.big, 2.big).fraction() shouldBe valueOf(1.big, 2.big)
            valueOf((-1).big, 2.big).fraction() shouldBe
                valueOf((-1).big, 2.big)
            valueOf((-3).big, 2.big).fraction() shouldBe
                valueOf((-1).big, 2.big)
        }
    }

    @Nested
    inner class Roots {
        @Test
        fun `should square root`() {
            valueOf(9.big, 25.big).sqrt() shouldBe valueOf(3.big, 5.big)

            shouldThrow<ArithmeticException> { valueOf(8.big, 25.big).sqrt() }
            shouldThrow<ArithmeticException> { valueOf(9.big, 26.big).sqrt() }
        }

        @Test
        fun `should square root approximately`() {
            valueOf(9.big, 25.big).sqrtApproximated() shouldBe valueOf(
                3.big,
                5.big
            )
            valueOf(8.big, 25.big).sqrtApproximated() shouldBe
                valueOf(282_842_712_474_619.big, 500_000_000_000_000.big)
            valueOf(9.big, 26.big).sqrtApproximated() shouldBe
                valueOf(
                    5_883_484_054_145_521.big,
                    10_000_000_000_000_000.big
                )
        }
    }

    @Nested
    inner class Measures {
        @Test
        fun `should find GCD (HCF)`() {
            valueOf(2.big, 9.big).gcd(valueOf(6.big, 21.big)) shouldBe
                valueOf(2.big, 63.big)
            valueOf((-2).big, 9.big).gcd(valueOf(6.big, 21.big)) shouldBe
                valueOf(2.big, 63.big)
            valueOf(2.big, 9.big).gcd(valueOf((-6).big, 21.big)) shouldBe
                valueOf(2.big, 63.big)
            valueOf((-2).big, 9.big).gcd(valueOf((-6).big, 21.big)) shouldBe
                valueOf(2.big, 63.big)
            ZERO.gcd(valueOf(2.big, 9.big)) shouldBe valueOf(2.big, 9.big)
            ZERO.gcd(ZERO) shouldBe ZERO
        }

        @Test
        fun `should find LCM (LCD)`() {
            valueOf(2.big, 9.big).lcm(valueOf(6.big, 21.big)) shouldBe
                valueOf(2.big, 1.big)
            valueOf((-2).big, 9.big).lcm(valueOf(6.big, 21.big)) shouldBe
                valueOf(2.big, 1.big)
            valueOf(2.big, 9.big).lcm(valueOf((-6).big, 21.big)) shouldBe
                valueOf(2.big, 1.big)
            valueOf((-2).big, 9.big).lcm(valueOf((-6).big, 21.big)) shouldBe
                valueOf(2.big, 1.big)
            ZERO.lcm(valueOf(6.big, 21.big)) shouldBe ZERO
            ZERO.lcm(ZERO) shouldBe ZERO
        }

        @Test
        fun `should find between`() {
            ZERO.mediant(ZERO) shouldBe ZERO
            ONE.mediant(TWO) shouldBe valueOf(3.big, 2.big)
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
