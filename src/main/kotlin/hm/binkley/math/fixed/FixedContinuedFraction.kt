package hm.binkley.math.fixed

import hm.binkley.math.BInt
import hm.binkley.math.ContinuedFractionBase
import hm.binkley.math.ContinuedFractionCompanionBase
import hm.binkley.math.backAgain
import hm.binkley.math.fixed.FixedBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational
import java.math.BigInteger

/**
 * `ContinuedFraction` represents a [FloatingBigRational] as a finite
 * continued fraction sequence with the integer part at the natural index
 * of 0.  Subsequent fraction parts use their natural index, starting at 1.
 * All numerators are 1.
 *
 * Elements are [BRat] (rather than [BigInteger]) to express
 * continued fractions of non-finite [BRat]s.
 *
 * This class does not support infinite continued fractions; all represented
 * values are convertible to [BRat].
 */
class FixedContinuedFraction private constructor(
    terms: List<BRat>,
) : ContinuedFractionBase<BRat, FixedContinuedFraction>(
    terms
) {
    override fun construct(terms: List<BRat>): FixedContinuedFraction =
        FixedContinuedFraction(terms)

    override fun toBigRational(): BRat = backAgain()

    companion object :
        ContinuedFractionCompanionBase<BRat,
            FixedContinuedFraction>(ONE) {
        override fun construct(integerPart: BInt) =
            BRat.valueOf(integerPart)

        override fun construct(terms: List<BRat>) =
            FixedContinuedFraction(terms)
    }
}
