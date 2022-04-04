package hm.binkley.math

import java.util.Objects.hash

private typealias BIter<T> = BigRationalIterator<T>
private typealias BProgression<T> = BigRationalProgression<T>
private typealias BRange<T> = BigRationalRange<T>
private typealias DecIter<T> = DecrementingBigRationalIterator<T>
private typealias IncIter<T> = IncrementingBigRationalIterator<T>

/** Represents a range of big rational values. */
public interface BigRationalRange<T : BRatBase<T>> : Iterable<T>, ClosedRange<T>

private sealed class BigRationalIterator<T : BRatBase<T>>(
    _current: T,
    protected val last: T,
    private val step: T,
) : Iterator<T> {
    init {
        _current.companion.iteratorCheck(_current, last, step)
    }

    /** Some hoop-jumping to make JaCoCo happier */
    protected var current: T = _current
        private set

    override fun next(): T {
        val next = current
        current += step
        return next
    }
}

private class IncrementingBigRationalIterator<T : BRatBase<T>>(
    /** The first element in the progression. */
    first: T,
    /** The last element in the progression. */
    last: T,
    step: T,
) : BIter<T>(first, last, step) {
    init {
        if (first > last) error("Step must advance range to avoid overflow.")
    }

    override fun hasNext() = current <= last
}

private class DecrementingBigRationalIterator<T : BRatBase<T>>(
    /** The first element in the progression. */
    first: T,
    /** The last element in the progression. */
    last: T,
    step: T,
) : BIter<T>(first, last, step) {
    init {
        if (first < last) error("Step must advance range to avoid overflow.")
    }

    override fun hasNext() = current >= last
}

private class BigRationalProgression<T : BRatBase<T>>(
    override val start: T,
    override val endInclusive: T,
    private val step: T,
) : BRange<T> {
    override fun iterator() =
        if (start.companion.ZERO > step) DecIter(start, endInclusive, step)
        else IncIter(start, endInclusive, step)

    override fun equals(other: Any?) = this === other ||
        other is BProgression<*> &&
        start == other.start &&
        endInclusive == other.endInclusive &&
        step == other.step

    override fun hashCode() = hash(javaClass, start, endInclusive, step)

    override fun toString() =
        if (start.companion.ZERO <= step) "$start..$endInclusive step $step"
        else "$start downTo $endInclusive step $step"
}

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> T.rangeTo(
    endInclusive: T,
): BRange<T> = BProgression(this, endInclusive, companion.ONE)

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> T.rangeTo(
    endInclusive: BFloating,
): BRange<T> = this..companion.valueOf(endInclusive)

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> BFloating.rangeTo(
    endInclusive: T,
): BRange<T> = endInclusive.companion.valueOf(this)..endInclusive

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> T.rangeTo(
    endInclusive: Double,
): BRange<T> = this..companion.valueOf(endInclusive)

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> Double.rangeTo(
    endInclusive: T,
): BRange<T> = endInclusive.companion.valueOf(this)..endInclusive

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> T.rangeTo(
    endInclusive: Float,
): BRange<T> = this..companion.valueOf(endInclusive)

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> Float.rangeTo(
    endInclusive: T,
): BRange<T> = endInclusive.companion.valueOf(this)..endInclusive

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> T.rangeTo(
    endInclusive: BFixed,
): BRange<T> = this..companion.valueOf(endInclusive)

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> BFixed.rangeTo(
    endInclusive: T,
): BRange<T> = endInclusive.companion.valueOf(this)..endInclusive

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> T.rangeTo(
    endInclusive: Long,
): BRange<T> = this..companion.valueOf(endInclusive)

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> Long.rangeTo(
    endInclusive: T,
): BRange<T> = endInclusive.companion.valueOf(this)..endInclusive

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> T.rangeTo(
    endInclusive: Int,
): BRange<T> = this..companion.valueOf(endInclusive)

/** Creates a range from this value to [endInclusive]. */
public operator fun <T : BRatBase<T>> Int.rangeTo(
    endInclusive: T,
): BRange<T> = endInclusive.companion.valueOf(this)..endInclusive

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> T.downTo(
    endInclusive: T,
): BRange<T> = BProgression(this, endInclusive, -companion.ONE)

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> T.downTo(
    endInclusive: BFloating,
): BRange<T> = this downTo companion.valueOf(endInclusive)

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> BFloating.downTo(
    endInclusive: T,
): BRange<T> = endInclusive.companion.valueOf(this) downTo endInclusive

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> T.downTo(
    endInclusive: Double,
): BRange<T> = this downTo companion.valueOf(endInclusive)

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> Double.downTo(
    endInclusive: T,
): BRange<T> = endInclusive.companion.valueOf(this) downTo endInclusive

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> T.downTo(
    endInclusive: Float,
): BRange<T> = this downTo companion.valueOf(endInclusive)

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> Float.downTo(
    endInclusive: T,
): BRange<T> = endInclusive.companion.valueOf(this) downTo endInclusive

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> T.downTo(
    endInclusive: BFixed,
): BRange<T> = this downTo companion.valueOf(endInclusive)

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> BFixed.downTo(
    endInclusive: T,
): BRange<T> = endInclusive.companion.valueOf(this) downTo endInclusive

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> T.downTo(
    endInclusive: Long,
): BRange<T> = this downTo companion.valueOf(endInclusive)

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> Long.downTo(
    endInclusive: T,
): BRange<T> = endInclusive.companion.valueOf(this) downTo endInclusive

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> T.downTo(
    endInclusive: Int,
): BRange<T> = this downTo companion.valueOf(endInclusive)

/** Creates a range from this value _down_ to [endInclusive]. */
public infix fun <T : BRatBase<T>> Int.downTo(
    endInclusive: T,
): BRange<T> = endInclusive.companion.valueOf(this) downTo endInclusive

/** Creates a progression that goes over the same range with the given step. */
public infix fun <T : BRatBase<T>> BRange<T>.step(step: T): BRange<T> =
    BProgression(start, endInclusive, step)

/** Creates a progression that goes over the same range with the given step. */
public infix fun <T : BRatBase<T>> BRange<T>.step(step: BFloating): BRange<T> =
    BProgression(start, endInclusive, start.companion.valueOf(step))

/** Creates a progression that goes over the same range with the given step. */
public infix fun <T : BRatBase<T>> BRange<T>.step(step: Double): BRange<T> =
    BProgression(start, endInclusive, start.companion.valueOf(step))

/** Creates a progression that goes over the same range with the given step. */
public infix fun <T : BRatBase<T>> BRange<T>.step(step: Float): BRange<T> =
    BProgression(start, endInclusive, start.companion.valueOf(step))

/** Creates a progression that goes over the same range with the given step. */
public infix fun <T : BRatBase<T>> BRange<T>.step(step: BFixed): BRange<T> =
    BProgression(start, endInclusive, start.companion.valueOf(step))

/** Creates a progression that goes over the same range with the given step. */
public infix fun <T : BRatBase<T>> BRange<T>.step(step: Long): BRange<T> =
    BProgression(start, endInclusive, start.companion.valueOf(step))

/** Creates a progression that goes over the same range with the given step. */
public infix fun <T : BRatBase<T>> BRange<T>.step(step: Int): BRange<T> =
    BProgression(start, endInclusive, start.companion.valueOf(step))
