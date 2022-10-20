package hm.binkley.math.floating

import hm.binkley.math.BFixed
import hm.binkley.math.ContinuedFractionCompanionBase
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational.Companion.TWO
import hm.binkley.math.fractionateInPlace
import java.math.BigInteger

/**
 * `ContinuedFraction` represents a [FloatingBigRational] as a finite
 * continued fraction sequence with the integer part at the natural index
 * of 0.
 * Subsequent fraction parts use their natural index starting at 1.
 * All numerators are 1.
 *
 * Elements are [FloatingBigRational] (rather than [BigInteger]) to express
 * continued fractions of non-finite [FloatingBigRational]s.
 * The continued fraction of a non-finite [FloatingBigRational] is `[NaN;]`
 *
 * This class supports infinite continued fractions in a very limited sense;
 * none is calculated to their limit; all convert to [NaN].
 */
public class FloatingContinuedFraction private constructor(
    terms: List<BRat>,
) : CFracBase(terms, FloatingContinuedFraction) {
    /** Creates a new big continued fraction of big floating rationals. */
    override fun construct(terms: List<BRat>): CFrac = CFrac(terms)

    override fun toBigRational(): BRat =
        if (!isFinite()) {
            NaN
        } else {
            backAgain()
        }

    public companion object :
        ContinuedFractionCompanionBase<BRat, CFrac>(ONE, TWO) {
        override fun constructTerm(term: BFixed) = BRat.valueOf(term)

        override fun construct(terms: List<BRat>) = CFrac(terms)

        override fun valueOf(r: BRat): CFrac {
            val terms = mutableListOf<BRat>()
            when {
                !r.isFinite() -> terms += NaN
                else -> fractionateInPlace(r, terms)
            }
            return CFrac(terms)
        }
    }
}

/**
 * Checks that this continued fraction is finite.
 * All finite BigRationals produce finite continued fractions; all non-finite
 * BigRationals produce non-finite continued fractions.
 */
public fun CFrac.isFinite(): Boolean = integerPart.isFinite()
