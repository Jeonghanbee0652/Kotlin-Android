package com.example.kotlingrammar

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// apply 예제에서 사용할 클래스
class Person {
    var name: String = ""
    var age: Int = 0
}

class MainActivity : AppCompatActivity() {

    private val output = StringBuilder()

    // Logcat(태그: 코틀린)과 화면에 동시에 출력
    private fun log(message: String) {
        Log.d("코틀린", message)
        output.append(message).append("\n")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. let : 객체를 it으로 받아서 블록 실행 (null이 아닐 때만)
        log("===== 1. let =====")
        val name1: String? = "Kotlin"
        name1?.let {
            log("이름의 길이는 ${it.length}입니다.")
        }

        // 2. run : 객체를 this로 받아서 작업 후 블록의 결괏값 반환
        log("===== 2. run =====")
        val name2: String? = "Hello, Kotlin"
        val messageLength = name2?.run {
            log("이름은 $this 입니다.")
            length
        }
        log("길이: $messageLength")

        // 3. with : 객체를 인자로 받아 여러 작업 후 결괏값 반환
        log("===== 3. with =====")
        val result = with(StringBuilder()) {
            append("Hello, ")
            append("Kotlin!")
            toString()
        }
        log(result)

        // 4. apply : 객체를 this로 받아 초기화한 뒤 객체 자신을 반환
        log("===== 4. apply =====")
        val person = Person().apply {
            name = "John"
            age = 25
        }
        log("이름: ${person.name}, 나이: ${person.age}")

        // 5. also : 객체를 it으로 받아 부수 작업 후 객체 자신을 반환
        log("===== 5. also =====")
        val number = mutableListOf(1, 2, 3).also {
            log("리스트의 크기: ${it.size}")
            it.add(4)
        }
        log(number.toString())

        findViewById<TextView>(R.id.tvResult).text = output.toString()
    }
}