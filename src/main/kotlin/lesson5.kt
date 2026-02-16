package forLessonKaspresso

import kotlin.math.min

// Практическая часть
// Разработаем свой DSL который позволит в виде удобного конфигурационного файла наполнить склад товарами.

data class Product(val name: String, val weight: Int, val price: Double)
private var countProduct: Int = 1

class Stock {

    private val products = mutableMapOf<Product, Int>()

    fun addProduct(product: Product) {
        products[product] = products.getOrDefault(product, 0) + 1
    }

    fun get(product: Product, amount: Int): Int {
        val currentAmount = products.getOrDefault(product, 0)
        val amountToReturn = min(currentAmount, amount)
        products[product] = currentAmount - amountToReturn
        return amountToReturn
    }

    override fun toString(): String {
        return products.map { (product, quantity) ->
            "${product.name} (${product.weight}g): $${product.price} $quantity items"
        }.joinToString("\n")
            .let { "** Stock **\n$it" }
    }
    // 1
    operator fun invoke(func: Stock.() -> Unit) {
        func()
    }
    // 2
    fun addProduct(name: String, weight: Int, price: Double) = addProduct(Product(name, weight, price))
    //3
    infix fun String.weight(weight: Int): Pair<String, Int> {
        return this to weight
    }
    infix fun Pair<String, Int>.by(price: Double) {
        repeat(countProduct) {
            addProduct(first, second, price)
        }
        countProduct = 1
    }
    // 4
    operator fun Int.times(name: String): String {
        countProduct = this
        return name
    }

}



fun main() {
    val stock1 = Stock()
    stock1 {

    }
    println(stock1.toString())
}