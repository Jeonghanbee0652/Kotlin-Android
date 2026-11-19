package com.example.bookmarket

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

// 도서 한 권의 정보
data class Book(
    val id: String,
    val title: String,
    val author: String,
    val publisher: String,
    val price: Int,
    val releaseDate: String,
    val category: String,
    val description: String,
    val coverColor: Long,
    val coverEmoji: String
)

// 앱에서 사용할 도서 목록 (예제용 데이터)
object BookRepository {
    val books = listOf(
        Book(
            "BOOK1234", "쉽게 배우는 PHP 웹 프로그래밍", "김코딩", "북마켓출판",
            29000, "2025-06-01", "IT전문서",
            "PHP의 기초 문법부터 MySQL 연동, 회원 관리 게시판까지 단계별로 배우는 웹 프로그래밍 입문서입니다.",
            0xFFF4B942, "🐘"
        ),
        Book(
            "BOOK1235", "쉽게 배우는 알고리즘 with 파이썬", "이자바", "북마켓출판",
            25000, "2025-03-15", "IT전문서",
            "정렬, 탐색, 그래프 등 핵심 알고리즘을 파이썬 코드와 그림으로 쉽게 설명합니다.",
            0xFF3B7DC4, "🐍"
        ),
        Book(
            "BOOK1236", "쉽게 배우는 MySQL", "박디비", "북마켓출판",
            27000, "2025-01-20", "IT전문서",
            "데이터베이스 설계부터 SQL 활용까지 MySQL로 실습하며 익히는 데이터베이스 입문서입니다.",
            0xFF2E9C8F, "🐬"
        ),
        Book(
            "BOOK1237", "미라클 HTML5+CSS3+자바스크립트", "최웹", "북마켓출판",
            31000, "2024-11-10", "IT전문서",
            "웹 표준 HTML5와 CSS3, 자바스크립트로 반응형 웹 페이지를 직접 만들어 봅니다.",
            0xFFE0505A, "✨"
        ),
        Book(
            "BOOK1238", "LEAN AI 실무 활용", "정에이아이", "북마켓출판",
            24000, "2025-08-05", "IT일반",
            "업무에 바로 쓰는 생성형 AI 활용법과 프롬프트 작성 요령을 사례 중심으로 소개합니다.",
            0xFFA65C9E, "🤖"
        ),
        Book(
            "BOOK1239", "코틀린으로 배우는 안드로이드", "한코틀", "북마켓출판",
            33000, "2025-09-01", "IT전문서",
            "코틀린 문법부터 위젯, 레이아웃, 인텐트, 데이터베이스까지 안드로이드 앱 개발 전 과정을 다룹니다.",
            0xFF7E62B5, "🤖"
        )
    )

    fun find(id: String?): Book? = books.firstOrNull { it.id == id }
}

// 도서 표지 모양의 뷰 만들기 (색 배경 + 그림 + 제목)
fun makeCover(context: Context, book: Book, heightDp: Int): TextView {
    val cover = TextView(context)
    cover.layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, context.dp(heightDp)
    )
    cover.setBackgroundColor(book.coverColor.toInt())
    cover.gravity = Gravity.CENTER
    val pad = context.dp(12)
    cover.setPadding(pad, pad, pad, pad)
    cover.text = "${book.coverEmoji}\n\n${book.title}"
    cover.textSize = 15f
    cover.setTextColor(0xFFFFFFFF.toInt())
    cover.setTypeface(cover.typeface, Typeface.BOLD)
    return cover
}