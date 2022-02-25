package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.ZERO
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class CantorSpiralTest {
    @Test
    fun `should find Cantor spiral`() {
        CantorSpiral(TestBigRational).take(17).toList() shouldBe
                listOf(
                    0 over 1,
                    1 over 1,
                    1 over -1,
                    -1 over 2,
                    1 over 2,
                    2 over 1,
                    2 over -1,
                    -2 over 3,
                    -1 over 3,
                    1 over 3,
                    2 over 3,
                    3 over 2,
                    3 over 1,
                    3 over -1,
                    3 over -2,
                    -3 over 4,
                    -1 over 4,
                )
    }

    @Test
    fun `should find specific Cantor spiral element`() {
        val spiral = CantorSpiral(TestBigRational)
        spiral[2] shouldBe -ONE
        spiral[0] shouldBe ZERO
        // Cantor spiral has no limit -- no upper bound to test

        shouldThrow<IndexOutOfBoundsException> {
            spiral[-1]
        }
    }
}
