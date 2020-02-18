package hm.binkley.math

import hm.binkley.math.BigRational.Companion.ONE
import hm.binkley.math.BigRational.Companion.ZERO
import org.junit.jupiter.api.Assertions.assertEquals
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
}
