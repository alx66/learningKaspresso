package lesson7.homework

import org.hamcrest.Description
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anyOf

//
data class Shape(
    val sideLength: Float,
    val sideCount: Int,
    val color: Color
)

// цвета
enum class Color { RED, BLUE, GREEN, YELLOW, BLACK, WHITE }

// Матчер: Проверка длины стороны в заданном диапазоне
class SideLengthInRangeMatcher(
    private val min: Float,
    private val max: Float
) : TypeSafeDiagnosingMatcher<Shape>() {

    override fun matchesSafely(item: Shape, mismatchDescription: Description): Boolean {
        return when {
            item.sideLength < min -> {
                mismatchDescription.appendText("side length ${item.sideLength} is less than $min")
                false
            }
            item.sideLength > max -> {
                mismatchDescription.appendText("side length ${item.sideLength} is greater than $max")
                false
            }
            else -> true
        }
    }

    override fun describeTo(description: Description) {
        description.appendText("side length between $min and $max")
    }
}

// Матчер: проверка количества углов
class AngleCountMatcher(
    private val expectedAngleCount: Int
) : TypeSafeDiagnosingMatcher<Shape>() {

    override fun matchesSafely(item: Shape, mismatchDescription: Description): Boolean {
        val actualAngleCount = if (item.sideCount >= 3) item.sideCount else 0

        return if (actualAngleCount == expectedAngleCount) {
            true
        } else {
            mismatchDescription.appendText("angle count was $actualAngleCount")
            false
        }
    }

    override fun describeTo(description: Description) {
        description.appendText("angle count is $expectedAngleCount")
    }
}

// Матчер: проверка чётного количества сторон
class EvenSideCountMatcher : TypeSafeDiagnosingMatcher<Shape>() {

    override fun matchesSafely(item: Shape, mismatchDescription: Description): Boolean {
        val isEven = item.sideCount % 2 == 0

        if (!isEven) {
            mismatchDescription.appendText("side count ${item.sideCount} is odd")
        }
        return isEven
    }

    override fun describeTo(description: Description) {
        description.appendText("even side count")
    }
}

// Матчер: проверка цвета фигуры
class ColorMatcher(
    private val expectedColor: Color
) : TypeSafeDiagnosingMatcher<Shape>() {

    override fun matchesSafely(item: Shape, mismatchDescription: Description): Boolean {
        if (item.color != expectedColor) {
            mismatchDescription.appendText("color was ${item.color}")
            return false
        }
        return true
    }

    override fun describeTo(description: Description) {
        description.appendText("color is $expectedColor")
    }
}

// Матчер: проверка отрицательной длины стороны
class NegativeSideLengthMatcher : TypeSafeDiagnosingMatcher<Shape>() {

    override fun matchesSafely(item: Shape, mismatchDescription: Description): Boolean {
        if (item.sideLength >= 0) {
            mismatchDescription.appendText("side length was ${item.sideLength} (not negative)")
            return false
        }
        return true
    }

    override fun describeTo(description: Description) {
        description.appendText("negative side length")
    }
}

// Матчер: проверка отрицательного количества сторон
class NegativeSideCountMatcher : TypeSafeDiagnosingMatcher<Shape>() {

    override fun matchesSafely(item: Shape, mismatchDescription: Description): Boolean {
        if (item.sideCount >= 0) {
            mismatchDescription.appendText("side count was ${item.sideCount} (not negative)")
            return false
        }
        return true
    }

    override fun describeTo(description: Description) {
        description.appendText("negative side count")
    }
}

