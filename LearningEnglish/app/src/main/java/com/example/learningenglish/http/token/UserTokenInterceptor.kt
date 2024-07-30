package com.example.learningenglish.http.token

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.nio.charset.StandardCharsets


class UserTokenInterceptor(private val userTokenStorage: ITokenStorage) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        addAuthToken(request)
        val build = request.build()
        val proceed = chain.proceed(build)

        Timber.v(" response status code : ${proceed.code}")
        // Clone the response body for logging or other purposes without consuming the original body
        val responseBody = proceed.body

        // Recreate the response before returning it because the body can be read only once
        return proceed.newBuilder()
            .body(responseBody)
            .build()
    }

    private fun addAuthToken(request: Request.Builder) {
        val requestBuild = request.build()
        val isLogin = requestBuild.url.encodedPath.contains("/login/")
        if (!isLogin) {
            userTokenStorage.getToken()?.let {
                request.removeHeader("Authorization")
                request.addHeader("Authorization", "Bearer " + userTokenStorage.getToken())
            }
        }
//        request.removeHeader("Connection")
//        request.addHeader("Connection","Close")
    }

}