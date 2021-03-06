@file:Suppress("NonAsciiCharacters")

package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.ZERO
import hm.binkley.math.TestBigRational.Companion.valueOf
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class OperatorsTest {
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
}
