package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.fixed.FixedBigRational.Companion.ONE
import hm.binkley.math.times
import lombok.Generated

@Generated // Lie to JaCoCo -- inline class confuses it
inline class FixedImaginary(val value: BRat) :
    Comparable<FixedImaginary> {
    override fun compareTo(other: FixedImaginary) =
        value.compareTo(other.value)

    operator fun unaryPlus() = this
    operator fun unaryMinus() = (-value).toImaginary()

    operator fun plus(addend: FixedImaginary) =
        (value + addend.value).toImaginary()

    operator fun minus(subtrahend: FixedImaginary) =
        (value - subtrahend.value).toImaginary()

    operator fun times(multiplier: FixedImaginary) =
        -(value * multiplier.value)

    operator fun times(multiplier: BRat) =
        (value * multiplier).toImaginary()

    operator fun times(multiplier: BInt) =
        (value * multiplier).toImaginary()

    operator fun times(multiplier: Long) =
        (value * multiplier).toImaginary()

    operator fun times(multiplier: Int) =
        (value * multiplier).toImaginary()

    override fun toString() = "${value}i"

    companion object {
        val I = ONE.i
    }
}

fun BRat.toImaginary() = FixedImaginary(this)
val BRat.i get() = toImaginary()
fun BInt.toImaginary() = toBigRational().toImaginary()
val BInt.i get() = toImaginary()
fun Long.toImaginary() = toBigRational().toImaginary()
val Long.i get() = toImaginary()
fun Int.toImaginary() = toBigRational().toImaginary()
val Int.i get() = toImaginary()

operator fun BRat.times(multiplier: FixedImaginary) =
    multiplier * this

operator fun BInt.times(multiplier: FixedImaginary) = multiplier * this
operator fun Long.times(multiplier: FixedImaginary) = multiplier * this
operator fun Int.times(multiplier: FixedImaginary) = multiplier * this
