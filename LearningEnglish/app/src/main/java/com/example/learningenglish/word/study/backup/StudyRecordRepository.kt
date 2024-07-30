// ReciteRecordRepository.kt
package com.example.learningenglish.word.study.backup

import android.content.Context
import com.example.learningenglish.database.AppDatabase
import com.example.learningenglish.database.WordStudyRecord
import com.example.learningenglish.database.WordStudyRecordDao
import com.example.learningenglish.word.model.Ebbinghaus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudyRecordRepository(context: Context) {
    private val wordStudyRecordDao: WordStudyRecordDao =
        AppDatabase.getInstance(context).reciteRecordDao()
    private val ebbinghaus = Ebbinghaus()

    suspend fun getReciteRecords(): List<WordStudyRecord> {
        return wordStudyRecordDao.getAllRecords()
            .filter { ebbinghaus.needReview(it) }
    }

    fun queryReciteRecords(userId: Long, bookId: Long): Flow<List<WordStudyRecord>> {
        return wordStudyRecordDao.queryRecordsByUserAndBook(userId, bookId)
    }

    fun queryTodayReciteRecords(userId: Long, bookId: Long): Flow<List<WordStudyRecord>> {
        return wordStudyRecordDao.queryRecordsByUserAndBookInToday(userId, bookId)
    }

    fun queryReciteRecordsToReview(userId: Long, bookId: Long): Flow<List<WordStudyRecord>> {
        return wordStudyRecordDao
            .queryRecordsByUserAndBook(userId, bookId)
            .map { it.filter { ebbinghaus.needReview(it) } }
    }

    suspend fun saveReciteRecord(record: WordStudyRecord) {
        wordStudyRecordDao.insertRecord(record)
    }

}