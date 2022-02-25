package hm.binkley.math

import hm.binkley.kotlin.SeekableSequence
import hm.binkley.math.Direction.E
import hm.binkley.math.Direction.N
import hm.binkley.math.Direction.S
import hm.binkley.math.Direction.W
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

private enum class Direction { N, S, E, W }

/**
 * Spirals through Cantor's diagonalization starting at the origin and heading
 * up; thereafter it progresses clockwise.  Each integer coordinate pair on the
 * grid represents the fraction, "x / y".
 *
 * The sequence skips over:
 * - Points on the X or Y axes except for (0, 1) (representing "0")
 * - Equivalent points already visited such as (-2, 3) and (2, -3) (both
 *   represent "-2 / 3")
 * - Points whose fraction reduces in lowest terms to an already visited point
 *   such as (1, 2) and (2, 4) (presenting "1 / 2")
 *
 * See https://youtu.be/3xyYs_eQTUc
 */
internal class CantorSpiral<T : BigRationalBase<T>>(
    private val companion: BigRationalCompanion<T>,
) : SeekableSequence<T> {
    override fun iterator() = object : Iterator<T> {
        private var x = ZERO
        private var y = ZERO
        private var dir = N

        /**
         * The spiral has no stopping point: it is infinite until caller grows
         * tired of new values.  It walks clockwise.
         */
        override fun hasNext() = true

        override fun next(): T {
            do {
                val (x, y) = walk()

                // Skip the Y axis, but include 0/1 (zero)
                if (x.isZero() && !y.isUnit()) continue
                // Skip the X axis
                if (y.isZero()) continue
                // Skip bottom side, but include SE corner
                if (W == dir && x != -y) continue
                // Skip left side, but include NW corner
                if (N == dir) continue
                // Skip reducible ratios, but include whole numbers
                if (!y.isUnit() && !x.gcd(y).isUnit()) continue

                return companion.valueOf(x, y)
            } while (true)
        }

        private fun walk(): Pair<BFixed, BFixed> {
            when (dir) { // TODO: JaCoCo claims missing branch
                N -> {
                    ++y; if (y - ONE == -x) dir = E
                }
                E -> {
                    ++x; if (x == y) dir = S
                }
                S -> {
                    --y; if (y == -x) dir = W
                }
                W -> {
                    --x; if (x == y) dir = N
                }
            }
            return x to y
        }
    }
}
