package hm.binkley.math

/**
 * `ContinuedFraction` represents a BigRational as a finite continued fraction
 * sequence with the integer part at the natural index of 0.  Subsequent
 * fraction parts use their natural index, starting at 1.
 *
 * The continued fraction of a non-finite BigRational is `[NaN;]`
 */
class ContinuedFraction(
    private val r: BigRational,
    private val l: MutableList<BigRational> = mutableListOf()
) : List<BigRational> by l {
    init {
        when {
            !r.isFinite() -> l += BigRational.NaN
            else -> continuedFraction0(r, l)
        }
    }

    /** The integer part of this continued fraction. */
    val a0: BigRational
        get() = this[0]

    /**
     * Checks that this is a finite continued fraction.  All finite
     * BigRationals produce a finite continued fraction; all non-finite
     * BigRationals produce a non-finite continued fraction.
     */
    fun isFinite() = a0.isFinite()

    /** Returns the BigRational for the continued fraction. */
    fun toBigRational() = r

    /** Returns the canonical representation of this continued fraction. */
    override fun toString() = when (size) {
        1 -> "[$a0;]"
        else -> l.toString().replaceFirst(',', ';')
    }
}

private tailrec fun continuedFraction0(
    r: BigRational,
    sequence: MutableList<BigRational>
): List<BigRational> {
    val (a_n, f) = r.integerAndFraction()
    sequence += a_n
    if (f == BigRational.ZERO) return sequence
    return continuedFraction0(f.reciprocal, sequence)
}

private fun BigRational.integerAndFraction(): Pair<BigRational, BigRational> {
    val integer = floor()
    val fraction = this - integer
    return integer to fraction
}
