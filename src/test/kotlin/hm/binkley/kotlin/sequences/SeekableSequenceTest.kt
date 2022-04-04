package hm.binkley.kotlin.sequences

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SeekableSequenceTest {
    @Test
    fun `should select first valid index`() {
        TestSeekableSequence[0] shouldBe "abc"
    }

    @Test
    fun `should select second valid index`() {
        TestSeekableSequence[1] shouldBe "def"
    }

    @Test
    fun `should reject negative indices`() {
        shouldThrow<IndexOutOfBoundsException> {
            TestSeekableSequence[-1]
        }
    }

    @Test
    fun `should reject excessive indices`() {
        shouldThrow<IndexOutOfBoundsException> {
            TestSeekableSequence[2]
        }
    }
}

private object TestSeekableSequence :
    SeekableSequence<String>,
    Sequence<String> by sequenceOf("abc", "def")
