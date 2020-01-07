package hm.binkley.math

import hm.binkley.math.Rational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.Rational.Companion.NaN
import hm.binkley.math.Rational.Companion.ONE
import hm.binkley.math.Rational.Companion.POSITIVE_INFINITY
import hm.binkley.math.Rational.Companion.TWO
import hm.binkley.math.Rational.Companion.ZERO
import hm.binkley.math.Rational.Companion.new
import lombok.Generated
import java.math.BigDecimal
import java.math.BigInteger
import java.util.Objects

private typealias BInt = BigInteger

/**
 * @todo Consider `Short` and `Byte` overloads
 * @todo Assign properties at construction; avoid circular ctors
 */
class Rational private constructor(
    val numerator: BInt,
    val denominator: BInt
) : Number(), Comparable<Rational> {
    val sign: Rational
        get() = when {
            isNaN() -> NaN
            else -> numerator.signum().toRational()
        }

    /** NB -- Reciprocal of infinities is zero. */
    val reciprocal: Rational
        get() = new(denominator, numerator)

    val absoluteValue: Rational
        get() = new(numerator.abs(), denominator)

    override fun toChar(): Char = error("Characters are non-numeric")
    override fun toByte() = toLong().toByte()
    override fun toShort() = toLong().toShort()
    override fun toInt() = toLong().toInt()
    override fun toLong() = (numerator / denominator).toLong()
    override fun toFloat() = toDouble().toFloat()

    override fun toDouble() = when {
        isNaN() -> Double.NaN
        isPositiveInfinity() -> Double.POSITIVE_INFINITY
        isNegativeInfinity() -> Double.NEGATIVE_INFINITY
        else -> numerator.toDouble() / denominator.toDouble()
    }

    /**
     * Sorts while ignoring [equals].  So [NaN] sorts to the end, even as
     * `NaN != NaN` (and similarly for the infinities).
     */
    override fun compareTo(other: Rational) = when {
        this === other -> 0 // Sort stability for constants
        isNegativeInfinity() -> -1
        // NaN sorts after +Inf
        isNaN() -> 1
        other.isNaN() -> -1
        // isPositiveInfinity() -> 1 -- else handles this
        else -> {
            val a = numerator * other.denominator
            val b = other.numerator * denominator
            a.compareTo(b)
        }
    }

    /** NB -- NaN != NaN, nor infinities are equal to themselves */
    override fun equals(other: Any?) = when {
        !isFinite() -> false
        this === other -> true
        other !is Rational -> false
        !other.isFinite() -> false
        else -> numerator == other.numerator
                && denominator == other.denominator
    }

    override fun hashCode() = Objects.hash(numerator, denominator)

    override fun toString() = when {
        isNaN() -> "NaN"
        isPositiveInfinity() -> "+∞"
        isNegativeInfinity() -> "-∞"
        denominator == BInt.ONE -> numerator.toString()
        else -> "$numerator/$denominator"
    }

    operator fun unaryPlus() = this
    operator fun unaryMinus() = new(numerator.negate(), denominator)
    operator fun inc() = new(numerator + denominator, denominator)
    operator fun dec() = new(numerator - denominator, denominator)

    operator fun plus(other: Int) = this + other.toRational()
    operator fun plus(other: Long) = this + other.toRational()
    operator fun plus(other: BInt) = this + other.toRational()
    operator fun plus(other: Rational) = new(
        numerator * other.denominator + other.numerator * denominator,
        denominator * other.denominator
    )

    operator fun minus(other: Int) = this - other.toRational()
    operator fun minus(other: Long) = this - other.toRational()
    operator fun minus(other: BInt) = this - other.toRational()
    operator fun minus(other: Rational) = this + -other

    operator fun times(other: Int) = this * other.toRational()
    operator fun times(other: Long) = this * other.toRational()
    operator fun times(other: BInt) = this * other.toRational()
    operator fun times(other: Rational) = new(
        numerator * other.numerator,
        denominator * other.denominator
    )

    operator fun div(other: Int) = this / other.toRational()
    operator fun div(other: Long) = this / other.toRational()
    operator fun div(other: BInt) = this / other.toRational()
    /** NB -- Division by zero returns NaN, does not raise exception */
    operator fun div(other: Rational) = this * other.reciprocal

    operator fun rangeTo(other: Int) = rangeTo(other.toRational())
    operator fun rangeTo(other: Long) = rangeTo(other.toRational())
    operator fun rangeTo(other: BInt) = rangeTo(other.toRational())
    operator fun rangeTo(other: Rational) = RationalProgression(this, other)

    fun pow(exponent: Int) = when {
        0 <= exponent ->
            new(numerator.pow(exponent), denominator.pow(exponent))
        else ->
            new(denominator.pow(-exponent), numerator.pow(-exponent))
    }

    fun gcd(other: Rational) = new(
        numerator.gcd(other.numerator),
        denominator.lcm(other.denominator)
    )

    fun lcm(other: Rational) = new(
        numerator.lcm(other.numerator),
        denominator.gcd(other.denominator)
    )

    /** NB -- NaN is not finite */
    fun isFinite() = !isNaN() && !isInfinite()

    fun isDyadic() = isFinite()
            && (BigInteger.ONE == denominator
            || BigInteger.ZERO == denominator % BigInteger.TWO)

    /** NB -- NaN is not infinite */
    fun isInfinite() = isPositiveInfinity() || isNegativeInfinity()

    fun isNaN() = this === NaN
    fun isPositiveInfinity() = this === POSITIVE_INFINITY
    fun isNegativeInfinity() = this === NEGATIVE_INFINITY

    private fun BInt.lcm(other: BInt) = (this * (other / gcd(other))).abs()

    companion object {
        val NaN = Rational(BInt.ZERO, BInt.ZERO)
        val ZERO = Rational(BInt.ZERO, BInt.ONE)
        val ONE = Rational(BInt.ONE, BInt.ONE)
        val TWO = Rational(BInt.TWO, BInt.ONE)
        val POSITIVE_INFINITY = Rational(BInt.ONE, BInt.ZERO)
        val NEGATIVE_INFINITY = Rational(BInt.ONE.negate(), BInt.ZERO)

        fun new(numerator: BInt, denominator: BInt = BInt.ONE): Rational {
            var n = numerator
            var d = denominator
            if (d < BInt.ZERO) {
                n = n.negate()
                d = d.negate()
            }

            val gcd = numerator.gcd(denominator)
            if (gcd != BInt.ZERO) {
                n /= gcd
                d /= gcd
            }

            fun BInt.isZero() = this == BInt.ZERO
            fun BInt.isOne() = this == BInt.ONE
            fun BInt.isTwo() = this == BInt.TWO

            // Ensure constants return the *same* object
            if (d.isZero()) when {
                n.isZero() -> return NaN
                n.isOne() -> return POSITIVE_INFINITY
                n.negate().isOne() -> return NEGATIVE_INFINITY
            }
            if (n.isZero()) return ZERO
            if (d.isOne()) {
                if (n.isOne()) return ONE
                if (n.isTwo()) return TWO
            }

            return Rational(n, d)
        }
    }
}

