package com.example.learningenglish.user.userinfo

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.TitleBar
import com.example.learningenglish.user.usermanager.AppUserManager
import com.example.learningenglish.user.usermanager.UserState

class UserInfoActivity : BaseComposeActivity() {

    companion object {
        fun start(context: Context): Intent {
            val intent = Intent(context, UserInfoActivity::class.java)
            return intent
        }
    }

    @Composable
    override fun RenderContent() {

        val userState =
            AppUserManager.instance().subUserInfoState().collectAsState(initial = null).value

        Column {
            TitleBar(title = "个人信息")
            when (userState) {
                is UserState.Logged -> {
                    userState.userInfo.let {
                        UserInfoScreen(
                            modifier = Modifier.fillMaxSize(),
                            onSave = {
                                finish()
                            }
                        )
                    }
                }

                is UserState.Logout -> {
                    Text(text = "未登录")
                }

                null -> {
                    CircularProgressIndicator()
                }
            }
        }

    }

}
