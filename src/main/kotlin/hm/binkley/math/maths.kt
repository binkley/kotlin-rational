package hm.binkley.math

import java.math.BigDecimal
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.TEN
import java.math.BigInteger.TWO
import java.math.BigInteger.ZERO

internal typealias BInt = BigInteger
internal typealias BDouble = BigDecimal

internal fun BInt.isZero() = ZERO == this
internal fun BInt.isOne() = ONE == this
internal fun BInt.isTwo() = TWO == this
internal fun BInt.isTen() = TEN == this

internal fun BInt.isDyadic() = (isOne() || (this % TWO).isZero())

internal fun BInt.isPAdic(p: Long) =
    (isOne() || (this % BInt.valueOf(p)).isZero())

internal fun BInt.lcm(other: BInt) = (this * (other / gcd(other))).abs()

internal fun BInt.isEven() = (this % TWO).isZero()
