package hm.binkley.math.finite

import hm.binkley.math.finite.FiniteBigRational.Companion.ONE
import hm.binkley.math.finite.FiniteBigRational.Companion.TEN
import hm.binkley.math.finite.FiniteBigRational.Companion.TWO
import hm.binkley.math.finite.FiniteBigRational.Companion.ZERO
import hm.binkley.math.finite.FiniteBigRational.Companion.valueOf
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Objects.hash

internal typealias BInt = BigInteger
internal typealias BDouble = BigDecimal

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
    val numerator: BInt,
    val denominator: BInt
) : Number(), Comparable<FiniteBigRational> {
    /**
     * Raises an [IllegalStateException].  Kotlin provides a [Number.toChar];
     * Java does not have a conversion to [Character] for [java.lang.Number].
     */
    override fun toChar(): Char = error("Characters are non-numeric")

    override fun toByte() = toLong().toByte()
    override fun toShort() = toLong().toShort()
    override fun toInt() = toLong().toInt()

    /** @see [Double.toLong] */
    override fun toLong() = (numerator / denominator).toLong()

    override fun toFloat() = toDouble().toFloat()

    /**
     * Returns the value of this number as a [Double], which may involve
     * rounding.  This produces an _exact_ conversion, that is,
     * `123.455.toFiniteBigRational().toDouble == 123.456`.
     *
     * @see [Double.toLong] which has similar behavior
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
    override fun compareTo(other: FiniteBigRational) = when {
        this === other -> 0 // Sort stability for constants
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
    override fun equals(other: Any?) = this === other ||
            other is FiniteBigRational &&
            numerator == other.numerator &&
            denominator == other.denominator

    override fun hashCode() = hash(numerator, denominator)

    /**
     * Returns a string representation of the object.  In particular:
     * * Finite values are [numerator]/[denominator]
     */
    override fun toString() = when {
        denominator.isOne() -> numerator.toString()
        else -> "$numerator⁄$denominator" // UNICODE fraction slash
    }

    companion object {
        /**
         * A constant holding 0 value of type [FiniteBigRational]. It is
         * equivalent `0 over 1`.
         */
        val ZERO = FiniteBigRational(BInt.ZERO, BInt.ONE)

        /**
         * A constant holding 1 value of type [FiniteBigRational]. It is
         * equivalent `1 over 1`.
         */
        val ONE = FiniteBigRational(BInt.ONE, BInt.ONE)

        /**
         * A constant holding 2 value of type [FiniteBigRational]. It is
         * equivalent `2 over 1`.
         */
        val TWO = FiniteBigRational(BInt.TWO, BInt.ONE)

        /**
         * A constant holding 10 value of type [FiniteBigRational]. It is
         * equivalent `10 over 1`.
         */
        val TEN = FiniteBigRational(BInt.TEN, BInt.ONE)

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
        fun valueOf(numerator: BInt, denominator: BInt): FiniteBigRational {
            if (denominator.isZero())
                throw ArithmeticException("division by zero")
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
                else -> FiniteBigRational(n, d)
            }

            val gcd = n.gcd(d)
            if (!gcd.isOne()) {
                n /= gcd
                d /= gcd
            }

            return FiniteBigRational(n, d)
        }
    }
}

/**
 * The signum of this FiniteBigRational: -1 for negative, 0 for zero, or
 * 1 for positive.
 */
val FiniteBigRational.sign: FiniteBigRational
    get() = numerator.signum().toFiniteBigRational()

/**
 * Returns a `FiniteBigRational` whose value is the absolute value of this
 * FiniteBigRational.
 */
val FiniteBigRational.absoluteValue: FiniteBigRational
    get() = valueOf(numerator.abs(), denominator)

/**
 * Returns a `FiniteBigRational` whose value is the reciprocal of this
 * `FiniteBigRational`.  Reciprocals throw [ArithmeticException].
 */
val FiniteBigRational.reciprocal: FiniteBigRational
    get() = valueOf(denominator, numerator)

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
fun BInt.toFiniteBigRational() = valueOf(this, BInt.ONE)

