package hm.binkley.math.algebra

interface GroupCompanion<T : Group<T>> {
    @Suppress("PropertyName")
    val ZERO: T
}

interface Group<T : Group<T>> {
    val companion: GroupCompanion<T>

    @Suppress("UNCHECKED_CAST")
    operator fun unaryPlus(): T = this as T
    operator fun unaryMinus(): T
    operator fun plus(addend: T): T
    operator fun minus(subtrahend: T): T = this + -subtrahend
}
