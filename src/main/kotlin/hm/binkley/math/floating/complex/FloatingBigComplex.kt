package hm.binkley.math.floating.complex

import hm.binkley.math.BFixed
import hm.binkley.math.BFloating
import hm.binkley.math.algebra.Field
import hm.binkley.math.algebra.FieldCompanion
import hm.binkley.math.floating.FloatingBigRational
import hm.binkley.math.floating.FloatingBigRational.Companion.TWO
import hm.binkley.math.floating.complex.FloatingBigComplex.Companion.ONE
import hm.binkley.math.floating.toBigRational
import hm.binkley.math.isZero
import hm.binkley.math.sqrt
import hm.binkley.math.sqrtApproximated
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.absoluteValue

public data class FloatingBigComplex(
    val real: FloatingBigRational,
    val imag: FloatingBigImaginary,
) : Field<FloatingBigComplex> {
    // Not covered in tests -- API ensures 0 and 1 are defined
    override val companion: Companion get() = FloatingBigComplex

    override operator fun unaryMinus(): FloatingBigComplex = -real + -imag

    /** *NB* &mdash; `1/(a+bi)` is `(a-bi)/(a² + b²)`. */
    override fun unaryDiv(): FloatingBigComplex = conjugate / det

    override operator fun plus(addend: FloatingBigComplex): FloatingBigComplex =
        (real + addend.real) + (imag + addend.imag)

    override operator fun times(factor: FloatingBigComplex)
    : FloatingBigComplex =
        (real * factor.real + imag * factor.imag) +
            (real * factor.imag + imag * factor.real)

    override operator fun div(divisor: FloatingBigComplex): FloatingBigComplex =
        this * divisor.unaryDiv()

    override fun toString(): String =
        if (FloatingBigRational.ZERO > imag.value) "$real-${-imag}"
        else "$real+$imag"

    public companion object : FieldCompanion<FloatingBigComplex> {
        override val ZERO: FloatingBigComplex = FloatingBigComplex(
            FloatingBigRational.ZERO, FloatingBigRational.ZERO.i
        )
        override val ONE: FloatingBigComplex = FloatingBigComplex(
            FloatingBigRational.ONE, FloatingBigRational.ZERO.i
        )
    }
}

// Properties

public val FloatingBigComplex.conjugate: FloatingBigComplex get() = real - imag
public val FloatingBigComplex.det: FloatingBigRational
    get() = real * real - imag * imag
public val FloatingBigComplex.absoluteValue: FloatingBigRational
    get() = det.sqrt()
public val FloatingBigComplex.reciprocal: FloatingBigComplex
    get() = unaryDiv()

// Factories, plus and minus real+imag (include improper order like "i+1")

public operator fun FloatingBigRational.plus(imag: FloatingBigImaginary)
: FloatingBigComplex = FloatingBigComplex(this, imag)

public operator fun FloatingBigImaginary.plus(real: FloatingBigRational)
: FloatingBigComplex = real + this

public operator fun BFloating.plus(imag: FloatingBigImaginary)
: FloatingBigComplex = toBigRational() + imag

public operator fun FloatingBigImaginary.plus(real: BFloating)
: FloatingBigComplex = real + this

public operator fun Double.plus(imag: FloatingBigImaginary)
: FloatingBigComplex = toBigRational() + imag

public operator fun FloatingBigImaginary.plus(real: Double)
: FloatingBigComplex = real + this

public operator fun Float.plus(imag: FloatingBigImaginary)
: FloatingBigComplex = toBigRational() + imag

public operator fun FloatingBigImaginary.plus(real: Float)
: FloatingBigComplex = real + this

public operator fun BFixed.plus(imag: FloatingBigImaginary)
: FloatingBigComplex = toBigRational() + imag

public operator fun FloatingBigImaginary.plus(real: BFixed)
: FloatingBigComplex = real + this

public operator fun Long.plus(imag: FloatingBigImaginary)
: FloatingBigComplex = toBigRational() + imag

public operator fun FloatingBigImaginary.plus(real: Long)
: FloatingBigComplex = real + this

public operator fun Int.plus(imag: FloatingBigImaginary)
: FloatingBigComplex = toBigRational() + imag

