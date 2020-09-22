package hm.binkley.math.floating

import hm.binkley.math.big
import hm.binkley.math.convergent
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational.Companion.POSITIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.TWO
import hm.binkley.math.floating.FloatingBigRational.Companion.ZERO
import hm.binkley.math.floating.FloatingContinuedFraction.Companion.phi
import hm.binkley.math.isSimple
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Test

// The ever popular Euler's number, 2.71828 18284 59045...
private val eulerApproximation =
    (271_828_182_845 over 100_000_000_000).toContinuedFraction()

internal class FloatingContinuedFractionTest {
    @Test
    fun `should be a list`() {
        val c = (145 over 7).toContinuedFraction()

        c.shouldBe(listOf(20 over 1, ONE, TWO, TWO))
    }

    @Test
    fun `should continue`() {
        eulerApproximation.terms(0).shouldBe(listOf(2 over 1))
        eulerApproximation.terms(14).shouldBe(
            listOf(
                2 over 1,
                ONE,
                2 over 1,
                ONE,
                ONE,
                4 over 1,
                ONE,
                ONE,
                6 over 1,
                ONE,
                ONE,
                8 over 1,
                ONE,
                ONE,
                10 over 1
// truncated from here
            )
        )
    }

    @Test
    fun `should have integer part`() =
        ((2 over 1).toContinuedFraction().integerPart).shouldBe(TWO)

    @Test
    fun `should invert`() {
        (2 over 1).toContinuedFraction().unaryDiv().shouldBe(
            listOf(ZERO, 2 over 1),
        )
        (1 over 2).toContinuedFraction().reciprocal.shouldBe(
            listOf(2 over 1),
        )
    }

    @Test
    fun `should present continued fraction following convention`() {
        "${(3 over 1).toContinuedFraction()}".shouldBe("[3;]")
        "${(3245 over 1000).toContinuedFraction()}".shouldBe("[3; 4, 12, 4]")
    }

    @Test
    fun `should convert from continued fraction`() {
        FloatingContinuedFraction.valueOf(
            3.toBigInteger(),
            4.toBigInteger(),
            12.toBigInteger(),
            4.toBigInteger()
        ).toBigRational().shouldBe(3245 over 1000)

        POSITIVE_INFINITY.toContinuedFraction().toBigRational()
            .shouldBeSameInstanceAs(NaN)
    }

    @Test
    fun `should check if simple`() {
        (2 over 1).toContinuedFraction().isSimple().shouldBeTrue()
        (2 over 3).toContinuedFraction().isSimple().shouldBeFalse()
    }

    @Test
    fun `should converge`() {
        eulerApproximation.convergent(0).shouldBe(2 over 1)
        eulerApproximation.convergent(1).shouldBe(3 over 1)
        eulerApproximation.convergent(2).shouldBe(8 over 3)
        eulerApproximation.convergent(3).shouldBe(11 over 4)
        eulerApproximation.convergent(4).shouldBe(19 over 7)
        eulerApproximation.convergent(eulerApproximation.size - 1).shouldBe(
            eulerApproximation.toBigRational(),
        )

        val c1 = eulerApproximation.convergent(1)
        val c2 = eulerApproximation.convergent(2)
        val c3 = eulerApproximation.convergent(3)
        (c2.denominator * c1.numerator - c1.denominator * c2.numerator).shouldBe(
            1.big,
        )
        (c3.denominator * c2.numerator - c2.denominator * c3.numerator).shouldBe(
            -(1.big),
        )

        shouldThrow<IllegalStateException> {
            eulerApproximation.convergent(-1)
        }
        shouldThrow<IllegalStateException> {
            ONE.toContinuedFraction().convergent(1)
        }
    }

    @Test
    fun `should approximate the golden ratio`() {
        val decimalApproximation = 1_618_033 over 1_000_000
        val approximation = phi(10).toBigRational()

        approximation.shouldBe(89 over 55)
        (decimalApproximation - approximation).shouldBe(-1637 over 11000000)

        shouldThrow<IllegalStateException> { phi(0) }
        shouldThrow<IllegalStateException> { phi(-1) }
    }
}
