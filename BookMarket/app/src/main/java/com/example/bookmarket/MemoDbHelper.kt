package com.example.bookmarket

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 메모 한 개
data class Memo(val id: Long, val title: String, val content: String, val date: String)

// SQLite 데이터베이스 (memo.db 파일, memo 테이블)
class MemoDbHelper(context: Context) : SQLiteOpenHelper(context, "memo.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE memo (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT, content TEXT, date TEXT)"
        )
        // 처음 실행할 때 보여줄 예시 메모 3개
        insertSample(db, "데이터베이스", "1. 관계형 데이터베이스 개념\n2. SQLite 기본 문법\n3. 테이블 생성과 조회")
        insertSample(db, "SQLite 데이터베이스", "1. SQLiteOpenHelper 클래스\n2. 데이터 추가/수정/삭제\n3. rawQuery로 검색하기")
        insertSample(db, "안드로이드 공부", "코틀린 문법 복습\n레이아웃과 이벤트 처리 정리")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS memo")
        onCreate(db)
    }

    private fun insertSample(db: SQLiteDatabase, title: String, content: String) {
        val values = ContentValues()
        values.put("title", title)
        values.put("content", content)
        values.put("date", today())
        db.insert("memo", null, values)
    }

    // 메모 목록 가져오기 (keyword가 있으면 제목/내용에서 검색)
    fun getMemos(keyword: String): List<Memo> {
        val list = mutableListOf<Memo>()
        val cursor = if (keyword.isBlank()) {
            readableDatabase.rawQuery(
                "SELECT _id, title, content, date FROM memo ORDER BY _id DESC", null
            )
        } else {
            readableDatabase.rawQuery(
                "SELECT _id, title, content, date FROM memo " +
                        "WHERE title LIKE ? OR content LIKE ? ORDER BY _id DESC",
                arrayOf("%$keyword%", "%$keyword%")
            )
        }
        while (cursor.moveToNext()) {
            list.add(Memo(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)))
        }
        cursor.close()
        return list
    }

    fun insert(title: String, content: String) {
        val values = ContentValues()
        values.put("title", title)
        values.put("content", content)
        values.put("date", today())
        writableDatabase.insert("memo", null, values)
    }

    fun update(id: Long, title: String, content: String) {
        val values = ContentValues()
        values.put("title", title)
        values.put("content", content)
        values.put("date", today())
        writableDatabase.update("memo", values, "_id = ?", arrayOf(id.toString()))
    }

    fun delete(id: Long) {
        writableDatabase.delete("memo", "_id = ?", arrayOf(id.toString()))
    }

    private fun today(): String = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date())
}