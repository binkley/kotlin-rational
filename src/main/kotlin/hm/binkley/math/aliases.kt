@file:Suppress("ktlint:standard:filename")

package hm.binkley.math

import java.math.BigDecimal
import java.math.BigInteger

internal typealias BFixed = BigInteger
internal typealias BFloating = BigDecimal
internal typealias BRatBase<T> = BigRationalBase<T>
internal typealias BRatCompanion<T> = BigRationalCompanion<T>
internal typealias CFracBase<T, C> = ContinuedFractionBase<T, C>
