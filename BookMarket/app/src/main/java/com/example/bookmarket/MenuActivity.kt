package com.example.bookmarket

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MenuActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        applyEdgeInsets()

        // 툴 바 + 햄버거(≡) 아이콘 -> 내비게이션 메뉴 열기
        drawerLayout = findViewById(R.id.drawerLayout)
        val toolbar = setupToolbar("BookMarket", false)
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // 내비게이션 메뉴 항목 선택
        val navView = findViewById<NavigationView>(R.id.navView)
        navView.setNavigationItemSelectedListener { item ->
            Toast.makeText(this, "${item.title} 메뉴를 선택했습니다", Toast.LENGTH_SHORT).show()
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        setupTiles()
    }

    private fun setupTiles() {
        // 도서목록 -> 도서목록 화면으로 이동
        findViewById<TextView>(R.id.tileBookList).setOnClickListener {
            startActivity(Intent(this, BookListActivity::class.java))
        }

        // 메모장 -> 메모장 화면으로 이동
        findViewById<TextView>(R.id.tileMemo).setOnClickListener {
            startActivity(Intent(this, MemoActivity::class.java))
        }

        // 나머지 메뉴는 토스트만 출력
        val others = mapOf(
            R.id.tileEbook to "E-Book",
            R.id.tileCustomer to "고객센터",
            R.id.tileMyPage to "나의페이지",
            R.id.tileMarket to "마켓정보"
        )
        for ((id, name) in others) {
            findViewById<TextView>(id).setOnClickListener {
                Toast.makeText(this, "$name 메뉴를 선택했습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}