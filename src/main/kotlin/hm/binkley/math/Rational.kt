package hm.binkley.math

import hm.binkley.math.Rational.Companion.ONE
import hm.binkley.math.Rational.Companion.ZERO
import java.math.BigInteger
import java.util.Objects

private typealias BInt = BigInteger

class Rational private constructor(
    val numerator: BInt,
    val denominator: BInt
) : Number(), Comparable<Rational> {
    override fun toByte() = toLong().toByte()
    override fun toChar(): Char = error("Characters are non-numeric")
    override fun toDouble() = numerator.toDouble() / denominator.toDouble()
    override fun toFloat() = numerator.toFloat() / denominator.toFloat()
    override fun toInt() = toLong().toInt()
    override fun toLong() = (numerator / denominator).toLong()
    override fun toShort() = toLong().toShort()

    /**
     * Sorts while ignoring [equals].  So [NaN] sorts to the end, even as
     * `NaN != NaN`.
     * */
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

    operator fun plus(other: Rational) = new(
        numerator * other.denominator + other.numerator * denominator,
        denominator * other.denominator
    )

    operator fun minus(other: Rational) = this + -other

    operator fun times(other: Rational) = new(
        numerator * other.numerator,
        denominator * other.denominator
    )

    /** NB -- Division by zero returns NaN, does not raise exception */
    operator fun div(other: Rational) = this * other.reciprocal()

    /** NB -- Remainder by zero or NaN returns NaN, does not raise exception */
    // TODO: Find if there is a sensible meaning for modulus of a fraction
    //  operator fun rem(other: Rational): Rational

    operator fun rangeTo(other: Rational) = RationalProgression(this, other)

    /**
     * Bypasses the factory constructor as [numerator] and [denominator] are
     * coprime.
     * */
    fun reciprocal() = Rational(denominator, numerator)

    fun abs() = new(numerator.abs(), denominator)

    fun signum() = when {
        isNaN() -> NaN
        else -> new(numerator.signum())
    }

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

            // Ensure constants return the *same* object
            if (d.isZero()) when {
                n.isZero() -> return NaN
                n.isOne() -> return POSITIVE_INFINITY
                n.negate().isOne() -> return NEGATIVE_INFINITY
            }
            if (n.isZero()) return ZERO
            if (n.isOne() && d.isOne()) return ONE

            return Rational(n, d)
        }

        fun new(numerator: Long, denominator: Long = 1L) =
            new(BInt.valueOf(numerator), BInt.valueOf(denominator))

        fun new(numerator: Int, denominator: Int = 1) =
            new(numerator.toLong(), denominator.toLong())

        fun new(numerator: BInt, denominator: Long) =
            new(numerator, BInt.valueOf(denominator))

        fun new(numerator: Long, denominator: BInt) =
            new(BInt.valueOf(numerator), denominator)
    }
}

infix fun BInt.over(denominator: BInt) = Rational.new(this, denominator)
infix fun BInt.over(denominator: Long) = Rational.new(this, denominator)
infix fun BInt.over(denominator: Int) =
    Rational.new(this, denominator.toLong())

infix fun Long.over(denominator: BInt) = Rational.new(this, denominator)
infix fun Long.over(denominator: Long) = Rational.new(this, denominator)
infix fun Long.over(denominator: Int) =
    Rational.new(this, denominator.toLong())

infix fun Int.over(denominator: BInt) = Rational.new(toLong(), denominator)
infix fun Int.over(denominator: Long) = Rational.new(toLong(), denominator)
infix fun Int.over(denominator: Int) =
    Rational.new(toLong(), denominator.toLong())

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

    infix fun step(step: Int) =
        RationalProgression(start, endInclusive, step over 1)

    infix fun step(step: Long) =
        RationalProgression(start, endInclusive, step over 1)
}

infix fun Rational.downTo(other: Rational) =
    RationalProgression(this, other, -ONE)
