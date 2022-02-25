package hm.binkley.math

/** Adds [addend] to this value. */
public operator fun <T : BRatBase<T>> T.plus(addend: BFloating): T =
    this + companion.valueOf(addend)

/** Adds [addend] to this value. */
public operator fun <T : BRatBase<T>> BFloating.plus(addend: T): T =
    addend.companion.valueOf(this) + addend

/** Adds [addend] to this value. */
public operator fun <T : BRatBase<T>> T.plus(addend: Double): T =
    this + companion.valueOf(addend)

/** Adds [addend] to this value. */
public operator fun <T : BRatBase<T>> Double.plus(addend: T): T =
    addend.companion.valueOf(this) + addend

/** Adds [addend] to this value. */
public operator fun <T : BRatBase<T>> T.plus(addend: Float): T =
    this + companion.valueOf(addend)

/** Adds [addend] to this value. */
public operator fun <T : BRatBase<T>> Float.plus(addend: T): T =
    addend.companion.valueOf(this) + addend

/** Adds [addend] to this value. */
public operator fun <T : BRatBase<T>> T.plus(addend: BFixed): T =
    this + companion.valueOf(addend)

/** Adds [addend] to this value. */
public operator fun <T : BRatBase<T>> BFixed.plus(addend: T): T =
    addend.companion.valueOf(this) + addend

/** Adds [addend] to this value. */
public operator fun <T : BRatBase<T>> T.plus(addend: Long): T =
    this + companion.valueOf(addend)

/** Adds [addend] to this value. */
public operator fun <T : BRatBase<T>> Long.plus(addend: T): T =
    addend.companion.valueOf(this) + addend

/** Adds [addend] to this value. */
public operator fun <T : BRatBase<T>> T.plus(addend: Int): T =
    this + companion.valueOf(addend)

/** Adds [addend] to this value. */
public operator fun <T : BRatBase<T>> Int.plus(addend: T): T =
    addend.companion.valueOf(this) + addend

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BRatBase<T>> T.minus(subtrahend: BFloating): T =
    this - companion.valueOf(subtrahend)

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BRatBase<T>> BFloating.minus(subtrahend: T): T =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BRatBase<T>> T.minus(subtrahend: Double): T =
    this - companion.valueOf(subtrahend)

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BRatBase<T>> Double.minus(subtrahend: T): T =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BRatBase<T>> T.minus(subtrahend: Float): T =
    this - companion.valueOf(subtrahend)

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BRatBase<T>> Float.minus(subtrahend: T): T =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BRatBase<T>> T.minus(subtrahend: BFixed): T =
    this - companion.valueOf(subtrahend)

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BRatBase<T>> BFixed.minus(subtrahend: T): T =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BRatBase<T>> T.minus(subtrahend: Long): T =
    this - companion.valueOf(subtrahend)

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BRatBase<T>> Long.minus(subtrahend: T): T =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BRatBase<T>> T.minus(subtrahend: Int): T =
    this - companion.valueOf(subtrahend)

/** Subtracts [subtrahend] from this value. */
public operator fun <T : BRatBase<T>> Int.minus(subtrahend: T): T =
    subtrahend.companion.valueOf(this) - subtrahend

/** Multiplies this value by [multiplier]. */
public operator fun <T : BRatBase<T>> T.times(multiplier: BFloating): T =
    this * companion.valueOf(multiplier)

/** Multiplies this value by [multiplier]. */
public operator fun <T : BRatBase<T>> BFloating.times(multiplier: T): T =
    multiplier.companion.valueOf(this) * multiplier

/** Multiplies this value by [multiplier]. */
public operator fun <T : BRatBase<T>> T.times(multiplier: Double): T =
    this * companion.valueOf(multiplier)

/** Multiplies this value by [multiplier]. */
public operator fun <T : BRatBase<T>> Double.times(multiplier: T): T =
    multiplier.companion.valueOf(this) * multiplier

/** Multiplies this value by [multiplier]. */
public operator fun <T : BRatBase<T>> T.times(multiplier: Float): T =
    this * companion.valueOf(multiplier)

/** Multiplies this value by [multiplier]. */
public operator fun <T : BRatBase<T>> Float.times(multiplier: T): T =
    multiplier.companion.valueOf(this) * multiplier

/** Multiplies this value by [multiplier]. */
public operator fun <T : BRatBase<T>> T.times(multiplier: BFixed): T =
    this * companion.valueOf(multiplier)

