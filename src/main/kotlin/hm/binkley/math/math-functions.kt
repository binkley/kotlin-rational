package hm.binkley.math

import java.lang.Math.cbrt
import java.math.RoundingMode.CEILING
import java.math.RoundingMode.DOWN
import java.math.RoundingMode.FLOOR
import java.math.RoundingMode.HALF_EVEN
import kotlin.math.sqrt

/**
 * Returns an _exact_ square root (non-complex).  The caller should take
 * [BigRationalBase.sign] into consideration.
 *
 * @throws ArithmeticException if `this` is negative
 * @throws ArithmeticException if there is no exact root
 */
public fun <T : BigRationalBase<T>> T.sqrt(): T {
    val (nRoot, nRemainder) = numerator.sqrtAndRemainder()
    if (!nRemainder.isZero())
        throw ArithmeticException("No rational square root: $this")
    val (dRoot, dRemainder) = denominator.sqrtAndRemainder()
    if (!dRemainder.isZero())
        throw ArithmeticException("No rational square root: $this")

    return companion.valueOf(nRoot, dRoot)
}

/**
 * Returns an _exact_ square root (non-complex) `root` and a remainder `rem`
 * such that `this == root * root + remainder`, and `root` is the nearest
 * integer from beneath to the true square root (`floor(sqrt(n))` with `n` as
 * an IEEE 754 binary64 number). The caller should take [BigRationalBase.sign]
 * into consideration.
 *
 * Note: It follows from the above definition that root and remainder will
 * always be non-negative.
 *
 * @throws ArithmeticException if `this` is negative
 *
 * @todo Cleaner algorithm
 */
public fun <T : BigRationalBase<T>> T.sqrtAndRemainder(): Pair<T, T> {
    var root = companion.valueOf(numerator.sqrt(), denominator.sqrt())
    val square = root * root
    var remainder = this - square

    if (this >= square) return root to remainder

    root = companion.valueOf(root.numerator - BInt.ONE, root.denominator)
    remainder = this - root * root

    return root to remainder
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
 * @throws ArithmeticException if `this` is negative
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
 * Returns an _exact_ cube root (non-complex).  The caller should take
 * [BigRationalBase.sign] into consideration.
 *
 * @throws ArithmeticException if there is no exact root
 *
 * @todo Confirm that corner cases exist for denominator
 * @todo Nicer algorithm
 */
public fun <T : BigRationalBase<T>> T.cbrt(): T {
    val nRoot = cbrt(numerator.toDouble()).toBigDecimal().toBigIntegerExact()
    val dRoot = cbrt(denominator.toDouble()).toBigDecimal().toBigIntegerExact()
    val cbrt = companion.valueOf(nRoot, dRoot)

    if (this != cbrt * cbrt * cbrt)
        throw ArithmeticException("No rational cube root: $this")

    return cbrt
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
 * @todo The algorithm converts to a `Double` for the numerator and denominator
 *       seemingly unnecessarily rather than directly using `toDouble()`.  This
 *       avoids an `ArithmeticException`
 */
public fun <T : BigRationalBase<T>> T.cbrtApproximated(): T = try {
    cbrt()
} catch (_: ArithmeticException) {
    companion.valueOf(
        cbrt(numerator.toDouble() / denominator.toDouble())
    )
}

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

/** Rounds to the nearest _even_ whole number corresponding to [HALF_EVEN]. */
public fun <T : BigRationalBase<T>> T.round(): T = round(HALF_EVEN)

/**
 * Rounds to the nearest whole number towards the nearest infinity.  Zero
 * remains zero.
 *
 * @todo A better verb that is opposite of [truncate]
 */
public fun <T : BigRationalBase<T>> T.roundOut(): T =
    if (companion.ZERO < this) ceil() else floor()

/**
 * Rounds to the nearest whole number towards [goal].  The bound remains
 * itself.
 *
 * Current limitation: [goal] is assumed an integer
 *
 * @todo A more general algorithm that does not assume [goal] an integer
 */
public fun <T : BigRationalBase<T>> T.roundTowards(goal: T): T = when {
    goal == this -> this
    goal < this -> floor()
    else -> ceil()
}

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
