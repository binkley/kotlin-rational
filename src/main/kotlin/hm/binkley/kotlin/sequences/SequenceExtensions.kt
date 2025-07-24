package hm.binkley.kotlin.sequences

/**
 * Provides an index operator for [Sequence] to improve developer UX.
 * This allows using square bracket notation for indexing elements in a
 * sequence.
 * The stdlib does not provide this.
 *
 * Example:
 * ```
 * val element = mySequence[index]
 * ```
 *
 * For this library using sequences for series of rational numbers assumes a
 * non-negative index, and throws `IndexOutOfBoundsException` for non-negative
 * indices.
 * An underlying sequence indexing is an implicit function call to generate a
 * next element of a series represented as a `Sequence`.
 *
 * **Inlined**: use of bracket notation is "syntactic sugar" for `elementAt`,
 * so call sites are replaced with the function call for sequences.
 * The `@Suppress` annotation makes the compiler happy, but you may have issues
 * with mutation testing complaining that the code is not covered.
 *
 * @param index the index of the element to return
 * @return the element at the specified position in this sequence
 * @throws IndexOutOfBoundsException if the index is out of bounds
 */
@Suppress("NOTHING_TO_INLINE")
public inline operator fun <T> Sequence<T>.get(index: Int): T = elementAt(index)
