package com.example.learningenglish.http.word

import com.example.learningenglish.http.ApiWrapper
import com.example.learningenglish.http.bean.ApiPageResponse
import com.example.learningenglish.http.bean.ApiResponse
import okhttp3.ResponseBody
import timber.log.Timber

class WordRepository {

    private fun createApi(): WordApi {
        return ApiWrapper.create(WordApi::class.java)
    }


    suspend fun list(
        bookId: Long, page: Int, size: Int
    ): Result<ApiResponse<ApiPageResponse<Word>>> {
        val paramsMap = HashMap<String, Any>()
        bookId.let { paramsMap["bookId"] = it }
        page.let { paramsMap["page"] = it }
        size.let { paramsMap["size"] = it }
        Timber.d("paramsMap:$paramsMap")
        return kotlin.runCatching { createApi().list(paramsMap) }
    }

    suspend fun allwordsfile(bookId: Long): Result<ResponseBody> {

        val paramsMap = HashMap<String, Any>()
        bookId.let { paramsMap["bookId"] = it }

        return kotlin.runCatching { createApi().allwordsfile(paramsMap) }
    }

    suspend fun getWordById(wordId: Long): Result<ApiResponse<Word>> {
        val paramsMap = HashMap<String, Any>()
        wordId.let { paramsMap["wordId"] = it }
        return kotlin.runCatching { createApi().getWordById(paramsMap) }
    }

    suspend fun getWordByRank(bookId: Long, wordRank: Int): Result<ApiResponse<Word>> {
        val paramsMap = HashMap<String, Any>()
        bookId.let { paramsMap["bookId"] = it }
        wordRank.let { paramsMap["wordRank"] = it }
        return kotlin.runCatching { createApi().getWordByRank(paramsMap) }
    }

}
