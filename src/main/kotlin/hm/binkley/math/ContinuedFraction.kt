package hm.binkley.math

import hm.binkley.math.BigRational.Companion.NaN
import hm.binkley.math.BigRational.Companion.ZERO

/**
 * `ContinuedFraction` represents a BigRational as a finite continued fraction
 * sequence with the integer part at the natural index of 0.  Subsequent
 * fraction parts use their natural index, starting at 1.
 *
 * Elements are `BigRational` (rather than `BigInteger`) to express continued
 * fractions of non-finite BigRations.  The continued fraction of a non-finite
 * BigRational is `[NaN;]`
 */
@Suppress("LocalVariableName") // Underscores in names
class ContinuedFraction private constructor(
    private val terms: List<BigRational>
) : List<BigRational> by terms {
    /** The integer part of this continued fraction. */
    val a0: BigRational
        get() = terms.first()

    /**
     * Checks that this is a finite continued fraction.  All finite
     * BigRationals produce a finite continued fraction; all non-finite
     * BigRationals produce a non-finite continued fraction.
     */
    fun isFinite() = a0.isFinite()

    /**
     * Returns the BigRational for the continued fraction.
     *
     * Note that the roundtrip of BigRational → ContinuedFraction →
     * BigRational is lossy for infinities, producing `NaN`.
     *
     * @todo A nicer way to have a `twofold` that processes two elements at a
     *       time, rather than `fold`'s one at a time.
     */
    fun toBigRational() =
        if (!isFinite()) NaN
        else terms.subList(
            0,
            terms.size - 1
        ).asReversed().asSequence().fold(terms.last()) { previous, a_ni ->
            previous.reciprocal + a_ni
        }

    /** Returns the canonical representation of this continued fraction. */
    override fun toString() = when (size) {
        1 -> "[$a0;]"
        else -> terms.toString().replaceFirst(',', ';')
    }

    companion object {
        fun valueOf(r: BigRational): ContinuedFraction {
            val a_s = mutableListOf<BigRational>()
            when {
                !r.isFinite() -> a_s += NaN
                else -> continuedFraction0(r, a_s)
            }
            return ContinuedFraction(a_s)
        }
    }
}

private tailrec fun continuedFraction0(
    r: BigRational,
    sequence: MutableList<BigRational>
): List<BigRational> {
    val (a_n, f) = r.integerAndFraction()
    sequence += a_n
    if (f == ZERO) return sequence
    return continuedFraction0(f.reciprocal, sequence)
}

private fun BigRational.integerAndFraction(): Pair<BigRational, BigRational> {
    val integer = floor()
    val fraction = this - integer
    return integer to fraction
}

