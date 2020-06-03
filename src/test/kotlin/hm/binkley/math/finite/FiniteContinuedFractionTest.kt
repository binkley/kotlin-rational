package hm.binkley.math.finite

import hm.binkley.math.BInt
import hm.binkley.math.convergent
import hm.binkley.math.finite.FiniteContinuedFraction.Companion.phi
import hm.binkley.math.finite.FixedBigRational.Companion.ONE
import hm.binkley.math.finite.FixedBigRational.Companion.TWO
import hm.binkley.math.finite.FixedBigRational.Companion.ZERO
import hm.binkley.math.isSimple
import hm.binkley.math.minus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

// The ever popular Euler's number, 2.71828 18284 59045...
private val eulerApproximation =
    (271_828_182_845 over 100_000_000_000).toContinuedFraction()

internal class FiniteContinuedFractionTest {
    @Test
    fun `should continue`() {
        assertEquals(
            listOf(2 over 1),
            eulerApproximation.terms(0)
        )
        assertEquals(
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
            ),
            eulerApproximation.terms(14)
        )
    }

    @Test
    fun `should have integer part`() {
        assertEquals(
            TWO,
            (2 over 1).toContinuedFraction().integerPart
        )
    }

    @Test
    fun `should invert`() {
        assertEquals(
            listOf(ZERO, 2 over 1),
            (2 over 1).toContinuedFraction().reciprocal
        )
        assertEquals(
            listOf(2 over 1),
            (1 over 2).toContinuedFraction().reciprocal
        )
    }

    @Test
    fun `should present continued fraction following convention`() {
        assertEquals(
            "[3;]",
            (3 over 1).toContinuedFraction().toString()
        )
        assertEquals(
            "[3; 4, 12, 4]",
            (3245 over 1000).toContinuedFraction().toString()
        )
    }

    @Test
    fun `should convert from continued fraction`() {
        assertEquals(
            (3245 over 1000),
            FiniteContinuedFraction.valueOf(
                3.toBigInteger(),
                4.toBigInteger(),
                12.toBigInteger(),
                4.toBigInteger()
            ).toBigRational()
        )
    }

    @Test
    fun `should check if simple`() {
        assertTrue((2 over 1).toContinuedFraction().isSimple())
        assertFalse((2 over 3).toContinuedFraction().isSimple())
    }

    @Test
    fun `should converge`() {
        assertEquals(2 over 1, eulerApproximation.convergent(0))
        assertEquals(3 over 1, eulerApproximation.convergent(1))
        assertEquals(8 over 3, eulerApproximation.convergent(2))
        assertEquals(11 over 4, eulerApproximation.convergent(3))
        assertEquals(19 over 7, eulerApproximation.convergent(4))
        assertEquals(
            eulerApproximation.toBigRational(),
            eulerApproximation.convergent(
                eulerApproximation.size - 1
            )
        )

        val c1 = eulerApproximation.convergent(1)
        val c2 = eulerApproximation.convergent(2)
        val c3 = eulerApproximation.convergent(3)
        assertEquals(
            BInt.ONE,
            c2.denominator * c1.numerator - c1.denominator * c2.numerator
        )
        assertEquals(
            -BInt.ONE,
            c3.denominator * c2.numerator - c2.denominator * c3.numerator
        )

        assertThrows<IllegalStateException> {
            eulerApproximation.convergent(-1)
        }
        assertThrows<IllegalStateException> {
            (ONE.toContinuedFraction()).convergent(1)
        }
    }

    @Test
    fun `should approximate the golden ratio`() {
        val decimalApproximation = 1_618_033 over 1_000_000
        val approximation = phi(10).toBigRational()

        assertEquals(89 over 55, approximation)
        assertEquals(
            -1637 over 11000000,
            decimalApproximation - approximation
        )
    }
}
