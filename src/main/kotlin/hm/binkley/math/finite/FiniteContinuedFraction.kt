package hm.binkley.math.finite

import hm.binkley.math.BInt
import hm.binkley.math.ContinuedFractionBase
import hm.binkley.math.ContinuedFractionCompanionBase
import hm.binkley.math.backAgain
import java.math.BigInteger

/**
 * `FiniteContinuedFraction` represents a [FiniteBigRational] as a finite
 * continued fraction sequence with the integer part at the natural index of
 * 0. Subsequent fraction parts use their natural index, starting at 1.
 *
 * Elements are [FiniteBigRational] (rather than [BigInteger]) to express
 * continued fractions of non-finite [FiniteBigRational]s.
 *
 * This class does not support infinite continued fractions; all represented
 * values are convertible to [FiniteBigRational].
 */
class FiniteContinuedFraction private constructor(
    terms: List<FiniteBigRational>
) : ContinuedFractionBase<FiniteBigRational, FiniteContinuedFraction>(terms) {
    override fun construct(terms: List<FiniteBigRational>) =
        FiniteContinuedFraction(terms)

    companion object :
        ContinuedFractionCompanionBase<FiniteBigRational, FiniteContinuedFraction> {
        override fun construct(integerPart: BInt) =
            FiniteBigRational.valueOf(integerPart)

        override fun construct(terms: List<FiniteBigRational>) =
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
