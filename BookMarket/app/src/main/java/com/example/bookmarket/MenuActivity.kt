package com.example.bookmarket

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        applyEdgeInsets()

        // 도서목록 -> 도서목록 화면으로 이동
        findViewById<TextView>(R.id.tileBookList).setOnClickListener {
            startActivity(Intent(this, BookListActivity::class.java))
        }

        // 나머지 메뉴는 토스트만 출력
        val others = mapOf(
            R.id.tileEbook to "E-Book",
            R.id.tileCustomer to "고객센터",
            R.id.tileMyPage to "나의페이지",
            R.id.tileMarket to "마켓정보",
            R.id.tileMemo to "메모장"
        )
        for ((id, name) in others) {
            findViewById<TextView>(id).setOnClickListener {
                Toast.makeText(this, "$name 메뉴를 선택했습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}