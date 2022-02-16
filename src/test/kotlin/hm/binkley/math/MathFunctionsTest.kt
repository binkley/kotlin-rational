package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.TWO
import hm.binkley.math.TestBigRational.Companion.ZERO
import hm.binkley.math.fixed.toBigRational
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.RoundingMode.CEILING
import java.math.RoundingMode.FLOOR
import java.math.RoundingMode.HALF_DOWN
import java.math.RoundingMode.HALF_EVEN
import java.math.RoundingMode.HALF_UP
import java.math.RoundingMode.UNNECESSARY

@Suppress("RedundantInnerClassModifier")
internal class MathFunctionsTest {
    @Nested
    inner class Rounding {
        @Test
        fun `should round towards ceiling`() {
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
        fun `should round towards 0`() {
            ZERO.truncate() shouldBe ZERO
            ONE.truncate() shouldBe ONE
            (-ONE).truncate() shouldBe -ONE
            (3 over 2).truncate() shouldBe ONE
            (1 over 2).truncate() shouldBe ZERO
            (-1 over 2).truncate() shouldBe ZERO
            (-3 over 2).truncate() shouldBe -ONE
        }

        @Test
        fun `should round towards an infinity`() {
            ZERO.roundOut() shouldBe ZERO
            ONE.roundOut() shouldBe ONE
            (-ONE).roundOut() shouldBe -ONE
            (3 over 2).roundOut() shouldBe TWO
            (1 over 2).roundOut() shouldBe ONE
            (-1 over 2).roundOut() shouldBe -ONE
            (-3 over 2).roundOut() shouldBe -TWO
        }

        @Test
        fun `should round towards an a goal`() {
            fun TestBigRational.roundToOne() = roundTowards(ONE)

            ZERO.roundToOne() shouldBe ZERO
            ONE.roundToOne() shouldBe ONE
            (-ONE).roundToOne() shouldBe -ONE
            (3 over 2).roundToOne() shouldBe ONE
            (1 over 2).roundToOne() shouldBe ONE
            (-1 over 2).roundToOne() shouldBe ZERO
            (-3 over 2).roundToOne() shouldBe -ONE
        }

        @Test
        fun `should round as half-up mode by default`() {
            (1 over 2).round() shouldBe ZERO
            (3 over 2).round() shouldBe TWO
        }

        @Test
        fun `should round as instructed`() {
            (1 over 2).round(CEILING) shouldBe ONE
            (1 over 2).round(FLOOR) shouldBe ZERO
            (1 over 2).round(HALF_DOWN) shouldBe ZERO
            (1 over 2).round(HALF_UP) shouldBe ONE
            (1 over 2).round(HALF_EVEN) shouldBe ZERO

            ZERO.round(UNNECESSARY) shouldBe ZERO
            shouldThrow<ArithmeticException> {
                (1 over 2).round(UNNECESSARY)
            }
        }

        @Test
        fun `should truncate and fractionate`() {
            listOf(
                ZERO,
                ONE,
                -ONE,
                (3 over 2),
                (1 over 2),
                (-1 over 2),
                (-3 over 2),
            ).forAll {
                val (truncation, fraction) = it.truncateAndFraction()
                (truncation + fraction) shouldBe it
            }
        }
    }

    @Nested
    inner class Roots {
        @Test
        fun `should square root`() {
            (9 over 25).sqrt() shouldBe (3 over 5)

            shouldThrow<ArithmeticException> {
                (8 over 25).sqrt()
            }
            shouldThrow<ArithmeticException> {
                (9 over 26).sqrt()
            }
        }

        @Test
        fun `should square root with remainder`() {
            // Too big
            (11 over 25).sqrtAndRemainder() shouldBe
                    ((3 over 5) to (2 over 25))
            // Too small
            (8 over 25).sqrtAndRemainder() shouldBe
                    ((2 over 5) to (4 over 25))
            // Just right
            (9 over 25).sqrtAndRemainder() shouldBe
                    ((3 over 5) to ZERO)
            // Ginormous
            // TODO: How to find a "near Double.MAX_VALUE" who's sqrt produces a
            //  remainder?
            //  valueOf(Double.MAX_VALUE - ulp(Double.MAX_VALUE)).sqrtAndRemainder() shouldBe
            //  ((2 over 5) to (121 over 650))
            // Teensy weensy
            // TODO: What is math correct here?  I expected the sqrt of
            //  Double.MIN_VALUE to still be min value as sqrt is smaller
            //  valueOf(Double.MIN_VALUE).sqrtAndRemainder() shouldBe
            //  (valueOf(Double.MIN_VALUE) to ZERO)
        }

        @Test
        fun `should square root approximately`() {
            (9 over 25).sqrtApproximated() shouldBe
                    (3 over 5)
            (8 over 25).sqrtApproximated() shouldBe
                    (282_842_712_474_619 over 500_000_000_000_000)
            (9 over 26).sqrtApproximated() shouldBe
                    (5_883_484_054_145_521 over 10_000_000_000_000_000)
        }

        @Test
        fun `should cube root`() {
            val exactNumerator = (3 * 3 * 3).toBigInteger()
            val exactDenominator = (5 * 5 * 5).toBigInteger()

            (exactNumerator over exactDenominator).cbrt() shouldBe (3 over 5)
            (-ONE).cbrt() shouldBe -ONE

            shouldThrow<ArithmeticException> {
                ((exactNumerator + BInt.ONE) over exactDenominator).cbrt()
            }
            shouldThrow<ArithmeticException> {
                (exactNumerator over (exactDenominator + BInt.ONE)).cbrt()
            }
            shouldThrow<ArithmeticException> {
                Double.MAX_VALUE.toBigRational().cbrt()
            }
        }

        @Test
        fun `should cube root approximately`() {
            // Too big
            (28 over 125).cbrtApproximated() shouldBe
                    (3_036_588_971_875_663 over 5_000_000_000_000_000)
            // Too small
            (26 over 125).cbrtApproximated() shouldBe
                    (5_924_992_136_814_741 over 10_000_000_000_000_000)
            // Just right
            (27 over 125).cbrtApproximated() shouldBe (3 over 5)
        }
    }

    @Nested
    inner class Measures {
        @Test
        fun `should find GCD (HCF)`() {
            ZERO.gcd(ZERO) shouldBe ZERO
            (2 over 9).gcd((6 over 21)) shouldBe
                    (2 over 63)
            (-2 over 9).gcd((6 over 21)) shouldBe
                    (2 over 63)
            (2 over 9).gcd((-6 over 21)) shouldBe
                    (2 over 63)
            (-2 over 9).gcd((-6 over 21)) shouldBe
                    (2 over 63)
            ZERO.gcd((2 over 9)) shouldBe (2 over 9)
        }

        @Test
        fun `should find LCM (LCD)`() {
            ZERO.lcm(ZERO) shouldBe ZERO
            (2 over 9).lcm((6 over 21)) shouldBe
                    (2 over 1)
            (-2 over 9).lcm((6 over 21)) shouldBe
                    (2 over 1)
            (2 over 9).lcm((-6 over 21)) shouldBe
                    (2 over 1)
            (-2 over 9).lcm((-6 over 21)) shouldBe
                    (2 over 1)
            ZERO.lcm((6 over 21)) shouldBe ZERO
        }

        @Test
        fun `should find between`() {
            ZERO.mediant(ZERO) shouldBe ZERO
            ONE.mediant(TWO) shouldBe (3 over 2)
        }
    }
}
