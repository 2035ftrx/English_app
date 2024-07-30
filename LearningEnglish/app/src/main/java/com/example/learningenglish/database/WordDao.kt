// WordDao.kt
package com.example.learningenglish.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(it: List<WordEntity>): List<Long>

    @Query("SELECT * FROM WordEntity")
    suspend fun getAllWords(): List<WordEntity>

    @Query("SELECT * FROM WordEntity WHERE bookId = :bookId")
    suspend fun getWordsByBookId(bookId: Long): List<WordEntity>

    @Query("SELECT * FROM WordEntity WHERE id = :wordId")
    suspend fun getWordById(wordId: Long): WordEntity

    @Query("DELETE FROM WordEntity WHERE bookId = :bookId")
    suspend fun removeWordByBookId(bookId: Long)

    @Query("SELECT COUNT(*) FROM WordEntity WHERE bookId = :bookId")
    suspend fun queryCountByBookId(bookId: Long): Int

}
