package hm.binkley.kotlin

public interface SeekableSequence<T> : Sequence<T> {
    /**
     * Finds the "nth" element of the sequence.  Sequences are lazy by design,
     * so this must walk each element the beginning to find the nth element.  No
     * assumption is made that the sequence is bounded.
     *
     * Repeatable: each call starts a new iterator over the sequence.  This
     * assumes the sequence is restartable.
     */
    public operator fun get(index: Int): T {
        if (0 > index) throw IndexOutOfBoundsException("$index: Negative index")
        for ((i, it) in withIndex())
            if (index == i) return it
        throw IndexOutOfBoundsException("$index: Past end")
    }
}
