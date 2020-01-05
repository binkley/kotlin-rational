package hm.binkley

import hm.binkley.math.Rational
import hm.binkley.math.Rational.Companion.NEGATIVE_INFINITY
import hm.binkley.math.Rational.Companion.NaN
import hm.binkley.math.Rational.Companion.POSITIVE_INFINITY
import hm.binkley.math.Rational.Companion.ZERO
import hm.binkley.math.downTo
import hm.binkley.math.over
import lombok.Generated

@Generated // Lie to JaCoCo
fun main() {
    println("ZERO is $ZERO")
    println("NaN is $NaN")
    println("POSITIVE_INFINITY is $POSITIVE_INFINITY")
    println("NEGATIVE_INFINITY is $NEGATIVE_INFINITY")
    println("1 is ${Rational.new(1)}")
    println("4/10 is ${4 over 10}")
    println("4/2 is ${4 over 2}")
    println(
        "0/0 is ${0 over 0}, and NaN is NaN is same object is ${NaN === 0 over 0}, and NaN is equal to itself ${NaN == 0 over 0}, and NaN is equal to itself also ${0 over 0 == NaN}"
    )
    println("4/0 is ${4 over 0}")
    println("-4/0 is ${-4 over 0}")
    println("-4/-4 is ${-4 over -4}")

    val ratA = 3 over 5
    val ratB = 2 over 3
    println("$ratA / $ratB is ${ratA / ratB}")

    val ratC = ZERO
    val ratD = Rational.new(7, 3)
    val ratE = Rational.new(1, 2)
    for (r in ratC..ratD step ratE) println(r)
    for (r in ratD downTo ratC) println(r)
    try {
        for (r in POSITIVE_INFINITY..NaN);
    } catch (e: IllegalStateException) {
        println("Expected: $e")
    }

    println("Compare infinities? ${POSITIVE_INFINITY > ZERO} and ${NEGATIVE_INFINITY < ZERO}")

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
}
