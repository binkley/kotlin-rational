package hm.binkley.math;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static hm.binkley.math.TestBigRational.Companion;
import static io.kotest.matchers.ShouldKt.shouldBe;

class BigRationalJavaTest {
    // TODO: Using Companion in static import is flaking
    private static final TestBigRational ZERO = Companion.ZERO;
    private static final TestBigRational ONE = Companion.ONE;
    private static final TestBigRational TWO = Companion.TWO;
    private static final TestBigRational TEN = Companion.TEN;

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
}
