package com.example.learningenglish.admin.user

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.ui.view.TitleBar
import kotlinx.coroutines.launch
import timber.log.Timber

class UserManagerActivity : BaseComposeActivity() {

    companion object {
        fun action(context: Context): Intent {
            return Intent(context, UserManagerActivity::class.java)
        }
    }

    @Composable
    override fun RenderContent() {

        EnableEdgeLight()

        Column {

            TitleBar(title = "用户管理")
            val viewModel: UserViewModel = viewModel()
            val coroutineScope = rememberCoroutineScope()
            val pagingFlow = viewModel.userPagingFlow.collectAsLazyPagingItems()
            UserListScreen(pagingFlow, onCreateUser = { username, password ->
                coroutineScope.launch {
                    viewModel.createUser(username, password).collect {
                        pagingFlow.refresh()
                    }
                }
            }, onEditUser = {
                coroutineScope.launch {
                    Timber.d(" update user before : $it ")
                    viewModel.updateUser(
                        it.id, it.nickname, it.school ?: "", it.gender, it.grade ?: ""
                    ).collect {
                        pagingFlow.refresh()
                    }
                }
            }, onDeleteUser = {
                coroutineScope.launch {
                    viewModel.deleteUser(it.id).collect {
                        pagingFlow.refresh()
                    }
                }
            })

        }

    }

}