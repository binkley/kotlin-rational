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
        override fun constructTerm(term: BInt) =
            TestBigRational.valueOf(term)

        override fun construct(terms: List<TestBigRational>) =
            TestContinuedFraction(terms)
    }
}
