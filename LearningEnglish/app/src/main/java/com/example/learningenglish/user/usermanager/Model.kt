package com.example.learningenglish.user.usermanager

import com.example.learningenglish.database.User
import com.example.learningenglish.http.user.UserDTO


fun User.toAppUserInfo():AppUserInfo{
    val appUserInfo = AppUserInfo(
        this.id
    )
    appUserInfo.username = username
    return appUserInfo
}


fun UserDTO.toAppUserInfo():AppUserInfo{
    return AppUserInfo(this.id).apply {
        username = this@toAppUserInfo.username
        role =this@toAppUserInfo. role
    }
}