/** Returns the value of this number as a `FiniteBigRational`. */
fun Long.toFiniteBigRational() = toBigInteger().toFiniteBigRational()

/** Returns the value of this number as a `FiniteBigRational`. */
fun Int.toFiniteBigRational() = toBigInteger().toFiniteBigRational()

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

/** Returns this value. */
operator fun FiniteBigRational.unaryPlus() = this

/** Returns the arithmetic inverse of this value. */
operator fun FiniteBigRational.unaryMinus() =
    valueOf(numerator.negate(), denominator)

/** Increments this value by 1 (denominator / denominator). */
operator fun FiniteBigRational.inc() =
    valueOf(numerator + denominator, denominator)

/** Decrements this value by 1 (denominator / denominator). */
operator fun FiniteBigRational.dec() =
    valueOf(numerator - denominator, denominator)

operator fun FiniteBigRational.plus(other: FiniteBigRational) =
    if (denominator == other.denominator)
        valueOf(numerator + other.numerator, denominator)
    else valueOf(
        numerator * other.denominator + other.numerator * denominator,
        denominator * other.denominator
    )

/** Adds the other value to this value. */
operator fun FiniteBigRational.plus(addend: BDouble) =
    this + addend.toFiniteBigRational()

/** Adds the other value to this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.plus(addend: Double) =
    this + addend.toFiniteBigRational()

/** Adds the other value to this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.plus(addend: Float) =
    this + addend.toFiniteBigRational()

/** Adds the other value to this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.plus(addend: BInt) =
    this + addend.toFiniteBigRational()

/** Adds the other value to this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.plus(addend: Long) =
    this + addend.toFiniteBigRational()

/** Adds the other value to this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.plus(addend: Int) =
    this + addend.toFiniteBigRational()

/** Subtracts the other value from this value. */
operator fun FiniteBigRational.minus(subtrahend: FiniteBigRational) =
    this + -subtrahend

/** Subtracts the other value from this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.minus(subtrahend: BDouble) =
    this - subtrahend.toFiniteBigRational()

/** Subtracts the other value from this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.minus(subtrahend: Double) =
    this - subtrahend.toFiniteBigRational()

/** Subtracts the other value from this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.minus(subtrahend: Float) =
    this - subtrahend.toFiniteBigRational()

/** Subtracts the other value from this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.minus(subtrahend: BInt) =
    this - subtrahend.toFiniteBigRational()

/** Subtracts the other value from this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.minus(subtrahend: Long) =
    this - subtrahend.toFiniteBigRational()

/** Subtracts the other value from this value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.minus(subtrahend: Int) =
    this - subtrahend.toFiniteBigRational()

operator fun FiniteBigRational.times(other: FiniteBigRational) = valueOf(
    numerator * other.numerator,
    denominator * other.denominator
)

/** Multiplies this value by the other value. */
operator fun FiniteBigRational.times(multiplicand: BDouble) =
    this * multiplicand.toFiniteBigRational()

/** Multiplies this value by the other value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.times(multiplicand: Double) =
    this * multiplicand.toFiniteBigRational()

/** Multiplies this value by the other value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.times(multiplicand: Float) =
    this * multiplicand.toFiniteBigRational()

/** Multiplies this value by the other value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.times(multiplicand: BInt) =
    this * multiplicand.toFiniteBigRational()

/** Multiplies this value by the other value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.times(multiplicand: Long) =
    this * multiplicand.toFiniteBigRational()

/** Multiplies this value by the other value yielding a `FiniteBigRational`. */
operator fun FiniteBigRational.times(multiplicand: Int) =
    this * multiplicand.toFiniteBigRational()

