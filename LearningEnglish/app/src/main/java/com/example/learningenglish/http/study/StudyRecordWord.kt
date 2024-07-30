package com.example.learningenglish.http.study

data class StudyRecordWord(
    val studyRecordId: Long,
    val userId: Long,
    val bookId: Long,
    val wordId: Long,
    val stage: Int,
    val strange: Int,
    val createTime: Long,
    val lastReviewTime: Long,
    val updateTime: Long,
    val wordRank: Int,
    val headWord: String,
    val word: String
)
