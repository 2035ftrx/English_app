package com.example.learningenglish.http.user

import com.example.learningenglish.http.ApiWrapper
import com.example.learningenglish.http.bean.ApiResponse

class UserRepository {


    private fun createApi(): UserApi {
        return ApiWrapper.create(UserApi::class.java)
    }

    suspend fun login(
        username: String? = null,
        password: String? = null
    ): Result<ApiResponse<LoginResponse>> {

        val paramsMap = HashMap<String, Any>()
        username?.let { paramsMap.put("username", username) }
        password?.let { paramsMap.put("password", password) }
        return kotlin.runCatching { createApi().login(paramsMap) }
    }

    suspend fun register(
        username: String? = null,
        password: String? = null,
    ): Result<ApiResponse<UserDTO>> {

        val paramsMap = HashMap<String, Any>()
        username?.let { paramsMap.put("username", username) }
        password?.let { paramsMap.put("password", password) }
        return kotlin.runCatching { createApi().register(paramsMap) }
    }

    suspend fun userInfo(): Result<ApiResponse<UserInfoDTO>> {
        val paramsMap = HashMap<String, Any>()
        return kotlin.runCatching { createApi().userInfo() }
    }

    suspend fun updateUserInfo(
        nickname: String? = null,
        gender: Int? = null,
        school: String? = null,
        grade: String? = null,
    ): Result<ApiResponse<UserInfoDTO>> {
        val params = HashMap<String, Any>()
        nickname?.let { params.put("nickname", it) }
        gender?.let { params.put("gender", it) }
        school?.let { params.put("school", it) }
        grade?.let { params.put("grade", it) }
        return kotlin.runCatching { createApi().updateUserInfo(params) }
    }

}