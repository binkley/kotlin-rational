package hm.binkley.math

import java.util.Collections.nCopies

/**
 * @todo Consider providing semiconvergents, eg,
 *       https://en.wikipedia.org/wiki/Continued_fraction#Semiconvergents
 */
public abstract class ContinuedFractionBase<
    T : BigRationalBase<T>,
    C : ContinuedFractionBase<T, C>,
    > protected constructor(
    private val terms: List<T>,
    public open val companion: ContinuedFractionCompanionBase<T, C>,
) : Number(),
    List<T> by terms,
    Comparable<ContinuedFractionBase<T, C>> {
    protected abstract fun construct(terms: List<T>): C

    /** Returns the big rational for the continued fraction. */
    public abstract fun toBigRational(): T

    /** The integer part of this continued fraction. */
    public val integerPart: T get() = first()

    /** The fractional parts of this continued fraction. */
    public val fractionalParts: List<T> get() = subList(1, lastIndex + 1)

    /**
     * The multiplicative inverse of this continued fraction.
     *
     * @see unaryDiv
     */
    public val reciprocal: C get() = unaryDiv()

    /**
     * Adds this to [other].
     *
     * @see BigRationalBase.plus
     */
    public operator fun plus(other: C): C =
        companion.valueOf(toBigRational() + other.toBigRational())

    /**
     * Divides this by [other].
     *
     * @see BigRationalBase.plus
     */
    public operator fun minus(other: C): C =
        companion.valueOf(toBigRational() - other.toBigRational())

    /**
     * Multiplies this by [other].
     *
     * @see BigRationalBase.plus
     */
    public operator fun times(other: C): C =
        companion.valueOf(toBigRational() * other.toBigRational())

    /**
     * Divides this by [other].
     *
     * @see BigRationalBase.plus
     */
    public operator fun div(other: C): C =
        companion.valueOf(toBigRational() / other.toBigRational())

    /**
     * Simulates a non-existent "unary div" operator.
     *
     * @see reciprocal
     */
    public fun unaryDiv(): C = if (integerPart.isZero())
        construct(fractionalParts)
    else
        construct(listOf(integerPart.companion.ZERO) + terms)

    /**
     * Raises an [UnsupportedOperationException].  There is no sensible way to
     * express a general continued fraction as a character in a language.
     */
    override fun toChar(): Char =
        throw UnsupportedOperationException("Characters are non-numeric")

    override fun toByte(): Byte = toBigRational().toByte()
    override fun toDouble(): Double = toBigRational().toDouble()
    override fun toFloat(): Float = toBigRational().toFloat()
    override fun toInt(): Int = toBigRational().toInt()
    override fun toLong(): Long = toBigRational().toLong()
    override fun toShort(): Short = toBigRational().toShort()

    override fun compareTo(other: ContinuedFractionBase<T, C>): Int =
        toBigRational().compareTo(other.toBigRational())

    /**
     * @todo Provide `equivalent` so that [1; 2] eq? [1; 1, 1], but is not JVM
     *       sense of `equals`
     */
    override fun equals(other: Any?): Boolean = this === other ||
        other is ContinuedFractionBase<*, *> &&
        javaClass == other.javaClass &&
        terms == other.terms

    override fun hashCode(): Int = terms.hashCode()

    /** Returns the canonical representation of this continued fraction. */
    override fun toString(): String = when (size) {
        1 -> "[$integerPart;]"
        else -> terms.toString().replaceFirst(',', ';')
    }

    /**
     * Returns a limited list of terms for the continued fraction.  For
     * example, `terms(0)` returns only the _integral part_ of this continued
     * fraction.
     */
    public fun terms(fractionalTerms: Int): List<T> =
        subList(0, fractionalTerms + 1)
}

/**
 * Returns the convergent up to [n] terms of the continued fraction.  The 0th
 * convergent is the integer part of the continued fraction.
 *
 * @todo How is caller to know the # of convergents available?!
 */
public fun <T : BigRationalBase<T>, C : ContinuedFractionBase<T, C>> C.convergent(
    n: Int,
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
    c_2: T, // "c-2", meaning previous previous
): T {
    val termI = terms[i]
    val ci = (termI * c_1.numerator + c_2.numerator) /
        (termI * c_1.denominator + c_2.denominator)

    return if (n == i) ci
    else converge(terms, n, i + 1, ci, c_1)
}

public abstract class ContinuedFractionCompanionBase<
    T : BigRationalBase<T>,
    C : ContinuedFractionBase<T, C>,
    >(private val ONE: T) {
    internal abstract fun construct(integerPart: BInt): T
    internal abstract fun construct(terms: List<T>): C

    /**
     * Decomposes the given big rational into a canonical continued
     * fraction.
     */
    public open fun valueOf(r: T): C {
        val terms = mutableListOf<T>()
        fractionateInPlace(r, terms)
        return construct(terms)
    }

    /** Creates a continued fraction from the given decomposed elements. */
    public fun valueOf(
        integerPart: BInt,
        vararg fractionalParts: BInt,
    ): C {
        val terms = mutableListOf(construct(integerPart))
        terms += fractionalParts.map { construct(it) }
        return construct(terms)
    }

    /**
     * Creates a continued fraction for φ (the golden ration) of [n]
     * parts.  Note two key properties:
     * - Convergents are ratios of Fibonacci numbers
     * - The approximation is rather slow
     */
    public fun phi(n: Int): C =
        if (0 < n) construct(nCopies(n, ONE))
        else error("Not enough digits to approximate φ: $n")
}

internal fun <
    T : BigRationalBase<T>,
    C : ContinuedFractionBase<T, C>,
    > C.backAgain() = subList(0, size - 1)
    .asReversed()
    .fold(last()) { previous, a_ni ->
        previous.unaryDiv() + a_ni
    }

/**
 * Checks if this continued fraction is _simple_ (has only 1 in all
 * numerators).
 */
public fun <T : BigRationalBase<T>, C : ContinuedFractionBase<T, C>> C.isSimple(): Boolean =
    fractionalParts.all { it.numerator.isUnit() }

internal tailrec fun <T : BigRationalBase<T>> fractionateInPlace(
    r: T,
    sequence: MutableList<T>,
): List<T> {
    val (i, f) = r.toParts()
    sequence += i
    return if (f.isZero()) sequence
    else fractionateInPlace(f.unaryDiv(), sequence)
}

private fun <T : BigRationalBase<T>> T.toParts(): Pair<T, T> {
    val i = floor()
    return i to (this - i)
}
