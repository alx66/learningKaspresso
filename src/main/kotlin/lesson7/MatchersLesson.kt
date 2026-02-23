package lesson7

import org.hamcrest.Description
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeDiagnosingMatcher
import java.util.regex.Matcher

enum class Color {
    RED,
    GREEN,
    BLACK,
    BLUE,
}

data class Car(val brand: String, val color: Color, val age: Int)

// Простые функции проверки
fun checkMyFavoriteCar(car: Car) = with(car) {
    if (
        brand != "Subaru"
        || color != Color.BLUE
        || age > 8
    ) throw AssertionError("It is not my favorite car")
}

fun checkMyFavoriteCar2(car: Car) = with(car) {
    require(brand == "Subaru") { "Не подходящий бренд $brand. Ожидался 'Subaru'." }
    require(color == Color.BLUE) { "Не подходящий цвет $color. Ожидался BLUE." }
    require(age <= 8) { "Не подходящий возраст $age. Ожидалось <= 8 лет." }
}

//fun main() {
//    val car1 = Car("Subaru", Color.RED, 10)
//    val car2 = Car("Audi", Color.GREEN, 5)
//    val car3 = Car("Subaru", Color.BLUE, 8)
//    checkMyFavoriteCar2(car2)
//}

fun isMyFavoriteCar(car: Car): Boolean = with(car) {
    return brand == "Subaru"
            && color == Color.BLUE
            && age <= 8
}

// Матчеры хамкреста(проверки)

class BrandMatcher(
    private val expectedBrand: String
) : TypeSafeDiagnosingMatcher<Car>() {

    override fun describeTo(description: Description) {
        description.appendText("car with brand ")
            .appendValue(expectedBrand)
    }

    override fun matchesSafely(item: Car, mismatchDescription: Description): Boolean {
        if (item.brand != expectedBrand) {
            mismatchDescription
                .appendText("brand was ")
                .appendValue(item.brand)
            return false
        }
        return true
    }
}

class ColorMatcher(private val expectedColor: Color) : TypeSafeDiagnosingMatcher<Car>() {

    override fun describeTo(description: Description) {
        description.appendText("car with color ")
            .appendValue(expectedColor.name)
    }

    override fun matchesSafely(
        item: Car,
        mismatchDescription: Description
    ): Boolean {
        if (item.color != expectedColor) {
            mismatchDescription.appendText("color was ")
                .appendValue(item.color)
            return false
        }
        return true
    }
}

class FromAgeMatcher(private val expectedFromAge: Int) : TypeSafeDiagnosingMatcher<Car>() {
    override fun matchesSafely(
        item: Car,
        mismatchDescription: Description
    ): Boolean {
        if (item.age < expectedFromAge) {
            mismatchDescription.appendText("age was ")
                .appendValue(item.age)
            return false
        }
        return true
    }

    override fun describeTo(description: Description) {
        description.appendText("car age from ")
            .appendValue(expectedFromAge)
    }
}

class ToAgeMatcher(private val expectedToAge: Int) : TypeSafeDiagnosingMatcher<Car>() {
    override fun matchesSafely(
        item: Car,
        mismatchDescription: Description
    ): Boolean {
        if (item.age > expectedToAge) {
            mismatchDescription.appendText("age was ")
                .appendValue(item.age)
            return false
        }
        return true
    }

    override fun describeTo(description: Description) {
        description.appendText("car age to ")
            .appendValue(expectedToAge)
    }
}

// Практика

class CarMatchetBuilders() {
    private val carMatcher = mutableListOf<Matcher<Car>>()

    fun withBrand(brand: String) {
        carMatcher.add(BrandMatcher(brand))
    }

    fun withColor(color: Color) {
        carMatcher.add(ColorMatcher(color))
    }

    fun fromAge(age: Int) {
        carMatcher.add(FromAgeMatcher(age))
    }

    fun toAge(age: Int) {
        carMatcher.add(ToAgeMatcher(age))
    }

    fun build(): Matcher<Car> {
        return allOf<Any>(carMatcher)
    }
}

fun filter(cars: List<Car>, builder: CarMatchetBuilders.() -> Unit): List<Car> {
        val carMatcherBuilder = CarMatchetBuilders()
       // carMatcherBuilder.builder()
        builder(carMatcherBuilder)
        val receivedMatchers = carMatcherBuilder.build()
        return cars.filter { receivedMatchers.matches(it) }
}

fun main() {
    val car1 = Car("Toyota", Color.BLUE, 10)
    val car2 = Car("Audi", Color.GREEN, 10)
    val car3 = Car("Subaru", Color.BLUE, 8)
    val car4 = Car("Subaru", Color.BLUE, 10)

    val isSubaru = BrandMatcher("Subaru")
    val isBlue = ColorMatcher(Color.BLUE)
    val from4Age = FromAgeMatcher(4)
    val to8age = ToAgeMatcher(8)
    val allMatchers = allOf(isSubaru, isBlue, from4Age, to8age)

    val cars = listOf(car1, car2, car3, car4)

    val filtered = cars.filter { allMatchers.matches(it) }
    println(filtered)
    val matcherComposition = allOf(
        anyOf(isSubaru, BrandMatcher("Toyota")),
        isBlue
    )
    val filtered2 = cars.filter { matcherComposition.matches(it) }
    println(filtered2)

    assertThat(car1, allMatchers)
}

