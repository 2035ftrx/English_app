package com.example.learningenglish.http.admin

import com.example.learningenglish.http.ApiWrapper
import com.example.learningenglish.http.bean.ApiPageResponse
import com.example.learningenglish.http.bean.ApiResponse
import com.example.learningenglish.http.word.Word
import com.example.learningenglish.http.wordbook.WordBook

class AdminWordRepository {

    private fun createApi(): WordApi {
        return ApiWrapper.create(WordApi::class.java)
    }


    suspend fun getAllWordBooks(
        page: Int,
        size: Int
    ): Result<ApiResponse<ApiPageResponse<WordBook>>> {

        val params = HashMap<String, Any>()
        params.put("page", page)
        params.put("size", size)
        return kotlin.runCatching { createApi().getAllWordBooks(params) }
    }

    suspend fun getWordBookById(id: Long): Result<ApiResponse<WordBook>> {

        val params = HashMap<String, Any>()
        params.put("id", id)
        return kotlin.runCatching { createApi().getWordBookById(params) }
    }

    suspend fun createWordBook(
        picUrl: String,
        title: String,
        wordNum: Int,
        tags: String
    ): Result<ApiResponse<WordBook>> {

        val params = HashMap<String, Any>()
        picUrl?.let { params.put("picUrl", picUrl) }
        title?.let { params.put("title", title) }
        wordNum?.let { params.put("wordNum", wordNum) }
        tags?.let { params.put("tags", tags) }
        return kotlin.runCatching { createApi().createWordBook(params) }
    }

    suspend fun updateWordBook(
        id: Long,
        picUrl: String,
        title: String,
        wordNum: Int,
        tags: String
    ): Result<ApiResponse<WordBook>> {

        val params = HashMap<String, Any>()
        id?.let { params.put("id", id) }
        id?.let { params.put("bookId", "bookId") }
        picUrl?.let { params.put("picUrl", picUrl) }
        title?.let { params.put("title", title) }
        wordNum?.let { params.put("wordNum", wordNum) }
        tags?.let { params.put("tags", tags) }
        return kotlin.runCatching { createApi().updateWordBook(id, params) }
    }

    suspend fun deleteWordBook(bookId: Long): Result<ApiResponse<Long>> {
        val params = HashMap<String, Any>()
        params.put("id", bookId)
        return kotlin.runCatching { createApi().deleteWordBook(bookId, params) }
    }

    suspend fun getAllWords(
        bookId: Long,
        page: Int,
        size: Int
    ): Result<ApiResponse<ApiPageResponse<Word>>> {

        val params = HashMap<String, Any>()
        params.put("bookId", bookId)
        params.put("page", page)
        params.put("size", size)
        return kotlin.runCatching { createApi().getAllWords(params) }
    }

    suspend fun getWordById(id: Long): Result<ApiResponse<Word>> {

        val params = HashMap<String, Any>()
        params.put("id", id)
        return kotlin.runCatching { createApi().getWordById(params) }
    }

    suspend fun createWord(
        wordBookId: Long,
        headWord: String,
        word: String
    ): Result<ApiResponse<Word>> {

        val params = HashMap<String, Any>()
        wordBookId?.let { params.put("bookId", wordBookId) }
        headWord?.let { params.put("headWord", headWord) }
        word?.let { params.put("word", word) }
        return kotlin.runCatching { createApi().createWord(params) }
    }

    suspend fun updateWord(
        id: Long,
        headWord: String,
        word: String
    ): Result<ApiResponse<Word>> {

        val params = HashMap<String, Any>()
        id?.let { params.put("id", id) }
        headWord?.let { params.put("headWord", headWord) }
        word?.let { params.put("word", word) }
        return kotlin.runCatching { createApi().updateWord(id, params) }
    }

    suspend fun deleteWord(id: Long): Result<ApiResponse<Long>> {
        val params = HashMap<String, Any>()
        id?.let { params.put("id", id) }
        return kotlin.runCatching { createApi().deleteWord(id, params) }
    }

}