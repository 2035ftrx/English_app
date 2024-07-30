package com.example.learningenglish.http.word

import com.example.learningenglish.http.bean.ApiPageResponse
import com.example.learningenglish.http.bean.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface WordApi {


//    @Headers("Connection: Close")
    @GET("app/words/list")
    suspend fun list(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<ApiPageResponse<Word>>

    @GET("app/words/getWordById")
    suspend fun getWordById(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<Word>

    @GET("app/words/getWordByRank")
    suspend fun getWordByRank(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<Word>

    @GET("app/words/allwordsfile")
    suspend fun allwordsfile(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ResponseBody

}
