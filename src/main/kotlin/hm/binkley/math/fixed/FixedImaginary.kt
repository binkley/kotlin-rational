package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.algebra.Group
import hm.binkley.math.algebra.GroupCompanion
import hm.binkley.math.isZero
import hm.binkley.math.times

data class FixedImaginary(val value: BRat) :
    Group<FixedImaginary>,
    Comparable<FixedImaginary> {
    override val companion get() = FixedImaginary

    override fun unaryMinus(): FixedImaginary = (-value).toImaginary()

    override fun plus(addend: FixedImaginary): FixedImaginary =
        (value + addend.value).toImaginary()

    override fun compareTo(other: FixedImaginary): Int =
        value.compareTo(other.value)

    override fun toString(): String = "${value}i"

    companion object : GroupCompanion<FixedImaginary> {
        override val ZERO: FixedImaginary = 0.i
        val I: FixedImaginary = 1.i
    }
}

// Constructors

fun BRat.toImaginary(): FixedImaginary = FixedImaginary(this)
val BRat.i: FixedImaginary get() = toImaginary()
fun BInt.toImaginary(): FixedImaginary = toBigRational().toImaginary()
val BInt.i: FixedImaginary get() = toImaginary()
fun Long.toImaginary(): FixedImaginary = toBigRational().toImaginary()
val Long.i: FixedImaginary get() = toImaginary()
fun Int.toImaginary(): FixedImaginary = toBigRational().toImaginary()
val Int.i: FixedImaginary get() = toImaginary()

// Multiplication operator

operator fun FixedImaginary.times(multiplier: FixedImaginary): BRat =
    -(value * multiplier.value)

operator fun FixedImaginary.times(multiplier: BRat): FixedImaginary =
    (value * multiplier).toImaginary()

operator fun BRat.times(multiplier: FixedImaginary): FixedImaginary =
    multiplier * this

operator fun FixedImaginary.times(multiplier: BInt): FixedImaginary =
    (value * multiplier).toImaginary()

operator fun BInt.times(multiplier: FixedImaginary): FixedImaginary =
    multiplier * this

operator fun FixedImaginary.times(multiplier: Long): FixedImaginary =
    (value * multiplier).toImaginary()

operator fun Long.times(multiplier: FixedImaginary): FixedImaginary =
    multiplier * this

operator fun FixedImaginary.times(multiplier: Int): FixedImaginary =
    (value * multiplier).toImaginary()

operator fun Int.times(multiplier: FixedImaginary): FixedImaginary =
    multiplier * this

// TODO: Division operator

fun FixedImaginary.isZero(): Boolean = value.isZero()
