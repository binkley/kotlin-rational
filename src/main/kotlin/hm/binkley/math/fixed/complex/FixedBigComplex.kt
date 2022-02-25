package hm.binkley.math.fixed.complex

import hm.binkley.math.BFixed
import hm.binkley.math.algebra.Field
import hm.binkley.math.algebra.FieldCompanion
import hm.binkley.math.fixed.FixedBigRational
import hm.binkley.math.fixed.FixedBigRational.Companion.TWO
import hm.binkley.math.fixed.complex.FixedBigComplex.Companion.ONE
import hm.binkley.math.fixed.toBigRational
import hm.binkley.math.isZero
import hm.binkley.math.sqrt
import hm.binkley.math.sqrtApproximated
import java.math.BigInteger
import kotlin.math.absoluteValue

public data class FixedBigComplex(
    val real: FixedBigRational,
    val imag: FixedBigImaginary,
) : Field<FixedBigComplex> {
    // Not covered in tests -- API ensures 0 and 1 are defined
    override val companion: Companion get() = FixedBigComplex

    override operator fun unaryMinus(): FixedBigComplex = -real + -imag

    /** *NB* &mdash; `1/(a+bi)` is `(a-bi)/(a² + b²)`. */
    override fun unaryDiv(): FixedBigComplex = conjugate / det

    override operator fun plus(addend: FixedBigComplex): FixedBigComplex =
        (real + addend.real) + (imag + addend.imag)

    override operator fun times(factor: FixedBigComplex): FixedBigComplex =
        (real * factor.real + imag * factor.imag) +
            (real * factor.imag + imag * factor.real)

    override operator fun div(divisor: FixedBigComplex): FixedBigComplex =
        this * divisor.unaryDiv()

    override fun toString(): String =
        if (FixedBigRational.ZERO > imag.value) "$real-${-imag}" else "$real+$imag"

    public companion object : FieldCompanion<FixedBigComplex> {
        override val ZERO: FixedBigComplex =
            FixedBigComplex(FixedBigRational.ZERO, FixedBigRational.ZERO.i)
        override val ONE: FixedBigComplex =
            FixedBigComplex(FixedBigRational.ONE, FixedBigRational.ZERO.i)
    }
}

// Properties

public val FixedBigComplex.conjugate: FixedBigComplex get() = real - imag
public val FixedBigComplex.det: FixedBigRational
    get() = real * real - imag * imag
public val FixedBigComplex.absoluteValue: FixedBigRational get() = det.sqrt()
public val FixedBigComplex.reciprocal: FixedBigComplex get() = unaryDiv()

// Factories, plus and minus real+imag (include improper order like "i+1")

public operator fun FixedBigRational.plus(imag: FixedBigImaginary): FixedBigComplex =
    FixedBigComplex(this, imag)

public operator fun FixedBigImaginary.plus(real: FixedBigRational): FixedBigComplex =
    real + this

public operator fun BFixed.plus(imag: FixedBigImaginary): FixedBigComplex =
    toBigRational() + imag

public operator fun FixedBigImaginary.plus(real: BFixed): FixedBigComplex =
    real + this

public operator fun Long.plus(imag: FixedBigImaginary): FixedBigComplex =
    toBigRational() + imag

public operator fun FixedBigImaginary.plus(real: Long): FixedBigComplex =
    real + this

public operator fun Int.plus(imag: FixedBigImaginary): FixedBigComplex =
    toBigRational() + imag

public operator fun FixedBigImaginary.plus(real: Int): FixedBigComplex =
    real + this

public operator fun FixedBigImaginary.minus(real: FixedBigRational): FixedBigComplex =
    -real + this

public operator fun FixedBigRational.minus(imag: FixedBigImaginary): FixedBigComplex =
    this + -imag

public operator fun FixedBigImaginary.minus(real: BFixed): FixedBigComplex =
    -real + this

public operator fun BFixed.minus(imag: FixedBigImaginary): FixedBigComplex =
    this + -imag

public operator fun FixedBigImaginary.minus(real: Long): FixedBigComplex =
    -real + this

public operator fun Long.minus(imag: FixedBigImaginary): FixedBigComplex =
    this + -imag

public operator fun FixedBigImaginary.minus(real: Int): FixedBigComplex =
    -real + this

public operator fun Int.minus(imag: FixedBigImaginary): FixedBigComplex =
    this + -imag

// Addition operator

public operator fun FixedBigComplex.plus(addend: FixedBigImaginary): FixedBigComplex =
    this + (FixedBigRational.ZERO + addend)

public operator fun FixedBigImaginary.plus(addend: FixedBigComplex): FixedBigComplex =
    addend + this

public operator fun FixedBigRational.plus(addend: FixedBigComplex): FixedBigComplex =
    addend + this

public operator fun FixedBigComplex.plus(addend: FixedBigRational): FixedBigComplex =
    this + (addend + FixedBigRational.ZERO.i)

public operator fun BFixed.plus(addend: FixedBigComplex): FixedBigComplex =
    addend + this

public operator fun FixedBigComplex.plus(addend: BFixed): FixedBigComplex =
    this + (addend + FixedBigRational.ZERO.i)

public operator fun Long.plus(addend: FixedBigComplex): FixedBigComplex =
    addend + this

public operator fun FixedBigComplex.plus(addend: Long): FixedBigComplex =
    this + (addend + FixedBigRational.ZERO.i)

public operator fun Int.plus(addend: FixedBigComplex): FixedBigComplex =
    addend + this

public operator fun FixedBigComplex.plus(addend: Int): FixedBigComplex =
    this + (addend + FixedBigRational.ZERO.i)

// Subtraction operator

public operator fun FixedBigComplex.minus(subtrahend: FixedBigImaginary): FixedBigComplex =
    this + -subtrahend

