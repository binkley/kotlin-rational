package hm.binkley.math

import hm.binkley.math.BigRational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.BigRational.Companion.NaN
import hm.binkley.math.BigRational.Companion.ONE
import hm.binkley.math.BigRational.Companion.POSITIVE_INFINITY
import hm.binkley.math.BigRational.Companion.TEN
import hm.binkley.math.BigRational.Companion.TWO
import hm.binkley.math.BigRational.Companion.ZERO
import hm.binkley.math.BigRational.Companion.valueOf
import java.math.BigDecimal

/**
 * Immutable arbitrary-precision rationals (finite fractions).  `BigRational`
 * provides analogues to all of Kotlin's [Long] operators where appropriate.
 * Additionally, `BigRational` provides operations for GCD and LCM
 * calculation.
 *
 * Comparison operations perform signed comparisons, analogous to those
 * performed by Kotlin's relational and equality operators.
 *
 * Division by [ZERO] does not raise an [ArithmeticException]; rather, it
 * produces infinities or "not a number".  Infinities and "not a number"
 * propagate where appropriate.
 *
 * Ranges increment by 1 unless otherwise specified.
 *
 * @todo Consider `Short` and `Byte` overloads
 * @todo Assign properties at construction; avoid circular ctors
 */
@Suppress("EqualsOrHashCode")
class BigRational private constructor(
    numerator: BInt,
    denominator: BInt
) : BigRationalBase<BigRational>(numerator, denominator, BigRational) {
    /**
     * @see [Double.toLong]
     * @see [BigDecimal.toLong]
     */
    override fun toLong() = when {
        isNaN() -> 0L
        isPositiveInfinity() -> Long.MAX_VALUE
        isNegativeInfinity() -> Long.MIN_VALUE
        else -> super.toLong()
    }

    /**
     * Returns the value of this number as a [Double], which may involve
     * rounding.  This should produce an _exact_ conversion, that is,
     * `123.456.toBigRational().toDouble == 123.456`.
     *
     * Handles special values `NaN`, `POSITIVE_INFINITY`, and
     * `NEGATIVE_INFINITY`.
     *
     * @see [BigDecimal.toDouble] with similar behavior
     */
    override fun toDouble() = when {
        isNaN() -> Double.NaN
        isPositiveInfinity() -> Double.POSITIVE_INFINITY
        isNegativeInfinity() -> Double.NEGATIVE_INFINITY
        else -> super.toDouble()
    }

    /**
     * Compares this object with the specified object for order. Returns
     * 0 when this object is equal to the specified [other] object, -1 when
     * it is less than [other], or 1 when it is greater than [other].
     *
     * Sorting ignores [equals] for special values.  [NaN] sorts to the end,
     * even as `NaN != NaN` (and similarly for the infinities).
     *
     * Considering special values, stable ordering produces:
     * - -∞
     * - -1
     * - 0
     * - 1
     * - +∞
     * - NaN
     */
    override fun compareTo(other: BigRational) = when {
        this === other -> 0 // Sort stability for constants
        isNegativeInfinity() -> -1
        isNaN() -> 1 // NaN sorts after +Inf at the end
        other.isNaN() -> -1
        // +∞ is handled by else, but be explicit to aid reading
        isPositiveInfinity() -> 1
        else -> super.compareTo(other)
    }

    /**
     * The signum of this value: -1 for negative, 0 for zero, or 1 for
     * positive.  `sign` of [NaN] is [NaN].
     */
    override val sign: BigRational
        get() = when {
            isNaN() -> NaN
            else -> super.sign
        }

    /**
     * Returns the Farey value between this FiniteBigRational and [other], the
     * same value when equal.  If either value is [NaN], returns [NaN]. [ZERO]
     * is between the two infinities, and the infinities are between
     * themselves.
     *
     * If `a/b` and `c/d` are rational numbers such that `a/b ≠ c/d` or, then
     * this function returns `(a+c)/(b+d)` (order of `this` and [other] does
     * not matter).
     */
    override fun mediant(other: BigRational) = when {
        isNaN() || other.isNaN() -> NaN
        (isPositiveInfinity() && other.isNegativeInfinity())
                || (isNegativeInfinity() && other.isPositiveInfinity()) -> ZERO
        else -> super.mediant(other)
    }

    /**
     * Checks that this rational is dyadic, that is, the denominator is a power
     * of 2, or `false` if this number is not finite.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Dyadic_rational"><cite>Dyadic rational</cite></a>
     */
    override fun isDyadic() = isFinite() && super.isDyadic()

    /**
     * Checks that this rational is _p_-adic, that is, the denominator is a power
     * of [p], or `false` if this number is not finite.
     *
     * *NB* &mdash; No check is made that [p] is prime, as required by the
     * definition of _p_-adic numbers.
     *
     * @see <a href="https://en.wikipedia.org/wiki/P-adic_number"><cite>_p_-adic
     * number</cite></a>
     */
    override fun isPAdic(p: Long) = isFinite() && super.isPAdic(p)

    /**
     * NB -- Infinities and "not a number" are not equal to themselves.
     *
     * @see Any.equals
     */
    override fun equals(other: Any?) = when {
        !isFinite() -> false
        else -> super.equals(other)
    }

    /**
     * Returns a string representation of the object.  In particular:
     * * [NaN] is "NaN"
     * * [POSITIVE_INFINITY] is "+∞" (UNICODE)
     * * [NEGATIVE_INFINITY] is "-∞" (UNICODE)
     * * Finite values are [numerator]/[denominator]
     */
    override fun toString() = when {
        isNaN() -> "NaN"
        isPositiveInfinity() -> "+∞"
        isNegativeInfinity() -> "-∞"
        else -> super.toString()
    }

    companion object : BigRationalCompanion<BigRational> {
        /**
         * A constant holding "not a number" (NaN) value of type
         * [BigRational]. It is equivalent `0 over 0`.
         */
        val NaN = BigRational(BInt.ZERO, BInt.ZERO)

        override val ZERO = BigRational(BInt.ZERO, BInt.ONE)
        override val ONE = BigRational(BInt.ONE, BInt.ONE)
        override val TWO = BigRational(BInt.TWO, BInt.ONE)
        override val TEN = BigRational(BInt.TEN, BInt.ONE)

        /**
         * A constant holding positive infinity value of type [BigRational].
         * It is equivalent `1 over 0`.
         */
        val POSITIVE_INFINITY = BigRational(BInt.ONE, BInt.ZERO)

        /**
         * A constant holding negative infinity value of type [BigRational].
         * It is equivalent `-1 over 0`.
         */
        val NEGATIVE_INFINITY = BigRational(BInt.ONE.negate(), BInt.ZERO)

        /**
         * Returns a `BigRational` whose value is equal to that of the
         * specified ratio, `numerator / denominator`.
         *
         * This factory method is in preference to an explicit constructor
         * allowing for reuse of frequently used BigRationals.  In particular:
         *
         * * NaN
         * * POSITIVE_INFINITY
         * * NEGATIVE_INFINITY
         * * ZERO
         * * ONE
         * * TWO
         * * TEN
         */
        override fun valueOf(
            numerator: BInt,
            denominator: BInt
        ): BigRational {
            if (denominator.isZero()) return when {
                numerator.isZero() -> NaN
                numerator.signum() == 1 -> POSITIVE_INFINITY
                else -> NEGATIVE_INFINITY
            }

            return construct(numerator, denominator) { n, d ->
                BigRational(n, d)
            }
        }

        override fun iteratorCheck(
            first: BigRational,
            last: BigRational,
            step: BigRational
        ) {
            if (!step.isFinite()) error("Non-finite step.")
            if (!first.isFinite() || !last.isFinite())
                error("Non-finite bounds.")
            if (step == ZERO) error("Step must be non-zero.")
        }
    }
}

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: BDouble) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: Double) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: Float) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: BInt) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: Long) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: Int) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: BDouble) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: BInt) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: Long) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: Int) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: Double) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: Float) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: BDouble) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: BInt) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: Long) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: Int) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: Double) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: Float) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: BDouble) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: Double) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: Float) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: BInt) = valueOf(this, denominator)

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: Long) =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: Int) =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: Double) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: Float) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: BDouble) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: BInt) = valueOf(toBigInteger(), denominator)

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: Long) =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: Int) =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: BDouble) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: Double) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: Float) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: BInt) = valueOf(toBigInteger(), denominator)

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: Long) =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: Int) =
    valueOf(toBigInteger(), denominator.toBigInteger())

