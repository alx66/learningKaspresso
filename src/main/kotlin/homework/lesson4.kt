package homework
// 1
class Inventory {
    val items = mutableListOf<String>()

    operator fun plus(item: String): Inventory {
        val newInventory = Inventory()
        newInventory.items.addAll(this.items)
        newInventory.items.add(item)
        return newInventory
    }

    operator fun get(index: Int): String {
        return items[index]
    }

    operator fun contains(item: String): Boolean {
        return item in items
    }
}

// 2
class Toggle(val enabled: Boolean) {
    operator fun not(): Toggle {
        return Toggle(!enabled)
    }
}

//3
class Price(val amount: Int) {
    operator fun times(multiplier: Int): Price {
        return Price(amount * multiplier)
    }
}

//4
class Step(val number: Int) : Comparable<Step> {
    override fun compareTo(other: Step): Int = this.number.compareTo(other.number)

    operator fun rangeTo(other: Step): IntRange {
        return IntRange(this.number, other.number)
    }
}

operator fun IntRange.contains(step: Step): Boolean {
    return step.number in this
}

//5
class Log(val entries: List<String>) {
    operator fun plus(other: Log): Log {
        return Log(this.entries + other.entries)
    }
}

// infix
class Person(private val name: String) {
    private val phrases = mutableListOf<String>()

    infix fun says(text: String): Person {
        phrases.add(text)
        return this
    }

    infix fun and(text: String): Person {
        if (phrases.isEmpty()) throw IllegalStateException("и может быть вызвана первая")
        phrases.add(text)
        return this
    }

    infix fun or(text: String): Person {
        if (phrases.isEmpty()) throw IllegalStateException("или не может быть вызвана первая")
        val lastPhrase = phrases.last()
        val selected = selectPhrase(lastPhrase, text)
        phrases[phrases.size - 1] = selected
        return this
    }

    private fun selectPhrase(first: String, second: String): String {
        return if ((0..1).random() == 0) first else second
    }
}