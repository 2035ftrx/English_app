package com.example.learningenglish.http.user

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String
)

data class UserDTO(
    val id: Long,
    val username: String,
    val role: Int
)

data class UserInfoDTO(
    val nickname: String?,
    val gender: Int?,
    val school: String?,
    val grade: String?
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val confirmPassword: String
)

data class LoginResponse(
    @SerializedName("token")
    val token: String ,
    @SerializedName("user")
    val user: UserDTO
)
