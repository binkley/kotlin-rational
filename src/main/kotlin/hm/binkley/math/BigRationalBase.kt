package hm.binkley.math

import hm.binkley.math.algebra.Field
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.math.RoundingMode.FLOOR
import java.math.RoundingMode.HALF_UP
import java.util.Objects.hash

public abstract class BigRationalBase<
    T : BigRationalBase<T>
    > protected constructor(
    public val numerator: BFixed,
    public val denominator: BFixed,
) : Number(), Comparable<T>, Field<T> {
    public abstract override val companion: BRatCompanion<T>

    /** Returns the absolute value. */
    @Suppress("UNCHECKED_CAST")
    public val absoluteValue: T
        get() =
            if (0 > numerator.signum()) -this
            else this as T

    /**
     * Returns the reciprocal.
     *
     * @see unaryDiv
     */
    public val reciprocal: T get() = companion.valueOf(denominator, numerator)

    /**
     * The signum of this value as an integer: -1 for negative, 0 for zero, or 1
     * for positive.
     */
    public fun signum(): Int = numerator.signum()

    /**
     * The signum of this value as a big rational type: `-ONE` for negative,
     * `ZERO` for zero, or `ONE` for positive.
     */
    public open val sign: T get() = companion.valueOf(signum())

    /**
     * Returns this as a [BigDecimal] corresponding to [toDouble] following the
     * same rules as [Double.toBigDecimal].  Note: this maintains that "double
     * -> BigDecimal" and "BigRational -> BigDecimal" look the same.
     *
     * @throws ArithmeticException if denominator are coprime (produce a
     * repeating decimal)
     */
    public open fun toBigDecimal(): BigDecimal = toDouble().toBigDecimal()

    /**
     * Returns this as a [BigDecimal] corresponding to [toDouble] up to
     * [limitPlaces] digits after the decimal place, for example when working
     * with repeating expansions such as "1/3".  For non-repeating decimals,
     * zero-pads any remaining places to reach the limit.
     *
     * Examples with `1 over 2`:
     * - `toBigDecimal(0)` -> 0
     * - `toBigDecimal(0, CEILING)` -> 1
     * - `toBigDecimal(1)` -> 0.5
     * - `toBigDecimal(1, CEILING)` -> 0.5
     * - `toBigDecimal(2)` -> 0.50
     * - `toBigDecimal(2, CEILING)` -> 0.50
     *
     * Examples with `1 over 3`:
     * - `toBigDecimal(0)` -> 0
     * - `toBigDecimal(0, CEILING)` -> 1
     * - `toBigDecimal(1)` -> 0.3
     * - `toBigDecimal(1, CEILING)` -> 0.4
     * - `toBigDecimal(2)` -> 0.33
     * - `toBigDecimal(2, CEILING)` -> 0.34
     *
     * The default [roundingMode] is [FLOOR] when truncating digits past
     * [limitPlaces].
     */
    @JvmOverloads
    public fun toBigDecimal(
        limitPlaces: Int,
        roundingMode: RoundingMode = FLOOR,
    ): BigDecimal = BFloating(numerator, limitPlaces)
        .divide(BFloating(denominator, limitPlaces), roundingMode)

    /**
     * Raises an [IllegalStateException].  Kotlin provides a [Number.toChar];
     * Java does not have a conversion to [Character] for [java.lang.Number].
     */
    override fun toChar(): Char =
        throw UnsupportedOperationException("Characters are non-numeric")

    /** @see [Long.toByte] */
    override fun toByte(): Byte = toLong().toByte()

    /** @see [Long.toShort] */
    override fun toShort(): Short = toLong().toShort()

    /** @see [Long.toInt] */
    override fun toInt(): Int = toLong().toInt()

    /** @see [BigDecimal.toLong] */
    override fun toLong(): Long = (numerator / denominator).toLong()

    /**
     * @see [Double.toFloat]
     * @see [toDouble]
     */
    override fun toFloat(): Float = toDouble().toFloat()

    /**
     * Returns the value of this number as a [Double], which may involve
     * rounding.  This produces an _exact_ conversion, that is,
     * `123.456.toBigRational().toDouble == 123.456`, and follows the rules
     * for [BigDecimal.toDouble].
     *
     * @see [BigDecimal.toDouble]
     * @see [BRatCompanion.valueOf(Double)]
     */
    override fun toDouble(): Double =
        numerator.toBigDecimal().divide(denominator.toBigDecimal()).toDouble()

    /**
     * Compares this object with the specified object for order. Returns
     * 0 when this object is equal to the specified [other] object, -1 when
     * it is less than [other], or 1 when it is greater than [other].
     *
     * Stable ordering produces:
     * - -1
     * - 0
     * - 1
     */
    override fun compareTo(other: T): Int = when {
        this === other -> 0 // Sort stability for constants
        else -> {
            val a = numerator * other.denominator
            val b = other.numerator * denominator
            a.compareTo(b)
        }
    }

    /** Returns the arithmetic inverse of this value. */
    override operator fun unaryMinus(): T =
        companion.valueOf(numerator.negate(), denominator)

    /** Adds the other value to this value. */
    override operator fun plus(addend: T): T =
        if (denominator == addend.denominator)
            companion.valueOf(
                numerator + addend.numerator,
                denominator
            )
        else companion.valueOf(
            numerator * addend.denominator + addend.numerator * denominator,
            denominator * addend.denominator
        )

    /** Multiplies this value by the other value. */
    override operator fun times(factor: T): T =
        companion.valueOf(
            numerator * factor.numerator,
            denominator * factor.denominator
        )

    /**
     * Simulates a non-existent "unary div" operator.
     *
     * @see reciprocal
     */
    override fun unaryDiv(): T = reciprocal

    /**
     * Finds the remainder of this value by [divisor] exactly: always 0
     * (division is exact for rationals).
     *
     * @see [divideAndRemainder]
     */
    @Suppress("UNUSED_PARAMETER")
    public open operator fun rem(divisor: T): T = when (divisor) {
        companion.ZERO -> throw ArithmeticException("Modulus by zero")
        else -> companion.ZERO
    }

    /**
     * Returns the value `(this^exponent)`. Note that [exponent] is an integer
     * rather than a big rational.
     *
     * Note that for floating big rationals, extra rules apply:
     * - NaN to any power is NaN
     * - Either infinity to the 0th power is NaN
     * - Either infinity to a negative power is 0
     * - Positive infinity to a positive power is positive infinity
     * - Negative infinity to an odd positive power is negative infinity
     * - Negative infinity to an even positive power is positive infinity
     */
    public open fun pow(exponent: Int): T = when {
        0 > exponent -> unaryDiv() `^` -exponent
        else -> companion.valueOf(
            numerator `^` exponent,
            denominator `^` exponent
        )
    }

    /**
     * Returns the Farey value between this big rational and [that], or
     * the same value when equal.
     *
     * If `a/b` and `c/d` are rational numbers such that `a/b ≠ c/d`, then
     * this function returns `(a+c)/(b+d)` (order of `this` and [that] does
     * not matter).
     */
    public open fun mediant(that: T): T = companion.valueOf(
        numerator + that.numerator,
        denominator + that.denominator
    )

    /** Checks that this rational is a whole number (no fractional part). */
    public fun isWhole(): Boolean = denominator.isUnit()

    /** Rounds to the nearest whole number according to [roundingMode]. */
    @Suppress("UNCHECKED_CAST")
    public open fun round(roundingMode: RoundingMode): T =
        if (isWhole()) this as T
        else companion.valueOf(
            // BigInteger does not have a divide with rounding mode
            numerator.toBigDecimal()
                .divide(denominator.toBigDecimal(), roundingMode)
                .setScale(0)
        )

    /**
     * Checks that this rational is _p_-adic, that is, the denominator is a
     * power of [p].
     *
     * *NB* &mdash; No check is made that [p] is prime, as required by the
     * definition of _p_-adic numbers.
     *
     * @see <a href="https://en.wikipedia.org/wiki/P-adic_number"><cite>_p_-adic
     * number</cite></a>
     */
    public open fun isPAdic(p: Long): Boolean = denominator.isPAdic(p)

    override fun equals(other: Any?): Boolean = this === other ||
        other is BRatBase<*> &&
        javaClass == other.javaClass &&
        numerator == other.numerator &&
        denominator == other.denominator

    override fun hashCode(): Int = hash(javaClass, numerator, denominator)

    /**
     * Returns a string representation of the object,
     * "[numerator]/[denominator]".
     */
    override fun toString(): String = when {
        denominator.isUnit() -> numerator.toString()
        else -> "$numerator⁄$denominator" // UNICODE fraction slash
    }
}

