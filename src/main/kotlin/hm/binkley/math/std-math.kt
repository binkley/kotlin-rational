package hm.binkley.math

import java.math.BigDecimal
import java.math.BigInteger

internal typealias BFixed = BigInteger
internal typealias BFloating = BigDecimal

/** Note that [BigDecimal.valueOf] does not check for all constants. */
internal val Double.big: BFloating
    get() = when (this) {
        0.0 -> BFloating.ZERO // Unlike BInt, BDouble.valueOf does not handle
        1.0 -> BFloating.ONE
        // BigDecimal does not have a `TWO`
        10.0 -> BFloating.TEN
        else -> BFloating.valueOf(this)
    }

internal fun Float.isFinite() = !isInfinite() && !isNaN()

internal fun BFixed.isZero() = BFixed.ZERO == this
internal fun BFixed.isUnit() = BFixed.ONE == this
internal fun BFixed.isTwo() = BFixed.TWO == this
internal fun BFixed.isTen() = BFixed.TEN == this
internal fun BFixed.isPAdic(p: Long) = (isUnit() || (this % p.big).isZero())
internal fun BFixed.isDyadic() = isPAdic(2)
internal fun BFixed.lcm(other: BFixed) = (this * (other / gcd(other))).abs()
internal fun BFixed.isEven() = (this % 2.big).isZero()

/** Note that [BigInteger.valueOf] checks for cached constants. */
internal val Long.big: BFixed get() = BFixed.valueOf(this)
internal val Int.big: BFixed get() = toLong().big

internal fun Int.isZero() = this == 0
