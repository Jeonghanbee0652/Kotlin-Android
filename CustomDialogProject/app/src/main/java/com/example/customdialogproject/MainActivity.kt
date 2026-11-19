package com.example.customdialogproject

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // 계산기 상태
    private var current = ""           // 지금 입력 중인 숫자
    private var first: Double? = null  // 첫 번째 숫자
    private var op: String? = null     // 연산자 (+, -, *, /)
    private var justCalculated = false // 방금 = 을 눌렀는지

    private lateinit var tvExpr: TextView
    private lateinit var tvDisplay: TextView

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

        findViewById<Button>(R.id.btnCalculator).setOnClickListener {
            showCalculatorDialog()
        }
    }

    // 사용자 정의 대화상자 띄우기
    private fun showCalculatorDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_calculator, null)
        tvExpr = dialogView.findViewById(R.id.tvExpr)
        tvDisplay = dialogView.findViewById(R.id.tvDisplay)
        clearAll()

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        // 둥근 모서리 배경이 보이도록 기본 배경을 투명하게
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // tag가 있는 모든 버튼에 클릭 이벤트 연결
        bindButtons(dialogView)

        dialogView.findViewById<Button>(R.id.btnClose).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun bindButtons(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                bindButtons(view.getChildAt(i))
            }
        } else if (view is Button && view.tag != null) {
            view.setOnClickListener { onKey(view.tag.toString()) }
        }
    }

    // 버튼 하나를 눌렀을 때 처리
    private fun onKey(key: String) {
        when (key) {
            "AC" -> clearAll()
            "C" -> current = ""
            "back" -> current = current.dropLast(1)
            "." -> {
                if (justCalculated) { current = ""; justCalculated = false }
                if (!current.contains(".")) {
                    current = if (current.isEmpty()) "0." else "$current."
                }
            }
            "+", "-", "*", "/" -> onOperator(key)
            "=" -> onEquals()
            else -> { // 숫자
                if (justCalculated) { current = ""; justCalculated = false }
                if (current == "0") current = ""
                if (current.length < 12) current += key
            }
        }
        tvDisplay.text = current.ifEmpty { "0" }
    }

    private fun onOperator(key: String) {
        justCalculated = false
        if (current.isNotEmpty()) {
            val number = current.toDouble()
            val f = first
            val o = op
            first = if (f != null && o != null) {
                calculate(f, number, o) ?: return
            } else {
                number
            }
            current = ""
        }
        if (first == null) return
        op = key
        tvExpr.text = "${format(first!!)} ${symbol(key)}"
    }

    private fun onEquals() {
        val f = first ?: return
        val o = op ?: return
        if (current.isEmpty()) return
        val second = current.toDouble()
        val result = calculate(f, second, o) ?: return

        tvExpr.text = "${format(f)} ${symbol(o)} ${format(second)} = ${format(result)}"
        current = format(result)
        first = null
        op = null
        justCalculated = true
    }

    // 사칙연산 (0으로 나누면 null)
    private fun calculate(a: Double, b: Double, operator: String): Double? {
        return when (operator) {
            "+" -> a + b
            "-" -> a - b
            "*" -> a * b
            "/" -> {
                if (b == 0.0) {
                    Toast.makeText(this, "0으로 나눌 수 없습니다", Toast.LENGTH_SHORT).show()
                    null
                } else {
                    a / b
                }
            }
            else -> null
        }
    }

    private fun clearAll() {
        current = ""
        first = null
        op = null
        justCalculated = false
        tvExpr.text = ""
        tvDisplay.text = "0"
    }

    // 44.0 -> "44", 14.6666666667 -> "14.666666667"
    private fun format(value: Double): String {
        return if (value == Math.floor(value) && !value.isInfinite() && Math.abs(value) < 1e15) {
            value.toLong().toString()
        } else {
            String.format(Locale.US, "%.9f", value).trimEnd('0').trimEnd('.')
        }
    }

    private fun symbol(operator: String): String = when (operator) {
        "*" -> "×"
        "/" -> "÷"
        "-" -> "−"
        else -> operator
    }
}