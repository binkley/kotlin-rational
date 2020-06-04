package hm.binkley.math.finite

import hm.binkley.math.BInt
import hm.binkley.math.finite.FixedBigRational.Companion.ONE
import hm.binkley.math.minus
import hm.binkley.math.plus
import hm.binkley.math.times
import hm.binkley.math.unaryMinus

val I = ONE.i

inline class FixedImaginary(val value: FixedBigRational) :
    Comparable<FixedImaginary> {
    override fun compareTo(other: FixedImaginary) =
        value.compareTo(other.value)

    override fun toString() = "${value}i"
}

fun FixedBigRational.toImaginary() = FixedImaginary(this)
val FixedBigRational.i get() = toImaginary()
fun BInt.toImaginary() = toBigRational().toImaginary()
val BInt.i get() = toImaginary()
fun Long.toImaginary() = toBigRational().toImaginary()
val Long.i get() = toImaginary()
fun Int.toImaginary() = toBigRational().toImaginary()
val Int.i get() = toImaginary()

operator fun FixedImaginary.unaryPlus() = this
operator fun FixedImaginary.unaryMinus() = (-value).toImaginary()

operator fun FixedImaginary.plus(addend: FixedImaginary) =
    (value + addend.value).toImaginary()

operator fun FixedImaginary.minus(subtrahend: FixedImaginary) =
    (value - subtrahend.value).toImaginary()

operator fun FixedImaginary.times(multiplicand: FixedImaginary) =
    -(value * multiplicand.value)

operator fun FixedImaginary.times(multiplicand: FixedBigRational) =
    (this.value * multiplicand).toImaginary()

operator fun FixedImaginary.times(multiplicand: BInt) =
    (this.value * multiplicand).toImaginary()

operator fun FixedImaginary.times(multiplicand: Long) =
    (this.value * multiplicand).toImaginary()

operator fun FixedImaginary.times(multiplicand: Int) =
    (this.value * multiplicand).toImaginary()

operator fun FixedBigRational.times(multiplicand: FixedImaginary) =
    multiplicand * this

operator fun BInt.times(multiplicand: FixedImaginary) = multiplicand * this
operator fun Long.times(multiplicand: FixedImaginary) = multiplicand * this
operator fun Int.times(multiplicand: FixedImaginary) = multiplicand * this
