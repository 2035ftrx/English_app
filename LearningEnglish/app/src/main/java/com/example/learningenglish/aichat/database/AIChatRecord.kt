package com.example.learningenglish.aichat.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AIChatRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cId: Long,
    val msg: String,
    val role: Int,
    val status: Int,
    val createdAt: Long,
)
