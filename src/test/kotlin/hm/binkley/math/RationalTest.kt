package hm.binkley.math

import hm.binkley.math.Rational.Companion.NaN
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

internal class RationalTest {
    @Test
    fun `should construct NaN`() {
        assertSame(
            NaN,
            0 over 0
        )
    }
}
