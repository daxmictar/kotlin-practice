# `var`/`val` & making data types with data classes

## Why is this concept important?

`var` and `val` are important keywords for defining variables. Each has its own behavior and will
have an effect on how a value for a variable will behave.

If you're familiar with Java, you may have used the `static` and `final` keywords *together*, which means that the value
cannot change once it has been assigned and it cannot be overriden in a subclass. 

C and C++ have the `const` keyword, which stands for "constant". This too imposes immutability onto a variable.

When declaring and assigning values in Kotlin, `var` allows for mutability, while `val` renders a
variable immutable. The `var` keyword is short for "variable" while `val` is short for "value".

`var`s are mutable.

```Kotlin
var x = 5
x = 10 // Works!
```

`val`s are immutable.

```Kotlin
val y = 5
y = 10 // Won't work!
```

What about `data class`es? Data classes are a way to represent a container that holds data. While
they are similar to a regular `class` in principle, what separates the two is code generation. 

## How do we define a data class?

Use the keywords `data` and `class` together along with a `name`. After, a parameter list `()` is required,
which lists the properties (or fields) of the data class. Here we define a simple data class called Example.

```Kotlin
data class Example(val name: String, var number: Int)
var myExample = Example("Hello World!", 10)
myExample.name = "FooBar" // No! It's labeled as val, so it's immutable.
myExample.number = 20 // Yes! It's labeled as a var, so it's mutable.
```

Using a generated `toString()` in Android Studio:

```Kotlin
// We don't have to call toString() if we use string interpolation (using the $ inside of a String and a var/val).
Text(text = "$myExample") // -> Coerces to a String as `Example(name="Hello World", number=20)`
```

In Kotlin, data classes have a few methods automatically generated based on the fields of a 
data class. The `class` body `{}` is optional (see above) if you'd like to write your own methods.

For example, the `toString()`, `equals()`, `hashCode()`, `componentN()`, and `copy()` methods are
generated for you.

In Java or a regular Kotlin `class`, we have to write our own versions of these methods for each new class that we define
(unless we inherit from a class where it has been overriden from `Object`).

The `toString()` method is inherited from the super `Object` class, it returns the object's location in heap memory by default.

## Why is this concept important in business, games, or life?

To start, lets pretend we're an ecommerce business. Such a business may have several items on a 
marketplace to be sold to people. Each item has a fixed price that won't change at runtime. In this
case, the "value" of each item can be assigned to a `val`.

```kotlin
val itemOnePrice: Float = 9.99
val itemTwoPrice: Float = 19.99
...and so on
```

But this can get tedious... What about the item's name, a unique identifier, the quantity of the
item available in the marketplace?

Well what about just adding new `val`s for each item?

```kotlin
val itemOnePrice: Float = 9.99
val itemOneName: String = "Ceramic Plate Set"

val itemTwoPrice: Float = 19.99
val itemTwoName: String = "Linen Rug"

...and so on
```

But imagine if we had 10, 20, or even 100 items, we would need to copy and paste each set of 
variables for each new item, and if we ran out of stock for that item, we would need to go in, find
that specific set of variables, and then delete the variables. What if we restocked that item? Well,
go back and replace the variables we *just* deleted. This is a cumbersome process... What if instead,
we used the `data class` feature?

### `data class` Example

As we discussed earlier, a few of the properties (or fields) of the items being sold won't be privy
to changes *at runtime*, so we can assign them as `val`s!

However, what if we wanted our marketplace to dynamically update based on recent purchases? We would
need to have a way for the quantity of items to go up or down. This is where `var` comes in! We can
encapsulate all of our data for an `Item` object in one class, as seen below:

```kotlin
data class Item(
    val name: String = "Placeholder Name",
    val price: Float = 9.99,
    val discount: Float = 1.00,
    var quantity: Int = 0,
) {}

val marketPlace: List<Item> = listOf(
    Item(name="Ceramic Plate Set", price=9.99, discount=0.9, quantity=25),
    Item(name="Linen Rug", price=19.99, discount=1.0, quantity=10),
)
```

Once we have our `data class`, all we need to do is define it an add it to a data structure. To keep
things simple, I'll use a list here (as seen above). If we want to update a quantity, all we need
to do is iterate through the list, find the proper item, and update the quantity.




