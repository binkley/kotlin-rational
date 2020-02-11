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
import java.math.BigInteger
import java.util.Objects.hash

private typealias BInt = BigInteger
private typealias BDouble = BigDecimal

/**
 * Immutable arbitrary-precision rationals (finite fractions).  BigRational
 * provides analogues to all of Kotlin's `Long` operators where appropriate.
 * Additionally, BigRational provides operations for GCD and LCM calculation.
 *
 * Comparison operations perform signed comparisons, analogous to those
 * performed by Kotlin's relational and equality operators.
 *
 * Division by `ZERO` does not raise an `ArithmeticException`; rather, it
 * produces infinities or "not a number".  Infinities and "not a number"
 * propagate where appropriate.
 *
 * Ranges increment by 1 unless otherwise specified.
 *
 * @todo Consider `Short` and `Byte` overloads
 * @todo Assign properties at construction; avoid circular ctors
 */
class BigRational private constructor(
    val numerator: BInt,
    val denominator: BInt
) : Number(), Comparable<BigRational> {
    /**
     * Raises an `IllegalStateException`.  Kotlin provides a `toChar` in its
     * `Number` class; Java does not have a conversion to `Character` for
     * `java.lang.Number`.
     */
    override fun toChar(): Char = error("Characters are non-numeric")

    override fun toByte() = toLong().toByte()
    override fun toShort() = toLong().toShort()
    override fun toInt() = toLong().toInt()

    /** @see [Double.toLong] */
    override fun toLong() = when {
        isNaN() -> 0L
        isPositiveInfinity() -> Long.MAX_VALUE
        isNegativeInfinity() -> Long.MIN_VALUE
        else -> (numerator / denominator).toLong()
    }

    override fun toFloat() = toDouble().toFloat()

    /**
     * Returns the value of this number as a [Double], which may involve
     * rounding.  This should produce an _exact_ conversion, that is,
     * `123.455.toBigRational().toDouble == 123.456`.
     *
     * @see [Double.toLong] which has similar behavior
     */
    override fun toDouble() = when {
        isNaN() -> Double.NaN
        isPositiveInfinity() -> Double.POSITIVE_INFINITY
        isNegativeInfinity() -> Double.NEGATIVE_INFINITY
        else -> numerator.toDouble() / denominator.toDouble()
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
        else -> {
            val a = numerator * other.denominator
            val b = other.numerator * denominator
            a.compareTo(b)
        }
    }

    /**
     * NB -- Infinities and "not a number" are not equal to themselves.
     *
     * @see Any.equals
     */
    override fun equals(other: Any?) = when {
        !isFinite() -> false
        this === other -> true
        other !is BigRational -> false
        else -> numerator == other.numerator &&
                denominator == other.denominator
    }

    override fun hashCode() = hash(numerator, denominator)

    /**
     * Returns a string representation of the object.  In particular:
     * * `NaN` is "NaN"
     * * `POSITIVE_INFINITY` is "+∞" (UNICODE)
     * * `NEGATIVE_INFINITY` is "-∞" (UNICODE)
     * * Finite values are _numerator_/_denominator_
     */
    override fun toString() = when {
        isNaN() -> "NaN"
        isPositiveInfinity() -> "+∞"
        isNegativeInfinity() -> "-∞"
        denominator.isOne() -> numerator.toString()
        else -> "$numerator/$denominator"
    }

    companion object {
        /**
         * A constant holding "not a number" (NaN) value of type
         * `BigRational`. It is equivalent `0 over 0`.
         */
        val NaN = BigRational(BInt.ZERO, BInt.ZERO)

        /**
         * A constant holding 0 value of type `BigRational`. It is equivalent
         * `0 over 1`.
         */
        val ZERO = BigRational(BInt.ZERO, BInt.ONE)

        /**
         * A constant holding 1 value of type `BigRational`. It is equivalent
         * `1 over 1`.
         */
        val ONE = BigRational(BInt.ONE, BInt.ONE)

        /**
         * A constant holding 2 value of type `BigRational`. It is equivalent
         * `2 over 1`.
         */
        val TWO = BigRational(BInt.TWO, BInt.ONE)

        /**
         * A constant holding 10 value of type `BigRational`. It is equivalent
         * `10 over 1`.
         */
        val TEN = BigRational(BInt.TEN, BInt.ONE)

        /**
         * A constant holding positive infinity value of type `BigRational`.
         * It is equivalent `1 over 0`.
         */
        val POSITIVE_INFINITY = BigRational(BInt.ONE, BInt.ZERO)

        /**
         * A constant holding negative infinity value of type `BigRational`.
         * It is equivalent `-1 over 0`.
         */
        val NEGATIVE_INFINITY = BigRational(BInt.ONE.negate(), BInt.ZERO)

        /**
         * Returns a BigRational whose value is equal to that of the
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
        fun valueOf(numerator: BInt, denominator: BInt): BigRational {
            if (denominator.isZero()) return when {
                numerator.isZero() -> NaN
                numerator.signum() == 1 -> POSITIVE_INFINITY
                else -> NEGATIVE_INFINITY
            }
            if (numerator.isZero()) return ZERO
            if (denominator.isOne()) return when {
                numerator.isOne() -> ONE
                numerator.isTwo() -> TWO
                numerator.isTen() -> TEN
                else -> BigRational(numerator, denominator)
            }

            var n = numerator
            var d = denominator
            if (d.signum() == -1) {
                n = n.negate()
                d = d.negate()
            }

            val gcd = n.gcd(d)
            n /= gcd
            d /= gcd

            return BigRational(n, d)
        }
    }
}

/**
 * The signum of this BigRational: -1 for negative, 0 for zero, or
 * 1 for positive.  `sign` of `NaN` is another `NaN`.
 */
val BigRational.sign: BigRational
    get() = when {
        isNaN() -> NaN
        else -> numerator.signum().toBigRational()
    }

/**
 * Returns a BigRational whose value is the absolute value of this
 * BigRational.  `absoluteValue` of `NaN` is another `NaN`.
 */
val BigRational.absoluteValue: BigRational
    get() = valueOf(numerator.abs(), denominator)

/**
 * Returns a BigRational whose value is the reciprocal of this
 * BigRational.  `reciprocal` of `NaN` is another `NaN`.  Reciprocals of
 * infinities are `ZERO`.
 */
val BigRational.reciprocal: BigRational
    get() = valueOf(denominator, numerator)

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: BDouble) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: Double) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: Float) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: BInt) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: Long) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BDouble.over(denominator: Int) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: BDouble) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: BInt) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: Long) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: Int) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: Double) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Double.over(denominator: Float) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: BDouble) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: BInt) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: Long) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: Int) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: Double) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Float.over(denominator: Float) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: BDouble) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: Double) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: Float) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: BInt) = valueOf(this, denominator)

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: Long) =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun BInt.over(denominator: Int) =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: Double) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: Float) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: BDouble) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: BInt) = valueOf(toBigInteger(), denominator)

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: Long) =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Long.over(denominator: Int) =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: BDouble) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: Double) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: Float) =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: BInt) = valueOf(toBigInteger(), denominator)

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: Long) =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a BigRational whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
infix fun Int.over(denominator: Int) =
    valueOf(toBigInteger(), denominator.toBigInteger())

