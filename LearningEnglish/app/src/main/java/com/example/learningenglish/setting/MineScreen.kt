package com.example.learningenglish.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningenglish.http.user.UserRepository
import com.example.learningenglish.ui.theme.AppTheme
import com.example.learningenglish.user.userinfo.UserInfoActivity
import com.example.learningenglish.user.usermanager.AppUserManager
import com.example.learningenglish.user.usermanager.UserState
import kotlinx.coroutines.launch
import timber.log.Timber


@Preview
@Composable
private fun PreviewUserInfo() {
    AppTheme {
        UserInfoScreen(modifier = Modifier.fillMaxSize(), username = "ahahhahah") {
//        context.startActivity(Intent(context, LoginActivity::class.java))
//        finish()
        }
    }
}


@Composable
fun MineScreen(modifier: Modifier = Modifier, onLogout: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val username = remember { mutableStateOf("") }
    LaunchedEffect(key1 = Unit) {
        AppUserManager.instance().subUserInfoState().collect { userState ->
            Timber.d(" user state : $userState ")
            when (userState) {
                is UserState.Logged -> {
                    val userRepository = UserRepository()
                    userRepository.userInfo()
                        .onSuccess {
                            if (it.isSuccess) {
                                username.value = it.data.nickname ?: userState.userInfo.username
                            } else {
                                username.value = it.message
                            }
                        }
                        .onFailure {
                            it.printStackTrace()
                            username.value = it.message ?: ""
                        }
                }

                is UserState.Logout -> {
                    username.value = "未登录"
                }
            }
        }
    }
    UserInfoScreen(modifier = modifier, username = username.value) {
        coroutineScope.launch { AppUserManager.instance().logout() }
        onLogout()
    }
}

@Composable
private fun UserInfoScreen(modifier: Modifier = Modifier, username: String, onLogout: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.background(MaterialTheme.colorScheme.surface)
    ) {
        val context = LocalContext.current

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Spacer(modifier = Modifier.size(20.dp))
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.size(20.dp))
            Text(text = username)
            Spacer(modifier = Modifier.size(50.dp))

        }
        Spacer(modifier = Modifier.size(50.dp))
        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(topEnd = 100f, topStart = 100f))
                .padding(30.dp)
                .fillMaxHeight()
        ) {

            DividerLine()
            TextButton(
                onClick = { context.startActivity(UserInfoActivity.start(context)) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "个人信息")
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = ""
                )
            }
            DividerLine()

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = { onLogout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red, shape = RoundedCornerShape(10.dp))
            ) {
                Text(text = "退出账号", color = Color.White)
            }
            Spacer(modifier = Modifier.weight(2f))
        }


    }

}

@Composable
private fun DividerLine() {
    Spacer(
        modifier = Modifier
            .height(0.5.dp)
            .fillMaxWidth()
            .background(Color.LightGray)
    )
}