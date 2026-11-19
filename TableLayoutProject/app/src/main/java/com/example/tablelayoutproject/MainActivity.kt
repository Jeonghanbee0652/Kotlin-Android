package com.example.tablelayoutproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val pictures = listOf("🐢", "🍄", "🐞", "🌷", "🐟", "🍎", "🐤")
    private var answer1 = 0
    private var answer2 = 0

    private lateinit var edtCount1: EditText
    private lateinit var edtCount2: EditText
    private lateinit var tvPics1: TextView
    private lateinit var tvPics2: TextView

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

        edtCount1 = findViewById(R.id.edtCount1)
        edtCount2 = findViewById(R.id.edtCount2)
        tvPics1 = findViewById(R.id.tvPics1)
        tvPics2 = findViewById(R.id.tvPics2)

        newQuiz()

        findViewById<Button>(R.id.btnResult).setOnClickListener {
            val input1 = edtCount1.text.toString().toIntOrNull()
            val input2 = edtCount2.text.toString().toIntOrNull()

            if (input1 == null || input2 == null) {
                Toast.makeText(this, "그림의 개수를 모두 입력하세요", Toast.LENGTH_SHORT).show()
            } else if (input1 == answer1 && input2 == answer2) {
                Toast.makeText(this, "맞았습니다", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "틀렸습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 그림 종류와 개수를 무작위로 정해서 출력
    private fun newQuiz() {
        val shuffled = pictures.shuffled()
        answer1 = Random.nextInt(2, 7)
        answer2 = Random.nextInt(2, 7)
        tvPics1.text = shuffled[0].repeat(answer1)
        tvPics2.text = shuffled[1].repeat(answer2)
    }
}