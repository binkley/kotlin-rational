package hm.binkley.math.algebra

public interface GroupCompanion<T : Group<T>> {
    @Suppress("PropertyName")
    public val ZERO: T
}

public interface Group<T : Group<T>> {
    public val companion: GroupCompanion<T>

    @Suppress("UNCHECKED_CAST")
    public operator fun unaryPlus(): T = this as T
    public operator fun unaryMinus(): T
    public operator fun plus(addend: T): T
    public operator fun minus(subtrahend: T): T = this + -subtrahend
}
