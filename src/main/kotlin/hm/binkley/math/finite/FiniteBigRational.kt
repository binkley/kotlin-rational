package hm.binkley.math.finite

import hm.binkley.math.BDouble
import hm.binkley.math.BInt
import hm.binkley.math.BigRationalBase
import hm.binkley.math.BigRationalCompanion
import hm.binkley.math.BigRationalProgression
import hm.binkley.math.div
import hm.binkley.math.divideAndRemainder
import hm.binkley.math.exponent
import hm.binkley.math.finite.FiniteBigRational.Companion.ONE
import hm.binkley.math.finite.FiniteBigRational.Companion.TEN
import hm.binkley.math.finite.FiniteBigRational.Companion.TWO
import hm.binkley.math.finite.FiniteBigRational.Companion.ZERO
import hm.binkley.math.finite.FiniteBigRational.Companion.valueOf
import hm.binkley.math.isInteger
import hm.binkley.math.isZero
import hm.binkley.math.lcm
import hm.binkley.math.mantissa
import hm.binkley.math.minus
import hm.binkley.math.plus
import hm.binkley.math.pow
import hm.binkley.math.times
import hm.binkley.math.unaryMinus

/**
 * Immutable arbitrary-precision rationals (finite fractions).
 * `FiniteBigRational` provides analogues to all of Kotlin's [Long] operators
 * where appropriate. Additionally, `FiniteBigRational` provides operations
 * for GCD and LCM calculation.
 *
 * Comparison operations perform signed comparisons, analogous to those
 * performed by Kotlin's relational and equality operators.
 *
 * Division by [ZERO] (or implied) throws [ArithmeticException] in all cases.
 *
 * Ranges increment by 1 unless otherwise specified.
 *
 * @todo Consider `Short` and `Byte` overloads
 * @todo Assign properties at construction; avoid circular ctors
 */
class FiniteBigRational private constructor(
    numerator: BInt,
    denominator: BInt
) : BigRationalBase<FiniteBigRational>(
    numerator,
    denominator,
    FiniteBigRational
) {
    companion object : BigRationalCompanion<FiniteBigRational> {
        override val ZERO = FiniteBigRational(BInt.ZERO, BInt.ONE)
        override val ONE = FiniteBigRational(BInt.ONE, BInt.ONE)
        override val TWO = FiniteBigRational(BInt.TWO, BInt.ONE)
        override val TEN = FiniteBigRational(BInt.TEN, BInt.ONE)

        /**
         * Returns a `FiniteBigRational` whose value is equal to that of the
         * specified ratio, `numerator / denominator`.
         *
         * This factory method is in preference to an explicit constructor
         * allowing for reuse of frequently used FiniteBigRationals.  In
         * particular:
         *
         * * ZERO
         * * ONE
         * * TWO
         * * TEN
         */
        override fun valueOf(
            numerator: BInt,
            denominator: BInt
        ): FiniteBigRational {
            if (denominator.isZero())
                throw ArithmeticException("division by zero")

            return construct(numerator, denominator) { n, d ->
                FiniteBigRational(n, d)
            }
        }

        override fun iteratorCheck(
            first: FiniteBigRational,
            last: FiniteBigRational,
            step: FiniteBigRational
        ) {
            if (step == ZERO) error("Step must be non-zero.")
        }
    }
}

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: BDouble) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: Double) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: Float) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: BInt) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: Long) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: Int) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: BDouble) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: BInt) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: Long) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: Int) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: Double) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: Float) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: BDouble) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: BInt) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: Long) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: Int) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: Double) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: Float) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: BDouble) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: Double) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: Float) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: BInt) = valueOf(this, denominator)

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: Long) =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: Int) =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: Double) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: Float) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: BDouble) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: BInt) = valueOf(toBigInteger(), denominator)

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: Long) =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: Int) =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: BDouble) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: Double) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: Float) =
    toFiniteBigRational() / denominator.toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: BInt) = valueOf(toBigInteger(), denominator)

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: Long) =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: Int) =
    valueOf(toBigInteger(), denominator.toBigInteger())

/** Returns the value of this number as a `FiniteBigRational`. */
fun BDouble.toFiniteBigRational() = convert(this)

/** Returns the value of this number as a `FiniteBigRational`. */
fun Double.toFiniteBigRational() = convert(this)

/** Returns the value of this number as a `FiniteBigRational`. */
fun Float.toFiniteBigRational() = toDouble().toFiniteBigRational()

/** Returns the value of this number as a `FiniteBigRational`. */
fun BInt.toFiniteBigRational() = valueOf(this)

/** Returns the value of this number as a `FiniteBigRational`. */
fun Long.toFiniteBigRational() = valueOf(this)

