package com.example.learningenglish.http.study

import com.example.learningenglish.http.word.Word
import com.example.learningenglish.http.wordbook.WordBook

data class PlanTask(
    val book: WordBook,
    val list: List<Word>
)
