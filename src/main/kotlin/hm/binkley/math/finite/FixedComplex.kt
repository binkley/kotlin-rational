package hm.binkley.math.finite

import hm.binkley.math.BInt
import hm.binkley.math.finite.FixedBigRational.Companion.ZERO
import hm.binkley.math.plus
import hm.binkley.math.unaryMinus

data class FixedComplex(
    val real: FixedBigRational,
    val imag: FixedImaginary
) {
    operator fun plus(addend: FixedComplex) =
        (real + addend.real) + (imag + addend.imag)

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
