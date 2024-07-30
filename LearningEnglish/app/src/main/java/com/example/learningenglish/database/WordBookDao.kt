package com.example.learningenglish.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wordBook: WordBookEntity)

    @Query("SELECT * FROM WordBookEntity")
    fun getAllWordBooks(): Flow<List<WordBookEntity>>

    @Query("SELECT * FROM WordBookEntity WHERE id = :id")
    fun queryById(id: Long): Flow<WordBookEntity?>

    @Query("SELECT * FROM WordBookEntity WHERE id = :id")
    suspend fun findById(id: Long): WordBookEntity?

    @Query("DELETE FROM WordBookEntity WHERE id = :id")
    suspend fun removeById(id: Long)

}