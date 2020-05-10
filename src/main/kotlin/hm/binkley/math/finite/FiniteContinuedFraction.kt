package hm.binkley.math.finite

import hm.binkley.math.BInt
import hm.binkley.math.ContinuedFractionBase
import hm.binkley.math.ContinuedFractionCompanionBase
import hm.binkley.math.backAgain
import hm.binkley.math.finite.FixedBigRational.Companion.ONE
import java.math.BigInteger

/**
 * `FiniteContinuedFraction` represents a [FixedBigRational] as a finite
 * continued fraction sequence with the integer part at the natural index of
 * 0. Subsequent fraction parts use their natural index, starting at 1.
 *
 * Elements are [FixedBigRational] (rather than [BigInteger]) to express
 * continued fractions of non-finite [FixedBigRational]s.
 *
 * This class does not support infinite continued fractions; all represented
 * values are convertible to [FixedBigRational].
 */
class FiniteContinuedFraction private constructor(
    terms: List<FixedBigRational>
) : ContinuedFractionBase<FixedBigRational, FiniteContinuedFraction>(terms) {
    override fun construct(terms: List<FixedBigRational>) =
        FiniteContinuedFraction(terms)

    companion object :
        ContinuedFractionCompanionBase<FixedBigRational,
                FiniteContinuedFraction>(ONE) {
        override fun construct(integerPart: BInt) =
            FixedBigRational.valueOf(integerPart)

        override fun construct(terms: List<FixedBigRational>) =
            FiniteContinuedFraction(terms)
    }
}

/**
 * Returns the FiniteBigRational for the continued fraction.
 *
 * Note that the roundtrip of FiniteBigRational → ContinuedFraction →
 * FiniteBigRational is lossy for infinities, producing `NaN`.
 */
fun FiniteContinuedFraction.toFiniteBigRational() = backAgain()
