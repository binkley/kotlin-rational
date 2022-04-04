package hm.binkley.math.algebra

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
    /** Access to the companion object specialized for type [T]. */
    override val companion: FieldCompanion<T>

    // No such thing as `operator unaryDiv`
    public fun unaryDiv(): T

    public operator fun div(divisor: T): T = this * divisor.unaryDiv()
}
