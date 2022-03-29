package hm.binkley.math

/**
 * Checks if this is an equivalent rational fraction to [other].
 * This is distinct from [equals]: `equivalent` is mathematically equal
 * regardless of representation; `equals` is programming equal including
 * object type.
 */
public fun <T : BRatBase<T>, U : BRatBase<U>> T.equivalent(
    other: U,
): Boolean = 0 == this.compareTo(other)

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>, U : BRatBase<U>> T.compareTo(
    other: U
): Int =
    (numerator * other.denominator).compareTo(other.numerator * denominator)

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>> T.compareTo(other: BFloating): Int =
    compareTo(companion.valueOf(other))

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>> BFloating.compareTo(other: T): Int =
    other.companion.valueOf(this).compareTo(other)

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>> T.compareTo(other: Double): Int =
    compareTo(companion.valueOf(other))

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>> Double.compareTo(other: T): Int =
    other.companion.valueOf(this).compareTo(other)

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>> T.compareTo(other: Float): Int =
    compareTo(companion.valueOf(other))

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>> Float.compareTo(other: T): Int =
    other.companion.valueOf(this).compareTo(other)

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>> T.compareTo(other: BFixed): Int =
    compareTo(companion.valueOf(other))

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>> BFixed.compareTo(other: T): Int =
    other.companion.valueOf(this).compareTo(other)

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>> T.compareTo(other: Long): Int =
    compareTo(companion.valueOf(other))

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>> Long.compareTo(other: T): Int =
    other.companion.valueOf(this).compareTo(other)

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>> T.compareTo(other: Int): Int =
    compareTo(companion.valueOf(other))

/** Compares this value to [other]. */
public operator fun <T : BRatBase<T>> Int.compareTo(other: T): Int =
    other.companion.valueOf(this).compareTo(other)
