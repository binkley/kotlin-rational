package hm.binkley.math.fixed

import hm.binkley.math.BDouble
import hm.binkley.math.BImag
import hm.binkley.math.BInt
import hm.binkley.math.algebra.Field
import hm.binkley.math.algebra.FieldCompanion
import hm.binkley.math.fixed.FixedBigComplex.Companion.ONE
import hm.binkley.math.fixed.FixedBigRational.Companion.TWO
import hm.binkley.math.isZero
import hm.binkley.math.sqrt
import hm.binkley.math.sqrtApproximated
import java.math.BigInteger
import kotlin.math.absoluteValue

public data class FixedBigComplex(
    val real: BRat,
    val imag: BImag,
) : Field<FixedBigComplex> {
    override val companion: Companion = FixedBigComplex

    override operator fun unaryMinus(): FixedBigComplex = -real + -imag

    /** *NB* &mdash; `1/(a+bi)` is `(a-bi)/(a² + b²)`. */
    override fun unaryDiv(): FixedBigComplex = conjugate / det

    override operator fun plus(addend: FixedBigComplex): FixedBigComplex =
        (real + addend.real) + (imag + addend.imag)

    override operator fun times(multiplier: FixedBigComplex): FixedBigComplex =
        (real * multiplier.real + imag * multiplier.imag) +
            (real * multiplier.imag + imag * multiplier.real)

    override operator fun div(divisor: FixedBigComplex): FixedBigComplex =
        this * divisor.unaryDiv()

    override fun toString(): String =
        if (BRat.ZERO > imag.value) "$real-${-imag}" else "$real+$imag"

    public companion object : FieldCompanion<FixedBigComplex> {
        override val ZERO: FixedBigComplex =
            FixedBigComplex(BRat.ZERO, BRat.ZERO.i)
        override val ONE: FixedBigComplex =
            FixedBigComplex(BRat.ONE, BRat.ZERO.i)
    }
}

// Properties

public val FixedBigComplex.conjugate: FixedBigComplex get() = real - imag
public val FixedBigComplex.det: FixedBigRational get() = real * real - imag * imag
public val FixedBigComplex.absoluteValue: FixedBigRational get() = det.sqrt()
public val FixedBigComplex.reciprocal: FixedBigComplex get() = unaryDiv()

// Constructors -- including improper order, eg, 1.i + 1.big

public operator fun BImag.plus(real: BRat): FixedBigComplex = real + this
public operator fun BRat.plus(imag: BImag): FixedBigComplex =
    FixedBigComplex(this, imag)

public operator fun BImag.plus(real: BDouble): FixedBigComplex = real + this
public operator fun BDouble.plus(imag: BImag): FixedBigComplex =
    toBigRational() + imag

public operator fun BImag.plus(real: BInt): FixedBigComplex = real + this
public operator fun BInt.plus(imag: BImag): FixedBigComplex =
    toBigRational() + imag

public operator fun BImag.plus(real: Long): FixedBigComplex = real + this
public operator fun Long.plus(imag: BImag): FixedBigComplex =
    toBigRational() + imag

public operator fun BImag.plus(real: Int): FixedBigComplex = real + this
public operator fun Int.plus(imag: BImag): FixedBigComplex =
    toBigRational() + imag

public operator fun BImag.minus(real: BRat): FixedBigComplex = -real + this
public operator fun BRat.minus(imag: BImag): FixedBigComplex = this + -imag
public operator fun BImag.minus(real: BInt): FixedBigComplex = -real + this
public operator fun BInt.minus(imag: BImag): FixedBigComplex = this + -imag
public operator fun BImag.minus(real: Long): FixedBigComplex = -real + this
public operator fun Long.minus(imag: BImag): FixedBigComplex = this + -imag
public operator fun BImag.minus(real: Int): FixedBigComplex = -real + this
public operator fun Int.minus(imag: BImag): FixedBigComplex = this + -imag

// Addition operator

public operator fun FixedBigComplex.plus(addend: BImag): FixedBigComplex =
    this + (BRat.ZERO + addend)

public operator fun BImag.plus(addend: FixedBigComplex): FixedBigComplex =
    addend + this

public operator fun FixedBigComplex.plus(addend: BRat): FixedBigComplex =
    this + (addend + BRat.ZERO.i)

public operator fun BRat.plus(addend: FixedBigComplex): FixedBigComplex =
    addend + this

public operator fun FixedBigComplex.plus(addend: BInt): FixedBigComplex =
    this + (addend + BRat.ZERO.i)

public operator fun BInt.plus(addend: FixedBigComplex): FixedBigComplex =
    addend + this

