// ReciteRecordDao.kt
package com.example.learningenglish.aichat.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AIChatConversationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: AIChatConversation): Long

    @Query("SELECT * FROM AIChatConversation")
    suspend fun getAll(): List<AIChatConversation>

    @Query("SELECT * FROM AIChatConversation ORDER BY createdAt DESC ")
    fun getAllByPage(): PagingSource<Int, AIChatConversation>
}
