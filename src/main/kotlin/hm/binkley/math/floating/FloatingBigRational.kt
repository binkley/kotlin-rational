package hm.binkley.math.floating

import hm.binkley.math.BDouble
import hm.binkley.math.BInt
import hm.binkley.math.BigRationalBase
import hm.binkley.math.BigRationalCompanion
import hm.binkley.math.big
import hm.binkley.math.divideAndRemainder
import hm.binkley.math.floating.FloatingBigRational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.POSITIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.ZERO
import hm.binkley.math.floating.FloatingBigRational.Companion.valueOf
import hm.binkley.math.isZero
import java.math.BigDecimal
import java.math.RoundingMode

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
 */
@Suppress("EqualsOrHashCode")
public class FloatingBigRational private constructor(
    numerator: BInt,
    denominator: BInt,
) : BigRationalBase<FloatingBigRational>(
    numerator,
    denominator,
) {
    override val companion: Companion get() = Companion

    /**
     * Returns this as a [BigDecimal] corresponding to [toDouble] following the
     * same rules as [Double.toBigDecimal].  Note: this maintains that "double
     * -> BigDecimal" and "BigRational -> BigDecimal" look the same.
     *
     * @throws ArithmeticException if denominator are coprime (produce a
     * repeating decimal) or for non-finite rationals
     */
    override fun toBigDecimal(): BigDecimal =
        if (!isFinite()) throw ArithmeticException("Non-finite")
        else super.toBigDecimal()

    /**
     * @see [Double.toLong]
     * @see [BigDecimal.toLong]
     */
    override fun toLong(): Long = when {
        !isFinite() -> throw ArithmeticException("No conversion for $this")
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
    override fun toDouble(): Double = when {
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
    override fun compareTo(other: FloatingBigRational): Int = when {
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
    override val sign: FloatingBigRational
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
    override operator fun rem(
        divisor: FloatingBigRational,
    ): FloatingBigRational = when {
        isNaN() || divisor.isNaN() -> NaN
        else -> super.rem(divisor)
    }

    override fun pow(exponent: Int): FloatingBigRational = when {
        isNaN() -> NaN
        isInfinite() && 0 == exponent -> NaN
        else -> super.pow(exponent)
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
    override fun mediant(that: FloatingBigRational): FloatingBigRational =
        when {
            isNaN() || that.isNaN() -> NaN
            (isPositiveInfinity() && that.isNegativeInfinity())
                || (isNegativeInfinity() && that.isPositiveInfinity()) -> ZERO
            else -> super.mediant(that)
        }

    override fun round(roundingMode: RoundingMode): FloatingBigRational =
        when {
            isNaN() || isPositiveInfinity() || isNegativeInfinity() -> this
            else -> super.round(roundingMode)
        }

    /**
     * Checks that this rational is dyadic, that is, the denominator is a power
     * of 2, or `false` if this number is not finite.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Dyadic_rational"><cite>Dyadic rational</cite></a>
     */
    override fun isDyadic(): Boolean = isFinite() && super.isDyadic()

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
    override fun isPAdic(p: Long): Boolean = isFinite() && super.isPAdic(p)

    /**
     * NB -- Infinities and "not a number" are not equal to themselves.
     *
     * @see Any.equals
     */
    override fun equals(other: Any?): Boolean = when {
        isNaN() -> false
        else -> super.equals(other)
    }

    /**
     * Returns a string representation of the object.  In particular:
     * * [NaN] is "NaN"
     * * [POSITIVE_INFINITY] is "+∞" (UNICODE)
     * * [NEGATIVE_INFINITY] is "-∞" (UNICODE)
     * * Finite values are [numerator]/[denominator]
     */
    override fun toString(): String = when {
        isNaN() -> "NaN"
        isPositiveInfinity() -> "Infinity"
        isNegativeInfinity() -> "-Infinity"
        else -> super.toString()
    }

    public companion object : BigRationalCompanion<FloatingBigRational>(
        ZERO = FloatingBigRational(0.big, 1.big),
        ONE = FloatingBigRational(1.big, 1.big),
        TWO = FloatingBigRational(2.big, 1.big),
        TEN = FloatingBigRational(10.big, 1.big),
    ) {
        /**
         * A constant holding "not a number" (NaN) value of type
         * [FloatingBigRational]. It is equivalent `0 over 0`.
         *
         * Usable directly from Java via `Companion`.
         */
        @JvmField
        public val NaN: FloatingBigRational = FloatingBigRational(0.big, 0.big)

        /**
         * A constant holding positive infinity value of type [FloatingBigRational].
         * It is equivalent `1 over 0`.
         *
         * Usable directly from Java via `Companion`.
         */
        @JvmField
        public val POSITIVE_INFINITY: FloatingBigRational =
            FloatingBigRational(1.big, 0.big)

        /**
         * A constant holding negative infinity value of type [FloatingBigRational].
         * It is equivalent `-1 over 0`.
         *
         * Usable directly from Java via `Companion`.
         */
        @JvmField
        public val NEGATIVE_INFINITY: FloatingBigRational =
            FloatingBigRational(1.big.negate(), 0.big)

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
            denominator: BInt,
        ): FloatingBigRational {
            if (denominator.isZero()) return when {
                numerator.isZero() -> NaN
                numerator.signum() == 1 -> POSITIVE_INFINITY
                else -> NEGATIVE_INFINITY
            }

            return construct(numerator, denominator) { n, d ->
                FloatingBigRational(n, d)
            }
        }

        override fun valueOf(floatingPoint: Double): FloatingBigRational =
            when {
                floatingPoint.isNaN() -> NaN
                floatingPoint == Double.POSITIVE_INFINITY -> POSITIVE_INFINITY
                floatingPoint == Double.NEGATIVE_INFINITY -> NEGATIVE_INFINITY
                else -> super.valueOf(floatingPoint)
            }

        override fun valueOf(floatingPoint: Float): FloatingBigRational =
            when {
                floatingPoint.isNaN() -> NaN
                floatingPoint == Float.POSITIVE_INFINITY -> POSITIVE_INFINITY
                floatingPoint == Float.NEGATIVE_INFINITY -> NEGATIVE_INFINITY
                else -> super.valueOf(floatingPoint)
            }

        override fun iteratorCheck(
            first: FloatingBigRational,
            last: FloatingBigRational,
            step: FloatingBigRational,
        ) {
            super.iteratorCheck(first, last, step)
            if (!step.isFinite()) error("Non-finite step.")
            if (!first.isFinite() || !last.isFinite())
                error("Non-finite bounds.")
        }
    }
}

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BDouble.over(denominator: BDouble): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BDouble.over(denominator: Double): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BDouble.over(denominator: Float): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BDouble.over(denominator: BInt): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BDouble.over(denominator: Long): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BDouble.over(denominator: Int): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: BDouble): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: BInt): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Long): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Int): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Double): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Float): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: BDouble): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: BInt): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Long): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Int): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Double): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Float): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BInt.over(denominator: BDouble): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BInt.over(denominator: Double): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BInt.over(denominator: Float): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BInt.over(denominator: BInt): FloatingBigRational =
    valueOf(this, denominator)

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BInt.over(denominator: Long): FloatingBigRational =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BInt.over(denominator: Int): FloatingBigRational =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Double): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Float): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: BDouble): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: BInt): FloatingBigRational =
    valueOf(toBigInteger(), denominator)

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Long): FloatingBigRational =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Int): FloatingBigRational =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: BDouble): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Double): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Float): FloatingBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: BInt): FloatingBigRational =
    valueOf(toBigInteger(), denominator)

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Long): FloatingBigRational =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Int): FloatingBigRational =
    valueOf(toBigInteger(), denominator.toBigInteger())

