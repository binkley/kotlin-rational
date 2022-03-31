package hm.binkley.math

import hm.binkley.math.TestBigRational.Companion.ONE
import hm.binkley.math.TestBigRational.Companion.TWO

/** @todo IntelliJ inspection is confused */
@Suppress("unused")
internal class TestContinuedFraction private constructor(
    terms: List<TestBigRational>,
) : ContinuedFractionBase<TestBigRational, TestContinuedFraction>(
    terms, TestContinuedFraction
) {
    override fun construct(terms: List<TestBigRational>) =
        TestContinuedFraction(terms)

    override fun toBigRational(): TestBigRational = backAgain()

    companion object : ContinuedFractionCompanionBase<
        TestBigRational, TestContinuedFraction>(ONE, TWO) {
        override fun constructTerm(term: BFixed) = TestBigRational.valueOf(term)
        override fun construct(terms: List<TestBigRational>) =
            TestContinuedFraction(terms)
    }
}
