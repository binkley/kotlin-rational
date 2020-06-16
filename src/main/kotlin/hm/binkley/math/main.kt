package hm.binkley.math

import hm.binkley.math.algebra.Mod3Int
import hm.binkley.math.floating.FloatingBigRational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational.Companion.POSITIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.ZERO
import hm.binkley.math.floating.over
import hm.binkley.math.floating.toBigRational
import lombok.Generated
import java.math.BigDecimal

@Generated // Lie to JaCoCo
fun main() {
    println("== FLOATING BIG RATIONALS")
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
        @Suppress("ControlFlowWithEmptyBody")
        for (r in POSITIVE_INFINITY..NaN); // ktlint-disable no-semi
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
        NEGATIVE_INFINITY
    )
    println("$toSort sorted is ${toSort.sorted()}")

    for (d in listOf(
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
        Double.NaN,
        Double.MAX_VALUE,
        Double.MIN_VALUE // TODO: Conversion back yields 0.0 :(
    ))
        dump(d)

    println(BigDecimal("77.770").toBigRational())
    println(BigDecimal.ZERO.toBigRational())
    println(BigDecimal.ONE.toBigRational())
    println(BigDecimal.ONE.movePointLeft(1).toBigRational())

    println()
    println("== MOD3 INT")
    println("-1 (constructor) -> ${Mod3Int.of(-1)}")
    println("-1 (inverse) -> ${-Mod3Int.ONE}")
    println("3-4 -> ${Mod3Int.of(3) - Mod3Int.of(4)}")
    println("3+4 -> ${Mod3Int.of(3) + Mod3Int.of(4)}")
    println("3*4 -> ${Mod3Int.of(3) * Mod3Int.of(4)}")
}

@Generated // Lie to JaCoCo
fun dump(d: Double) {
    val rat = d.toBigRational()

    println("$d is $rat is ${rat.toDouble()}")
}