public operator fun FixedBigComplex.plus(addend: Long): FixedBigComplex =
    this + (addend + BRat.ZERO.i)

public operator fun Long.plus(addend: FixedBigComplex): FixedBigComplex =
    addend + this

public operator fun FixedBigComplex.plus(addend: Int): FixedBigComplex =
    this + (addend + BRat.ZERO.i)

public operator fun Int.plus(addend: FixedBigComplex): FixedBigComplex =
    addend + this

// Subtraction operator

public operator fun FixedBigComplex.minus(subtrahend: BImag): FixedBigComplex =
    this + -subtrahend

public operator fun BImag.minus(subtrahend: FixedBigComplex): FixedBigComplex =
    this + -subtrahend

public operator fun FixedBigComplex.minus(subtrahend: BRat): FixedBigComplex =
    this + -subtrahend

public operator fun BRat.minus(subtrahend: FixedBigComplex): FixedBigComplex =
    this + -subtrahend

public operator fun FixedBigComplex.minus(subtrahend: BInt): FixedBigComplex =
    this + -subtrahend

public operator fun BInt.minus(subtrahend: FixedBigComplex): FixedBigComplex =
    this + -subtrahend

public operator fun FixedBigComplex.minus(subtrahend: Long): FixedBigComplex =
    this + -subtrahend

public operator fun Long.minus(subtrahend: FixedBigComplex): FixedBigComplex =
    this + -subtrahend

public operator fun FixedBigComplex.minus(subtrahend: Int): FixedBigComplex =
    this + -subtrahend

public operator fun Int.minus(subtrahend: FixedBigComplex): FixedBigComplex =
    this + -subtrahend

// Multiplication operator

public operator fun FixedBigComplex.times(multiplier: BImag): FixedBigComplex =
    this * (FixedBigComplex.ZERO + multiplier)

public operator fun BImag.times(multiplier: FixedBigComplex): FixedBigComplex =
    multiplier * this

public operator fun FixedBigComplex.times(multiplier: BRat): FixedBigComplex =
    this * (multiplier + BRat.ZERO.i)

public operator fun BRat.times(multiplier: FixedBigComplex): FixedBigComplex =
    multiplier * this

public operator fun FixedBigComplex.times(multiplier: BInt): FixedBigComplex =
    this * (multiplier + BRat.ZERO.i)

public operator fun BInt.times(multiplier: FixedBigComplex): FixedBigComplex =
    multiplier * this

public operator fun FixedBigComplex.times(multiplier: Long): FixedBigComplex =
    this * (multiplier + BRat.ZERO.i)

public operator fun Long.times(multiplier: FixedBigComplex): FixedBigComplex =
    multiplier * this

public operator fun FixedBigComplex.times(multiplier: Int): FixedBigComplex =
    this * (multiplier + BRat.ZERO.i)

public operator fun Int.times(multiplier: FixedBigComplex): FixedBigComplex =
    multiplier * this

// Division operator

public operator fun FixedBigComplex.div(divisor: BImag): FixedBigComplex =
    this / (FixedBigComplex.ZERO + divisor)

public operator fun BImag.div(divisor: FixedBigComplex): FixedBigComplex =
    divisor / this

public operator fun FixedBigComplex.div(divisor: BRat): FixedBigComplex =
    real / divisor + (imag.value / divisor).i

public operator fun BRat.div(divisor: FixedBigComplex): FixedBigComplex =
    divisor / this

public operator fun FixedBigComplex.div(divisor: BInt): FixedBigComplex =
    this / divisor.toBigRational()

public operator fun BInt.div(divisor: FixedBigComplex): FixedBigComplex =
    divisor / this

public operator fun FixedBigComplex.div(divisor: Long): FixedBigComplex =
    this / divisor.toBigRational()

public operator fun Long.div(divisor: FixedBigComplex): FixedBigComplex =
    divisor / this

public operator fun FixedBigComplex.div(divisor: Int): FixedBigComplex =
    this / divisor.toBigRational()

public operator fun Int.div(divisor: FixedBigComplex): FixedBigComplex =
    divisor / this

// Functions

public fun FixedBigComplex.toImaginary(): BImag =
    if (real.isZero()) imag
    else throw ArithmeticException("Not imaginary: $this")

public fun FixedBigComplex.toBigRational(): BRat =
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
public infix fun FixedBigComplex.`**`(n: Int): FixedBigComplex = pow(n)

/** @todo Improve on brute force */
public fun FixedBigComplex.pow(n: Int): FixedBigComplex {
    when (n) {
        0 -> return ONE // TODO: What is 0^0?
        1 -> return this
        -1 -> return unaryDiv() // TODO: What is 0^-1?
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
