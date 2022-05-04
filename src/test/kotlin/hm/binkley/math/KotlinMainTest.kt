package hm.binkley.math

import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrAndOutNormalized
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class KotlinMainTest {
    @Test
    fun `should run`() {
        tapSystemErrAndOutNormalized {
            main()
        } shouldBeAfterTrimming """
==FLOATING BIG RATIONALS
ZERO is 0
NaN is NaN
POSITIVE_INFINITY is Infinity
NEGATIVE_INFINITY is -Infinity
1 is 1
4/10 is 2⁄5
4/2 is 2
0/0 is NaN
NaN is a unique object is true
But no NaN is equal is true
4/0 is Infinity
-4/0 is -Infinity
-4/-4 is 1
3⁄5 ÷ 2⁄3 is 9⁄10
Progression from 0 to 7⁄3 incrementing by 1⁄2: 0 1⁄2 1 3⁄2 2
Progression from 7⁄3 to 0 decrementing by 1: 7⁄3 4⁄3 1⁄3
Expected error for progression containing NaN: java.lang.IllegalStateException: Non-finite bounds.
Infinity greater than 0 is true
-Infinity less than 0 is true
[Infinity, NaN, 0, Infinity, NaN, -Infinity, 0, -Infinity] sorted is [-Infinity, -Infinity, 0, 0, Infinity, Infinity, NaN, NaN]

==DOUBLE CONVERSIONS
-4.0 -> -4 -> -4.0
-3.0 -> -3 -> -3.0
-2.0 -> -2 -> -2.0
-0.5 -> -1⁄2 -> -0.5
-0.3 -> -3⁄10 -> -0.3
-0.1 -> -1⁄10 -> -0.1
0.0 -> 0 -> 0.0
0.1 -> 1⁄10 -> 0.1
0.3 -> 3⁄10 -> 0.3
0.5 -> 1⁄2 -> 0.5
2.0 -> 2 -> 2.0
3.0 -> 3 -> 3.0
4.0 -> 4 -> 4.0
123.456 -> 15432⁄125 -> 123.456
1.7976931348623157E308 -> 179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000 -> 1.7976931348623157E308
4.9E-324 -> 49⁄10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000 -> 4.9E-324
Infinity -> Infinity -> Infinity
-Infinity -> -Infinity -> -Infinity
NaN -> NaN -> NaN

==FLOAT CONVERSIONS
-4.0 -> -4 -> -4.0
-3.0 -> -3 -> -3.0
-2.0 -> -2 -> -2.0
-0.5 -> -1⁄2 -> -0.5
-0.3 -> -3⁄10 -> -0.3
-0.1 -> -1⁄10 -> -0.1
0.0 -> 0 -> 0.0
0.1 -> 1⁄10 -> 0.1
0.3 -> 3⁄10 -> 0.3
0.5 -> 1⁄2 -> 0.5
2.0 -> 2 -> 2.0
3.0 -> 3 -> 3.0
4.0 -> 4 -> 4.0
123.456 -> 15432⁄125 -> 123.456
3.4028235E38 -> 340282350000000000000000000000000000000 -> 3.4028235E38
1.4E-45 -> 7⁄5000000000000000000000000000000000000000000000 -> 1.4E-45
Infinity -> Infinity -> Infinity
-Infinity -> -Infinity -> -Infinity
NaN -> NaN -> NaN

==MOD3 INT
-1 (constructor) -> 2
-1 (inverse) -> 2
3-4 -> 2
3+4 -> 1
3*4 -> 0

==CANTOR SPIRAL
0
1
-1
-1⁄2
1⁄2
2
-2
-2⁄3
-1⁄3
1⁄3

==FIXED BIG COMPLEX NUMBERS
1+1i
2+0i

==COMPARING FIXED AND FLOATING BIG RATIONALS
1 fixed eq? 1 floating? -> true

==SPECIAL PRINTING (UNICODE vulgar with solidus in some terminals)
½, ⅓, ⅔, ¼, ¾, ⅕, ⅖, ⅗, ⅘, ⅙, ⅚, ⅐, 2⁄7, ⅛, ⅜, ⅝, ⅞, ⅑, 2⁄9, ⅒, 3⁄10, 1⁄11

== E^X (UP TO FIVE TERMS)
APPROX E^0 -> 1, 1, 1, 1, 1, 1
APPROX E^1⁄2 -> 1, 3⁄2, 13⁄8, 79⁄48, 211⁄128, 6331⁄3840
APPROX E^1 -> 1, 2, 5⁄2, 8⁄3, 65⁄24, 163⁄60
        """
    }
}

private infix fun String.shouldBeAfterTrimming(expected: String) =
    trimIndent().trim() shouldBe expected.trimIndent().trim()
