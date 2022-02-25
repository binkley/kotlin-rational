package hm.binkley.math.floating.complex

import hm.binkley.math.BFixed
import hm.binkley.math.BFloating
import hm.binkley.math.big
import hm.binkley.math.floating.FloatingBigRational
import hm.binkley.math.floating.complex.FloatingBigComplex.Companion.ONE
import hm.binkley.math.floating.complex.FloatingBigComplex.Companion.ZERO
import hm.binkley.math.floating.complex.FloatingBigImaginary.Companion.I
import hm.binkley.math.floating.isNaN
import hm.binkley.math.floating.over
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Test

private val ONE_PLUS_I = FloatingBigRational.ONE + I
private val ONE_MINUS_I = FloatingBigRational.ONE - I
private val NEG_ONE_MINUS_I = -FloatingBigRational.ONE - I

internal class FloatingBigComplexTest {
    @Test
    fun `should have constants`() {
        ONE.companion shouldBeSameInstanceAs FloatingBigComplex.Companion
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
        val complex = 1 + 1.i
        val left = 2 + I // complex + one
        val right = 2 + I // one + complex

        (I + complex) shouldBe (1 + 2.i)
        (complex + I) shouldBe (1 + 2.i)
        (I + FloatingBigRational.ONE) shouldBe complex
        (FloatingBigRational.ONE + I) shouldBe complex
        (I + BFloating.ONE) shouldBe complex
        (BFloating.ONE + I) shouldBe complex
        (I + 1.0) shouldBe complex
        (1.0 + I) shouldBe complex
        (I + 1.0f) shouldBe complex
        (1.0f + I) shouldBe complex
        (I + BFixed.ONE) shouldBe complex
        (BFixed.ONE + I) shouldBe complex
        (I + 1L) shouldBe complex
        (1L + I) shouldBe complex
        (I + 1) shouldBe complex
        (1 + I) shouldBe complex

        (complex + complex) shouldBe 2 + 2.i
        (complex + FloatingBigRational.ONE) shouldBe left
        (FloatingBigRational.ONE + complex) shouldBe right
        (complex + 1.0.big) shouldBe left
        (1.0.big + complex) shouldBe right
        (complex + 1.0) shouldBe left
        (1.0 + complex) shouldBe right
        (complex + 1.0f) shouldBe left
        (1.0f + complex) shouldBe right
        (complex + 1.big) shouldBe left
        (1.big + complex) shouldBe right
        (complex + 1L) shouldBe left
        (1L + complex) shouldBe right
        (complex + 1) shouldBe left
        (1 + complex) shouldBe right
    }

    @Test
    fun `should subtract`() {
        val complex = 1 + 1.i
        val left = 0 + I // complex - one
        val right = 0 + -I // one - complex

        (I - complex) shouldBe (-1 + 0.i)
        (complex - I) shouldBe (1 + 0.i)
        (I - FloatingBigRational.ONE) shouldBe (-1 + 1.i)
        (FloatingBigRational.ONE - I) shouldBe (1 - 1.i)
        (I - BFloating.ONE) shouldBe (-1 + 1.i)
        (BFloating.ONE - I) shouldBe (1 - 1.i)
        (I - 1.0) shouldBe (-1 + 1.i)
        (1.0 - I) shouldBe (1 - 1.i)
        (I - 1.0f) shouldBe (-1 + 1.i)
        (1.0f - I) shouldBe (1 - 1.i)
        (I - BFixed.ONE) shouldBe (-1 + 1.i)
        (BFixed.ONE - I) shouldBe (1 - 1.i)
        (I - 1L) shouldBe (-1 + 1.i)
        (1L - I) shouldBe (1 - 1.i)
        (I - 1) shouldBe (-1 + 1.i)
        (1 - I) shouldBe (1 - 1.i)

        (complex - complex) shouldBe ZERO
        (complex - FloatingBigRational.ONE) shouldBe left
        (FloatingBigRational.ONE - complex) shouldBe right
        (complex - 1.0.big) shouldBe left
        (1.0.big - complex) shouldBe right
        (complex - 1.0) shouldBe left
        (1.0 - complex) shouldBe right
        (complex - 1.0f) shouldBe left
        (1.0f - complex) shouldBe right
        (complex - 1.big) shouldBe left
        (1.big - complex) shouldBe right
        (complex - 1L) shouldBe left
        (1L - complex) shouldBe right
        (complex - 1) shouldBe left
        (1 - complex) shouldBe right
    }