/** Multiplies this value by [multiplier]. */
public operator fun <T : BRatBase<T>> BFixed.times(multiplier: T): T =
    multiplier.companion.valueOf(this) * multiplier

/** Multiplies this value by [multiplier]. */
public operator fun <T : BRatBase<T>> T.times(multiplier: Long): T =
    this * companion.valueOf(multiplier)

/** Multiplies this value by [multiplier]. */
public operator fun <T : BRatBase<T>> Long.times(multiplier: T): T =
    multiplier.companion.valueOf(this) * multiplier

/** Multiplies this value by [multiplier]. */
public operator fun <T : BRatBase<T>> T.times(multiplier: Int): T =
    this * companion.valueOf(multiplier)

/** Multiplies this value by [multiplier]. */
public operator fun <T : BRatBase<T>> Int.times(multiplier: T): T =
    multiplier.companion.valueOf(this) * multiplier

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> T.div(divisor: BFloating): T =
    this / companion.valueOf(divisor)

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> BFloating.div(divisor: T): T =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> T.div(divisor: Double): T =
    this / companion.valueOf(divisor)

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> Double.div(divisor: T): T =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> T.div(divisor: Float): T =
    this / companion.valueOf(divisor)

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> Float.div(divisor: T): T =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> T.div(divisor: BFixed): T =
    this / companion.valueOf(divisor)

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> BFixed.div(divisor: T): T =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> T.div(divisor: Long): T =
    this / companion.valueOf(divisor)

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> Long.div(divisor: T): T =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> T.div(divisor: Int): T =
    this / companion.valueOf(divisor)

/**
 * Divides this value by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> Int.div(divisor: T): T =
    divisor.companion.valueOf(this) / divisor

/**
 * Finds the remainder of this value divided by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> T.rem(divisor: BFloating): T =
    this % companion.valueOf(divisor)

/**
 * Finds the remainder of this value divided by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
public operator fun <T : BRatBase<T>> BFloating.rem(divisor: T): T =
    divisor.companion.valueOf(this) % divisor

/**
 * Finds the remainder of this value divided by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> T.rem(divisor: Double): T =
    this % companion.valueOf(divisor)

/**
 * Finds the remainder of this value divided by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> Double.rem(divisor: T): T =
    divisor.companion.valueOf(this) % divisor

/**
 * Finds the remainder of this value divided by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> T.rem(divisor: Float): T =
    this % companion.valueOf(divisor)

/**
 * Finds the remainder of this value divided by [divisor] exactly.
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> Float.rem(divisor: T): T =
    divisor.companion.valueOf(this) % divisor

/**
 * Finds the remainder of this value by [divisor] exactly: always 0 (division is
 * exact for rationals).
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> T.rem(divisor: BFixed): T =
    this % companion.valueOf(divisor)

/**
 * Finds the remainder of this value by [divisor] exactly: always 0 (division is
 * exact for rationals).
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> BFixed.rem(divisor: T): T =
    divisor.companion.valueOf(this) % divisor

/**
 * Finds the remainder of this value by [divisor] exactly: always 0 (division is
 * exact for rationals).
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> T.rem(divisor: Long): T =
    this % companion.valueOf(divisor)

/**
 * Finds the remainder of this value by [divisor] exactly: always 0 (division is
 * exact for rationals).
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> Long.rem(divisor: T): T =
    divisor.companion.valueOf(this) % divisor

/**
 * Finds the remainder of this value by [divisor] exactly: always 0 (division is
 * exact for rationals).
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> T.rem(divisor: Int): T =
    this % companion.valueOf(divisor)

/**
 * Finds the remainder of this value by [divisor] exactly: always 0 (division is
 * exact for rationals).
 *
 * @see [divideAndRemainder]
 */
public operator fun <T : BRatBase<T>> Int.rem(divisor: T): T =
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
public fun <T : BRatBase<T>> T.divideAndRemainder(divisor: T): Pair<T, T> {
    val quotient = (this / divisor).truncate()
    val remainder = this - divisor * quotient

    return quotient to remainder
}

/**
 * Provides a pseudo-operator for exponentiation, raising this value to the
 * power of [exponent].
 */
@Suppress("FunctionName")
public infix fun <T : BRatBase<T>> T.`^`(exponent: Int): T = pow(exponent)

@Suppress("FunctionName")
public infix fun BFixed.`^`(exponent: Int): BFixed = pow(exponent)
