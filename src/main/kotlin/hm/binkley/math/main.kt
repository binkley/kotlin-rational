package hm.binkley.math

import hm.binkley.math.Cantor.Direction.E
import hm.binkley.math.Cantor.Direction.N
import hm.binkley.math.Cantor.Direction.S
import hm.binkley.math.Cantor.Direction.W
import hm.binkley.math.algebra.Mod3Int
import hm.binkley.math.floating.FloatingBigRational
import hm.binkley.math.floating.FloatingBigRational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.NaN
import hm.binkley.math.floating.FloatingBigRational.Companion.ONE
import hm.binkley.math.floating.FloatingBigRational.Companion.POSITIVE_INFINITY
import hm.binkley.math.floating.FloatingBigRational.Companion.ZERO
import hm.binkley.math.floating.isFinite
import hm.binkley.math.floating.over
import hm.binkley.math.floating.toBigRational
import lombok.Generated

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

    println("DOUBLE CONVERSIONS")
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
            Double.NaN
        )
    )
        dump(d)

    println("FLOAT CONVERSIONS")
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
            Float.NaN
        )
    )
        dump(d)

    println()
    println("== MOD3 INT")
    println("-1 (constructor) -> ${Mod3Int.valueOf(-1)}")
    println("-1 (inverse) -> ${-Mod3Int.ONE}")
    println("3-4 -> ${Mod3Int.valueOf(3) - Mod3Int.valueOf(4)}")
    println("3+4 -> ${Mod3Int.valueOf(3) + Mod3Int.valueOf(4)}")
    println("3*4 -> ${Mod3Int.valueOf(3) * Mod3Int.valueOf(4)}")

    println()
    println("== CANTOR")

    Cantor.take(10).forEach {
        println(it)
    }
}

/** See https://youtu.be/3xyYs_eQTUc */
@Generated // Lie to JaCoCo
private object Cantor : Sequence<FloatingBigRational> {
    enum class Direction { N, S, E, W }

    override fun iterator() = @Generated object
        : Iterator<FloatingBigRational> {
        private val seen = mutableSetOf<FloatingBigRational>()
        private var p = BInt.ZERO
        private var q = BInt.ZERO
        private var dir = N

        override fun hasNext() = true

        override fun next(): FloatingBigRational {
            var rat = walk()
            while (!rat.isFinite() || !seen.add(rat)) rat = walk()
            return rat
        }

        private fun walk(): FloatingBigRational {
            when (dir) {
                N -> {
                    ++q
                    if (q == p.abs() + BInt.ONE) dir = E
                }
                E -> {
                    ++p
                    if (p == q) dir = S
                }
                S -> {
                    --q
                    if (q.abs() == p) dir = W
                }
                W -> {
                    --p
                    if (p == q) dir = N
                }
            }
            return p over q
        }
    }
}

@Generated // Lie to JaCoCo
fun dump(d: Double) {
    val rat = d.toBigRational()

    println("$d -> $rat -> ${rat.toDouble()}")
}

@Generated // Lie to JaCoCo
fun dump(f: Float) {
    val rat = f.toBigRational()

    println("$f -> $rat -> ${rat.toFloat()}")
}
