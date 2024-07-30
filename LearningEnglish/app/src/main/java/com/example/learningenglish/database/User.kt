package com.example.learningenglish.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.learningenglish.user.usermanager.AppUserInfo

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val username: String,
    val password: String
)
