package hm.binkley.math.algebra

public interface RingCompanion<T : Ring<T>> : GroupCompanion<T> {
    @Suppress("PropertyName")
    public val ONE: T
}

public interface Ring<T : Ring<T>> : Group<T> {
    override val companion: RingCompanion<T>

    public operator fun times(factor: T): T
}
