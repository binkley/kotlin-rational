package hm.binkley.math.algebra

interface RingCompanion<T : Ring<T>> : GroupCompanion<T> {
    @Suppress("PropertyName")
    val ONE: T
}

interface Ring<T : Ring<T>> : Group<T> {
    override val companion: RingCompanion<T>

    operator fun times(multiplier: T): T
}
