package hm.binkley.kotlin

public interface SeekableSequence<T> : Sequence<T> {
    /**
     * Finds the "nth" element of the sequence.  Sequences are lazy by design,
     * so this must walk each element from the beginning to find the nth
     * element.  No assumption is made that the sequence is bounded: this could
     * be very expensive for large indices or hard-to-compute sequences.
     *
     * The index must be non-negative, else throws `IndexOutOfBoundsException`.
     *
     * Repeatable: each call starts a new iterator over the sequence.  This
     * assumes the sequence is restartable.
     */
    public operator fun get(index: Int): T {
        // NB -- check up front: A sequence could be infinite length
        if (0 > index) throw IndexOutOfBoundsException("$index: Negative index")
        withIndex().forEach { (i, it) ->
            if (index == i) return it
        }
        throw IndexOutOfBoundsException("$index: Past end")
    }
}
