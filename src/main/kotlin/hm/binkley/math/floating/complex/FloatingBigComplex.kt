package hm.binkley.math.floating.complex

import hm.binkley.math.BFixed
import hm.binkley.math.BFloating
import hm.binkley.math.algebra.Field
import hm.binkley.math.algebra.FieldCompanion
import hm.binkley.math.floating.FloatingBigRational.Companion.TWO
import hm.binkley.math.floating.complex.FloatingBigComplex.Companion.ONE
import hm.binkley.math.floating.toBigRational
import hm.binkley.math.isZero
import hm.binkley.math.sqrt
import hm.binkley.math.sqrtApproximated
import hm.binkley.math.toBigInteger
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.absoluteValue

public data class FloatingBigComplex(
    val real: BRat,
    val imag: BImag,
) : Field<FloatingBigComplex> {
    override val companion: Companion get() = Companion

    override operator fun unaryMinus(): BComplex = -real + -imag

    /** *NB* &mdash; `1/(a+bi)` is `(a-bi)/(a² + b²)`. */
    override fun unaryDiv(): BComplex = conjugate / det

    override operator fun plus(addend: BComplex): BComplex =
        (real + addend.real) + (imag + addend.imag)

    override operator fun times(factor: BComplex): BComplex =
        (real * factor.real + imag * factor.imag) +
            (real * factor.imag + imag * factor.real)

    override operator fun div(divisor: BComplex): BComplex =
        this * divisor.unaryDiv()

    override fun toString(): String =
        if (BRat.ZERO > imag.value) "$real-${-imag}"
        else "$real+$imag"

    public companion object : FieldCompanion<BComplex> {
        override val ZERO: BComplex = BComplex(BRat.ZERO, BRat.ZERO.i)
        override val ONE: BComplex = BComplex(BRat.ONE, BRat.ZERO.i)
    }
}

// Properties

public val BComplex.conjugate: BComplex get() = real - imag
public val BComplex.det: BRat get() = real * real - imag * imag
public val BComplex.absoluteValue: BRat get() = det.sqrt()
public val BComplex.reciprocal: BComplex get() = unaryDiv()

// Factories, plus and minus real+imag (include improper order like "i+1")

public operator fun BRat.plus(imag: BImag): BComplex = BComplex(this, imag)

public operator fun BImag.plus(real: BRat): BComplex = real + this

public operator fun BFloating.plus(imag: BImag): BComplex =
    toBigRational() + imag

public operator fun BImag.plus(real: BFloating): BComplex = real + this

public operator fun Double.plus(imag: BImag): BComplex = toBigRational() + imag

public operator fun BImag.plus(real: Double): BComplex = real + this

public operator fun Float.plus(imag: BImag): BComplex = toBigRational() + imag

public operator fun BImag.plus(real: Float): BComplex = real + this

public operator fun BFixed.plus(imag: BImag): BComplex = toBigRational() + imag

public operator fun BImag.plus(real: BFixed): BComplex = real + this

public operator fun Long.plus(imag: BImag): BComplex = toBigRational() + imag

public operator fun BImag.plus(real: Long): BComplex = real + this

public operator fun Int.plus(imag: BImag): BComplex = toBigRational() + imag

public operator fun BImag.plus(real: Int): BComplex = real + this

public operator fun BImag.minus(real: BRat): BComplex = -real + this

public operator fun BRat.minus(imag: BImag): BComplex = this + -imag

public operator fun BImag.minus(real: BFloating): BComplex = -real + this

public operator fun BFloating.minus(imag: BImag): BComplex = this + -imag

public operator fun BImag.minus(real: Double): BComplex = -real + this

public operator fun Double.minus(imag: BImag): BComplex = this + -imag

public operator fun BImag.minus(real: Float): BComplex = -real + this

public operator fun Float.minus(imag: BImag): BComplex = this + -imag

public operator fun BImag.minus(real: BFixed): BComplex = -real + this

public operator fun BFixed.minus(imag: BImag): BComplex = this + -imag

public operator fun BImag.minus(real: Long): BComplex = -real + this