public operator fun FloatingBigImaginary.plus(real: Int)
: FloatingBigComplex = real + this

public operator fun FloatingBigImaginary.minus(real: FloatingBigRational)
: FloatingBigComplex = -real + this

public operator fun FloatingBigRational.minus(imag: FloatingBigImaginary)
: FloatingBigComplex = this + -imag

public operator fun FloatingBigImaginary.minus(real: BFloating)
: FloatingBigComplex = -real + this

public operator fun BFloating.minus(imag: FloatingBigImaginary)
: FloatingBigComplex = this + -imag

public operator fun FloatingBigImaginary.minus(real: Double)
: FloatingBigComplex = -real + this

public operator fun Double.minus(imag: FloatingBigImaginary)
: FloatingBigComplex = this + -imag

public operator fun FloatingBigImaginary.minus(real: Float)
: FloatingBigComplex = -real + this

public operator fun Float.minus(imag: FloatingBigImaginary)
: FloatingBigComplex = this + -imag

public operator fun FloatingBigImaginary.minus(real: BFixed)
: FloatingBigComplex = -real + this

public operator fun BFixed.minus(imag: FloatingBigImaginary)
: FloatingBigComplex = this + -imag

public operator fun FloatingBigImaginary.minus(real: Long)
: FloatingBigComplex = -real + this

public operator fun Long.minus(imag: FloatingBigImaginary)
: FloatingBigComplex = this + -imag

public operator fun FloatingBigImaginary.minus(real: Int)
: FloatingBigComplex = -real + this

public operator fun Int.minus(imag: FloatingBigImaginary)
: FloatingBigComplex = this + -imag

// Addition operator

public operator fun FloatingBigComplex.plus(addend: FloatingBigImaginary)
: FloatingBigComplex = this + (FloatingBigRational.ZERO + addend)

public operator fun FloatingBigImaginary.plus(addend: FloatingBigComplex)
: FloatingBigComplex = addend + this

public operator fun FloatingBigRational.plus(addend: FloatingBigComplex)
: FloatingBigComplex = addend + this

public operator fun FloatingBigComplex.plus(addend: FloatingBigRational)
: FloatingBigComplex = this + (addend + FloatingBigRational.ZERO.i)

public operator fun BFloating.plus(addend: FloatingBigComplex)
: FloatingBigComplex = addend + this

public operator fun FloatingBigComplex.plus(addend: BFloating)
: FloatingBigComplex = this + (addend + FloatingBigRational.ZERO.i)

public operator fun Double.plus(addend: FloatingBigComplex)
: FloatingBigComplex = addend + this

public operator fun FloatingBigComplex.plus(addend: Double)
: FloatingBigComplex = this + (addend + FloatingBigRational.ZERO.i)

public operator fun Float.plus(addend: FloatingBigComplex)
: FloatingBigComplex = addend + this

public operator fun FloatingBigComplex.plus(addend: Float)
: FloatingBigComplex = this + (addend + FloatingBigRational.ZERO.i)

public operator fun BFixed.plus(addend: FloatingBigComplex)
: FloatingBigComplex = addend + this

public operator fun FloatingBigComplex.plus(addend: BFixed)
: FloatingBigComplex = this + (addend + FloatingBigRational.ZERO.i)

public operator fun Long.plus(addend: FloatingBigComplex)
: FloatingBigComplex = addend + this

public operator fun FloatingBigComplex.plus(addend: Long)
: FloatingBigComplex = this + (addend + FloatingBigRational.ZERO.i)

public operator fun Int.plus(addend: FloatingBigComplex)
: FloatingBigComplex = addend + this

public operator fun FloatingBigComplex.plus(addend: Int)
: FloatingBigComplex = this + (addend + FloatingBigRational.ZERO.i)

// Subtraction operator

public operator fun FloatingBigComplex.minus(subtrahend: FloatingBigImaginary)
: FloatingBigComplex = this + -subtrahend

public operator fun FloatingBigImaginary.minus(subtrahend: FloatingBigComplex)
: FloatingBigComplex = this + -subtrahend

public operator fun FloatingBigRational.minus(subtrahend: FloatingBigComplex)
: FloatingBigComplex = this + -subtrahend

