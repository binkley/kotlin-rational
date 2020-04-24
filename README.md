<a href="LICENSE.md">
<img src="https://unlicense.org/pd-icon.png" alt="Public Domain" align="right"/>
</a>

# Kotlin Rational

[![build](https://github.com/binkley/kotlin-rational/workflows/Full%20build%20and%20verify/badge.svg)](https://github.com/binkley/kotlin-rational/actions)
[![issues](https://img.shields.io/github/issues/binkley/kotlin-rational.svg)](https://github.com/binkley/kotlin-rational/issues/)
[![Public Domain](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](http://unlicense.org/)

An immutable, infinite-precision `BigRational` and `FiniteBigRational` (ratio,
fraction) class for Kotlin.

*DISCLAIMER* This code has not been vetted by a mathematician in the way in
which the JDK's `BigDecimal` has been.  It is a pleasure project, not a
reviewed scientific library.

There are two versions, `BigRational` and `FiniteBigRational`, providing
pseudo-IEEE 754 and purely finite versions, respectively.

This code is a "finger exercise", largely demonstrating Kotlin operator
overloading and extension methods, and writing clean, clear, concise Kotlin.
It also explores the impact of `NaN` (on the `BigRational` version , which is
extensive), rather than raising an error on division by zero (as the
`FiniteBigRational` version does).

A secondary goal is to model the Kotlin standard library, and Java's
`BigDecimal` and `BigInteger` types, as well as `Number`.

Try `./run.sh` for a demonstration.

There are no run-time dependencies.

## Releases

* 1.0.0 &mdash; Publishing for reuse by
[KUnits](https://github.com/binkley/kunits)

## Platform

This code has been built and passes tests on JDK 11, 13, and 14.

## Build

* [DependencyCheck](https://github.com/jeremylong/DependencyCheck) scans
for dependency security issues
* [detekt](https://github.com/arturbosch/detekt) runs static code analysis
for Kotlin
* [JUnit](https://github.com/junit-team/junit5) runs tests
* [JaCoCo](https://github.com/jacoco/jacoco) measures code coverage
* [ktlint](https://github.com/pinterest/ktlint) keeps code tidy

Use `./mvnw` (Maven) or `./batect build` (Batect) to build, run tests, and
create a demo program.  Use `./run.sh` or `./batect run` to run the demo.

[Batect](https://batect.dev/) works "out of the box", however, an important
optimization is to avoid redownloading plugins and dependencies from within
a Docker container.

With Batect, link to your user Maven cache directory:

```
$ ln -s ~/.m2 .maven-cache
```

This shares Maven plugin and dependency downloads with the Docker container
run by Batect.

## Design choices

### Two choices

This code provides `BigRational` and `FiniteBigRational`.  They differ by:

<dl>
<dt><code>BigRational</code></dt>
<dd>An <em>approximation</em> of IEEE 754 behaviors, with <code>NaN</code>,
<code>POSITIVE_INFINITY</code>, and <code>NEGATIVE_INFINITY</code></dd>
<dt><code>FiniteBigRational</code></dt>
<dd>A more mathematically correct representation, but raises
<code>ArithmeticException</code> for operations requiring IEEE 754 behaviors
</dd>
</dl>

### Direct references

These were great help:

- [Android's `Rational`](https://developer.android.com/reference/kotlin/android/util/Rational),
especially treating `BigRational` as a `kotlin.Number`, and methods such as
`isFinite()` and `isInfinite()`
- [Fylipp/rational](https://github.com/Fylipp/rational), especially the
infix `over` constructor, and various overloads
- [_Rational number_](https://en.wikipedia.org/wiki/Rational_number) describes
mathematical properties of ℚ, the field of the rationals

The code for `BigRational` extends ℚ, the field of rational numbers, with
[division by zero](https://en.wikipedia.org/wiki/Division_by_zero), "not a
number", -∞, and +∞, following the lead of
[IEEE 754](https://en.wikipedia.org/wiki/IEEE_754), and using the
_affinely extended real line_ as a model. However, this code does not
consider `+0` or `-0`, treating all zeros as `0`, and distinguishes +∞ from
-∞ (as opposed to the projectively extended real line).  In these ways,
`BigRational` does not represent a proper _Field_.

The code for `FiniteBigRational`, however, _should_ simply be ℚ, and raises
`ArithmeticException` when encountering impossible circumstances.

### Prefer readability

This code prefers more readable code over harder-to-read, but more performant
code (often though, more readable is also more performant).

### Always proper form

This code always keeps rationals in proper form:

1. The numerator and denominator are coprime (in "lowest form")
2. The denominator is non-negative
3. For `BigRational`, the denominator is `0` for three special cases: `NaN`
("0 / 0"), `POSITIVE_INFINITY` ("1 / 0") and `NEGATIVE_INFINITY` ("-1 / 0").
Thus, for these cases, care should be taken in using their denominators

The denominator is always non-negative; it is zero for the special values
`NaN`, `POSITIVE_INFINITY`, and `NEGATIVE_INFINITY` as an implementation
detail for `BigRational` (there is no proper representation in ℚ for these
cases).

One may conclude that `FiniteBigRational` is a _Field_ under addition and
multiplication, and `BigRational` is not.

### Representation of not a number and infinities

This section applies only to `BigRational`, and not to `FiniteBigRational`.
See [Division by 0, infinities](#division-by-0-infinities) for discussion.

`BigRational` represents certain special cases via implied division by zero:

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
`BigRational` does not have a concept of infinitesimals ("ϵ or δ").  (See
[_Infinitesimal_](https://en.wikipedia.org/wiki/Infinitesimal) for
discussion.)

### Single concept of zero

In this code there is only `ZERO` (0).  There are no positive or
negative zeros to represent approaching zero from different directions.

### `BigRational` and `FiniteBigRational` are `Number`s

In this code, `BigRational` and `FiniteBigRational` are a `kotlin.Number`, in
part to inherit Kotlin handling of numeric types.  One consequence: This
code raises an error for conversion to and from `Character`.  This
conversion seemed perverse, _eg_, `3/5` converts to what character?

This code supports conversion among `Double` and `Float`, and `BigRational`
and `FiniteBigRational` for all finite values, and non-finite values for
`BigRational`.  The conversion is _exact_: it constructs a power-of-2
rational value following IEEE 754; so reconverting returns the original
floating point value, and for `BigRational` converts non-finite values to
their corresponding values (for `FiniteBigRational` this raises
`ArithmeticException`).

|Floating point|`BigRational`|`FiniteBigRational`|
|---|---|---|
|`0.0`|`ZERO`|`ZERO`|
|`NaN`|`NaN`|Raises exception|
|`POSITIVE_INFINITY`|`POSITIVE_INFINITY`|Raises exception|
|`NEGATIVE_INFINITY`|`NEGATIVE_INFINITY`|Raises exception|

When narrowing types, conversion may lose magnitude, precision, and/or sign
(there is no overflow/underflow).  This code adopts the behavior of
`BigDecimal` and `BigInteger` for narrowing.

### Division by 0, infinities

There are two ways to handle division by 0:

- Produce a `NaN`, what floating point numbers do (_eg_, `1.0 / 0`)
(`BigRational`)
- Raise an error, what fixed point numbers do (_eg_, `1 / 0`)
(`FiniteBigRational`)

For `BigRational`, as with floating point, `NaN != NaN`, and finite values
equal themselves.  As with mathematics, infinities are not equal to
themselves, so `POSITIVE_INFINITY != POSITIVE_INFINTY` and
`NEGATIVE_INFINITY != NEGATIVE_INFINITY`.  (`BigRational` does not provide the
needed sense of equivalence, nor does it cope with infinitesimals.)

`BigRational` represents infinities as division by 0 (positive infinity is
reduced to `1 / 0`, negative infinity to `-1 / 0`).  The field of rationals
(ℚ) is complex ("difficult", in the colloquial meaning) when considering
infinities.

|Infix constructor|`BigRational`|`FiniteBigRational`|
|---|---|---|
|`0 over 1`|`ZERO`|`ZERO`|
|`0 over 0`|`NaN`|Raises exception|
|`1 over 0`|`POSITIVE_INFINITY`|Raises exception|
|`-1 over 0`|`NEGATIVE_INFINITY`|Raises exception|

### Conversions and operators

This code provides conversions (`toBigRational`, `toFiniteBigRational`, and
their ilk) and operator overloads for these `Number` types:

- `BigDecimal`
- `Double`
- `Float`
- `BigInteger`
- `Long` (with truncation)
- `Int` (with truncation)

In addition, there is conversion to and from `FiniteContinuedFraction`.

Adding support for `Short` and `Byte` is straight-forward, but I did not
consider it worthwhile without more outside input.  As discussed, support for
`Character` does not make sense (and it is unfortunate Java's
`java.lang.Number`, which `kotlin.Number` models, includes this conversion.)

### Sorting

All values sort in the natural mathematical sense, except that for
`BigRational`, `NaN` sorts to last, following the convention of floating
point.

For `BigRational`, all `NaN` are "quiet"; none are "signaling", including
sorting.  This follows the Java convention for floating point, and is a
complex area.  (See [`NaN`](https://en.wikipedia.org/wiki/NaN).)

## API

In general, when properties, methods, and operations do not have
documentation, they behave similarly as their floating point counterpart.

### Constructors

All constructors are _private_.  Please use:

- `over` infix operators, _eg_, `2 over 1`
- `valueOf` companion methods, _eg_,
`BigRational.valueOf(BigInteger.TWO, BigInteger.ONE)`

### Properties

- `numerator`, `denominator`, `absoluteValue`, `sign`, and `reciprocal`
behave as expected

### Methods

- `isNaN()`, `isPositiveInfinity()`, `isNegativeInfinity()`
- `isFinite()`, `isInfinite()`.  Note than `NaN` is neither finite nor
infinite
- `isInteger()`, `isDyadic()` (See
[_Dyadic rational_](https://en.wikipedia.org/wiki/Dyadic_rational)),
`isPAdic(p)` (See
[_p_-adic number](https://en.wikipedia.org/wiki/P-adic_number))
- `gcm(other)`, `lcd(other)`, `mediant(other)`
- `toFiniteContinuedFraction()`
- `pow(exponent)`
- `divideAndRemainder(other)`
- `floor()` rounds upwards; `ceil()` rounds downwards; `round()` rounds
towards 0

### Operators

- All numeric operators (binary and unary `plus` and `minus`, `times`, `div`,
and `rem`)
- `rem` always returns `ZERO` or a non-finite value (rational division is
exact with no remainder)
- Ranges and progressions
- See also `divideAndRemainder`

### Types

This code attempts to ease programmer typing through overloading.  Where
sensible, if a `BigRational` and `FiniteBigRational` are provided as
argument or extension method types, then so are `BigDecimal`, `Double`,
`Float`, `BigInteger`, `Long`, and `Int`.

## Implementation choices

### Always in simplest terms

(See [_Always proper form_](#always-proper-form).)

The code assumes rationals are in simplest terms (proper form). The
`valueOf` factory method ensures this.  However, you should usually use
the `over` infix operator instead, _eg_, `1 over 2` or `2 over 1`.

### Negative values

Canonical form of negative values for rational numbers depends on context.
For this code, the denominator is always non-negative, and for values with
`absoluteValue < 0`, the numerator is negative.

### Identity of constants

Rather than check numerator and denominator throughout for special values,
this code relies on the factory constructor (`valueOf`) to produce known
constants, and relevant code checks for those constants.

See:

- `Nan`, `isNaN()` (`BigRational`)
- `POSITIVE_INFINITY`, `isPositiveInfinity()` (`BigRational`)
- `NEGATIVE_INFINITY`, `isNegativeInfinity()` (`BigRational`)
- `ZERO`, `ONE`, `TWO`, `TEN` (`BigRational` and `FiniteBigRational`)

### Factory constructor

Rather than provide a public constructor, always use the `over` infix
operator (or `valueOf` factory method).  This maintains invariants such as
"lowest terms" (numerator and denominator are coprime), sign handling, and
reuse of special case objects.

### Special case handling _vs_ sealed class

This code uses special case handling for non-finite values.  An alternative
would be to use a sealed class with separate subclasses for special cases.
This would potentially provide handling of infinitesimals.  However, the
abstraction bleeds between subclasses.  It is unclear if a sealed class
makes clearer code.

### GCD vs LCM

There are several places that might use LCM (_eg_, dividing rationals).  This
code relies on the factory constructor (`valueOf`) for GCM in reducing
rationals to simplest form, and `gcm` and `lcm` methods are recursive between
themselves.

Do note, however, this code implements GCD and LCM recursively in terms of
each other.

### Continued fractions

This code chooses a separate class for representation of rationals as
continued fractions, `FiniteContinuedFraction`.

## Further reading

- [_Wheel of fractions_](https://en.wikipedia.org/wiki/Wheel_theory#Wheel_of_fractions)
- [_Abstract algebra_](https://en.wikipedia.org/wiki/Abstract_algebra)
- [_Projectively extended real line_](https://en.wikipedia.org/wiki/Projectively_extended_real_line)
_vs_ [_Extended real number line_](https://en.wikipedia.org/wiki/Extended_real_number_line)
- [_Double-precision floating-point format_](https://en.wikipedia.org/wiki/Double-precision_floating-point_format)
- [_Exact value of a floating-point number as a rational_](https://stackoverflow.com/questions/51142275/exact-value-of-a-floating-point-number-as-a-rational).
- [_Continued fraction_](https://en.wikipedia.org/wiki/Continued_fraction)
- [_An introduction to context-oriented programming in Kotlin_](https://proandroiddev.com/an-introduction-context-oriented-programming-in-kotlin-2e79d316b0a2)
- [_Continued Fractions_<sup>\[PDF\]</sup>](http://pi.math.cornell.edu/~gautam/FiniteContinuedFractions.pdf)
- [_Generalized continued fracion_](https://en.wikipedia.org/wiki/Generalized_continued_fraction)
