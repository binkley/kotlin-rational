<a href="LICENSE.md">
<img src="https://unlicense.org/pd-icon.png" alt="Public Domain" align="right"/>
</a>

# Kotlin Rational

An immutable, infinite-precision Rational (ratio, fraction) class for Kotlin

This code is "finger exercise", largely demonstrating Kotlin operator
overloading, and writing clear, concise, clean Kotlin.  It also explores the
impact of `NaN` (which is extensive).

## Design choices

These were great help:

- [Android's `Rational`](https://developer.android.com/reference/kotlin/android/util/Rational),
expecially treating `Rational` as a `kotlin.Number`, and methods such as
`isFinite()` and `isInfinite()`
- [Fylipp/rational](https://github.com/Fylipp/rational), especially the
infix `over` constructor
- [_Rational number_](https://en.wikipedia.org/wiki/Rational_number) describes
mathematical properties of ℚ, the field of the rationals

### Always proper form

This code always keeps rationals in proper form:

1. The numerator and denominator are coprime
2. The denominator is non-negative

(In more detail, the denominator is always positive when the rational is
finite.)

### Representation of positive infinity, negative infinity, and not a number

(It is unclear if this code should cope with infinities and not a number.  See
[Division by 0, infinities](#division-by-0-infinities) for discussion.)

This code represents certain special cases via implied division by zero:

* `+∞` is `1 over 0`
* `NaN` is `0 over 0`
* `-∞` is `-1 over 0`

And preserve standard meanings:

* `NaN` propagates
* Operations with infinities produce an infinity, or not a number

Division by an infinity is zero.  This code does not have a concept of
infinitesimals.

### `Rational` is a `Number`

In this code, `Rational` is a `kotlin.Number`, in part to pick up Kotlin
handling of numeric types.  One consequence: This code has no conversion from
a `Rational` to a `Char`; it raises an error.  This conversion seemed
perverse, eg, `3/5 -> ??some character`.

This code treats other conversions numerically, performing the implied
division of a rational.  This means rounding for conversion to whole numbers
following Java conventions (positive result if numerator and denominator
have the same sign; negative result if they are oppositely signed), and
closest approximation for conversion to floating point (similar rules on
signs).

### Division by 0, infinities

There are two ways to handle division by 0:

- Raise an error, what whole numbers do (eg, `1 / 0`)
- Produce a `NaN`, what floating point does (eg, `1.0 / 0`)

This code produces `NaN`, mostly to explore the problem space (which turns
out to be rather bothersome).  A production version might rather throw an
`ArithmeticError` than a `NaN`.

As with floating point, `NaN != NaN`, and finite values equals themselves.
As with mathematics, inifities are also not equal to themselves, so
`POSITIVE_INFINITY != POSITIVE_INFINTY` and
`NEGATIVE_INFINITY != NEGATIVE_INFINITY`.  (This code does not provide the
needed sense of equivalence, nor does it cope with infinitesimals.)

This code also represents infinities as division by 0 (positive infinity is
`1 / 0`; negative infinity is `-1 / 0`).  The field of rationals (ℚ) is
complex (in the colloquial meaning) when considering infinities.

### Sorting

All values sort in the natural mathematical sense, except that `NaN` always
sort to last, following the convention of floating point.

All `NaN` are "quiet"; none are "signaling", including sorting.  This follows
the Java convention for floating point.  (See
[`NaN`](https://en.wikipedia.org/wiki/NaN).)

## API

In general, properties, methods, and operations do not have documentation,
with the understanding they behave the same as their floating point
counterpart.

### Properties

- `numerator` and `denominator` behave as expected

### Methods

- `isNan()`, `isPositiveInfinity()`, `isNegativeInfinity()`
- `isFinite()`, `isInfinite()`.  Note than `NaN` is neither finite nor
infinite
- `reciprocal()`, `abs()`, `signum()`, `pow(exponent)`
- `gcm(other)`, `lcd(other)`

### Operators

- All Kotlin numeric operators except `rem` (division is exact; also, avoid
implementing LCM)
- Ranges and progressions

## Implementation choices

### GCD vs LCM

There are several places that might use LCM (eg, dividing rationals).  This
code relies on the factory constructor (`Rational.new`) to use GCM for
reducing rationals to simplest form.

### Identity of constants

Rather than check numerator and denominator throughout for special values,
this code relies on the factory constructor (`Rational.new`) to produce known
constants, and relevant code checks for those constants.

See:

- `Nan`, `isNaN()`
- `POSITIVE_INFINITY`, `isPositiveInfinity()`
- `NEGATIVE_INFINITY`, `isNegativeInfinity()`
- `ZERO`, `ONE`

### Special case handling _vs_ sealed class

This code uses special case handling for `NaN`, `POSITIVE_INFINITY`, and
`NEGATIVE_INFINITY`.  An alternative is to make `Rational` a sealed class with
separate subclasses for those.  This would also allow for handling of
infinitesimals.  However, the abstraction bleeds between subclasses.  It is
unclear if a sealed class makes clearer code.