    @Test
    fun `should multiply`() {
        val complex = 1 + 1.i
        val left = 2 + 2.i // complex * two
        val right = 2 + 2.i // two * complex

        (I * complex) shouldBe (-1 + 1.i)
        (complex * I) shouldBe (-1 + 1.i)

        (complex * complex) shouldBe (0 + 2.i)
        (complex * FloatingBigRational.TWO) shouldBe left
        (FloatingBigRational.TWO * complex) shouldBe right
        (complex * 2.0.big) shouldBe left
        (2.0.big * complex) shouldBe right
        (complex * 2.0) shouldBe left
        (2.0 * complex) shouldBe right
        (complex * 2.0f) shouldBe left
        (2.0f * complex) shouldBe right
        (complex * 2.big) shouldBe left
        (2.big * complex) shouldBe right
        (complex * 2L) shouldBe left
        (2L * complex) shouldBe right
        (complex * 2) shouldBe left
        (2 * complex) shouldBe right
    }

    @Test
    fun `should divide`() {
        val complex = 1 + 1.i
        val left = (1 over 2) + (1 over 2).i // complex / two
        val right = (1 over 2) + (1 over 2).i // two / complex

        (I / complex) shouldBe (1 - 1.i)
        (complex / I) shouldBe (1 - 1.i)

        (complex / complex) shouldBe ONE
        (complex / FloatingBigRational.TWO) shouldBe left
        (FloatingBigRational.TWO / complex) shouldBe right
        (complex / 2.0.big) shouldBe left
        (2.0.big / complex) shouldBe right
        (complex / 2.0) shouldBe left
        (2.0 / complex) shouldBe right
        (complex / 2.0f) shouldBe left
        (2.0f / complex) shouldBe right
        (complex / 2.big) shouldBe left
        (2.big / complex) shouldBe right
        (complex / 2L) shouldBe left
        (2L / complex) shouldBe right
        (complex / 2) shouldBe left
        (2 / complex) shouldBe right
    }

    @Test
    fun `should convert`() {
        val one = ONE
        one.toBigRational() shouldBe FloatingBigRational.ONE
        one.toBigDecimal() shouldBe BFloating("1.0")
        one.toDouble() shouldBe 1.0
        one.toFloat() shouldBe 1.0f
        one.toBigInteger() shouldBe BFixed.ONE
        one.toLong() shouldBe 1L
        one.toInt() shouldBe 1
        (0 + 1.i).toImaginary() shouldBe 1.i
        // Sad paths
        shouldThrow<ArithmeticException> { ONE_PLUS_I.toBigRational() }
        shouldThrow<ArithmeticException> { ONE_PLUS_I.toBigDecimal() }
        shouldThrow<ArithmeticException> { ONE_PLUS_I.toDouble() }
        shouldThrow<ArithmeticException> { ONE_PLUS_I.toFloat() }
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

    /** @todo Test all rules in [Math.pow] */
    @Test
    fun `should define by fiat 0^0`() {
        ZERO `^` 0 shouldBe ONE
    }

    @Test
    fun `should fail gracelessly on inverting zero`() {
        // TODO As NaN is not comparable to itself, cleaner test?
        val (real, imag) = ZERO `^` -1
        real.isNaN().shouldBeTrue()
        imag.value.isNaN().shouldBeTrue()
    }

    @Test
    fun `should square root approximately`() {
        val root = 1 + 2.i

        (root * root).sqrtApproximated() shouldBe root
    }
}
