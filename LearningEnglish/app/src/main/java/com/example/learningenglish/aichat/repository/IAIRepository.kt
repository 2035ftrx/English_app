package com.example.learningenglish.aichat.repository

import kotlinx.coroutines.flow.Flow

interface IAIRepository {

    fun queryConversation()

    fun sendQuestion(question: String): Flow<Boolean>

}