infix fun BInt.over(denominator: BInt) = new(this, denominator)
infix fun BInt.over(denominator: Long) = new(this, denominator.toBigInteger())
infix fun BInt.over(denominator: Int) = new(this, denominator.toBigInteger())
infix fun BInt.over(denominator: Double) =
    toRational() / denominator.toRational()

infix fun BInt.over(denominator: Float) =
    toRational() / denominator.toRational()

infix fun Long.over(denominator: BInt) = new(toBigInteger(), denominator)
infix fun Long.over(denominator: Long) =
    new(toBigInteger(), denominator.toBigInteger())

infix fun Long.over(denominator: Int) =
    new(toBigInteger(), denominator.toBigInteger())

infix fun Long.over(denominator: Double) =
    toRational() / denominator.toRational()

infix fun Long.over(denominator: Float) =
    toRational() / denominator.toRational()

infix fun Int.over(denominator: BInt) = new(toBigInteger(), denominator)
infix fun Int.over(denominator: Long) =
    new(toBigInteger(), denominator.toBigInteger())

infix fun Int.over(denominator: Int) =
    new(toBigInteger(), denominator.toBigInteger())

infix fun Int.over(denominator: Double) =
    toRational() / denominator.toRational()

infix fun Int.over(denominator: Float) =
    toRational() / denominator.toRational()

