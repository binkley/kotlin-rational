package hm.binkley.math.fixed.complex

import hm.binkley.math.BDouble
import hm.binkley.math.big
import hm.binkley.math.fixed.complex.FixedBigComplex.Companion.ONE
import hm.binkley.math.fixed.complex.FixedBigComplex.Companion.ZERO
import hm.binkley.math.fixed.complex.FixedBigImaginary.Companion.I
import hm.binkley.math.fixed.FixedBigRational
import hm.binkley.math.fixed.FixedBigRational.Companion.TWO
import hm.binkley.math.fixed.over
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private val ONE_PLUS_I = FixedBigRational.ONE + I
private val ONE_MINUS_I = FixedBigRational.ONE - I
private val NEG_ONE_PLUS_I = -FixedBigRational.ONE + I
private val NEG_ONE_MINUS_I = -FixedBigRational.ONE - I

internal class FixedBigComplexTest {
    @Test
    fun `should have constants`() {
        ZERO shouldBe 0 + 0.i
        ONE shouldBe 1 + 0.i
    }

    @Test
    fun `should destructure`() {
        val (real, imag) = ONE_PLUS_I
        real shouldBe ONE_PLUS_I.real
        imag shouldBe ONE_PLUS_I.imag
    }

    @Test
    fun `should pretty print`() {
        "$ONE_PLUS_I" shouldBe "1+1i"
        "$ONE_MINUS_I" shouldBe "1-1i"
    }

    @Test
    fun `should conjugate`() {
        ONE_PLUS_I.conjugate shouldBe ONE_MINUS_I
    }

    @Test
    fun `should determinate`() {
        (2 + 2.i).det shouldBe (8 over 1)
    }

    @Test
    fun `should absolve`() {
        (3 + 4.i).absoluteValue shouldBe (5 over 1)
    }

    @Test
    fun `should absolve approximately`() {
        (8 + 25.i).modulusApproximated() shouldBe
                (BDouble("410137648387709") over
                        BDouble("15625000000000"))

        val jvmValue = BDouble("410137648387709")
            .divideAndRemainder(BDouble("15625000000000"))
        jvmValue shouldBe listOf(
            BDouble.valueOf(26),
            BDouble("3887648387709")
        )
    }

    @Test
    fun `should reciprocate`() {
        val z = 2 + 2.i
        z.unaryDiv() shouldBe ((1 over 4) - (1 over 4).i)
        z.reciprocal shouldBe z.unaryDiv()
    }

    @Test
    fun `should posite`() {
        +ONE_PLUS_I shouldBe ONE_PLUS_I
    }

    @Test
    fun `should negate`() {
        -ONE_PLUS_I shouldBe NEG_ONE_MINUS_I
    }

    @Test
    fun `should add`() {
        (FixedBigRational.ONE + I) shouldBe ONE_PLUS_I
        (I + FixedBigRational.ONE) shouldBe ONE_PLUS_I
        (1.0.big + 1.i) shouldBe ONE_PLUS_I
        (1.i + 1.0.big) shouldBe ONE_PLUS_I
        (I + 1.big) shouldBe ONE_PLUS_I
        (1L + I) shouldBe ONE_PLUS_I
        (I + 1L) shouldBe ONE_PLUS_I
        (1 + I) shouldBe ONE_PLUS_I
        (I + 1) shouldBe ONE_PLUS_I
        (ONE_PLUS_I + ONE_PLUS_I) shouldBe 2 + 2.i
        (ONE_PLUS_I + FixedBigRational.ONE) shouldBe 2 + 1.i
        (FixedBigRational.ONE + ONE_PLUS_I) shouldBe 2 + 1.i
        (ONE_PLUS_I + 1.big) shouldBe 2 + 1.i
        (1.big + ONE_PLUS_I) shouldBe 2 + 1.i
        (ONE_PLUS_I + 1L) shouldBe 2 + 1.i
        (1L + ONE_PLUS_I) shouldBe 2 + 1.i
        (ONE_PLUS_I + 1) shouldBe 2 + 1.i
        (1 + ONE_PLUS_I) shouldBe 2 + 1.i
        (ONE_PLUS_I + I) shouldBe 1 + 2.i
        (I + ONE_PLUS_I) shouldBe 1 + 2.i
    }

