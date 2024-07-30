package com.example.learningenglish.user

import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitError
import com.example.learningenglish.ui.base.emitLoading
import com.example.learningenglish.ui.base.emitSuccess
import com.example.learningenglish.user.usermanager.AppUserInfo
import com.example.learningenglish.user.usermanager.toAppUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class IUserRepositoryLocal(private val userRepository: UserRepository) : IUserRepository {
    override fun login(username: String, password: String): Flow<LoadingData<AppUserInfo>> {
        return flow {
            emitLoading()
            val user = userRepository.login(username, password)
            user?.let {
                emitSuccess(it.toAppUserInfo())
            } ?: let {
                emitError("登录失败，账号名或密码错误！")
            }
        }
    }

    override fun register(
        username: String,
        password: String
    ): Flow<LoadingData<Boolean>> {
        return flow {
            emitLoading()
            val register = userRepository.register(username, password)
            if (register > 0) {
                emitSuccess(true)
            } else {
                emitSuccess(false)
            }
        }
    }
}