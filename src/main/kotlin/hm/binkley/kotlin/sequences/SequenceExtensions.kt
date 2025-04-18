package hm.binkley.kotlin.sequences

/**
 * Provides an index operator for [Sequence] to improve developer UX.
 * This allows using square bracket notation for indexing elements in a sequence.
 *
 * Example:
 * ```
 * val element = mySequence[index]
 * ```
 *
 * Sequences are lazy by design, so this must walk each element from the
 * beginning to find the nth element.
 * No assumption is made that the sequence is bounded: this could be very
 * expensive for large indices or hard-to-compute sequences.
 *
 * The index is typically non-negative, but depends on the underlying
 * sequence, and should throw `IndexOutOfBoundsException` when a boundary
 * constraint is violated.
 * Usually the sequence in its implementation of [Sequence.elementAt]
 * relies on the an iterator that may go forward, but some may use a
 * [ListIterator] that can go backward.
 *
 * Repeatable: each call starts a new iterator over the sequence.
 * This assumes the sequence is restartable.
 *
 * @param index the index of the element to return
 * @return the element at the specified position in this sequence
 * @throws IndexOutOfBoundsException if the index is out of bounds
 */
public operator fun <T> Sequence<T>.get(index: Int): T = elementAt(index)
