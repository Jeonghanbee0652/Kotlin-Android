package com.example.bookmarket

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order)
        applyEdgeInsets()
        setupToolbar("주문서", true)

        // 배송정보 화면에서 전달받은 값
        val name = intent.getStringExtra("name") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val phone = intent.getStringExtra("phone") ?: ""
        val zip = intent.getStringExtra("zip") ?: ""
        val address = intent.getStringExtra("address") ?: ""

        findViewById<TextView>(R.id.tvDate).text = "배송일: $date"
        findViewById<TextView>(R.id.tvShipping).text =
            "배송주소\n" +
                    "성명: $name ($phone)\n" +
                    "우편번호: $zip\n" +
                    "주소: $address"

        // 주문 도서 표 만들기
        val table = findViewById<TableLayout>(R.id.tableOrder)
        table.addView(makeRow(listOf("도서명", "가격", "수량", "소계"), true))
        for (item in Cart.items) {
            table.addView(
                makeRow(
                    listOf(
                        "${item.book.title}\n${item.book.id}",
                        item.book.price.toString(),
                        item.quantity.toString(),
                        item.subtotal.toString()
                    ),
                    false
                )
            )
        }
        findViewById<TextView>(R.id.tvTotal).text = "총액: ${Cart.total().won()}"

        findViewById<Button>(R.id.btnCancelOrder).setOnClickListener {
            Toast.makeText(this, "주문을 취소했습니다", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 주문완료 -> 확인 대화상자
        findViewById<Button>(R.id.btnComplete).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("📦 도서 주문 완료")
                .setMessage("도서 주문을 완료하시겠습니까?")
                .setNegativeButton("아니요", null)
                .setPositiveButton("예") { _, _ ->
                    Cart.clear()
                    Toast.makeText(this, "주문이 완료되었습니다. 감사합니다!", Toast.LENGTH_SHORT).show()
                    // 메뉴 목록 화면으로 돌아가기
                    val intent = Intent(this, MenuActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                }
                .show()
        }
    }

    // 표의 한 행 만들기
    private fun makeRow(cells: List<String>, header: Boolean): TableRow {
        val row = TableRow(this)
        if (header) row.setBackgroundColor(0xFFE0F2F1.toInt())
        for ((index, text) in cells.withIndex()) {
            val tv = TextView(this)
            tv.text = text
            tv.textSize = 13f
            tv.setPadding(dp(4), dp(6), dp(4), dp(6))
            tv.gravity = if (index == 0) Gravity.START else Gravity.END
            if (header) tv.setTypeface(tv.typeface, Typeface.BOLD)
            row.addView(tv)
        }
        return row
    }
}