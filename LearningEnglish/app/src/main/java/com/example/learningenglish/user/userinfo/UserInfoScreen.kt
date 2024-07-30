package com.example.learningenglish.user.userinfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.learningenglish.http.user.UserInfoDTO
import com.example.learningenglish.http.user.UserRepository
import com.example.learningenglish.ui.base.CommonLoadingLayout
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.dialog.GetInputDialog
import com.example.learningenglish.ui.dialog.rememberDialogState
import com.example.learningenglish.ui.view.rememberBooleanState
import com.example.learningenglish.ui.view.rememberIntState
import com.example.learningenglish.ui.view.rememberStringState
import com.example.learningenglish.ui.view.showShotToast
import com.example.learningenglish.user.usermanager.AppUserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Preview
@Composable
private fun PreviewUserInfo() {
    UserInfoScreen(
        modifier = Modifier.fillMaxSize(),
        userInfoDTO = UserInfoDTO(
            nickname = "ahahhahah",
            gender = 1,
            school = "xxxx",
            grade = "23f8j"
        ),
        onSave = { nickname, gender, school, grade ->

        }
    )
}


@Composable
fun UserInfoScreen(
    modifier: Modifier = Modifier,
    onSave: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val user = remember { UserRepository() }
    val userInfo = remember {
        mutableStateOf<LoadingData<UserInfoDTO>>(LoadingData.Loading())
    }
    LaunchedEffect(Unit) {
        user.userInfo()
            .onSuccess {
                if (it.isSuccess) {
                    userInfo.value = LoadingData.Success(it.data)
                } else {
                    userInfo.value = LoadingData.Error(it.message)
                }
            }
            .onFailure {
                it.printStackTrace()
                userInfo.value = LoadingData.Error(it.message)
            }
    }
    CommonLoadingLayout(data = userInfo.value) {

        UserInfoScreen(
            modifier = modifier,
            userInfoDTO = it,
            onSave = { nickname, gender, school, grade ->
                coroutineScope.launch(Dispatchers.IO) {

                    user.updateUserInfo(
                        nickname = nickname,
                        gender = gender,
                        school = school,
                        grade = grade
                    )
                        .onSuccess {
                            if (it.isSuccess) {
                                AppUserManager.instance().onUpdateInfo()
                                onSave()
                            } else {
                                withContext(Dispatchers.Main) {
                                    context.showShotToast(it.message)
                                }
                            }
                        }
                        .onFailure {
                            it.printStackTrace()
                            withContext(Dispatchers.Main) {
                                it.message?.let { it1 -> context.showShotToast(it1) }
                            }
                        }
                }
            })

    }
}

@Composable
private fun UserInfoScreen(
    modifier: Modifier = Modifier,
    userInfoDTO: UserInfoDTO,
    onSave: (nickname: String, gender: Int, school: String, grade: String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Column(
            Modifier
                .background(
                    MaterialTheme.colorScheme.tertiaryContainer,
                    RoundedCornerShape(bottomEnd = 60.dp, bottomStart = 60.dp)
                )
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.size(20.dp))
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = "",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.size(50.dp))
        }

        Spacer(modifier = Modifier.size(20.dp))
        Column(modifier = Modifier.padding(16.dp)) {

            val nicknameState = rememberStringState(userInfoDTO.nickname ?: "")
            UserInfoItemInputDialog(
                modifier = Modifier.fillMaxWidth(),
                text = "昵称",
                value = nicknameState.get(),
                onChange = {
                    nicknameState.update(it)
                }
            )
            HorizontalDivider()
            val genderState = rememberIntState(initialValue = userInfoDTO.gender ?: 0)
            UserGenderItem(
                modifier = Modifier.fillMaxWidth(),
                text = "性别",
                value = when (genderState.get()) {
                    0 -> {
                        "女"
                    }

                    1 -> {
                        "男"
                    }

                    else -> {
                        "其它"
                    }
                },
                onChange = {
                    genderState.update(it)
                }
            )
            HorizontalDivider()
            val schoolState = rememberStringState(userInfoDTO.school ?: "")
            UserInfoItemInputDialog(
                modifier = Modifier.fillMaxWidth(),
                text = "学校",
                value = schoolState.get(),
                onChange = {
                    schoolState.update(it)
                }
            )
            HorizontalDivider()
            val gradeState = rememberStringState(userInfoDTO.grade ?: "")
            UserInfoItemInputDialog(
                modifier = Modifier.fillMaxWidth(),
                text = "年级",
                value = gradeState.get(),
                onChange = {
                    gradeState.update(it)
                }
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onSave(
                        nicknameState.get(),
                        genderState.get(),
                        schoolState.get(),
                        gradeState.get()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "保存")
            }

            Spacer(modifier = Modifier.weight(1f))

        }
    }

}


@Composable
fun UserGenderItem(
    modifier: Modifier = Modifier,
    text: String,
    value: String,
    onChange: (Int) -> Unit
) {
    val genderDialog = rememberBooleanState()
    UserInfoItem(modifier, {
        genderDialog.beTrue()
    }, true, text, value)

    if (genderDialog.get()) {
        GenderSelectionDialog(
            onDismiss = { genderDialog.beFalse() },
            onGenderSelected = { gender ->
                onChange(gender)
            }
        )
    }
}

@Composable
fun UserInfoItemInputDialog(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    value: String,
    onChange: (String) -> Unit
) {
    val dialogState = rememberDialogState()
    UserInfoItem(modifier, {
        dialogState.open()
    }, enabled, text, value)
    GetInputDialog(
        dialogState = dialogState,
        title = text,
        hint = "请输入$text",
        defaultInput = value,
        onDismiss = {
            dialogState.close()
        },
        onConfirm = {
            onChange(it)
            dialogState.close()
        })
}

@Composable
private fun UserInfoItem(
    modifier: Modifier,
    onClick: () -> Unit,
    enabled: Boolean,
    text: String,
    value: String
) {
    TextButton(
        onClick = {
            onClick()
        },
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF333333))
    ) {
        Text(text = text)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = value)
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "")
    }
}