package hm.binkley.math

import java.math.BigDecimal
import java.math.BigInteger

/** Note that [BigDecimal.valueOf] does not check for all constants. */
internal val Double.big: BFloating
    get() = when (this) {
        0.0 -> BFloating.ZERO // Unlike BInt, BDouble.valueOf does not handle
        1.0 -> BFloating.ONE
        // BigDecimal does not have a `TWO`
        10.0 -> BFloating.TEN
        else -> BFloating.valueOf(this)
    }

/**
 * Reuses the JDK's `Float.isFinite(float)`.
 *
 * @todo Smell around complexity of double/float conversion.
 *       Trace usages, and compare with other `valueOf` methods, and also has
 *       an uncovered branch
 */
internal fun Float.isFinite() = java.lang.Float.isFinite(this)

internal fun BFixed.isZero() = BFixed.ZERO == this
internal fun BFixed.isUnit() = BFixed.ONE == this
internal fun BFixed.isTwo() = BFixed.TWO == this
internal fun BFixed.isTen() = BFixed.TEN == this
internal fun BFixed.isPAdic(p: Long) = (isUnit() || (this % p.big).isZero())
internal fun BFixed.isEven() = (this % 2.big).isZero()
internal fun BFixed.lcm(other: BFixed) = (this * (other / gcd(other))).abs()

/** Note that [BigInteger.valueOf] checks for cached constants. */
internal inline val Long.big: BFixed get() = BFixed.valueOf(this)
internal inline val Int.big: BFixed get() = toLong().big

internal fun Int.isZero() = this == 0
