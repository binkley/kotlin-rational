<a href="LICENSE.md">
<img src="https://unlicense.org/pd-icon.png" alt="Public Domain" align="right"/>
</a>

# Kotlin Rational

An immutable, infinite-precision `Rational` (ratio, fraction) class for Kotlin

This code is a "finger exercise", largely demonstrating Kotlin operator
overloading, and writing clear, concise, clean Kotlin.  It also explores the
impact of `NaN` (which is extensive) rather than raising an error on division
by zero.

Try `./run.sh` for a demonstration.

## Design choices

These were great help:

- [Android's `Rational`](https://developer.android.com/reference/kotlin/android/util/Rational),
especially treating `Rational` as a `kotlin.Number`, and methods such as
`isFinite()` and `isInfinite()`
- [Fylipp/rational](https://github.com/Fylipp/rational), especially the
infix `over` constructor, and various overloads
- [_Rational number_](https://en.wikipedia.org/wiki/Rational_number) describes
mathematical properties of ℚ, the field of the rationals

### Always proper form

This code always keeps rationals in proper form:

1. The numerator and denominator are coprime (in "lowest form")
2. The denominator is non-negative

(The denominator is always positive when the rational is finite; it is zero
for the special values `NaN`, `POSITIVE_INFINITY`, and `NEGATIVE_INFINITY` as
an implementation detail).

### Representation of not a number and infinities

(It is unclear if this code should cope with infinities and not a number.  See
[Division by 0, infinities](#division-by-0-infinities) for discussion.)

This code represents certain special cases via implied division by zero:

* `+∞` is `1 over 0`
* `NaN` is `0 over 0`
* `-∞` is `-1 over 0`

And preserve standard meanings:

* `NaN` propagates
* Operations with infinities produce an infinity, or not a number

So `NaN.denominator`, `POSITIVE_INFINITY.denominator`, and
`NEGATIVE_INFINITY.denominator` all return zero.

Division by an infinity is zero, as is the reciprocal of an infinity.  This
code does not have a concept of infinitesimals ("ϵ or δ").  (See
[_Infinitesimal_](https://en.wikipedia.org/wiki/Infinitesimal) for a
discussion.)

### `Rational` is a `Number`

In this code, `Rational` is a `kotlin.Number`, in part to pick up Kotlin
handling of numeric types.  One consequence: This code raises an error for
conversion between `Rational` and `Char`.  This conversion seemed perverse,
_eg_, `3/5` to what character?

This code supports conversion among `Double` and `Float`, and`Rational`,
preserving infinities and not a number.  The conversion is _exact_: it
constructs a power-of-2 rational following IEEE 754; so converting the
resulting `Rational` back returns the original floating point value, including
infinities and not a number.

### Division by 0, infinities

There are two ways to handle division by 0:

- Raise an error, what whole numbers do (_eg_, `1 / 0`)
- Produce a `NaN`, what floating point does (_eg_, `1.0 / 0`)

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

### Conversions and operators

This code provides conversions (`toRational` and ilk) and operator overloads
for these `Number` types:

- `BigDecimal`
- `Double`
- `Float`
- `BigInteger`
- `Long`
- `Int`

Adding support for `Short` and `Byte` is simple, but I did not consider it
worthwhile.

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

- `numerator`, `denominator` behave as expected

### Methods

- `isNan()`, `isPositiveInfinity()`, `isNegativeInfinity()`
- `isFinite()`, `isInfinite()`.  Note than `NaN` is neither finite nor
infinite
- `isDyadic()` (see
[_Dyadic rational_](https://en.wikipedia.org/wiki/Dyadic_rational))
- `reciprocal()`, `abs()`, `signum()`, `pow(exponent)`
- `gcm(other)`, `lcd(other)`

### Operators

- All numeric operators except `rem` (division is exact)
- Ranges and progressions

### Types

This code attempts to ease programmer typing through overloading.  Where
sensible, if a `Rational` is provided as an argument or extension method type,
then so is a `BigInteger`, `Long`, and `Int`.

## Implementation choices

### Always in simplest terms

(See [_Always proper form_](#always-proper-form).)

Much of the code assumes the rational is in simplest terms (proper form).
The `Rational.new` factory method ensures this.

### Identity of constants

Rather than check numerator and denominator throughout for special values,
this code relies on the factory constructor (`Rational.new`) to produce known
constants, and relevant code checks for those constants.

See:

- `Nan`, `isNaN()`
- `POSITIVE_INFINITY`, `isPositiveInfinity()`
- `NEGATIVE_INFINITY`, `isNegativeInfinity()`
- `ZERO`, `ONE`

### Factory constructor

Rather than provide a public constructor, always use `Rational.new` factory
method.  This maintains invariants such as "lowest terms" (numerator and
denominator are coprime), sign handling, and reuse of special constant
objects.

### Special case handling _vs_ sealed class

This code uses special case handling for `NaN`, `POSITIVE_INFINITY`, and
`NEGATIVE_INFINITY`.  An alternative is to make `Rational` a sealed class with
separate subclasses for those.  This would also allow for handling of
infinitesimals.  However, the abstraction bleeds between subclasses.  It is
unclear if a sealed class makes clearer code.

### GCD vs LCM

There are several places that might use LCM (_eg_, dividing rationals).  This
code relies on the factory constructor (`Rational.new`) to use GCM for
reducing rationals to simplest form.

## Further reading

- [_Wheel of fractions_](https://en.wikipedia.org/wiki/Wheel_theory#Wheel_of_fractions)
- [_Abstract algebra_](https://en.wikipedia.org/wiki/Abstract_algebra)
- [Double-precision floating-point format](https://en.wikipedia.org/wiki/Double-precision_floating-point_format)
- [_Exact value of a floating-point number as a rational_](https://stackoverflow.com/questions/51142275/exact-value-of-a-floating-point-number-as-a-rational)).
