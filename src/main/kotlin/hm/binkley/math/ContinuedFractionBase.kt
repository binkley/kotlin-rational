package hm.binkley.math

abstract class ContinuedFractionBase<
        T : BigRationalBase<T>,
        C : ContinuedFractionBase<T, C>
        >(
    private val terms: List<T>
) : List<T> by terms {
    protected abstract fun ctor(terms: List<T>): C

    /** The integer part of this continued fraction. */
    val integerPart = first()

    /** The fractional parts of this continued fraction. */
    val fractionalParts = subList(1, lastIndex + 1)

    /** The multiplicative inverse of this continued fraction. */
    val reciprocal: C
        get() = if (integerPart.isZero())
            ctor(fractionalParts)
        else
            ctor(listOf(integerPart.companion.ZERO) + terms)

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

interface ContinuedFractionCompanionBase<
        T : BigRationalBase<T>,
        C : ContinuedFractionBase<T, C>
        > {
    fun ctor(integerPart: BInt): T
    fun ctor(terms: List<T>): C

    /**
     * Decomposes the given BigRational into a canonical continued
     * fraction.
     */
    fun valueOf(r: T): C {
        val terms = mutableListOf<T>()
        fractionateInPlace(r, terms)
        return ctor(terms)
    }

    /**
     * Creates a continued fraction from the given decomposed elements.
     */
    fun valueOf(
        integerPart: BInt,
        vararg fractionalParts: BInt
    ): C {
        val terms = mutableListOf(ctor(integerPart))
        terms += fractionalParts.map { ctor(it) }
        return ctor(terms)
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
