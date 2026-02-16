// перегрузка
class Distance(val meters: Int) {
    operator fun plus(other: Distance ): Distance {
        val sum = meters + other.meters
        return Distance(sum)
    }
}

class Score(private var points: Int) {
    operator fun plusAssign(value: Int) {
        points += value
    }

    fun getPoints() = points
}

class Level(val number: Int) {
    operator fun compareTo(other: Level): Int {
        return number.compareTo(other.number)
    }
}
class User(val id: Int, val name: String) {
    override operator fun equals(other: Any?): Boolean {
        if(other !is User) return false
        return id == other.id
    }

}

// инфиксные функции
infix fun Int.with(number: Int): Int {
    return "$this$number".toInt()
}

infix fun Int.without(number: Int): Int {
    return "$this".replace("$number", "").toInt()
}

fun main() {
    val distance = Distance(10)
    val otherDistance = Distance(20)
    val sum = distance + otherDistance
    println(sum.meters)

    val score = Score(1)
    score += 3
    println(score.getPoints())

    val uranus = Level(336)
    val cezium = Level(674)
    println(uranus > cezium)

    val firsUser = User(23, "Ivan")
    val secondUser = User(23, "Gay")
    println(firsUser == secondUser)

    10 with 20

    println(2342 without 2)
}