/**
 * Returns this as a [BigInteger] which may involve rounding
 * corresponding to rounding mode [HALF_UP].
 */
public fun <T : BRatBase<T>> T.toBigInteger(): BigInteger =
    numerator / denominator

/** Increments this value by 1. */
public operator fun <T : BRatBase<T>> T.inc(): T =
    companion.valueOf(numerator + denominator, denominator)

/** Decrements this value by 1. */
public operator fun <T : BRatBase<T>> T.dec(): T =
    companion.valueOf(numerator - denominator, denominator)

/** Finds the absolute difference between values. */
public fun <T : BRatBase<T>> T.diff(other: T): T =
    (this - other).absoluteValue

/** Checks that this rational is 0. */
public fun <T : BRatBase<T>> T.isZero(): Boolean =
    companion.ZERO === this

/** Checks that this rational is 1. */
public fun <T : BRatBase<T>> T.isUnit(): Boolean = companion.ONE === this

/** Checks that this rational is greater than zero. */
public fun <T : BRatBase<T>> T.isPositive(): Boolean = 1 == signum()

/** Checks that this rational is less than zero. */
public fun <T : BRatBase<T>> T.isNegative(): Boolean = -1 == signum()

/**
 * Checks that this rational has an even denominator.  The odds of a random
 * rational number having an even denominator is exactly 1/3 (Salamin and
 * Gosper 1972).
 *
 * See [HAKMEM](https://en.wikipedia.org/wiki/HAKMEM).
 */
public fun <T : BRatBase<T>> T.isDenominatorEven(): Boolean =
    denominator.isEven()

/**
 * Checks that this rational is dyadic, that is, the denominator is a power
 * of 2.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Dyadic_rational"><cite>Dyadic rational</cite></a>
 */
public fun <T : BRatBase<T>> T.isDyadic(): Boolean = isPAdic(2)
