package hm.binkley.math

import hm.binkley.kotlin.SeekableSequence
import hm.binkley.math.Direction.E
import hm.binkley.math.Direction.N
import hm.binkley.math.Direction.S
import hm.binkley.math.Direction.W
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

private enum class Direction { N, S, E, W }

/** See https://youtu.be/3xyYs_eQTUc */
internal class CantorSpiral<T : BigRationalBase<T>>(
    private val companion: BigRationalCompanion<T>,
) : SeekableSequence<T> {
    override fun iterator() = object : Iterator<T> {
        // Reducing fractions produces duplicates
        private val seen = mutableSetOf<T>()
        private var x = ZERO
        private var y = ZERO
        private var dir = N

        /**
         * The spiral has no stopping point: it is infinite until caller grows
         * tired of new values.
         */
        override fun hasNext() = true

        override fun next(): T {
            do {
                val (numerator, denominator) = walk()
                // Skip over walks along the X and Y axes
                if (denominator.isZero()) continue
                val rat = companion.valueOf(numerator, denominator)
                if (seen.add(rat)) return rat
            } while (true)
        }

        private fun walk(): Pair<BFixed, BFixed> {
            when (dir) { // TODO: JaCoCo claims missing branch
                N -> {
                    ++y; if (y == x.abs() + ONE) dir = E
                }
                E -> {
                    ++x; if (x == y) dir = S
                }
                S -> {
                    --y; if (y.abs() == x) dir = W
                }
                W -> {
                    --x; if (x == y) dir = N
                }
            }
            return x to y
        }
    }
}
