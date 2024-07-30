package com.example.learningenglish.aichat

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.ui.view.TitleBar

class AIChatListActivity : BaseComposeActivity() {

    companion object {
        fun start(context: Context): Intent {
            val intent = Intent(context, AIChatListActivity::class.java)
            return intent
        }
    }

    @Composable
    override fun RenderContent() {
        EnableEdgeLight()
        Column {
            TitleBar(title = "AI机器人对话历史记录")
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    startActivity(AIChatActivity.start(this@AIChatListActivity))
                }) {
                Text(
                    text = "开始新的对话",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            AIChatListScreen(modifier = Modifier.fillMaxSize())
        }
    }


}