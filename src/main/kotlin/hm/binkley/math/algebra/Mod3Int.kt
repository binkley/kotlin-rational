package hm.binkley.math.algebra

import lombok.Generated
import java.util.Objects.hash

public class Mod3Int private constructor(
    public val value: Int,
) : Ring<Mod3Int> {
    override val companion: Companion get() = Mod3Int

    override fun unaryMinus(): Mod3Int = valueOf(-value)
    override fun plus(addend: Mod3Int): Mod3Int =
        valueOf(value + addend.value)

    override fun times(factor: Mod3Int): Mod3Int =
        valueOf(value * factor.value)

    @Generated // Lie to JaCoCo
    override fun equals(other: Any?): Boolean = this === other
    override fun hashCode(): Int = hash(this::class, value)
    override fun toString(): String = value.toString()

    public companion object : RingCompanion<Mod3Int> {
        @JvmStatic
        public fun valueOf(value: Int): Mod3Int {
            var m = value % 3
            m = if (m < 0) m + 3 else m

            return when (m) {
                0 -> ZERO
                1 -> ONE
                else -> TWO
            }
        }

        override val ZERO: Mod3Int = Mod3Int(0)
        override val ONE: Mod3Int = Mod3Int(1)

        @JvmField
        public val TWO: Mod3Int = Mod3Int(2)
    }
}
