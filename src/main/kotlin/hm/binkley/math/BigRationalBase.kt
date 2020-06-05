package hm.binkley.math

import java.math.BigDecimal
import java.util.Objects.hash

/**
 * @todo Provide`sqrt` via continued fractions, ie,
 *       https://en.wikipedia.org/wiki/Continued_fraction#Square_roots
 */
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

    /**
     * Since the conversion to a rational is _exact_, converting the resulting
     * rational back to a [BigDecimal] produces the original value.
     */
    fun valueOf(floatingPoint: BDouble): T = when (floatingPoint) {
        BDouble.ZERO -> ZERO
        BDouble.ONE -> ONE
        BDouble.TEN -> TEN
        else -> {
            val bd = floatingPoint.stripTrailingZeros()
            val scale = bd.scale()
            val unscaledValue = bd.unscaledValue()
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
    fun valueOf(floatingPoint: Double) = when {
        floatingPoint == 0.0 -> ZERO
        floatingPoint == 1.0 -> ONE
        floatingPoint == 2.0 -> TWO
        // Not 10.0 -- decimal rounding to floating point is tricksy
        floatingPoint < 0 -> -TWO.pow(exponent(floatingPoint)) * factor(
            floatingPoint
        )
        else -> TWO.pow(exponent(floatingPoint)) * factor(floatingPoint)
    }

    private fun factor(other: Double): T {
        val denominator = 1L shl 52
        val numerator = mantissa(other) + denominator

        return valueOf(numerator.toBigInteger(), denominator.toBigInteger())
    }

    fun valueOf(floatingPoint: Float) = valueOf(floatingPoint.toDouble())
    fun valueOf(wholeNumber: BInt) = valueOf(wholeNumber, BInt.ONE)
    fun valueOf(wholeNumber: Long) = valueOf(wholeNumber.toBigInteger())
    fun valueOf(wholeNumber: Int) = valueOf(wholeNumber.toBigInteger())

    fun iteratorCheck(first: T, last: T, step: T)

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
     * `123.456.toBigRational().toDouble == 123.456`.
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
     * The signum of this value: -1 for negative, 0 for zero, or 1 for
     * positive.
     */
    open val sign: T
        get() = companion.valueOf(numerator.signum())

    /**
     * Finds the remainder of this value by [divisor]: always 0 (division is
     * exact).
     *
     * @see [divideAndRemainder]
     */
    @Suppress("UNUSED_PARAMETER")
    open operator fun rem(divisor: T) = companion.ZERO

    /**
     * Returns the Farey value between this FiniteBigRational and [that], or
     * the same value when equal.
     *
     * If `a/b` and `c/d` are rational numbers such that `a/b ≠ c/d`, then
     * this function returns `(a+c)/(b+d)` (order of `this` and [that] does
     * not matter).
     */
    open fun mediant(that: T) = companion.valueOf(
        numerator + that.numerator,
        denominator + that.denominator
    )

    /** Checks that this rational is an integer. */
    fun isInteger() = BInt.ONE == denominator

    internal open fun roundsToSelf() = isInteger()

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
            // TODO: Kotlin smart casting needs this, but is redundant
            other is BigRationalBase<*> &&
            javaClass == other.javaClass &&
            numerator == other.numerator &&
            denominator == other.denominator

    override fun hashCode() = hash(javaClass, numerator, denominator)

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
    get() =
        if (numerator >= BInt.ZERO) this
        else companion.valueOf(numerator.abs(), denominator)

/** Returns the reciprocal. */
val <T : BigRationalBase<T>> T.reciprocal: T
    get() = companion.valueOf(denominator, numerator)

/**
 * Compares this value to the other.
 *
 * @see [BigRationalBase.compareTo]
 */
operator fun <T : BigRationalBase<T>> T.compareTo(other: BDouble) =
    compareTo(companion.valueOf(other))

/**
 * Compares this value to the other.
 *
 * @see [BigRationalBase.compareTo]
 */
operator fun <T : BigRationalBase<T>> BDouble.compareTo(other: T) =
    other.companion.valueOf(this).compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [BigRationalBase.compareTo]
 */
operator fun <T : BigRationalBase<T>> T.compareTo(other: Double) =
    compareTo(companion.valueOf(other))

/**
 * Compares this value to the other.
 *
 * @see [BigRationalBase.compareTo]
 */
operator fun <T : BigRationalBase<T>> Double.compareTo(other: T) =
    other.companion.valueOf(this).compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [BigRationalBase.compareTo]
 */
operator fun <T : BigRationalBase<T>> T.compareTo(other: Float) =
    compareTo(companion.valueOf(other))

/**
 * Compares this value to the other.
 *
 * @see [BigRationalBase.compareTo]
 */
operator fun <T : BigRationalBase<T>> Float.compareTo(other: T) =
    other.companion.valueOf(this).compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [BigRationalBase.compareTo]
 */
operator fun <T : BigRationalBase<T>> T.compareTo(other: BInt) =
    compareTo(companion.valueOf(other))

/**
 * Compares this value to the other.
 *
 * @see [BigRationalBase.compareTo]
 */
operator fun <T : BigRationalBase<T>> BInt.compareTo(other: T) =
    other.companion.valueOf(this).compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [BigRationalBase.compareTo]
 */
operator fun <T : BigRationalBase<T>> T.compareTo(other: Long) =
    compareTo(companion.valueOf(other))

/**
 * Compares this value to the other.
 *
 * @see [BigRationalBase.compareTo]
 */
operator fun <T : BigRationalBase<T>> Long.compareTo(other: T) =
    other.companion.valueOf(this).compareTo(other)

/**
 * Compares this value to the other.
 *
 * @see [BigRationalBase.compareTo]
 */
operator fun <T : BigRationalBase<T>> T.compareTo(other: Int) =
    compareTo(companion.valueOf(other))

/**
 * Compares this value to the other.
 *
 * @see [BigRationalBase.compareTo]
 */
operator fun <T : BigRationalBase<T>> Int.compareTo(other: T) =
    other.companion.valueOf(this).compareTo(other)

/** Returns this value. */
operator fun <T : BigRationalBase<T>> T.unaryPlus() = this

/** Returns the arithmetic inverse of this value. */
operator fun <T : BigRationalBase<T>> T.unaryMinus(): T =
    companion.valueOf(numerator.negate(), denominator)

/** Increments this value by 1. */
operator fun <T : BigRationalBase<T>> T.inc() =
    companion.valueOf(numerator + denominator, denominator)

/** Decrements this value by 1. */
operator fun <T : BigRationalBase<T>> T.dec() =
    companion.valueOf(numerator - denominator, denominator)

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> T.plus(addend: T) =
    if (denominator == addend.denominator)
        companion.valueOf(numerator + addend.numerator, denominator)
    else companion.valueOf(
        numerator * addend.denominator + addend.numerator * denominator,
        denominator * addend.denominator
    )

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> T.plus(addend: BDouble) =
    this + companion.valueOf(addend)

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> T.plus(addend: Double) =
    this + companion.valueOf(addend)

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> T.plus(addend: Float) =
    this + companion.valueOf(addend)

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> T.plus(addend: BInt) =
    this + companion.valueOf(addend)

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> T.plus(addend: Long) =
    this + companion.valueOf(addend)

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> T.plus(addend: Int) =
    this + companion.valueOf(addend)

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> BDouble.plus(addend: T) =
    addend.companion.valueOf(this) + addend

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> Double.plus(addend: T) =
    addend.companion.valueOf(this) + addend

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> Float.plus(addend: T) =
    addend.companion.valueOf(this) + addend

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> BInt.plus(addend: T) =
    addend.companion.valueOf(this) + addend

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> Long.plus(addend: T) =
    addend.companion.valueOf(this) + addend

/** Adds the other value to this value. */
operator fun <T : BigRationalBase<T>> Int.plus(addend: T) =
    addend.companion.valueOf(this) + addend

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> T.minus(subtrahend: T) =
    this + -subtrahend

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> T.minus(subtrahend: BDouble) =
    this - companion.valueOf(subtrahend)

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> T.minus(subtrahend: Double) =
    this - companion.valueOf(subtrahend)

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> T.minus(subtrahend: Float) =
    this - companion.valueOf(subtrahend)

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> T.minus(subtrahend: BInt) =
    this - companion.valueOf(subtrahend)

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> T.minus(subtrahend: Long) =
    this - companion.valueOf(subtrahend)

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> T.minus(subtrahend: Int) =
    this - companion.valueOf(subtrahend)

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> BDouble.minus(subtrahend: T) =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> Double.minus(subtrahend: T) =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> Float.minus(subtrahend: T) =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> BInt.minus(subtrahend: T) =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> Long.minus(subtrahend: T) =
    subtrahend.companion.valueOf(this) - subtrahend

/** Subtracts the other value from this value. */
operator fun <T : BigRationalBase<T>> Int.minus(subtrahend: T) =
    subtrahend.companion.valueOf(this) - subtrahend

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> T.times(multiplicand: T): T =
    companion.valueOf(
        numerator * multiplicand.numerator,
        denominator * multiplicand.denominator
    )

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> T.times(multiplicand: BDouble) =
    this * companion.valueOf(multiplicand)

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> T.times(multiplicand: Double) =
    this * companion.valueOf(multiplicand)

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> T.times(multiplicand: Float) =
    this * companion.valueOf(multiplicand)

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> T.times(multiplicand: BInt) =
    this * companion.valueOf(multiplicand)

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> T.times(multiplicand: Long) =
    this * companion.valueOf(multiplicand)

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> T.times(multiplicand: Int) =
    this * companion.valueOf(multiplicand)

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> BDouble.times(multiplicand: T) =
    multiplicand.companion.valueOf(this) * multiplicand

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> Double.times(multiplicand: T) =
    multiplicand.companion.valueOf(this) * multiplicand

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> Float.times(multiplicand: T) =
    multiplicand.companion.valueOf(this) * multiplicand

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> BInt.times(multiplicand: T) =
    multiplicand.companion.valueOf(this) * multiplicand

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> Long.times(multiplicand: T) =
    multiplicand.companion.valueOf(this) * multiplicand

/** Multiplies this value by the other value. */
operator fun <T : BigRationalBase<T>> Int.times(multiplicand: T) =
    multiplicand.companion.valueOf(this) * multiplicand

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.div(divisor: T) =
    this * divisor.reciprocal

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.div(divisor: BDouble) =
    this / companion.valueOf(divisor)

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.div(divisor: Double) =
    this / companion.valueOf(divisor)

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.div(divisor: Float) =
    this / companion.valueOf(divisor)

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.div(divisor: BInt) =
    this / companion.valueOf(divisor)

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.div(divisor: Long) =
    this / companion.valueOf(divisor)

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.div(divisor: Int) =
    this / companion.valueOf(divisor)

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> BDouble.div(divisor: T) =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> Double.div(divisor: T) =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> Float.div(divisor: T) =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> BInt.div(divisor: T) =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> Long.div(divisor: T) =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> Int.div(divisor: T) =
    divisor.companion.valueOf(this) / divisor

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.rem(divisor: BDouble) =
    this % companion.valueOf(divisor)

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.rem(divisor: Double) =
    this % companion.valueOf(divisor)

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.rem(divisor: Float) =
    this % companion.valueOf(divisor)

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.rem(divisor: BInt) =
    this % companion.valueOf(divisor)

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.rem(divisor: Long) =
    this % companion.valueOf(divisor)

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun <T : BigRationalBase<T>> T.rem(divisor: Int) =
    this % companion.valueOf(divisor)

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
operator fun <T : BigRationalBase<T>> BDouble.rem(other: T) =
    other.companion.ZERO

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
operator fun <T : BigRationalBase<T>> Double.rem(other: T) =
    other.companion.ZERO

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
operator fun <T : BigRationalBase<T>> Float.rem(other: T) =
    other.companion.ZERO

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
operator fun <T : BigRationalBase<T>> BInt.rem(other: T) =
    other.companion.ZERO

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
operator fun <T : BigRationalBase<T>> Long.rem(other: T) =
    other.companion.ZERO

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
operator fun <T : BigRationalBase<T>> Int.rem(other: T) =
    other.companion.ZERO

/**
 * Returns the pair of `this / other` (quotient) and `this % other`
 * (remainder) integral division and modulo operations.
 *
 * @see [div]
 */
fun <T : BigRationalBase<T>> T.divideAndRemainder(that: T): Pair<T, T> {
    val quotient = (this / that).round()
    val remainder = this - that * quotient

    return quotient to remainder
}

/**
 * Returns a the value `(this^exponent)`. Note that [exponent] is an integer
 * rather than a big rational.
 */
fun <T : BigRationalBase<T>> T.pow(exponent: Int): T = when {
    0 <= exponent ->
        companion.valueOf(
            numerator.pow(exponent),
            denominator.pow(exponent)
        )
    else -> reciprocal.pow(-exponent)
}

/**
 * Returns the rational square root.
 *
 * @throws ArithmeticException if the square root is non-rational
 */
fun <T : BigRationalBase<T>> T.sqrt(): T {
    val n = numerator.sqrt()
    if (numerator != n * n)
        throw ArithmeticException("No rational square root: $this")
    val d = denominator.sqrt()
    if (denominator != d * d)
        throw ArithmeticException("No rational square root: $this")
    return companion.valueOf(n, d)
}

/** Rounds to the nearest whole number _less than or equal_ to this. */
fun <T : BigRationalBase<T>> T.floor() = when {
    roundsToSelf() -> this
    companion.ZERO <= this -> round()
    else -> round() - companion.ONE
}

/** Rounds to the nearest whole number _greater than or equal_ to this. */
fun <T : BigRationalBase<T>> T.ceil() = when {
    roundsToSelf() -> this
    companion.ZERO <= this -> round() + companion.ONE
    else -> round()
}

/**
 * Rounds to the nearest whole number _closer to 0_ than this BigRational,
 * or when this is a whole number, returns this.
 */
fun <T : BigRationalBase<T>> T.round() = when {
    roundsToSelf() -> this
    else -> companion.valueOf(numerator / denominator)
}

/**
 * Returns the greatest common divisor of the absolute values of `this` and
 * [that].  Returns 0 when `this` and [that] are both 0.
 */
fun <T : BigRationalBase<T>> T.gcd(that: T) =
    if (isZero()) that else companion.valueOf(
        numerator.gcd(that.numerator),
        denominator.lcm(that.denominator)
    )

/**
 * Returns the lowest common multiple of the absolute values of `this` and
 * [that].  Returns 0 when `this` and [that] are both 0.
 */
fun <T : BigRationalBase<T>> T.lcm(that: T) =
    if (isZero()) companion.ZERO else companion.valueOf(
        numerator.lcm(that.numerator),
        denominator.gcd(that.denominator)
    )

/** Checks that this rational is 0. */
fun <T : BigRationalBase<T>> T.isZero() = companion.ZERO === this

/**
 * Checks that this rational has an even denominator.  The odds of a random
 * rational number having an even denominator is exactly 1/3 (Salamin and
 * Gosper 1972).
 */
fun <T : BigRationalBase<T>> T.isDenominatorEven() = denominator.isEven()