/** Returns the value of this number as a `FiniteBigRational`. */
fun Int.toFiniteBigRational() = valueOf(this)

/**
 * Compares this value to the other.
 *
 * @see [FiniteBigRational.compareTo]
 */
operator fun FiniteBigRational.compareTo(other: BDouble) =
    this.compareTo(other.toFiniteBigRational())

/**
 * Compares this value to the other.
 *
 * @see [FiniteBigRational.compareTo]
 */
operator fun BDouble.compareTo(other: FiniteBigRational) =
    this.toFiniteBigRational().compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [FiniteBigRational.compareTo]
 */
operator fun FiniteBigRational.compareTo(other: Double) =
    this.compareTo(other.toFiniteBigRational())

/**
 * Compares this value to the other.
 *
 * @see [FiniteBigRational.compareTo]
 */
operator fun Double.compareTo(other: FiniteBigRational) =
    this.toFiniteBigRational().compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [FiniteBigRational.compareTo]
 */
operator fun FiniteBigRational.compareTo(other: Float) =
    this.compareTo(other.toFiniteBigRational())

/**
 * Compares this value to the other.
 *
 * @see [FiniteBigRational.compareTo]
 */
operator fun Float.compareTo(other: FiniteBigRational) =
    this.toFiniteBigRational().compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [FiniteBigRational.compareTo]
 */
operator fun FiniteBigRational.compareTo(other: BInt) =
    this.compareTo(other.toFiniteBigRational())

/**
 * Compares this value to the other.
 *
 * @see [FiniteBigRational.compareTo]
 */
operator fun BInt.compareTo(other: FiniteBigRational) =
    this.toFiniteBigRational().compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [FiniteBigRational.compareTo]
 */
operator fun FiniteBigRational.compareTo(other: Long) =
    this.compareTo(other.toFiniteBigRational())

/**
 * Compares this value to the other.
 *
 * @see [FiniteBigRational.compareTo]
 */
operator fun Long.compareTo(other: FiniteBigRational) =
    this.toFiniteBigRational().compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [FiniteBigRational.compareTo]
 */
operator fun FiniteBigRational.compareTo(other: Int) =
    this.compareTo(other.toFiniteBigRational())

/**
 * Compares this value to the other.
 *
 * @see [FiniteBigRational.compareTo]
 */
operator fun Int.compareTo(other: FiniteBigRational) =
    this.toFiniteBigRational().compareTo(other)

/** Adds the other value to this value. */
operator fun FiniteBigRational.plus(addend: BDouble) =
    this + addend.toFiniteBigRational()

/** Adds the other value to this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.plus(addend: Double) =
    this + addend.toFiniteBigRational()

/** Adds the other value to this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.plus(addend: Float) =
    this + addend.toFiniteBigRational()

/**
 * Subtracts the other value from this value yielding a `FiniteBigRational`.
 */
operator fun FiniteBigRational.minus(subtrahend: BDouble) =
    this - subtrahend.toFiniteBigRational()

/**
 * Subtracts the other value from this value yielding a `FiniteBigRational`.
 */
operator fun FiniteBigRational.minus(subtrahend: Double) =
    this - subtrahend.toFiniteBigRational()

/**
 * Subtracts the other value from this value yielding a `FiniteBigRational`.
 */
operator fun FiniteBigRational.minus(subtrahend: Float) =
    this - subtrahend.toFiniteBigRational()

/**
 * Multiplies this value by the other value yielding a `FiniteBigRational`.
 */
operator fun FiniteBigRational.times(multiplicand: BDouble) =
    this * multiplicand.toFiniteBigRational()

/**
 * Multiplies this value by the other value yielding a `FiniteBigRational`.
 */
operator fun FiniteBigRational.times(multiplicand: Double) =
    this * multiplicand.toFiniteBigRational()

/**
 * Multiplies this value by the other value yielding a `FiniteBigRational`.
 */
operator fun FiniteBigRational.times(multiplicand: Float) =
    this * multiplicand.toFiniteBigRational()

