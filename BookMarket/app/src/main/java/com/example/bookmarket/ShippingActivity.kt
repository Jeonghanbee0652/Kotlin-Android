package com.example.bookmarket

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class ShippingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shipping)
        applyEdgeInsets()
        setupToolbar("배송정보", true)

        val edtName = findViewById<EditText>(R.id.edtName)
        val edtDate = findViewById<EditText>(R.id.edtDate)
        val edtPhone = findViewById<EditText>(R.id.edtPhone)
        val edtZip = findViewById<EditText>(R.id.edtZip)
        val edtAddress = findViewById<EditText>(R.id.edtAddress)

        // 배송일 칸을 누르면 날짜 선택 대화상자 표시
        edtDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day -> edtDate.setText("${year}년 ${month + 1}월 ${day}일") },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 등록 -> 주문서 화면으로 배송 정보 전달
        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val name = edtName.text.toString().trim()
            val date = edtDate.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val zip = edtZip.text.toString().trim()
            val address = edtAddress.text.toString().trim()

            if (name.isEmpty() || date.isEmpty() || phone.isEmpty() || zip.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "배송 정보를 모두 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, OrderActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("date", date)
            intent.putExtra("phone", phone)
            intent.putExtra("zip", zip)
            intent.putExtra("address", address)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            finish()
        }
    }
}