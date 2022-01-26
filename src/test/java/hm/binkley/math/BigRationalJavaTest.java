package hm.binkley.math;

import hm.binkley.math.floating.FloatingBigRational;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static hm.binkley.math.TestBigRational.Companion;
import static io.kotest.matchers.ShouldKt.shouldBe;

class BigRationalJavaTest {
    private static final TestBigRational ZERO = Companion.getZERO();
    private static final TestBigRational ONE = Companion.getONE();
    private static final TestBigRational TWO = Companion.TWO;
    private static final TestBigRational TEN = Companion.TEN;
    private static final FloatingBigRational NaN = FloatingBigRational.NaN;
    private static final FloatingBigRational POSITIVE_INFINITY = FloatingBigRational.POSITIVE_INFINITY;
    private static final FloatingBigRational NEGATIVE_INFINITY = FloatingBigRational.NEGATIVE_INFINITY;

    @Test
    void shouldHaveManifestConstantForZero() {
        shouldBe(ZERO.getNumerator(), BigInteger.ZERO);
        shouldBe(ZERO.getDenominator(), BigInteger.ONE);
    }

    @Test
    void shouldHaveManifestConstantForOne() {
        shouldBe(ONE.getNumerator(), BigInteger.ONE);
        shouldBe(ONE.getDenominator(), BigInteger.ONE);
    }

    @Test
    void shouldHaveManifestConstantForTwo() {
        shouldBe(TWO.getNumerator(), BigInteger.TWO);
        shouldBe(TWO.getDenominator(), BigInteger.ONE);
    }

    @Test
    void shouldHaveManifestConstantForTen() {
        shouldBe(TEN.getNumerator(), BigInteger.TEN);
        shouldBe(TEN.getDenominator(), BigInteger.ONE);
    }

    @Test
    void shouldConstruct() {
        shouldBe(ZERO, Companion.valueOf(BigInteger.ZERO, BigInteger.ONE));
        shouldBe(ZERO, Companion.valueOf(BigDecimal.ZERO));
        shouldBe(ZERO, Companion.valueOf(0.0d));
        shouldBe(ZERO, Companion.valueOf(0.0f));
        shouldBe(ZERO, Companion.valueOf(BigInteger.ZERO));
        shouldBe(ZERO, Companion.valueOf(0L));
        shouldBe(ZERO, Companion.valueOf(0));
    }

    @Test
    void shouldHaveTypeSpecificManifestConstants() {
        shouldBe(NaN.getNumerator(), BigInteger.ZERO);
        shouldBe(NaN.getDenominator(), BigInteger.ZERO);
        shouldBe(POSITIVE_INFINITY.getNumerator(), BigInteger.ONE);
        shouldBe(POSITIVE_INFINITY.getDenominator(), BigInteger.ZERO);
        shouldBe(NEGATIVE_INFINITY.getNumerator(), BigInteger.ONE.negate());
        shouldBe(NEGATIVE_INFINITY.getDenominator(), BigInteger.ZERO);
    }
}
