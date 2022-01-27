package hm.binkley.math

import hm.binkley.math.algebra.Field
import hm.binkley.math.algebra.FieldCompanion
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.math.RoundingMode.FLOOR
import java.math.RoundingMode.HALF_UP
import java.util.Objects.hash

/**
 * An abuse of Fields: `FixedBigRational` is a field, but
 * `FloatingBigRational` is not because of `NaN` and the infinities.
 *
 * *NB* &mdash; To avoid circular references, initialize [ZERO], [ONE], [TWO],
 * and [TEN] directly from constructors, and use neither [reduce] nor
 * [valueOf].
 *
 * @todo Provide`sqrt` via continued fractions, ie,
 *       https://en.wikipedia.org/wiki/Continued_fraction#Square_roots and
 *       [BigInteger.sqrtAndRemainder]
 * @todo Explore other ways to share code between fixed and floating flavors;
 *       `BigRationalBase` only exists to provide implementation inheritance
 * @todo `FieldCompanion` prevents ZERO and ONE from being JVM fields
 */
@Suppress("PropertyName")
public abstract class BigRationalCompanion<T : BigRationalBase<T>>(
    /**
     * A constant holding value 0 equivalent to `0 over 1`.
     *
     * Usable directly from Java via getter in `Companion`.
     */
    final override val ZERO: T,
    /**
     * A constant holding value 1 equivalent to `1 over 1`.
     *
     * Usable directly from Java via getter in `Companion`.
     */
    final override val ONE: T,
    /**
     * A constant holding value 2 equivalent to `2 over 1`.
     *
     * Usable directly from Java via `Companion`.
     */
    @JvmField
    public val TWO: T,
    /**
     * A constant holding value 10 equivalent to `10 over 1`.
     *
     * Usable directly from Java via `Companion`.
     */
    @JvmField
    public val TEN: T,
) : FieldCompanion<T> {
    public abstract fun valueOf(numerator: BInt, denominator: BInt): T

    /**
     * Since the conversion to a rational is _exact_, converting the resulting
     * rational back to a [BigDecimal] produces the original value.
     *
     * Note, however, such a round trip will _not_ preserve trailing zeroes,
     * just as converting BigDecimal -> Double -> BigDecimal does not preserve
     * them.
     */
    public open fun valueOf(floatingPoint: BDouble): T = when (floatingPoint) {
        0.0.big -> ZERO
        1.0.big -> ONE
        10.0.big -> TEN
        else -> {
            val scale = floatingPoint.scale()
            val unscaledValue = floatingPoint.unscaledValue()
            when {
                0 == scale -> valueOf(unscaledValue)
                0 > scale -> valueOf(unscaledValue * BInt.TEN.pow(-scale))
                else -> valueOf(unscaledValue, BInt.TEN.pow(scale))
            }
        }
    }

    /**
     * Since the conversion to a rational is _exact_, converting the resulting
     * rational back to a [Double] produces the original value.
     */
    public open fun valueOf(floatingPoint: Double): T =
        valueOf(floatingPoint.toBigDecimal())

    /**
     * Since the conversion to a rational is _exact_, converting the resulting
     * rational back to a [Float] produces the original value.
     *
     * Note that [BigDecimal] has a constructor for `Double` but not for `Float`
     * and that leads to awkward handling for NaN and the infinities.
     */
    public open fun valueOf(floatingPoint: Float): T = try {
        valueOf(floatingPoint.toBigDecimal())
    } catch (e: NumberFormatException) {
        throw ArithmeticException("$floatingPoint: ${e.message}")
    }

    public fun valueOf(wholeNumber: BInt): T = valueOf(wholeNumber, 1.big)
    public fun valueOf(wholeNumber: Long): T =
        valueOf(wholeNumber.toBigInteger())

    public fun valueOf(wholeNumber: Int): T =
        valueOf(wholeNumber.toBigInteger())

    internal open fun iteratorCheck(first: T, last: T, step: T) {
        if (step.isZero()) error("Step must be non-zero.")
    }

    /**
     * Constructs a `T` _after_ derived types handle all special cases in
     * `valueOf`.  The most important special case is when [denominator] is
     * `ZERO`  Common behavior for all derived types:
     * - Produces the constant `ZERO` for a numerator of `ZERO`
     * - Transfers the negative sign from the [denominator] to the constructed
     *   numerator
     * - Reduces the constructed numerator and denominator to lowest terms
     * - Produces the constants `ONE`, `TWO`, and `TEN` when the constructed big
     *   rational has those values
     *
     *  **[denominator] may not be `ZERO`**.
     *
     * A typical `valueOf` looks like:
     * ```
     * override fun valueOf(numerator: BInt, denominator: BInt)
     *         : MyBigRationalType {
     *     // Handle special cases for numerator and denominator, such as
     *     // raising exceptions, or producing special values, especially for a
     *     // denominator of ZERO
     *     return construct(numerator, denominator) { n, d ->
     *         MyBigRationalType(n, d)
     *     }
     * }
     * ```
     */
    protected fun reduce(
        numerator: BInt,
        denominator: BInt,
        ctor: (BInt, BInt) -> T,
    ): T {
        require(!denominator.isZero()) {
            "BUG: Internal division by zero"
        }
        if (numerator.isZero()) return ZERO

        var n = numerator
        var d = denominator
        if (-1 == d.signum()) {
            n = n.negate()
            d = d.negate()
        }

        val gcd = n.gcd(d)
        if (!gcd.isUnit()) {
            n /= gcd
            d /= gcd
        }

        if (d.isUnit()) when {
            n.isUnit() -> return ONE
            n.isTwo() -> return TWO
            n.isTen() -> return TEN
        }

        return ctor(n, d)
    }

    /** Generates the Cantor spiral for walking the rationals. */
    public fun cantorSpiral(): Sequence<T> = CantorSpiral(this)
}

