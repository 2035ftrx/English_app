package com.example.learningenglish

import android.app.Application
import com.example.learningenglish.http.ApiWrapper
import com.example.learningenglish.http.token.AppUserTokenStorage
import com.example.learningenglish.http.token.UserTokenInterceptor
import com.example.learningenglish.user.usermanager.AppUserInfo
import com.example.learningenglish.user.usermanager.AppUserInfoStorage
import com.example.learningenglish.user.usermanager.AppUserManager
import com.example.learningenglish.user.usermanager.IUserInfoStorage
import com.example.learningenglish.user.usermanager.UserState
import com.example.learningenglish.utils.BuildParams
import com.example.learningenglish.utils.IBuildParams
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        registerUserInfoStorage()
        initHttp()
        injectBuildParams()
    }

    private fun injectBuildParams() {
        BuildParams.injectionParams(object : IBuildParams {
            override fun buildType(): String {
                return BuildConfig.BuildType
            }
        })
    }

    private fun initHttp() {
        ApiWrapper.init(
            "http://192.168.0.108:8099",// 局域网地址。端口不用改。
            OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(UserTokenInterceptor(AppUserTokenStorage(applicationContext)))
                .build()
        )
    }

    private fun registerUserInfoStorage() {
        val appUserInfoStorage: IUserInfoStorage<AppUserInfo> = AppUserInfoStorage(this)
        GlobalScope.launch {
            Timber.i("app launch user state : ${appUserInfoStorage.checkUserInfoLogin()}")
            appUserInfoStorage.getUserInfo().first()?.let {
                AppUserManager.instance().updateInfo(it)
            }
            AppUserManager.instance().subUserInfoState()
                .onEach {
                    Timber.v("user state change 2 : $it")
                    when (it) {
                        is UserState.Logged -> {
                            Timber.v(" before save info : ${it.userInfo} ")
                            appUserInfoStorage.saveUserInfo(it.userInfo)
                        }

                        is UserState.Logout -> {
                            val deleteUserInfo = appUserInfoStorage.deleteUserInfo()
                            Timber.v(" delete user info : $deleteUserInfo")
                            Timber.v(
                                " after delete info : " + appUserInfoStorage.getUserInfo().first()
                            )
                        }
                    }
                }
                .collect {
                    Timber.v("user state change : $it")
                }
        }
    }

}