/** Returns the value of this number as a BigRational. */
fun BDouble.toBigRational() = convert(this)

/** Returns the value of this number as a BigRational. */
fun Double.toBigRational() = convert(this)

/** Returns the value of this number as a BigRational. */
fun Float.toBigRational() = toDouble().toBigRational()

/** Returns the value of this number as a BigRational. */
fun BInt.toBigRational() = valueOf(this, BInt.ONE)

/** Returns the value of this number as a BigRational. */
fun Long.toBigRational() = toBigInteger().toBigRational()

/** Returns the value of this number as a BigRational. */
fun Int.toBigRational() = toBigInteger().toBigRational()

/** Returns this value. */
operator fun BigRational.unaryPlus() = this

/** Returns the arithmetic inverse of this value. */
operator fun BigRational.unaryMinus() =
    valueOf(numerator.negate(), denominator)

/** Increments this value by 1 (denominator / denominator). */
operator fun BigRational.inc() = valueOf(numerator + denominator, denominator)

/** Decrements this value by 1 (denominator / denominator). */
operator fun BigRational.dec() = valueOf(numerator - denominator, denominator)

operator fun BigRational.plus(other: BigRational) =
    if (denominator == other.denominator)
        valueOf(numerator + other.numerator, denominator)
    else valueOf(
        numerator * other.denominator + other.numerator * denominator,
        denominator * other.denominator
    )

