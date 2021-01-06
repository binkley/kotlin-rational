package hm.binkley.math.fixed

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
    val imag: FixedImaginary,
) : Field<FixedComplex> {
    override val companion: Companion = Companion

    val conjugate: FixedComplex get() = real + -imag
    val det: FixedBigRational get() = real * real - imag * imag
    val absoluteValue: FixedBigRational get() = det.sqrt()
    val reciprocal: FixedComplex get() = unaryDiv()

    override operator fun unaryMinus(): FixedComplex = -real + -imag

    /** *NB* &mdash; `1/(a+bi)` is `(a-bi)/(a² + b²)`. */
    override fun unaryDiv(): FixedComplex = conjugate / det

    override operator fun plus(addend: FixedComplex): FixedComplex =
        (real + addend.real) + (imag + addend.imag)

    operator fun plus(addend: BRat): FixedComplex =
        this + (addend + BRat.ZERO.i)

    operator fun plus(addend: BInt): FixedComplex =
        this + (addend + BRat.ZERO.i)

    operator fun plus(addend: Long): FixedComplex =
        this + (addend + BRat.ZERO.i)

    operator fun plus(addend: Int): FixedComplex =
        this + (addend + BRat.ZERO.i)

    operator fun plus(addend: FixedImaginary): FixedComplex =
        this + (BRat.ZERO + addend)

    operator fun minus(subtrahend: BRat): FixedComplex = this + -subtrahend
    operator fun minus(subtrahend: BInt): FixedComplex = this + -subtrahend
    operator fun minus(subtrahend: Long): FixedComplex = this + -subtrahend
    operator fun minus(subtrahend: Int): FixedComplex = this + -subtrahend
    operator fun minus(subtrahend: FixedImaginary): FixedComplex =
        this + -subtrahend

    override operator fun times(multiplier: FixedComplex): FixedComplex =
        (real * multiplier.real + imag * multiplier.imag) +
            (real * multiplier.imag + imag * multiplier.real)

    operator fun times(multiplier: BRat): FixedComplex =
        this * (multiplier + BRat.ZERO.i)

    operator fun times(multiplier: BInt): FixedComplex =
        this * (multiplier + BRat.ZERO.i)

    operator fun times(multiplier: Long): FixedComplex =
        this * (multiplier + BRat.ZERO.i)

    operator fun times(multiplier: Int): FixedComplex =
        this * (multiplier + BRat.ZERO.i)

    operator fun times(multiplier: FixedImaginary): FixedComplex =
        this * (ZERO + multiplier)

    override operator fun div(divisor: FixedComplex): FixedComplex =
        this * divisor.unaryDiv()

    operator fun div(divisor: FixedImaginary): FixedComplex =
        this / (ZERO + divisor)

    operator fun div(divisor: BRat): FixedComplex =
        real / divisor + (imag.value / divisor).i

    operator fun div(divisor: BInt): FixedComplex =
        this / divisor.toBigRational()

    operator fun div(divisor: Long): FixedComplex =
        this / divisor.toBigRational()

    operator fun div(divisor: Int): FixedComplex =
        this / divisor.toBigRational()

    override fun toString(): String =
        if (BRat.ZERO > imag.value) "$real-${-imag}" else "$real+$imag"

    companion object : FieldCompanion<FixedComplex> {
        override val ONE: FixedComplex =
            FixedComplex(BRat.ONE, BRat.ZERO.i)
        override val ZERO: FixedComplex =
            FixedComplex(BRat.ZERO, BRat.ZERO.i)
    }
}

// Constructors using +/-

operator fun BRat.plus(imag: FixedImaginary): FixedComplex =
    FixedComplex(this, imag)

operator fun BInt.plus(imag: FixedImaginary): FixedComplex =
    toBigRational() + imag

operator fun Long.plus(imag: FixedImaginary): FixedComplex =
    toBigRational() + imag

operator fun Int.plus(imag: FixedImaginary): FixedComplex =
    toBigRational() + imag

