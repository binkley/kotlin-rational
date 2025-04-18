package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.TWO
import hm.binkley.math.TestBigRational.Companion.ZERO
import hm.binkley.math.TestContinuedFraction.Companion.e
import hm.binkley.math.TestContinuedFraction.Companion.phi
import hm.binkley.math.TestContinuedFraction.Companion.root2
import hm.binkley.math.TestContinuedFraction.Companion.root3
import hm.binkley.math.TestContinuedFraction.Companion.valueOf
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

internal class ContinuedFractionTest {
    @Test
    fun `should be or not be itself`() {
        val cf = valueOf(TWO)

        @Suppress("KotlinConstantConditions")
        (cf == cf).shouldBeTrue()
        cf shouldNotBe 2
        cf shouldNotBe valueOf(ONE)
    }

    @Test
    fun `should know fractional parts`() {
        valueOf(ZERO).fractionalParts shouldBe emptyList()
        valueOf(TWO).fractionalParts shouldBe emptyList()
        valueOf(3 over 2).fractionalParts shouldBe listOf(TWO)
    }

    @Test
    fun `should find continued fraction`() {
        val cfA = valueOf(bRat(3245.big, 1000.big))
        cfA shouldBe listOf(
            bRat(3.big, 1.big),
            bRat(4.big, 1.big),
            bRat(12.big, 1.big),
            bRat(4.big, 1.big),
        )
        cfA.toBigRational() shouldBe bRat(3245.big, 1000.big)
        val negCfA = valueOf(bRat((-3245).big, 1000.big))
        negCfA shouldBe listOf(
            bRat((-4).big, 1.big),
            ONE,
            bRat(3.big, 1.big),
            bRat(12.big, 1.big),
            bRat(4.big, 1.big),
        )
        negCfA.toBigRational() shouldBe bRat((-3245).big, 1000.big)
        valueOf(ZERO) shouldBe listOf(ZERO)
        valueOf(ONE) shouldBe listOf(ONE)
        valueOf(bRat(1.big, 3.big)) shouldBe listOf(
            ZERO,
            bRat(3.big, 1.big),
        )
    }

    @Suppress("DEPRECATION")
    @Test
    fun `should not be a language character`() {
        shouldThrow<UnsupportedOperationException> {
            valueOf(ZERO).toChar()
        }
    }

    @Test
    fun `should approximate Euler's number`() {
        val decimalApproximation = 2_718_282 over 1_000_000
        val approximation = e(10).toBigRational()

        approximation shouldBe (1_457 over 536)
        (decimalApproximation - approximation) shouldBe (-53 over 33_500_000)

        shouldThrow<IllegalStateException> { e(0) }
        shouldThrow<IllegalStateException> { e(-1) }
    }

    @Test
    fun `should approximate the golden ratio`() {
        val decimalApproximation = 1_618_033 over 1_000_000
        val approximation = phi(10).toBigRational()

        approximation shouldBe (89 over 55)
        (decimalApproximation - approximation) shouldBe (-1_637 over 11_000_000)

        shouldThrow<IllegalStateException> { phi(0) }
        shouldThrow<IllegalStateException> { phi(-1) }
    }

    @Test
    fun `should approximate root two`() {
        val decimalApproximation = 1_414_214 over 1_000_000
        val approximation = root2(10).toBigRational()

        approximation shouldBe (3363 over 2378)
        (decimalApproximation - approximation) shouldBe (223 over 594_500_000)

        shouldThrow<IllegalStateException> { root2(0) }
        shouldThrow<IllegalStateException> { root2(-1) }
    }

    @Test
    fun `should approximate root three`() {
        val decimalApproximation = 1_732_051 over 1_000_000
        val approximation = root3(10).toBigRational()

        approximation shouldBe (362 over 209)
        (decimalApproximation - approximation) shouldBe
            (-1_341 over 209_000_000)

        shouldThrow<IllegalStateException> { root3(0) }
        shouldThrow<IllegalStateException> { root3(-1) }
    }
}

private fun bRat(numerator: BFixed, denominator: BFixed) =
    TestBigRational.valueOf(numerator, denominator)
