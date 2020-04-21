package hm.binkley.math

import java.util.Objects.hash

sealed class BigRationalIterator<T : BigRationalBase<T>>(
    first: T,
    protected val last: T,
    private val step: T
) : Iterator<T> {
    init {
        first.companion.iteratorCheck(first, last, step)
    }

    protected var current = first

    override fun next(): T {
        val next = current
        current += step
        return next
    }
}

class IncrementingBigRationalIterator<T : BigRationalBase<T>>(
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

class DecrementingBigRationalIterator<T : BigRationalBase<T>>(
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

open class BigRationalProgression<T : BigRationalBase<T>>(
    override val start: T,
    override val endInclusive: T,
    private val step: T
) : Iterable<T>, ClosedRange<T> {
    override fun iterator() =
        if (step < start.companion.ZERO)
            DecrementingBigRationalIterator(start, endInclusive, step)
        else
            IncrementingBigRationalIterator(start, endInclusive, step)

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is BigRationalProgression<*> -> false
        else -> start == other.start &&
                endInclusive == other.endInclusive &&
                step == other.step
    }

    override fun hashCode() = hash(start, endInclusive, step)

    override fun toString() =
        if (step < start.companion.ZERO) "$start downTo $endInclusive step $step"
        else "$start..$endInclusive step $step"
}

infix fun <T : BigRationalBase<T>> BigRationalProgression<T>.step(step: T) =
    BigRationalProgression(
        start,
        endInclusive,
        step
    )

infix fun <T : BigRationalBase<T>> BigRationalProgression<T>.step(step: BInt) =
    BigRationalProgression(start, endInclusive, start.companion.valueOf(step))

infix fun <T : BigRationalBase<T>> BigRationalProgression<T>.step(step: Long) =
    BigRationalProgression(start, endInclusive, start.companion.valueOf(step))

infix fun <T : BigRationalBase<T>> BigRationalProgression<T>.step(step: Int) =
    BigRationalProgression(start, endInclusive, start.companion.valueOf(step))
