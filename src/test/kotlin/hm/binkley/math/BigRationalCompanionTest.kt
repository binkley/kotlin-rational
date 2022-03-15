package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.TWO
import hm.binkley.math.TestBigRational.Companion.ZERO
import hm.binkley.math.TestBigRational.Companion.average
import hm.binkley.math.TestBigRational.Companion.sum
import hm.binkley.math.TestBigRational.Companion.sumOf
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class BigRationalCompanionTest {
    @Nested
    inner class CollectionTests {
        @Test
        fun `should average`() {
            listOf(ZERO, ONE, TWO).average() shouldBe ONE
        }

        @Test
        fun `should sum`() {
            listOf(ZERO, ONE, TWO).sum() shouldBe (3 over 1)
        }

        @Test
        fun `should sum of`() {
            listOf(ZERO, ONE, TWO).sumOf { it * 2 } shouldBe (6 over 1)
        }
    }
}