public operator fun FixedBigImaginary.minus(subtrahend: FixedBigComplex): FixedBigComplex =
    this + -subtrahend

public operator fun FixedBigRational.minus(subtrahend: FixedBigComplex): FixedBigComplex =
    this + -subtrahend

public operator fun FixedBigComplex.minus(subtrahend: FixedBigRational): FixedBigComplex =
    this + -subtrahend

public operator fun BFixed.minus(subtrahend: FixedBigComplex): FixedBigComplex =
    this + -subtrahend

public operator fun FixedBigComplex.minus(subtrahend: BFixed): FixedBigComplex =
    this + -subtrahend

public operator fun Long.minus(subtrahend: FixedBigComplex): FixedBigComplex =
    this + -subtrahend

public operator fun FixedBigComplex.minus(subtrahend: Long): FixedBigComplex =
    this + -subtrahend

public operator fun Int.minus(subtrahend: FixedBigComplex): FixedBigComplex =
    this + -subtrahend

public operator fun FixedBigComplex.minus(subtrahend: Int): FixedBigComplex =
    this + -subtrahend

// Multiplication operator

public operator fun FixedBigComplex.times(multiplier: FixedBigImaginary): FixedBigComplex =
    this * (FixedBigComplex.ZERO + multiplier)

public operator fun FixedBigImaginary.times(multiplier: FixedBigComplex): FixedBigComplex =
    multiplier * this

public operator fun FixedBigRational.times(multiplier: FixedBigComplex): FixedBigComplex =
    multiplier * this

public operator fun FixedBigComplex.times(multiplier: FixedBigRational): FixedBigComplex =
    this * (multiplier + FixedBigRational.ZERO.i)

public operator fun BFixed.times(multiplier: FixedBigComplex): FixedBigComplex =
    multiplier * this

public operator fun FixedBigComplex.times(multiplier: BFixed): FixedBigComplex =
    this * (multiplier + FixedBigRational.ZERO.i)

public operator fun Long.times(multiplier: FixedBigComplex): FixedBigComplex =
    multiplier * this

public operator fun FixedBigComplex.times(multiplier: Long): FixedBigComplex =
    this * (multiplier + FixedBigRational.ZERO.i)

public operator fun Int.times(multiplier: FixedBigComplex): FixedBigComplex =
    multiplier * this

public operator fun FixedBigComplex.times(multiplier: Int): FixedBigComplex =
    this * (multiplier + FixedBigRational.ZERO.i)

// Division operator

public operator fun FixedBigComplex.div(divisor: FixedBigImaginary): FixedBigComplex =
    this / (FixedBigComplex.ZERO + divisor)

public operator fun FixedBigImaginary.div(divisor: FixedBigComplex): FixedBigComplex =
    divisor / this

public operator fun FixedBigRational.div(divisor: FixedBigComplex): FixedBigComplex =
    divisor / this

public operator fun FixedBigComplex.div(divisor: FixedBigRational): FixedBigComplex =
    real / divisor + (imag.value / divisor).i

public operator fun BFixed.div(divisor: FixedBigComplex): FixedBigComplex =
    divisor / this

public operator fun FixedBigComplex.div(divisor: BFixed): FixedBigComplex =
    this / divisor.toBigRational()

public operator fun Long.div(divisor: FixedBigComplex): FixedBigComplex =
    divisor / this

public operator fun FixedBigComplex.div(divisor: Long): FixedBigComplex =
    this / divisor.toBigRational()

public operator fun Int.div(divisor: FixedBigComplex): FixedBigComplex =
    divisor / this

public operator fun FixedBigComplex.div(divisor: Int): FixedBigComplex =
    this / divisor.toBigRational()

// Functions

public fun FixedBigComplex.toImaginary(): FixedBigImaginary =
    if (real.isZero()) imag
    else throw ArithmeticException("Not imaginary: $this")

public fun FixedBigComplex.toBigRational(): FixedBigRational =
    if (imag.isZero()) real
    else throw ArithmeticException("Not real: $this")

public fun FixedBigComplex.toBigInteger(): BigInteger =
    if (imag.isZero()) real.toBigInteger()
    else throw ArithmeticException("Not real: $this")

public fun FixedBigComplex.toLong(): Long =
    if (imag.isZero()) real.toLong()
    else throw ArithmeticException("Not real: $this")

public fun FixedBigComplex.toInt(): Int =
    if (imag.isZero()) real.toInt()
    else throw ArithmeticException("Not real: $this")

public fun FixedBigComplex.modulusApproximated(): FixedBigRational =
    det.sqrtApproximated()

public fun FixedBigComplex.sqrtApproximated(): FixedBigComplex {
    val gamma = ((real + modulusApproximated()) / TWO).sqrtApproximated()
    val delta = imag.value.sign *
        ((-real + modulusApproximated()) / TWO).sqrtApproximated()
    return gamma + delta.i
}

@Suppress("DANGEROUS_CHARACTERS", "FunctionName")
public infix fun FixedBigComplex.`^`(n: Int): FixedBigComplex = pow(n)

/**
 * Note: Following expectations for discrete exponents, `0^0` is defined as `1`.
 *
 * @todo Improve on brute force
 */
public fun FixedBigComplex.pow(n: Int): FixedBigComplex {
    when (n) {
        0 -> return ONE
        1 -> return this
        -1 -> return unaryDiv()
    }

    tailrec fun FixedBigComplex.pow0(
        exponent: Int,
        current: FixedBigComplex,
    ): FixedBigComplex = when (exponent) {
        0 -> current
        else -> pow0(exponent - 1, this * current)
    }

    val z = pow0(n.absoluteValue - 1, this)

    return if (0 > n) z.unaryDiv() else z
}
