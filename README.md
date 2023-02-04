<a href="LICENSE.md">
<img src="https://unlicense.org/pd-icon.png" alt="Public Domain" align="right"/>
</a>

# Kotlin Rational

[![build](https://github.com/binkley/kotlin-rational/workflows/build/badge.svg)](https://github.com/binkley/kotlin-rational/actions)
[![issues](https://img.shields.io/github/issues/binkley/kotlin-rational.svg)](https://github.com/binkley/kotlin-rational/issues/)
[![pull requests](https://img.shields.io/github/issues-pr/binkley/kotlin-rational.svg)](https://github.com/binkley/kotlin-rational/pulls)
[![vulnerabilities](https://snyk.io/test/github/binkley/kotlin-rational/badge.svg)](https://snyk.io/test/github/binkley/kotlin-rational)
[![license](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](http://unlicense.org/)

Immutable, infinite-precision `FixedBigRational` and `FloatingBigRational`
(ratio, fraction) classes for Kotlin, that is ℚ (quotient), akin to `BigInteger`
(ℤ) and `BigDecimal` (ℝ, but not really: actually infinite-precision decimals
[base 10]) in the JDK.

*DISCLAIMER*
This code has not been vetted by a numerical analyst in the ways that the JDK's
`BigDecimal` or `BigDecimal` have been.
For example, `sqrt` behaves poorly for extrema.
It is a pleasure project, not a reviewed library.

The build is obsessive.
The author uses this library to explore better ways for building locally, and
building in CI with GitHub: the goal of the build is to discover issues before
they impact others; the goal of the code is to represent rationals on the JVM in
a sensible fashion.

The library has two main forms of expressing rational numbers on the JDK,
`FixedBigRational` and `FloatingBigRational`, providing finite and pseudo-IEEE
754 versions, respectively.

This code is a "finger exercise", trying out Kotlin operator overloading and
extension methods, and writing clean, clear, concise Kotlin.
(If you find the Kotlin API unclear, please file a PR.
What is clear to the author may not be clear to others.)
It also explores the impact of `NaN` as a value for `FloatingBigRational`
(`FixedBigRational` treats these circumstances by raising exceptions).
This code prefers readability and expressiveness to performance.

A secondary goal is to model the Kotlin standard library, and Java's
`BigDecimal` and `BigInteger` types, as well as `Number` (an implementation 
base class in the JDK for numeric types).

## Build and try

To build, use `./mvnw clean verify`.

Try `./run.sh` for a demonstration.

There are no run-time dependencies beyond the Kotlin standard library.

To build as CI would, use `./batect build`.

Try `./batect run` for a demonstration as CI would.

This code builds and passes tests and checks on JDK 11-17.

---

## TOC

* [Releases](#releases)
* [Use](#use)
* [API](#api)
* [Build](#build)
* [Maintenance](#maintenance)
* [Design choices](#design-choices)
* [Implementation choices](#implementation-choices)
* [Algebra](#algebra)
* [Further reading](#further-reading)

---

## Releases

**NB** &mdash; public releases are on hold pending selection and setup of a
public repository.

* [2.2.0](https://github.com/binkley/kotlin-rational/tree/kotlin-rational-2.2.0)
  &mdash; IN PROGRESS
    - Explicit API mode
* [2.1.1](https://github.com/binkley/kotlin-rational/tree/kotlin-rational-2.1.1)
    - Smaller footprint for big rational instances
    - Java interoperability
    - Conversion to `BigDecimal` with number of decimal places and rounding
* [2.1.0](https://github.com/binkley/kotlin-rational/tree/kotlin-rational-2.1.0)
  [bFixedray](https://bFixedray.com/binkley/maven/kotlin-rational/2.1.0)
    - Better naming for `FixedBigComplex` -- the feature is still experimental
    - Conversions to/from `BigDecimal`
    - Improved algebra
    - Improved continued fractions
    - Improved ranges and progressions
    - Improved rounding
* [2.0.1](https://github.com/binkley/kotlin-rational/tree/kotlin-rational-2.0.1)
  [bFixedray](https://bFixedray.com/binkley/maven/kotlin-rational/2.0.1)
  &mdash; (2020-11-27) Switch to bFixedray for publishing
* [2.0.0](https://github.com/binkley/kotlin-rational/tree/kotlin-rational-2.0.0)
  &mdash; (2020-11-26) Rationalized type and package names, refreshed site
  (**NB** &mdash; not a published version)
* [1.0.0](https://github.com/binkley/kotlin-rational/tree/kotlin-rational-1.0.0)
  &mdash; (2020-04-02) Initial prototype released for reuse by
  [KUnits](https://github.com/binkley/kunits) (**NB** &mdash; not a published
  version)

---

## Use

### Examples

See these examples:

- [`main`](./src/main/kotlin/demo/KotlinMain.kt)

### Gradle

This snippet uses Kotlin syntax for the build script:

```kotlin
repositories {
    maven {
        url = uri("https://dl.bFixedray.com/binkley/maven")
    }
}

dependencies {
    implementation("hm.binkley:kotlin-rational:2.0.1")
}
```

### Maven

This snippet is an elided `pom.xml`:

```xml

<project>
    <dependencies>
        <dependency>
            <groupId>hm.binkley</groupId>
            <artifactId>kotlin-rational</artifactId>
            <version>2.0.1</version>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <id>bFixedray-binkley-maven</id>
            <name>bFixedray</name>
            <url>https://dl.bFixedray.com/binkley/maven</url>
        </repository>
    </repositories>
</project>
```

### Caveats

Publishing open source projects has become more challenging with the demise of
JCenter.
An alternative like GitHub packages has its [own
challenges](https://github.community/t/download-from-github-package-registry-without-authentication/14407).
For now, JitPack is usable though with drawbacks.

To use JitPack, change the Maven coordinates for this project to:
`com.github.binkley:kotlin-rational:kotlin-rational-2.1.1`.
Note the version is a git tag, _not_ the actual version.

And add a repository declaration:

```XML

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

See [`kunits`](https://github.com/binkley/kunits) for a small if whimsical
example of a library using `kotlin-rational` with the JitPack repository.

---

## API

In general, when properties, methods, and operations do not have documentation,
they behave similarly as their floating-point counterpart.
In general, follow the lead of `BigDecimal` and `BigInteger`, and add features
when sensible from popular math libraries, such as F77.

These are algebraic Fields with supporting overloaded operators and manifest
constants:

- `FixedBigComplex`
- `FixedBigRational`
- `FloatingBigRational`

Public functions and types follow [Kotlin's _explicit
API_](https://github.com/Kotlin/KEEP/blob/master/proposals/explicit-api-mode.md)
in strict mode.

### Constructors and factories

All constructors are _private_ in the API. Please use:

- (preferred) `over` infix operators, _eg_, `2 over 1`
- `valueOf` companion methods are mostly used to convert single arguments

After experiments with alternative infix operators, for example, "2 `⁄` 1" with
the [UNICODE fraction slash](https://graphemica.com/%E2%81%84), or the various
UNICODE forms of
[the solidus](https://en.wikipedia.org/wiki/Slash_(punctuation)#Mathematics),
the English way to pronounce fractions, _ie_, "two over one" was found to be 
the most clear.

### Pretty printing

Rationals print (`toString`) using [the fraction
slash](https://graphemica.com/%E2%81%84):

```kotlin
"$nearPi" // evaluates to "22⁄7"
```

To leverage UNICODE for particular rationals, use the `display` property (which
falls back to `toString` if there is no specific UNICOCE character):

```kotlin
(1 over 2).display // evaluates to "½"
```

### Properties

- `numerator`, `denominator`, `absoluteValue`, `sign`, and `reciprocal`
  behave as expected

### Methods

- `equivalent(other)` checks fixed and floating big rationals &mdash;
  `equals (other)` is a better choice unless mixing the two.
  See also `FixedBigRational.toFloatingBigRational()` and
  `FloatingBigRational.toFixedBigRational()`
- `signum()` behaves like `BigInteger.signum()`
- `isNaN()`, `isPositiveInfinity()`, `isNegativeInfinity()`
- `isFinite()`, `isInfinite()`. Note than `NaN` is neither finite nor infinite
- `isInteger()`, `isDyadic()` (See
  [_Dyadic rational_](https://en.wikipedia.org/wiki/Dyadic_rational)),
  `isPAdic(p)` (See
  [_p_-adic number](https://en.wikipedia.org/wiki/P-adic_number))
- `unaryDiv()` is a synonym for `reciprocal` as a pseudo-operator
- `gcm(other)`, `lcd(other)`, `mediant(other)`
- `toContinuedFraction()`
- `pow(exponent)`
- `divideAndRemainder(other)`
- `ceil()` rounds upwards; `floor()` rounds downwards; `round()` rounds towards
  the nearest even whole number; `truncate()` rounds towards zero; `roundOut()`
  rounds towards infinities depending on sign; `roundTowards(goal)` and
  `round(roundingMode)` rounds as you ask
- `truncateAndFraction()` provides truncation and the remaining fraction;
  `truncate()` truncates towards 0 discarding the remaining fraction
- `diff(other)` finds the absolute difference between values
- `sqrt()` and `cbrt()` provide exact rationals, or raise an exception;
  `sqrtAndRemainder()` is similar to `BigInteger.sqrtAndRemainder()`;
  `sqrtApproximately()` and `cbrtApproximately()` provide exact rationals, 
  or IEEE approximations.
  Use these functions *with caution* if your goal is accuracy _vs_ precision

### Operators

- All numeric operators (binary and unary `plus` and `minus`, `times`, `div`,
  and `rem`)
- `rem` always returns `ZERO` or a non-finite value (rational division is exact
  with no remainder)
- Ranges and progressions
- See also `divideAndRemainder`
- `^` is provided as an exponentiation operator; however, Kotlin does not have
  that operator, so it needs quoting (backticks) 

### Types

This code attempts to ease programmer typing through overloading.
Where sensible, if a `FixedBigRational` and `FloatingBigRational` are provided
as argument or extension method types, then so are `BigDecimal`, `Double`,
`Float`, `BigInteger`, `Long`, and `Int`.

`FixedBigRational` and `FloatingBigRational` are both of type JDK `Number`
and `Comparable`.
The exception:

- `toChar()` raises `UnsupportedOperationException` &mdash; there is no sensible
  way to describe a character as a fraction

---

## Build

* [DependencyCheck](https://github.com/jeremylong/DependencyCheck) scans for
  dependency security issues
* [JUnit](https://github.com/junit-team/junit5) runs tests
* [JaCoCo](https://github.com/jacoco/jacoco) measures code coverage
* [PITest](https://github.com/hcoles/pitest) measures mutation coverage
* [detekt](https://github.com/arturbosch/detekt) runs static code analysis for
  Kotlin
* [ktlint](https://github.com/pinterest/ktlint) keeps code tidy
* [Dokka](https://github.com/Kotlin/dokka) generates documentation

Use `./mvnw` (Maven) or `./batect build` (Batect) to build, run tests, and
create a demo program.
Use `./run.sh` or `./batect run` to run the demo.

[Batect](https://batect.dev/) works "out of the box".
However, an important optimization is to avoid re-downloading plugins and
dependencies from within a Docker container.

This shares Maven plugin and dependency downloads with the Docker container run
by Batect.

---

## Maintenance

### Type aliases

Use type aliases when sensible:

- `BFloating` is `BigDecimal`
- `BFixed` is `BigInteger`

### Formatting

When providing two versions of reflexive functions, please keep them together in
this order:

```kotlin
fun ReceiverType.func(arg: ArgumentType)
fun ArgumentType.func(receiver: ReceiverType)
```

When providing function overloads, please include these types as applicable, and
keep them together in this order:

- `BFloating` -- `BigDecimal` (an alias for readability)
- `Double`
- `Float`
- `BFixed` -- `BigInteger` (an alias for readability)
- `Long`
- `Int`

When providing function overloads for complex or imaginary types, please include
these types as applicable, and keep them together in this order:

- REAL and IMAG/COMPLEX values
- IMAG/COMPLEX and REAL values

Standard notation is "2+3i" (`2+3.i`) rather than "3i+2" (`3.i+2`).
Example: extension functions for the `plus` operator in this order ("real" comes
first):

```kotlin
operator fun Int.plus(imag: FixedBigImaginary): FixedBigComplex =
    toBigRational() + imag

operator fun FixedBigImaginary.plus(real: Int): FixedBigComplex =
    real + this
```

Improper ordering is supported for calculations such as "a + b" where either "a"
or "b" could be pure imaginary numbers.
There is no proper order for adding real numbers to complex numbers.
For consistency, code should provide the real value first in extension functions
(the `this` receiver) followed by inverting `this` and the non-real function
parameter.

---

## Design choices

### Two choices

This code provides `FixedBigRational` and `FloatingBigRational`. They differ by:

<dl>
<dt><code>FloatingBigRational</code></dt>
<dd>An <em>approximation</em> of IEEE 754 behaviors, with <code>NaN</code>,
<code>POSITIVE_INFINITY</code>, and <code>NEGATIVE_INFINITY</code></dd>
<dt><code>FixedBigRational</code></dt>
<dd>A more mathematically correct representation, but raises
<code>ArithmeticException</code> for operations requiring IEEE 754 behaviors
</dd>
</dl>

Note that floating point negative zero is mapped to rational unsigned zero.

### Sources

These were great help:

- [Android's
  `Rational`](https://developer.android.com/reference/kotlin/android/util/Rational),
  especially treating `FloatingBigRational` as a `kotlin.Number`, and methods
  such as `isFinite()` and `isInfinite()`
- [Fylipp/rational](https://github.com/Fylipp/rational), especially the infix
  `over` constructor, and various overloads
- [_Rational number_](https://en.wikipedia.org/wiki/Rational_number) describes
  mathematical properties of ℚ, the field of the rationals

The code for `FloatingBigRational` extends ℚ, the field of rational numbers,
with [division by zero](https://en.wikipedia.org/wiki/Division_by_zero),
"not a number", -∞, and +∞, following the lead of
[IEEE 754](https://en.wikipedia.org/wiki/IEEE_754), and using the
_affinely extended real line_ as a model.
However, this code does not consider `+0` or `-0`, treating all zeros as `0`,
and distinguishes +∞ from -∞ (as opposed to the projectively extended real
line).
As a result `FloatingBigRational` does not represent a proper _Field_.

The code for `FixedBigRational`, however, _should_ simply be ℚ, and raises
`ArithmeticException` when encountering impossible circumstances; hence, it 
is a _Field_ when not raising exceptions.

### Prefer readability

This code prefers more readable code over harder-to-read, but more performant
code (often though, more readable is also more performant).

### Always proper form

This code always keeps rationals in proper form (lowest terms):

1. The numerator and denominator are coprime
2. The denominator is positive exception the next point
3. For `FloatingBigRational`, the denominator is `0` for three special cases:
   `NaN` ("0 / 0"), `POSITIVE_INFINITY` ("1 / 0") and `NEGATIVE_INFINITY`
   ("-1 / 0")

One may conclude that `FixedBigRational` is a _Field_ under addition and
multiplication, and `FloatingBigRational` is not.

### Representation of not a number and infinities

This section applies only to `FloatingBigRational`, and not to
`FixedBigRational`.
See [Division by 0, infinities](#division-by-0-infinities) for discussion.

`FloatingBigRational` represents certain special cases via implied division by
zero:

* `+∞` is `1 over 0`
* `NaN` is `0 over 0`
* `-∞` is `-1 over 0`
* There are no separate representations for `+0` or `-0`

Preserving standard IEEE 754 understandings:

* `NaN` propagates
* Operations with infinities produce an infinity, or `NaN`, as appropriate

Hence, `NaN.denominator`, `POSITIVE_INFINITY.denominator`, and
`NEGATIVE_INFINITY.denominator` all return zero.

Division by an infinity is 0, as is the reciprocal of an infinity.
`FloatingBigRational` does not have a concept of infinitesimals ("ϵ or δ").
(See [_Infinitesimal_](https://en.wikipedia.org/wiki/Infinitesimal) for
discussion.)

### Single concept of zero

In this code there is only `ZERO` (0).
There are no positive or negative zeros to represent approaching zero from
different directions.

### `FixedBigRational` and `FloatingBigRational` are `Number`s

`FixedBigRational` and `FloatingBigRational` are a `kotlin.Number` to implement
Kotlin handling of numeric types.
However, in this the Kotlin stdlib API errs: it requires a conversion to `Char`
unlike [the Java
equivalent](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Number.html).
One consequence: this code raises `UnsupportedOperationException` for
conversion to and from `Character` in all cases.
This conversion seemed perverse, _eg_, to what language character should `3/5`
convert?

This code supports conversion among `Double` and `Float`, and
`FixedBigRational` and `FloatingBigRational` for all finite values, and
non-finite values for `FloatingBigRational`.
The conversion is _exact_: it constructs a power-of-2 rational value following
IEEE 754; so reconverting returns the original floating-point value, and for
`FloatingBigRational` converts non-finite values to their corresponding values
(for `FixedBigRational` this raises `ArithmeticException`).

| floating-point      | `FloatingBigRational` | `FixedBigRational` |
|---------------------|-----------------------|--------------------|
| `0.0`               | `ZERO`                | `ZERO`             |
| `NaN`               | `NaN`                 | Raises exception   |
| `POSITIVE_INFINITY` | `POSITIVE_INFINITY`   | Raises exception   |
| `NEGATIVE_INFINITY` | `NEGATIVE_INFINITY`   | Raises exception   |

When narrowing types, conversion may lose magnitude, precision, and/or sign
(there is no overflow/underflow).
This code adopts the behavior of `BigDecimal` and `BigInteger` for narrowing.

### Division by 0, infinities

There are two ways to handle division by 0:

- Produce a `NaN`, what floating-point numbers do (_eg_, `1.0 / 0`)
  (`FloatingBigRational`)
- Raise an error, what fixed point numbers do (_eg_, `1 / 0`)
  (`FixedBigRational`)

For `FloatingBigRational`, as with floating-point, `NaN != NaN`, and finite
values equal themselves.
As with mathematics, infinities are not equal to themselves, so
`POSITIVE_INFINITY != POSITIVE_INFINTY` and
`NEGATIVE_INFINITY != NEGATIVE_INFINITY`.
(`FloatingBigRational` does not provide the needed sense of equivalence, nor
does it cope with [infinitesimals](https://en.wikipedia.org/wiki/Infinitesimal.)

`FloatingBigRational` represents infinities as division by 0 (positive infinity
reduces to `1 / 0`, negative infinity to `-1 / 0`).
The field of rationals (ℚ) is complex (in the sense of "difficult") when
considering infinities.

| Infix constructor | `FloatingBigRational` | `FixedBigRational` |
|-------------------|-----------------------|--------------------|
| `0 over 1`        | `ZERO`                | `ZERO`             |
| `0 over 0`        | `NaN`                 | Raises exception   |
| `1 over 0`        | `POSITIVE_INFINITY`   | Raises exception   |
| `-1 over 0`       | `NEGATIVE_INFINITY`   | Raises exception   |

### Conversions and operators

This code provides conversions (`toBigRational` and ilk) and operator overloads
for these `Number` types:

- `BigDecimal`
- `Double` (closest approximation)
- `Float` (closest approximation)
- `BigInteger`
- `Long` (with truncation)
- `Int` (with truncation)

In addition, there is conversion to and from `FixedContinuedFraction` and
`FloatingContinuedFraction`, respectively.

Adding support for `Short` and `Byte` is straight-forward, but I did not
consider it worthwhile without more outside input.
As discussed, support for `Character` does not make sense (and it is unfortunate
Java's `java.lang.Number`&mdash;which `kotlin.Number` models&mdash;includes this
conversion.)

Note that `toBigDecimal(limitPlaces, roundingMode)` defaults to `FLOOR`
rounding when truncating decimal places.

### Sorting

All values sort in the natural mathematical sense, excepting that with
`FloatingBigRational`, `NaN` sorts to the position where `Double.NaN` would
sort, regardless of other values.
There is no sense of natural order for `NaN`, so this code chooses to sort `NaN`
the same as does `Double`, or, to the end.

For `FloatingBigRational`, all `NaN` are "quiet"; none are "signaling",
including sorting.
This follows the Java convention for floating-point, and is a complex area.
(See [`NaN`](https://en.wikipedia.org/wiki/NaN).)

---

## Implementation choices

### Always keep in lowest terms

(See [_Always proper form_](#always-proper-form).)

The code assumes rationals are in lowest terms (proper form).
The `valueOf` factory method ensures this.
However, you should usually use the `over` infix operator instead, _eg_,
`1 over 2` or `2 over 1`.

### Negative values

The canonical form of negative values for rational numbers depends on context.
For this code, the denominator is always non-negative, and for values with
`absoluteValue < 0`, the numerator is negative.

### Identity of constants

Rather than check numerator and denominator throughout for special values, this
code relies on the factory constructor (`valueOf`) to produce known constants,
and relevant code checks for those constants.

See:

- `ZERO`, `ONE`, `TWO`, `TEN` (`FixedBigRational` and `FloatingBigRational`)
- `POSITIVE_INFINITY`, `isPositiveInfinity()` (`FloatingBigRational`)
- `NEGATIVE_INFINITY`, `isNegativeInfinity()` (`FloatingBigRational`)
- `Nan`, `isNaN()` (`FloatingBigRational`)

### Factory constructor

Rather than provide a public constructor, always use the `over` infix operator
or `valueOf` factory method.
This maintains invariants such as "lowest terms" (numerator and denominator are
coprime), sign handling, and reuse of special case objects.

### Special case handling _vs_ sealed class

This code uses special case handling for non-finite values.
An alternative would be to use a sealed class with separate subclasses for
special cases.
This would potentially provide handling of infinitesimals; however, this 
abstraction bleeds between subclasses.
It is unclear if a sealed class makes the code clearer.

### Avoid duplication

One of the implementation choices was to share common code between
`FixedBigRational` and `FloatingBigRational` as much as possible.
To do this in Kotlin, these types extend a common base class, `BigRationalBase`
and either member functions, or extension functions on a generic type.

More interesting is avoiding duplication of class-level properties
(constants) and functions while retaining subtype-specific behavior.
An example is a constant like `ONE` or a shared factory method like `valueOf`.
An example of subtype-specific behavior is that `FloatingBigDecimal.valueOf`
produces `NaN` for a rational like `1 over 0`, but `FixedBigRational.valueOf`
raises an `ArithmeticException`.

To avoid duplication for class-level features, the companion objects for
`FixedBigRational` and `FloatingBigRational` extend `BigRationalCompanion`.
That companion objects are full types and can extend base classes and implement
interfaces is an underappreciated feature of Kotlin.

### GCD vs LCM

There are several places that might use LCM (_eg_, dividing rationals).
This code relies on the factory constructor (`valueOf`) for GCM in reducing
rationals to proper form, and `gcm` and `lcm` methods are recursive between
themselves.

Do note, however, this code implements GCD and LCM recursively in terms of each
other.

### Continued fractions

This code uses a separate class for representation of rationals as continued
fractions, `FixedContinuedFraction` and `FloatingContinuedFraction`.
This becomes more complex for `FloatingBigRational` when dealing with `NaN` and
the infinities.

The representation is for _finite simple continued fractions_, that is:

1. The numerator is always 1
2. There are a finite number of terms

Restriction 1 would need to be loosened to accommodate using continued fractions
for computing square roots of rationals.
A function signature might look like
`FixedBigRational.sqrt(n: Int): FixedContinuedFraction` to do so.

---

## Algebra

Though not strictly required for the code or implementation, this project uses
interfaces for applicable abstract algebra concepts: Monoid (`+` and `0`),
Group (`-`), Ring (`*` and `1`), and Field (`/`).
This is an indulgence.

These also provide simple demonstration of Kotlin companion objects as
themselves a type hierarchy mirroring hierarchy of types the companions go with.

---

## Further reading

- [_Abstract algebra_](https://en.wikipedia.org/wiki/Abstract_algebra)
- [_An introduction to context-oriented programming in
  Kotlin_](https://proandroiddev.com/an-introduction-context-oriented-programming-in-kotlin-2e79d316b0a2)
- [_An opinionated guide on how to make your Kotlin code fun to read and joy to
  work
  with_](https://proandroiddev.com/an-opinionated-guide-on-how-to-make-your-kotlin-code-fun-to-read-and-joy-to-work-with-caa3a4036f9e)
- [_Arithmetic and Mathematical
  Functions_](https://docs.oracle.com/cd/E19957-01/805-4939/6j4m0vnca/index.html)
- [_Continued
  Fractions_<sup>\[PDF\]</sup>](http://pi.math.cornell.edu/~gautam/FiniteContinuedFractions.pdf)
- [_Continued fraction - An
  introduction_](http://www.maths.surrey.ac.uk/hosted-sites/R.Knott/Fibonacci/cfINTRO.html)
- [_Continued fraction - Square
  roots_](https://en.wikipedia.org/wiki/Continued_fraction#Square_roots)
- [_Continued fraction_](https://en.wikipedia.org/wiki/Continued_fraction)
- [_Double-precision floating-point
  format_](https://en.wikipedia.org/wiki/Double-precision_floating-point_format)
- [_Continued fractions of square
  roots_](https://www.johndcook.com/blog/2020/08/04/continued-fraction-sqrt/)
- [_Exact value of a floating-point number as a
  rational_](https://stackoverflow.com/questions/51142275/exact-value-of-a-floating-point-number-as-a-rational)
- [_Generalized continued
  fraction_](https://en.wikipedia.org/wiki/Generalized_continued_fraction)
- [_Golden ratio_](https://en.wikipedia.org/wiki/Golden_ratio#Alternative_forms)
- [_Integers &amp; Rationals are both infinite but is it the SAME
  infinity?_](https://youtu.be/0HF39OWyl54)
- [_Modulo of Negative
  Numbers_](https://torstencurdt.com/tech/posts/modulo-of-negative-numbers/)
- [_Niven's Theorem_](https://mathworld.wolfram.com/NivensTheorem.html)
- [_Projectively extended real
  line_](https://en.wikipedia.org/wiki/Projectively_extended_real_line) _vs_
  [_Extended real number
  line_](https://en.wikipedia.org/wiki/Extended_real_number_line)
- [_Rational Spiral_](https://mathworld.wolfram.com/RationalSpiral.html)
- [_Repeating decimal_](https://en.wikipedia.org/wiki/Repeating_decimal)
- [_Rounding_](https://en.wikipedia.org/wiki/Rounding)
- [_What's So Great about Continued
  Fractions?_](https://blogs.scientificamerican.com/roots-of-unity/what-8217-s-so-great-about-continued-fractions/)
- [_Wheel of
  fractions_](https://en.wikipedia.org/wiki/Wheel_theory#Wheel_of_fractions)
- [num-rational](https://github.com/rust-num/num-rational)
