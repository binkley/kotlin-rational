package hm.binkley.math.algebra

/** Exposes an abstract `ZERO` element as a companion property. */
public interface MonoidCompanion<T : Monoid<T>> {
    /** The identity element for addition. */
    @Suppress("PropertyName")
    public val ZERO: T
}

/**
 * Elementary addition:
 * - `plus` operator (but not `minus`)
 * - `ZERO` element (but not `ONE`)
 *
 * See https://en.wikipedia.org/wiki/Monoid
 */
public interface Monoid<T : Monoid<T>> {
    /** Access to the companion object specialized for type [T]. */
    public val companion: MonoidCompanion<T>

    /** Idempotent unary `+`. */
    @Suppress("UNCHECKED_CAST")
    public operator fun unaryPlus(): T = this as T

    /** Adds [addend] to this element. */
    public operator fun plus(addend: T): T
}
