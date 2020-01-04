# Kotlin Rational

An immutable, infinite-precision Rational (ratio, fraction) class for Kotlin

This code is "finger exercise", largely demonstrating Kotlin operator
overloading, and writing clear, concise, clean Kotlin.  It also explores the
impact of `NaN` (which is extensive).

## Design choices

These heavily influenced the API:

- [Android's `Rational`](https://developer.android.com/reference/kotlin/android/util/Rational),
expecially treating `Rational` as a `kotlin.Number`, and methods such as
`isFinite()`
- [Fylipp/rational](https://github.com/Fylipp/rational), especially the
infix `over` constructor

### `Rational` is a `Number`

In this code, `Rational` is a `kotlin.Number`, in part to pick up Kotlin
handling of numeric types.  One consequence: This code has no conversion from
a `Rational` to a `Char`; it raises an error.  This conversion seemed
perverse, eg, `3/5 -> ??`.

This code treats other conversions numerically, performing the implied
division of a rational.  This means rounding for conversion to whole numbers,
and closest approximation for conversion to floating point.

### Division by 0

There are two ways to handle division by 0:

- Raise an error, what whole numbers do (eg, `1 / 0`)
- Produce a `NaN`, what floating point does (eg, `1F / 0`)

This code produces `NaN`, mostly to explore the problem space (which turns
out to be rather bothersome).  A production version would likely raise an
error rather than produce NaN.

As with floating point, `NaN != NaN`; all other values equal themselves.

### Sorting

All values sort in the natural mathematical sense, except that `NaN` always
sort to last, following the convention of floating point.

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

### Operators

- All Kotlin numeric operators except `rem` (division is exact)
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

- `isNaN()`
- `isPositiveInfinity()`
- `isNegativeInfinity()`
