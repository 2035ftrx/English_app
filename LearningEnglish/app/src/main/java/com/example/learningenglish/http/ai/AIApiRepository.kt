package com.example.learningenglish.http.ai

import com.example.learningenglish.http.ApiWrapper
import com.example.learningenglish.http.bean.ApiResponse

class AIApiRepository {

    private fun createApi(): AIApi {
        return ApiWrapper.create(AIApi::class.java)
    }

    suspend fun aiToken(): Result<ApiResponse<AITokenResponse>> {
        val params = HashMap<String, Any>()
        return kotlin.runCatching { createApi().aiToken(params) }
    }

    suspend fun audioToken(): Result<ApiResponse<AITokenResponse>> {
        val params = HashMap<String, Any>()
        return kotlin.runCatching { createApi().audioToken(params) }
    }

}
