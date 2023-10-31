package hm.binkley.math.fixed.complex

import hm.binkley.math.BFixed
import hm.binkley.math.algebra.Field
import hm.binkley.math.algebra.FieldCompanion
import hm.binkley.math.fixed.FixedBigRational.Companion.TWO
import hm.binkley.math.fixed.complex.FixedBigComplex.Companion.ONE
import hm.binkley.math.fixed.toBigRational
import hm.binkley.math.isZero
import hm.binkley.math.sqrt
import hm.binkley.math.sqrtApproximated
import hm.binkley.math.toBigInteger
import java.math.BigInteger
import kotlin.math.absoluteValue

/** Complex numbers of big fixed rationals. */
public data class FixedBigComplex(
    /** The real part of the complex number. */
    val real: BRat,
    /** The imaginary part of the complex number. */
    val imag: BImag,
) : Field<FixedBigComplex> {
    override val companion: Companion get() = Companion

    override operator fun unaryMinus(): BComplex = -real + -imag

    /**
     * Returns the multiplicative inverse.
     * - [inv] is a sensible alternative name, however has a bitwise meaning
     *   for primitives
     *
     * *NB* &mdash; `1/(a+bi)` is `(a-bi)/(a² + b²)`.
     */
    override fun unaryDiv(): BComplex = conjugate / det

    override operator fun plus(addend: BComplex): BComplex =
        (real + addend.real) + (imag + addend.imag)

    override operator fun times(multiplicand: BComplex): BComplex =
        (real * multiplicand.real + imag * multiplicand.imag) +
            (real * multiplicand.imag + imag * multiplicand.real)

    override operator fun div(divisor: BComplex): BComplex =
        this * divisor.unaryDiv()

    override fun toString(): String = if (BRat.ZERO > imag.value) {
        "$real-${-imag}"
    } else {
        "$real+$imag"
    }

    public companion object : FieldCompanion<BComplex> {
        @Suppress("ktlint:standard:property-naming")
        override val ZERO: BComplex = BComplex(BRat.ZERO, BRat.ZERO.i)

        @Suppress("ktlint:standard:property-naming")
        override val ONE: BComplex = BComplex(BRat.ONE, BRat.ZERO.i)
    }
}

// Properties

/** The complex conjugate. */
public val BComplex.conjugate: BComplex get() = real - imag

/** The determinant of the matrix representing this complex number. */
public val BComplex.det: BRat get() = real * real - imag * imag

/** The complex absolute value (magnitude, modulus). */
public val BComplex.absoluteValue: BRat get() = det.sqrt()

/** The complex reciprocal. */
public val BComplex.reciprocal: BComplex get() = unaryDiv()

// Factories, plus and minus real+imag (include improper order like "i+1")

/** Creates a new complex number. */
public operator fun BRat.plus(imag: BImag): BComplex = BComplex(this, imag)

/** Creates a new complex number. */
public operator fun BImag.plus(real: BRat): BComplex = real + this

/** Creates a new complex number. */
public operator fun BFixed.plus(imag: BImag): BComplex = toBigRational() + imag

/** Creates a new complex number. */
public operator fun BImag.plus(real: BFixed): BComplex = real + this

/** Creates a new complex number. */
public operator fun Long.plus(imag: BImag): BComplex = toBigRational() + imag

/** Creates a new complex number. */
public operator fun BImag.plus(real: Long): BComplex = real + this

/** Creates a new complex number. */
public operator fun Int.plus(imag: BImag): BComplex = toBigRational() + imag

/** Creates a new complex number. */
public operator fun BImag.plus(real: Int): BComplex = real + this

/** Creates a new complex number. */
public operator fun BImag.minus(real: BRat): BComplex = -real + this

/** Creates a new complex number. */
public operator fun BRat.minus(imag: BImag): BComplex = this + -imag

/** Creates a new complex number. */
public operator fun BImag.minus(real: BFixed): BComplex = -real + this

/** Creates a new complex number. */
public operator fun BFixed.minus(imag: BImag): BComplex = this + -imag

