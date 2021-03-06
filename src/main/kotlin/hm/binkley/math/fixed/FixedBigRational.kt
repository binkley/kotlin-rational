package hm.binkley.math.fixed

import hm.binkley.math.BDouble
import hm.binkley.math.BInt
import hm.binkley.math.BigRationalBase
import hm.binkley.math.BigRationalCompanion
import hm.binkley.math.big
import hm.binkley.math.fixed.FixedBigRational.Companion.ZERO
import hm.binkley.math.fixed.FixedBigRational.Companion.valueOf
import hm.binkley.math.isZero

internal typealias BRat = FixedBigRational

/**
 * Immutable arbitrary-precision rationals (finite fractions).
 * `FiniteBigRational` provides analogues to all of Kotlin's [Long] operators
 * where appropriate. Additionally, `FiniteBigRational` provides operations
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
    numerator: BInt,
    denominator: BInt,
) : BigRationalBase<FixedBigRational>(
    numerator,
    denominator,
) {
    override val companion: Companion get() = Companion

    public companion object : BigRationalCompanion<FixedBigRational>(
        ZERO = FixedBigRational(0.big, 1.big),
        ONE = FixedBigRational(1.big, 1.big),
        TWO = FixedBigRational(2.big, 1.big),
        TEN = FixedBigRational(10.big, 1.big),
    ) {
        /**
         * Returns a `FiniteBigRational` whose value is equal to that of the
         * specified ratio, `numerator / denominator`.
         *
         * This factory method is in preference to an explicit constructor
         * allowing for reuse of frequently used FiniteBigRationals.  In
         * particular:
         *
         * * ZERO
         * * ONE
         * * TWO
         * * TEN
         */
        override fun valueOf(
            numerator: BInt,
            denominator: BInt,
        ): FixedBigRational {
            if (denominator.isZero())
                throw ArithmeticException("division by zero")

            return construct(numerator, denominator) { n, d ->
                FixedBigRational(n, d)
            }
        }

        override fun valueOf(floatingPoint: Double): FixedBigRational = when {
            !floatingPoint.isFinite() -> throw ArithmeticException(
                "non-finite"
            )
            else -> super.valueOf(floatingPoint)
        }
    }
}

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BDouble.over(denominator: BDouble): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BDouble.over(denominator: Double): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BDouble.over(denominator: Float): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BDouble.over(denominator: BInt): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BDouble.over(denominator: Long): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BDouble.over(denominator: Int): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: BDouble): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: BInt): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Long): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Int): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Double): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Double.over(denominator: Float): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: BDouble): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: BInt): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Long): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Int): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Double): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Float.over(denominator: Float): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BInt.over(denominator: BDouble): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BInt.over(denominator: Double): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BInt.over(denominator: Float): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BInt.over(denominator: BInt): FixedBigRational =
    valueOf(this, denominator)

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BInt.over(denominator: Long): FixedBigRational =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun BInt.over(denominator: Int): FixedBigRational =
    valueOf(this, denominator.toBigInteger())

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Double): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Float): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: BDouble): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: BInt): FixedBigRational =
    valueOf(toBigInteger(), denominator)

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Long): FixedBigRational =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Long.over(denominator: Int): FixedBigRational =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: BDouble): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Double): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Float): FixedBigRational =
    toBigRational() / denominator.toBigRational()

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: BInt): FixedBigRational =
    valueOf(toBigInteger(), denominator)

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Long): FixedBigRational =
    valueOf(toBigInteger(), denominator.toBigInteger())

/**
 * Returns a `FiniteBigRational` whose value is equal to that of the
 * specified ratio, `numerator / denominator`.
 *
 * @see valueOf
 */
public infix fun Int.over(denominator: Int): FixedBigRational =
    valueOf(toBigInteger(), denominator.toBigInteger())

/** Returns the value of this number as a `FiniteBigRational`. */
public fun BDouble.toBigRational(): FixedBigRational = valueOf(this)

/** Returns the value of this number as a `FiniteBigRational`. */
public fun Double.toBigRational(): FixedBigRational = valueOf(this)

/** Returns the value of this number as a `FiniteBigRational`. */
public fun Float.toBigRational(): FixedBigRational = valueOf(this)

/** Returns the value of this number as a `FiniteBigRational`. */
public fun BInt.toBigRational(): FixedBigRational = valueOf(this)

/** Returns the value of this number as a `FiniteBigRational`. */
public fun Long.toBigRational(): FixedBigRational = valueOf(this)

/** Returns the value of this number as a `FiniteBigRational`. */
public fun Int.toBigRational(): FixedBigRational = valueOf(this)

/** Returns the finite continued fraction of this `FiniteBigRational`. */
public fun FixedBigRational.toContinuedFraction(): FixedContinuedFraction =
    FixedContinuedFraction.valueOf(this)
