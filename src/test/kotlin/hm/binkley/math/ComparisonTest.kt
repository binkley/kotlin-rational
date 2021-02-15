package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.ZERO
import hm.binkley.math.fixed.FixedBigRational
import hm.binkley.math.floating.FloatingBigRational
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ComparisonTest {
    @Test
    fun `should compare to big rational`() {
        (ONE > ZERO).shouldBeTrue()
        (ZERO < ONE).shouldBeTrue()
    }

    @Test
    fun `should compare to big decimal`() {
        (1.0.big > ZERO).shouldBeTrue()
        (ONE > 0.0.big).shouldBeTrue()
    }

    @Test
    fun `should compare to double`() {
        (1.0 > ZERO).shouldBeTrue()
        (ONE > 0.0).shouldBeTrue()
    }

    @Test
    fun `should compare to float`() {
        (1.0f > ZERO).shouldBeTrue()
        (ONE > 0.0f).shouldBeTrue()
    }

    @Test
    fun `should compare to big integer`() {
        (1.big > ZERO).shouldBeTrue()
        (ONE > 0.big).shouldBeTrue()
    }

    @Test
    fun `should compare to long`() {
        (1L > ZERO).shouldBeTrue()
        (ONE > 0L).shouldBeTrue()
    }

    @Test
    fun `should compare to int`() {
        (1 > ZERO).shouldBeTrue()
        (ONE > 0).shouldBeTrue()
    }

    @Test
    fun `should sort`() {
        val sorted = listOf(
            ZERO,
            ONE,
            ZERO
        ).sorted()

        sorted shouldBe listOf(
            ZERO,
            ZERO,
            ONE
        )
    }

    @Test
    fun `should treat fixed and floating equivalently`() {
        FixedBigRational.ZERO.equivalent(FloatingBigRational.ZERO)
            .shouldBeTrue()
        FixedBigRational.ZERO.equivalent(FloatingBigRational.ONE)
            .shouldBeFalse()
    }

    @Test
    fun `should compare fixed and floating`() {
        FixedBigRational.ZERO.compareTo(FloatingBigRational.ZERO) shouldBe 0
        FixedBigRational.ZERO.compareTo(FloatingBigRational.ONE) shouldBe -1
        FixedBigRational.ONE.compareTo(FloatingBigRational.ZERO) shouldBe 1
    }
}
