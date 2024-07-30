// ReciteRecordDao.kt
package com.example.learningenglish.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordStudyRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: WordStudyRecord)

    @Query("SELECT * FROM WordStudyRecord")
    suspend fun getAllRecords(): List<WordStudyRecord>

    @Query("SELECT * FROM WordStudyRecord WHERE userId = :userId AND bookId = :bookId")
    fun queryRecordsByUserAndBook(userId: Long, bookId: Long): Flow<List<WordStudyRecord>>

    @Query("SELECT * FROM WordStudyRecord WHERE userId = :userId AND bookId = :bookId AND date(updateTime) = date('now')")
    fun queryRecordsByUserAndBookInToday(userId: Long, bookId: Long): Flow<List<WordStudyRecord>>

}
