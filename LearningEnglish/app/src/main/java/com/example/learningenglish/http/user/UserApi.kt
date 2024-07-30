package com.example.learningenglish.http.user

import com.example.learningenglish.http.bean.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    @POST("app/user/login")
    suspend fun login(@Body params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<LoginResponse>

    @POST("app/user/register")
    suspend fun register(@Body params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<UserDTO>

    @GET("app/user/userInfo")
    suspend fun userInfo(): ApiResponse<UserInfoDTO>

    @POST("app/user/updateUser")
    suspend fun updateUserInfo(@Body params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<UserInfoDTO>

}