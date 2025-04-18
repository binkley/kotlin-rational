package hm.binkley.kotlin.sequences

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SequenceExtensionsTest {
    private val sequence = sequenceOf(1, 2)

    @Test
    fun `should complain for negative indices`() {
        shouldThrow<IndexOutOfBoundsException> {
            sequence[-1]
        }
    }

    @Test
    fun `should get first two elements`() {
        listOf(sequence[0], sequence[1]) shouldBe listOf(1, 2)
    }

    @Test
    fun `should complain when exhausting sequence`() {
        shouldThrow<IndexOutOfBoundsException> {
            sequence[2]
        }
    }
}
