package com.example.learningenglish.aichat.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AIChatConversation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val title: String,
    val createdAt: Long,
)
