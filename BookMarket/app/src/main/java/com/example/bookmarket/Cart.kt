package com.example.bookmarket

// 장바구니에 담긴 도서 한 권
data class CartItem(val book: Book, var quantity: Int) {
    val subtotal: Int
        get() = book.price * quantity
}

// 앱 전체에서 같이 쓰는 장바구니
object Cart {
    val items = mutableListOf<CartItem>()

    fun add(book: Book) {
        val found = items.find { it.book.id == book.id }
        if (found != null) {
            found.quantity++
        } else {
            items.add(CartItem(book, 1))
        }
    }

    fun total(): Int = items.sumOf { it.subtotal }

    fun clear() {
        items.clear()
    }
}