package hm.binkley.math

import java.util.Objects.hash

interface BigRationalRange<T : BigRationalBase<T>> :
    Iterable<T>, ClosedRange<T>

private sealed class BigRationalIterator<T : BigRationalBase<T>>(
    protected var current: T,
    protected val last: T,
    private val step: T
) : Iterator<T> {
    init {
        current.companion.iteratorCheck(current, last, step)
    }

    override fun next(): T {
        val next = current
        current += step
        return next
    }
}

private class IncrementingBigRationalIterator<T : BigRationalBase<T>>(
    /** The first element in the progression. */
    first: T,
    /** The last element in the progression. */
    last: T,
    step: T
) : BigRationalIterator<T>(first, last, step) {
    init {
        if (first > last)
            error("Step must be advance range to avoid overflow.")
    }

    override fun hasNext() = current <= last
}

private class DecrementingBigRationalIterator<T : BigRationalBase<T>>(
    /** The first element in the progression. */
    first: T,
    /** The last element in the progression. */
    last: T,
    step: T
) : BigRationalIterator<T>(first, last, step) {
    init {
        if (first < last)
            error("Step must be advance range to avoid overflow.")
    }

    override fun hasNext() = current >= last
}

private class BigRationalProgression<T : BigRationalBase<T>>(
    override val start: T,
    override val endInclusive: T,
    private val step: T
) : BigRationalRange<T> {
    override fun iterator() =
        if (step < start.companion.ZERO)
            DecrementingBigRationalIterator(start, endInclusive, step)
        else
            IncrementingBigRationalIterator(start, endInclusive, step)

    override fun equals(other: Any?) = this === other ||
        other is BigRationalProgression<*> &&
        javaClass == other.javaClass &&
        start == other.start &&
        endInclusive == other.endInclusive &&
        step == other.step

    override fun hashCode() = hash(javaClass, start, endInclusive, step)

    override fun toString() =
        if (step < start.companion.ZERO) "$start downTo $endInclusive step $step"
        else "$start..$endInclusive step $step"
}

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> T.rangeTo(endInclusive: T): BigRationalRange<T> =
    BigRationalProgression(this, endInclusive, companion.ONE)

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> T.rangeTo(endInclusive: BDouble): BigRationalRange<T> =
    this..companion.valueOf(endInclusive)

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> BDouble.rangeTo(endInclusive: T): BigRationalRange<T> =
    endInclusive.companion.valueOf(this)..endInclusive

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> T.rangeTo(endInclusive: Double): BigRationalRange<T> =
    this..companion.valueOf(endInclusive)

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> Double.rangeTo(endInclusive: T): BigRationalRange<T> =
    endInclusive.companion.valueOf(this)..endInclusive

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> T.rangeTo(endInclusive: Float): BigRationalRange<T> =
    this..companion.valueOf(endInclusive)

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> Float.rangeTo(endInclusive: T): BigRationalRange<T> =
    endInclusive.companion.valueOf(this)..endInclusive

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> T.rangeTo(endInclusive: BInt): BigRationalRange<T> =
    this..companion.valueOf(endInclusive)

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> BInt.rangeTo(endInclusive: T) =
    endInclusive.companion.valueOf(this)..endInclusive

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> T.rangeTo(endInclusive: Long): BigRationalRange<T> =
    this..companion.valueOf(endInclusive)

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> Long.rangeTo(endInclusive: T): BigRationalRange<T> =
    endInclusive.companion.valueOf(this)..endInclusive

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> T.rangeTo(endInclusive: Int): BigRationalRange<T> =
    this..companion.valueOf(endInclusive)

/** Creates a range from this value to [endInclusive]. */
operator fun <T : BigRationalBase<T>> Int.rangeTo(endInclusive: T): BigRationalRange<T> =
    endInclusive.companion.valueOf(this)..endInclusive

/** Creates a range from this value _down_ to [endInclusive]. */
infix fun <T : BigRationalBase<T>> T.downTo(endInclusive: T): BigRationalRange<T> =
    BigRationalProgression(this, endInclusive, -companion.ONE)

/** Creates a range from this value _down_ to [endInclusive]. */
infix fun <T : BigRationalBase<T>> BInt.downTo(endInclusive: T): BigRationalRange<T> =
    endInclusive.companion.valueOf(this) downTo endInclusive

/** Creates a range from this value _down_ to [endInclusive]. */
infix fun <T : BigRationalBase<T>> Long.downTo(endInclusive: T): BigRationalRange<T> =
    endInclusive.companion.valueOf(this) downTo endInclusive

/** Creates a range from this value _down_ to [endInclusive]. */
infix fun <T : BigRationalBase<T>> Int.downTo(endInclusive: T): BigRationalRange<T> =
    endInclusive.companion.valueOf(this) downTo endInclusive

infix fun <T : BigRationalBase<T>> BigRationalRange<T>.step(step: T): BigRationalRange<T> =
    BigRationalProgression(start, endInclusive, step)

infix fun <T : BigRationalBase<T>> BigRationalRange<T>.step(
    step: BInt
): BigRationalRange<T> =
    BigRationalProgression(start, endInclusive, start.companion.valueOf(step))

infix fun <T : BigRationalBase<T>> BigRationalRange<T>.step(
    step: Long
): BigRationalRange<T> =
    BigRationalProgression(start, endInclusive, start.companion.valueOf(step))

infix fun <T : BigRationalBase<T>> BigRationalRange<T>.step(step: Int): BigRationalRange<T> =
    BigRationalProgression(start, endInclusive, start.companion.valueOf(step))
