package com.example.learningenglish.http.admin

import com.example.learningenglish.user.usermanager.AppUserInfo
import com.google.gson.annotations.SerializedName

data class UserDTO(
    val id: Long,
    val username: String,
    val role: Int,
)

data class LoginResponse(
    @SerializedName("token")
    val token: String ,
    @SerializedName("user")
    val user: UserDTO
)

fun UserDTO.toAppUserInfo():AppUserInfo{
    return AppUserInfo(this.id).apply {
        username = this@toAppUserInfo.username
        role = 2
    }
}
