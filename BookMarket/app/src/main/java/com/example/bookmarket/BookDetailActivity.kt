package com.example.bookmarket

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class BookDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_detail)
        applyEdgeInsets()
        setupToolbar("도서상세정보", true)

        // 도서목록 화면에서 전달한 도서 ID로 도서 찾기
        val book = BookRepository.find(intent.getStringExtra("bookId"))
        if (book == null) {
            finish()
            return
        }

        findViewById<LinearLayout>(R.id.coverHolder).addView(makeCover(this, book, 240))
        findViewById<TextView>(R.id.tvInfo).text =
            "도서ID : ${book.id}\n" +
                    "도서명 : ${book.title}\n" +
                    "저  자 : ${book.author}\n" +
                    "출판사 : ${book.publisher}\n" +
                    "가  격 : ${book.price.won()}\n" +
                    "출판일 : ${book.releaseDate}\n" +
                    "분  류 : ${book.category}\n\n" +
                    "설  명 :\n${book.description}"

        Toast.makeText(this, "${book.title} 상세 정보입니다", Toast.LENGTH_SHORT).show()

        // 장바구니 담기 -> 확인 대화상자
        findViewById<Button>(R.id.btnAddCart).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("🛒 장바구니 담기")
                .setMessage("${book.title}\n도서를 장바구니에 추가하시겠습니까?")
                .setNegativeButton("아니요", null)
                .setPositiveButton("예") { _, _ ->
                    Cart.add(book)
                    Toast.makeText(this, "장바구니에 담았습니다", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // 장바구니 보기 -> 장바구니 화면
        findViewById<Button>(R.id.btnViewCart).setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_book, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return handleBookMenu(item)
    }
}