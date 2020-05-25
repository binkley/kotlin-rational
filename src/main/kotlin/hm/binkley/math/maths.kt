package hm.binkley.math

import java.math.BigDecimal
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.TEN
import java.math.BigInteger.TWO
import java.math.BigInteger.ZERO

internal typealias BInt = BigInteger
internal typealias BDouble = BigDecimal

internal fun BInt.isZero() = this == ZERO
internal fun BInt.isOne() = this == ONE
internal fun BInt.isTwo() = this == TWO
internal fun BInt.isTen() = this == TEN

internal fun BInt.isDyadic() = (isOne() || (this % TWO).isZero())

internal fun BInt.isPAdic(p: Long) =
    (isOne() || (this % BInt.valueOf(p)).isZero())

/**
 * See https://en.wikipedia.org/wiki/Double-precision_floating-point_format
 */
internal fun exponent(d: Double) =
    ((d.toBits() shr 52).toInt() and 0x7ff) - 1023

/**
 * See https://en.wikipedia.org/wiki/Double-precision_floating-point_format
 */
internal fun mantissa(d: Double) = d.toBits() and 0xfffffffffffffL

internal fun BInt.lcm(other: BInt) = (this * (other / gcd(other))).abs()

internal fun BInt.isEven() = ZERO == this % TWO