/**
 * Divides this value by the other value exactly.
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.div(divisor: FiniteBigRational) =
    this * divisor.reciprocal

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
 * Divides this value by the other value exactly yielding a FiniteBigRational.
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.div(divisor: BInt) =
    this / divisor.toFiniteBigRational()

/**
 * Divides this value by the other value exactly yielding a FiniteBigRational.
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.div(divisor: Long) =
    this / divisor.toFiniteBigRational()

/**
 * Divides this value by the other value exactly yielding a FiniteBigRational.
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.div(divisor: Int) =
    this / divisor.toFiniteBigRational()

/**
 * Modulos this value by the other value; always 0 (division is exact).
 *
 * @see [divideAndRemainder]
 */
@Suppress("UNUSED_PARAMETER")
operator fun FiniteBigRational.rem(divisor: FiniteBigRational) = ZERO

/**
 * Finds the remainder of this value by other: always 0 (division is
 * exact), or throws [ArithmeticException].
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.rem(divisor: BDouble) =
    this % divisor.toFiniteBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is
 * exact), or throws [ArithmeticException].
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.rem(divisor: Double) =
    this % divisor.toFiniteBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is
 * exact), or throws [ArithmeticException].
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.rem(divisor: Float) =
    this % divisor.toFiniteBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is
 * exact), or throws [ArithmeticException].
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.rem(divisor: BInt) =
    this % divisor.toFiniteBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is
 * exact), or throws [ArithmeticException].
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.rem(divisor: Long) =
    this % divisor.toFiniteBigRational()

/**
 * Finds the remainder of this value by other: always 0 (division is
 * exact), or throws [ArithmeticException].
 *
 * @see [divideAndRemainder]
 */
operator fun FiniteBigRational.rem(divisor: Int) =
    this % divisor.toFiniteBigRational()

/** Creates a range from this value to the specified [other] value. */
operator fun FiniteBigRational.rangeTo(other: FiniteBigRational) =
    FiniteBigRationalProgression(this, other)

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

sealed class FiniteBigRationalIterator(
    first: FiniteBigRational,
    protected val last: FiniteBigRational,
    private val step: FiniteBigRational
) : Iterator<FiniteBigRational> {
    init {
        if (step == ZERO) error("Step must be non-zero.")
    }

    protected var current = first

    override fun next(): FiniteBigRational {
        val next = current
        current += step
        return next
    }
}

class IncrementingFiniteBigRationalIterator(
    /** The first element in the progression. */
    first: FiniteBigRational,
    /** The last element in the progression. */
    last: FiniteBigRational,
    step: FiniteBigRational
) : FiniteBigRationalIterator(first, last, step) {
    init {
        if (first > last)
            error("Step must be advance range to avoid overflow.")
    }

    override fun hasNext() = current <= last
}

class DecrementingFiniteBigRationalIterator(
    /** The first element in the progression. */
    first: FiniteBigRational,
    /** The last element in the progression. */
    last: FiniteBigRational,
    step: FiniteBigRational
) : FiniteBigRationalIterator(first, last, step) {
    init {
        if (first < last)
            error("Step must be advance range to avoid overflow.")
    }

    override fun hasNext() = current >= last
}

class FiniteBigRationalProgression(
    override val start: FiniteBigRational,
    override val endInclusive: FiniteBigRational,
    step: FiniteBigRational = ONE
) : SteppedFiniteBigRationalProgression(start, endInclusive, step) {
    infix fun step(step: FiniteBigRational) =
        SteppedFiniteBigRationalProgression(start, endInclusive, step)

    infix fun step(step: BInt) =
        SteppedFiniteBigRationalProgression(start, endInclusive, step over 1)

    infix fun step(step: Long) =
        SteppedFiniteBigRationalProgression(start, endInclusive, step over 1)

    infix fun step(step: Int) =
        SteppedFiniteBigRationalProgression(start, endInclusive, step over 1)
}

