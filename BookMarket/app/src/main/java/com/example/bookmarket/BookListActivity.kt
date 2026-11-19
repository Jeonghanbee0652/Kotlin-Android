package com.example.bookmarket

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class BookListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_list)
        applyEdgeInsets()
        setupToolbar("도서목록", true)

        showBooks()
    }

    // 옵션 메뉴 만들기
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_book, menu)
        return true
    }

    // 옵션 메뉴 항목 선택
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return handleBookMenu(item)
    }

    // 도서를 2권씩 한 줄로 배치
    private fun showBooks() {
        val container = findViewById<LinearLayout>(R.id.bookContainer)
        val books = BookRepository.books
        for (i in books.indices step 2) {
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            for (j in i until minOf(i + 2, books.size)) {
                row.addView(makeBookItem(books[j]))
            }
            container.addView(row)
        }
    }

    // 도서 한 권 (표지 + 제목 + 가격)
    private fun makeBookItem(book: Book): LinearLayout {
        val item = LinearLayout(this)
        item.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        params.setMargins(dp(8), dp(8), dp(8), dp(8))
        item.layoutParams = params

        item.addView(makeCover(this, book, 190))

        val tvTitle = TextView(this)
        tvTitle.text = book.title
        tvTitle.textSize = 14f
        tvTitle.maxLines = 2
        tvTitle.setTypeface(tvTitle.typeface, Typeface.BOLD)
        tvTitle.setPadding(0, dp(6), 0, 0)
        item.addView(tvTitle)

        val tvPrice = TextView(this)
        tvPrice.text = "정가: ${book.price.won()}"
        tvPrice.textSize = 12f
        item.addView(tvPrice)

        item.setOnClickListener {
            onBookClick(book)
        }
        return item
    }

    // 도서를 클릭하면 도서 상세 정보 화면으로 이동 (도서 ID 전달)
    private fun onBookClick(book: Book) {
        val intent = Intent(this, BookDetailActivity::class.java)
        intent.putExtra("bookId", book.id)
        startActivity(intent)
    }
}