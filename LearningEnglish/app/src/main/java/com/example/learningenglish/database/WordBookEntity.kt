package com.example.learningenglish.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WordBookEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val picUrl: String,
    val title: String,
    val wordNum: Int,
    val tags: String,
)