/** Adds the other value to this value. */
operator fun BigRational.plus(addend: BDouble) = this + addend.toBigRational()

/** Adds the other value to this value yielding a BigRational. */
operator fun BigRational.plus(addend: Double) = this + addend.toBigRational()

/** Adds the other value to this value yielding a BigRational. */
operator fun BigRational.plus(addend: Float) = this + addend.toBigRational()

/** Adds the other value to this value yielding a BigRational. */
operator fun BigRational.plus(addend: BInt) = this + addend.toBigRational()

/** Adds the other value to this value yielding a BigRational. */
operator fun BigRational.plus(addend: Long) = this + addend.toBigRational()

/** Adds the other value to this value yielding a BigRational. */
operator fun BigRational.plus(addend: Int) = this + addend.toBigRational()

/** Subtracts the other value from this value. */
operator fun BigRational.minus(subtrahend: BigRational) = this + -subtrahend

/** Subtracts the other value from this value yielding a BigRational. */
operator fun BigRational.minus(subtrahend: BDouble) =
    this - subtrahend.toBigRational()

/** Subtracts the other value from this value yielding a BigRational. */
operator fun BigRational.minus(subtrahend: Double) =
    this - subtrahend.toBigRational()

/** Subtracts the other value from this value yielding a BigRational. */
operator fun BigRational.minus(subtrahend: Float) =
    this - subtrahend.toBigRational()

/** Subtracts the other value from this value yielding a BigRational. */
operator fun BigRational.minus(subtrahend: BInt) =
    this - subtrahend.toBigRational()

/** Subtracts the other value from this value yielding a BigRational. */
operator fun BigRational.minus(subtrahend: Long) =
    this - subtrahend.toBigRational()

/** Subtracts the other value from this value yielding a BigRational. */
operator fun BigRational.minus(subtrahend: Int) =
    this - subtrahend.toBigRational()

operator fun BigRational.times(other: BigRational) = valueOf(
    numerator * other.numerator,
    denominator * other.denominator
)

/** Multiplies this value by the other value. */
operator fun BigRational.times(multiplicand: BDouble) =
    this * multiplicand.toBigRational()

/** Multiplies this value by the other value yielding a BigRational. */
operator fun BigRational.times(multiplicand: Double) =
    this * multiplicand.toBigRational()

/** Multiplies this value by the other value yielding a BigRational. */
operator fun BigRational.times(multiplicand: Float) =
    this * multiplicand.toBigRational()

/** Multiplies this value by the other value yielding a BigRational. */
operator fun BigRational.times(multiplicand: BInt) =
    this * multiplicand.toBigRational()

/** Multiplies this value by the other value yielding a BigRational. */
operator fun BigRational.times(multiplicand: Long) =
    this * multiplicand.toBigRational()

/** Multiplies this value by the other value yielding a BigRational. */
operator fun BigRational.times(multiplicand: Int) =
    this * multiplicand.toBigRational()

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.div(divisor: BigRational) = this * divisor.reciprocal

/**
 * Divides this value by the other value exactly yielding a BigRational.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.div(divisor: BDouble) =
    this / divisor.toBigRational()

/**
 * Divides this value by the other value exactly yielding a BigRational.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.div(divisor: Double) = this / divisor.toBigRational()

/**
 * Divides this value by the other value exactly yielding a BigRational.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.div(divisor: Float) = this / divisor.toBigRational()

/**
 * Divides this value by the other value exactly yielding a BigRational.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.div(divisor: BInt) = this / divisor.toBigRational()

/**
 * Divides this value by the other value exactly yielding a BigRational.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.div(divisor: Long) = this / divisor.toBigRational()

/**
 * Divides this value by the other value exactly yielding a BigRational.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.div(divisor: Int) = this / divisor.toBigRational()

/**
 * Modulos this value by the other value; always 0 (division is exact).
 *
 * @see [divideAndRemainder] */
operator fun BigRational.rem(divisor: BigRational) = when {
    isNaN() || divisor.isNaN() -> NaN
    else -> ZERO
}

