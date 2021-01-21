package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.algebra.Group
import hm.binkley.math.big
import hm.binkley.math.fixed.FixedBigImaginary.Companion.I
import hm.binkley.math.fixed.FixedBigImaginary.Companion.ZERO
import hm.binkley.math.fixed.FixedBigRational.Companion.ONE
import hm.binkley.math.fixed.FixedBigRational.Companion.TWO
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test

internal class FixedBigImaginaryTest {
    @Test
    fun `should be a group`() {
        I.shouldBeInstanceOf<Group<FixedBigImaginary>>()
    }

    @Test
    fun `should pick correct branch`() {
        I * I shouldBe -ONE
    }

    @Test
    fun `should have value`() {
        I.value shouldBe ONE
    }

    @Test
    fun `should pretty print`() {
        "${+I}" shouldBe "1i"
        "${-I}" shouldBe "-1i"
        "${ONE.i}" shouldBe "1i"
        "${(-ONE).i}" shouldBe "-1i"
        "${(1.big.i)}" shouldBe "1i"
        "${-(1.big).i}" shouldBe "-1i"
        "${1L.i}" shouldBe "1i"
        "${(-1L).i}" shouldBe "-1i"
        "${1.i}" shouldBe "1i"
        "${(-1).i}" shouldBe "-1i"
    }

    @Test
    fun `should positivize`() {
        +I shouldBe 1.i
    }

    @Test
    fun `should negate`() {
        -I shouldBe (-1).i
    }

    @Test
    fun `should add`() {
        (1.i + 1.i) shouldBe 2.i
    }

    @Test
    fun `should subtract`() {
        (1.i - 1.i) shouldBe ZERO
    }

    @Test
    fun `should multiply`() {
        (2.i * I) shouldBe -TWO
        (TWO * I) shouldBe 2.i
        (BInt.TWO * I) shouldBe 2.i
        (2L * I) shouldBe 2.i
        (2 * I) shouldBe 2.i
    }

    @Test
    fun `should compare`() {
        (I < 2.i).shouldBeTrue()
        (I <= 2.i).shouldBeTrue()
        (I <= I).shouldBeTrue()
        (I == I).shouldBeTrue()
        (I >= I).shouldBeTrue()
        (I >= ZERO).shouldBeTrue()
        (I > ZERO).shouldBeTrue()
    }
}
