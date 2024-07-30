package com.example.learningenglish.user

import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.user.usermanager.AppUserInfo
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    fun login(username: String, password: String): Flow<LoadingData<AppUserInfo>>

    fun register(username: String, password: String): Flow<LoadingData<Boolean>>

}