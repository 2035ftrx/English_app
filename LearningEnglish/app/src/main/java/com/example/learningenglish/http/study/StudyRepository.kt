package com.example.learningenglish.http.study

import com.example.learningenglish.http.ApiWrapper
import com.example.learningenglish.http.bean.ApiResponse

class StudyRepository {

    private fun createApi(): StudyApi {
        return ApiWrapper.create(StudyApi::class.java)
    }


    suspend fun getTaskByPlan(bookId: Long, planWordCount: Int): Result<ApiResponse<PlanTask>> {
        val params = HashMap<String, Any>()
        params.put("bookId", bookId)
        params.put("planWordCount", planWordCount)
        return kotlin.runCatching { createApi().getTaskByPlan(params) }
    }


    suspend fun getReviewTask(bookId: Long ): Result<ApiResponse<ReviewTask>> {
        val params = HashMap<String, Any>()
        params.put("bookId", bookId)
        return kotlin.runCatching { createApi().getReviewTask(params) }
    }


    suspend fun getTodayTaskInfo(bookId: Long ): Result<ApiResponse<TodayTaskInfo>> {
        val params = HashMap<String, Any>()
        params.put("bookId", bookId)
        return kotlin.runCatching { createApi().getTodayTaskInfo(params) }
    }

}