/** Returns the value of this number as a `BigRational`. */
public fun BDouble.toBigRational(): FloatingBigRational = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
public fun Double.toBigRational(): FloatingBigRational = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
public fun Float.toBigRational(): FloatingBigRational = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
public fun BInt.toBigRational(): FloatingBigRational = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
public fun Long.toBigRational(): FloatingBigRational = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
public fun Int.toBigRational(): FloatingBigRational = valueOf(this)

/**
 * Returns the finite continued fraction of this BigRational.
 *
 * Non-finite BigRationals produce `[NaN;]`.
 */
public fun FloatingBigRational.toContinuedFraction(): FloatingContinuedFraction =
    FloatingContinuedFraction.valueOf(this)

/**
 * Checks that this rational is infinite, positive or negative.  "Not a
 * number" is not infinite.
 */
public fun FloatingBigRational.isInfinite(): Boolean =
    isPositiveInfinity() || isNegativeInfinity()

/**
 * Checks that this rational is finite.  "Not a number" and infinities are
 * not finite.
 */
public fun FloatingBigRational.isFinite(): Boolean = !isNaN() && !isInfinite()

/**
 * Checks that this rational is "not a number".
 *
 * NB -- `NaN != NaN`
 */
public fun FloatingBigRational.isNaN(): Boolean = this === NaN

/**
 * Checks that this rational is positive infinity.
 *
 * NB -- `POSITIVE_INFINITY != POSITIVE_INFINITY`
 */
public fun FloatingBigRational.isPositiveInfinity(): Boolean =
    this === POSITIVE_INFINITY

/**
 * Checks that this rational is negative infinity.
 *
 * NB -- `NEGATIVE_INFINITY != NEGATIVE_INFINITY`
 */
public fun FloatingBigRational.isNegativeInfinity(): Boolean =
    this === NEGATIVE_INFINITY
