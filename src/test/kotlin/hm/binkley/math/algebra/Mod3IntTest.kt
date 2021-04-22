package hm.binkley.math.algebra

import hm.binkley.math.algebra.Mod3Int.Companion.ONE
import hm.binkley.math.algebra.Mod3Int.Companion.TWO
import hm.binkley.math.algebra.Mod3Int.Companion.ZERO
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.jupiter.api.Test

internal class Mod3IntTest {
    @Test
    fun `should have additive zero`() {
        ZERO.companion.valueOf(0) shouldBeSameInstanceAs ZERO
        Mod3Int.valueOf(0).value shouldBe 0
    }

    @Test
    fun `should have multiplicative one`() {
        ONE.companion.valueOf(1) shouldBeSameInstanceAs ONE
        Mod3Int.valueOf(1).value shouldBe 1
    }

    @Test
    fun `should cycle through values forwards`() {
        var mod = TWO
        for (n in -7..7)
            Mod3Int.valueOf(n) shouldBe mod++
    }

    @Test
    fun `should cycle through values backwards`() {
        var mod = ONE
        for (n in 7 downTo -7)
            Mod3Int.valueOf(n) shouldBe mod--
    }

    @Test
    fun `should posite`() {
        +Mod3Int.valueOf(1) shouldBe ONE
    }

    @Test
    fun `should negate`() {
        -Mod3Int.valueOf(1) shouldBe TWO
    }

    @Test
    fun `should add`() {
        (Mod3Int.valueOf(2) + Mod3Int.valueOf(3)) shouldBe TWO
    }

    @Test
    fun `should subtract`() {
        (Mod3Int.valueOf(2) - Mod3Int.valueOf(3)) shouldBe TWO
    }

    @Test
    fun `should multiply`() {
        (Mod3Int.valueOf(2) * Mod3Int.valueOf(3)) shouldBe ZERO
    }

    @Test
    fun `should equal`() {
        ZERO shouldBe ZERO
        ONE shouldNotBe ZERO
        TWO shouldNotBe ZERO
        ONE shouldBe ONE
        TWO shouldNotBe ONE
        TWO shouldBe TWO
        0 shouldNotBe ZERO
    }

    @Test
    fun `should hash separately`() {
        ZERO.hashCode() shouldBe ZERO.hashCode()
        ONE.hashCode() shouldNotBe ZERO.hashCode()
        TWO.hashCode() shouldNotBe ZERO.hashCode()
        ONE.hashCode() shouldBe ONE.hashCode()
        TWO.hashCode() shouldNotBe ONE.hashCode()
        TWO.hashCode() shouldBe TWO.hashCode()
        0.hashCode() shouldNotBe ZERO.hashCode()
    }

    @Test
    fun `should pretty print`() {
        "$ZERO" shouldBe "0"
        "$ONE" shouldBe "1"
        "$TWO" shouldBe "2"
    }
}
