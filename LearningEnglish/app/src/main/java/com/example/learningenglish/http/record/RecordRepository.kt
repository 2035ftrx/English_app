package com.example.learningenglish.http.record

import com.example.learningenglish.http.ApiWrapper
import com.example.learningenglish.http.bean.ApiResponse

class RecordRepository {

    private fun createApi(): RecordApi {
        return ApiWrapper.create(RecordApi::class.java)
    }


    suspend fun updateStudyRecord(
        bookId: Long,
        wordId: Long,
        status: Int
    ): Result<ApiResponse<StudyRecord>> {

        val params = HashMap<String, Any>()
        bookId?.let { params.put("bookId", bookId) }
        wordId?.let { params.put("wordId", wordId) }
        status?.let { params.put("status", status) }
        return kotlin.runCatching { createApi().updateStudyRecord(params) }
    }

    suspend fun recordStudySession(
        bookId: Long,
        wordId: Long,
        startTime: Long,
        endTime: Long,
        type: Int
    ): Result<ApiResponse<StudySession>> {

        val params = HashMap<String, Any>()
        bookId?.let { params.put("bookId", bookId) }
        wordId?.let { params.put("wordId", wordId) }
        startTime?.let { params.put("startTime", startTime) }
        endTime?.let { params.put("endTime", endTime) }
        type?.let { params.put("type", type) }
        return kotlin.runCatching { createApi().recordStudySession(params) }
    }

    suspend fun getStudyStats(
        startDate: Long,
        endDate: Long
    ): Result<ApiResponse<StudyStatsDTO>> {

        val params = HashMap<String, Any>()
        params.put("startDate", startDate)
        params.put("endDate", endDate)
        return kotlin.runCatching { createApi().getStudyStats(params) }
    }

}