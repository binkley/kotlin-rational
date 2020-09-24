package hm.binkley.math.floating

import hm.binkley.math.big
import hm.binkley.math.downTo
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational.Companion.TWO
import hm.binkley.math.floating.FloatingBigRational.Companion.ZERO
import hm.binkley.math.rangeTo
import hm.binkley.math.step
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

// TODO: What goes wrong here?
private const val shouldNotBeWorks = false

class FloatingProgressionTests {
    @Suppress("ReplaceCallWithBinaryOperator")
    @Test
    fun `should be itself`() {
        val zeroToOne = ZERO..ONE
        (zeroToOne).shouldBe(zeroToOne)
        (ZERO..ONE).shouldBe(zeroToOne)
        (zeroToOne step ONE).shouldBe(zeroToOne)
        if (shouldNotBeWorks)
            (zeroToOne step TWO).shouldNotBe(zeroToOne step ONE)
        else
            assertNotEquals(zeroToOne step ONE, zeroToOne step TWO)
        if (shouldNotBeWorks)
            (ZERO..TWO).shouldNotBe(zeroToOne)
        else
            assertNotEquals(zeroToOne, ZERO..TWO)
        (zeroToOne step (1 over 2)).hashCode().shouldBe(
            (zeroToOne step (1 over 2)).hashCode(),
        )
    }

    @Test
    fun `should pretty print`() {
        "${(ZERO..ONE)}".shouldBe("0..1 step 1")
        "${(ZERO..ONE step 2)}".shouldBe("0..1 step 2")
        "${(ONE downTo ZERO)}".shouldBe("1 downTo 0 step -1")
        "${(1 downTo ZERO step -TWO)}".shouldBe("1 downTo 0 step -2")
    }

    @Test
    fun `should progress`() {
        (ZERO..(-ONE)).isEmpty().shouldBeTrue()
        (ZERO..TWO).contains(ONE).shouldBeTrue()
        ((1 over 1)..(5 over 2)).toList().shouldBe(
            listOf(ONE, (2 over 1)),
        )
        val three = 3 over 1
        (1.0.big..three step 2).toList().shouldBe(listOf(ONE, three))
        (ONE..3.0.big step 2).toList().shouldBe(listOf(ONE, three))
        (1.0..three step 2).toList().shouldBe(listOf(ONE, three))
        (ONE..3.0 step 2).toList().shouldBe(listOf(ONE, three))
        (1.0f..three step 2).toList().shouldBe(listOf(ONE, three))
        (ONE..3.0f step 2).toList().shouldBe(listOf(ONE, three))
        (1.big..three step 2).toList().shouldBe(listOf(ONE, three))
        (ONE..3.big step 2).toList().shouldBe(listOf(ONE, three))
        (1L..three step 2L).toList().shouldBe(listOf(ONE, three))
        (ONE..3L step 2L).toList().shouldBe(listOf(ONE, three))
        (1..three step (2 over 1)).toList().shouldBe(listOf(ONE, three))
        (ONE..3 step (2 over 1)).toList().shouldBe(listOf(ONE, three))
        ((2 over 1) downTo (1 over 2) step -(1.big)).toList().shouldBe(
            listOf((2 over 1), ONE),
        )
    }

    @Suppress("ControlFlowWithEmptyBody")
    @Test
    fun `should not progress`() {
        val noop = { }

        shouldThrow<IllegalStateException> {
            for (r in ZERO..NaN) noop()
        }
        shouldThrow<IllegalStateException> {
            for (r in NaN..ZERO) noop()
        }
        shouldThrow<IllegalStateException> {
            for (r in ZERO..ZERO step NaN) noop()
        }
        shouldThrow<IllegalStateException> {
            for (r in ZERO..ONE step -1) noop()
        }
        shouldThrow<IllegalStateException> {
            for (r in 1.big downTo ZERO step 1); noop()
        }
        shouldThrow<IllegalStateException> {
            for (r in 0L downTo ONE step ZERO); noop()
        }
    }
}