/**
 * Finds the remainder of this value by other: always 0 (division is
 * exact), or not a number if either value is `NaN`.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.rem(divisor: BDouble) =
    this % divisor.toBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is
 * exact), or not a number if either value is `NaN`.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.rem(divisor: Double) = this % divisor.toBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is
 * exact), or not a number if either value is `NaN`.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.rem(divisor: Float) = this % divisor.toBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is
 * exact), or not a number if either value is `NaN`.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.rem(divisor: BInt) = this % divisor.toBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is
 * exact), or not a number if either value is `NaN`.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.rem(divisor: Long) = this % divisor.toBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is
 * exact), or not a number if either value is `NaN`.
 *
 * @see [divideAndRemainder] */
operator fun BigRational.rem(divisor: Int) = this % divisor.toBigRational()

/** Creates a range from this value to the specified [other] value. */
operator fun BigRational.rangeTo(other: BigRational) =
    BigRationalProgression(this, other)

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

sealed class BigRationalIterator(
    first: BigRational,
    protected val last: BigRational,
    private val step: BigRational
) : Iterator<BigRational> {
    init {
        if (!step.isFinite()) error("Non-finite step.")
        if (!first.isFinite() || !last.isFinite())
            error("Non-finite bounds.")
        if (step == ZERO) error("Step must be non-zero.")
    }

    protected var current = first

    override fun next(): BigRational {
        val next = current
        current += step
        return next
    }
}

class IncrementingBigRationalIterator(
    /** The first element in the progression. */
    first: BigRational,
    /** The last element in the progression. */
    last: BigRational,
    step: BigRational
) : BigRationalIterator(first, last, step) {
    init {
        if (first > last)
            error("Step must be advance range to avoid overflow.")
    }

    override fun hasNext() = current <= last
}

class DecrementingBigRationalIterator(
    /** The first element in the progression. */
    first: BigRational,
    /** The last element in the progression. */
    last: BigRational,
    step: BigRational
) : BigRationalIterator(first, last, step) {
    init {
        if (first < last)
            error("Step must be advance range to avoid overflow.")
    }

    override fun hasNext() = current >= last
}

class BigRationalProgression(
    override val start: BigRational,
    override val endInclusive: BigRational,
    step: BigRational = ONE
) : SteppedBigRationalProgression(start, endInclusive, step) {
    infix fun step(step: BigRational) =
        SteppedBigRationalProgression(start, endInclusive, step)

    infix fun step(step: BInt) =
        SteppedBigRationalProgression(start, endInclusive, step over 1)

    infix fun step(step: Long) =
        SteppedBigRationalProgression(start, endInclusive, step over 1)

    infix fun step(step: Int) =
        SteppedBigRationalProgression(start, endInclusive, step over 1)
}

open class SteppedBigRationalProgression(
    override val start: BigRational,
    override val endInclusive: BigRational,
    private val step: BigRational
) : Iterable<BigRational>, ClosedRange<BigRational> {
    override fun iterator() =
        if (step < ZERO)
            DecrementingBigRationalIterator(start, endInclusive, step)
        else
            IncrementingBigRationalIterator(start, endInclusive, step)

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is SteppedBigRationalProgression -> false
        else -> start == other.start &&
                endInclusive == other.endInclusive &&
                step == other.step
    }

    override fun hashCode() = hash(start, endInclusive, step)

    override fun toString() =
        if (step < ZERO) "$start downTo $endInclusive step $step"
        else "$start..$endInclusive step $step"
}

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
 * rational back to a `Double` should produce the original value.
 */
private fun convert(d: Double) = when {
    d == 0.0 -> ZERO
    d == 1.0 -> ONE
    d.isNaN() -> NaN
    d.isInfinite() -> if (d < 0.0) NEGATIVE_INFINITY else POSITIVE_INFINITY
    d < 0 -> -TWO.pow(exponent(d)) * factor(d)
    else -> TWO.pow(exponent(d)) * factor(d)
}

private fun exponent(d: Double) =
    ((d.toBits() shr 52).toInt() and 0x7ff) - 1023

private fun factor(other: Double): BigRational {
    val denominator = 1L shl 52
    val numerator = mantissa(other) + denominator

    return valueOf(numerator.toBigInteger(), denominator.toBigInteger())
}

private fun mantissa(d: Double) = d.toBits() and 0xfffffffffffffL

private fun BInt.isZero() = this == BInt.ZERO
private fun BInt.isOne() = this == BInt.ONE
private fun BInt.isTwo() = this == BInt.TWO
private fun BInt.isTen() = this == BInt.TEN