/** Returns the value of this number as a `BigRational`. */
fun BDouble.toBigRational() = convert(this)

/** Returns the value of this number as a `BigRational`. */
fun Double.toBigRational() = convert(this)

/** Returns the value of this number as a `BigRational`. */
fun Float.toBigRational() = toDouble().toBigRational()

/** Returns the value of this number as a `BigRational`. */
fun BInt.toBigRational() = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
fun Long.toBigRational() = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
fun Int.toBigRational() = valueOf(this)

/**
 * Compares this value to the other.
 *
 * @see [BigRational.compareTo]
 */
operator fun BigRational.compareTo(other: BDouble) =
    this.compareTo(other.toBigRational())

/**
 * Compares this value to the other.
 *
 * @see [BigRational.compareTo]
 */
operator fun BDouble.compareTo(other: BigRational) =
    this.toBigRational().compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [BigRational.compareTo]
 */
operator fun BigRational.compareTo(other: Double) =
    this.compareTo(other.toBigRational())

/**
 * Compares this value to the other.
 *
 * @see [BigRational.compareTo]
 */
operator fun Double.compareTo(other: BigRational) =
    this.toBigRational().compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [BigRational.compareTo]
 */
operator fun BigRational.compareTo(other: Float) =
    this.compareTo(other.toBigRational())

