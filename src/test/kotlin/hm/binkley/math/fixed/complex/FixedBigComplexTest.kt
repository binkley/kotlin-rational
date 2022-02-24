package hm.binkley.math.fixed.complex

import hm.binkley.math.big
import hm.binkley.math.fixed.FixedBigRational
import hm.binkley.math.fixed.complex.FixedBigComplex.Companion.ONE
import hm.binkley.math.fixed.complex.FixedBigComplex.Companion.ZERO
import hm.binkley.math.fixed.complex.FixedBigImaginary.Companion.I
import hm.binkley.math.fixed.over
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private val ONE_PLUS_I = FixedBigRational.ONE + I
private val ONE_MINUS_I = FixedBigRational.ONE - I
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
                (410137648387709L.big over 15625000000000L.big)
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
        val complex = 1 + I
        val left = 2 + I // complex + one
        val right = 2 + I // one + complex

        (complex + complex) shouldBe (2 + 2.i)
        (complex + FixedBigRational.ONE) shouldBe left
        (FixedBigRational.ONE + complex) shouldBe right
        (complex + 1.big) shouldBe left
        (1.big + complex) shouldBe right
        (complex + 1L) shouldBe left
        (1L + complex) shouldBe right
        (complex + 1) shouldBe left
        (1 + complex) shouldBe right
    }

    @Test
    fun `should subtract`() {
        val complex = 1 + I
        val left = 0 + I // complex - one
        val right = 0 + -I // one - complex

        (complex - complex) shouldBe ZERO
        (complex - FixedBigRational.ONE) shouldBe left
        (FixedBigRational.ONE - complex) shouldBe right
        (complex - 1.big) shouldBe left
        (1.big - complex) shouldBe right
        (complex - 1L) shouldBe left
        (1L - complex) shouldBe right
        (complex - 1) shouldBe left
        (1 - complex) shouldBe right
    }

    @Test
    fun `should multiply`() {
        val complex = 1 + I
        val left = 2 + 2.i // complex * two
        val right = 2 + 2.i // two * complex

        (complex * complex) shouldBe (0 + 2.i)
        (complex * FixedBigRational.TWO) shouldBe left
        (FixedBigRational.TWO * complex) shouldBe right
        (complex * 2.big) shouldBe left
        (2.big * complex) shouldBe right
        (complex * 2L) shouldBe left
        (2L * complex) shouldBe right
        (complex * 2) shouldBe left
        (2 * complex) shouldBe right
    }

    @Test
    fun `should divide`() {
        val complex = 1 + I
        val left = (1 over 2) + (1 over 2).i // complex / two
        val right = (1 over 2) + (1 over 2).i // two / complex

        (complex / complex) shouldBe ONE
        (complex / FixedBigRational.TWO) shouldBe left
        (FixedBigRational.TWO / complex) shouldBe right
        (complex / 2.big) shouldBe left
        (2.big / complex) shouldBe right
        (complex / 2L) shouldBe left
        (2L / complex) shouldBe right
        (complex / 2) shouldBe left
        (2 / complex) shouldBe right
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
        ONE_PLUS_I `^` 0 shouldBe 1 + 0.i
        ONE_PLUS_I `^` 1 shouldBe ONE_PLUS_I
        ONE_PLUS_I `^` -1 shouldBe half - half.i
        ONE_PLUS_I `^` 2 shouldBe 0 + 2.i
        ONE_PLUS_I `^` -2 shouldBe 0 - half.i
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

    @Test
    fun `should square root approximately`() {
        val root = 1 + 2.i

        (root * root).sqrtApproximated() shouldBe root
    }
}
