package hm.binkley.math

/**
 * @todo Consider providing semiconvergents, eg,
 *       https://en.wikipedia.org/wiki/Continued_fraction#Semiconvergents
 * @todo Recast into a lazily-computed sequence, or provide an alternate form
 *       suitable for coroutines
 */
public abstract class ContinuedFractionBase<
    T : BRatBase<T>,
    C : ContinuedFractionBase<T, C>,
    > protected constructor(
    private val terms: List<T>,
    private val companion: ContinuedFractionCompanionBase<T, C>,
) : Number(), List<T> by terms, Comparable<CFracBase<T, C>> {
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

    override fun compareTo(other: CFracBase<T, C>): Int =
        toBigRational().compareTo(other.toBigRational())

    /**
     * @todo Provide `equivalent` so that `[1; 2] eq? [1; 1, 1]`, but is not JVM
     *       sense of `equals`.  Alternative: reduce continued fractions to
     *       "lowest terms"
     */
    override fun equals(other: Any?): Boolean = this === other ||
        other is CFracBase<*, *> &&
        terms == other.terms

    override fun hashCode(): Int = terms.hashCode()

    /** Returns the canonical representation of this continued fraction. */
    override fun toString(): String = when (size) {
        1 -> "[$integerPart;]"
        else -> terms.toString().replaceFirst(',', ';')
    }

    /** Helper for conversion to [T]. */
    protected fun backAgain(): T = subList(0, size - 1)
        .asReversed()
        .fold(last()) { previous, a_ni ->
            previous.unaryDiv() + a_ni
        }
}

/**
 * Returns a limited list of terms for the continued fraction.  For
 * example, `terms(0)` returns only the _integral part_ of this continued
 * fraction, and fractional terms begin at index 1.
 */
public fun <T : BRatBase<T>, C : CFracBase<T, C>> C.terms(
    fractionalTerms: Int
): List<T> = subList(0, fractionalTerms + 1)

/**
 * Returns the convergent up to [n] terms of the continued fraction.  The 0th
 * convergent is the integer part of the continued fraction.
 */
public fun <T : BRatBase<T>, C : CFracBase<T, C>> C.convergent(n: Int): T {
    if (0 > n) error("Convergents start from the 0th")

    val c0 = integerPart
    if (0 == n || fractionalParts.isEmpty()) return c0

    val term1 = fractionalParts[0]
    val c1 = (term1 * c0 + 1) / term1
    if (1 == n || 1 == fractionalParts.size) return c1

    return converge(this, n, 2, c1, c0)
}

private tailrec fun <T : BRatBase<T>> converge(
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
    T : BRatBase<T>,
    C : CFracBase<T, C>,
    >(private val ONE: T, private val TWO: T) {
    internal abstract fun constructTerm(term: BFixed): T
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
        integerPart: BFixed,
        vararg fractionalParts: BFixed,
    ): C {
        val terms = mutableListOf(constructTerm(integerPart))
        terms += fractionalParts.map { constructTerm(it) }
        return construct(terms)
    }

    /**
     * Creates a continued fraction for φ (the golden ration) of [n] parts.
     * Note two key properties:
     * - Convergents are ratios of Fibonacci numbers
     * - The approximation is rather slow
     */
    public fun phi(n: Int): C =
        if (0 < n) construct(List(n) { ONE })
        else error("Not enough digits to approximate φ: $n")

    /** Creates a continued fraction for √2 of [n] parts. */
    public fun root2(n: Int): C =
        if (0 < n) construct(MutableList(n - 1) { TWO }.prepend(ONE))
        else error("Not enough digits to approximate √2: $n")

    /** Creates a continued fraction for √3 of [n] parts. */
    public fun root3(n: Int): C =
        if (0 < n) construct(
            MutableList(n - 1) { index ->
                if (0 == index % 2) ONE else TWO
            }.prepend(ONE)
        )
        else error("Not enough digits to approximate √3: $n")
}

private fun <T> MutableList<T>.prepend(element: T) =
    also { add(0, element) }

/**
 * Checks if this continued fraction is _simple_ (has only 1 in all
 * numerators).
 */
public fun <T : BRatBase<T>, C : CFracBase<T, C>> C.isSimple(): Boolean =
    fractionalParts.all { it.numerator.isUnit() }

internal tailrec fun <T : BRatBase<T>> fractionateInPlace(
    r: T,
    sequence: MutableList<T>,
): List<T> {
    val (i, f) = r.toParts()
    sequence += i
    return if (f.isZero()) sequence
    else fractionateInPlace(f.unaryDiv(), sequence)
}

private fun <T : BRatBase<T>> T.toParts(): Pair<T, T> {
    val i = floor()
    return i to (this - i)
}
