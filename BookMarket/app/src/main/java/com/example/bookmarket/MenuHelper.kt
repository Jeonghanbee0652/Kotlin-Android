package com.example.bookmarket

import android.content.Intent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

// 툴 바를 액션 바로 설정 (showBack = true 이면 ← 뒤로가기 버튼 표시)
fun AppCompatActivity.setupToolbar(title: String, showBack: Boolean): MaterialToolbar {
    val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)
    supportActionBar?.title = title
    if (showBack) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
    }
    return toolbar
}

// 액션 바 메뉴 선택 처리 : 메뉴 제목을 토스트로 출력하고 해당 화면으로 이동
fun AppCompatActivity.handleBookMenu(item: MenuItem): Boolean {
    Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
    when (item.itemId) {
        R.id.menuSearch -> {
            // 액션 아이템(검색) : 토스트만 출력
        }
        R.id.menuHome -> {
            // 홈으로 : 메뉴 목록 화면
            val intent = Intent(this, MenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        else -> {
            // 동영상강좌 / 고객센터 / 마이페이지 : 도서 상세 정보 화면으로 이동
            val intent = Intent(this, BookDetailActivity::class.java)
            intent.putExtra("bookId", BookRepository.books[0].id)
            startActivity(intent)
        }
    }
    return true
}