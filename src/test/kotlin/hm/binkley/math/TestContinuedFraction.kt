package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE

internal class TestContinuedFraction private constructor(
    terms: List<TestBigRational>,
) : ContinuedFractionBase<TestBigRational, TestContinuedFraction>(
    terms, TestContinuedFraction
) {
    override fun construct(terms: List<TestBigRational>) =
        TestContinuedFraction(terms)

    override fun toBigRational(): TestBigRational = backAgain()

    companion object : ContinuedFractionCompanionBase<
        TestBigRational, TestContinuedFraction>(ONE) {
        override fun construct(integerPart: BInt) =
            TestBigRational.valueOf(integerPart)

        override fun construct(terms: List<TestBigRational>) =
            TestContinuedFraction(terms)
    }
}
