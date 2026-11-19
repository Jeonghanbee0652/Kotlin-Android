package com.example.bookmarket

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView

class MemoActivity : AppCompatActivity() {

    private lateinit var dbHelper: MemoDbHelper
    private lateinit var memoContainer: LinearLayout
    private lateinit var tvSearchInfo: TextView

    private var keyword = ""            // 현재 검색어
    private var selectedMemo: Memo? = null  // 선택한 메모 (수정/삭제 대상)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_memo)
        applyEdgeInsets()
        setupToolbar("메모장", true)

        dbHelper = MemoDbHelper(this)
        memoContainer = findViewById(R.id.memoContainer)
        tvSearchInfo = findViewById(R.id.tvSearchInfo)

        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            showMemoDialog(null)
        }

        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            val memo = selectedMemo
            if (memo == null) {
                Toast.makeText(this, "수정할 메모를 먼저 선택하세요", Toast.LENGTH_SHORT).show()
            } else {
                showMemoDialog(memo)
            }
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            val memo = selectedMemo
            if (memo == null) {
                Toast.makeText(this, "삭제할 메모를 먼저 선택하세요", Toast.LENGTH_SHORT).show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("메모 삭제")
                    .setMessage("'${memo.title}' 메모를 삭제하시겠습니까?")
                    .setNegativeButton("아니요", null)
                    .setPositiveButton("예") { _, _ ->
                        dbHelper.delete(memo.id)
                        selectedMemo = null
                        showMemos()
                        Toast.makeText(this, "삭제했습니다", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }
        }

        showMemos()
    }

    // 앱 바에 검색 액션 버튼 추가
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_memo, menu)
        val searchView = menu.findItem(R.id.menuSearch).actionView as SearchView
        searchView.queryHint = "검색어를 입력하세요"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // 검색 버튼(키보드 엔터)을 눌렀을 때
            override fun onQueryTextSubmit(query: String?): Boolean {
                keyword = query ?: ""
                showMemos()
                searchView.clearFocus()
                return true
            }

            // 검색어가 바뀔 때마다 바로 검색
            override fun onQueryTextChange(newText: String?): Boolean {
                keyword = newText ?: ""
                showMemos()
                return true
            }
        })
        return true
    }

    // 데이터베이스에서 메모를 읽어 화면에 출력
    private fun showMemos() {
        val memos = dbHelper.getMemos(keyword)
        memoContainer.removeAllViews()

        if (keyword.isBlank()) {
            tvSearchInfo.visibility = View.GONE
        } else {
            tvSearchInfo.visibility = View.VISIBLE
            tvSearchInfo.text = "'$keyword' 검색 결과: ${memos.size}건"
        }

        if (memos.isEmpty()) {
            val tvEmpty = TextView(this)
            tvEmpty.text = "메모가 없습니다."
            tvEmpty.setPadding(dp(8), dp(24), dp(8), 0)
            memoContainer.addView(tvEmpty)
        }

        for (memo in memos) {
            memoContainer.addView(makeMemoItem(memo))
        }
    }

    // 메모 한 개 화면 (제목 + 날짜 + 내용)
    private fun makeMemoItem(memo: Memo): LinearLayout {
        val item = LinearLayout(this)
        item.orientation = LinearLayout.VERTICAL
        item.setPadding(dp(10), dp(10), dp(10), dp(10))
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, dp(4), 0, dp(4))
        item.layoutParams = params

        // 선택한 메모 또는 검색 결과는 주황색 배경으로 강조
        val isSelected = selectedMemo?.id == memo.id
        when {
            isSelected -> item.setBackgroundColor(0xFFFBD9B0.toInt())
            keyword.isNotBlank() -> item.setBackgroundColor(0xFFFDEBD3.toInt())
            else -> item.setBackgroundColor(0xFFF7F7F7.toInt())
        }

        val top = LinearLayout(this)
        top.orientation = LinearLayout.HORIZONTAL
        top.gravity = Gravity.CENTER_VERTICAL

        val tvTitle = TextView(this)
        tvTitle.text = memo.title
        tvTitle.textSize = 15f
        tvTitle.setTypeface(tvTitle.typeface, Typeface.BOLD)
        tvTitle.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        top.addView(tvTitle)

        val tvDate = TextView(this)
        tvDate.text = memo.date
        tvDate.textSize = 12f
        tvDate.setTextColor(0xFF2B7CC4.toInt())
        top.addView(tvDate)

        item.addView(top)

        val tvContent = TextView(this)
        tvContent.text = memo.content
        tvContent.textSize = 13f
        tvContent.setTextColor(0xFF555555.toInt())
        tvContent.setPadding(0, dp(4), 0, 0)
        item.addView(tvContent)

        // 누르면 선택 / 다시 누르면 선택 해제
        item.setOnClickListener {
            selectedMemo = if (isSelected) null else memo
            showMemos()
        }
        return item
    }

    // 메모 등록(memo == null) / 수정 대화상자
    private fun showMemoDialog(memo: Memo?) {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(dp(20), dp(8), dp(20), 0)

        val edtTitle = EditText(this)
        edtTitle.hint = "제목"
        edtTitle.setText(memo?.title ?: "")
        layout.addView(edtTitle)

        val edtContent = EditText(this)
        edtContent.hint = "내용"
        edtContent.minLines = 3
        edtContent.gravity = Gravity.TOP
        edtContent.setText(memo?.content ?: "")
        layout.addView(edtContent)

        AlertDialog.Builder(this)
            .setTitle(if (memo == null) "메모 등록" else "메모 수정")
            .setView(layout)
            .setNegativeButton("취소", null)
            .setPositiveButton("저장") { _, _ ->
                val title = edtTitle.text.toString().trim()
                val content = edtContent.text.toString().trim()
                if (title.isEmpty()) {
                    Toast.makeText(this, "제목을 입력하세요", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                if (memo == null) {
                    dbHelper.insert(title, content)
                    Toast.makeText(this, "등록했습니다", Toast.LENGTH_SHORT).show()
                } else {
                    dbHelper.update(memo.id, title, content)
                    selectedMemo = null
                    Toast.makeText(this, "수정했습니다", Toast.LENGTH_SHORT).show()
                }
                showMemos()
            }
            .show()
    }
}