package hm.binkley.math

import java.util.Collections.nCopies

/**
 * @todo Make this a full mathematical object, eg,
 *       https://en.wikipedia.org/wiki/Continued_fraction#Comparison
 * @todo Consider providing infinite continued fractions as generators, eg,
 *       https://en.wikipedia.org/wiki/Continued_fraction#Other_continued_fraction_expansions
 * @todo Consider providing semiconvergents, eg,
 *       https://en.wikipedia.org/wiki/Continued_fraction#Semiconvergents
 */
abstract class ContinuedFractionBase<
        T : BigRationalBase<T>,
        C : ContinuedFractionBase<T, C>
        >(private val terms: List<T>) : List<T> by terms {
    protected abstract fun construct(terms: List<T>): C

    /** The integer part of this continued fraction. */
    val integerPart = first()

    /** The fractional parts of this continued fraction. */
    val fractionalParts = subList(1, lastIndex + 1)

    /** The multiplicative inverse of this continued fraction. */
    val reciprocal: C
        get() = if (integerPart.isZero())
            construct(fractionalParts)
        else
            construct(listOf(integerPart.companion.ZERO) + terms)

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
}

/**
 * Returns the convergent.
 *
 * See
 * [Infinite continued fractions and convergents](https://en.wikipedia.org/wiki/Continued_fraction#Infinite_continued_fractions_and_convergents)
 *
 * @todo https://en.wikipedia.org/wiki/Continued_fraction#Best_rational_approximations
 */
fun <T : BigRationalBase<T>, C : ContinuedFractionBase<T, C>> C.convergent(
    n: Int
): T {
    if (0 > n) error("Convergents start from the 0th")
    if (size <= n) error("Not enough terms for convergent: $n")

    val c0 = integerPart

    if (0 == n) return c0

    val term1 = this[1]
    val c1 = (term1 * c0 + 1) / term1

    return if (1 == n) c1
    else converge(this, n, 2, c1, c0)
}

private tailrec fun <T : BigRationalBase<T>> converge(
    terms: List<T>,
    n: Int, // limiting case
    i: Int, // current case
    c_1: T, // "c-1", meaning previous
    c_2: T // "c-2", meaning previous previous
): T {
    val termI = terms[i]
    val ci = (termI * c_1.numerator + c_2.numerator) /
            (termI * c_1.denominator + c_2.denominator)

    return if (n == i) ci
    else converge(terms, n, i + 1, ci, c_1)
}

abstract class ContinuedFractionCompanionBase<
        T : BigRationalBase<T>,
        C : ContinuedFractionBase<T, C>
        >(
            private val ONE: T
        ) {
    internal abstract fun construct(integerPart: BInt): T
    internal abstract fun construct(terms: List<T>): C

    /**
     * Decomposes the given big rational into a canonical continued
     * fraction.
     */
    open fun valueOf(r: T): C {
        val terms = mutableListOf<T>()
        fractionateInPlace(r, terms)
        return construct(terms)
    }

    /** Creates a continued fraction from the given decomposed elements. */
    fun valueOf(
        integerPart: BInt,
        vararg fractionalParts: BInt
    ): C {
        val terms = mutableListOf(construct(integerPart))
        terms += fractionalParts.map { construct(it) }
        return construct(terms)
    }

    /**
     * Creates a continued fraction for φ (the golden ration) of [n]
     * parts.
     * */
    fun phi(n: Int): C {
        if (1 > n) error("Not enough digits to approximate φ: $n")

        return construct(nCopies(n, ONE))
    }
}

/**
 * @todo A nicer way to have a `twofold` that processes two elements at a
 *       time, rather than `fold`'s one at a time.
 */
internal fun <
        T : BigRationalBase<T>,
        C : ContinuedFractionBase<T, C>
        > C.backAgain() = subList(0, size - 1)
    .asReversed()
    .asSequence()
    .fold(last()) { previous, a_ni ->
        previous.reciprocal + a_ni
    }

/**
 * Checks if this continued fraction is _simple_ (has only 1 in all
 * numerators).
 */
fun <T : BigRationalBase<T>, C : ContinuedFractionBase<T, C>> C.isSimple() =
    fractionalParts.all { BInt.ONE === it.numerator }

internal tailrec fun <T : BigRationalBase<T>> fractionateInPlace(
    r: T,
    sequence: MutableList<T>
): List<T> {
    val (i, f) = r.toParts()
    sequence += i
    if (f.isZero()) return sequence
    return fractionateInPlace(f.reciprocal, sequence)
}

private fun <T : BigRationalBase<T>> T.toParts(): Pair<T, T> {
    val i = floor()
    return i to (this - i)
}
