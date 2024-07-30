package com.example.learningenglish.word.model.wordbook

import android.content.Context
import com.example.learningenglish.database.AppDatabase
import com.example.learningenglish.database.WordBookEntity
import kotlinx.coroutines.flow.Flow

class WordBookRepositoryLocal( context: Context) {

    private val wordBookDao = AppDatabase.getInstance(context.applicationContext).wordBookDao()

    fun queryAll(): Flow<List<WordBookEntity>> {
        return wordBookDao.getAllWordBooks()
    }

    suspend fun insert(wordBookEntity: WordBookEntity) {
        wordBookDao.insert(wordBookEntity)
    }

    suspend fun findById(id: Long): WordBookEntity? {
        return wordBookDao.findById(id)
    }

    suspend fun remove(toEntity: WordBookEntity) {
        wordBookDao.removeById(toEntity.id)
    }


}
