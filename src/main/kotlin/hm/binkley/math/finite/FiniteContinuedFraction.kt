package hm.binkley.math.finite

import hm.binkley.math.finite.FiniteBigRational.Companion.ZERO
import java.math.BigInteger

/**
 * `FiniteContinuedFraction` represents a [FiniteBigRational] as a finite
 * continued fraction sequence with the integer part at the natural index of
 * 0. Subsequent fraction parts use their natural index, starting at 1.
 *
 * Elements are [FiniteBigRational] (rather than [BigInteger]) to express
 * continued fractions of non-finite [FiniteBigRational]s.
 *
 * This class does not support infinite continued fractions; all represented
 * values are convertible to [FiniteBigRational].
 *
 * @todo Properties/methods for convergents
 */
class FiniteContinuedFraction private constructor(
    private val terms: List<FiniteBigRational>
) : List<FiniteBigRational> by terms {
    /** The integer part of this continued fraction. */
    val integerPart = first()

    /** The fractional parts of this continued fraction. */
    val fractionalParts = subList(1, terms.lastIndex + 1)

    /** The multiplicative inverse of this continued fraction. */
    val reciprocal: FiniteContinuedFraction
        get() = if (integerPart.isZero())
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
         * Decomposes the given FiniteBigRational into a canonical continued
         * fraction.
         */
        fun valueOf(r: FiniteBigRational): FiniteContinuedFraction {
            val terms = mutableListOf<FiniteBigRational>()
            fractionateInPlace(r, terms)
            return FiniteContinuedFraction(terms)
        }

        /**
         * Creates a continued fraction from the given decomposed elements.
         */
        fun valueOf(
            integerPart: BigInteger,
            vararg fractionalParts: BigInteger
        ): FiniteContinuedFraction {
            val terms = mutableListOf(integerPart.toFiniteBigRational())
            terms += fractionalParts.map { it.toFiniteBigRational() }
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
 * Returns the FiniteBigRational for the continued fraction.
 *
 * Note that the roundtrip of FiniteBigRational → ContinuedFraction →
 * FiniteBigRational is lossy for infinities, producing `NaN`.
 *
 * @todo A nicer way to have a `twofold` that processes two elements at a
 *       time, rather than `fold`'s one at a time.
 */
fun FiniteContinuedFraction.toFiniteBigRational() =
    subList(
        0,
        size - 1
    ).asReversed().asSequence().fold(last()) { previous, a_ni ->
        a_ni + previous.reciprocal
    }

private tailrec fun fractionateInPlace(
    r: FiniteBigRational,
    sequence: MutableList<FiniteBigRational>
): List<FiniteBigRational> {
    val (i, f) = r.toParts()
    sequence += i
    if (f.isZero()) return sequence
    return fractionateInPlace(f.reciprocal, sequence)
}

private fun FiniteBigRational.toParts(): Pair<FiniteBigRational, FiniteBigRational> {
    val i = floor()
    return i to (this - i)
}
