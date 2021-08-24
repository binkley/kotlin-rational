package hm.binkley.math

import hm.binkley.kotlin.SeekableSequence
import hm.binkley.math.Direction.E
import hm.binkley.math.Direction.N
import hm.binkley.math.Direction.S
import hm.binkley.math.Direction.W

private enum class Direction { N, S, E, W }

/** See https://youtu.be/3xyYs_eQTUc */
internal class CantorSpiral<T : BigRationalBase<T>>(
    private val companion: BigRationalCompanion<T>,
) : SeekableSequence<T> {
    override fun iterator() = object : Iterator<T> {
        private val seen = mutableSetOf<T>() // cache previously computed values
        private var p = BInt.ZERO
        private var q = BInt.ZERO
        private var dir = N

        /**
         * The spiral has no stopping point: it is infinite until caller grows
         * tired of new values.
         */
        override fun hasNext() = true

        override fun next(): T {
            do {
                val (numerator, denominator) = walk()
                // Explicitly check for zero denominators so usable by fixed
                // big rational, which would otherwise raise an exception
                if (denominator.isZero()) continue
                val rat = companion.valueOf(numerator, denominator)
                if (seen.add(rat)) return rat
            } while (true)
        }

        private fun walk(): Pair<BInt, BInt> {
            when (dir) {
                N -> {
                    ++q; if (q == p.abs() + 1.big) dir = E
                }
                E -> {
                    ++p; if (p == q) dir = S
                }
                S -> {
                    --q; if (q.abs() == p) dir = W
                }
                W -> {
                    --p; if (p == q) dir = N
                }
            }
            return p to q
        }
    }
}
