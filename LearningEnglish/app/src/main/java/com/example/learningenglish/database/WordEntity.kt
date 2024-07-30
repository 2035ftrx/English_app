// WordEntity.kt
package com.example.learningenglish.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val word: String,
    val info: String, // json string
    val bookId: Long,
    val wordBookName: String,
)
