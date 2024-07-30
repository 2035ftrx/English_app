package com.example.learningenglish.aichat.repository

import com.example.learningenglish.aichat.database.AIChatConversationDao
import com.example.learningenglish.aichat.database.AIChatRecordDao
import kotlinx.coroutines.flow.Flow

class IAIRepositoryImpl(
    private val conversationDao: AIChatConversationDao,
    private val aiChatRecordDao: AIChatRecordDao,
    private val userId: Long,
) : IAIRepository {
    override fun queryConversation() {
conversationDao
    }

    override fun sendQuestion(question: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}