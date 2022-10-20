package hm.binkley.math.fixed.complex

import hm.binkley.math.BFixed
import hm.binkley.math.algebra.Group
import hm.binkley.math.algebra.GroupCompanion
import hm.binkley.math.div
import hm.binkley.math.fixed.toBigRational
import hm.binkley.math.isZero
import hm.binkley.math.times

/**
 * Imaginary numbers of big fixed rationals.
 *
 * @todo A value class may be a better choice than a data class
 */
public data class FixedBigImaginary(
    /** The big rational quantity of `i`. */
    val value: BRat
) : Group<FixedBigImaginary>,
    Comparable<FixedBigImaginary> {
    override val companion: Companion get() = Companion

    override fun unaryMinus(): BImag = (-value).toImaginary()

    override fun plus(addend: BImag): BImag =
        (value + addend.value).toImaginary()

    override fun compareTo(other: BImag): Int = value.compareTo(other.value)

    override fun toString(): String = "${value}i"

    public companion object : GroupCompanion<BImag> {
        override val ZERO: BImag = 0.i

        /** The imaginary unit. */
        @JvmField
        public val I: BImag = 1.i
    }
}

// Factories

/** Creates a new imaginary number. */
public fun BRat.toImaginary(): BImag = BImag(this)

/** Creates a new imaginary number. */
public inline val BRat.i: BImag get() = toImaginary()

/** Creates a new imaginary number. */
public fun BFixed.toImaginary(): BImag = toBigRational().toImaginary()

/** Creates a new imaginary number. */
public inline val BFixed.i: BImag get() = toImaginary()

/** Creates a new imaginary number. */
public fun Long.toImaginary(): BImag = toBigRational().toImaginary()

/** Creates a new imaginary number. */
public inline val Long.i: BImag get() = toImaginary()

/** Creates a new imaginary number. */
public fun Int.toImaginary(): BImag = toBigRational().toImaginary()

/** Creates a new imaginary number. */
public inline val Int.i: BImag get() = toImaginary()

// Multiplication operator

/** Multiplies this number by [multiplicand]. */
public operator fun BImag.times(multiplicand: BImag): BRat =
    -(value * multiplicand.value)

/** Multiplies this number by [multiplicand]. */
public operator fun BRat.times(multiplicand: BImag): BImag = multiplicand * this

/** Multiplies this number by [multiplicand]. */
public operator fun BImag.times(multiplicand: BRat): BImag =
    (value * multiplicand).toImaginary()

/** Multiplies this number by [multiplicand]. */
public operator fun BFixed.times(multiplicand: BImag): BImag =
    multiplicand * this

/** Multiplies this number by [multiplicand]. */
public operator fun BImag.times(multiplicand: BFixed): BImag =
    (value * multiplicand).toImaginary()

/** Multiplies this number by [multiplicand]. */
public operator fun Long.times(multiplicand: BImag): BImag = multiplicand * this

/** Multiplies this number by [multiplicand]. */
public operator fun BImag.times(multiplicand: Long): BImag =
    (value * multiplicand).toImaginary()

/** Multiplies this number by [multiplicand]. */
public operator fun Int.times(multiplicand: BImag): BImag = multiplicand * this

/** Multiplies this number by [multiplicand]. */
public operator fun BImag.times(multiplicand: Int): BImag =
    (value * multiplicand).toImaginary()

// Division operator

/**
 * Returns the multiplicative inverse.
 * - [inv] is a sensible alternative name, however has a bitwise meaning
 *   for primitives
 */
public fun BImag.unaryDiv(): BImag = -value.unaryDiv().i

/** Divides this number by [divisor]. */
public operator fun BImag.div(divisor: BImag): BRat = this * divisor.unaryDiv()

/** Divides this number by [divisor]. */
public operator fun BRat.div(divisor: BImag): BImag = this * divisor.unaryDiv()

/** Divides this number by [divisor]. */
public operator fun BImag.div(divisor: BRat): BImag = this * divisor.unaryDiv()

/** Divides this number by [divisor]. */
public operator fun BFixed.div(divisor: BImag): BImag =
    this * divisor.unaryDiv()

/** Divides this number by [divisor]. */
public operator fun BImag.div(divisor: BFixed): BImag =
    (value / divisor).toImaginary()

/** Divides this number by [divisor]. */
public operator fun Long.div(divisor: BImag): BImag = this * divisor.unaryDiv()

/** Divides this number by [divisor]. */
public operator fun BImag.div(divisor: Long): BImag =
    (value / divisor).toImaginary()

/** Divides this number by [divisor]. */
public operator fun Int.div(divisor: BImag): BImag = this * divisor.unaryDiv()

/** Divides this number by [divisor]. */
public operator fun BImag.div(divisor: Int): BImag =
    (value / divisor).toImaginary()

// Other

/** Checks if this number is the additive identity. */
public fun BImag.isZero(): Boolean = value.isZero()
