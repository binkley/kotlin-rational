package hm.binkley.math

/** Adds [addend] to this value. */
public operator fun <T : BigRationalBase<T>> T.plus(addend: BFloating): T =
    this + companion.valueOf(addend)

/** Adds [addend] to this value. */
public operator fun <T : BigRationalBase<T>> BFloating.plus(addend: T): T =
    addend.companion.valueOf(this) + addend

/** Adds [addend] to this value. */
public operator fun <T : BigRationalBase<T>> T.plus(addend: Double): T =
    this + companion.valueOf(addend)

/** Adds [addend] to this value. */
public operator fun <T : BigRationalBase<T>> Double.plus(addend: T): T =
    addend.companion.valueOf(this) + addend

/** Adds [addend] to this value. */
public operator fun <T : BigRationalBase<T>> T.plus(addend: Float): T =
    this + companion.valueOf(addend)

/** Adds [addend] to this value. */
public operator fun <T : BigRationalBase<T>> Float.plus(addend: T): T =
    addend.companion.valueOf(this) + addend

/** Adds [addend] to this value. */
public operator fun <T : BigRationalBase<T>> T.plus(addend: BFixed): T =
    this + companion.valueOf(addend)

/** Adds [addend] to this value. */
public operator fun <T : BigRationalBase<T>> BFixed.plus(addend: T): T =
    addend.companion.valueOf(this) + addend

/** Adds [addend] to this value. */
public operator fun <T : BigRationalBase<T>> T.plus(addend: Long): T =
    this + companion.valueOf(addend)

/** Adds [addend] to this value. */
public operator fun <T : BigRationalBase<T>> Long.plus(addend: T): T =
    addend.companion.valueOf(this) + addend

/** Adds [addend] to this value. */
public operator fun <T : BigRationalBase<T>> T.plus(addend: Int): T =
    this + companion.valueOf(addend)

/** Adds [addend] to this value. */
public operator fun <T : BigRationalBase<T>> Int.plus(addend: T): T =
    addend.companion.valueOf(this) + addend

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BigRationalBase<T>> T.minus(subtrahend: BFloating): T =
    this - companion.valueOf(subtrahend)

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BigRationalBase<T>> BFloating.minus(subtrahend: T): T =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BigRationalBase<T>> T.minus(subtrahend: Double): T =
    this - companion.valueOf(subtrahend)

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BigRationalBase<T>> Double.minus(subtrahend: T): T =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BigRationalBase<T>> T.minus(subtrahend: Float): T =
    this - companion.valueOf(subtrahend)

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BigRationalBase<T>> Float.minus(subtrahend: T): T =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BigRationalBase<T>> T.minus(subtrahend: BFixed): T =
    this - companion.valueOf(subtrahend)

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BigRationalBase<T>> BFixed.minus(subtrahend: T): T =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BigRationalBase<T>> T.minus(subtrahend: Long): T =
    this - companion.valueOf(subtrahend)

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BigRationalBase<T>> Long.minus(subtrahend: T): T =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BigRationalBase<T>> T.minus(subtrahend: Int): T =
    this - companion.valueOf(subtrahend)

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BigRationalBase<T>> Int.minus(subtrahend: T): T =
    subtrahend.companion.valueOf(this) - subtrahend

/** Multiplies this value by [multiplier]. */
public operator fun <T : BigRationalBase<T>> T.times(multiplier: BFloating): T =
    this * companion.valueOf(multiplier)

/** Multiplies this value by [multiplier]. */
public operator fun <T : BigRationalBase<T>> BFloating.times(multiplier: T): T =
    multiplier.companion.valueOf(this) * multiplier

/** Multiplies this value by [multiplier]. */
public operator fun <T : BigRationalBase<T>> T.times(multiplier: Double): T =
    this * companion.valueOf(multiplier)

/** Multiplies this value by [multiplier]. */
public operator fun <T : BigRationalBase<T>> Double.times(multiplier: T): T =
    multiplier.companion.valueOf(this) * multiplier

/** Multiplies this value by [multiplier]. */
public operator fun <T : BigRationalBase<T>> T.times(multiplier: Float): T =
    this * companion.valueOf(multiplier)

/** Multiplies this value by [multiplier]. */
public operator fun <T : BigRationalBase<T>> Float.times(multiplier: T): T =
    multiplier.companion.valueOf(this) * multiplier

/** Multiplies this value by [multiplier]. */
public operator fun <T : BigRationalBase<T>> T.times(multiplier: BFixed): T =
    this * companion.valueOf(multiplier)

/** Multiplies this value by [multiplier]. */
public operator fun <T : BigRationalBase<T>> BFixed.times(multiplier: T): T =
    multiplier.companion.valueOf(this) * multiplier

