package com.example.learningenglish.aichat.model

data class ChatRecordUI(
    val id: Long,
    val msg: String,
    val time: String,
    val role: IChatRole,
    val status: IChatRecordStatus,
)
