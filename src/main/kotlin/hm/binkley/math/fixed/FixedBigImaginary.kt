package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.algebra.Group
import hm.binkley.math.algebra.GroupCompanion
import hm.binkley.math.isZero
import hm.binkley.math.times

public data class FixedBigImaginary(val value: BRat) :
    Group<FixedBigImaginary>,
    Comparable<FixedBigImaginary> {
    override val companion: Companion get() = FixedBigImaginary

    override fun unaryMinus(): FixedBigImaginary = (-value).toImaginary()

    override fun plus(addend: FixedBigImaginary): FixedBigImaginary =
        (value + addend.value).toImaginary()

    override fun compareTo(other: FixedBigImaginary): Int =
        value.compareTo(other.value)

    override fun toString(): String = "${value}i"

    public companion object : GroupCompanion<FixedBigImaginary> {
        override val ZERO: FixedBigImaginary = 0.i
        public val I: FixedBigImaginary = 1.i
    }
}

// Constructors

public fun BRat.toImaginary(): FixedBigImaginary = FixedBigImaginary(this)
public val BRat.i: FixedBigImaginary get() = toImaginary()
public fun BInt.toImaginary(): FixedBigImaginary = toBigRational().toImaginary()
public val BInt.i: FixedBigImaginary get() = toImaginary()
public fun Long.toImaginary(): FixedBigImaginary = toBigRational().toImaginary()
public val Long.i: FixedBigImaginary get() = toImaginary()
public fun Int.toImaginary(): FixedBigImaginary = toBigRational().toImaginary()
public val Int.i: FixedBigImaginary get() = toImaginary()

// Multiplication operator

public operator fun FixedBigImaginary.times(multiplier: FixedBigImaginary): BRat =
    -(value * multiplier.value)

public operator fun FixedBigImaginary.times(multiplier: BRat): FixedBigImaginary =
    (value * multiplier).toImaginary()

public operator fun BRat.times(multiplier: FixedBigImaginary): FixedBigImaginary =
    multiplier * this

public operator fun FixedBigImaginary.times(multiplier: BInt): FixedBigImaginary =
    (value * multiplier).toImaginary()

public operator fun BInt.times(multiplier: FixedBigImaginary): FixedBigImaginary =
    multiplier * this

public operator fun FixedBigImaginary.times(multiplier: Long): FixedBigImaginary =
    (value * multiplier).toImaginary()

public operator fun Long.times(multiplier: FixedBigImaginary): FixedBigImaginary =
    multiplier * this

public operator fun FixedBigImaginary.times(multiplier: Int): FixedBigImaginary =
    (value * multiplier).toImaginary()

public operator fun Int.times(multiplier: FixedBigImaginary): FixedBigImaginary =
    multiplier * this

// TODO: Division operator

public fun FixedBigImaginary.isZero(): Boolean = value.isZero()
