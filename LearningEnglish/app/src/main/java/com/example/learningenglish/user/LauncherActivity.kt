package com.example.learningenglish.user

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.learningenglish.R
import com.example.learningenglish.admin.main.AdminMainActivity
import com.example.learningenglish.http.admin.AdminUserRepository
import com.example.learningenglish.http.admin.toAppUserInfo
import com.example.learningenglish.http.token.AppUserTokenStorage
import com.example.learningenglish.http.user.UserRepository
import com.example.learningenglish.main.MainActivity
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.view.shotToast
import com.example.learningenglish.ui.view.showShotToast
import com.example.learningenglish.user.usermanager.AppUserInfo
import com.example.learningenglish.user.usermanager.AppUserInfoStorage
import com.example.learningenglish.user.usermanager.AppUserManager
import com.example.learningenglish.utils.BuildParams
import com.example.learningenglish.utils.BuildType
import kotlinx.coroutines.launch

class LauncherActivity : BaseComposeActivity() {

    @Composable
    override fun RenderContent() {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer)) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .weight(6f)
                    .background(Color.White, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
            ) {
                Content()
            }
        }

    }

    @Composable
    fun Content(modifier: Modifier = Modifier) {

        val context = LocalContext.current.applicationContext
        // A surface container using the 'background' color from the theme
        val userRepository: IUserRepository = remember {
            IUserRepositoryRemote(context, UserRepository())
        }
        val iLoginScreen = remember { mutableStateOf<ILoginScreen>(ILoginScreen.Login) }
        val coroutineScope = rememberCoroutineScope()
        val userStorage = remember { AppUserInfoStorage(context.applicationContext) }
        val login = userStorage.checkUserInfoLogin().collectAsState(initial = null)
        when (login.value) {
            null -> {
                CircularProgressIndicator()
            }

            true -> {
                val appUserInfo = userStorage.getUserInfo().collectAsState(initial = null).value
                if (appUserInfo?.role == 3) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else if (appUserInfo?.role == 2) {
                    startActivity(Intent(this, AdminMainActivity::class.java))
                    finish()
                }
            }

            false -> {
                when (iLoginScreen.value) {
                    ILoginScreen.AdminLogin ->
                        LoginScreen(
                            title = "管理员登录",
                            showRegister = false,
                            onSwitch = { iLoginScreen.value = ILoginScreen.Login },
                            onRegister = { },
                            onLogin = { username, password ->
                                coroutineScope.launch {
                                    adminLogin(username, password, userStorage)
                                }
                            })

                    ILoginScreen.Login ->
                        LoginScreen(
                            title = "用户登录",
                            showRegister = true,
                            onSwitch = { iLoginScreen.value = ILoginScreen.AdminLogin },
                            onRegister = { iLoginScreen.value = ILoginScreen.Register },
                            onLogin = { username, password ->
                                userLogin(
                                    userRepository, username, password, userStorage
                                )

                            })

                    ILoginScreen.Register -> {
                        RegisterScreen({ username, password ->
                            coroutineScope.launch {
                                // 注册逻辑
                                userRepository.register(username, password).collect {
                                    when (it) {
                                        is LoadingData.Error -> {
                                            shotToast(context, it.getMessageNonNull("注册失败"))
                                        }

                                        is LoadingData.Loading -> {
                                            // shotToast(context, "登录中")
                                        }

                                        is LoadingData.Success -> {
                                            shotToast(context, "注册成功")
                                            iLoginScreen.value = ILoginScreen.Login
                                        }
                                    }
                                }
                            }
                        }, {
                            iLoginScreen.value = ILoginScreen.Login
                        })
                    }
                }
            }

        }

    }

    private fun userLogin(
        userRepository: IUserRepository,
        username: String,
        password: String,
        userStorage: AppUserInfoStorage
    ) {
        lifecycleScope.launch {
            // 登录逻辑
            userRepository.login(username, password).collect {
                when (it) {
                    is LoadingData.Error -> {
                        shotToast(context, it.getMessageNonNull("登录失败"))
                    }

                    is LoadingData.Loading -> {
                        shotToast(context, "登录中")
                    }

                    is LoadingData.Success -> {
                        AppUserManager.instance().updateInfo(it.getValue())
                        userStorage.saveUserInfo(it.getValue())
                        startActivity(
                            Intent(context, MainActivity::class.java)
                        )
                        finish()
                    }
                }
            }
        }
    }

    private suspend fun adminLogin(
        username: String,
        password: String,
        userStorage: AppUserInfoStorage
    ) {
        val adminUserRepository = AdminUserRepository()
        // 登录逻辑
        adminUserRepository.login(username, password)
            .onSuccess {
                it.data?.let {
                    AppUserTokenStorage(context.applicationContext).saveToken(it.token)
                    AppUserManager.instance()                        .updateInfo(it.user.toAppUserInfo())
                    userStorage.saveUserInfo(it.user.toAppUserInfo())
                    startActivity(Intent(this@LauncherActivity, AdminMainActivity::class.java))
                    finish()
                }
            }
            .onFailure {
                it.message?.let { showShotToast(it) }
                it.printStackTrace()
            }
    }
}
