package com.example.learningenglish.http.admin

import com.example.learningenglish.http.bean.ApiPageResponse
import com.example.learningenglish.http.bean.ApiResponse
import com.example.learningenglish.http.word.Word
import com.example.learningenglish.http.wordbook.WordBook
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface WordApi {
    @GET("app/admin/words/wordBooks")
    suspend fun getAllWordBooks(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<ApiPageResponse<WordBook>>

    @GET("app/admin/words/getBook")
    suspend fun getWordBookById(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<WordBook>

    @POST("app/admin/words/createBook")
    suspend fun createWordBook(@Body params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<WordBook>

    @POST("app/admin/words/updateBook")
    suspend fun updateWordBook(
        @Query("id") id: Long,
        @Body params: Map<String, @JvmSuppressWildcards Any>
    ): ApiResponse<WordBook>

    @POST("app/admin/words/deleteBook")
    suspend fun deleteWordBook(
        @Query("id") id: Long,
        @Body params: Map<String, @JvmSuppressWildcards Any>
    ): ApiResponse<Long>

    @GET("app/admin/words/getWordsByBookId")
    suspend fun getAllWords(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<ApiPageResponse<Word>>

    @GET("app/admin/words/getWord")
    suspend fun getWordById(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<Word>

    @POST("app/admin/words/createWord")
    suspend fun createWord(@Body params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<Word>

    @POST("app/admin/words/updateWord")
    suspend fun updateWord(
        @Query("id") id: Long,
        @Body params: Map<String, @JvmSuppressWildcards Any>
    ): ApiResponse<Word>

    @POST("app/admin/words/deleteWord")
    suspend fun deleteWord(
        @Query("id") id: Long,
        @Body params: Map<String, @JvmSuppressWildcards Any>
    ): ApiResponse<Long>

}
