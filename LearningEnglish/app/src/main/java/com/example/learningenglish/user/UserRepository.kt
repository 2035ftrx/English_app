package com.example.learningenglish.user

import com.example.learningenglish.database.User
import com.example.learningenglish.database.UserDao

class UserRepository(private val database: UserDao) {

    suspend fun register(username: String, password: String): Long {
        return database.insert(User(0, username,  password))
    }

    suspend fun login(username: String, password: String): User? {
        return database.findByUsernameAndPassword(username, password)
    }
}
