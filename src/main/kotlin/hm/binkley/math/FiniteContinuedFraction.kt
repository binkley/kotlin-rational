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
class FiniteContinuedFraction private constructor(
    private val terms: List<BigRational>
) : List<BigRational> by terms {
    /** The integer part of this continued fraction. */
    val integerPart: BigRational = first()

    /** The fractional parts of this continued fraction. */
    val fractionalParts = subList(1, terms.lastIndex + 1)

    /** The multiplicative inverse of this continued fraction. */
    val reciprocal: FiniteContinuedFraction
        get() = if (ZERO === integerPart)
            FiniteContinuedFraction(fractionalParts)
        else
            FiniteContinuedFraction(listOf(ZERO) + terms)

    /** Returns the canonical representation of this continued fraction. */
    override fun toString() = when (size) {
        1 -> "[$integerPart;]"
        else -> terms.toString().replaceFirst(',', ';')
    }

    /**
     * Returns a limited list of terms for the continued fraction.  For
     * example, `terms(0)` returns only the _integral part_ of this continued
     * fraction.
     */
    fun terms(fractionalTerms: Int) = subList(0, fractionalTerms + 1)

    companion object {
        /**
         * Decomposes the given BigRational into a canonical continued
         * fraction.
         */
        fun valueOf(r: BigRational): FiniteContinuedFraction {
            val terms = mutableListOf<BigRational>()
            when {
                !r.isFinite() -> terms += NaN
                else -> fractionateInPlace(r, terms)
            }
            return FiniteContinuedFraction(terms)
        }

        /**
         * Creates a continued fraction from the given decomposed elements.
         */
        fun valueOf(
            integerPart: BigInteger,
            vararg a_i: BigInteger
        ): FiniteContinuedFraction {
            val terms = mutableListOf(integerPart.toBigRational())
            terms += a_i.map { it.toBigRational() }
            return FiniteContinuedFraction(terms)
        }
    }
}

/**
 * Checks if this continued fraction is _simple_ (has only 1 in all
 * numerators).
 */
fun FiniteContinuedFraction.isSimple(): Boolean {
    return fractionalParts.all { BInt.ONE === it.numerator }
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
 *
 * @todo A nicer way to have a `twofold` that processes two elements at a
 *       time, rather than `fold`'s one at a time.
 */
fun FiniteContinuedFraction.toBigRational() =
    if (!isFinite()) NaN
    else subList(
        0,
        size - 1
    ).asReversed().asSequence().fold(last()) { previous, a_ni ->
        a_ni + previous.reciprocal
    }

private tailrec fun fractionateInPlace(
    r: BigRational,
    sequence: MutableList<BigRational>
): List<BigRational> {
    val (i, f) = r.toParts()
    sequence += i
    if (ZERO === f) return sequence
    return fractionateInPlace(f.reciprocal, sequence)
}

private fun BigRational.toParts(): Pair<BigRational, BigRational> {
    val i = floor()
    return i to (this - i)
}