/**
 * Divides this value by the other value exactly yielding a FiniteBigRational.
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.div(divisor: BDouble) =
    this / divisor.toFiniteBigRational()

/**
 * Divides this value by the other value exactly yielding a FiniteBigRational.
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.div(divisor: Double) =
    this / divisor.toFiniteBigRational()

/**
 * Divides this value by the other value exactly yielding a FiniteBigRational.
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.div(divisor: Float) =
    this / divisor.toFiniteBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or throws [ArithmeticException] when [divisor] is [ZERO].
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
operator fun FiniteBigRational.rem(divisor: FiniteBigRational) = ZERO

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or throws [ArithmeticException] when [divisor] is [ZERO].
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.rem(divisor: BDouble) =
    this % divisor.toFiniteBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or throws [ArithmeticException] when [divisor] is [ZERO].
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.rem(divisor: Double) =
    this % divisor.toFiniteBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or throws [ArithmeticException] when [divisor] is [ZERO].
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.rem(divisor: Float) =
    this % divisor.toFiniteBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or throws [ArithmeticException] when [divisor] is [ZERO].
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.rem(divisor: BInt) =
    this % divisor.toFiniteBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or throws [ArithmeticException] when [divisor] is [ZERO].
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.rem(divisor: Long) =
    this % divisor.toFiniteBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or throws [ArithmeticException] when [divisor] is [ZERO].
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.rem(divisor: Int) =
    this % divisor.toFiniteBigRational()

/** Creates a range from this value to the specified [other] value. */
operator fun FiniteBigRational.rangeTo(other: FiniteBigRational) =
    BigRationalProgression(this, other, ONE)

operator fun FiniteBigRational.rangeTo(other: BDouble) =
    rangeTo(other.toFiniteBigRational())

/** Creates a range from this value to the specified [other] value. */
operator fun FiniteBigRational.rangeTo(other: Double) =
    rangeTo(other.toFiniteBigRational())

/** Creates a range from this value to the specified [other] value. */
operator fun FiniteBigRational.rangeTo(other: Float) =
    rangeTo(other.toFiniteBigRational())

/** Creates a range from this value to the specified [other] value. */
operator fun FiniteBigRational.rangeTo(other: BInt) =
    rangeTo(other.toFiniteBigRational())

/** Creates a range from this value to the specified [other] value. */
operator fun FiniteBigRational.rangeTo(other: Long) =
    rangeTo(other.toFiniteBigRational())

/** Creates a range from this value to the specified [other] value. */
operator fun FiniteBigRational.rangeTo(other: Int) =
    rangeTo(other.toFiniteBigRational())

infix fun FiniteBigRational.downTo(other: FiniteBigRational) =
    BigRationalProgression(this, other, -ONE)

operator fun BDouble.plus(other: FiniteBigRational) =
    toFiniteBigRational() + other

operator fun Double.plus(other: FiniteBigRational) =
    toFiniteBigRational() + other

operator fun Float.plus(other: FiniteBigRational) =
    toFiniteBigRational() + other

operator fun BInt.plus(other: FiniteBigRational) =
    toFiniteBigRational() + other

operator fun Long.plus(other: FiniteBigRational) =
    toFiniteBigRational() + other

operator fun Int.plus(other: FiniteBigRational) =
    toFiniteBigRational() + other

operator fun BDouble.minus(other: FiniteBigRational) =
    toFiniteBigRational() - other

operator fun Double.minus(other: FiniteBigRational) =
    toFiniteBigRational() - other

operator fun Float.minus(other: FiniteBigRational) =
    toFiniteBigRational() - other

operator fun BInt.minus(other: FiniteBigRational) =
    toFiniteBigRational() - other

operator fun Long.minus(other: FiniteBigRational) =
    toFiniteBigRational() - other

operator fun Int.minus(other: FiniteBigRational) =
    toFiniteBigRational() - other

operator fun BDouble.times(other: FiniteBigRational) =
    toFiniteBigRational() * other

operator fun Double.times(other: FiniteBigRational) =
    toFiniteBigRational() * other

operator fun Float.times(other: FiniteBigRational) =
    toFiniteBigRational() * other

operator fun BInt.times(other: FiniteBigRational) =
    toFiniteBigRational() * other

operator fun Long.times(other: FiniteBigRational) =
    toFiniteBigRational() * other

operator fun Int.times(other: FiniteBigRational) =
    toFiniteBigRational() * other

operator fun BDouble.div(other: FiniteBigRational) =
    toFiniteBigRational() / other

operator fun Double.div(other: FiniteBigRational) =
    toFiniteBigRational() / other

operator fun Float.div(other: FiniteBigRational) =
    toFiniteBigRational() / other

operator fun BInt.div(other: FiniteBigRational) =
    toFiniteBigRational() / other

operator fun Long.div(other: FiniteBigRational) =
    toFiniteBigRational() / other

operator fun Int.div(other: FiniteBigRational) = toFiniteBigRational() / other

@Suppress("UNUSED_PARAMETER")
operator fun BDouble.rem(other: FiniteBigRational) = ZERO

@Suppress("UNUSED_PARAMETER")
operator fun Double.rem(other: FiniteBigRational) = ZERO

@Suppress("UNUSED_PARAMETER")
operator fun Float.rem(other: FiniteBigRational) = ZERO

