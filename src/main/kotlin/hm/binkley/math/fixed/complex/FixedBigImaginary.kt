package hm.binkley.math.fixed.complex

import hm.binkley.math.BFixed
import hm.binkley.math.algebra.Group
import hm.binkley.math.algebra.GroupCompanion
import hm.binkley.math.div
import hm.binkley.math.fixed.FixedBigRational
import hm.binkley.math.fixed.toBigRational
import hm.binkley.math.isZero
import hm.binkley.math.times

public data class FixedBigImaginary(val value: FixedBigRational) :
    Group<FixedBigImaginary>,
    Comparable<FixedBigImaginary> {
    // Not covered in tests -- API ensures 0 and 1 are defined
    override val companion: Companion get() = FixedBigImaginary

    override fun unaryMinus(): FixedBigImaginary = (-value).toImaginary()

    override fun plus(addend: FixedBigImaginary): FixedBigImaginary =
        (value + addend.value).toImaginary()

    override fun compareTo(other: FixedBigImaginary): Int =
        value.compareTo(other.value)

    override fun toString(): String = "${value}i"

    public companion object : GroupCompanion<FixedBigImaginary> {
        override val ZERO: FixedBigImaginary = 0.i

        @JvmField
        public val I: FixedBigImaginary = 1.i
    }
}

// Constructors

public fun FixedBigRational.toImaginary(): FixedBigImaginary =
    FixedBigImaginary(this)

public val FixedBigRational.i: FixedBigImaginary get() = toImaginary()
public fun BFixed.toImaginary(): FixedBigImaginary =
    toBigRational().toImaginary()

public val BFixed.i: FixedBigImaginary get() = toImaginary()
public fun Long.toImaginary(): FixedBigImaginary = toBigRational().toImaginary()
public val Long.i: FixedBigImaginary get() = toImaginary()
public fun Int.toImaginary(): FixedBigImaginary = toBigRational().toImaginary()
public val Int.i: FixedBigImaginary get() = toImaginary()

// Multiplication operator

public operator fun FixedBigImaginary.times(
    multiplier: FixedBigImaginary
): FixedBigRational = -(value * multiplier.value)

public operator fun FixedBigImaginary.times(
    multiplier: FixedBigRational
): FixedBigImaginary = (value * multiplier).toImaginary()

public operator fun FixedBigRational.times(
    multiplier: FixedBigImaginary
): FixedBigImaginary = multiplier * this

public operator fun FixedBigImaginary.times(
    multiplier: BFixed
): FixedBigImaginary = (value * multiplier).toImaginary()

public operator fun BFixed.times(
    multiplier: FixedBigImaginary
): FixedBigImaginary = multiplier * this

public operator fun FixedBigImaginary.times(
    multiplier: Long
): FixedBigImaginary = (value * multiplier).toImaginary()

public operator fun Long.times(
    multiplier: FixedBigImaginary
): FixedBigImaginary = multiplier * this

public operator fun FixedBigImaginary.times(
    multiplier: Int
): FixedBigImaginary = (value * multiplier).toImaginary()

public operator fun Int.times(
    multiplier: FixedBigImaginary
): FixedBigImaginary = multiplier * this

// Division operator

public fun FixedBigImaginary.unaryDiv(): FixedBigImaginary = -value.unaryDiv().i

public operator fun FixedBigImaginary.div(divisor: FixedBigImaginary): FixedBigRational =
    this * divisor.unaryDiv()

public operator fun FixedBigImaginary.div(divisor: FixedBigRational): FixedBigImaginary =
    this * divisor.unaryDiv()

public operator fun FixedBigRational.div(divisor: FixedBigImaginary): FixedBigImaginary =
    this * divisor.unaryDiv()

public operator fun FixedBigImaginary.div(divisor: BFixed): FixedBigImaginary =
    (value / divisor).toImaginary()

public operator fun BFixed.div(divisor: FixedBigImaginary): FixedBigImaginary =
    this * divisor.unaryDiv()

public operator fun FixedBigImaginary.div(divisor: Long): FixedBigImaginary =
    (value / divisor).toImaginary()

public operator fun Long.div(divisor: FixedBigImaginary): FixedBigImaginary =
    this * divisor.unaryDiv()

public operator fun FixedBigImaginary.div(divisor: Int): FixedBigImaginary =
    (value / divisor).toImaginary()

public operator fun Int.div(divisor: FixedBigImaginary): FixedBigImaginary =
    this * divisor.unaryDiv()

// Other

public fun FixedBigImaginary.isZero(): Boolean = value.isZero()
