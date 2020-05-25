package hm.binkley.math

import java.math.BigDecimal
import java.math.BigInteger

internal typealias BInt = BigInteger
internal typealias BDouble = BigDecimal

internal fun BInt.isZero() = this == BInt.ZERO
internal fun BInt.isOne() = this == BInt.ONE
internal fun BInt.isTwo() = this == BInt.TWO
internal fun BInt.isTen() = this == BInt.TEN

internal fun BInt.isDyadic() =
    (isOne() || (this % BInt.TWO).isZero())

internal fun BInt.isPAdic(p: Long) =
    (isOne() || (this % BInt.valueOf(p)).isZero())

internal fun exponent(d: Double) =
    ((d.toBits() shr 52).toInt() and 0x7ff) - 1023

internal fun mantissa(d: Double) = d.toBits() and 0xfffffffffffffL

internal fun BInt.lcm(other: BInt) = (this * (other / gcd(other))).abs()

internal fun BInt.isEven() = BInt.ZERO == this % BInt.TWO
