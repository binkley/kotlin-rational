package hm.binkley.math

import java.math.BigDecimal
import java.math.BigInteger

internal typealias BInt = BigInteger
internal typealias BDouble = BigDecimal

internal fun BInt.isZero() = 0.big == this
internal fun BInt.isOne() = 1.big == this
internal fun BInt.isTwo() = 2.big == this
internal fun BInt.isTen() = 10.big == this
internal fun BInt.isDyadic() = (isOne() || (this % 2.big).isZero())

internal fun BInt.isPAdic(p: Long) =
    (isOne() || (this % p.big).isZero())

internal fun BInt.lcm(other: BInt) = (this * (other / gcd(other))).abs()
internal fun BInt.isEven() = (this % 2.big).isZero()

internal val Int.big: BInt get() = toLong().big

/** A sad property.  [BigInteger.valueOf] does not check for constants. */
internal val Long.big: BInt
    get() = when (this) {
        // ZERO handled internally by valueOf, unlike the others
        1L -> BInt.ONE
        2L -> BInt.TWO
        10L -> BInt.TEN
        else -> BInt.valueOf(this)
    }

/** A sad property.  [BigDecimal.valueOf] does not check for constants. */
internal val Double.big: BDouble
    get() = when (this) {
        0.0 -> BDouble.ZERO // Unlike BInt, BDouble.valueOf does not handle
        1.0 -> BDouble.ONE
        10.0 -> BDouble.TEN
        else -> BDouble.valueOf(this)
    }

internal val String.big: BDouble get() = BDouble(this)