public abstract class BigRationalBase<
        T : BigRationalBase<T>
        > protected constructor(
    public val numerator: BInt,
    public val denominator: BInt,
) : Number(), Comparable<T>, Field<T> {
    public abstract override val companion: BigRationalCompanion<T>

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
    ): BigDecimal = BDouble(numerator, limitPlaces)
        .divide(BDouble(denominator, limitPlaces), roundingMode)

    /**
     * Returns this as a [BigInteger] which may involve rounding
     * corresponding to rounding mode [HALF_UP].
     */
    public fun toBigInteger(): BigInteger = numerator / denominator

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
     * @see [BigRationalCompanion.valueOf(Double)]
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

    /** Increments this value by 1. */
    public operator fun inc(): T =
        companion.valueOf(numerator + denominator, denominator)

    /** Decrements this value by 1. */
    public operator fun dec(): T =
        companion.valueOf(numerator - denominator, denominator)

    /** Adds the other value to this value. */
    override operator fun plus(addend: T): T =
        if (denominator == addend.denominator)
            companion.valueOf(numerator + addend.numerator, denominator)
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
     * Returns a the value `(this^exponent)`. Note that [exponent] is an
     * integer rather than a big rational.
     */
    public open fun pow(exponent: Int): T = when {
        0 > exponent -> unaryDiv().pow(-exponent)
        else -> companion.valueOf(
            numerator.pow(exponent),
            denominator.pow(exponent)
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

    /** Checks that this rational is an integer. */
    public fun isInteger(): Boolean = denominator.isUnit()

    /** Rounds to the nearest whole number according to [roundingMode]. */
    @Suppress("UNCHECKED_CAST")
    public open fun round(roundingMode: RoundingMode): T =
        if (isInteger()) this as T
        else companion.valueOf(
            // BigInteger does not have a divide with rounding mode
            numerator.toBigDecimal()
                .divide(denominator.toBigDecimal(), roundingMode)
                .setScale(0)
        )

    /**
     * Checks that this rational is dyadic, that is, the denominator is a power
     * of 2.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Dyadic_rational"><cite>Dyadic rational</cite></a>
     */
    public open fun isDyadic(): Boolean = denominator.isDyadic()

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
    public open fun isPAdic(p: Long): Boolean = denominator.isPAdic(p)

    override fun equals(other: Any?): Boolean = this === other ||
            other is BigRationalBase<*> &&
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

/** Finds the absolute difference between values. */
public fun <T : BigRationalBase<T>> T.diff(other: T): T =
    (this - other).absoluteValue

/** Checks that this rational is 0. */
public fun <T : BigRationalBase<T>> T.isZero(): Boolean =
    companion.ZERO === this

/** Checks that this rational is 1. */
public fun <T : BigRationalBase<T>> T.isUnit(): Boolean = companion.ONE === this

/** Checks that this rational is greater than zero. */
public fun <T : BigRationalBase<T>> T.isPositive(): Boolean = 1 == signum()

/** Checks that this rational is less than zero. */
public fun <T : BigRationalBase<T>> T.isNegative(): Boolean = -1 == signum()

/**
 * Checks that this rational has an even denominator.  The odds of a random
 * rational number having an even denominator is exactly 1/3 (Salamin and
 * Gosper 1972).
 *
 * See [HAKMEM](https://en.wikipedia.org/wiki/HAKMEM).
 */
public fun <T : BigRationalBase<T>> T.isDenominatorEven(): Boolean =
    denominator.isEven()
