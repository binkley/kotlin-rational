package hm.binkley.math.algebra

public interface MonoidCompanion<T : Monoid<T>> {
    @Suppress("PropertyName")
    public val ZERO: T
}

public interface Monoid<T : Monoid<T>> {
    public val companion: MonoidCompanion<T>

    @Suppress("UNCHECKED_CAST")
    public operator fun unaryPlus(): T = this as T
    public operator fun plus(addend: T): T
}