public operator fun FloatingBigComplex.minus(subtrahend: FloatingBigRational)
: FloatingBigComplex = this + -subtrahend

public operator fun BFloating.minus(subtrahend: FloatingBigComplex)
: FloatingBigComplex = this + -subtrahend

public operator fun FloatingBigComplex.minus(subtrahend: BFloating)
: FloatingBigComplex = this + -subtrahend

public operator fun Double.minus(subtrahend: FloatingBigComplex)
: FloatingBigComplex = this + -subtrahend

public operator fun FloatingBigComplex.minus(subtrahend: Double)
: FloatingBigComplex = this + -subtrahend

public operator fun Float.minus(subtrahend: FloatingBigComplex)
: FloatingBigComplex = this + -subtrahend

public operator fun FloatingBigComplex.minus(subtrahend: Float)
: FloatingBigComplex = this + -subtrahend

public operator fun BFixed.minus(subtrahend: FloatingBigComplex)
: FloatingBigComplex = this + -subtrahend

public operator fun FloatingBigComplex.minus(subtrahend: BFixed)
: FloatingBigComplex = this + -subtrahend

public operator fun Long.minus(subtrahend: FloatingBigComplex)
: FloatingBigComplex = this + -subtrahend

public operator fun FloatingBigComplex.minus(subtrahend: Long)
: FloatingBigComplex = this + -subtrahend

public operator fun Int.minus(subtrahend: FloatingBigComplex)
: FloatingBigComplex = this + -subtrahend

public operator fun FloatingBigComplex.minus(subtrahend: Int)
: FloatingBigComplex = this + -subtrahend

// Multiplication operator

public operator fun FloatingBigComplex.times(multiplier: FloatingBigImaginary)
: FloatingBigComplex = this * (FloatingBigComplex.ZERO + multiplier)

public operator fun FloatingBigImaginary.times(multiplier: FloatingBigComplex)
: FloatingBigComplex = multiplier * this

public operator fun FloatingBigRational.times(multiplier: FloatingBigComplex)
: FloatingBigComplex = multiplier * this

public operator fun FloatingBigComplex.times(multiplier: FloatingBigRational)
: FloatingBigComplex = this * (multiplier + FloatingBigRational.ZERO.i)

public operator fun BFloating.times(multiplier: FloatingBigComplex)
: FloatingBigComplex = multiplier * this

public operator fun FloatingBigComplex.times(multiplier: BFloating)
: FloatingBigComplex = this * (multiplier + FloatingBigRational.ZERO.i)

public operator fun Double.times(multiplier: FloatingBigComplex)
: FloatingBigComplex = multiplier * this

public operator fun FloatingBigComplex.times(multiplier: Double)
: FloatingBigComplex = this * (multiplier + FloatingBigRational.ZERO.i)

public operator fun Float.times(multiplier: FloatingBigComplex)
: FloatingBigComplex = multiplier * this

public operator fun FloatingBigComplex.times(multiplier: Float)
: FloatingBigComplex = this * (multiplier + FloatingBigRational.ZERO.i)

public operator fun BFixed.times(multiplier: FloatingBigComplex)
: FloatingBigComplex = multiplier * this

public operator fun FloatingBigComplex.times(multiplier: BFixed)
: FloatingBigComplex = this * (multiplier + FloatingBigRational.ZERO.i)

public operator fun Long.times(multiplier: FloatingBigComplex)
: FloatingBigComplex = multiplier * this

public operator fun FloatingBigComplex.times(multiplier: Long)
: FloatingBigComplex = this * (multiplier + FloatingBigRational.ZERO.i)

public operator fun Int.times(multiplier: FloatingBigComplex)
: FloatingBigComplex = multiplier * this

public operator fun FloatingBigComplex.times(multiplier: Int)
: FloatingBigComplex = this * (multiplier + FloatingBigRational.ZERO.i)

// Division operator

public operator fun FloatingBigComplex.div(divisor: FloatingBigImaginary)
: FloatingBigComplex = this / (FloatingBigComplex.ZERO + divisor)

public operator fun FloatingBigImaginary.div(divisor: FloatingBigComplex)
: FloatingBigComplex = divisor / this

public operator fun FloatingBigRational.div(divisor: FloatingBigComplex)
: FloatingBigComplex = divisor / this

