package hm.binkley.math

import java.lang.Math.cbrt
import java.math.RoundingMode.CEILING
import java.math.RoundingMode.DOWN
import java.math.RoundingMode.FLOOR
import java.math.RoundingMode.HALF_EVEN
import kotlin.math.sqrt

/**
 * Returns an _exact_ square root (non-complex).  The caller should take
 * [BigRationalBase.sign] into consideration
 *
 * @throws ArithmeticException if there is no exact square root
 */
public fun <T : BigRationalBase<T>> T.sqrt(): T {
    val p = numerator.sqrt()
    if (numerator != p * p)
        throw ArithmeticException("No rational square root: $this")
    val q = denominator.sqrt()
    if (denominator != q * q)
        throw ArithmeticException("No rational square root: $this")
    return companion.valueOf(p, q)
}

/**
 * Returns the nearest _positive_ (non-complex) rational square root _based
 * on IEEE 754_ double-precision floating-point values.  The caller should take
 * [BigRationalBase.sign] into consideration.
 *
 * Note: Approximations are limited to the precision of
 * [IEEE 754 binary64](https://en.wikipedia.org/wiki/Double-precision_floating-point_format).
 * It is not clear given handling of extrema by IEEE what the best approach is.
 *
 * @todo The algorithm converts to a `Double` for the numerator and denominator
 *       seemingly unnecessarily rather than directly using `toDouble()`.  This
 *       avoids an `ArithmeticException`
 */
public fun <T : BigRationalBase<T>> T.sqrtApproximated(): T = try {
    sqrt()
} catch (_: ArithmeticException) {
    companion.valueOf(
        sqrt(numerator.toDouble() / denominator.toDouble())
    )
}

/**
 * Returns the nearest _positive_ (non-complex) rational cube root _based
 * on IEEE 754_ double-precision floating-point values.  The caller should take
 * [BigRationalBase.sign] into consideration.
 *
 * Note: Approximations are limited to the precision of
 * [IEEE 754 binary64](https://en.wikipedia.org/wiki/Double-precision_floating-point_format).
 * It is not clear given handling of extrema by IEEE what the best approach is.
 *
 * @todo Use `try`/`catch` form as [sqrtApproximated] does
 * @todo The algorithm converts to a `Double` for the numerator and denominator
 *       seemingly unnecessarily rather than directly using `toDouble()`.  This
 *       avoids an `ArithmeticException`
 */
public fun <T : BigRationalBase<T>> T.cbrtApproximated(): T =
    companion.valueOf(
        cbrt(numerator.toDouble() / denominator.toDouble())
    )

/**
 * Rounds to the nearest whole number towards positive infinity corresponding
 * to [Math.ceil] and rounding mode [CEILING].
 */
public fun <T : BigRationalBase<T>> T.ceil(): T = round(CEILING)

/**
 * Rounds to the nearest whole number towards negative infinity corresponding
 * to [Math.floor] and rounding mode [FLOOR].
 */
public fun <T : BigRationalBase<T>> T.floor(): T = round(FLOOR)

/**
 * Truncates to the nearest whole number towards 0 corresponding to
 * rounding mode [DOWN], returning the truncation and remaining fraction.
 *
 * Summing the truncation and fraction should produce the original number
 * except for floating big rationals of `NaN` or positive or negative
 * infinities.
 */
public fun <T : BigRationalBase<T>> T.truncateAndFraction(): Pair<T, T> {
    val truncation = round(DOWN)
    val fraction = this - truncation

    return truncation to fraction
}

/**
 * Truncates to the nearest whole number _closer to 0_ than this BigRational,
 * or when this is a whole number, returns this.
 *
 * @see truncateAndFraction
 */
public fun <T : BigRationalBase<T>> T.truncate(): T {
    val (numerator, _) = truncateAndFraction()
    return numerator
}

/** Rounds to the nearest _even_ whole number corresponding to [HALF_EVEN]. */
public fun <T : BigRationalBase<T>> T.round(): T = round(HALF_EVEN)

/**
 * Provides the signed fractional remainder after [truncation][truncate].
 *
 * For `FloatingBigRational`, `NaN`, and positive and negative infinities
 * produce `NaN`.
 *
 * @see truncateAndFraction
 */
public fun <T : BigRationalBase<T>> T.fraction(): T =
    truncateAndFraction().second

/**
 * Returns the greatest common divisor of the absolute values of `this` and
 * [that].  Returns 0 when `this` and [that] are both 0.
 *
 * See an example
 * [tail-recursive version](https://github.com/breandan/kotlingrad/blob/56a6d4d03544db1bcaa93c31ffc7e075bc564e64/core/src/main/kotlin/edu/umontreal/kotlingrad/typelevel/TypeClassing.kt#L192)
 * Note: this code has recursive calls between [gcd] and [lcm], and Kotlin
 * `tailrec` does not support mutual recursion.
 */
public fun <T : BigRationalBase<T>> T.gcd(that: T): T =
    if (isZero()) that else companion.valueOf(
        numerator.gcd(that.numerator),
        denominator.lcm(that.denominator)
    )

/**
 * Returns the lowest common multiple of the absolute values of `this` and
 * [that].  Returns 0 when `this` and [that] are both 0.
 */
public fun <T : BigRationalBase<T>> T.lcm(that: T): T =
    if (isZero()) companion.ZERO else companion.valueOf(
        numerator.lcm(that.numerator),
        denominator.gcd(that.denominator)
    )
