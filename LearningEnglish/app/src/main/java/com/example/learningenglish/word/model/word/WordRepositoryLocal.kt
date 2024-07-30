// WordRepository.kt
package com.example.learningenglish.word.model.word

import android.content.Context
import com.example.learningenglish.database.AppDatabase
import com.example.learningenglish.database.WordEntity

class WordRepositoryLocal(context: Context) {
    private val wordDao = AppDatabase.getInstance(context.applicationContext).wordDao()

    suspend fun getRandomWord(): WordEntity {
        val words = wordDao.getAllWords()
        return words.random()
    }

    suspend fun getWordById(wordId: Long): WordEntity {
        return wordDao.getWordById(wordId)
    }

    suspend fun getWordsByBookId(bookId: Long): List<WordEntity> {
        return wordDao.getWordsByBookId(bookId)
    }

    suspend fun removeWordByBookId(bookId: Long) {
        wordDao.removeWordByBookId(bookId)
    }

    suspend fun save(it: List<WordEntity>): List<Long> {
        return wordDao.insertWords(it)
    }

    suspend fun queryCountByBookId(bookId: Long): Int {
        return wordDao.queryCountByBookId(bookId)
    }

}
