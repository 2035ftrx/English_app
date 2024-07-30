package com.example.learningenglish.user

import android.content.Context
import com.example.learningenglish.http.token.AppUserTokenStorage
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitLoading
import com.example.learningenglish.ui.base.emitSuccess
import com.example.learningenglish.user.usermanager.AppUserInfo
import com.example.learningenglish.user.usermanager.AppUserManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IUserRepositoryDebug(private val context: Context) : IUserRepository {
    override fun login(username: String, password: String): Flow<LoadingData<AppUserInfo>> {
        return flow {
            emitLoading()
            delay(1000)
            AppUserTokenStorage(context.applicationContext).saveToken("it.tokenit.tokenit.tokenit.tokenit.token")
            AppUserManager.instance().updateInfo(AppUserInfo(1))
            emitSuccess(AppUserInfo(1))
        }
    }

    override fun register(
        username: String,
        password: String
    ): Flow<LoadingData<Boolean>> {
        return flow {
            emitLoading()
            delay(1000)
            emitSuccess(true)
        }
    }
}