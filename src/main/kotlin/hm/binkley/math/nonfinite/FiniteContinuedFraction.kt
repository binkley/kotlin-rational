package hm.binkley.math.nonfinite

import hm.binkley.math.BInt
import hm.binkley.math.ContinuedFractionBase
import hm.binkley.math.ContinuedFractionCompanionBase
import hm.binkley.math.backAgain
import hm.binkley.math.fractionateInPlace
import hm.binkley.math.nonfinite.FloatingBigRational.Companion.NaN
import hm.binkley.math.nonfinite.FloatingBigRational.Companion.ONE
import java.math.BigInteger

/**
 * `ContinuedFraction` represents a [FloatingBigRational] as a finite continued
 * fraction sequence with the integer part at the natural index of 0.
 * Subsequent fraction parts use their natural index, starting at 1.
 *
 * Elements are [FloatingBigRational] (rather than [BigInteger]) to express continued
 * fractions of non-finite [FloatingBigRational]s.  The continued fraction of a
 * non-finite [FloatingBigRational] is `[NaN;]`
 *
 * This class supports infinite continued fractions in a very limited sense;
 * none are calculated to their limit; all convert to [NaN].
 */
class FiniteContinuedFraction private constructor(
    terms: List<FloatingBigRational>
) : ContinuedFractionBase<FloatingBigRational, FiniteContinuedFraction>(
    terms
) {
    override fun construct(terms: List<FloatingBigRational>) =
        FiniteContinuedFraction(terms)

    companion object :
        ContinuedFractionCompanionBase<FloatingBigRational,
                FiniteContinuedFraction>(ONE) {
        override fun construct(integerPart: BInt) =
            FloatingBigRational.valueOf(integerPart)

        override fun construct(terms: List<FloatingBigRational>) =
            FiniteContinuedFraction(terms)

        override fun valueOf(
            r: FloatingBigRational
        ): FiniteContinuedFraction {
            val terms = mutableListOf<FloatingBigRational>()
            when {
                !r.isFinite() -> terms += NaN
                else -> fractionateInPlace(r, terms)
            }
            return FiniteContinuedFraction(terms)
        }
    }
}

/**
 * Checks that this is a finite continued fraction.  All finite
 * BigRationals produce a finite continued fraction; all non-finite
 * BigRationals produce a non-finite continued fraction.
 */
fun FiniteContinuedFraction.isFinite() = integerPart.isFinite()

/**
 * Returns the BigRational for the continued fraction.
 *
 * Note that the roundtrip of BigRational → ContinuedFraction →
 * BigRational is lossy for infinities, producing `NaN`.
 */
fun FiniteContinuedFraction.toBigRational() =
    if (!isFinite()) NaN
    else backAgain()
