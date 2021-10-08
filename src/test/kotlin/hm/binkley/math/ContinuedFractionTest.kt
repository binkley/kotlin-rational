package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.TWO
import hm.binkley.math.TestBigRational.Companion.ZERO
import hm.binkley.math.TestContinuedFraction.Companion.valueOf
import hm.binkley.math.floating.FloatingBigRational
import hm.binkley.math.floating.toContinuedFraction
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

internal class ContinuedFractionTest {
    @Test
    fun `should be or not be itself`() {
        val cf = valueOf(TWO)

        cf.equals(cf).shouldBeTrue()
        cf shouldNotBe 2
        cf shouldNotBe valueOf(ONE)
    }

    @Test
    fun `should know fractional parts`() {
        FloatingBigRational.NaN.toContinuedFraction()
            .fractionalParts shouldBe emptyList()
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

    @Test
    fun `should not be a language character`() {
        shouldThrow<UnsupportedOperationException> {
            valueOf(ZERO).toChar()
        }
    }
}

private fun bRat(numerator: BInt, denominator: BInt) =
    TestBigRational.valueOf(numerator, denominator)
