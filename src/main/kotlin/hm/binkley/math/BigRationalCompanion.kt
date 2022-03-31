package hm.binkley.math

import hm.binkley.math.algebra.FieldCompanion
import java.math.BigInteger

/**
 * An abuse of Fields: `FixedBigRational` is a field, but
 * `FloatingBigRational` is not because of `NaN` and the infinities.
 *
 * *NB* &mdash; To avoid circular references, initialize [ZERO], [ONE], [TWO],
 * and [TEN] directly from constructors, and use neither [reduce] nor
 * [valueOf].
 *
 * @todo Provide`sqrt` via continued fractions, ie,
 *       https://en.wikipedia.org/wiki/Continued_fraction#Square_roots and
 *       [BigInteger.sqrtAndRemainder]
 * @todo Explore other ways to share code between fixed and floating flavors;
 *       `BigRationalBase` only exists to provide implementation inheritance
 * @todo `FieldCompanion` prevents ZERO and ONE from being JVM fields
 */
@Suppress("PropertyName")
public abstract class BigRationalCompanion<T : BRatBase<T>>(
    /**
     * A constant holding value 0 equivalent to `0 over 1`.
     *
     * Usable directly from Java via getter in `Companion`.
     */
    final override val ZERO: T,
    /**
     * A constant holding value 1 equivalent to `1 over 1`.
     *
     * Usable directly from Java via getter in `Companion`.
     */
    final override val ONE: T,
    /**
     * A constant holding value 2 equivalent to `2 over 1`.
     *
     * Usable directly from Java via `Companion`.
     */
    @JvmField
    public val TWO: T,
    /**
     * A constant holding value 10 equivalent to `10 over 1`.
     *
     * Usable directly from Java via `Companion`.
     */
    @JvmField
    public val TEN: T,
) : FieldCompanion<T> {
    public abstract fun valueOf(numerator: BFixed, denominator: BFixed): T

    /**
     * Since the conversion to a rational is _exact_, converting the resulting
     * rational back to a [BigDecimal] produces the original value.
     *
     * Note, however, such a round trip will _not_ preserve trailing zeroes,
     * just as converting BigDecimal -> Double -> BigDecimal does not preserve
     * them.
     */
    public open fun valueOf(floatingPoint: BFloating): T =
        when (floatingPoint) {
            0.0.big -> ZERO
            1.0.big -> ONE
            2.0.big -> TWO
            10.0.big -> TEN
            else -> {
                val scale = floatingPoint.scale()
                val unscaledValue = floatingPoint.unscaledValue()
                when {
                    0 == scale -> valueOf(unscaledValue)
                    0 > scale -> valueOf(
                        unscaledValue * (BigInteger.TEN `^` -scale)
                    )
                    else -> valueOf(unscaledValue, BigInteger.TEN `^` scale)
                }
            }
        }

    /**
     * Since the conversion to a rational is _exact_, converting the resulting
     * rational back to a [Double] produces the original value.
     */
    public open fun valueOf(floatingPoint: Double): T =
        valueOf(floatingPoint.toBigDecimal())

    /**
     * Since the conversion to a rational is _exact_, converting the resulting
     * rational back to a [Float] produces the original value.
     *
     * Note that [BigDecimal] has a constructor for `Double` but not for `Float`
     * and that leads to awkward handling for NaN and the infinities.
     */
    public open fun valueOf(floatingPoint: Float): T = when {
        floatingPoint.isFinite() -> valueOf(floatingPoint.toBigDecimal())
        else -> throw ArithmeticException("$floatingPoint: Not representable")
    }

    public fun valueOf(wholeNumber: BFixed): T = valueOf(wholeNumber, 1.big)
    public fun valueOf(wholeNumber: Long): T =
        valueOf(wholeNumber.toBigInteger())

    public fun valueOf(wholeNumber: Int): T =
        valueOf(wholeNumber.toBigInteger())

    /** Generates the Cantor spiral for walking the rationals. */
    public fun cantorSpiral(): Sequence<T> = CantorSpiral(this)

    /**
     * Returns an average value of elements in the collection.
     *
     * @todo Stdlib returns `Double`.  Should this fun return same?
     */
    public fun Iterable<T>.average(): T = sum() / count()

    /** Returns the sum of all elements in the collection. */
    public fun Iterable<T>.sum(): T = sumOf { it }

    /**
     * Returns the sum of all values produced by [selector] function applied to
     * each element in the collection.
     */
    public fun <E> Iterable<E>.sumOf(selector: (E) -> T): T =
        fold(ZERO) { acc, element -> acc + selector(element) }

    internal open fun iteratorCheck(first: T, last: T, step: T) {
        if (step.isZero()) error("Step must be non-zero.")
    }

    /**
     * Constructs a `T` _after_ derived types handle all special cases in
     * `valueOf`.
     * The most important special case is when [denominator] is `ZERO`
     * Common behavior for all derived types:
     * - Produces the constant `ZERO` for a numerator of `ZERO`
     * - Transfers the negative sign from the [denominator] to the constructed
     *   numerator
     * - Reduces the constructed numerator and denominator to lowest terms
     * - Produces the constants `ONE`, `TWO`, and `TEN` when the constructed big
     *   rational has those values
     *
     *  **The [denominator] may not be `ZERO`**.
     *
     * A typical `valueOf` looks like:
     * ```
     * override fun valueOf(numerator: BInt, denominator: BInt)
     *         : MyBigRationalType {
     *     // Handle special cases for numerator and denominator, such as
     *     // raising exceptions, or producing special values, especially for a
     *     // denominator of ZERO
     *     return construct(numerator, denominator) { n, d ->
     *         MyBigRationalType(n, d)
     *     }
     * }
     * ```
     */
    protected fun reduce(
        numerator: BFixed,
        denominator: BFixed,
        ctor: (BFixed, BFixed) -> T,
    ): T {
        if (numerator.isZero()) return ZERO

        var n = numerator
        var d = denominator
        if (-1 == d.signum()) {
            n = n.negate()
            d = d.negate()
        }

        val gcd = n.gcd(d)
        if (!gcd.isUnit()) {
            n /= gcd
            d /= gcd
        }

        if (d.isUnit()) when {
            n.isUnit() -> return ONE
            n.isTwo() -> return TWO
            n.isTen() -> return TEN
        }

        return ctor(n, d)
    }
}
