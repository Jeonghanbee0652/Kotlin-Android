package com.example.bookmarket

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    private lateinit var cartContainer: LinearLayout
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
        applyEdgeInsets()
        setupToolbar("장바구니", true)

        cartContainer = findViewById(R.id.cartContainer)
        tvTotal = findViewById(R.id.tvTotal)

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            Cart.clear()
            showCart()
            Toast.makeText(this, "장바구니를 비웠습니다", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnOrder).setOnClickListener {
            if (Cart.items.isEmpty()) {
                Toast.makeText(this, "장바구니가 비어 있습니다", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, ShippingActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showCart()
    }

    // 장바구니 목록 출력
    private fun showCart() {
        cartContainer.removeAllViews()
        if (Cart.items.isEmpty()) {
            val tvEmpty = TextView(this)
            tvEmpty.text = "장바구니가 비어 있습니다.\n도서 상세 화면에서 [장바구니 담기]를 눌러 보세요."
            tvEmpty.setPadding(0, dp(24), 0, 0)
            cartContainer.addView(tvEmpty)
        }
        for (item in Cart.items) {
            val tvItem = TextView(this)
            tvItem.text = "${item.book.title}\n" +
                    "${item.book.price.won()} × ${item.quantity}권 = ${item.subtotal.won()}"
            tvItem.textSize = 15f
            tvItem.setLineSpacing(0f, 1.3f)
            tvItem.setPadding(dp(4), dp(12), dp(4), dp(12))
            cartContainer.addView(tvItem)

            val line = View(this)
            line.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(1))
            line.setBackgroundColor(0xFFDDDDDD.toInt())
            cartContainer.addView(line)
        }
        tvTotal.text = "총액: ${Cart.total().won()}"
        tvTotal.setTypeface(tvTotal.typeface, Typeface.BOLD)
    }
}