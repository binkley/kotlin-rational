package demo

import hm.binkley.math.algebra.Mod3Int
import hm.binkley.math.div
import hm.binkley.math.downTo
import hm.binkley.math.equivalent
import hm.binkley.math.fixed.FixedBigRational
import hm.binkley.math.fixed.complex.FixedBigImaginary.Companion.I
import hm.binkley.math.fixed.complex.conjugate
import hm.binkley.math.fixed.complex.plus
import hm.binkley.math.floating.FloatingBigRational
import hm.binkley.math.floating.FloatingBigRational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational.Companion.POSITIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.ZERO
import hm.binkley.math.floating.FloatingBigRational.Companion.cantorSpiral
import hm.binkley.math.floating.FloatingBigRational.Companion.sum
import hm.binkley.math.floating.over
import hm.binkley.math.floating.toBigRational
import hm.binkley.math.rangeTo
import hm.binkley.math.step

/** Runs the demo. */
public fun main() {
    println("==FLOATING BIG RATIONALS")

    println("ZERO is $ZERO")
    println("NaN is $NaN")
    println("POSITIVE_INFINITY is $POSITIVE_INFINITY")
    println("NEGATIVE_INFINITY is $NEGATIVE_INFINITY")
    println("1 is ${1.toBigRational()}")
    println("4/10 is ${4 over 10}")
    println("4/2 is ${4 over 2}")
    println("0/0 is ${0 over 0}")
    println("NaN is a unique object is ${NaN === 0 over 0}")
    println("But no NaN is equal is ${NaN != 0 over 0}")
    println("4/0 is ${4 over 0}")
    println("-4/0 is ${-4 over 0}")
    println("-4/-4 is ${-4 over -4}")

    val ratA = 3 over 5
    val ratB = 2 over 3
    println("$ratA ÷ $ratB is ${ratA / ratB}")

    val ratC = ZERO
    val ratD = 7 over 3
    val ratE = 1 over 2
    print("Progression from $ratC to $ratD incrementing by $ratE:")
    for (r in ratC..ratD step ratE) print(" $r")
    println()
    print("Progression from $ratD to $ratC decrementing by $ONE:")
    for (r in ratD downTo ratC) print(" $r")
    println()

    try {
        @Suppress("ControlFlowWithEmptyBody") for (r in POSITIVE_INFINITY..NaN); // ktlint-disable no-semi
    } catch (e: IllegalStateException) {
        println("Expected error for progression containing $NaN: $e")
    }

    println(
        "$POSITIVE_INFINITY greater than $ZERO is ${POSITIVE_INFINITY > ZERO}"
    )
    println(
        "$NEGATIVE_INFINITY less than $ZERO is ${NEGATIVE_INFINITY < ZERO}"
    )

    val toSort = listOf(
        POSITIVE_INFINITY,
        NaN,
        ZERO,
        POSITIVE_INFINITY,
        NaN,
        NEGATIVE_INFINITY,
        ZERO,
        NEGATIVE_INFINITY,
    )
    println("$toSort sorted is ${toSort.sorted()}")

    println()
    println("==DOUBLE CONVERSIONS")

    for (
        d in listOf(
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
            Double.NaN,
        )
    ) dump(d)

    println()
    println("==FLOAT CONVERSIONS")

    for (
        d in listOf(
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
            Float.NaN,
        )
    ) dump(d)

    println()
    println("==MOD3 INT")

    println("-1 (constructor) -> ${Mod3Int.valueOf(-1)}")
    println("-1 (inverse) -> ${-Mod3Int.ONE}")
    println("3-4 -> ${Mod3Int.valueOf(3) - Mod3Int.valueOf(4)}")
    println("3+4 -> ${Mod3Int.valueOf(3) + Mod3Int.valueOf(4)}")
    println("3*4 -> ${Mod3Int.valueOf(3) * Mod3Int.valueOf(4)}")

    println()
    println("==CANTOR SPIRAL")

    cantorSpiral().take(10).forEach {
        println(it)
    }

    println()
    println("==FIXED BIG COMPLEX NUMBERS")

    val onePlusI = FixedBigRational.ONE + I
    println(onePlusI)
    println(onePlusI * onePlusI.conjugate)

    println()
    println("==COMPARING FIXED AND FLOATING BIG RATIONALS")
    println(
        "1 fixed eq? 1 floating? -> ${
        FixedBigRational.ONE.equivalent(ONE)
        }"
    )

    println()
    println(
        "==SPECIAL PRINTING (UNICODE vulgar with solidus in some terminals)"
    )
    println(
        // 0 over 3 -- reduces to ZERO, not to a fraction
        // Note: JaCoCo does not spot exhaustive cases (ie, n/5) from
        // non-exhaustive ones (ie, n/6), so complains about "when" cases
        listOf(
            1 over 2,
            1 over 3,
            2 over 3,
            1 over 4,
            3 over 4,
            1 over 5,
            2 over 5,
            3 over 5,
            4 over 5,
            1 over 6,
            5 over 6,
            1 over 7,
            2 over 7, // Should be "2/7" with solidus
            1 over 8,
            3 over 8,
            5 over 8,
            7 over 8,
            1 over 9,
            2 over 9, // Should be "2/9" with solidus
            1 over 10,
            3 over 10, // Should be "3/10" with solidus
            1 over 11,
        ).joinToString { it.display }
    )

    println()
    println("== E^X (UP TO FIVE TERMS)")
    // TODO: Who needs memoization?
    fun `approxE^X`(exponent: FloatingBigRational, limit: Int = 5) {
        println(
            (0..limit).map {
                e(exponent, it)
            }.joinToString(prefix = "APPROX E^$exponent -> ") {
                it.toString()
            }
        )
    }
    `approxE^X`(ZERO)
    `approxE^X`(1 over 2)
    `approxE^X`(ONE)
}

private fun e(exponent: FloatingBigRational, terms: Int) = (0..terms).map {
    exponent.pow(it) / it.fact()
}.sum()

private tailrec fun Int.fact(x: Int = 1): Int =
    if (0 == this) x
    else (this - 1).fact(x * this)

private fun dump(d: Double) {
    val rat = d.toBigRational()

    println("$d -> $rat -> ${rat.toDouble()}")
}

private fun dump(f: Float) {
    val rat = f.toBigRational()

    println("$f -> $rat -> ${rat.toFloat()}")
}
