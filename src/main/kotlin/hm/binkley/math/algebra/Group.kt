package hm.binkley.math.algebra

/** Base type for companion objects of [Group] types. */
public interface GroupCompanion<T : Group<T>> : MonoidCompanion<T>

/**
 * Elementary subtraction:
 * - `minus` operator
 *
 * See https://en.wikipedia.org/wiki/Group_(mathematics)
 */
public interface Group<T : Group<T>> : Monoid<T> {
    override val companion: GroupCompanion<T>

    /** Returns the additive inverse. */
    public operator fun unaryMinus(): T

    /** Subtracts [subtrahend] from this element. */
    public operator fun minus(subtrahend: T): T = this + -subtrahend
}
