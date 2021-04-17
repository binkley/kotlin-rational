package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.TWO
import hm.binkley.math.TestBigRational.Companion.ZERO
import hm.binkley.math.TestBigRational.Companion.valueOf
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.RoundingMode.UNNECESSARY

internal class MathFunctionsTest {
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

            shouldThrow<ArithmeticException> {
                valueOf(8.big, 25.big).sqrt()
            }
            shouldThrow<ArithmeticException> {
                valueOf(9.big, 26.big).sqrt()
            }
        }

        @Test
        fun `should square root approximately`() {
            valueOf(9.big, 25.big).sqrtApproximated() shouldBe
                valueOf(3.big, 5.big)
            valueOf(8.big, 25.big).sqrtApproximated() shouldBe
                valueOf(
                    282_842_712_474_619.big,
                    500_000_000_000_000.big
                )
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
}
