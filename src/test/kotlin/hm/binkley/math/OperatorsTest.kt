@file:Suppress("NonAsciiCharacters")

package hm.binkley.math

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class OperatorsTest {

    @Nested
    inner class Group {
        @Test
        fun `should add big rational`() {
            (
                TestBigRational.valueOf(
                    3.big,
                    5.big
                ) + TestBigRational.valueOf(2.big, 3.big)
                ) shouldBe
                TestBigRational.valueOf(19.big, 15.big)
        }

        @Test
        fun `should add big decimal`() {
            (1.0.big + TestBigRational.ONE) shouldBe TestBigRational.valueOf(
                2.big,
                1.big
            )
            (TestBigRational.ONE + 10.0.big) shouldBe TestBigRational.valueOf(
                11.big,
                1.big
            )
        }

        @Test
        fun `should add double`() {
            (1.0 + TestBigRational.ONE) shouldBe TestBigRational.valueOf(
                2.big,
                1.big
            )
            (TestBigRational.ONE + 1.0) shouldBe TestBigRational.valueOf(
                2.big,
                1.big
            )
        }

        @Test
        fun `should add float`() {
            (1.0f + TestBigRational.ONE) shouldBe TestBigRational.valueOf(
                2.big,
                1.big
            )
            (TestBigRational.ONE + 1.0f) shouldBe TestBigRational.valueOf(
                2.big,
                1.big
            )
        }

        @Test
        fun `should add big integer`() {
            (1.big + TestBigRational.ONE) shouldBe TestBigRational.valueOf(
                2.big,
                1.big
            )
            (TestBigRational.ONE + 1.big) shouldBe TestBigRational.valueOf(
                2.big,
                1.big
            )
        }

        @Test
        fun `should add long`() {
            (1L + TestBigRational.ONE) shouldBe TestBigRational.valueOf(
                2.big,
                1.big
            )
            (TestBigRational.ONE + 1L) shouldBe TestBigRational.valueOf(
                2.big,
                1.big
            )
        }

        @Test
        fun `should add int`() {
            (1 + TestBigRational.ONE) shouldBe TestBigRational.valueOf(
                2.big,
                1.big
            )
            (TestBigRational.ONE + 1) shouldBe TestBigRational.valueOf(
                2.big,
                1.big
            )
        }

        @Test
        fun `should subtract big rational`() {
            (
                TestBigRational.valueOf(
                    3.big,
                    5.big
                ) - TestBigRational.valueOf(2.big, 3.big)
                ) shouldBe
                TestBigRational.valueOf((-1).big, 15.big)
        }

        @Test
        fun `should subtract big decimal`() {
            (1.0.big - TestBigRational.ONE) shouldBe TestBigRational.ZERO
            (TestBigRational.ONE - 1.0.big) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should subtract double`() {
            (1.0 - TestBigRational.ONE) shouldBe TestBigRational.ZERO
            (TestBigRational.ONE - 1.0) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should subtract float`() {
            (1.0f - TestBigRational.ONE) shouldBe TestBigRational.ZERO
            (TestBigRational.ONE - 1.0f) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should subtract big integer`() {
            (1.big - TestBigRational.ONE) shouldBe TestBigRational.ZERO
            (TestBigRational.ONE - 1.big) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should subtract long`() {
            (1L - TestBigRational.ONE) shouldBe TestBigRational.ZERO
            (TestBigRational.ONE - 1L) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should subtract int`() {
            (1 - TestBigRational.ONE) shouldBe TestBigRational.ZERO
            (TestBigRational.ONE - 1) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should do nothing arithmetically`() {
            (
                +TestBigRational.valueOf(
                    2.big,
                    3.big
                )
                ) shouldBe TestBigRational.valueOf(2.big, 3.big)
        }

        @Test
        fun `should invert arithmetically`() {
            (
                -TestBigRational.valueOf(
                    2.big,
                    3.big
                )
                ) shouldBe TestBigRational.valueOf((-2).big, 3.big)
        }

        @Test
        fun `should increment`() {
            var a = TestBigRational.ZERO
            ++a shouldBe TestBigRational.ONE
        }

        @Test
        fun `should decrement`() {
            var a = TestBigRational.ONE
            --a shouldBe TestBigRational.ZERO
        }
    }

    @Nested
    inner class Ring {
        @Test
        fun `should multiply big rational`() {
            (
                TestBigRational.valueOf(
                    3.big,
                    5.big
                ) * TestBigRational.valueOf(2.big, 3.big)
                ) shouldBe
                TestBigRational.valueOf(2.big, 5.big)
        }

        @Test
        fun `should multiply big decimal`() {
            (1.0.big * TestBigRational.ONE) shouldBe TestBigRational.ONE
            (TestBigRational.ONE * 1.0.big) shouldBe TestBigRational.ONE
        }

        @Test
        fun `should multiply double`() {
            (1.0 * TestBigRational.ONE) shouldBe TestBigRational.ONE
            (TestBigRational.ONE * 1.0) shouldBe TestBigRational.ONE
        }

        @Test
        fun `should multiply float`() {
            (1.0f * TestBigRational.ONE) shouldBe TestBigRational.ONE
            (TestBigRational.ONE * 1.0f) shouldBe TestBigRational.ONE
        }

        @Test
        fun `should multiply big integer`() {
            (1.big * TestBigRational.ONE) shouldBe TestBigRational.ONE
            (TestBigRational.ONE * 1.big) shouldBe TestBigRational.ONE
        }

        @Test
        fun `should multiply long`() {
            (1L * TestBigRational.ONE) shouldBe TestBigRational.ONE
            (TestBigRational.ONE * 1L) shouldBe TestBigRational.ONE
        }

        @Test
        fun `should multiply int`() {
            (1 * TestBigRational.ONE) shouldBe TestBigRational.ONE
            (TestBigRational.ONE * 1) shouldBe TestBigRational.ONE
        }

        @Test
        fun `should exponentiate`() {
            TestBigRational.valueOf(
                3.big,
                5.big
            ) `**` 0 shouldBe TestBigRational.ONE
            TestBigRational.valueOf(
                3.big,
                5.big
            ) `**` 2 shouldBe TestBigRational.valueOf(9.big, 25.big)
            TestBigRational.valueOf(
                3.big,
                5.big
            ) `**` -2 shouldBe TestBigRational.valueOf(25.big, 9.big)
        }
    }

    @Nested
    inner class Field {
        @Test
        fun `should divide big rational`() {
            (
                TestBigRational.valueOf(
                    3.big,
                    5.big
                ) / TestBigRational.valueOf(2.big, 3.big)
                ) shouldBe
                TestBigRational.valueOf(9.big, 10.big)
        }

        @Test
        fun `should divide big decimal`() {
            (1.0.big / TestBigRational.ONE) shouldBe TestBigRational.ONE
            (TestBigRational.ONE / 1.0.big) shouldBe TestBigRational.ONE
        }

        @Test
        fun `should divide double`() {
            (1.0 / TestBigRational.ONE) shouldBe TestBigRational.ONE
            (TestBigRational.ONE / 1.0) shouldBe TestBigRational.ONE
        }

        @Test
        fun `should divide float`() {
            (1.0f / TestBigRational.ONE) shouldBe TestBigRational.ONE
            (TestBigRational.ONE / 1.0f) shouldBe TestBigRational.ONE
        }

        @Test
        fun `should divide big integer`() {
            (1.big / TestBigRational.ONE) shouldBe TestBigRational.ONE
            (TestBigRational.ONE / 1.big) shouldBe TestBigRational.ONE
        }

        @Test
        fun `should divide long`() {
            (1L / TestBigRational.ONE) shouldBe TestBigRational.ONE
            (TestBigRational.ONE / 1L) shouldBe TestBigRational.ONE
        }

        @Test
        fun `should divide int`() {
            (1 / TestBigRational.ONE) shouldBe TestBigRational.ONE
            (TestBigRational.ONE / 1) shouldBe TestBigRational.ONE
        }

        @Test
        fun `should modulo big rational`() {
            (
                TestBigRational.valueOf(
                    3.big,
                    5.big
                ) % TestBigRational.valueOf(
                    2.big,
                    3.big
                )
                ) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should modulo big decimal`() {
            (1.0.big % TestBigRational.ONE) shouldBe TestBigRational.ZERO
            (TestBigRational.ONE % 1.0.big) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should modulo double`() {
            (1.0 % TestBigRational.ONE) shouldBe TestBigRational.ZERO
            (TestBigRational.ONE % 1.0) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should modulo float`() {
            (1.0f % TestBigRational.ONE) shouldBe TestBigRational.ZERO
            (TestBigRational.ONE % 1.0f) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should modulo big integer`() {
            (1.big % TestBigRational.ONE) shouldBe TestBigRational.ZERO
            (TestBigRational.ONE % 1.big) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should modulo long`() {
            (1L % TestBigRational.ONE) shouldBe TestBigRational.ZERO
            (TestBigRational.ONE % 1L) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should modulo int`() {
            (1 % TestBigRational.ONE) shouldBe TestBigRational.ZERO
            (TestBigRational.ONE % 1) shouldBe TestBigRational.ZERO
        }

        @Test
        fun `should divide with remainder`() {
            TestBigRational.valueOf(13.big, 2.big).divideAndRemainder(
                TestBigRational.valueOf(3.big, 1.big)
            ) shouldBe
                (
                    TestBigRational.valueOf(
                        2.big,
                        1.big
                    ) to TestBigRational.valueOf(1.big, 2.big)
                    )
            TestBigRational.valueOf((-13).big, 2.big).divideAndRemainder(
                TestBigRational.valueOf((-3).big, 1.big)
            ) shouldBe
                (
                    TestBigRational.valueOf(
                        2.big,
                        1.big
                    ) to TestBigRational.valueOf((-1).big, 2.big)
                    )
            TestBigRational.valueOf((-13).big, 2.big).divideAndRemainder(
                TestBigRational.valueOf(3.big, 1.big)
            ) shouldBe
                (
                    TestBigRational.valueOf(
                        (-2).big,
                        1.big
                    ) to TestBigRational.valueOf((-1).big, 2.big)
                    )
            TestBigRational.valueOf(13.big, 2.big).divideAndRemainder(
                TestBigRational.valueOf((-3).big, 1.big)
            ) shouldBe
                (
                    TestBigRational.valueOf(
                        (-2).big,
                        1.big
                    ) to TestBigRational.valueOf(1.big, 2.big)
                    )
        }

        @Test
        fun `should invert multiplicatively`() {
            TestBigRational.valueOf(2.big, 3.big)
                .unaryDiv() shouldBe TestBigRational.valueOf(3.big, 2.big)
        }
    }

    @Test
    fun `should be ℚ-ish`() {
        val twoThirds = TestBigRational.valueOf(2.big, 3.big)
        val threeHalves = TestBigRational.valueOf(3.big, 2.big)
        val fiveSevenths = TestBigRational.valueOf(5.big, 7.big)

        // Identity elements
        (twoThirds + TestBigRational.ZERO) shouldBe twoThirds
        (twoThirds * TestBigRational.ONE) shouldBe twoThirds

        // Inverses
        (twoThirds + -twoThirds) shouldBe TestBigRational.ZERO
        (twoThirds * twoThirds.unaryDiv()) shouldBe TestBigRational.ONE

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
