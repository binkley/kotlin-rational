package hm.binkley.math.finite

import hm.binkley.math.BInt
import hm.binkley.math.ContinuedFractionBase
import hm.binkley.math.ContinuedFractionCompanionBase
import hm.binkley.math.backAgain
import hm.binkley.math.finite.FixedBigRational.Companion.ONE
import hm.binkley.math.nonfinite.FloatingBigRational
import java.math.BigInteger

/**
 * `ContinuedFraction` represents a [FloatingBigRational] as a finite
 * continued fraction sequence with the integer part at the natural index
 * of 0.  Subsequent fraction parts use their natural index, starting at 1.
 * All numerators are 1.
 *
 * Elements are [FixedBigRational] (rather than [BigInteger]) to express
 * continued fractions of non-finite [FixedBigRational]s.
 *
 * This class does not support infinite continued fractions; all represented
 * values are convertible to [FixedBigRational].
 */
class ContinuedFraction private constructor(
    terms: List<FixedBigRational>
) : ContinuedFractionBase<FixedBigRational, ContinuedFraction>(
    terms
) {
    override fun construct(terms: List<FixedBigRational>) =
        ContinuedFraction(terms)

    companion object :
        ContinuedFractionCompanionBase<FixedBigRational,
                ContinuedFraction>(ONE) {
        override fun construct(integerPart: BInt) =
            FixedBigRational.valueOf(integerPart)

        override fun construct(terms: List<FixedBigRational>) =
            ContinuedFraction(terms)
    }
}

/** Returns the FiniteBigRational for the continued fraction. */
fun ContinuedFraction.toBigRational() = backAgain()
