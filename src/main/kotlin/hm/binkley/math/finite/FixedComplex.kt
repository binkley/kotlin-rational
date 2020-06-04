package hm.binkley.math.finite

import hm.binkley.math.BInt
import hm.binkley.math.finite.FixedBigRational.Companion.ZERO
import hm.binkley.math.unaryMinus

data class FixedComplex(
    val real: FixedBigRational,
    val imag: FixedImaginary
) {
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
