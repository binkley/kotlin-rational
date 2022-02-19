package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.valueOf

internal infix fun BFixed.over(denominator: BFixed) = valueOf(this, denominator)
internal infix fun Long.over(denominator: Long) = big over denominator.big
internal infix fun Int.over(denominator: Long) = big over denominator.big
internal infix fun Int.over(denominator: Int) = big over denominator.big

internal class TestBigRational(
    numerator: BFixed,
    denominator: BFixed,
) : BigRationalBase<TestBigRational>(
    numerator,
    denominator,
) {
    override val companion: Companion get() = Companion

    companion object : BigRationalCompanion<TestBigRational>(
        ZERO = TestBigRational(BFixed.ZERO, BFixed.ONE),
        ONE = TestBigRational(BFixed.ONE, BFixed.ONE),
        TWO = TestBigRational(BFixed.TWO, BFixed.ONE),
        TEN = TestBigRational(BFixed.TEN, BFixed.ONE),
    ) {
        override fun valueOf(
            numerator: BFixed,
            denominator: BFixed,
        ): TestBigRational {
            if (denominator.isZero())
                throw ArithmeticException("division by zero")

            return reduce(numerator, denominator) { n, d ->
                TestBigRational(n, d)
            }
        }
    }
}
