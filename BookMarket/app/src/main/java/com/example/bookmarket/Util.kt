package com.example.bookmarket

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.DecimalFormat

// 상태 바 / 내비게이션 바 / 키보드에 화면이 가려지지 않도록 여백 설정
fun AppCompatActivity.applyEdgeInsets() {
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
        )
        v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
        insets
    }
}

// 29000 -> "29,000원"
fun Int.won(): String = DecimalFormat("#,###").format(this) + "원"

// dp 단위를 픽셀로 변환
fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()