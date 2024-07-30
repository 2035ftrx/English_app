package com.example.learningenglish.admin.user

import com.example.learningenglish.http.admin.AdminUserRepository
import com.example.learningenglish.ui.base.BaseViewModel
import com.example.learningenglish.ui.base.flatMapResultFlow
import com.example.learningenglish.ui.base.paging.GenericPagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserViewModel : BaseViewModel() {
    private val userRepository = AdminUserRepository() // 假设你有一个UserRepository来处理数据操作

    val userPagingFlow = GenericPagingRepository { page, size ->
        val allUsers = userRepository.getAllUsers(page, size)
        if (allUsers.isSuccess) {
            if (allUsers.getOrThrow().isSuccess) {
                allUsers.getOrThrow().data
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
    }.getPagingData()

    // 其他用户操作方法
    fun createUser(username: String, password: String): Flow<Any> {
        return flow {
            userRepository.createUser(username, password)
                .flatMapResultFlow { it }
                .collect {
                    emit(it)
                }
        }
    }

    fun updateUser(
        userId: Long,
        nickname: String, school: String, gender: Int, grade: String
    ): Flow<Any> {
        return flow {
            userRepository.updateUser(userId, nickname, school, gender, grade)
                .flatMapResultFlow { it }
                .collect {
                    emit(it)
                }
        }
    }

    fun deleteUser(userId: Long): Flow<Any> {
        return flow {
            userRepository.deleteUser(userId)
                .flatMapResultFlow { it }
                .collect {
                    emit(it)
                }
        }
    }
}