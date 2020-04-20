package hm.binkley.math

import java.math.BigDecimal
import java.util.Objects.hash

@Suppress("PropertyName")
interface BigRationalCompanion<T : BigRationalBase<T>> {
    /** A constant holding value 0. It is equivalent `0 over 1`. */
    val ZERO: T

    /** A constant holding value 1. It is equivalent `1 over 1`. */
    val ONE: T

    /** A constant holding value 2. It is equivalent `2 over 1`. */
    val TWO: T

    /** A constant holding value 10. It is equivalent `10 over 1`. */
    val TEN: T

    fun valueOf(numerator: BInt, denominator: BInt): T

    fun construct(
        numerator: BInt,
        denominator: BInt,
        ctor: (BInt, BInt) -> T
    ): T {
        if (numerator.isZero()) return ZERO

        var n = numerator
        var d = denominator
        if (-1 == d.signum()) {
            n = n.negate()
            d = d.negate()
        }

        if (d.isOne()) return when {
            n.isOne() -> ONE
            n.isTwo() -> TWO
            n.isTen() -> TEN
            else -> ctor(n, d)
        }

        val gcd = n.gcd(d)
        if (!gcd.isOne()) {
            n /= gcd
            d /= gcd
        }

        return ctor(n, d)
    }
}

abstract class BigRationalBase<T : BigRationalBase<T>> internal constructor(
    val numerator: BInt,
    val denominator: BInt,
    internal val companion: BigRationalCompanion<T>
) : Number(), Comparable<T> {
    /**
     * Raises an [IllegalStateException].  Kotlin provides a [Number.toChar];
     * Java does not have a conversion to [Character] for [java.lang.Number].
     */
    override fun toChar(): Char = error("Characters are non-numeric")

    /** @see [Long.toByte] */
    override fun toByte() = toLong().toByte()

    /** @see [Long.toShort] */
    override fun toShort() = toLong().toShort()

    /** @see [Long.toInt] */
    override fun toInt() = toLong().toInt()

    /** @see [BigDecimal.toLong] */
    override fun toLong() = (numerator / denominator).toLong()

    /**
     * @see [Double.toFloat]
     * @see [toDouble]
     */
    override fun toFloat() = toDouble().toFloat()

    /**
     * Returns the value of this number as a [Double], which may involve
     * rounding.  This produces an _exact_ conversion, that is,
     * `123.456.toFiniteBigRational().toDouble == 123.456`.
     *
     * @see [BigDecimal.toDouble] with similar behavior
     */
    override fun toDouble() = numerator.toDouble() / denominator.toDouble()

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
    override fun compareTo(other: T) = when {
        this === other -> 0 // Sort stability for constants
        else -> {
            val a = numerator * other.denominator
            val b = other.numerator * denominator
            a.compareTo(b)
        }
    }

    /**
     * Returns the Farey value between this FiniteBigRational and [other], or
     * the same value when equal.
     *
     * If `a/b` and `c/d` are rational numbers such that `a/b ≠ c/d` or, then
     * this function returns `(a+c)/(b+d)` (order of `this` and [other] does
     * not matter).
     */
    open fun mediant(other: T) = companion.valueOf(
        numerator + other.numerator,
        denominator + other.denominator
    )

    /**
     * Checks that this rational is dyadic, that is, the denominator is a power
     * of 2.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Dyadic_rational"><cite>Dyadic rational</cite></a>
     */
    open fun isDyadic() = denominator.isDyadic()

    /**
     * Checks that this rational is _p_-adic, that is, the denominator is a power
     * of [p].
     *
     * *NB* &mdash; No check is made that [p] is prime, as required by the
     * definition of _p_-adic numbers.
     *
     * @see <a href="https://en.wikipedia.org/wiki/P-adic_number"><cite>_p_-adic
     * number</cite></a>
     */
    open fun isPAdic(p: Long) = denominator.isPAdic(p)

    override fun equals(other: Any?) = this === other ||
            other is BigRationalBase<*> &&
            numerator == other.numerator &&
            denominator == other.denominator

    override fun hashCode() = hash(numerator, denominator)

    /**
     * Returns a string representation of the object,
     * "[numerator]/[denominator]".
     */
    override fun toString() = when {
        denominator.isOne() -> numerator.toString()
        else -> "$numerator⁄$denominator" // UNICODE fraction slash
    }
}

/** Returns the absolute value. */
val <T : BigRationalBase<T>> T.absoluteValue: T
    get() = companion.valueOf(numerator.abs(), denominator)

/** Returns the reciprocal. */
val <T : BigRationalBase<T>> T.reciprocal: T
    get() = companion.valueOf(denominator, numerator)

/** Returns this value. */
operator fun <T : BigRationalBase<T>> T.unaryPlus() = this

/** Returns the arithmetic inverse of this value. */
operator fun <T : BigRationalBase<T>> T.unaryMinus() =
    companion.valueOf(numerator.negate(), denominator)

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> T.plus(addend: T) =
    if (denominator == addend.denominator)
        companion.valueOf(numerator + addend.numerator, denominator)
    else companion.valueOf(
        numerator * addend.denominator + addend.numerator * denominator,
        denominator * addend.denominator
    )

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> T.minus(subtrahend: T) =
    this + -subtrahend

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> T.times(multiplicand: T) =
    companion.valueOf(
        numerator * multiplicand.numerator,
        denominator * multiplicand.denominator
    )

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.div(divisor: T) =
    this * divisor.reciprocal

/** Checks that this rational is an integer. */
fun <T : BigRationalBase<T>> T.isInteger() = BInt.ONE == denominator

/**
 * Checks that this rational has an even denominator.  The odds of a random
 * rational number having an even denominator is exactly 1/3 (Salamin and
 * Gosper 1972).
 */
fun <T : BigRationalBase<T>> T.isDenominatorEven() = denominator.isEven()
