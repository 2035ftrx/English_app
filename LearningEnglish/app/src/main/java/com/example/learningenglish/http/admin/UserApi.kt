package com.example.learningenglish.http.admin

import com.example.learningenglish.http.bean.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface UserApi {

    @POST("app/admin/login")
    suspend fun login(@Body params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<LoginResponse>

    @GET("app/admin/user/list")
    suspend fun getAllUsers(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<List<UserInfoADTO>>

    @POST("app/admin/user/create")
    suspend fun createUser(@Body params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<UserInfoADTO>

    @GET("app/admin/user/getUser")
    suspend fun getUserById(@QueryMap params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<UserInfoADTO>

    @POST("app/admin/user/update")
    suspend fun updateUser(@Query("id") userId: Long, @Body params: Map<String, @JvmSuppressWildcards Any>): ApiResponse<UserInfoADTO>

    @POST("app/admin/user/delete")
    suspend fun deleteUser(
        @Query("id")  userId: Long,
        @Body params: Map<String, @JvmSuppressWildcards Any>
    ): ApiResponse<Long>

}