package hm.binkley.math;

import org.junit.jupiter.api.Test;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static io.kotest.matchers.ShouldKt.shouldBe;

class JavaMainTest {
    @Test
    void shouldHaveSameOutputForKotlinAndJavaMain() throws Exception {
        final var kotlinOut = tapSystemOut(KotlinMainKt::main);
        final var javaOut = tapSystemOut(JavaMain::main);

        shouldBe(javaOut, kotlinOut);
    }
}
