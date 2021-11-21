package hm.binkley.math.floating

import hm.binkley.math.BInt
import hm.binkley.math.ContinuedFractionBase
import hm.binkley.math.ContinuedFractionCompanionBase
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.ONE
import hm.binkley.math.fractionateInPlace
import java.math.BigInteger

/**
 * `ContinuedFraction` represents a [FloatingBigRational] as a finite
 * continued fraction sequence with the integer part at the natural index
 * of 0.  Subsequent fraction parts use their natural index, starting at 1.
 * All numerators are 1.
 *
 * Elements are [FloatingBigRational] (rather than [BigInteger]) to express
 * continued fractions of non-finite [FloatingBigRational]s.  The continued
 * fraction of a non-finite [FloatingBigRational] is `[NaN;]`
 *
 * This class supports infinite continued fractions in a very limited sense;
 * none are calculated to their limit; all convert to [NaN].
 */
public class FloatingContinuedFraction private constructor(
    terms: List<FloatingBigRational>,
) : ContinuedFractionBase<FloatingBigRational, FloatingContinuedFraction>(
    terms, FloatingContinuedFraction
) {
    override fun construct(
        terms: List<FloatingBigRational>,
    ): FloatingContinuedFraction =
        FloatingContinuedFraction(terms)

    override fun toBigRational(): FloatingBigRational =
        if (!isFinite()) NaN
        else backAgain()

    public companion object :
        ContinuedFractionCompanionBase<FloatingBigRational,
            FloatingContinuedFraction>(ONE) {
        override fun constructTerm(term: BInt) =
            FloatingBigRational.valueOf(term)

        override fun construct(terms: List<FloatingBigRational>) =
            FloatingContinuedFraction(terms)

        override fun valueOf(
            r: FloatingBigRational,
        ): FloatingContinuedFraction {
            val terms = mutableListOf<FloatingBigRational>()
            when {
                !r.isFinite() -> terms += NaN
                else -> fractionateInPlace(r, terms)
            }
            return FloatingContinuedFraction(terms)
        }
    }
}

/**
 * Checks that this is a finite continued fraction.  All finite
 * BigRationals produce a finite continued fraction; all non-finite
 * BigRationals produce a non-finite continued fraction.
 */
public fun FloatingContinuedFraction.isFinite(): Boolean =
    integerPart.isFinite()
