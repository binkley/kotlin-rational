package hm.binkley.math

import hm.binkley.math.fixed.FixedBigImaginary
import java.math.BigDecimal
import java.math.BigInteger

internal typealias BImag = FixedBigImaginary
internal typealias BInt = BigInteger
internal typealias BDouble = BigDecimal

internal fun BInt.isZero() = 0.big == this
internal fun BInt.isUnit() = 1.big == this
internal fun BInt.isTwo() = 2.big == this
internal fun BInt.isTen() = 10.big == this
internal fun BInt.isDyadic() = (isUnit() || (this % 2.big).isZero())
internal fun BInt.isPAdic(p: Long) = (isUnit() || (this % p.big).isZero())
internal fun BInt.lcm(other: BInt) = (this * (other / gcd(other))).abs()
internal fun BInt.isEven() = (this % 2.big).isZero()

internal val String.big: BDouble get() = BDouble(this)

/** A sad property.  [BigDecimal.valueOf] does not check for all constants. */
internal val Double.big: BDouble
    get() = when (this) {
        0.0 -> BDouble.ZERO // Unlike BInt, BDouble.valueOf does not handle
        1.0 -> BDouble.ONE
        // BigDecimal does not have a `TWO`
        10.0 -> BDouble.TEN
        else -> BDouble.valueOf(this)
    }

/** A sad property.  [BigInteger.valueOf] does not check for all constants. */
internal val Long.big: BInt
    get() = when (this) {
        // ZERO handled internally by valueOf, unlike the others
        1L -> BInt.ONE
        2L -> BInt.TWO
        10L -> BInt.TEN
        else -> BInt.valueOf(this)
    }

internal val Int.big: BInt get() = toLong().big
