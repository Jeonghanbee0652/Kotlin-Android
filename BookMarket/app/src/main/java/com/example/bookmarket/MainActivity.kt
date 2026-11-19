package com.example.bookmarket

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        applyEdgeInsets()

        val btnStart = findViewById<Button>(R.id.btnStart)
        btnStart.setOnClickListener {
            Toast.makeText(this, "도서 쇼핑몰을 시작합니다", Toast.LENGTH_SHORT).show()
        }
    }
}