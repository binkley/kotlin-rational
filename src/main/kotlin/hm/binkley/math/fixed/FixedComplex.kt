package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.algebra.Ring
import hm.binkley.math.algebra.RingCompanion
import hm.binkley.math.pow
import hm.binkley.math.sqrt
import kotlin.math.absoluteValue

data class FixedComplex(
    val real: BRat,
    val imag: FixedImaginary
) : Ring<FixedComplex> {
    override val companion = Companion

    val conjugate get() = real + -imag
    val det get() = real.pow(2) + imag.value.pow(2)
    val absoluteValue get() = det.sqrt()
    val reciprocal: FixedComplex get() = unaryDiv()

    override operator fun unaryMinus() = -real + -imag

    fun unaryDiv(): FixedComplex {
        val det = det
        return real / det - (imag.value / det).i
    }

    override operator fun plus(addend: FixedComplex) =
        (real + addend.real) + (imag + addend.imag)

    operator fun plus(addend: BRat) = this + (addend + BRat.ZERO.i)
    operator fun plus(addend: BInt) = this + (addend + BRat.ZERO.i)
    operator fun plus(addend: Long) = this + (addend + BRat.ZERO.i)
    operator fun plus(addend: Int) = this + (addend + BRat.ZERO.i)
    operator fun plus(addend: FixedImaginary) = this + (BRat.ZERO + addend)

    operator fun minus(subtrahend: BRat) = this + -subtrahend
    operator fun minus(subtrahend: BInt) = this + -subtrahend
    operator fun minus(subtrahend: Long) = this + -subtrahend
    operator fun minus(subtrahend: Int) = this + -subtrahend
    operator fun minus(subtrahend: FixedImaginary) = this + -subtrahend

    override operator fun times(multiplier: FixedComplex) =
        (real * multiplier.real + imag * multiplier.imag) +
            (real * multiplier.imag + imag * multiplier.real)

    operator fun times(multiplier: BRat) =
        this * (multiplier + BRat.ZERO.i)

    operator fun times(multiplier: BInt) =
        this * (multiplier + BRat.ZERO.i)

    operator fun times(multiplier: Long) =
        this * (multiplier + BRat.ZERO.i)

    operator fun times(multiplier: Int) =
        this * (multiplier + BRat.ZERO.i)

    operator fun times(multiplier: FixedImaginary) =
        this * (ZERO + multiplier)

    operator fun div(divisor: FixedComplex) = this * divisor.unaryDiv()
    operator fun div(divisor: BRat) = this / (divisor + BRat.ZERO.i)
    operator fun div(divisor: BInt) = this / (divisor + BRat.ZERO.i)
    operator fun div(divisor: Long) = this / (divisor + BRat.ZERO.i)
    operator fun div(divisor: Int) = this / (divisor + BRat.ZERO.i)
    operator fun div(divisor: FixedImaginary) = this / (ZERO + divisor)

    override fun toString() =
        if (BRat.ZERO > imag.value) "$real-${-imag}" else "$real+$imag"

    companion object : RingCompanion<FixedComplex> {
        override val ONE =
            FixedComplex(BRat.ONE, BRat.ZERO.i)
        override val ZERO =
            FixedComplex(BRat.ZERO, BRat.ZERO.i)
    }
}

// Constructors using +/-

operator fun BRat.plus(imag: FixedImaginary) =
    FixedComplex(this, imag)

operator fun BInt.plus(imag: FixedImaginary) = toBigRational() + imag
operator fun Long.plus(imag: FixedImaginary) = toBigRational() + imag
operator fun Int.plus(imag: FixedImaginary) = toBigRational() + imag

operator fun FixedImaginary.plus(real: BRat) = real + this
operator fun FixedImaginary.plus(real: BInt) = real + this
operator fun FixedImaginary.plus(real: Long) = real + this
operator fun FixedImaginary.plus(real: Int) = real + this

operator fun BRat.minus(imag: FixedImaginary) = this + -imag
operator fun BInt.minus(imag: FixedImaginary) = this + -imag
operator fun Long.minus(imag: FixedImaginary) = this + -imag
operator fun Int.minus(imag: FixedImaginary) = this + -imag

operator fun FixedImaginary.minus(real: BRat) = -real + this
operator fun FixedImaginary.minus(real: BInt) = -real + this
operator fun FixedImaginary.minus(real: Long) = -real + this
operator fun FixedImaginary.minus(real: Int) = -real + this

// Operators with complex on the right side

operator fun BRat.plus(addend: FixedComplex) = addend + this
operator fun BInt.plus(addend: FixedComplex) = addend + this
operator fun Long.plus(addend: FixedComplex) = addend + this
operator fun Int.plus(addend: FixedComplex) = addend + this
operator fun FixedImaginary.plus(addend: FixedComplex) = addend + this

operator fun BRat.minus(subtrahend: FixedComplex) =
    this + -subtrahend

operator fun BInt.minus(subtrahend: FixedComplex) = this + -subtrahend
operator fun Long.minus(subtrahend: FixedComplex) = this + -subtrahend
operator fun Int.minus(subtrahend: FixedComplex) = this + -subtrahend
operator fun FixedImaginary.minus(subtrahend: FixedComplex) =
    this + -subtrahend

operator fun BRat.times(multiplier: FixedComplex) =
    multiplier * this

operator fun BInt.times(multiplier: FixedComplex) = multiplier * this
operator fun Long.times(multiplier: FixedComplex) = multiplier * this
operator fun Int.times(multiplier: FixedComplex) = multiplier * this
operator fun FixedImaginary.times(multiplier: FixedComplex) =
    multiplier * this

operator fun BRat.div(divisor: FixedComplex) = divisor / this
operator fun BInt.div(divisor: FixedComplex) = divisor / this
operator fun Long.div(divisor: FixedComplex) = divisor / this
operator fun Int.div(divisor: FixedComplex) = divisor / this
operator fun FixedImaginary.div(divisor: FixedComplex) = divisor / this

// Functions

fun FixedComplex.toBigRational() =
    if (BRat.ZERO == imag.value) real
    else throw ArithmeticException("Not real: $this")

fun FixedComplex.toBigInteger() =
    if (BRat.ZERO == imag.value) real.toBigInteger()
    else throw ArithmeticException("Not real: $this")

fun FixedComplex.toLong() =
    if (BRat.ZERO == imag.value) real.toLong()
    else throw ArithmeticException("Not real: $this")

fun FixedComplex.toInt() =
    if (BRat.ZERO == imag.value) real.toInt()
    else throw ArithmeticException("Not real: $this")

fun FixedComplex.toImaginary() =
    if (BRat.ZERO == real) imag
    else throw ArithmeticException("Not imaginary: $this")

fun FixedComplex.pow(n: Int): FixedComplex {
    // TODO: Improve on brute force
    when (n) {
        0 -> return 1 + 0.i
        1 -> return this
        -1 -> return unaryDiv()
    }
    var z = this
    var i = n.absoluteValue
    while (1 < i) {
        z *= this
        --i
    }
    return if (0 > n) z.unaryDiv() else z
}
