package hm.binkley.math.fixed

import hm.binkley.math.BImag
import hm.binkley.math.BInt
import hm.binkley.math.algebra.Field
import hm.binkley.math.algebra.FieldCompanion
import hm.binkley.math.fixed.FixedBigRational.Companion.TWO
import hm.binkley.math.fixed.FixedComplex.Companion.ONE
import hm.binkley.math.isZero
import hm.binkley.math.sqrt
import hm.binkley.math.sqrtApproximated
import java.math.BigInteger
import kotlin.math.absoluteValue

data class FixedComplex(
    val real: BRat,
    val imag: BImag,
) : Field<FixedComplex> {
    override val companion: Companion = FixedComplex

    override operator fun unaryMinus(): FixedComplex = -real + -imag

    /** *NB* &mdash; `1/(a+bi)` is `(a-bi)/(a² + b²)`. */
    override fun unaryDiv(): FixedComplex = conjugate / det

    override operator fun plus(addend: FixedComplex): FixedComplex =
        (real + addend.real) + (imag + addend.imag)

    override operator fun times(multiplier: FixedComplex): FixedComplex =
        (real * multiplier.real + imag * multiplier.imag) +
            (real * multiplier.imag + imag * multiplier.real)

    override operator fun div(divisor: FixedComplex): FixedComplex =
        this * divisor.unaryDiv()

    override fun toString(): String =
        if (BRat.ZERO > imag.value) "$real-${-imag}" else "$real+$imag"

    companion object : FieldCompanion<FixedComplex> {
        override val ZERO: FixedComplex = FixedComplex(BRat.ZERO, BRat.ZERO.i)
        override val ONE: FixedComplex = FixedComplex(BRat.ONE, BRat.ZERO.i)
    }
}

// Properties

val FixedComplex.conjugate: FixedComplex get() = real + -imag
val FixedComplex.det: FixedBigRational get() = real * real - imag * imag
val FixedComplex.absoluteValue: FixedBigRational get() = det.sqrt()
val FixedComplex.reciprocal: FixedComplex get() = unaryDiv()

// Constructors -- including improper order, eg, -1.i + 1.big

operator fun BImag.plus(real: BRat): FixedComplex = real + this
operator fun BRat.plus(imag: BImag): FixedComplex =
    FixedComplex(this, imag)

operator fun BImag.plus(real: BInt): FixedComplex = real + this
operator fun BInt.plus(imag: BImag): FixedComplex =
    toBigRational() + imag

operator fun BImag.plus(real: Long): FixedComplex = real + this
operator fun Long.plus(imag: BImag): FixedComplex =
    toBigRational() + imag

operator fun BImag.plus(real: Int): FixedComplex = real + this
operator fun Int.plus(imag: BImag): FixedComplex =
    toBigRational() + imag

operator fun BImag.minus(real: BRat): FixedComplex = -real + this
operator fun BRat.minus(imag: BImag): FixedComplex = this + -imag
operator fun BImag.minus(real: BInt): FixedComplex = -real + this
operator fun BInt.minus(imag: BImag): FixedComplex = this + -imag
operator fun BImag.minus(real: Long): FixedComplex = -real + this
operator fun Long.minus(imag: BImag): FixedComplex = this + -imag
operator fun BImag.minus(real: Int): FixedComplex = -real + this
operator fun Int.minus(imag: BImag): FixedComplex = this + -imag

// Addition operator

operator fun FixedComplex.plus(addend: BImag): FixedComplex =
    this + (BRat.ZERO + addend)

operator fun BImag.plus(addend: FixedComplex): FixedComplex = addend + this

operator fun FixedComplex.plus(addend: BRat): FixedComplex =
    this + (addend + BRat.ZERO.i)

operator fun BRat.plus(addend: FixedComplex): FixedComplex = addend + this

operator fun FixedComplex.plus(addend: BInt): FixedComplex =
    this + (addend + BRat.ZERO.i)

operator fun BInt.plus(addend: FixedComplex): FixedComplex = addend + this

operator fun FixedComplex.plus(addend: Long): FixedComplex =
    this + (addend + BRat.ZERO.i)

operator fun Long.plus(addend: FixedComplex): FixedComplex = addend + this

operator fun FixedComplex.plus(addend: Int): FixedComplex =
    this + (addend + BRat.ZERO.i)

operator fun Int.plus(addend: FixedComplex): FixedComplex = addend + this

// Subtraction operator

operator fun FixedComplex.minus(subtrahend: BImag): FixedComplex =
    this + -subtrahend

operator fun BImag.minus(subtrahend: FixedComplex): FixedComplex =
    this + -subtrahend

operator fun FixedComplex.minus(subtrahend: BRat): FixedComplex =
    this + -subtrahend

