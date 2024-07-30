// ReciteRecordDao.kt
package com.example.learningenglish.aichat.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AIChatRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: AIChatRecord):Long

    @Update
    suspend fun update(record: AIChatRecord)

    @Query("SELECT * FROM AIChatRecord")
    suspend fun getAll(): List<AIChatRecord>

    @Query("SELECT * FROM AIChatRecord WHERE cId = :conversationId ORDER BY createdAt DESC")
    fun getAllByPage(conversationId: Long): PagingSource<Int, AIChatRecord>

    @Query("SELECT * FROM AIChatRecord WHERE cId = :conversationId ORDER BY createdAt DESC LIMIT 20")
    suspend fun getAllBy20(conversationId: Long): List<AIChatRecord>

}
