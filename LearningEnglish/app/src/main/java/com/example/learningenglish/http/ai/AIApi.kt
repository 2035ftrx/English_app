package com.example.learningenglish.http.ai

import com.example.learningenglish.http.bean.ApiResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface AIApi {

    @GET("app/ai/aiToken")
    suspend fun aiToken(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<AITokenResponse>

    @GET("app/ai/audioToken")
    suspend fun audioToken(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<AITokenResponse>

}
