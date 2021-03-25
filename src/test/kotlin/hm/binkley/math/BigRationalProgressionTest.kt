package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.TWO
import hm.binkley.math.TestBigRational.Companion.ZERO
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class BigRationalProgressionTest {
    @Test
    fun `should equate`() {
        ((ZERO..TWO step 2) == ZERO..TWO step 2).shouldBeTrue()
        ((ZERO..TWO step 1) == ZERO..TWO step 2).shouldBeFalse()
    }

    @Test
    fun `should move forward for big rational`() {
        (ZERO..TWO).toList() shouldBe listOf(ZERO, ONE, TWO)
    }

    @Test
    fun `should move forward for big decimal`() {
        (0.0.big..TWO).toList() shouldBe listOf(ZERO, ONE, TWO)
        (ZERO..(2.0.big)).toList() shouldBe listOf(ZERO, ONE, TWO)
    }

    @Test
    fun `should move forward for double`() {
        (0.0..TWO).toList() shouldBe listOf(ZERO, ONE, TWO)
        (ZERO..2.0).toList() shouldBe listOf(ZERO, ONE, TWO)
    }

    @Test
    fun `should move forward for float`() {
        (0.0f..TWO).toList() shouldBe listOf(ZERO, ONE, TWO)
        (ZERO..2.0f).toList() shouldBe listOf(ZERO, ONE, TWO)
    }

    @Test
    fun `should move forward for big integer`() {
        (0.big..TWO).toList() shouldBe listOf(ZERO, ONE, TWO)
        (ZERO..(2.big)).toList() shouldBe listOf(ZERO, ONE, TWO)
    }

    @Test
    fun `should move forward for long`() {
        (0L..TWO).toList() shouldBe listOf(ZERO, ONE, TWO)
        (ZERO..2L).toList() shouldBe listOf(ZERO, ONE, TWO)
    }

    @Test
    fun `should move forward for int`() {
        (0..TWO).toList() shouldBe listOf(ZERO, ONE, TWO)
        (ZERO..2).toList() shouldBe listOf(ZERO, ONE, TWO)
    }

    @Test
    fun `should move forward in steps for big rational`() {
        (ZERO..TWO step TWO).toList() shouldBe listOf(ZERO, TWO)
    }

    @Test
    fun `should move forward in steps for big decimal`() {
        (ZERO..TWO step 2.0.big).toList() shouldBe listOf(ZERO, TWO)
    }

    @Test
    fun `should move forward in steps for double`() {
        (ZERO..TWO step 2.0).toList() shouldBe listOf(ZERO, TWO)
    }

    @Test
    fun `should move forward in steps for float`() {
        (ZERO..TWO step 2.0f).toList() shouldBe listOf(ZERO, TWO)
    }

    @Test
    fun `should move forward in steps for big integer`() {
        (ZERO..TWO step 2.big).toList() shouldBe listOf(ZERO, TWO)
    }

    @Test
    fun `should move forward in steps for long`() {
        (ZERO..TWO step 2L).toList() shouldBe listOf(ZERO, TWO)
    }

    @Test
    fun `should move forward in steps for int`() {
        (ZERO..TWO step 2).toList() shouldBe listOf(ZERO, TWO)
    }

    @Test
    fun `should move backward for big rational`() {
        (TWO downTo ZERO).toList() shouldBe listOf(TWO, ONE, ZERO)
    }

    @Test
    fun `should move backward for big decimal`() {
        (2.0.big downTo ZERO).toList() shouldBe listOf(TWO, ONE, ZERO)
        (TWO downTo 0.0.big).toList() shouldBe listOf(TWO, ONE, ZERO)
    }

    @Test
    fun `should move backward for double`() {
        (2.0 downTo ZERO).toList() shouldBe listOf(TWO, ONE, ZERO)
        (TWO downTo 0.0).toList() shouldBe listOf(TWO, ONE, ZERO)
    }

    @Test
    fun `should move backward for float`() {
        (2.0f downTo ZERO).toList() shouldBe listOf(TWO, ONE, ZERO)
        (TWO downTo 0.0f).toList() shouldBe listOf(TWO, ONE, ZERO)
    }

    @Test
    fun `should move backward for big integer`() {
        (2.big downTo ZERO).toList() shouldBe listOf(TWO, ONE, ZERO)
        (TWO downTo 0.big).toList() shouldBe listOf(TWO, ONE, ZERO)
    }

    @Test
    fun `should move backward for long`() {
        (2L downTo ZERO).toList() shouldBe listOf(TWO, ONE, ZERO)
        (TWO downTo 0L).toList() shouldBe listOf(TWO, ONE, ZERO)
    }

    @Test
    fun `should move backward for int`() {
        (2 downTo ZERO).toList() shouldBe listOf(TWO, ONE, ZERO)
        (TWO downTo 0).toList() shouldBe listOf(TWO, ONE, ZERO)
    }

    @Test
    fun `should complain for wrong direction`() {
        shouldThrow<IllegalStateException> {
            (ONE..ZERO).toList()
        }
    }

    @Test
    fun `should complain for infinitesimal steps`() {
        shouldThrow<IllegalStateException> {
            (ZERO..ONE step ZERO).toList()
        }
    }
}
