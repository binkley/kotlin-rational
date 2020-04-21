package hm.binkley.math

import hm.binkley.math.BigRational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.BigRational.Companion.NaN
import hm.binkley.math.BigRational.Companion.POSITIVE_INFINITY
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
     * Finds the remainder of this value by [divisor]: always 0 (division is
     * exact), or [NaN] if either value is [NaN].
     *
     * @see [divideAndRemainder]
     */
    override operator fun rem(divisor: BigRational) = when {
        isNaN() || divisor.isNaN() -> NaN
        else -> super.rem(divisor)
    }

    /**
     * Returns the Farey value between this FiniteBigRational and [that], the
     * same value when equal.  If either value is [NaN], returns [NaN]. [ZERO]
     * is between the two infinities, and the infinities are between
     * themselves.
     *
     * If `a/b` and `c/d` are rational numbers such that `a/b ≠ c/d` or, then
     * this function returns `(a+c)/(b+d)` (order of `this` and [that] does
     * not matter).
     */
    override fun mediant(that: BigRational) = when {
        isNaN() || that.isNaN() -> NaN
        (isPositiveInfinity() && that.isNegativeInfinity())
                || (isNegativeInfinity() && that.isPositiveInfinity()) -> ZERO
        else -> super.mediant(that)
    }

    override fun roundsToSelf() = super.roundsToSelf() || !isFinite()

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

        override fun valueOf(floatingPoint: Double) = when {
            floatingPoint.isNaN() -> NaN
            floatingPoint.isInfinite() -> if (floatingPoint < 0.0) NEGATIVE_INFINITY else POSITIVE_INFINITY
            else -> super.valueOf(floatingPoint)
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
fun BDouble.toBigRational() = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
fun Double.toBigRational() = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
fun Float.toBigRational() = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
fun BInt.toBigRational() = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
fun Long.toBigRational() = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
fun Int.toBigRational() = valueOf(this)

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
