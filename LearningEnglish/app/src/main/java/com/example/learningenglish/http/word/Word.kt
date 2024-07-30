package com.example.learningenglish.http.word


data class Word(
    val id: Long,
    val bookId: Long,
    val wordRank: Int,
    val headWord: String,
    val word: String,
)