    @Test
    fun `should subtract`() {
        (I - FixedBigRational.ONE) shouldBe NEG_ONE_PLUS_I
        (1.big - I) shouldBe ONE_MINUS_I
        (I - 1.big) shouldBe NEG_ONE_PLUS_I
        (1L - I) shouldBe ONE_MINUS_I
        (I - 1L) shouldBe NEG_ONE_PLUS_I
        (1 - I) shouldBe ONE_MINUS_I
        (I - 1) shouldBe NEG_ONE_PLUS_I
        (ONE_PLUS_I - ONE_PLUS_I) shouldBe 0 + 0.i
        (ONE_PLUS_I - FixedBigRational.ONE) shouldBe 0 + I
        (FixedBigRational.ONE - ONE_MINUS_I) shouldBe 0 + I
        (ONE_PLUS_I - 1.big) shouldBe 0 + I
        (1.big - ONE_MINUS_I) shouldBe 0 + I
        (ONE_PLUS_I - 1L) shouldBe 0 + I
        (1L - ONE_MINUS_I) shouldBe 0 + I
        (ONE_PLUS_I - 1) shouldBe 0 + I
        (1 - ONE_MINUS_I) shouldBe 0 + I
        (ONE_PLUS_I - I) shouldBe 1 + 0.i
        (I - ONE_PLUS_I) shouldBe -1 + 0.i
    }

    @Test
    fun `should multiply`() {
        (ONE_PLUS_I * ONE_PLUS_I) shouldBe 0 + 2.i
        (TWO * ONE_PLUS_I) shouldBe 2 + 2.i
        (ONE_PLUS_I * TWO) shouldBe 2 + 2.i
        (2.big * ONE_PLUS_I) shouldBe 2 + 2.i
        (ONE_PLUS_I * 2.big) shouldBe 2 + 2.i
        (2L * ONE_PLUS_I) shouldBe 2 + 2.i
        (ONE_PLUS_I * 2L) shouldBe 2 + 2.i
        (2 * ONE_PLUS_I) shouldBe 2 + 2.i
        (ONE_PLUS_I * 2) shouldBe 2 + 2.i
        (ONE_PLUS_I * 2.i) shouldBe -2 + 2.i
        (2.i * ONE_PLUS_I) shouldBe -2 + 2.i
    }

    @Test
    fun `should divide`() {
        val half = 1 over 2
        (ONE_PLUS_I / ONE_PLUS_I) shouldBe 1 + 0.i
        (TWO / ONE_PLUS_I) shouldBe half + half.i
        (ONE_PLUS_I / TWO) shouldBe half + half.i
        (2.big / ONE_PLUS_I) shouldBe half + half.i
        (ONE_PLUS_I / 2.big) shouldBe half + half.i
        (2L / ONE_PLUS_I) shouldBe half + half.i
        (ONE_PLUS_I / 2L) shouldBe half + half.i
        (2 / ONE_PLUS_I) shouldBe half + half.i
        (ONE_PLUS_I / 2) shouldBe half + half.i
        (ONE_PLUS_I / 2.i) shouldBe half - half.i
        (2.i / ONE_PLUS_I) shouldBe half - half.i
    }

    @Test
    fun `should convert`() {
        val one = ONE + 0.i
        one.toBigRational() shouldBe FixedBigRational.ONE
        one.toBigInteger() shouldBe 1.big
        one.toLong() shouldBe 1L
        one.toInt() shouldBe 1
        (0 + 1.i).toImaginary() shouldBe 1.i
        // Sad paths
        shouldThrow<ArithmeticException> { ONE_PLUS_I.toBigRational() }
        shouldThrow<ArithmeticException> { ONE_PLUS_I.toBigInteger() }
        shouldThrow<ArithmeticException> { ONE_PLUS_I.toLong() }
        shouldThrow<ArithmeticException> { ONE_PLUS_I.toInt() }
        shouldThrow<ArithmeticException> { ONE_PLUS_I.toImaginary() }
    }

    @Test
    fun `should raise`() {
        val half = 1 over 2
        ONE_PLUS_I `**` 0 shouldBe 1 + 0.i
        ONE_PLUS_I `**` 1 shouldBe ONE_PLUS_I
        ONE_PLUS_I `**` -1 shouldBe half - half.i
        ONE_PLUS_I `**` 2 shouldBe 0 + 2.i
        ONE_PLUS_I `**` -2 shouldBe 0 - half.i
    }

    @Test
    fun `should define by fiat 0^0`() {
        ZERO `**` 0 shouldBe ONE
    }

    @Test
    fun `should complain on inverting zero`() {
        assertThrows<ArithmeticException> {
            ZERO `**` -1
        }
    }

    @Test
    fun `should square root approximately`() {
        val root = 1 + 2.i

        (root * root).sqrtApproximated() shouldBe root
    }
}
