package hm.binkley.math.algebra

public interface FieldCompanion<T : Field<T>> : RingCompanion<T>

public interface Field<T : Field<T>> : Ring<T> {
    override val companion: FieldCompanion<T>

    // No such thing as `operator unaryDiv`
    public fun unaryDiv(): T

    public operator fun div(divisor: T): T = this * divisor.unaryDiv()
}
