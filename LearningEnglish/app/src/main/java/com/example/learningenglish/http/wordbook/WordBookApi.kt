package com.example.learningenglish.http.wordbook

import com.example.learningenglish.http.bean.ApiPageResponse
import com.example.learningenglish.http.bean.ApiResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WordBookApi {

    @GET("app/books/list")
    suspend fun list(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<ApiPageResponse<WordBook>>

    @GET("app/books/get")
    suspend fun get(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<WordBook>

}
