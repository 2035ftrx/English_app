package com.example.learningenglish.http.record

import com.example.learningenglish.http.bean.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface RecordApi {


    @POST("app/words/record/update")
    suspend fun updateStudyRecord(@Body params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<StudyRecord>

    @POST("app/words/record/record")
    suspend fun recordStudySession(@Body params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<StudySession>

    @GET("app/words/record/record/range")
    suspend fun getStudyStats(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<StudyStatsDTO>

}
