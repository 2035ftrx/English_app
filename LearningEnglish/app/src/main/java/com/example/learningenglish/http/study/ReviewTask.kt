package com.example.learningenglish.http.study

import com.example.learningenglish.http.wordbook.WordBook

data class ReviewTask(
    val book: WordBook,
    val list: List<StudyRecordWord>
)