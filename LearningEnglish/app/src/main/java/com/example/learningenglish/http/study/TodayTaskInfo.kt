package com.example.learningenglish.http.study

import com.example.learningenglish.http.wordbook.WordBook

data class TodayTaskInfo(
    val bookName: WordBook,
    val learnedWordCount: Int,
    val totalWordCount: Int,
    val todayWordCount: Int,
    val todayReviewWordCount: Int
)