/**
 * Returns a pair of `this / other` (quotient) and `this % other`
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
 * Returns a BigRational whose value is `(this^exponent)`. Note that
 * `exponent` is an integer rather than a BigRational (and the underlying
 * BigInteger does not support `BInt.pow(BInt)`).
 */
fun BigRational.pow(exponent: Int): BigRational /* type check issue */ =
    when {
        0 <= exponent ->
            valueOf(numerator.pow(exponent), denominator.pow(exponent))
        else -> reciprocal.pow(-exponent)
    }

/**
 * Returns a BigRational whose value is the greatest common divisor of
 * the absolute values of `this` and `other`.  Returns 0 when `this` and
 * `other` are both 0.
 */
fun BigRational.gcd(other: BigRational) =
    if (ZERO == this) other else valueOf(
        numerator.gcd(other.numerator),
        denominator.lcm(other.denominator)
    )

/**
 * Returns a BigRational whose value is the lowest common multiple of
 * the absolute values of `this` and `other`.  Returns 1 when `this` and
 * `other` are both 0.
 */
fun BigRational.lcm(other: BigRational) =
    if (ZERO == this) ZERO else valueOf(
        numerator.lcm(other.numerator),
        denominator.gcd(other.denominator)
    )

private fun BInt.lcm(other: BInt) = (this * (other / gcd(other))).abs()

/**
 * Rounds to the nearest whole number _less than or equal_ to this
 * BigRational.  Non-finite values return themselves.
 */
fun BigRational.floor() = when {
    roundsToSelf() -> this
    ZERO <= this -> round()
    else -> round() - ONE
}

/**
 * Rounds to the nearest whole number _greater than or equal_ to this
 * BigRational.  Non-finite values return themselves.
 */
fun BigRational.ceil() = when {
    roundsToSelf() -> this
    ZERO <= this -> round() + ONE
    else -> round()
}

/**
 * Rounds to the nearest whole number _closer to 0_ than this BigRational,
 * or when this BigRational is whole, the same BigRational.  Non-finite values
 * return themselves.
 */
fun BigRational.round() = when {
    roundsToSelf() -> this
    else -> (numerator / denominator).toBigRational()
}

private fun BigRational.roundsToSelf() = isInteger() || !isFinite()

/**
 * Returns a BigRational between this BigRational and the other one, or
 * `NaN` if this or the other BigRational are `NaN` or the same.
 *
 * If `a/b` and `c/d` are rational numbers such that `a/b ≠ c/d` or, then
 * this function returns `(a+c)/(b+d)` (order of `this` and `other` does
 * not matter).  When `a/b = c/d`, returns `NaN`.
 */
fun BigRational.between(other: BigRational) = when {
    equals(other) -> NaN
    isNaN() || other.isNaN() -> NaN
    (isPositiveInfinity() && other.isNegativeInfinity())
            || (isNegativeInfinity() && other.isPositiveInfinity()) -> ZERO
    else -> valueOf(
        numerator + other.numerator,
        denominator + other.denominator
    )
}

/**
 * Returns the finite continued fraction of this BigRational.
 *
 * Non-finite BigRationals produce `[NaN;]`.
 */
fun BigRational.toContinuedFraction() = ContinuedFraction.valueOf(this)

/**
 * Checks that this rational is a finite fraction.  Infinities and "not a
 * number" are not finite.
 *
 * @todo Consider separate types, which leads to sealed types
 */
fun BigRational.isFinite() = !isNaN() && !isInfinite()

/** Checks that this rational is an integer. */
fun BigRational.isInteger() = BInt.ONE == denominator

/**
 * Checks that this rational is dyadic, that is, the denominator is a power
 * of 2.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Dyadic_rational"><cite>Dyadic rational</cite></a>
 */
fun BigRational.isDyadic() = isFinite() &&
        (denominator.isOne() ||
                (denominator % BInt.TWO).isZero())

/**
 * Checks that this rational has an even denominator.  The odds of a random
 * rational number having an even denominator is exactly 1/3 (Salamin and
 * Gosper 1972).
 */
fun BigRational.isDenominatorEven() = denominator.isEven()

private fun BInt.isEven() = BInt.ZERO == this % BInt.TWO

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
