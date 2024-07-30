package com.example.learningenglish.admin.main

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.learningenglish.admin.user.UserManagerActivity
import com.example.learningenglish.admin.word.WordBookManagerActivity
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.ui.view.ViewSpacer
import com.example.learningenglish.user.LauncherActivity
import com.example.learningenglish.user.usermanager.AppUserManager

class AdminMainActivity : BaseComposeActivity() {

    @Composable
    override fun RenderContent() {

        EnableEdgeLight()

        Column(
            modifier = Modifier
                .systemBarsPadding()
                .padding(50.dp)
        ) {

            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                startActivity(UserManagerActivity.action(context))
            }) { Text(text = "管理用户") }

            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                startActivity(WordBookManagerActivity.action(context))
            }) { Text(text = "管理单词本") }

            ViewSpacer(size = 50)

            TextButton(
                onClick = {
                    AppUserManager.instance().logout()
                    context.startActivity(Intent(context, LauncherActivity::class.java))
                    finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red, shape = RoundedCornerShape(10.dp))
            ) {
                Text(text = "退出账号", color = Color.White)
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        AppUserManager.instance().logout()
    }
}