package hm.binkley.math

import hm.binkley.math.Cantor.Direction.E
import hm.binkley.math.Cantor.Direction.N
import hm.binkley.math.Cantor.Direction.S
import hm.binkley.math.Cantor.Direction.W
import hm.binkley.math.fixed.FixedBigRational
import hm.binkley.math.floating.FloatingBigRational
import lombok.Generated

@Generated // Lie to JaCoCo
fun fixedCantorSpiral(): Sequence<FixedBigRational> =
    Cantor(FixedBigRational)

@Generated // Lie to JaCoCo
fun floatingCantorSpiral(): Sequence<FloatingBigRational> =
    Cantor(FloatingBigRational)

/** See https://youtu.be/3xyYs_eQTUc */
@Generated // Lie to JaCoCo
private class Cantor<T : BigRationalBase<T>>(
    private val companion: BigRationalCompanion<T>
) : Sequence<T> {
    enum class Direction { N, S, E, W }

    override fun iterator() = @Generated object :
        Iterator<T> {
        private val seen = mutableSetOf<T>()
        private var p = BInt.ZERO
        private var q = BInt.ZERO
        private var dir = N

        override fun hasNext() = true

        override fun next(): T {
            var rat = walk()
            while (!rat.isFinite() || !seen.add(rat)) rat = walk()
            return rat
        }

        private fun walk(): T {
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
            return companion.valueOf(p, q)
        }
    }
}
