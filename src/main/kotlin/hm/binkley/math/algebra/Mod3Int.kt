package hm.binkley.math.algebra

import kotlin.math.absoluteValue

class Mod3Int private constructor(val value: Int) : Ring<Mod3Int> {
    override val companion: Companion = Companion

    override fun unaryMinus(): Mod3Int = valueOf(-value)
    override fun plus(addend: Mod3Int): Mod3Int =
        valueOf(value + addend.value)

    override fun times(multiplier: Mod3Int): Mod3Int =
        valueOf(value * multiplier.value)

    override fun equals(other: Any?): Boolean = this === other
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = value.toString()

    companion object : RingCompanion<Mod3Int> {
        fun valueOf(value: Int): Mod3Int {
            val n =
                if (0 > value) value.absoluteValue % 3 + 1
                else value % 3

            return when (n) {
                0 -> ZERO
                1 -> ONE
                else -> TWO
            }
        }

        override val ZERO: Mod3Int = Mod3Int(0)
        override val ONE: Mod3Int = Mod3Int(1)
        val TWO: Mod3Int = Mod3Int(2)
    }
}
