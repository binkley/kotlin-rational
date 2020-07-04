package hm.binkley.math

import hm.binkley.math.Cantor.Direction.E
import hm.binkley.math.Cantor.Direction.N
import hm.binkley.math.Cantor.Direction.S
import hm.binkley.math.Cantor.Direction.W
import hm.binkley.math.floating.FloatingBigRational
import hm.binkley.math.floating.isFinite
import hm.binkley.math.floating.over
import lombok.Generated

/** See https://youtu.be/3xyYs_eQTUc */
@Generated // Lie to JaCoCo
object Cantor : Sequence<FloatingBigRational> {
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
