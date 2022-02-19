package hm.binkley.math.floating.complex

import hm.binkley.math.BFixed
import hm.binkley.math.BFloating
import hm.binkley.math.algebra.Group
import hm.binkley.math.algebra.GroupCompanion
import hm.binkley.math.div
import hm.binkley.math.floating.FloatingBigRational
import hm.binkley.math.floating.toBigRational
import hm.binkley.math.isZero
import hm.binkley.math.times

public data class FloatingBigImaginary(val value: FloatingBigRational) :
    Group<FloatingBigImaginary>,
    Comparable<FloatingBigImaginary> {
    // Not covered in tests -- API ensures 0 and 1 are defined
    override val companion: Companion get() = FloatingBigImaginary

    override fun unaryMinus(): FloatingBigImaginary = (-value).toImaginary()

    override fun plus(addend: FloatingBigImaginary): FloatingBigImaginary =
        (value + addend.value).toImaginary()

    override fun compareTo(other: FloatingBigImaginary): Int =
        value.compareTo(other.value)

    override fun toString(): String = "${value}i"

    public companion object : GroupCompanion<FloatingBigImaginary> {
        override val ZERO: FloatingBigImaginary = 0.0.i

        @JvmField
        public val I: FloatingBigImaginary = 1.0.i
    }
}

// Constructors

public fun FloatingBigRational.toImaginary(): FloatingBigImaginary =
    FloatingBigImaginary(this)

public val FloatingBigRational.i: FloatingBigImaginary get() = toImaginary()
public fun BFloating.toImaginary(): FloatingBigImaginary =
    toBigRational().toImaginary()

public val BFloating.i: FloatingBigImaginary get() = toBigRational().toImaginary()
public fun Double.toImaginary(): FloatingBigImaginary =
    toBigRational().toImaginary()

public val Double.i: FloatingBigImaginary get() = toImaginary()
public fun Float.toImaginary(): FloatingBigImaginary =
    toBigRational().toImaginary()

public val Float.i: FloatingBigImaginary get() = toImaginary()
public fun BFixed.toImaginary(): FloatingBigImaginary =
    toBigRational().toImaginary()

public val BFixed.i: FloatingBigImaginary get() = toImaginary()
public fun Long.toImaginary(): FloatingBigImaginary =
    toBigRational().toImaginary()

public val Long.i: FloatingBigImaginary get() = toImaginary()
public fun Int.toImaginary(): FloatingBigImaginary =
    toBigRational().toImaginary()

public val Int.i: FloatingBigImaginary get() = toImaginary()

// Multiplication operator

public operator fun FloatingBigImaginary.times(
    multiplier: FloatingBigImaginary
): FloatingBigRational = -(value * multiplier.value)

public operator fun FloatingBigImaginary.times(
    multiplier: FloatingBigRational
): FloatingBigImaginary = (value * multiplier).toImaginary()

public operator fun FloatingBigRational.times(
    multiplier: FloatingBigImaginary
): FloatingBigImaginary = multiplier * this

public operator fun FloatingBigImaginary.times(
    multiplier: BFixed
): FloatingBigImaginary = (value * multiplier).toImaginary()

public operator fun BFixed.times(
    multiplier: FloatingBigImaginary
): FloatingBigImaginary = multiplier * this

public operator fun FloatingBigImaginary.times(
    multiplier: Long
): FloatingBigImaginary = (value * multiplier).toImaginary()

public operator fun Long.times(
    multiplier: FloatingBigImaginary
): FloatingBigImaginary = multiplier * this

public operator fun FloatingBigImaginary.times(
    multiplier: Int
): FloatingBigImaginary = (value * multiplier).toImaginary()

public operator fun Int.times(
    multiplier: FloatingBigImaginary
): FloatingBigImaginary = multiplier * this

// Division operator

public fun FloatingBigImaginary.unaryDiv(): FloatingBigImaginary =
    -value.unaryDiv().i

public operator fun FloatingBigImaginary.div(divisor: FloatingBigImaginary): FloatingBigRational =
    this * divisor.unaryDiv()

public operator fun FloatingBigImaginary.div(divisor: FloatingBigRational): FloatingBigImaginary =
    this * divisor.unaryDiv()

public operator fun FloatingBigRational.div(divisor: FloatingBigImaginary): FloatingBigImaginary =
    this * divisor.unaryDiv()

public operator fun FloatingBigImaginary.div(divisor: BFixed): FloatingBigImaginary =
    (value / divisor).toImaginary()

public operator fun BFixed.div(divisor: FloatingBigImaginary): FloatingBigImaginary =
    this * divisor.unaryDiv()

public operator fun FloatingBigImaginary.div(divisor: Long): FloatingBigImaginary =
    (value / divisor).toImaginary()

public operator fun Long.div(divisor: FloatingBigImaginary): FloatingBigImaginary =
    this * divisor.unaryDiv()

public operator fun FloatingBigImaginary.div(divisor: Int): FloatingBigImaginary =
    (value / divisor).toImaginary()

public operator fun Int.div(divisor: FloatingBigImaginary): FloatingBigImaginary =
    this * divisor.unaryDiv()

// Other

public fun FloatingBigImaginary.isZero(): Boolean = value.isZero()
