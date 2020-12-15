package hm.binkley.math

internal class TestBigRational(
    numerator: BInt,
    denominator: BInt,
) : BigRationalBase<TestBigRational>(numerator, denominator, Companion) {
    companion object : BigRationalCompanion<TestBigRational> {
        override val ZERO: TestBigRational =
            TestBigRational(BInt.ZERO, BInt.ONE)
        override val ONE: TestBigRational =
            TestBigRational(BInt.ONE, BInt.ONE)
        override val TWO: TestBigRational =
            TestBigRational(BInt.TWO, BInt.ONE)
        override val TEN: TestBigRational =
            TestBigRational(BInt.TEN, BInt.ONE)

        override fun valueOf(
            numerator: BInt,
            denominator: BInt,
        ): TestBigRational = construct(numerator, denominator) { n, d ->
            TestBigRational(n, d)
        }

        override fun iteratorCheck(
            first: TestBigRational,
            last: TestBigRational,
            step: TestBigRational,
        ) {
            if (step.isZero()) error("Step must be non-zero.")
        }
    }
}
