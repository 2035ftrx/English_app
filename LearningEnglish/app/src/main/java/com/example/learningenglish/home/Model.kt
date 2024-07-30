package com.example.learningenglish.home

data class TodayTaskUI(
    val bookName: String,
    val learnedWordCount: Int,
    val totalWordCount: Int,
    val todayWordCount: Int,
    val todayReviewWordCount: Int,

    val hasPlan: Boolean
)


