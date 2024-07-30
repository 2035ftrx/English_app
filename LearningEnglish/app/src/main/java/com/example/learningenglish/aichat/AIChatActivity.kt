package com.example.learningenglish.aichat

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.learningenglish.aichat.worker.ChatAudioInstance
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.ui.view.TitleBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AIChatActivity : BaseComposeActivity() {

    companion object {
        private const val CONVERSATION_ID = "conversation_id"
        fun start(context: Context, conversationId: Long): Intent {
            val intent = Intent(context, AIChatActivity::class.java)
            intent.putExtra(CONVERSATION_ID, conversationId)
            return intent
        }

        fun start(context: Context): Intent {
            val intent = Intent(context, AIChatActivity::class.java)
            return intent
        }
    }

    @Composable
    override fun RenderContent() {
        EnableEdgeLight()
        LaunchedEffect(key1 = Unit) {
            withContext(Dispatchers.IO) {
                ChatAudioInstance.refreshToken()
            }
        }
        Column(modifier = Modifier.systemBarsPadding().imePadding()) {
            Box(modifier = Modifier.fillMaxWidth() ){
                TitleBar(title = "AI机器人")
                IconButton(onClick = {
                    startActivity(AIConversationListActivity.start(context))
                }, modifier = Modifier.align(Alignment.BottomEnd)) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "")
                }
            }
            if (intent.hasExtra(CONVERSATION_ID)) {
                val conversationId = intent.getLongExtra(CONVERSATION_ID, 0)
                AIConversationChatScreen(conversationId = conversationId)
            } else {
                AIChatScreen()
            }
        }

    }


}