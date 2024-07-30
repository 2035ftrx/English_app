package com.example.learningenglish.http

import okhttp3.Call
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiWrapper {

    private lateinit var retrofit: Retrofit

    fun init(
        url: String,
        interceptor: List<Interceptor> = listOf(),
        networkInterceptor: List<Interceptor> = listOf()
    ) {
        // okHttpClientBuilder
        val okHttpClientBuilder = OkHttpClient().newBuilder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            connectionPool(ConnectionPool(50, 60 * 5, TimeUnit.SECONDS))
            interceptor.forEach {
                addInterceptor(it)
            }
            networkInterceptor.forEach {
                addNetworkInterceptor(it)
            }
        }

        init(url, okHttpClientBuilder.build())
    }

    fun init(
        url: String,
        okHttpClientBuilder: OkHttpClient
    ) {

        retrofit = Retrofit.Builder().apply {
            baseUrl(url)
            client(okHttpClientBuilder)
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

    /**
     * get ServiceApi
     */
    fun <T> create(service: Class<T>): T = retrofit.create(service)

    fun getBaseUrl(): String {
        return retrofit.baseUrl().toString()
    }

    fun getClient(): Call.Factory {
        return retrofit.callFactory()
    }

}