@file:Suppress("NonAsciiCharacters")

package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.TEN
import hm.binkley.math.TestBigRational.Companion.TWO
import hm.binkley.math.TestBigRational.Companion.ZERO
import hm.binkley.math.TestBigRational.Companion.valueOf
import io.kotest.assertions.throwables.shouldThrow
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
        }

        @Test
        fun `should use constant 1`() {
            valueOf(1.big, 1.big) shouldBeSameInstanceAs ONE
        }

        @Test
        fun `should use constant 2`() {
            valueOf(2.big, 1.big) shouldBeSameInstanceAs TWO
        }

        @Test
        fun `should use constant 10`() {
            valueOf(10.big, 1.big) shouldBeSameInstanceAs TEN
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
    inner class Ordering {
        @Test
        fun `should compare to big rational`() {
            (ONE > ZERO) shouldBe true
            (ZERO < ONE) shouldBe true
        }

        @Test
        fun `should compare to big decimal`() {
            (1.0.big > ZERO) shouldBe true
            (ONE > 0.0.big) shouldBe true
        }

        @Test
        fun `should compare to double`() {
            (1.0 > ZERO) shouldBe true
            (ONE > 0.0) shouldBe true
        }

        @Test
        fun `should compare to float`() {
            (1.0f > ZERO) shouldBe true
            (ONE > 0.0f) shouldBe true
        }

        @Test
        fun `should compare to big integer`() {
            (1.big > ZERO) shouldBe true
            (ONE > 0.big) shouldBe true
        }

        @Test
        fun `should compare to long`() {
            (1L > ZERO) shouldBe true
            (ONE > 0L) shouldBe true
        }

        @Test
        fun `should compare to int`() {
            (1 > ZERO) shouldBe true
            (ONE > 0) shouldBe true
        }

        @Test
        fun `should sort`() {
            val sorted = listOf(
                ZERO,
                ONE,
                ZERO
            ).sorted()

            sorted shouldBe listOf(ZERO, ZERO, ONE)
        }
    }

    @Nested
    inner class Group {
        @Test
        fun `should add big rational`() {
            (valueOf(3.big, 5.big) + valueOf(2.big, 3.big)) shouldBe
                valueOf(19.big, 15.big)
        }

        @Test
        fun `should add big decimal`() {
            (1.0.big + ONE) shouldBe valueOf(2.big, 1.big)
            (ONE + 10.0.big) shouldBe valueOf(11.big, 1.big)
        }

        @Test
        fun `should add double`() {
            (1.0 + ONE) shouldBe valueOf(2.big, 1.big)
            (ONE + 1.0) shouldBe valueOf(2.big, 1.big)
        }

        @Test
        fun `should add float`() {
            (1.0f + ONE) shouldBe valueOf(2.big, 1.big)
            (ONE + 1.0f) shouldBe valueOf(2.big, 1.big)
        }

        @Test
        fun `should add big integer`() {
            (1.big + ONE) shouldBe valueOf(2.big, 1.big)
            (ONE + 1.big) shouldBe valueOf(2.big, 1.big)
        }

        @Test
        fun `should add long`() {
            (1L + ONE) shouldBe valueOf(2.big, 1.big)
            (ONE + 1L) shouldBe valueOf(2.big, 1.big)
        }

        @Test
        fun `should add int`() {
            (1 + ONE) shouldBe valueOf(2.big, 1.big)
            (ONE + 1) shouldBe valueOf(2.big, 1.big)
        }

        @Test
        fun `should subtract big rational`() {
            (valueOf(3.big, 5.big) - valueOf(2.big, 3.big)) shouldBe
                valueOf((-1).big, 15.big)
        }

        @Test
        fun `should subtract big decimal`() {
            (1.0.big - ONE) shouldBe ZERO
            (ONE - 1.0.big) shouldBe ZERO
        }

        @Test
        fun `should subtract double`() {
            (1.0 - ONE) shouldBe ZERO
            (ONE - 1.0) shouldBe ZERO
        }

        @Test
        fun `should subtract float`() {
            (1.0f - ONE) shouldBe ZERO
            (ONE - 1.0f) shouldBe ZERO
        }

        @Test
        fun `should subtract big integer`() {
            (1.big - ONE) shouldBe ZERO
            (ONE - 1.big) shouldBe ZERO
        }

        @Test
        fun `should subtract long`() {
            (1L - ONE) shouldBe ZERO
            (ONE - 1L) shouldBe ZERO
        }

        @Test
        fun `should subtract int`() {
            (1 - ONE) shouldBe ZERO
            (ONE - 1) shouldBe ZERO
        }

        @Test
        fun `should do nothing arithmetically`() {
            (+valueOf(2.big, 3.big)) shouldBe valueOf(2.big, 3.big)
        }

        @Test
        fun `should invert arithmetically`() {
            (-valueOf(2.big, 3.big)) shouldBe valueOf((-2).big, 3.big)
        }

        @Test
        fun `should increment`() {
            var a = ZERO
            ++a shouldBe ONE
        }

        @Test
        fun `should decrement`() {
            var a = ONE
            --a shouldBe ZERO
        }
    }

    @Nested
    inner class Ring {
        @Test
        fun `should multiply big rational`() {
            (valueOf(3.big, 5.big) * valueOf(2.big, 3.big)) shouldBe
                valueOf(2.big, 5.big)
        }

        @Test
        fun `should multiply big decimal`() {
            (1.0.big * ONE) shouldBe ONE
            (ONE * 1.0.big) shouldBe ONE
        }

        @Test
        fun `should multiply double`() {
            (1.0 * ONE) shouldBe ONE
            (ONE * 1.0) shouldBe ONE
        }

        @Test
        fun `should multiply float`() {
            (1.0f * ONE) shouldBe ONE
            (ONE * 1.0f) shouldBe ONE
        }

        @Test
        fun `should multiply big integer`() {
            (1.big * ONE) shouldBe ONE
            (ONE * 1.big) shouldBe ONE
        }

        @Test
        fun `should multiply long`() {
            (1L * ONE) shouldBe ONE
            (ONE * 1L) shouldBe ONE
        }

        @Test
        fun `should multiply int`() {
            (1 * ONE) shouldBe ONE
            (ONE * 1) shouldBe ONE
        }

        @Test
        fun `should exponentiate`() {
            valueOf(3.big, 5.big) `**` 0 shouldBe ONE
            valueOf(3.big, 5.big) `**` 2 shouldBe valueOf(9.big, 25.big)
            valueOf(3.big, 5.big) `**` -2 shouldBe valueOf(25.big, 9.big)
        }
    }

    @Nested
    inner class Field {
        @Test
        fun `should divide big rational`() {
            (valueOf(3.big, 5.big) / valueOf(2.big, 3.big)) shouldBe
                valueOf(9.big, 10.big)
        }

        @Test
        fun `should divide big decimal`() {
            (1.0.big / ONE) shouldBe ONE
            (ONE / 1.0.big) shouldBe ONE
        }

        @Test
        fun `should divide double`() {
            (1.0 / ONE) shouldBe ONE
            (ONE / 1.0) shouldBe ONE
        }

        @Test
        fun `should divide float`() {
            (1.0f / ONE) shouldBe ONE
            (ONE / 1.0f) shouldBe ONE
        }

        @Test
        fun `should divide big integer`() {
            (1.big / ONE) shouldBe ONE
            (ONE / 1.big) shouldBe ONE
        }

        @Test
        fun `should divide long`() {
            (1L / ONE) shouldBe ONE
            (ONE / 1L) shouldBe ONE
        }

        @Test
        fun `should divide int`() {
            (1 / ONE) shouldBe ONE
            (ONE / 1) shouldBe ONE
        }

        @Test
        fun `should modulo big rational`() {
            (valueOf(3.big, 5.big) % valueOf(2.big, 3.big)) shouldBe ZERO
        }

        @Test
        fun `should modulo big decimal`() {
            (1.0.big % ONE) shouldBe ZERO
            (ONE % 1.0.big) shouldBe ZERO
        }

        @Test
        fun `should modulo double`() {
            (1.0 % ONE) shouldBe ZERO
            (ONE % 1.0) shouldBe ZERO
        }

        @Test
        fun `should modulo float`() {
            (1.0f % ONE) shouldBe ZERO
            (ONE % 1.0f) shouldBe ZERO
        }

        @Test
        fun `should modulo big integer`() {
            (1.big % ONE) shouldBe ZERO
            (ONE % 1.big) shouldBe ZERO
        }

        @Test
        fun `should modulo long`() {
            (1L % ONE) shouldBe ZERO
            (ONE % 1L) shouldBe ZERO
        }

        @Test
        fun `should modulo int`() {
            (1 % ONE) shouldBe ZERO
            (ONE % 1) shouldBe ZERO
        }

        @Test
        fun `should divide with remainder`() {
            valueOf(13.big, 2.big).divideAndRemainder(
                valueOf(3.big, 1.big)
            ) shouldBe
                (valueOf(2.big, 1.big) to valueOf(1.big, 2.big))
            valueOf((-13).big, 2.big).divideAndRemainder(
                valueOf((-3).big, 1.big)
            ) shouldBe
                (valueOf(2.big, 1.big) to valueOf((-1).big, 2.big))
            valueOf((-13).big, 2.big).divideAndRemainder(
                valueOf(3.big, 1.big)
            ) shouldBe
                (valueOf((-2).big, 1.big) to valueOf((-1).big, 2.big))
            valueOf(13.big, 2.big).divideAndRemainder(
                valueOf((-3).big, 1.big)
            ) shouldBe
                (valueOf((-2).big, 1.big) to valueOf(1.big, 2.big))
        }

        @Test
        fun `should invert multiplicatively`() {
            valueOf(2.big, 3.big).unaryDiv() shouldBe valueOf(3.big, 2.big)
        }
    }

    @Test
    fun `should be ℚ-ish`() {
        val twoThirds = valueOf(2.big, 3.big)
        val threeHalves = valueOf(3.big, 2.big)
        val fiveSevenths = valueOf(5.big, 7.big)

        // Identity elements
        (twoThirds + ZERO) shouldBe twoThirds
        (twoThirds * ONE) shouldBe twoThirds

        // Inverses
        (twoThirds + -twoThirds) shouldBe ZERO
        (twoThirds * twoThirds.unaryDiv()) shouldBe ONE

        // Associativity
        ((twoThirds + threeHalves) + fiveSevenths) shouldBe
            (twoThirds + (threeHalves + fiveSevenths))
        ((twoThirds * threeHalves) * fiveSevenths) shouldBe
            (twoThirds * (threeHalves * fiveSevenths))

        // Commutativity
        (twoThirds + threeHalves) shouldBe threeHalves + twoThirds
        (twoThirds * threeHalves) shouldBe threeHalves * twoThirds

        // Distributivity
        ((twoThirds + threeHalves) * fiveSevenths) shouldBe
            (twoThirds * fiveSevenths + threeHalves * fiveSevenths)
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
            ZERO.isZero() shouldBe true
        }

        @Test
        fun `should be one`() {
            ONE.isOne() shouldBe true
        }

        @Test
        fun `should be integral`() {
            ZERO.isInteger() shouldBe true
            valueOf(1.big, 2.big).isInteger() shouldBe false
            valueOf(2.big, 1.big).isInteger() shouldBe true
        }

        @Test
        fun `should be dyadic`() {
            ZERO.isDyadic() shouldBe true
            valueOf(1.big, 2.big).isDyadic() shouldBe true
            valueOf(2.big, 1.big).isDyadic() shouldBe true
            valueOf(2.big, 3.big).isDyadic() shouldBe false
        }

        @Test
        fun `should be p-adic`() {
            ZERO.isPAdic(3) shouldBe true
            valueOf(1.big, 3.big).isPAdic(3) shouldBe true
            valueOf(2.big, 1.big).isPAdic(3) shouldBe true
            valueOf(2.big, 5.big).isPAdic(3) shouldBe false
        }

        @Test
        fun `should be evenly denominated`() {
            ZERO.isDenominatorEven() shouldBe false
            valueOf(1.big, 2.big).isDenominatorEven() shouldBe true
            valueOf(1.big, 3.big).isDenominatorEven() shouldBe false
        }
    }
}

private class TestBigRational(
    numerator: BInt,
    denominator: BInt,
) : BigRationalBase<TestBigRational>(numerator, denominator, Companion) {
    companion object : BigRationalCompanion<TestBigRational> {
        override val ZERO: TestBigRational =
            TestBigRational(BInt.ZERO, BInt.ONE)
        override val ONE: TestBigRational = TestBigRational(BInt.ONE, BInt.ONE)
        override val TWO: TestBigRational = TestBigRational(BInt.TWO, BInt.ONE)
        override val TEN: TestBigRational = TestBigRational(BInt.TEN, BInt.ONE)

        override fun valueOf(
            numerator: BInt,
            denominator: BInt,
        ): TestBigRational = construct(numerator, denominator) { n, d ->
            TestBigRational(n, d)
        }

        override fun iteratorCheck(
            first: TestBigRational,
            last: TestBigRational,
            step: TestBigRational,
        ) {
            if (step.isZero()) error("Step must be non-zero.")
        }
    }
}
