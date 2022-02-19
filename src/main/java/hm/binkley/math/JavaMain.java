package hm.binkley.math;

import hm.binkley.math.algebra.Mod3Int;
import hm.binkley.math.fixed.complex.FixedBigImaginary;
import hm.binkley.math.fixed.FixedBigRationalKt;
import hm.binkley.math.floating.FloatingBigRational;
import lombok.Generated;

import java.util.List;

import static hm.binkley.math.BigRationalProgressionKt.downTo;
import static hm.binkley.math.BigRationalProgressionKt.rangeTo;
import static hm.binkley.math.BigRationalProgressionKt.step;
import static hm.binkley.math.ComparisonsKt.equivalent;
import static hm.binkley.math.fixed.complex.FixedBigComplexKt.getConjugate;
import static hm.binkley.math.fixed.complex.FixedBigComplexKt.plus;
import static hm.binkley.math.floating.FloatingBigRational.NEGATIVE_INFINITY;
import static hm.binkley.math.floating.FloatingBigRational.NaN;
import static hm.binkley.math.floating.FloatingBigRational.POSITIVE_INFINITY;
import static hm.binkley.math.floating.FloatingBigRationalKt.ONE;
import static hm.binkley.math.floating.FloatingBigRationalKt.ZERO;
import static hm.binkley.math.floating.FloatingBigRationalKt.over;
import static hm.binkley.math.floating.FloatingBigRationalKt.toBigRational;
import static java.lang.System.out;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Generated // Lie to JaCoCo
public final class JavaMain {
    public static void main(final String... args) {
        out.println("==FLOATING BIG RATIONALS");

        out.printf("ZERO is %s%n", ZERO);
        out.printf("NaN is %s%n", NaN);
        out.printf("POSITIVE_INFINITY is %s%n", POSITIVE_INFINITY);
        out.printf("NEGATIVE_INFINITY is %s%n", NEGATIVE_INFINITY);
        out.printf("1 is %s%n", toBigRational(1));
        out.printf("4/10 is %s%n", over(4, 10));
        out.printf("4/2 is %s%n", over(4, 2));
        out.printf("0/0 is %s%n", over(0, 0));
        //noinspection NumberEquality
        out.printf("NaN is a unique object is %s%n", NaN == over(0, 0));
        out.printf("But no NaN is equal is %s%n", !NaN.equals(over(0, 0)));
        out.printf("4/0 is %s%n", over(4, 0));
        out.printf("-4/0 is %s%n", over(-4, 0));
        out.printf("-4/-4 is %s%n", over(-4, -4));

        var ratA = over(3, 5);
        var ratB = over(2, 3);
        out.printf("%s ÷ %s is %s%n", ratA, ratB, ratA.div(ratB));

        var ratC = ZERO;
        var ratD = over(7, 3);
        var ratE = over(1, 2);
        out.printf("Progression from %s to %s incrementing by %s:",
                ratC, ratD, ratE);
        for (var r : step(rangeTo(ratC, ratD), ratE))
            out.print(" " + r);
        out.println();
        out.printf("Progression from %s to %s decrementing by %s:",
                ratD, ratC, ONE);
        for (var r : downTo(ratD, ratC))
            out.print(" " + r);
        out.println();

        try {
            //noinspection StatementWithEmptyBody
            for (var ignored : rangeTo(POSITIVE_INFINITY, NaN)) ;
        } catch (final IllegalStateException e) {
            out.printf("Expected error for progression containing %s: %s%n", NaN, e);
        }

        out.printf("%s greater than %s is %s%n",
                POSITIVE_INFINITY, ZERO,
                0 < POSITIVE_INFINITY.compareTo(ZERO));
        out.printf("%s less than %s is %s%n",
                NEGATIVE_INFINITY, ZERO,
                0 > NEGATIVE_INFINITY.compareTo(ZERO));

        final var toSort = List.of(
                POSITIVE_INFINITY,
                NaN,
                ZERO,
                POSITIVE_INFINITY,
                NaN,
                NEGATIVE_INFINITY,
                ZERO,
                NEGATIVE_INFINITY);

        out.printf("%s sorted is %s%n", toSort,
                toSort.stream().sorted().collect(toList()));

        out.println();
        out.println("==DOUBLE CONVERSIONS");

        for (var d : List.of(
                -4.0,
                -3.0,
                -2.0,
                -0.5,
                -0.3,
                -0.1,
                0.0,
                0.1,
                0.3,
                0.5,
                2.0,
                3.0,
                4.0,
                123.456,
                Double.MAX_VALUE,
                Double.MIN_VALUE,
                Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY,
                Double.NaN)
        )
            dump(d);

        out.println();
        out.println("==FLOAT CONVERSIONS");

        for (var d : List.of(
                -4.0f,
                -3.0f,
                -2.0f,
                -0.5f,
                -0.3f,
                -0.1f,
                0.0f,
                0.1f,
                0.3f,
                0.5f,
                2.0f,
                3.0f,
                4.0f,
                123.456f,
                Float.MAX_VALUE,
                Float.MIN_VALUE,
                Float.POSITIVE_INFINITY,
                Float.NEGATIVE_INFINITY,
                Float.NaN)
        )
            dump(d);

        out.println();
        out.println("==MOD3 INT");

        out.printf("-1 (constructor) -> %s%n", Mod3Int.valueOf(-1));
        out.printf("-1 (inverse) -> %s%n", Mod3Int.Companion.getONE().unaryMinus());
        out.printf("3-4 -> %s%n", Mod3Int.valueOf(3).minus(Mod3Int.valueOf(4)));
        out.printf("3+4 -> %s%n", Mod3Int.valueOf(3).plus(Mod3Int.valueOf(4)));
        out.printf("3*4 -> %s%n", Mod3Int.valueOf(3).times(Mod3Int.valueOf(4)));

        out.println();
        out.println("==CANTOR SPIRAL");

        final var cantorSequence = FloatingBigRational.Companion.cantorSpiral();
        final Iterable<FloatingBigRational> cantorIterable = cantorSequence::iterator;
        stream(cantorIterable.spliterator(), false)
                .limit(10)
                .forEach(out::println);

        out.println();
        out.println("==FIXED BIG COMPLEX NUMBERS");

        final var onePlusI = plus(FixedBigRationalKt.ONE,
                FixedBigImaginary.I);
        out.println(onePlusI);
        out.println(onePlusI.times(getConjugate(onePlusI)));

        out.println();
        out.println("==COMPARING FIXED AND FLOATING BIG RATIONALS");
        out.printf("1 fixed eq? 1 floating? -> %s%n",
                equivalent(FixedBigRationalKt.ONE, ONE));
    }

    private static void dump(final Double d) {
        var rat = toBigRational(d);

        out.printf("%s -> %s -> %s%n", d, rat, rat.toDouble());
    }

    private static void dump(final Float f) {
        var rat = toBigRational(f);

        out.printf("%s -> %s -> %s%n", f, rat, rat.toFloat());
    }
}
