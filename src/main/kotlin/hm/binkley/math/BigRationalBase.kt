package hm.binkley.math

import java.math.BigDecimal
import java.util.Objects.hash

abstract class BigRationalBase<T : BigRationalBase<T>> internal constructor(
    val numerator: BInt,
    val denominator: BInt
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

    override fun equals(other: Any?) = this === other ||
            other is BigRationalBase<*> &&
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
}
