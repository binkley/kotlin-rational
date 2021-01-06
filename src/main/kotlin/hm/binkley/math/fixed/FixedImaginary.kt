package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.fixed.FixedBigRational.Companion.ONE
import hm.binkley.math.isZero
import hm.binkley.math.times
import lombok.Generated

@Generated // Lie to JaCoCo -- inline class confuses it
inline class FixedImaginary(val value: BRat) :
    Comparable<FixedImaginary> {
    override fun compareTo(other: FixedImaginary): Int =
        value.compareTo(other.value)

    override fun toString(): String = "${value}i"

    companion object {
        val I: FixedImaginary = ONE.i
    }
}

fun BRat.toImaginary(): FixedImaginary = FixedImaginary(this)
val BRat.i: FixedImaginary get() = toImaginary()
fun BInt.toImaginary(): FixedImaginary = toBigRational().toImaginary()
val BInt.i: FixedImaginary get() = toImaginary()
fun Long.toImaginary(): FixedImaginary = toBigRational().toImaginary()
val Long.i: FixedImaginary get() = toImaginary()
fun Int.toImaginary(): FixedImaginary = toBigRational().toImaginary()
val Int.i: FixedImaginary get() = toImaginary()

operator fun FixedImaginary.unaryPlus(): FixedImaginary = this
operator fun FixedImaginary.unaryMinus(): FixedImaginary =
    (-value).toImaginary()

operator fun FixedImaginary.plus(addend: FixedImaginary): FixedImaginary =
    (value + addend.value).toImaginary()

operator fun FixedImaginary.minus(subtrahend: FixedImaginary): FixedImaginary =
    (value - subtrahend.value).toImaginary()

operator fun FixedImaginary.times(multiplier: FixedImaginary): FixedBigRational =
    -(value * multiplier.value)

operator fun FixedImaginary.times(multiplier: BRat): FixedImaginary =
    (value * multiplier).toImaginary()

operator fun FixedImaginary.times(multiplier: BInt): FixedImaginary =
    (value * multiplier).toImaginary()

operator fun FixedImaginary.times(multiplier: Long): FixedImaginary =
    (value * multiplier).toImaginary()

operator fun FixedImaginary.times(multiplier: Int): FixedImaginary =
    (value * multiplier).toImaginary()

operator fun BRat.times(multiplier: FixedImaginary): FixedImaginary =
    multiplier * this

operator fun BInt.times(multiplier: FixedImaginary): FixedImaginary =
    multiplier * this

operator fun Long.times(multiplier: FixedImaginary): FixedImaginary =
    multiplier * this

operator fun Int.times(multiplier: FixedImaginary): FixedImaginary =
    multiplier * this

fun FixedImaginary.isZero(): Boolean = value.isZero()
