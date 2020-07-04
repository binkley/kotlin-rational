package hm.binkley.math

import hm.binkley.math.Cantor.Direction.E
import hm.binkley.math.Cantor.Direction.N
import hm.binkley.math.Cantor.Direction.S
import hm.binkley.math.Cantor.Direction.W

/** See https://youtu.be/3xyYs_eQTUc */
internal class Cantor<T : BigRationalBase<T>>(
    private val companion: BigRationalCompanion<T>
) : Sequence<T> {
    enum class Direction { N, S, E, W }

    override fun iterator() = object : Iterator<T> {
        private val seen = mutableSetOf<T>()
        private var p = BInt.ZERO
        private var q = BInt.ZERO
        private var dir = N

        override fun hasNext() = true

        override fun next(): T {
            do {
                val ratio = walk()
                if (BInt.ZERO == ratio.second) continue
                val rat = companion.valueOf(ratio.first, ratio.second)
                if (seen.add(rat)) return rat
            } while (true)
        }

        private fun walk(): Pair<BInt, BInt> {
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
            return p to q
        }
    }
}