open class SteppedFiniteBigRationalProgression(
    override val start: FiniteBigRational,
    override val endInclusive: FiniteBigRational,
    private val step: FiniteBigRational
) : Iterable<FiniteBigRational>, ClosedRange<FiniteBigRational> {
    override fun iterator() =
        if (step < ZERO)
            DecrementingFiniteBigRationalIterator(start, endInclusive, step)
        else
            IncrementingFiniteBigRationalIterator(start, endInclusive, step)

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is SteppedFiniteBigRationalProgression -> false
        else -> start == other.start &&
                endInclusive == other.endInclusive &&
                step == other.step
    }

    override fun hashCode() = hash(start, endInclusive, step)

    override fun toString() =
        if (step < ZERO) "$start downTo $endInclusive step $step"
        else "$start..$endInclusive step $step"
}

infix fun FiniteBigRational.downTo(other: FiniteBigRational) =
    FiniteBigRationalProgression(this, other, -ONE)

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
private fun convert(d: Double) = when {
    d == 0.0 -> ZERO
    d == 1.0 -> ONE
    !d.isFinite() -> throw ArithmeticException("non-finite")
    d < 0 -> -TWO.pow(exponent(d)) * factor(d)
    else -> TWO.pow(exponent(d)) * factor(d)
}

private fun exponent(d: Double) =
    ((d.toBits() shr 52).toInt() and 0x7ff) - 1023

private fun factor(other: Double): FiniteBigRational {
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
fun FiniteBigRational.divideAndRemainder(other: FiniteBigRational):
        Pair<FiniteBigRational, FiniteBigRational> {
    val quotient = (this / other).round()
    val remainder = this - other * quotient
    return quotient to remainder
}

/**
 * Returns a `FiniteBigRational` whose value is `(this^exponent)`. Note that
 * `exponent` is an integer rather than a FiniteBigRational (and the
 * underlying [BigInteger] does not support `BInt.pow(BInt)`).
 */
fun FiniteBigRational.pow(exponent: Int): FiniteBigRational /* type check issue */ =
    when {
        0 <= exponent ->
            valueOf(numerator.pow(exponent), denominator.pow(exponent))
        else -> reciprocal.pow(-exponent)
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

private fun BInt.lcm(other: BInt) = (this * (other / gcd(other))).abs()

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
 * Rounds to the nearest whole number _closer to 0_ than this FiniteBigRational,
 * or when this FiniteBigRational is whole, the same `FiniteBigRational`.  Non-finite values
 * return themselves.
 */
fun FiniteBigRational.round() = when {
    roundsToSelf() -> this
    else -> (numerator / denominator).toFiniteBigRational()
}

private fun FiniteBigRational.roundsToSelf() = isInteger()

/**
 * Returns a `FiniteBigRational` between this FiniteBigRational and the other
 * one, or throws [ArithmeticException] if the endpoints are the same.
 *
 * If `a/b` and `c/d` are rational numbers such that `a/b ≠ c/d` or, then
 * this function returns `(a+c)/(b+d)` (order of `this` and `other` does
 * not matter).  When `a/b = c/d`, throws [ArithmeticException].
 */
fun FiniteBigRational.between(other: FiniteBigRational) = when {
    this === other || equals(other) -> throw ArithmeticException("nothing between")
    else -> valueOf(
        numerator + other.numerator,
        denominator + other.denominator
    )
}

/** Returns the finite continued fraction of this `FiniteBigRational`. */
fun FiniteBigRational.toContinuedFraction() =
    FiniteContinuedFraction.valueOf(this)

/** Checks that this rational is an integer. */
fun FiniteBigRational.isInteger() = BInt.ONE == denominator

/** Checks that this rational is 0. */
fun FiniteBigRational.isZero() = ZERO === this

/**
 * Checks that this rational is dyadic, that is, the denominator is a power
 * of 2.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Dyadic_rational"><cite>Dyadic rational</cite></a>
 */
fun FiniteBigRational.isDyadic() = (denominator.isOne() ||
        (denominator % BInt.TWO).isZero())

/**
 * Checks that this rational has an even denominator.  The odds of a random
 * rational number having an even denominator is exactly 1/3 (Salamin and
 * Gosper 1972).
 */
fun FiniteBigRational.isDenominatorEven() = denominator.isEven()

private fun BInt.isEven() = BInt.ZERO == this % BInt.TWO