/**
 * Compares this value to the other.
 *
 * @see [BigRational.compareTo]
 */
operator fun Float.compareTo(other: BigRational) =
    this.toBigRational().compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [BigRational.compareTo]
 */
operator fun BigRational.compareTo(other: BInt) =
    this.compareTo(other.toBigRational())

/**
 * Compares this value to the other.
 *
 * @see [BigRational.compareTo]
 */
operator fun BInt.compareTo(other: BigRational) =
    this.toBigRational().compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [BigRational.compareTo]
 */
operator fun BigRational.compareTo(other: Long) =
    this.compareTo(other.toBigRational())

/**
 * Compares this value to the other.
 *
 * @see [BigRational.compareTo]
 */
operator fun Long.compareTo(other: BigRational) =
    this.toBigRational().compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [BigRational.compareTo]
 */
operator fun BigRational.compareTo(other: Int) =
    this.compareTo(other.toBigRational())

/**
 * Compares this value to the other.
 *
 * @see [BigRational.compareTo]
 */
operator fun Int.compareTo(other: BigRational) =
    this.toBigRational().compareTo(other)

/** Adds the other value to this value. */
operator fun BigRational.plus(addend: BDouble) = this + addend.toBigRational()

/** Adds the other value to this value yielding a `BigRational`. */
operator fun BigRational.plus(addend: Double) = this + addend.toBigRational()

/** Adds the other value to this value yielding a `BigRational`. */
operator fun BigRational.plus(addend: Float) = this + addend.toBigRational()

/** Subtracts the other value from this value yielding a `BigRational`. */
operator fun BigRational.minus(subtrahend: BDouble) =
    this - subtrahend.toBigRational()

/** Subtracts the other value from this value yielding a `BigRational`. */
operator fun BigRational.minus(subtrahend: Double) =
    this - subtrahend.toBigRational()

/** Subtracts the other value from this value yielding a `BigRational`. */
operator fun BigRational.minus(subtrahend: Float) =
    this - subtrahend.toBigRational()

/** Multiplies this value by the other value. */
operator fun BigRational.times(multiplicand: BDouble) =
    this * multiplicand.toBigRational()

/** Multiplies this value by the other value yielding a `BigRational`. */
operator fun BigRational.times(multiplicand: Double) =
    this * multiplicand.toBigRational()

/** Multiplies this value by the other value yielding a `BigRational`. */
operator fun BigRational.times(multiplicand: Float) =
    this * multiplicand.toBigRational()

/**
 * Divides this value by the other value exactly yielding a `BigRational`.
 *
 * @see [divideAndRemainder]
 */
operator fun BigRational.div(divisor: BDouble) =
    this / divisor.toBigRational()

/**
 * Divides this value by the other value exactly yielding a `BigRational`.
 *
 * @see [divideAndRemainder]
 */
operator fun BigRational.div(divisor: Double) = this / divisor.toBigRational()

/**
 * Divides this value by the other value exactly yielding a `BigRational`.
 *
 * @see [divideAndRemainder]
 */
operator fun BigRational.div(divisor: Float) = this / divisor.toBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or [NaN] if either value is [NaN].
 *
 * @see [divideAndRemainder]
 */
