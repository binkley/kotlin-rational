package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.valueOf

internal infix fun BInt.over(denominator: BInt) = valueOf(this, denominator)
internal infix fun Long.over(denominator: Long) = big over denominator.big
internal infix fun Int.over(denominator: Long) = big over denominator.big
internal infix fun Int.over(denominator: Int) = big over denominator.big

internal class TestBigRational(
    numerator: BInt,
    denominator: BInt,
) : BigRationalBase<TestBigRational>(
    numerator,
    denominator,
) {
    override val companion: Companion get() = Companion

    companion object : BigRationalCompanion<TestBigRational>(
        ZERO = TestBigRational(BInt.ZERO, BInt.ONE),
        ONE = TestBigRational(BInt.ONE, BInt.ONE),
        TWO = TestBigRational(BInt.TWO, BInt.ONE),
        TEN = TestBigRational(BInt.TEN, BInt.ONE),
    ) {
        override fun valueOf(
            numerator: BInt,
            denominator: BInt,
        ): TestBigRational {
            if (denominator.isZero())
                throw ArithmeticException("division by zero")

            return reduce(numerator, denominator) { n, d ->
                TestBigRational(n, d)
            }
        }
    }
}
