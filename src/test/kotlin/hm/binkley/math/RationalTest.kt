@file:Suppress("NonAsciiCharacters")

package hm.binkley.math

import hm.binkley.math.Rational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.Rational.Companion.NaN
import hm.binkley.math.Rational.Companion.ONE
import hm.binkley.math.Rational.Companion.POSITIVE_INFINITY
import hm.binkley.math.Rational.Companion.ZERO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.math.BigInteger

internal class RationalTest {
    @Test
    fun `should construct NaN`() {
        assertSame(
            NaN,
            0 over 0
        )
    }

    @Test
    fun `should construct +∞`() {
        assertSame(
            POSITIVE_INFINITY,
            Long.MAX_VALUE over 0
        )
    }

    @Test
    fun `should construct -∞`() {
        assertSame(
            NEGATIVE_INFINITY,
            Long.MIN_VALUE over 0
        )
    }

    @Test
    fun `should construct 0`() {
        assertSame(
            ZERO,
            0 over Long.MIN_VALUE
        )
    }

    @Test
    fun `should reduce fractions`() {
        assertEquals(
            Rational.new(2),
            4 over 2
        )
    }

    @Test
    fun `should simplify fractions`() {
        assertEquals(
            1 over 2,
            4 over 8
        )
        assertEquals(
            BigInteger.valueOf(2),
            (4 over 8).denominator
        )
    }

    @Test
    fun `should maintain positive denominator`() {
        assertEquals(
            -ONE,
            4 over -4
        )
        assertEquals(
            BigInteger.ONE,
            (4 over -4).denominator
        )
    }

    @Test
    fun `should add`() {
        assertEquals(
            19 over 15,
            (3 over 5) + (2 over 3)
        )
    }

    @Test
    fun `should subtract`() {
        assertEquals(
            -1 over 15,
            (3 over 5) - (2 over 3)
        )
    }

    @Test
    fun `should multiply`() {
        assertEquals(
            2 over 5,
            (3 over 5) * (2 over 3)
        )
    }

    @Test
    fun `should divide`() {
        assertEquals(
            9 over 10,
            (3 over 5) / (2 over 3)
        )
    }

    @Test
    fun `should raise`() {
        assertEquals(
            9 over 25,
            (3 over 5).pow(2)
        )
        assertEquals(
            25 over 9,
            (3 over 5).pow(-2)
        )
    }

    @Test
    fun `should absolute`() {
        assertEquals(
            3 over 5,
            (3 over 5).abs()
        )
        assertEquals(
            3 over 5,
            (-3 over 5).abs()
        )
    }

    @Test
    fun `should signum`() {
        assertEquals(
            1,
            (3 over 5).signum()
        )
        assertEquals(
            0,
            (0 over 5).signum()
        )
        assertEquals(
            -1,
            (-3 over 5).signum()
        )
    }

    @Test
    fun `should progress`() {
        assertEquals(
            listOf(ONE, (2 over 1)),
            ((1 over 1)..(2 over 1)).toList()
        )
        assertEquals(
            listOf((2 over 1), ONE),
            ((2 over 1) downTo (1 over 1)).toList()
        )
    }
}
