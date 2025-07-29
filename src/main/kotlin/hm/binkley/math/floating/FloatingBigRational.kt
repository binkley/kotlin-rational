package hm.binkley.math.floating

import hm.binkley.math.BFixed
import hm.binkley.math.BFloating
import hm.binkley.math.BigRationalBase
import hm.binkley.math.BigRationalCompanion
import hm.binkley.math.big
import hm.binkley.math.divideAndRemainder
import hm.binkley.math.equivalent
import hm.binkley.math.fixed.FixedBigRational
import hm.binkley.math.floating.FloatingBigRational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational.Companion.POSITIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.ZERO
import hm.binkley.math.floating.FloatingBigRational.Companion.valueOf
import hm.binkley.math.isZero
import java.math.BigDecimal
import java.math.RoundingMode

// Workarounds for Java interoperability

/** The identity element for addition. */
@JvmField
public val ZERO: BRat = ZERO

/** The identity element for multiplication. */
@JvmField
public val ONE: BRat = ONE

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
 * produces infinities or "not a number".
 * Infinities and "not a number" propagate where appropriate.
 *
 * Ranges increment by 1 unless otherwise specified.
 */
@Suppress("EqualsOrHashCode")
public class FloatingBigRational private constructor(
    numerator: BFixed,
    denominator: BFixed,
) : BigRationalBase<FloatingBigRational>(
    numerator,
    denominator,
) {
    override val companion: Companion get() = Companion

    /**
     * Returns this as a [BigDecimal] corresponding to [toDouble] following the
     * same rules as [Double.toBigDecimal].
     * Note: this maintains that "double -> BigDecimal" and "BigRational ->
     * BigDecimal" look the same.
     *
     * @throws ArithmeticException if denominator are coprime (produce a
     * repeating decimal) or for non-finite rationals
     */
    override fun toBigDecimal(): BFloating = if (!isFinite()) {
        throw ArithmeticException("Non-finite")
    } else {
        super.toBigDecimal()
    }

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
     * rounding.
     * This should produce an _exact_ conversion; that is,
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
     * Compares this object with the specified object for order.
     * Returns 0 when this object is equal to the specified [other] object, -1
     * when it is less than [other], or 1 when it is greater than [other].
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
    override fun compareTo(other: BRat): Int = when {
        this === other -> 0 // Sort stability for constants
        isNegativeInfinity() -> -1
        isNaN() -> 1 // NaN sorts after +Inf at the end
        other.isNaN() -> -1
        // +∞ is handled by else, but be explicit to aid reading
        isPositiveInfinity() -> 1
        else -> super.compareTo(other)
    }

    /**
     * The signum function of this value as a big rational type: `-ONE` for
     * negative, `ZERO` for zero, or `ONE` for positive.
     * `sign` of [NaN] is [NaN].
     */
    override val sign: BRat
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
    override operator fun rem(divisor: BRat): BRat = when {
        isNaN() || divisor.isNaN() -> NaN
        divisor.isZero() -> NaN
        else -> super.rem(divisor)
    }

    override fun pow(exponent: Int): BRat = when {
        isNaN() -> NaN
        isInfinite() && exponent.isZero() -> NaN
        else -> super.pow(exponent)
    }

    /**
     * Returns the Farey value between this FixedBigRational and [that], the
     * same value when equal.
     * If either value is [NaN], returns [NaN].
     * [ZERO] lies between the two infinities, and the infinities are between
     * themselves.
     *
     * If `a/b` and `c/d` are rational numbers such that `a/b ≠ c/d` or, then
     * this function returns `(a+c)/(b+d)` (order of `this` and [that] does
     * not matter).
     */
    override fun mediant(that: BRat): BRat = when {
        isNaN() || that.isNaN() -> NaN
        (isPositiveInfinity() && that.isNegativeInfinity()) ||
            (isNegativeInfinity() && that.isPositiveInfinity()) -> ZERO
        else -> super.mediant(that)
    }

    override fun round(roundingMode: RoundingMode): BRat = when {
        isNaN() || isPositiveInfinity() || isNegativeInfinity() -> this
        else -> super.round(roundingMode)
    }

    /**
     * Checks that this rational is _p_-adic, that is, the denominator is a
     * power of [p], or `false` if this number is not finite.
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
     * Returns a string representation of the object.
     * In particular:
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

    public companion object : BigRationalCompanion<BRat>(
        ZERO = BRat(0.big, 1.big),
        ONE = BRat(1.big, 1.big),
        TWO = BRat(2.big, 1.big),
        TEN = BRat(10.big, 1.big),
    ) {
        /**
         * A constant holding "not a number" (NaN) value of type [BRat].
         * It is equivalent `0 over 0`.
         *
         * Usable directly from Java via `Companion`.
         *
         * The construction avoids the obvious `ZERO / ZERO` for class loading.
         */
        @JvmField
        public val NaN: BRat = BRat(0.big, 0.big)

        /**
         * A constant holding positive infinity value of type [BRat].
         * It is equivalent `1 over 0`.
         *
         * Usable directly from Java via `Companion`.
         *
         * The construction avoids the obvious `ONE / ZERO` for class loading.
         */
        @JvmField
        public val POSITIVE_INFINITY: BRat = BRat(1.big, 0.big)

        /**
         * A constant holding negative infinity value of type [BRat].
         * It is equivalent `-1 over 0`.
         *
         * Usable directly from Java via `Companion`.
         *
         * The construction avoids the obvious `-ONE / ZERO` for class loading.
         */
        @JvmField
        public val NEGATIVE_INFINITY: BRat = BRat(1.big.negate(), 0.big)

        /**
         * Returns a [BRat] whose value is equal to that of the
         * specified ratio, `numerator / denominator`.
         *
         * This factory method is in preference to an explicit constructor, and
         * allows for reuse of frequently used values.
         * In particular:
         *
         * * NaN
         * * POSITIVE_INFINITY
         * * NEGATIVE_INFINITY
         * * ZERO
         * * ONE
         * * TWO
         * * TEN
         */
        override fun valueOf(numerator: BFixed, denominator: BFixed): BRat {
            if (denominator.isZero()) {
                return when {
                    numerator.isZero() -> NaN
                    numerator.signum() == 1 -> POSITIVE_INFINITY
                    else -> NEGATIVE_INFINITY
                }
            }

            return reduce(numerator, denominator) { n, d ->
                BRat(n, d)
            }
        }

        override fun valueOf(floatingPoint: Double): BRat = when {
            Double.POSITIVE_INFINITY == floatingPoint -> POSITIVE_INFINITY
            Double.NEGATIVE_INFINITY == floatingPoint -> NEGATIVE_INFINITY
            floatingPoint.isNaN() -> NaN
            else -> super.valueOf(floatingPoint)
        }

        override fun valueOf(floatingPoint: Float): BRat = when {
            Float.POSITIVE_INFINITY == floatingPoint -> POSITIVE_INFINITY
            Float.NEGATIVE_INFINITY == floatingPoint -> NEGATIVE_INFINITY
            floatingPoint.isNaN() -> NaN
            else -> super.valueOf(floatingPoint)
        }

        override fun iteratorCheck(first: BRat, last: BRat, step: BRat) {
            super.iteratorCheck(first, last, step)
            if (!step.isFinite()) error("Non-finite step.")
            if (!first.isFinite() || !last.isFinite()) {
                error("Non-finite bounds.")
            }
        }
    }
}

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFloating.over(denominator: BFloating): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFloating.over(denominator: Double): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFloating.over(denominator: Float): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFloating.over(denominator: BFixed): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFloating.over(denominator: Long): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFloating.over(denominator: Int): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: BFloating): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: BFixed): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Long): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Int): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Double): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Float): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: BFloating): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: BFixed): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Long): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Int): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Double): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Float): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFixed.over(denominator: BFloating): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFixed.over(denominator: Double): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFixed.over(denominator: Float): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFixed.over(denominator: BFixed): BRat =
    valueOf(this, denominator)

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFixed.over(denominator: Long): BRat =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFixed.over(denominator: Int): BRat =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Double): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Float): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: BFloating): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: BFixed): BRat =
    valueOf(toBigInteger(), denominator)

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Long): BRat =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Int): BRat =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: BFloating): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Double): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Float): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: BFixed): BRat =
    valueOf(toBigInteger(), denominator)

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Long): BRat =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `BigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Int): BRat =
    valueOf(toBigInteger(), denominator.toBigInteger())

/** Returns the value of this number as a `BigRational`. */
public fun BFloating.toBigRational(): BRat = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
public fun Double.toBigRational(): BRat = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
public fun Float.toBigRational(): BRat = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
public fun BFixed.toBigRational(): BRat = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
public fun Long.toBigRational(): BRat = valueOf(this)

/** Returns the value of this number as a `BigRational`. */
public fun Int.toBigRational(): BRat = valueOf(this)

/**
 * Returns the finite continued fraction of this BigRational.
 *
 * Non-finite BigRationals produce `[NaN;]`.
 */
public fun BRat.toContinuedFraction(): FloatingContinuedFraction =
    FloatingContinuedFraction.valueOf(this)

/**
 * Converts this _floating_ big rational to a _fixed_ equivalent.
 *
 * @see [equivalent]
 *
 * @throws ArithmeticException if this is `NaN`, `POSITIVE_INFINITY`, or
 * `NEGATIVE_INFINITY`
 */
public fun BRat.toFixedBigRational(): FixedBigRational =
    FixedBigRational.valueOf(numerator, denominator)

/**
 * Checks that this rational is infinite, positive or negative.
 * "Not a number" is neither finite nor infinite.
 */
public fun BRat.isInfinite(): Boolean =
    isPositiveInfinity() || isNegativeInfinity()

/**
 * Checks that this rational is finite.
 * "Not a number" is neither finite nor infinite.
 */
public fun BRat.isFinite(): Boolean = !isNaN() && !isInfinite()

/**
 * Checks that this rational is "not a number".
 *
 * **NB** &mdash; `NaN != NaN`
 */
public fun BRat.isNaN(): Boolean = this === NaN

/**
 * Checks that this rational is positive infinity.
 *
 * NB -- `POSITIVE_INFINITY != POSITIVE_INFINITY`
 */
public fun BRat.isPositiveInfinity(): Boolean = POSITIVE_INFINITY === this

/**
 * Checks that this rational is negative infinity.
 *
 * NB -- `NEGATIVE_INFINITY != NEGATIVE_INFINITY`
 */
public fun BRat.isNegativeInfinity(): Boolean = NEGATIVE_INFINITY === this
