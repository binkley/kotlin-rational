package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.fixed.FixedBigRational.Companion.ZERO
import hm.binkley.math.plus
import hm.binkley.math.times
import hm.binkley.math.unaryMinus

data class FixedComplex(
    val real: FixedBigRational,
    val imag: FixedImaginary
) {
    operator fun unaryPlus() = this
    operator fun unaryMinus() = -real + -imag

    operator fun plus(addend: FixedComplex) =
        (real + addend.real) + (imag + addend.imag)

    operator fun minus(subtrahend: FixedComplex) = this + -subtrahend

    operator fun times(multiplicand: FixedComplex) =
        (real * multiplicand.real + imag * multiplicand.imag) +
                (real * multiplicand.imag + imag * multiplicand.real)

    override fun toString() =
        if (ZERO > imag.value) "$real-${-imag}" else "$real+$imag"
}

operator fun FixedBigRational.plus(imag: FixedImaginary) =
    FixedComplex(this, imag)

operator fun BInt.plus(imag: FixedImaginary) = toBigRational() + imag
operator fun Long.plus(imag: FixedImaginary) = toBigRational() + imag
operator fun Int.plus(imag: FixedImaginary) = toBigRational() + imag

operator fun FixedImaginary.plus(real: FixedBigRational) = real + this
operator fun FixedImaginary.plus(real: BInt) = real + this
operator fun FixedImaginary.plus(real: Long) = real + this
operator fun FixedImaginary.plus(real: Int) = real + this

operator fun FixedBigRational.minus(imag: FixedImaginary) = this + -imag
operator fun BInt.minus(imag: FixedImaginary) = this + -imag
operator fun Long.minus(imag: FixedImaginary) = this + -imag
operator fun Int.minus(imag: FixedImaginary) = this + -imag

operator fun FixedImaginary.minus(real: FixedBigRational) = -real + this
operator fun FixedImaginary.minus(real: BInt) = -real + this
operator fun FixedImaginary.minus(real: Long) = -real + this
operator fun FixedImaginary.minus(real: Int) = -real + this

operator fun FixedComplex.plus(addend: FixedBigRational) =
    this + (addend + ZERO.i)

operator fun FixedComplex.plus(addend: BInt) = this + (addend + ZERO.i)
operator fun FixedComplex.plus(addend: Long) = this + (addend + ZERO.i)
operator fun FixedComplex.plus(addend: Int) = this + (addend + ZERO.i)

operator fun FixedComplex.plus(addend: FixedImaginary) =
    this + (ZERO + addend)

operator fun FixedBigRational.plus(addend: FixedComplex) = addend + this
operator fun BInt.plus(addend: FixedComplex) = addend + this
operator fun Long.plus(addend: FixedComplex) = addend + this
operator fun Int.plus(addend: FixedComplex) = addend + this
operator fun FixedImaginary.plus(addend: FixedComplex) = addend + this

operator fun FixedComplex.minus(subtrahend: FixedBigRational) =
    this + -subtrahend

operator fun FixedComplex.minus(subtrahend: BInt) = this + -subtrahend
operator fun FixedComplex.minus(subtrahend: Long) = this + -subtrahend
operator fun FixedComplex.minus(subtrahend: Int) = this + -subtrahend
operator fun FixedComplex.minus(subtrahend: FixedImaginary) =
    this + -subtrahend

operator fun FixedBigRational.minus(subtrahend: FixedComplex) =
    this + -subtrahend

operator fun BInt.minus(subtrahend: FixedComplex) =
    this + -subtrahend

operator fun Long.minus(subtrahend: FixedComplex) =
    this + -subtrahend

operator fun Int.minus(subtrahend: FixedComplex) =
    this + -subtrahend

operator fun FixedImaginary.minus(subtrahend: FixedComplex) =
    this + -subtrahend

operator fun FixedComplex.times(multiplicand: FixedBigRational) =
    this * (multiplicand + ZERO.i)

operator fun FixedComplex.times(multiplicand: BInt) =
    this * (multiplicand + ZERO.i)

operator fun FixedComplex.times(multiplicand: Long) =
    this * (multiplicand + ZERO.i)

operator fun FixedComplex.times(multiplicand: Int) =
    this * (multiplicand + ZERO.i)

operator fun FixedComplex.times(multiplicand: FixedImaginary) =
    this * (ZERO + multiplicand)

operator fun FixedBigRational.times(multiplicand: FixedComplex) =
    multiplicand * this

operator fun BInt.times(multiplicand: FixedComplex) = multiplicand * this
operator fun Long.times(multiplicand: FixedComplex) = multiplicand * this
operator fun Int.times(multiplicand: FixedComplex) = multiplicand * this
operator fun FixedImaginary.times(multiplicand: FixedComplex) =
    multiplicand * this
