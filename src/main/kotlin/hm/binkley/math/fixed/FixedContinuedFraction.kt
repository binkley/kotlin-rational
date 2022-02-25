package hm.binkley.math.fixed

import hm.binkley.math.BFixed
import hm.binkley.math.ContinuedFractionBase
import hm.binkley.math.ContinuedFractionCompanionBase
import hm.binkley.math.fixed.FixedBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational
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
public class FixedContinuedFraction private constructor(terms: List<BRat>) :
    ContinuedFractionBase<BRat, FixedContinuedFraction>(
        terms, FixedContinuedFraction
    ) {
    override fun construct(terms: List<BRat>): CFrac = CFrac(terms)

    override fun toBigRational(): BRat = backAgain()

    public companion object : ContinuedFractionCompanionBase<BRat,
        CFrac>(ONE) {
        override fun constructTerm(term: BFixed) = BRat.valueOf(term)

        override fun construct(terms: List<BRat>) = CFrac(terms)
    }
}
