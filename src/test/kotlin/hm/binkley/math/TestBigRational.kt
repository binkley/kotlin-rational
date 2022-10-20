package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.valueOf

private typealias BRat = TestBigRational

internal infix fun BFixed.over(denominator: BFixed) = valueOf(this, denominator)
internal infix fun Long.over(denominator: Long) = big over denominator.big
internal infix fun Int.over(denominator: Long) = big over denominator.big
internal infix fun Int.over(denominator: Int) = big over denominator.big

internal class TestBigRational(
    numerator: BFixed,
    denominator: BFixed,
) : BRatBase<TestBigRational>(numerator, denominator) {
    override val companion: Companion get() = Companion

    companion object : BigRationalCompanion<BRat>(
        ZERO = BRat(BFixed.ZERO, BFixed.ONE),
        ONE = BRat(BFixed.ONE, BFixed.ONE),
        TWO = BRat(BFixed.TWO, BFixed.ONE),
        TEN = BRat(BFixed.TEN, BFixed.ONE),
    ) {
        override fun valueOf(numerator: BFixed, denominator: BFixed): BRat {
            if (denominator.isZero()) {
                throw ArithmeticException("division by zero")
            }

            return reduce(numerator, denominator) { n, d ->
                BRat(n, d)
            }
        }
    }
}
