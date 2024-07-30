package com.example.learningenglish.aichat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        AIChatRecord::class,
        AIChatConversation::class,
    ],
    version = 1
)
abstract class AIChatDatabase : RoomDatabase() {

    abstract fun aiChatRecordDao(): AIChatRecordDao
    abstract fun aiChatConversationDao(): AIChatConversationDao

    companion object {
        private var INSTANCE: AIChatDatabase? = null

        fun getInstance(context: Context): AIChatDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AIChatDatabase::class.java,
                        "ai_chat_database"
                    ).build()
                }
                return INSTANCE!!
            }
        }
    }
}
