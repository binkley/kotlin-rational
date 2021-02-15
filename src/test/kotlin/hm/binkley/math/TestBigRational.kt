package hm.binkley.math

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
