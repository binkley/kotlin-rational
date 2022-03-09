@file:Suppress("NonAsciiCharacters")

package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.TEN
import hm.binkley.math.TestBigRational.Companion.TWO
import hm.binkley.math.TestBigRational.Companion.ZERO
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Suppress("RedundantInnerClassModifier")
internal class OperatorsTest {
    @Test
    fun `should destructure`() {
        val (numerator, denominator) = TEN

        numerator shouldBe 10.big
        denominator shouldBe 1.big
    }

    @Nested
    inner class Group {
        @Test
        fun `should add big rational`() {
            ((3 over 5) + (2 over 3)) shouldBe (19 over 15)
        }

        @Test
        fun `should add big decimal`() {
            (1.0.big + ONE) shouldBe (2 over 1)
            (ONE + 10.0.big) shouldBe (11 over 1)
        }

        @Test
        fun `should add double`() {
            (1.0 + ONE) shouldBe (2 over 1)
            (ONE + 1.0) shouldBe (2 over 1)
        }

        @Test
        fun `should add float`() {
            (1.0f + ONE) shouldBe (2 over 1)
            (ONE + 1.0f) shouldBe (2 over 1)
        }

        @Test
        fun `should add big integer`() {
            (1.big + ONE) shouldBe (2 over 1)
            (ONE + 1.big) shouldBe (2 over 1)
        }

        @Test
        fun `should add long`() {
            (1L + ONE) shouldBe (2 over 1)
            (ONE + 1L) shouldBe (2 over 1)
        }

        @Test
        fun `should add int`() {
            (1 + ONE) shouldBe (2 over 1)
            (ONE + 1) shouldBe (2 over 1)
        }

        @Test
        fun `should subtract big rational`() {
            ((3 over 5) - (2 over 3)) shouldBe (-1 over 15)
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
            +(2 over 3) shouldBe (2 over 3)
        }

        @Test
        fun `should invert arithmetically`() {
            -(2 over 3) shouldBe (-2 over 3)
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
            (3 over 5) * (2 over 3) shouldBe (2 over 5)
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
        fun `should raise`() {
            (3 over 5) `^` 0 shouldBe ONE
            (3 over 5) `^` 2 shouldBe (9 over 25)
            (3 over 5) `^` -2 shouldBe (25 over 9)
        }

        @Test
        fun `should define by fiat 0^0`() {
            ZERO `^` 0 shouldBe ONE
        }

        @Test
        fun `should complain on inverting zero`() {
            assertThrows<ArithmeticException> {
                ZERO `^` -1
            }
        }
    }

    @Nested
    inner class Field {
        @Test
        fun `should divide big rational`() {
            (3 over 5) / (2 over 3) shouldBe (9 over 10)
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
            (3 over 5) % (2 over 3) shouldBe ZERO
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
        fun `should not modulo by 0`() {
            shouldThrow<ArithmeticException> {
                ONE % ZERO
            }
            shouldThrow<ArithmeticException> {
                ONE % BFloating.ZERO
            }
            shouldThrow<ArithmeticException> {
                BFloating.ONE % ZERO
            }
            shouldThrow<ArithmeticException> {
                ONE % 0.0
            }
            shouldThrow<ArithmeticException> {
                0.0 % ZERO
            }
            shouldThrow<ArithmeticException> {
                ONE % 0.0f
            }
            shouldThrow<ArithmeticException> {
                1.0f % ZERO
            }
            shouldThrow<ArithmeticException> {
                ONE % BFixed.ZERO
            }
            shouldThrow<ArithmeticException> {
                BFixed.ONE % ZERO
            }
            shouldThrow<ArithmeticException> {
                1L % ZERO
            }
            shouldThrow<ArithmeticException> {
                ONE % 0
            }
            shouldThrow<ArithmeticException> {
                1 % ZERO
            }
        }

        @Test
        fun `should divide with remainder`() {
            (6 over 1).divideAndRemainder(TWO) shouldBe ((3 over 1) to ZERO)
            (13 over 2).divideAndRemainder(3 over 1) shouldBe
                ((2 over 1) to (1 over 2))
            (-13 over 2).divideAndRemainder(-3 over 1) shouldBe
                ((2 over 1) to (-1 over 2))
            (-13 over 2).divideAndRemainder(3 over 1) shouldBe
                ((-2 over 1) to (-1 over 2))
            (13 over 2).divideAndRemainder(-3 over 1) shouldBe
                ((-2 over 1) to (1 over 2))
        }

        @Test
        fun `should be a whole number and a remainder`() {
            (6 over 1).wholeAndRemainder() shouldBe ((6 over 1) to ZERO)
            (5 over 2).wholeAndRemainder() shouldBe (TWO to (1 over 2))
            (-5 over 2).wholeAndRemainder() shouldBe (-TWO to (-1 over 2))
        }

        @Test
        fun `should invert multiplicatively`() {
            (2 over 3).unaryDiv() shouldBe (3 over 2)
        }
    }

    @Test
    fun `should be ℚ-ish`() {
        val twoThirds = (2 over 3)
        val threeHalves = (3 over 2)
        val fiveSevenths = (5 over 7)

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
}