@Suppress("UNUSED_PARAMETER")
operator fun BInt.rem(other: FiniteBigRational) = ZERO

@Suppress("UNUSED_PARAMETER")
operator fun Long.rem(other: FiniteBigRational) = ZERO

@Suppress("UNUSED_PARAMETER")
operator fun Int.rem(other: FiniteBigRational) = ZERO

operator fun BDouble.rangeTo(other: FiniteBigRational) =
    toFiniteBigRational()..other

operator fun Double.rangeTo(other: FiniteBigRational) =
    toFiniteBigRational()..other

operator fun Float.rangeTo(other: FiniteBigRational) =
    toFiniteBigRational()..other

operator fun BInt.rangeTo(other: FiniteBigRational) =
    toFiniteBigRational()..other

operator fun Long.rangeTo(other: FiniteBigRational) =
    toFiniteBigRational()..other

operator fun Int.rangeTo(other: FiniteBigRational) =
    toFiniteBigRational()..other

private fun convert(other: BDouble) = when (other) {
    BDouble.ZERO -> ZERO
    BDouble.ONE -> ONE
    BDouble.TEN -> TEN
    else -> {
        val bd = other.stripTrailingZeros()
        val scale = bd.scale()
        val unscaledValue = bd.unscaledValue()
        when {
            0 == scale -> unscaledValue over 1
            0 > scale -> (unscaledValue * BInt.TEN.pow(-scale)) over 1
            else -> unscaledValue over BInt.TEN.pow(scale)
        }
    }
}

/**
 * Since the conversion to a rational is _exact_, converting the resulting
 * rational back to a [Double] produces the original value.
 */
private fun convert(other: Double) = when {
    other == 0.0 -> ZERO
    other == 1.0 -> ONE
    other == 2.0 -> TWO
    // Not 10.0 -- decimal rounding to floating point is tricksy
    !other.isFinite() -> throw ArithmeticException("non-finite")
    other < 0 -> -TWO.pow(exponent(other)) * factor(other)
    else -> TWO.pow(exponent(other)) * factor(other)
}

private fun factor(other: Double): FiniteBigRational {
    val denominator = 1L shl 52
    val numerator = mantissa(other) + denominator

    return valueOf(numerator.toBigInteger(), denominator.toBigInteger())
}

/**
 * Returns the pair of `this / other` (quotient) and `this % other`
 * (remainder) integral division and modulo operations.
 *
 * @see [div]
 */
fun FiniteBigRational.divideAndRemainder(other: FiniteBigRational):
        Pair<FiniteBigRational, FiniteBigRational> {
    val quotient = (this / other).round()
    val remainder = this - other * quotient
    return quotient to remainder
}

/**
 * Returns a `FiniteBigRational` whose value is the greatest common divisor of
 * the absolute values of `this` and `other`.  Returns 0 when `this` and
 * `other` are both 0.
 */
fun FiniteBigRational.gcd(other: FiniteBigRational) =
    if (ZERO == this) other else valueOf(
        numerator.gcd(other.numerator),
        denominator.lcm(other.denominator)
    )

/**
 * Returns a `FiniteBigRational` whose value is the lowest common multiple of
 * the absolute values of `this` and `other`.  Returns 0 when `this` and
 * `other` are both 0.
 */
fun FiniteBigRational.lcm(other: FiniteBigRational) =
    if (ZERO == this) ZERO else valueOf(
        numerator.lcm(other.numerator),
        denominator.gcd(other.denominator)
    )

/**
 * Rounds to the nearest whole number _less than or equal_ to this
 * `FiniteBigRational`.  Non-finite values return themselves.
 */
fun FiniteBigRational.floor() = when {
    roundsToSelf() -> this
    ZERO <= this -> round()
    else -> round() - ONE
}

/**
 * Rounds to the nearest whole number _greater than or equal_ to this
 * `FiniteBigRational`.  Non-finite values return themselves.
 */
fun FiniteBigRational.ceil() = when {
    roundsToSelf() -> this
    ZERO <= this -> round() + ONE
    else -> round()
}

/**
 * Rounds to the nearest whole number _closer to 0_ than this
 * FiniteBigRational, or when this FiniteBigRational is whole, the same
 * `FiniteBigRational`.  Non-finite values return themselves.
 */
fun FiniteBigRational.round() = when {
    roundsToSelf() -> this
    else -> (numerator / denominator).toFiniteBigRational()
}

private fun FiniteBigRational.roundsToSelf() = isInteger()

/** Returns the finite continued fraction of this `FiniteBigRational`. */
fun FiniteBigRational.toContinuedFraction() =
    FiniteContinuedFraction.valueOf(this)

/** Checks that this rational is 0. */
fun FiniteBigRational.isZero() = ZERO === this
