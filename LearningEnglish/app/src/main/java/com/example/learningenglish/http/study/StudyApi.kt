package com.example.learningenglish.http.study

import com.example.learningenglish.http.bean.ApiResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface StudyApi {


    @GET("app/words/study/plan")
    suspend fun getTaskByPlan(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<PlanTask>

    @GET("app/words/study/review")
    suspend fun getReviewTask(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<ReviewTask>

    @GET("app/words/study/today")
    suspend fun getTodayTaskInfo(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<TodayTaskInfo>

}
