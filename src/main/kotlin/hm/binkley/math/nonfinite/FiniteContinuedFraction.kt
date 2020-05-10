package hm.binkley.math.nonfinite

import hm.binkley.math.BInt
import hm.binkley.math.ContinuedFractionBase
import hm.binkley.math.ContinuedFractionCompanionBase
import hm.binkley.math.backAgain
import hm.binkley.math.fractionateInPlace
import hm.binkley.math.nonfinite.BigRational.Companion.NaN
import java.math.BigInteger

/**
 * `ContinuedFraction` represents a [BigRational] as a finite continued
 * fraction sequence with the integer part at the natural index of 0.
 * Subsequent fraction parts use their natural index, starting at 1.
 *
 * Elements are [BigRational] (rather than [BigInteger]) to express continued
 * fractions of non-finite [BigRational]s.  The continued fraction of a
 * non-finite [BigRational] is `[NaN;]`
 *
 * This class supports infinite continued fractions in a very limited sense;
 * none are calculated to their limit; all convert to [NaN].
 */
class FiniteContinuedFraction private constructor(
    terms: List<BigRational>
) : ContinuedFractionBase<BigRational, FiniteContinuedFraction>(terms) {
    override fun construct(terms: List<BigRational>) =
        FiniteContinuedFraction(terms)

    companion object :
        ContinuedFractionCompanionBase<BigRational, FiniteContinuedFraction> {
        override fun construct(integerPart: BInt) =
            BigRational.valueOf(integerPart)

        override fun construct(terms: List<BigRational>) =
            FiniteContinuedFraction(terms)

        override fun valueOf(r: BigRational): FiniteContinuedFraction {
            val terms = mutableListOf<BigRational>()
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