operator fun BigRational.rem(divisor: BigRational) = when {
    isNaN() || divisor.isNaN() -> NaN
    else -> ZERO
}

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or [NaN] if either value is [NaN].
 *
 * @see [divideAndRemainder]
 */
operator fun BigRational.rem(divisor: BDouble) =
    this % divisor.toBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or [NaN] if either value is [NaN].
 *
 * @see [divideAndRemainder]
 */
operator fun BigRational.rem(divisor: Double) = this % divisor.toBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or [NaN] if either value is [NaN].
 *
 * @see [divideAndRemainder]
 */
operator fun BigRational.rem(divisor: Float) = this % divisor.toBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or [NaN] if either value is [NaN].
 *
 * @see [divideAndRemainder]
 */
operator fun BigRational.rem(divisor: BInt) = this % divisor.toBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or [NaN] if either value is [NaN].
 *
 * @see [divideAndRemainder]
 */
operator fun BigRational.rem(divisor: Long) = this % divisor.toBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is exact),
 * or [NaN] if either value is [NaN].
 *
 * @see [divideAndRemainder]
 */
operator fun BigRational.rem(divisor: Int) = this % divisor.toBigRational()

/** Creates a range from this value to the specified [other] value. */
operator fun BigRational.rangeTo(other: BigRational) =
    BigRationalProgression(this, other, ONE)

operator fun BigRational.rangeTo(other: BDouble) =
    rangeTo(other.toBigRational())

/** Creates a range from this value to the specified [other] value. */
operator fun BigRational.rangeTo(other: Double) =
    rangeTo(other.toBigRational())

/** Creates a range from this value to the specified [other] value. */
operator fun BigRational.rangeTo(other: Float) =
    rangeTo(other.toBigRational())

/** Creates a range from this value to the specified [other] value. */
operator fun BigRational.rangeTo(other: BInt) = rangeTo(other.toBigRational())

/** Creates a range from this value to the specified [other] value. */
operator fun BigRational.rangeTo(other: Long) = rangeTo(other.toBigRational())

/** Creates a range from this value to the specified [other] value. */
operator fun BigRational.rangeTo(other: Int) = rangeTo(other.toBigRational())

infix fun BigRational.downTo(other: BigRational) =
    BigRationalProgression(this, other, -ONE)

operator fun BDouble.plus(other: BigRational) = toBigRational() + other
operator fun Double.plus(other: BigRational) = toBigRational() + other
operator fun Float.plus(other: BigRational) = toBigRational() + other
operator fun BInt.plus(other: BigRational) = toBigRational() + other
operator fun Long.plus(other: BigRational) = toBigRational() + other
operator fun Int.plus(other: BigRational) = toBigRational() + other

operator fun BDouble.minus(other: BigRational) = toBigRational() - other
operator fun Double.minus(other: BigRational) = toBigRational() - other
operator fun Float.minus(other: BigRational) = toBigRational() - other
operator fun BInt.minus(other: BigRational) = toBigRational() - other
operator fun Long.minus(other: BigRational) = toBigRational() - other
operator fun Int.minus(other: BigRational) = toBigRational() - other

operator fun BDouble.times(other: BigRational) = toBigRational() * other
operator fun Double.times(other: BigRational) = toBigRational() * other
operator fun Float.times(other: BigRational) = toBigRational() * other
operator fun BInt.times(other: BigRational) = toBigRational() * other
operator fun Long.times(other: BigRational) = toBigRational() * other
operator fun Int.times(other: BigRational) = toBigRational() * other

operator fun BDouble.div(other: BigRational) = toBigRational() / other
operator fun Double.div(other: BigRational) = toBigRational() / other
operator fun Float.div(other: BigRational) = toBigRational() / other
operator fun BInt.div(other: BigRational) = toBigRational() / other
operator fun Long.div(other: BigRational) = toBigRational() / other
operator fun Int.div(other: BigRational) = toBigRational() / other

@Suppress("UNUSED_PARAMETER")
operator fun BDouble.rem(other: BigRational) = ZERO

@Suppress("UNUSED_PARAMETER")
operator fun Double.rem(other: BigRational) = ZERO

@Suppress("UNUSED_PARAMETER")
operator fun Float.rem(other: BigRational) = ZERO

