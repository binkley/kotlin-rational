package hm.binkley.math

import java.math.BigDecimal
import java.math.BigInteger

internal typealias BDouble = BigDecimal
internal typealias BInt = BigInteger

internal fun BInt.isZero() = 0.big == this
internal fun BInt.isUnit() = 1.big == this
internal fun BInt.isTwo() = 2.big == this
internal fun BInt.isTen() = 10.big == this
internal fun BInt.isPAdic(p: Long) = (isUnit() || (this % p.big).isZero())
internal fun BInt.isDyadic() = isPAdic(2)
internal fun BInt.lcm(other: BInt) = (this * (other / gcd(other))).abs()
internal fun BInt.isEven() = (this % 2.big).isZero()

internal val String.big: BDouble get() = BDouble(this)

/** Note that [BigDecimal.valueOf] does not check for all constants. */
internal val Double.big: BDouble
    get() = when (this) {
        0.0 -> BDouble.ZERO // Unlike BInt, BDouble.valueOf does not handle
        1.0 -> BDouble.ONE
        // BigDecimal does not have a `TWO`
        10.0 -> BDouble.TEN
        else -> BDouble.valueOf(this)
    }

/** Note that [BigInteger.valueOf] checks for cached constants. */
internal val Long.big: BInt get() = BInt.valueOf(this)
internal val Int.big: BInt get() = toLong().big

internal fun Float.isFinite() = !isInfinite() && !isNaN()
