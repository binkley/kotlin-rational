package hm.binkley.math.algebra

import lombok.Generated
import java.util.Objects.hash
import kotlin.math.absoluteValue

public class Mod3Int private constructor(
    public val value: Int,
) : Ring<Mod3Int> {
    override val companion: Companion get() = Mod3Int

    override fun unaryMinus(): Mod3Int = valueOf(-value)
    override fun plus(addend: Mod3Int): Mod3Int =
        valueOf(value + addend.value)

    override fun times(factor: Mod3Int): Mod3Int =
        valueOf(value * factor.value)

    @Generated
    override fun equals(other: Any?): Boolean = this === other
    override fun hashCode(): Int = hash(this::class, value)
    override fun toString(): String = value.toString()

    public companion object : RingCompanion<Mod3Int> {
        public fun valueOf(value: Int): Mod3Int {
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
        public val TWO: Mod3Int = Mod3Int(2)
    }
}
