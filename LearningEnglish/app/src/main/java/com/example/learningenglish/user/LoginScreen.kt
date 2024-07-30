package com.example.learningenglish.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview
@Composable
private fun PreviewAdminLoginScreen() {

    LoginScreen(
        title = "管理员登录",
        showRegister = false,
        onSwitch = {

        },
        onRegister = {

        }) { u, p ->

    }
}

@Preview
@Composable
private fun PreviewLoginScreen() {

    LoginScreen(
        title = "用户登录",
        showRegister = true,
        onSwitch = {

        },
        onRegister = {

        }) { u, p ->

    }
}

@Composable
fun LoginScreen(
    title: String,
    showRegister: Boolean,
    onSwitch: () -> Unit,
    onRegister: () -> Unit,
    onLogin: (String, String) -> Unit
) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.align(Alignment.Center)
            )

            OutlinedButton(
                onClick = { onSwitch() },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text(text = "切换")
            }

        }
        Spacer(modifier = Modifier.size(20.dp))
        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = {
                Text(
                    "用户名",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                )
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(20.dp))
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = {
                Text(
                    "密码",
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                )
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(20.dp))
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            onLogin(username.value, password.value)
        }) {
            Text("登录")
        }
        if (showRegister) {
            TextButton(modifier = Modifier.fillMaxWidth(), onClick = { onRegister() }) {
                Text("注册")
            }
        }
    }
}
