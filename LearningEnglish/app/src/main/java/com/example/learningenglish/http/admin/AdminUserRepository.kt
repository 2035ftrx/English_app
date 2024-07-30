package com.example.learningenglish.http.admin

import com.example.learningenglish.http.ApiWrapper
import com.example.learningenglish.http.bean.ApiResponse

class AdminUserRepository {

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

    suspend fun getAllUsers(
        page: Int,
        size: Int
    ): Result<ApiResponse<List<UserInfoADTO>>> {
        val params = HashMap<String, Any>()
        params.put("page", page)
        params.put("size", size)
        return kotlin.runCatching { createApi().getAllUsers(params) }
    }

    suspend fun createUser(username: String, password: String): Result<ApiResponse<UserInfoADTO>> {
        val params = HashMap<String, Any>()
        username?.let { params.put("username", username) }
        password?.let { params.put("password", password) }
        return kotlin.runCatching { createApi().createUser(params) }
    }

    suspend fun getUserById(id: Long): Result<ApiResponse<UserInfoADTO>> {
        val params = HashMap<String, Any>()
        params.put("id", id)
        return kotlin.runCatching { createApi().getUserById(params) }
    }

    suspend fun updateUser(
        userId: Long,
        nickname: String,
        school: String,
        gender: Int,
        grade: String
    ): Result<ApiResponse<UserInfoADTO>> {
        val params = HashMap<String, Any>()
        userId?.let { params.put("id", userId) }
        nickname?.let { params.put("nickname", nickname) }
        school?.let { params.put("school", school) }
        gender?.let { params.put("gender", gender) }
        grade?.let { params.put("grade", grade) }
        return kotlin.runCatching { createApi().updateUser(userId, params) }
    }

    suspend fun deleteUser(userId: Long): Result<ApiResponse<Long>> {
        val params = HashMap<String, Any>()
        userId?.let { params.put("id", userId) }
        return kotlin.runCatching { createApi().deleteUser(userId, params) }
    }

}