public operator fun FloatingBigComplex.div(divisor: FloatingBigRational)
: FloatingBigComplex = real / divisor + (imag.value / divisor).i

public operator fun BFloating.div(divisor: FloatingBigComplex)
: FloatingBigComplex = divisor / this

public operator fun FloatingBigComplex.div(divisor: BFloating)
: FloatingBigComplex = this / divisor.toBigRational()

public operator fun Double.div(divisor: FloatingBigComplex)
: FloatingBigComplex = divisor / this

public operator fun FloatingBigComplex.div(divisor: Double)
: FloatingBigComplex = this / divisor.toBigRational()

public operator fun Float.div(divisor: FloatingBigComplex)
: FloatingBigComplex = divisor / this

public operator fun FloatingBigComplex.div(divisor: Float): FloatingBigComplex =
    this / divisor.toBigRational()

public operator fun BFixed.div(divisor: FloatingBigComplex): FloatingBigComplex =
    divisor / this

public operator fun FloatingBigComplex.div(divisor: BFixed): FloatingBigComplex =
    this / divisor.toBigRational()

public operator fun Long.div(divisor: FloatingBigComplex): FloatingBigComplex =
    divisor / this

public operator fun FloatingBigComplex.div(divisor: Long): FloatingBigComplex =
    this / divisor.toBigRational()

public operator fun Int.div(divisor: FloatingBigComplex): FloatingBigComplex =
    divisor / this

public operator fun FloatingBigComplex.div(divisor: Int): FloatingBigComplex =
    this / divisor.toBigRational()

// Functions

public fun FloatingBigComplex.toImaginary(): FloatingBigImaginary =
    if (real.isZero()) imag
    else throw ArithmeticException("Not imaginary: $this")

public fun FloatingBigComplex.toBigRational(): FloatingBigRational =
    if (imag.isZero()) real
    else throw ArithmeticException("Not real: $this")

public fun FloatingBigComplex.toBigDecimal(): BigDecimal =
    if (imag.isZero()) real.toBigDecimal()
    else throw ArithmeticException("Not real: $this")

public fun FloatingBigComplex.toDouble(): Double =
    if (imag.isZero()) real.toDouble()
    else throw ArithmeticException("Not real: $this")

public fun FloatingBigComplex.toFloat(): Float =
    if (imag.isZero()) real.toFloat()
    else throw ArithmeticException("Not real: $this")

public fun FloatingBigComplex.toBigInteger(): BigInteger =
    if (imag.isZero()) real.toBigInteger()
    else throw ArithmeticException("Not real: $this")

public fun FloatingBigComplex.toLong(): Long =
    if (imag.isZero()) real.toLong()
    else throw ArithmeticException("Not real: $this")

public fun FloatingBigComplex.toInt(): Int =
    if (imag.isZero()) real.toInt()
    else throw ArithmeticException("Not real: $this")

public fun FloatingBigComplex.modulusApproximated(): FloatingBigRational =
    det.sqrtApproximated()

public fun FloatingBigComplex.sqrtApproximated(): FloatingBigComplex {
    val gamma = ((real + modulusApproximated()) / TWO).sqrtApproximated()
    val delta = imag.value.sign *
        ((-real + modulusApproximated()) / TWO).sqrtApproximated()
    return gamma + delta.i
}

@Suppress("DANGEROUS_CHARACTERS", "FunctionName")
public infix fun FloatingBigComplex.`^`(n: Int): FloatingBigComplex = pow(n)

/**
 * Note: Following expectations for discrete exponents, `0^0` is defined as `1`.
 *
 * @todo Improve on brute force
 */
public fun FloatingBigComplex.pow(n: Int): FloatingBigComplex {
    when (n) {
        0 -> return ONE
        1 -> return this
        -1 -> return unaryDiv()
    }

    tailrec fun FloatingBigComplex.pow0(
        exponent: Int,
        current: FloatingBigComplex,
    ): FloatingBigComplex = when (exponent) {
        0 -> current
        else -> pow0(exponent - 1, this * current)
    }

    val z = pow0(n.absoluteValue - 1, this)

    return if (0 > n) z.unaryDiv() else z
}
