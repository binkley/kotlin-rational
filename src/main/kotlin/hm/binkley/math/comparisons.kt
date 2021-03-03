package hm.binkley.math

/** Provides the `numerator` and `denominator` as a pair. */
public fun <T : BigRationalBase<T>> T.toPair(): Pair<BInt, BInt> =
    numerator to denominator

/** Checks if this is an equivalent rational fraction to [other]. */
public fun <T : BigRationalBase<T>, U : BigRationalBase<U>> T.equivalent(
    other: U,
):
    Boolean = 0 == this.compareTo(other)

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>, U : BigRationalBase<U>> T.compareTo(
    other: U,
): Int =
    (numerator * other.denominator).compareTo(other.numerator * denominator)

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>> T.compareTo(other: BDouble): Int =
    compareTo(companion.valueOf(other))

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>> BDouble.compareTo(other: T): Int =
    other.companion.valueOf(this).compareTo(other)

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>> T.compareTo(other: Double): Int =
    compareTo(companion.valueOf(other))

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>> Double.compareTo(other: T): Int =
    other.companion.valueOf(this).compareTo(other)

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>> T.compareTo(other: Float): Int =
    compareTo(companion.valueOf(other))

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>> Float.compareTo(other: T): Int =
    other.companion.valueOf(this).compareTo(other)

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>> T.compareTo(other: BInt): Int =
    compareTo(companion.valueOf(other))

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>> BInt.compareTo(other: T): Int =
    other.companion.valueOf(this).compareTo(other)

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>> T.compareTo(other: Long): Int =
    compareTo(companion.valueOf(other))

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>> Long.compareTo(other: T): Int =
    other.companion.valueOf(this).compareTo(other)

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>> T.compareTo(other: Int): Int =
    compareTo(companion.valueOf(other))

/** Compares this value to [other]. */
public operator fun <T : BigRationalBase<T>> Int.compareTo(other: T): Int =
    other.companion.valueOf(this).compareTo(other)