operator fun FixedImaginary.plus(real: BRat): FixedComplex = real + this
operator fun FixedImaginary.plus(real: BInt): FixedComplex = real + this
operator fun FixedImaginary.plus(real: Long): FixedComplex = real + this
operator fun FixedImaginary.plus(real: Int): FixedComplex = real + this

operator fun BRat.minus(imag: FixedImaginary): FixedComplex = this + -imag
operator fun BInt.minus(imag: FixedImaginary): FixedComplex = this + -imag
operator fun Long.minus(imag: FixedImaginary): FixedComplex = this + -imag
operator fun Int.minus(imag: FixedImaginary): FixedComplex = this + -imag

operator fun FixedImaginary.minus(real: BRat): FixedComplex = -real + this
operator fun FixedImaginary.minus(real: BInt): FixedComplex = -real + this
operator fun FixedImaginary.minus(real: Long): FixedComplex = -real + this
operator fun FixedImaginary.minus(real: Int): FixedComplex = -real + this

// Operators with complex on the right side

operator fun BRat.plus(addend: FixedComplex): FixedComplex = addend + this
operator fun BInt.plus(addend: FixedComplex): FixedComplex = addend + this
operator fun Long.plus(addend: FixedComplex): FixedComplex = addend + this
operator fun Int.plus(addend: FixedComplex): FixedComplex = addend + this
operator fun FixedImaginary.plus(addend: FixedComplex): FixedComplex =
    addend + this

operator fun BRat.minus(subtrahend: FixedComplex): FixedComplex =
    this + -subtrahend

operator fun BInt.minus(subtrahend: FixedComplex): FixedComplex =
    this + -subtrahend

operator fun Long.minus(subtrahend: FixedComplex): FixedComplex =
    this + -subtrahend

operator fun Int.minus(subtrahend: FixedComplex): FixedComplex =
    this + -subtrahend

operator fun FixedImaginary.minus(subtrahend: FixedComplex): FixedComplex =
    this + -subtrahend

operator fun BRat.times(multiplier: FixedComplex): FixedComplex =
    multiplier * this

operator fun BInt.times(multiplier: FixedComplex): FixedComplex =
    multiplier * this

operator fun Long.times(multiplier: FixedComplex): FixedComplex =
    multiplier * this

operator fun Int.times(multiplier: FixedComplex): FixedComplex =
    multiplier * this

operator fun FixedImaginary.times(multiplier: FixedComplex): FixedComplex =
    multiplier * this

operator fun BRat.div(divisor: FixedComplex): FixedComplex = divisor / this
operator fun BInt.div(divisor: FixedComplex): FixedComplex = divisor / this
operator fun Long.div(divisor: FixedComplex): FixedComplex = divisor / this
operator fun Int.div(divisor: FixedComplex): FixedComplex = divisor / this
operator fun FixedImaginary.div(divisor: FixedComplex): FixedComplex =
    divisor / this

// Functions

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

fun FixedComplex.toImaginary(): FixedImaginary =
    if (real.isZero()) imag
    else throw ArithmeticException("Not imaginary: $this")

fun FixedComplex.modulusApproximated(): FixedBigRational =
    det.sqrtApproximated()

fun FixedComplex.sqrtApproximated(): FixedComplex {
    val gamma =
        ((real + modulusApproximated()) / TWO).sqrtApproximated()
    val delta = imag.value.sign *
        ((-real + modulusApproximated()) / TWO).sqrtApproximated()
    return gamma + delta.i
}

@Suppress("DANGEROUS_CHARACTERS")
infix fun FixedComplex.`**`(n: Int): FixedComplex = pow(n)

fun FixedComplex.pow(n: Int): FixedComplex {
    // TODO: Improve on brute force
    when (n) {
        0 -> return ONE // TODO: What is 0^0?
        1 -> return this
        -1 -> return unaryDiv() // TODO: What is 0^-1?
    }

    val z = pow0(n.absoluteValue - 1, this, this)

    return if (0 > n) z.unaryDiv() else z
}

private tailrec fun pow0(
    exponent: Int,
    power: FixedComplex,
    base: FixedComplex,
): FixedComplex =
    if (0 == exponent) power
    else pow0(exponent - 1, power * base, base)
