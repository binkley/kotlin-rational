package hm.binkley.math.floating.complex

import hm.binkley.math.BFixed
import hm.binkley.math.BFloating
import hm.binkley.math.algebra.Group
import hm.binkley.math.algebra.GroupCompanion
import hm.binkley.math.div
import hm.binkley.math.floating.toBigRational
import hm.binkley.math.isZero
import hm.binkley.math.times

public data class FloatingBigImaginary(val value: BRat) :
    Group<FloatingBigImaginary>, Comparable<FloatingBigImaginary> {
    override val companion: Companion get() = Companion

    override fun unaryMinus(): BImag = (-value).toImaginary()

    override fun plus(addend: BImag): BImag =
        (value + addend.value).toImaginary()

    override fun compareTo(other: BImag): Int = value.compareTo(other.value)

    override fun toString(): String = "${value}i"

    public companion object : GroupCompanion<BImag> {
        override val ZERO: BImag = 0.0.i

        @JvmField
        public val I: BImag = 1.0.i
    }
}

// Factories

public fun BRat.toImaginary(): BImag = BImag(this)

public val BRat.i: BImag get() = toImaginary()
public fun BFloating.toImaginary(): BImag = toBigRational().toImaginary()

public val BFloating.i: BImag get() = toBigRational().toImaginary()
public fun Double.toImaginary(): BImag = toBigRational().toImaginary()

public val Double.i: BImag get() = toImaginary()
public fun Float.toImaginary(): BImag = toBigRational().toImaginary()

public val Float.i: BImag get() = toImaginary()
public fun BFixed.toImaginary(): BImag = toBigRational().toImaginary()

public val BFixed.i: BImag get() = toImaginary()
public fun Long.toImaginary(): BImag = toBigRational().toImaginary()

public val Long.i: BImag get() = toImaginary()
public fun Int.toImaginary(): BImag = toBigRational().toImaginary()

public val Int.i: BImag get() = toImaginary()

// Multiplication operator

public operator fun BImag.times(multiplier: BImag): BRat =
    -(value * multiplier.value)

public operator fun BRat.times(multiplier: BImag): BImag = multiplier * this

public operator fun BImag.times(multiplier: BRat): BImag =
    (value * multiplier).toImaginary()

public operator fun BFixed.times(multiplier: BImag): BImag = multiplier * this

public operator fun BImag.times(multiplier: BFixed): BImag =
    (value * multiplier).toImaginary()

public operator fun Long.times(multiplier: BImag): BImag = multiplier * this

public operator fun BImag.times(multiplier: Long): BImag =
    (value * multiplier).toImaginary()

public operator fun Int.times(multiplier: BImag): BImag = multiplier * this

public operator fun BImag.times(multiplier: Int): BImag =
    (value * multiplier).toImaginary()

// Division operator

public fun BImag.unaryDiv(): BImag = -value.unaryDiv().i

public operator fun BImag.div(divisor: BImag): BRat = this * divisor.unaryDiv()

public operator fun BRat.div(divisor: BImag): BImag = this * divisor.unaryDiv()

public operator fun BImag.div(divisor: BRat): BImag = this * divisor.unaryDiv()

public operator fun BFixed.div(divisor: BImag): BImag =
    this * divisor.unaryDiv()

public operator fun BImag.div(divisor: BFixed): BImag =
    (value / divisor).toImaginary()

public operator fun Long.div(divisor: BImag): BImag = this * divisor.unaryDiv()

public operator fun BImag.div(divisor: Long): BImag =
    (value / divisor).toImaginary()

public operator fun Int.div(divisor: BImag): BImag = this * divisor.unaryDiv()

public operator fun BImag.div(divisor: Int): BImag =
    (value / divisor).toImaginary()

// Other

public fun BImag.isZero(): Boolean = value.isZero()
