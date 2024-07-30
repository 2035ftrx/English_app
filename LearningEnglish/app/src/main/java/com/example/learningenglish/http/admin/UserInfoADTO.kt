package com.example.learningenglish.http.admin
data class UserInfoADTO (
    val id: Long,
    val username: String,
    val nickname: String,
    val gender: Int,
    val lastLoginTime: Long,
    val school: String?,
    val grade: String?
)