// ReciteRecordDao.kt
package com.example.learningenglish.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyTimeRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReciteRecord(record: StudyTimeRecord)

    @Query("SELECT * FROM StudyTimeRecord")
    suspend fun getAllReciteRecords(): List<StudyTimeRecord>

    // 根据时间范围获取学习记录
    @Query("SELECT * FROM StudyTimeRecord WHERE startTime >= :startTime AND startTime <= :endTime")
    suspend fun getStudyRecordsByTimeRange(startTime: Long, endTime: Long): List<StudyTimeRecord>

}
