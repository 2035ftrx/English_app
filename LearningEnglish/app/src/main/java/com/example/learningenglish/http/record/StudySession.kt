package com.example.learningenglish.http.record
data class StudySession (
    val id: Long,
    val userId: Long,
    val bookId: Long,
    val wordId: Long,
    val startTime: Long,
    val endTime: Long,
    val type: Int
)