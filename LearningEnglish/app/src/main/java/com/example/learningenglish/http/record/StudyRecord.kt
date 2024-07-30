package com.example.learningenglish.http.record
data class StudyRecord (
    val id: Long,
    val userId: Long,
    val bookId: Long,
    val wordId: Long,
    val stage: Int,
    val strange: Int,
    val createTime: Long,
    val lastReviewTime: Long,
    val updateTime: Long
)