infix fun Double.over(denominator: BInt) =
    toRational() / denominator.toRational()

infix fun Double.over(denominator: Long) =
    toRational() / denominator.toRational()

infix fun Double.over(denominator: Int) =
    toRational() / denominator.toRational()

infix fun Double.over(denominator: Double) =
    toRational() / denominator.toRational()

infix fun Double.over(denominator: Float) =
    toRational() / denominator.toRational()

infix fun Float.over(denominator: BInt) =
    toRational() / denominator.toRational()

infix fun Float.over(denominator: Long) =
    toRational() / denominator.toRational()

infix fun Float.over(denominator: Int) =
    toRational() / denominator.toRational()

infix fun Float.over(denominator: Double) =
    toRational() / denominator.toRational()

infix fun Float.over(denominator: Float) =
    toRational() / denominator.toRational()

@Generated
fun BigDecimal.toRational(): Rational = TODO("IMPLEMENT")

fun Double.toRational() = convert(this)
fun Float.toRational() = toDouble().toRational()
fun BInt.toRational() = new(this, BigInteger.ONE)
fun Long.toRational() = toBigInteger().toRational()
fun Int.toRational() = toBigInteger().toRational()

class RationalIterator(
    start: Rational,
    private val endInclusive: Rational,
    private val step: Rational
) : Iterator<Rational> {
    init {
        if (step == ZERO) error("Infinite loop")
        if (start.isNaN() || endInclusive.isNaN() || step.isNaN())
            error("NaN != NaN")
    }

    private var current = start

    override fun hasNext() =
        if (step > ZERO)
            current <= endInclusive
        else
            current >= endInclusive

    override fun next(): Rational {
        val next = current
        current += step
        return next
    }
}

class RationalProgression(
    override val start: Rational,
    override val endInclusive: Rational,
    private val step: Rational = ONE
) : Iterable<Rational>, ClosedRange<Rational> {
    override fun iterator() =
        RationalIterator(start, endInclusive, step)

    infix fun step(step: Rational) =
        RationalProgression(start, endInclusive, step)

    infix fun step(step: BInt) =
        RationalProgression(start, endInclusive, step over 1)

    infix fun step(step: Long) =
        RationalProgression(start, endInclusive, step over 1)

    infix fun step(step: Int) =
        RationalProgression(start, endInclusive, step over 1)
}

infix fun Rational.downTo(other: Rational) =
    RationalProgression(this, other, -ONE)

operator fun BInt.plus(other: Rational) = toRational() + other
operator fun Long.plus(other: Rational) = toRational() + other
operator fun Int.plus(other: Rational) = toRational() + other
operator fun BInt.minus(other: Rational) = toRational() - other
operator fun Long.minus(other: Rational) = toRational() - other
operator fun Int.minus(other: Rational) = toRational() - other
operator fun BInt.times(other: Rational) = toRational() * other
operator fun Long.times(other: Rational) = toRational() * other
operator fun Int.times(other: Rational) = toRational() * other
operator fun BInt.div(other: Rational) = toRational() / other
operator fun Long.div(other: Rational) = toRational() / other
operator fun Int.div(other: Rational) = toRational() / other
operator fun BInt.rangeTo(other: Rational) = toRational()..other
operator fun Long.rangeTo(other: Rational) = toRational()..other
operator fun Int.rangeTo(other: Rational) = toRational()..other

private fun mantissa(d: Double) = d.toBits() and 0xfffffffffffffL

private fun exponent(d: Double) =
    ((d.toBits() shr 52).toInt() and 0x7ff) - 1023

private fun factor(mantissa: Long): Rational {
    val n = 1L shl 52
    var d = 0L
    for (e in 0..51)
        d += (mantissa shr e and 1L shl e) // MSB stored first
    return new((d + n).toBigInteger(), n.toBigInteger())
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
    d < 0 -> -TWO.pow(exponent(d)) * factor(mantissa(d))
    else -> TWO.pow(exponent(d)) * factor(mantissa(d))
}
