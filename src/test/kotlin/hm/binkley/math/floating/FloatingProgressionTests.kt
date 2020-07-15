package hm.binkley.math.floating

import hm.binkley.math.BDouble
import hm.binkley.math.BInt
import hm.binkley.math.downTo
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational.Companion.TWO
import hm.binkley.math.floating.FloatingBigRational.Companion.ZERO
import hm.binkley.math.rangeTo
import hm.binkley.math.step
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FloatingProgressionTests {
    @Suppress("ReplaceCallWithBinaryOperator")
    @Test
    fun `should be itself`() {
        val zeroToOne = ZERO..ONE
        assertEquals(zeroToOne, zeroToOne)
        assertEquals(zeroToOne, ZERO..ONE)
        assertFalse(zeroToOne.equals(ZERO))
        assertEquals(zeroToOne, zeroToOne step ONE)
        assertNotEquals(zeroToOne step ONE, zeroToOne step TWO)
        assertNotEquals(zeroToOne, ZERO..TWO)
        assertEquals(
            (zeroToOne step (1 over 2)).hashCode(),
            (zeroToOne step (1 over 2)).hashCode()
        )
    }

    @Test
    fun `should pretty print`() {
        assertEquals(
            "0..1 step 1",
            "${(ZERO..ONE)}"
        )
        assertEquals(
            "0..1 step 2",
            "${(ZERO..ONE step 2)}"
        )
        assertEquals(
            "1 downTo 0 step -1",
            "${(ONE downTo ZERO)}"
        )
        assertEquals(
            "1 downTo 0 step -2",
            "${(1 downTo ZERO step -TWO)}"
        )
    }

    @Test
    fun `should progress`() {
        assertTrue(((ZERO..(-ONE)).isEmpty()))
        assertTrue((ZERO..TWO).contains(ONE))
        assertEquals(
            listOf(ONE, (2 over 1)),
            ((1 over 1)..(5 over 2)).toList()
        )
        val three = 3 over 1
        assertEquals(
            listOf(ONE, three),
            (BDouble.ONE..three step 2).toList()
        )
        assertEquals(
            listOf(ONE, three),
            (ONE..BDouble.valueOf(3) step 2).toList()
        )
        assertEquals(
            listOf(ONE, three),
            (1.0..three step 2).toList()
        )
        assertEquals(
            listOf(ONE, three),
            (ONE..3.0 step 2).toList()
        )
        assertEquals(
            listOf(ONE, three),
            (1.0f..three step 2).toList()
        )
        assertEquals(
            listOf(ONE, three),
            (ONE..3.0f step 2).toList()
        )
        assertEquals(
            listOf(ONE, three),
            (BInt.ONE..three step 2).toList()
        )
        assertEquals(
            listOf(ONE, three),
            (ONE..BInt.valueOf(3) step 2).toList()
        )
        assertEquals(
            listOf(ONE, three),
            (1L..three step 2L).toList()
        )
        assertEquals(
            listOf(ONE, three),
            (ONE..3L step 2L).toList()
        )
        assertEquals(
            listOf(ONE, three),
            (1..three step (2 over 1)).toList()
        )
        assertEquals(
            listOf(ONE, three),
            (ONE..3 step (2 over 1)).toList()
        )
        assertEquals(
            listOf((2 over 1), ONE),
            ((2 over 1) downTo (1 over 2) step -BInt.ONE).toList()
        )
    }

    @Suppress("ControlFlowWithEmptyBody")
    @Test
    fun `should not progress`() {
        val noop = { }

        assertThrows<IllegalStateException> {
            for (r in ZERO..NaN) noop()
        }
        assertThrows<IllegalStateException> {
            for (r in NaN..ZERO) noop()
        }
        assertThrows<IllegalStateException> {
            for (r in ZERO..ZERO step NaN) noop()
        }
        assertThrows<IllegalStateException> {
            for (r in ZERO..ONE step -1) noop()
        }
        assertThrows<IllegalStateException> {
            for (r in BInt.ONE downTo ZERO step 1); noop()
        }
        assertThrows<IllegalStateException> {
            for (r in 0L downTo ONE step ZERO); noop()
        }
    }
}
