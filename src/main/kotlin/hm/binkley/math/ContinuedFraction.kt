package hm.binkley.math

import hm.binkley.math.BigRational.Companion.NaN
import hm.binkley.math.BigRational.Companion.ZERO
import java.math.BigInteger

/**
 * `ContinuedFraction` represents a BigRational as a finite continued fraction
 * sequence with the integer part at the natural index of 0.  Subsequent
 * fraction parts use their natural index, starting at 1.
 *
 * Elements are `BigRational` (rather than `BigInteger`) to express continued
 * fractions of non-finite BigRations.  The continued fraction of a non-finite
 * BigRational is `[NaN;]`
 *
 * This class does not support infinite continued fractions; all represented
 * values are convertible to `BigRational`.
 */
@Suppress("LocalVariableName", "PropertyName") // Underscores
class ContinuedFraction private constructor(
    private val terms: List<BigRational>
) : List<BigRational> by terms {
    /** Returns the canonical representation of this continued fraction. */
    override fun toString() = when (size) {
        1 -> "[$a_0;]"
        else -> terms.toString().replaceFirst(',', ';')
    }

    companion object {
        /**
         * Decomposes the given BigRational into a canonical continued
         * fraction.
         */
        fun valueOf(r: BigRational): ContinuedFraction {
            val terms = mutableListOf<BigRational>()
            when {
                !r.isFinite() -> terms += NaN
                else -> continuedFraction0(r, terms)
            }
            return ContinuedFraction(terms)
        }

        /**
         * Creates a continued fraction from the given decomposed elements.
         */
        fun valueOf(
            a0: BigInteger,
            vararg a_i: BigInteger
        ): ContinuedFraction {
            val terms = mutableListOf(a0.toBigRational())
            terms += a_i.map { it.toBigRational() }
            return ContinuedFraction(terms)
        }
    }
}

/**
 * The integer part of this continued fraction.
 *
 * @todo Find name for first element of continued fraction */
val ContinuedFraction.a_0: BigRational
    get() = first()

/**
 * Checks that this is a finite continued fraction.  All finite
 * BigRationals produce a finite continued fraction; all non-finite
 * BigRationals produce a non-finite continued fraction.
 */
fun ContinuedFraction.isFinite() = a_0.isFinite()

/**
 * Returns the BigRational for the continued fraction.
 *
 * Note that the roundtrip of BigRational → ContinuedFraction →
 * BigRational is lossy for infinities, producing `NaN`.
 *
 * @todo A nicer way to have a `twofold` that processes two elements at a
 *       time, rather than `fold`'s one at a time.
 */
fun ContinuedFraction.toBigRational() =
    if (!isFinite()) NaN
    else subList(
        0,
        size - 1
    ).asReversed().asSequence().fold(last()) { previous, a_ni ->
        a_ni + previous.reciprocal
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
    val floor = floor()
    return floor to (this - floor)
}