public operator fun Long.minus(imag: BImag): BComplex = this + -imag

public operator fun BImag.minus(real: Int): BComplex = -real + this

public operator fun Int.minus(imag: BImag): BComplex = this + -imag

// Addition operator

public operator fun BComplex.plus(addend: BImag): BComplex =
    this + (BRat.ZERO + addend)

public operator fun BImag.plus(addend: BComplex): BComplex = addend + this

public operator fun BRat.plus(addend: BComplex): BComplex = addend + this

public operator fun BComplex.plus(addend: BRat): BComplex =
    this + (addend + BRat.ZERO.i)

public operator fun BFloating.plus(addend: BComplex): BComplex = addend + this

public operator fun BComplex.plus(addend: BFloating): BComplex =
    this + (addend + BRat.ZERO.i)

public operator fun Double.plus(addend: BComplex): BComplex = addend + this

public operator fun BComplex.plus(addend: Double): BComplex =
    this + (addend + BRat.ZERO.i)

public operator fun Float.plus(addend: BComplex): BComplex = addend + this

public operator fun BComplex.plus(addend: Float): BComplex =
    this + (addend + BRat.ZERO.i)

public operator fun BFixed.plus(addend: BComplex): BComplex = addend + this

public operator fun BComplex.plus(addend: BFixed): BComplex =
    this + (addend + BRat.ZERO.i)

public operator fun Long.plus(addend: BComplex): BComplex = addend + this

public operator fun BComplex.plus(addend: Long): BComplex =
    this + (addend + BRat.ZERO.i)

public operator fun Int.plus(addend: BComplex): BComplex = addend + this

public operator fun BComplex.plus(addend: Int): BComplex =
    this + (addend + BRat.ZERO.i)

// Subtraction operator

public operator fun BComplex.minus(subtrahend: BImag): BComplex =
    this + -subtrahend

public operator fun BImag.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

public operator fun BRat.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

public operator fun BComplex.minus(subtrahend: BRat): BComplex =
    this + -subtrahend

public operator fun BFloating.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

public operator fun BComplex.minus(subtrahend: BFloating): BComplex =
    this + -subtrahend

public operator fun Double.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

public operator fun BComplex.minus(subtrahend: Double): BComplex =
    this + -subtrahend

public operator fun Float.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

public operator fun BComplex.minus(subtrahend: Float): BComplex =
    this + -subtrahend

public operator fun BFixed.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

public operator fun BComplex.minus(subtrahend: BFixed): BComplex =
    this + -subtrahend

public operator fun Long.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

public operator fun BComplex.minus(subtrahend: Long): BComplex =
    this + -subtrahend

public operator fun Int.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

public operator fun BComplex.minus(subtrahend: Int): BComplex =
    this + -subtrahend

// Multiplication operator

public operator fun BComplex.times(multiplier: BImag): BComplex =
    this * (BComplex.ZERO + multiplier)

public operator fun BImag.times(multiplier: BComplex): BComplex =
    multiplier * this

public operator fun BRat.times(multiplier: BComplex): BComplex =
    multiplier * this

public operator fun BComplex.times(multiplier: BRat): BComplex =
    this * (multiplier + BRat.ZERO.i)

public operator fun BFloating.times(multiplier: BComplex): BComplex =
    multiplier * this

public operator fun BComplex.times(multiplier: BFloating): BComplex =
    this * (multiplier + BRat.ZERO.i)

public operator fun Double.times(multiplier: BComplex): BComplex =
    multiplier * this

public operator fun BComplex.times(multiplier: Double): BComplex =
    this * (multiplier + BRat.ZERO.i)

public operator fun Float.times(multiplier: BComplex): BComplex =
    multiplier * this

public operator fun BComplex.times(multiplier: Float): BComplex =
    this * (multiplier + BRat.ZERO.i)

public operator fun BFixed.times(multiplier: BComplex): BComplex =
    multiplier * this

public operator fun BComplex.times(multiplier: BFixed): BComplex =
    this * (multiplier + BRat.ZERO.i)

public operator fun Long.times(multiplier: BComplex): BComplex =
    multiplier * this

