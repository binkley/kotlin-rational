package hm.binkley.kotlin

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SeekableSequenceTest {
    @Test
    fun `should select valid indices`() {
        TestSeekeableSequence[0] shouldBe "abc"
    }

    @Test
    fun `should reject negative indices`() {
        shouldThrow<IndexOutOfBoundsException> {
            TestSeekeableSequence[-1]
        }
    }

    @Test
    fun `should reject excessive indices`() {
        shouldThrow<IndexOutOfBoundsException> {
            TestSeekeableSequence[1]
        }
    }
}

private object TestSeekeableSequence :
    SeekableSequence<String>,
    Sequence<String> by sequenceOf("abc")
