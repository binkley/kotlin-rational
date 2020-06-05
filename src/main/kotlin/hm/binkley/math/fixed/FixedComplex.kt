package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.div
import hm.binkley.math.fixed.FixedBigRational.Companion.ZERO
import hm.binkley.math.plus
import hm.binkley.math.pow
import hm.binkley.math.sqrt
import hm.binkley.math.times
import hm.binkley.math.unaryMinus
import kotlin.math.absoluteValue

data class FixedComplex(
    val real: FixedBigRational,
    val imag: FixedImaginary
) {
    val conjugate get() = real + -imag
    val det get() = real.pow(2) + imag.value.pow(2)
    val absoluteValue get() = det.sqrt()
    val reciprocal: FixedComplex
        get() {
            val det = det
            return real / det - (imag.value / det).i
        }

    operator fun unaryPlus() = this
    operator fun unaryMinus() = -real + -imag

    operator fun plus(addend: FixedComplex) =
        (real + addend.real) + (imag + addend.imag)

    operator fun plus(addend: FixedBigRational) = this + (addend + ZERO.i)
    operator fun plus(addend: BInt) = this + (addend + ZERO.i)
    operator fun plus(addend: Long) = this + (addend + ZERO.i)
    operator fun plus(addend: Int) = this + (addend + ZERO.i)
    operator fun plus(addend: FixedImaginary) = this + (ZERO + addend)

    operator fun minus(subtrahend: FixedComplex) = this + -subtrahend
    operator fun minus(subtrahend: FixedBigRational) = this + -subtrahend
    operator fun minus(subtrahend: BInt) = this + -subtrahend
    operator fun minus(subtrahend: Long) = this + -subtrahend
    operator fun minus(subtrahend: Int) = this + -subtrahend
    operator fun minus(subtrahend: FixedImaginary) = this + -subtrahend

    operator fun times(multiplicand: FixedComplex) =
        (real * multiplicand.real + imag * multiplicand.imag) +
                (real * multiplicand.imag + imag * multiplicand.real)

    operator fun times(multiplicand: FixedBigRational) =
        this * (multiplicand + ZERO.i)

    operator fun times(multiplicand: BInt) =
        this * (multiplicand + ZERO.i)

    operator fun times(multiplicand: Long) =
        this * (multiplicand + ZERO.i)

    operator fun times(multiplicand: Int) =
        this * (multiplicand + ZERO.i)

    operator fun times(multiplicand: FixedImaginary) =
        this * (ZERO + multiplicand)

    operator fun div(divisor: FixedComplex) = this * divisor.reciprocal
    operator fun div(divisor: FixedBigRational) = this / (divisor + ZERO.i)
    operator fun div(divisor: BInt) = this / (divisor + ZERO.i)
    operator fun div(divisor: Long) = this / (divisor + ZERO.i)
    operator fun div(divisor: Int) = this / (divisor + ZERO.i)
    operator fun div(divisor: FixedImaginary) = this / (ZERO + divisor)

    override fun toString() =
        if (ZERO > imag.value) "$real-${-imag}" else "$real+$imag"
}

// Constructors using +/-

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

// Operators with complex on the right side

operator fun FixedBigRational.plus(addend: FixedComplex) = addend + this
operator fun BInt.plus(addend: FixedComplex) = addend + this
operator fun Long.plus(addend: FixedComplex) = addend + this
operator fun Int.plus(addend: FixedComplex) = addend + this
operator fun FixedImaginary.plus(addend: FixedComplex) = addend + this

operator fun FixedBigRational.minus(subtrahend: FixedComplex) =
    this + -subtrahend

operator fun BInt.minus(subtrahend: FixedComplex) = this + -subtrahend
operator fun Long.minus(subtrahend: FixedComplex) = this + -subtrahend
operator fun Int.minus(subtrahend: FixedComplex) = this + -subtrahend
operator fun FixedImaginary.minus(subtrahend: FixedComplex) =
    this + -subtrahend

operator fun FixedBigRational.times(multiplicand: FixedComplex) =
    multiplicand * this

operator fun BInt.times(multiplicand: FixedComplex) = multiplicand * this
operator fun Long.times(multiplicand: FixedComplex) = multiplicand * this
operator fun Int.times(multiplicand: FixedComplex) = multiplicand * this
operator fun FixedImaginary.times(multiplicand: FixedComplex) =
    multiplicand * this

operator fun FixedBigRational.div(divisor: FixedComplex) = divisor / this
operator fun BInt.div(divisor: FixedComplex) = divisor / this
operator fun Long.div(divisor: FixedComplex) = divisor / this
operator fun Int.div(divisor: FixedComplex) = divisor / this
operator fun FixedImaginary.div(divisor: FixedComplex) = divisor / this

// Functions

fun FixedComplex.pow(n: Int): FixedComplex {
    // TODO: Improve on brute force
    when (n) {
        0 -> return 1 + 0.i
        1 -> return this
        -1 -> return reciprocal
    }
    var z = this
    var i = n.absoluteValue
    while (1 < i) {
        z *= this
        --i
    }
    return if (0 > n) z.reciprocal else z
}
