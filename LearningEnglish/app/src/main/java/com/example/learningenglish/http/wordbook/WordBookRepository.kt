package com.example.learningenglish.http.wordbook

import com.example.learningenglish.http.ApiWrapper
import com.example.learningenglish.http.bean.ApiPageResponse
import com.example.learningenglish.http.bean.ApiResponse

class WordBookRepository {

    private fun createApi(): WordBookApi {
        return ApiWrapper.create(WordBookApi::class.java)
    }

    suspend fun list(
        page: Long = 0,
        size: Long = 20
    ): Result<ApiResponse<ApiPageResponse<WordBook>>> {
        val params = HashMap<String, Any>()
        params.put("page", page)
        params.put("size", size)
        return kotlin.runCatching { createApi().list(params) }
    }


    suspend fun get(bookId: Long): Result<ApiResponse<WordBook>> {

        val params = HashMap<String, Any>()
        params.put("bookId", bookId)
        return kotlin.runCatching { createApi().get(params) }
    }

}
