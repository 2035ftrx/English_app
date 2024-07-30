package com.example.learningenglish.user.usermanager

import kotlinx.coroutines.flow.Flow

interface IUserInfoStorage<UserInfo> {
    suspend fun saveUserInfo(userInfo: UserInfo)
    fun getUserInfo(): Flow<UserInfo?>
    suspend fun deleteUserInfo(): Boolean
    fun checkUserInfoLogin(): Flow<Boolean>
}