package com.example.learningenglish.user

import android.content.Context
import com.example.learningenglish.http.token.AppUserTokenStorage
import com.example.learningenglish.http.user.UserRepository
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitError
import com.example.learningenglish.ui.base.emitLoading
import com.example.learningenglish.ui.base.emitSuccess
import com.example.learningenglish.user.usermanager.AppUserInfo
import com.example.learningenglish.user.usermanager.AppUserManager
import com.example.learningenglish.user.usermanager.toAppUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class IUserRepositoryRemote(
    private val context: Context,
    private val userRepository: UserRepository
) : IUserRepository {
    override fun login(username: String, password: String): Flow<LoadingData<AppUserInfo>> {
        return flow {
            emitLoading()
            userRepository.login(username, password)
                .onSuccess {
                    if (it.isSuccess) {
                        it.data?.let {
                            AppUserTokenStorage(context.applicationContext).saveToken(it.token)
                            it.user.toAppUserInfo().apply {
                                Timber.d(" user info : $this")
                                AppUserManager.instance().updateInfo(this)
                                emitSuccess(this)
                            }
                        } ?: let {
                            emitError("数据错误，data 为空！")
                        }
                    } else {
                        emitError(it.message)
                    }
                }
                .onFailure {
                    it.printStackTrace()
                    it.message?.let { it1 -> emitError(it1) }
                }
        }
    }

    override fun register(
        username: String,
        password: String
    ): Flow<LoadingData<Boolean>> {
        return flow {
            // 注册逻辑
            userRepository.register(username, password)
                .onSuccess {
                    if (it.isSuccess) {
                        emitSuccess(it.isSuccess)
                    } else {
                        emitError(it.message)
                    }
                }
                .onFailure {
                    it.printStackTrace()
                    it.message?.let { it1 -> emitError(it1) }
                }
        }
    }
}