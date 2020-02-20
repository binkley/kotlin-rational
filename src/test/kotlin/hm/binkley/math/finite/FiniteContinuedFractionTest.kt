package hm.binkley.math.finite

import hm.binkley.math.finite.FiniteBigRational.Companion.ONE
import hm.binkley.math.finite.FiniteBigRational.Companion.TWO
import hm.binkley.math.finite.FiniteBigRational.Companion.ZERO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class FiniteContinuedFractionTest {
    @Test
    fun `should continue`() {
        // The ever popular Euler's number, 2.71828 18284 59045...
        val eulerApproximation =
            (271828182845 over 100_000_000_000).toContinuedFraction()
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
            ).toFiniteBigRational()
        )
    }

    @Test
    fun `should check if simple`() {
        assertTrue((2 over 1).toContinuedFraction().isSimple())
        assertFalse((2 over 3).toContinuedFraction().isSimple())
    }
}
