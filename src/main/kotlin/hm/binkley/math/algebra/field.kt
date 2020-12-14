package hm.binkley.math.algebra

interface FieldCompanion<T : Field<T>> : RingCompanion<T>

/** @todo Does modulo operation fit in here? */
interface Field<T : Field<T>> : Ring<T> {
    override val companion: FieldCompanion<T>

    // No such thing as `operator unaryDiv`
    fun unaryDiv(): T

    operator fun div(divisor: T): T = this * divisor.unaryDiv()
}
