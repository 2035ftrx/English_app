package com.example.learningenglish.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    suspend fun findByUsernameAndPassword(username: String, password: String): User?

}