/** Creates a new complex number. */
public operator fun BImag.minus(real: Long): BComplex = -real + this

/** Creates a new complex number. */
public operator fun Long.minus(imag: BImag): BComplex = this + -imag

/** Creates a new complex number. */
public operator fun BImag.minus(real: Int): BComplex = -real + this

/** Creates a new complex number. */
public operator fun Int.minus(imag: BImag): BComplex = this + -imag

// Addition operator

/** Adds [addend] to this number. */
public operator fun BComplex.plus(addend: BImag): BComplex =
    this + (BRat.ZERO + addend)

/** Adds [addend] to this number. */
public operator fun BImag.plus(addend: BComplex): BComplex = addend + this

/** Adds [addend] to this number. */
public operator fun BRat.plus(addend: BComplex): BComplex = addend + this

/** Adds [addend] to this number. */
public operator fun BComplex.plus(addend: BRat): BComplex =
    this + (addend + BRat.ZERO.i)

/** Adds [addend] to this number. */
public operator fun BFixed.plus(addend: BComplex): BComplex = addend + this

/** Adds [addend] to this number. */
public operator fun BComplex.plus(addend: BFixed): BComplex =
    this + (addend + BRat.ZERO.i)

/** Adds [addend] to this number. */
public operator fun Long.plus(addend: BComplex): BComplex = addend + this

/** Adds [addend] to this number. */
public operator fun BComplex.plus(addend: Long): BComplex =
    this + (addend + BRat.ZERO.i)

/** Adds [addend] to this number. */
public operator fun Int.plus(addend: BComplex): BComplex = addend + this

/** Adds [addend] to this number. */
public operator fun BComplex.plus(addend: Int): BComplex =
    this + (addend + BRat.ZERO.i)

// Subtraction operator

/** Subtracts [subtrahend] from this number. */
public operator fun BComplex.minus(subtrahend: BImag): BComplex =
    this + -subtrahend

/** Subtracts [subtrahend] from this number. */
public operator fun BImag.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

/** Subtracts [subtrahend] from this number. */
public operator fun BRat.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

/** Subtracts [subtrahend] from this number. */
public operator fun BComplex.minus(subtrahend: BRat): BComplex =
    this + -subtrahend

/** Subtracts [subtrahend] from this number. */
public operator fun BFixed.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

/** Subtracts [subtrahend] from this number. */
public operator fun BComplex.minus(subtrahend: BFixed): BComplex =
    this + -subtrahend

/** Subtracts [subtrahend] from this number. */
public operator fun Long.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

/** Subtracts [subtrahend] from this number. */
public operator fun BComplex.minus(subtrahend: Long): BComplex =
    this + -subtrahend

/** Subtracts [subtrahend] from this number. */
public operator fun Int.minus(subtrahend: BComplex): BComplex =
    this + -subtrahend

/** Subtracts [subtrahend] from this number. */
public operator fun BComplex.minus(subtrahend: Int): BComplex =
    this + -subtrahend

// Multiplication operator

/** Multiplies this number by [multiplicand]. */
public operator fun BComplex.times(multiplicand: BImag): BComplex =
    this * (BComplex.ZERO + multiplicand)

/** Multiplies this number by [multiplicand]. */
public operator fun BImag.times(multiplicand: BComplex): BComplex =
    multiplicand * this

/** Multiplies this number by [multiplicand]. */
public operator fun BRat.times(multiplicand: BComplex): BComplex =
    multiplicand * this

/** Multiplies this number by [multiplicand]. */
public operator fun BComplex.times(multiplicand: BRat): BComplex =
    this * (multiplicand + BRat.ZERO.i)

/** Multiplies this number by [multiplicand]. */
public operator fun BFixed.times(multiplicand: BComplex): BComplex =
    multiplicand * this

/** Multiplies this number by [multiplicand]. */
public operator fun BComplex.times(multiplicand: BFixed): BComplex =
    this * (multiplicand + BRat.ZERO.i)

