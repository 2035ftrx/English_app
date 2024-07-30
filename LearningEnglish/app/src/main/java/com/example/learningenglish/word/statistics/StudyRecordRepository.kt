package com.example.learningenglish.word.statistics

import android.content.Context
import com.example.learningenglish.database.AppDatabase
import com.example.learningenglish.database.StudyTimeRecord
import com.example.learningenglish.database.StudyTimeRecordDao

class StudyRecordRepository(context:Context) {
    private val dao: StudyTimeRecordDao = AppDatabase.getInstance(context.applicationContext).studyTimeRecordDao()

    suspend fun getRecordsForLastWeek(): List<StudyTimeRecord> {
        val currentTime = System.currentTimeMillis()
        val startTime = currentTime - 7 * 24 * 60 * 60 * 1000
        return dao.getStudyRecordsByTimeRange(startTime, currentTime)
    }

}