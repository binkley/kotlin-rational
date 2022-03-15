package hm.binkley.math.fixed

import hm.binkley.math.BFixed
import hm.binkley.math.BFloating
import hm.binkley.math.BigRationalBase
import hm.binkley.math.BigRationalCompanion
import hm.binkley.math.big
import hm.binkley.math.div
import hm.binkley.math.equivalent
import hm.binkley.math.fixed.FixedBigRational.Companion.ONE
import hm.binkley.math.fixed.FixedBigRational.Companion.ZERO
import hm.binkley.math.fixed.FixedBigRational.Companion.valueOf
import hm.binkley.math.floating.FloatingBigRational
import hm.binkley.math.isZero

// Workarounds for Java interop
@JvmField
public val ZERO: BRat = ZERO

@JvmField
public val ONE: BRat = ONE

/**
 * Immutable arbitrary-precision rationals (finite fractions).
 * `FixedBigRational` provides analogues to all of Kotlin's [Long] operators
 * where appropriate. Additionally, `FixedBigRational` provides operations
 * for GCD and LCM calculation.
 *
 * Comparison operations perform signed comparisons, analogous to those
 * performed by Kotlin's relational and equality operators.
 *
 * Division by [ZERO] (or implied) throws [ArithmeticException] in all cases.
 *
 * Ranges increment by 1 unless otherwise specified.
 */
public class FixedBigRational private constructor(
    numerator: BFixed,
    denominator: BFixed,
) : BigRationalBase<FixedBigRational>(
    numerator,
    denominator,
) {
    override val companion: Companion get() = Companion

    public companion object : BigRationalCompanion<BRat>(
        ZERO = BRat(0.big, 1.big),
        ONE = BRat(1.big, 1.big),
        TWO = BRat(2.big, 1.big),
        TEN = BRat(10.big, 1.big),
    ) {
        /**
         * Returns a `BRat` whose value is equal to that of the
         * specified ratio, `numerator / denominator`.
         *
         * This factory method is in preference to an explicit constructor, and
         * allows for reuse of frequently used values.  In particular:
         *
         * * ZERO
         * * ONE
         * * TWO
         * * TEN
         */
        override fun valueOf(numerator: BFixed, denominator: BFixed): BRat {
            if (denominator.isZero())
                throw ArithmeticException("division by zero")

            return reduce(numerator, denominator) { n, d ->
                BRat(n, d)
            }
        }

        override fun valueOf(floatingPoint: Double): BRat = when {
            !floatingPoint.isFinite() ->
                throw ArithmeticException("non-finite")
            else -> super.valueOf(floatingPoint)
        }
    }
}

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFloating.over(denominator: BFloating): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFloating.over(denominator: Double): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFloating.over(denominator: Float): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFloating.over(denominator: BFixed): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFloating.over(denominator: Long): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFloating.over(denominator: Int): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: BFloating): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: BFixed): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Long): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Int): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Double): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Float): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: BFloating): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: BFixed): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Long): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Int): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Double): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Float): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFixed.over(denominator: BFloating): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFixed.over(denominator: Double): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFixed.over(denominator: Float): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFixed.over(denominator: BFixed): BRat =
    valueOf(this, denominator)

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFixed.over(denominator: Long): BRat =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BFixed.over(denominator: Int): BRat =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Double): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Float): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: BFloating): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: BFixed): BRat =
    valueOf(toBigInteger(), denominator)

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Long): BRat =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Int): BRat =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: BFloating): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Double): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Float): BRat =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: BFixed): BRat =
    valueOf(toBigInteger(), denominator)

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Long): BRat =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `BRat` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Int): BRat =
    valueOf(toBigInteger(), denominator.toBigInteger())

/** Returns the value of this number as a `BRat`. */
public fun BFloating.toBigRational(): BRat = valueOf(this)

/** Returns the value of this number as a `BRat`. */
public fun Double.toBigRational(): BRat = valueOf(this)

/** Returns the value of this number as a `BRat`. */
public fun Float.toBigRational(): BRat = valueOf(this)

/** Returns the value of this number as a `BRat`. */
public fun BFixed.toBigRational(): BRat = valueOf(this)

/** Returns the value of this number as a `BRat`. */
public fun Long.toBigRational(): BRat = valueOf(this)

/** Returns the value of this number as a `BRat`. */
public fun Int.toBigRational(): BRat = valueOf(this)

/** Returns the finite continued fraction of this `BRat`. */
public fun BRat.toContinuedFraction(): CFrac = CFrac.valueOf(this)

/**
 * Converts this _fixed_ big rational to a _floating_ equivalent.
 *
 * @see [equivalent]
 */
public fun BRat.toFloatingBigRational(): FloatingBigRational =
    FloatingBigRational.valueOf(numerator, denominator)

/**
 * Returns an average value of elements in the collection.
 *
 * @todo Stdlib returns `Double`.  Should this fun return same?
 */
public fun Iterable<BRat>.average(): BRat = sum() / count()

/** Returns the sum of all elements in the collection. */
public fun Iterable<BRat>.sum(): BRat = sumOf { it }

/**
 * Returns the sum of all values produced by [selector] function applied to each
 * element in the collection.
 */
public fun <T> Iterable<T>.sumOf(selector: (T) -> BRat): BRat =
    fold(ZERO) { acc, element -> acc + selector(element) }