/** Multiplies this number by [multiplicand]. */
public operator fun Long.times(multiplicand: BComplex): BComplex =
    multiplicand * this

/** Multiplies this number by [multiplicand]. */
public operator fun BComplex.times(multiplicand: Long): BComplex =
    this * (multiplicand + BRat.ZERO.i)

/** Multiplies this number by [multiplicand]. */
public operator fun Int.times(multiplicand: BComplex): BComplex =
    multiplicand * this

/** Multiplies this number by [multiplicand]. */
public operator fun BComplex.times(multiplicand: Int): BComplex =
    this * (multiplicand + BRat.ZERO.i)

// Division operator

/** Divides this number by [divisor]. */
public operator fun BComplex.div(divisor: BImag): BComplex =
    this / (BComplex.ZERO + divisor)

/** Divides this number by [divisor]. */
public operator fun BImag.div(divisor: BComplex): BComplex = divisor / this

/** Divides this number by [divisor]. */
public operator fun BRat.div(divisor: BComplex): BComplex = divisor / this

/** Divides this number by [divisor]. */
public operator fun BComplex.div(divisor: BRat): BComplex =
    real / divisor + (imag.value / divisor).i

/** Divides this number by [divisor]. */
public operator fun BFixed.div(divisor: BComplex): BComplex = divisor / this

/** Divides this number by [divisor]. */
public operator fun BComplex.div(divisor: BFixed): BComplex =
    this / divisor.toBigRational()

/** Divides this number by [divisor]. */
public operator fun Long.div(divisor: BComplex): BComplex = divisor / this

/** Divides this number by [divisor]. */
public operator fun BComplex.div(divisor: Long): BComplex =
    this / divisor.toBigRational()

/** Divides this number by [divisor]. */
public operator fun Int.div(divisor: BComplex): BComplex = divisor / this

/** Divides this number by [divisor]. */
public operator fun BComplex.div(divisor: Int): BComplex =
    this / divisor.toBigRational()

// Functions

/**
 * Returns an imaginary number if this number is pure imaginary, else raises
 * [ArithmeticException].
 */
public fun BComplex.toImaginary(): BImag = if (real.isZero()) {
    imag
} else {
    throw ArithmeticException("Not imaginary: $this")
}

/**
 * Returns a real number if this number is purely real, else raises
 * [ArithmeticException].
 */
public fun BComplex.toBigRational(): BRat = if (imag.isZero()) {
    real
} else {
    throw ArithmeticException("Not real: $this")
}

/**
 * Returns a real number if this number is purely real, else raises
 * [ArithmeticException].
 */
public fun BComplex.toBigInteger(): BigInteger = if (imag.isZero()) {
    real.toBigInteger()
} else {
    throw ArithmeticException("Not real: $this")
}

/**
 * Returns a real number if this number is purely real, else raises
 * [ArithmeticException].
 */
public fun BComplex.toLong(): Long = if (imag.isZero()) {
    real.toLong()
} else {
    throw ArithmeticException("Not real: $this")
}

/**
 * Returns a real number if this number is purely real, else raises
 * [ArithmeticException].
 */
public fun BComplex.toInt(): Int = if (imag.isZero()) {
    real.toInt()
} else {
    throw ArithmeticException("Not real: $this")
}

/** Returns the approximate modulus (root of the determinant). */
public fun BComplex.modulusApproximated(): BRat = det.sqrtApproximated()

/** Returns the approximate square root. */
public fun BComplex.sqrtApproximated(): BComplex {
    val gamma = ((real + modulusApproximated()) / TWO).sqrtApproximated()
    val delta = imag.value.sign *
        ((-real + modulusApproximated()) / TWO).sqrtApproximated()
    return gamma + delta.i
}

/**
 * Pseudo-operator for raising this number to the [exponent] power.
 * - The [xor] infix function has no `^` syntax, and would be a confusing
 *   name in this context
 * - `**` is a sensible alternative name to `^`
 */
@Suppress("FunctionName")
public infix fun BComplex.`^`(exponent: Int): BComplex = pow(exponent)

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