/** Multiplies this value by [multiplier]. */
public operator fun <T : BigRationalBase<T>> T.times(multiplier: Long): T =
    this * companion.valueOf(multiplier)

/** Multiplies this value by [multiplier]. */
public operator fun <T : BigRationalBase<T>> Long.times(multiplier: T): T =
    multiplier.companion.valueOf(this) * multiplier

/** Multiplies this value by [multiplier]. */
public operator fun <T : BigRationalBase<T>> T.times(multiplier: Int): T =
    this * companion.valueOf(multiplier)

/** Multiplies this value by [multiplier]. */
public operator fun <T : BigRationalBase<T>> Int.times(multiplier: T): T =
    multiplier.companion.valueOf(this) * multiplier

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> T.div(divisor: BFloating): T =
    this / companion.valueOf(divisor)

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> BFloating.div(divisor: T): T =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> T.div(divisor: Double): T =
    this / companion.valueOf(divisor)

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> Double.div(divisor: T): T =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> T.div(divisor: Float): T =
    this / companion.valueOf(divisor)

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> Float.div(divisor: T): T =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> T.div(divisor: BFixed): T =
    this / companion.valueOf(divisor)

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> BFixed.div(divisor: T): T =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> T.div(divisor: Long): T =
    this / companion.valueOf(divisor)

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> Long.div(divisor: T): T =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> T.div(divisor: Int): T =
    this / companion.valueOf(divisor)

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> Int.div(divisor: T): T =
    divisor.companion.valueOf(this) / divisor

/**
 * Finds the remainder of this value divided by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> T.rem(divisor: BFloating): T =
    this % companion.valueOf(divisor)

/**
 * Finds the remainder of this value divided by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
public operator fun <T : BigRationalBase<T>> BFloating.rem(divisor: T): T =
    divisor.companion.valueOf(this) % divisor

/**
 * Finds the remainder of this value divided by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> T.rem(divisor: Double): T =
    this % companion.valueOf(divisor)

/**
 * Finds the remainder of this value divided by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
public operator fun <T : BigRationalBase<T>> Double.rem(divisor: T): T =
    divisor.companion.valueOf(this) % divisor

/**
 * Finds the remainder of this value divided by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> T.rem(divisor: Float): T =
    this % companion.valueOf(divisor)

/**
 * Finds the remainder of this value divided by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
public operator fun <T : BigRationalBase<T>> Float.rem(divisor: T): T =
    divisor.companion.valueOf(this) % divisor

/**
 * Finds the remainder of this value by [divisor] exactly: always 0 (division is
 * exact for rationals).
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> T.rem(divisor: BFixed): T =
    this % companion.valueOf(divisor)

/**
 * Finds the remainder of this value by [divisor] exactly: always 0 (division is
 * exact for rationals).
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
public operator fun <T : BigRationalBase<T>> BFixed.rem(divisor: T): T =
    divisor.companion.valueOf(this) % divisor

/**
 * Finds the remainder of this value by [divisor] exactly: always 0 (division is
 * exact for rationals).
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> T.rem(divisor: Long): T =
    this % companion.valueOf(divisor)

/**
 * Finds the remainder of this value by [divisor] exactly: always 0 (division is
 * exact for rationals).
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
public operator fun <T : BigRationalBase<T>> Long.rem(divisor: T): T =
    divisor.companion.valueOf(this) % divisor

/**
 * Finds the remainder of this value by [divisor] exactly: always 0 (division is
 * exact for rationals).
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BigRationalBase<T>> T.rem(divisor: Int): T =
    this % companion.valueOf(divisor)

/**
 * Finds the remainder of this value by [divisor] exactly: always 0 (division is
 * exact for rationals).
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
public operator fun <T : BigRationalBase<T>> Int.rem(divisor: T): T =
    divisor.companion.valueOf(this) % divisor

// The remainder below are not technically operators, but in actual usage are
// close enough as above and below share testing.  In Perl, below would be
// operators :)

/**
 * Returns the pair of `this / other` (quotient) and `this % other`
 * (remainder) integral division and modulo operations.
 *
 * @see [div]
 */
public fun <T : BigRationalBase<T>> T.divideAndRemainder(divisor: T):
        Pair<T, T> {
    val quotient = (this / divisor).truncate()
    val remainder = this - divisor * quotient

    return quotient to remainder
}

/**
 * Provides a pseudo-operator for exponentiation, raising this value to the
 * power of [exponent].
 */
@Suppress("DANGEROUS_CHARACTERS", "FunctionName", "Unused")
public infix fun <T : BigRationalBase<T>> T.`**`(exponent: Int): T =
    pow(exponent)