@Suppress("UNUSED_PARAMETER")
operator fun BInt.rem(other: BigRational) = ZERO

@Suppress("UNUSED_PARAMETER")
operator fun Long.rem(other: BigRational) = ZERO

@Suppress("UNUSED_PARAMETER")
operator fun Int.rem(other: BigRational) = ZERO

operator fun BDouble.rangeTo(other: BigRational) = toBigRational()..other
operator fun Double.rangeTo(other: BigRational) = toBigRational()..other
operator fun Float.rangeTo(other: BigRational) = toBigRational()..other
operator fun BInt.rangeTo(other: BigRational) = toBigRational()..other
operator fun Long.rangeTo(other: BigRational) = toBigRational()..other
operator fun Int.rangeTo(other: BigRational) = toBigRational()..other

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
    other.isNaN() -> NaN
    other.isInfinite() -> if (other < 0.0) NEGATIVE_INFINITY else POSITIVE_INFINITY
    other < 0 -> -TWO.pow(exponent(other)) * factor(other)
    else -> TWO.pow(exponent(other)) * factor(other)
}

private fun factor(other: Double): BigRational {
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
fun BigRational.divideAndRemainder(other: BigRational):
        Pair<BigRational, BigRational> {
    val quotient = (this / other).round()
    val remainder = this - other * quotient
    return quotient to remainder
}

/**
 * Returns a `BigRational` whose value is the greatest common divisor of
 * the absolute values of `this` and `other`.  Returns 0 when `this` and
 * `other` are both 0.
 */
fun BigRational.gcd(other: BigRational) =
    if (ZERO == this) other else valueOf(
        numerator.gcd(other.numerator),
        denominator.lcm(other.denominator)
    )

/**
 * Returns a `BigRational` whose value is the lowest common multiple of
 * the absolute values of `this` and `other`.  Returns 0 when `this` and
 * `other` are both 0.
 */
fun BigRational.lcm(other: BigRational) =
    if (ZERO == this) ZERO else valueOf(
        numerator.lcm(other.numerator),
        denominator.gcd(other.denominator)
    )

/**
 * Rounds to the nearest whole number _less than or equal_ to this
 * `BigRational`.  Non-finite values return themselves.
 */
fun BigRational.floor() = when {
    roundsToSelf() -> this
    ZERO <= this -> round()
    else -> round() - ONE
}

/**
 * Rounds to the nearest whole number _greater than or equal_ to this
 * `BigRational`.  Non-finite values return themselves.
 */
fun BigRational.ceil() = when {
    roundsToSelf() -> this
    ZERO <= this -> round() + ONE
    else -> round()
}

/**
 * Rounds to the nearest whole number _closer to 0_ than this BigRational,
 * or when this BigRational is whole, the same `BigRational`.  Non-finite values
 * return themselves.
 */
fun BigRational.round() = when {
    roundsToSelf() -> this
    else -> (numerator / denominator).toBigRational()
}

private fun BigRational.roundsToSelf() = isInteger() || !isFinite()

/**
 * Returns the finite continued fraction of this BigRational.
 *
 * Non-finite BigRationals produce `[NaN;]`.
 */
fun BigRational.toContinuedFraction() = FiniteContinuedFraction.valueOf(this)

/**
 * Checks that this rational is a finite fraction.  Infinities and "not a
 * number" are not finite.
 *
 * @todo Consider separate types, which leads to sealed types
 */
fun BigRational.isFinite() = !isNaN() && !isInfinite()

/** Checks that this rational is 0. */
fun BigRational.isZero() = ZERO === this

/**
 * Checks that this rational is infinite, positive or negative.  "Not a
 * number" is not infinite.
 */
fun BigRational.isInfinite() = isPositiveInfinity() || isNegativeInfinity()

/**
 * Checks that this rational is "not a number".
 *
 * NB -- `NaN != NaN`
 */
fun BigRational.isNaN() = this === NaN

/**
 * Checks that this rational is positive infinity.
 *
 * NB -- `POSITIVE_INFINITY != POSITIVE_INFINITY`
 */
fun BigRational.isPositiveInfinity() = this === POSITIVE_INFINITY

/**
 * Checks that this rational is negative infinity.
 *
 * NB -- `NEGATIVE_INFINITY != NEGATIVE_INFINITY`
 */
fun BigRational.isNegativeInfinity() = this === NEGATIVE_INFINITY
