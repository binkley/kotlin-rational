package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.fixed.FixedBigRational.Companion.ONE
import hm.binkley.math.times
import lombok.Generated

@Generated // Lie to JaCoCo -- inline class confuses it
inline class FixedImaginary(val value: FixedBigRational) :
    Comparable<FixedImaginary> {
    override fun compareTo(other: FixedImaginary) =
        value.compareTo(other.value)

    operator fun unaryPlus() = this
    operator fun unaryMinus() = (-value).toImaginary()

    operator fun plus(addend: FixedImaginary) =
        (value + addend.value).toImaginary()

    operator fun minus(subtrahend: FixedImaginary) =
        (value - subtrahend.value).toImaginary()

    operator fun times(multiplicand: FixedImaginary) =
        -(value * multiplicand.value)

    operator fun times(multiplicand: FixedBigRational) =
        (value * multiplicand).toImaginary()

    operator fun times(multiplicand: BInt) =
        (value * multiplicand).toImaginary()

    operator fun times(multiplicand: Long) =
        (value * multiplicand).toImaginary()

    operator fun times(multiplicand: Int) =
        (value * multiplicand).toImaginary()

    override fun toString() = "${value}i"

    companion object {
        val I = ONE.i
    }
}

fun FixedBigRational.toImaginary() = FixedImaginary(this)
val FixedBigRational.i get() = toImaginary()
fun BInt.toImaginary() = toBigRational().toImaginary()
val BInt.i get() = toImaginary()
fun Long.toImaginary() = toBigRational().toImaginary()
val Long.i get() = toImaginary()
fun Int.toImaginary() = toBigRational().toImaginary()
val Int.i get() = toImaginary()

operator fun FixedBigRational.times(multiplicand: FixedImaginary) =
    multiplicand * this

operator fun BInt.times(multiplicand: FixedImaginary) = multiplicand * this
operator fun Long.times(multiplicand: FixedImaginary) = multiplicand * this
operator fun Int.times(multiplicand: FixedImaginary) = multiplicand * this
