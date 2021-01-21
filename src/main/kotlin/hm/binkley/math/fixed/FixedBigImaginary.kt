package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.algebra.Group
import hm.binkley.math.algebra.GroupCompanion
import hm.binkley.math.isZero
import hm.binkley.math.times

data class FixedBigImaginary(val value: BRat) :
    Group<FixedBigImaginary>,
    Comparable<FixedBigImaginary> {
    override val companion: Companion get() = FixedBigImaginary

    override fun unaryMinus(): FixedBigImaginary = (-value).toImaginary()

    override fun plus(addend: FixedBigImaginary): FixedBigImaginary =
        (value + addend.value).toImaginary()

    override fun compareTo(other: FixedBigImaginary): Int =
        value.compareTo(other.value)

    override fun toString(): String = "${value}i"

    companion object : GroupCompanion<FixedBigImaginary> {
        override val ZERO: FixedBigImaginary = 0.i
        val I: FixedBigImaginary = 1.i
    }
}

// Constructors

fun BRat.toImaginary(): FixedBigImaginary = FixedBigImaginary(this)
val BRat.i: FixedBigImaginary get() = toImaginary()
fun BInt.toImaginary(): FixedBigImaginary = toBigRational().toImaginary()
val BInt.i: FixedBigImaginary get() = toImaginary()
fun Long.toImaginary(): FixedBigImaginary = toBigRational().toImaginary()
val Long.i: FixedBigImaginary get() = toImaginary()
fun Int.toImaginary(): FixedBigImaginary = toBigRational().toImaginary()
val Int.i: FixedBigImaginary get() = toImaginary()

// Multiplication operator

operator fun FixedBigImaginary.times(multiplier: FixedBigImaginary): BRat =
    -(value * multiplier.value)

operator fun FixedBigImaginary.times(multiplier: BRat): FixedBigImaginary =
    (value * multiplier).toImaginary()

operator fun BRat.times(multiplier: FixedBigImaginary): FixedBigImaginary =
    multiplier * this

operator fun FixedBigImaginary.times(multiplier: BInt): FixedBigImaginary =
    (value * multiplier).toImaginary()

operator fun BInt.times(multiplier: FixedBigImaginary): FixedBigImaginary =
    multiplier * this

operator fun FixedBigImaginary.times(multiplier: Long): FixedBigImaginary =
    (value * multiplier).toImaginary()

operator fun Long.times(multiplier: FixedBigImaginary): FixedBigImaginary =
    multiplier * this

operator fun FixedBigImaginary.times(multiplier: Int): FixedBigImaginary =
    (value * multiplier).toImaginary()

operator fun Int.times(multiplier: FixedBigImaginary): FixedBigImaginary =
    multiplier * this

// TODO: Division operator

fun FixedBigImaginary.isZero(): Boolean = value.isZero()
