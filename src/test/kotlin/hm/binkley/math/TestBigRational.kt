package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.valueOf

internal infix fun Int.over(denominator: Int) = valueOf(big, denominator.big)

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

internal class BuggyTestBigRational(
    numerator: BInt,
    denominator: BInt,
) : BigRationalBase<BuggyTestBigRational>(
    numerator,
    denominator,
) {
    override val companion: Companion get() = Companion

    companion object : BigRationalCompanion<BuggyTestBigRational>(
        ZERO = BuggyTestBigRational(BInt.ZERO, BInt.ONE),
        ONE = BuggyTestBigRational(BInt.ONE, BInt.ONE),
        TWO = BuggyTestBigRational(BInt.TWO, BInt.ONE),
        TEN = BuggyTestBigRational(BInt.TEN, BInt.ONE),
    ) {
        override fun valueOf(
            numerator: BInt,
            denominator: BInt,
        ) = reduce(numerator, denominator) { n, d ->
            // Skip check for zero denominator to trigger assertion
            BuggyTestBigRational(n, d)
        }
    }
}
