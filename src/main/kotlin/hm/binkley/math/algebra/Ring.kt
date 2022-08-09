package hm.binkley.math.algebra

/** Exposes an abstract `ONE` element as a companion property. */
public interface RingCompanion<T : Ring<T>> : GroupCompanion<T> {
    /** The identity element for multiplication. */
    @Suppress("PropertyName")
    public val ONE: T
}

/**
 * Elementary multiplication:
 * - `times` operator (but not `div` or `rem`)
 * - `ONE` element
 *
 * See https://en.wikipedia.org/wiki/Ring_(mathematics)
 */
public interface Ring<T : Ring<T>> : Group<T> {
    override val companion: RingCompanion<T>

    /** Multiplies this element by [multiplicand]. */
    public operator fun times(multiplicand: T): T
}
