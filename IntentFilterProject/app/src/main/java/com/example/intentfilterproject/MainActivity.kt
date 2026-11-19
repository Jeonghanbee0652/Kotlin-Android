package com.example.intentfilterproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val pad = (16 * resources.displayMetrics.density).toInt()  // 16dp 여백
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left + pad, bars.top + pad, bars.right + pad, bars.bottom + pad)
            insets
        }

        val edtTo = findViewById<EditText>(R.id.edtTo)
        val edtSubject = findViewById<EditText>(R.id.edtSubject)
        val edtContent = findViewById<EditText>(R.id.edtContent)

        findViewById<Button>(R.id.btnSend).setOnClickListener {
            // 암시적 인텐트 : ACTION_SEND 인텐트 필터를 가진 앱(Gmail 등)이 받아서 실행됨
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(edtTo.text.toString()))
            intent.putExtra(Intent.EXTRA_SUBJECT, edtSubject.text.toString())
            intent.putExtra(Intent.EXTRA_TEXT, edtContent.text.toString())
            startActivity(Intent.createChooser(intent, "이메일 앱을 선택하세요"))
        }
    }
}