operator fun BRat.minus(subtrahend: FixedComplex): FixedComplex =
    this + -subtrahend

operator fun FixedComplex.minus(subtrahend: BInt): FixedComplex =
    this + -subtrahend

operator fun BInt.minus(subtrahend: FixedComplex): FixedComplex =
    this + -subtrahend

operator fun FixedComplex.minus(subtrahend: Long): FixedComplex =
    this + -subtrahend

operator fun Long.minus(subtrahend: FixedComplex): FixedComplex =
    this + -subtrahend

operator fun FixedComplex.minus(subtrahend: Int): FixedComplex =
    this + -subtrahend

operator fun Int.minus(subtrahend: FixedComplex): FixedComplex =
    this + -subtrahend

// Multiplication operator

operator fun FixedComplex.times(multiplier: BImag): FixedComplex =
    this * (FixedComplex.ZERO + multiplier)

operator fun BImag.times(multiplier: FixedComplex): FixedComplex =
    multiplier * this

operator fun FixedComplex.times(multiplier: BRat): FixedComplex =
    this * (multiplier + BRat.ZERO.i)

operator fun BRat.times(multiplier: FixedComplex): FixedComplex =
    multiplier * this

operator fun FixedComplex.times(multiplier: BInt): FixedComplex =
    this * (multiplier + BRat.ZERO.i)

operator fun BInt.times(multiplier: FixedComplex): FixedComplex =
    multiplier * this

operator fun FixedComplex.times(multiplier: Long): FixedComplex =
    this * (multiplier + BRat.ZERO.i)

operator fun Long.times(multiplier: FixedComplex): FixedComplex =
    multiplier * this

operator fun FixedComplex.times(multiplier: Int): FixedComplex =
    this * (multiplier + BRat.ZERO.i)

operator fun Int.times(multiplier: FixedComplex): FixedComplex =
    multiplier * this

// Division operator

operator fun FixedComplex.div(divisor: BImag): FixedComplex =
    this / (FixedComplex.ZERO + divisor)

operator fun BImag.div(divisor: FixedComplex): FixedComplex = divisor / this

operator fun FixedComplex.div(divisor: BRat): FixedComplex =
    real / divisor + (imag.value / divisor).i

operator fun BRat.div(divisor: FixedComplex): FixedComplex = divisor / this

operator fun FixedComplex.div(divisor: BInt): FixedComplex =
    this / divisor.toBigRational()

operator fun BInt.div(divisor: FixedComplex): FixedComplex = divisor / this

operator fun FixedComplex.div(divisor: Long): FixedComplex =
    this / divisor.toBigRational()

operator fun Long.div(divisor: FixedComplex): FixedComplex = divisor / this

operator fun FixedComplex.div(divisor: Int): FixedComplex =
    this / divisor.toBigRational()

operator fun Int.div(divisor: FixedComplex): FixedComplex = divisor / this

// Functions

fun FixedComplex.toImaginary(): BImag =
    if (real.isZero()) imag
    else throw ArithmeticException("Not imaginary: $this")

fun FixedComplex.toBigRational(): BRat =
    if (imag.isZero()) real
    else throw ArithmeticException("Not real: $this")

fun FixedComplex.toBigInteger(): BigInteger =
    if (imag.isZero()) real.toBigInteger()
    else throw ArithmeticException("Not real: $this")

fun FixedComplex.toLong(): Long =
    if (imag.isZero()) real.toLong()
    else throw ArithmeticException("Not real: $this")

fun FixedComplex.toInt(): Int =
    if (imag.isZero()) real.toInt()
    else throw ArithmeticException("Not real: $this")

fun FixedComplex.modulusApproximated(): FixedBigRational =
    det.sqrtApproximated()

fun FixedComplex.sqrtApproximated(): FixedComplex {
    val gamma = ((real + modulusApproximated()) / TWO).sqrtApproximated()
    val delta = imag.value.sign *
        ((-real + modulusApproximated()) / TWO).sqrtApproximated()
    return gamma + delta.i
}

@Suppress("DANGEROUS_CHARACTERS")
infix fun FixedComplex.`**`(n: Int): FixedComplex = pow(n)

/** @todo Improve on brute force */
fun FixedComplex.pow(n: Int): FixedComplex {
    when (n) {
        0 -> return ONE // TODO: What is 0^0?
        1 -> return this
        -1 -> return unaryDiv() // TODO: What is 0^-1?
    }

    tailrec fun FixedComplex.pow0(
        exponent: Int,
        current: FixedComplex,
    ): FixedComplex = when (exponent) {
        0 -> current
        else -> pow0(exponent - 1, this * current)
    }

    val z = pow0(n.absoluteValue - 1, this)

    return if (0 > n) z.unaryDiv() else z
}
