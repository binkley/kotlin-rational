package hm.binkley.math.algebra

/** Base type for companion objects of [Field] types. */
public interface FieldCompanion<T : Field<T>> : RingCompanion<T>

/**
 * Elementary division:
 * - `div` operator (and `unaryDiv` pseudo-operator)
 *
 * See https://en.wikipedia.org/wiki/Field_(mathematics)
 *
 * @todo Should there also be an abstract `rem` operator?
 */
public interface Field<T : Field<T>> : Ring<T> {
    override val companion: FieldCompanion<T>

    /**
     * Returns the multiplicative inverse.
     * - [inv] is a sensible alternative name, however has a bitwise meaning
     *   for primitives
     */
    public fun unaryDiv(): T

    /** Divides this element by [divisor]. */
    public operator fun div(divisor: T): T = this * divisor.unaryDiv()
}
