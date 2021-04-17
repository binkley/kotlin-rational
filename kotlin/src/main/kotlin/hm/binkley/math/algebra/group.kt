package hm.binkley.math.algebra

public interface GroupCompanion<T : Group<T>> : MonoidCompanion<T>

public interface Group<T : Group<T>> : Monoid<T> {
    override val companion: GroupCompanion<T>

    public operator fun unaryMinus(): T
    public operator fun minus(subtrahend: T): T = this + -subtrahend
}