public operator fun BComplex.times(multiplier: Long): BComplex =
    this * (multiplier + BRat.ZERO.i)

public operator fun Int.times(multiplier: BComplex): BComplex =
    multiplier * this

public operator fun BComplex.times(multiplier: Int): BComplex =
    this * (multiplier + BRat.ZERO.i)

// Division operator

public operator fun BComplex.div(divisor: BImag): BComplex =
    this / (BComplex.ZERO + divisor)

public operator fun BImag.div(divisor: BComplex): BComplex = divisor / this

public operator fun BRat.div(divisor: BComplex): BComplex = divisor / this

public operator fun BComplex.div(divisor: BRat): BComplex =
    real / divisor + (imag.value / divisor).i

public operator fun BFloating.div(divisor: BComplex): BComplex = divisor / this

public operator fun BComplex.div(divisor: BFloating): BComplex =
    this / divisor.toBigRational()

public operator fun Double.div(divisor: BComplex): BComplex = divisor / this

public operator fun BComplex.div(divisor: Double): BComplex =
    this / divisor.toBigRational()

public operator fun Float.div(divisor: BComplex): BComplex = divisor / this

public operator fun BComplex.div(divisor: Float): BComplex =
    this / divisor.toBigRational()

public operator fun BFixed.div(divisor: BComplex): BComplex = divisor / this

public operator fun BComplex.div(divisor: BFixed): BComplex =
    this / divisor.toBigRational()

public operator fun Long.div(divisor: BComplex): BComplex = divisor / this

public operator fun BComplex.div(divisor: Long): BComplex =
    this / divisor.toBigRational()

public operator fun Int.div(divisor: BComplex): BComplex = divisor / this

public operator fun BComplex.div(divisor: Int): BComplex =
    this / divisor.toBigRational()

// Functions

public fun BComplex.toImaginary(): BImag =
    if (real.isZero()) imag
    else throw ArithmeticException("Not imaginary: $this")

public fun BComplex.toBigRational(): BRat =
    if (imag.isZero()) real
    else throw ArithmeticException("Not real: $this")

public fun BComplex.toBigDecimal(): BigDecimal =
    if (imag.isZero()) real.toBigDecimal()
    else throw ArithmeticException("Not real: $this")

public fun BComplex.toDouble(): Double =
    if (imag.isZero()) real.toDouble()
    else throw ArithmeticException("Not real: $this")

public fun BComplex.toFloat(): Float =
    if (imag.isZero()) real.toFloat()
    else throw ArithmeticException("Not real: $this")

public fun BComplex.toBigInteger(): BigInteger =
    if (imag.isZero()) real.toBigInteger()
    else throw ArithmeticException("Not real: $this")

public fun BComplex.toLong(): Long =
    if (imag.isZero()) real.toLong()
    else throw ArithmeticException("Not real: $this")

public fun BComplex.toInt(): Int =
    if (imag.isZero()) real.toInt()
    else throw ArithmeticException("Not real: $this")

public fun BComplex.modulusApproximated(): BRat = det.sqrtApproximated()

public fun BComplex.sqrtApproximated(): BComplex {
    val gamma = ((real + modulusApproximated()) / TWO).sqrtApproximated()
    val delta = imag.value.sign *
        ((-real + modulusApproximated()) / TWO).sqrtApproximated()
    return gamma + delta.i
}

@Suppress("DANGEROUS_CHARACTERS", "FunctionName")
public infix fun BComplex.`^`(n: Int): BComplex = pow(n)

/**
 * Note: Following expectations for discrete exponents, `0^0` is defined as `1`.
 *
 * @todo Improve on brute force
 */
public fun BComplex.pow(n: Int): BComplex {
    when (n) {
        0 -> return ONE
        1 -> return this
        -1 -> return unaryDiv()
    }

    tailrec fun BComplex.pow0(exponent: Int, current: BComplex): BComplex =
        when (exponent) {
            0 -> current
            else -> pow0(exponent - 1, this * current)
        }

    val z = pow0(n.absoluteValue - 1, this)

    return if (0 > n) z.unaryDiv() else z
}
