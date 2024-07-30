package com.example.learningenglish.aichat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.example.learningenglish.R
import com.example.learningenglish.aichat.database.AIChatConversation
import com.example.learningenglish.aichat.database.AIChatDatabase
import com.example.learningenglish.aichat.model.ChatRecordUI
import com.example.learningenglish.aichat.model.IChatRecordStatus
import com.example.learningenglish.aichat.model.IChatRole
import com.example.learningenglish.aichat.model.toUI
import com.example.learningenglish.aichat.worker.ChatAudioInstance
import com.example.learningenglish.aichat.worker.IAudioPlayerState
import com.example.learningenglish.aichat.worker.SendMessageWorker
import com.example.learningenglish.test.testChatRecordData
import com.example.learningenglish.ui.base.paging.GenericPagingRepositorySource
import com.example.learningenglish.ui.view.ViewSpacer
import com.example.learningenglish.ui.view.rememberBooleanState
import com.example.learningenglish.ui.view.rememberStringState
import com.example.learningenglish.user.usermanager.AppUserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber


@Preview
@Composable
private fun PreviewAiChat() {


    val collectAsLazyPagingItems =
        flow { emit(PagingData.from(testChatRecordData)) }.collectAsLazyPagingItems()

    ConversationContentScreen(
        modifier = Modifier.fillMaxSize(),
        pagingItems = collectAsLazyPagingItems,
        sendMessage = { msg ->

        }
    )

}


@Composable
fun AIChatScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val conversationId = remember { mutableLongStateOf(0L) }
    LaunchedEffect(key1 = Unit) {
        val conversationDao = AIChatDatabase.getInstance(context).aiChatConversationDao()
        val userId = AppUserManager.instance().userId()
        conversationDao.insert(
            AIChatConversation(
                userId = userId,
                title = "新建会话" + System.currentTimeMillis(),
                createdAt = System.currentTimeMillis()
            )
        ).let {
            conversationId.longValue = it
        }
    }
    if (conversationId.longValue != 0L) {
        AIConversationChatScreen(modifier = modifier, conversationId = conversationId.longValue)
    } else {
        CircularProgressIndicator()
    }
}

@Composable
fun AIConversationChatScreen(modifier: Modifier = Modifier, conversationId: Long) {

    val context = LocalContext.current

    val paging = remember {
        val dao = AIChatDatabase.getInstance(context).aiChatRecordDao()
        GenericPagingRepositorySource { dao.getAllByPage(conversationId) }
            .getPagingData()
            .map { it.map { it.toUI() } }
    }

    val pagingItems = paging.collectAsLazyPagingItems()

    ConversationContentScreen(
        modifier = modifier,
        pagingItems = pagingItems,
        sendMessage = { msg ->
            SendMessageWorker.action(context, conversationId, msg)
        }
    )

}

@Composable
private fun ConversationContentScreen(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<ChatRecordUI>,
    sendMessage: (String) -> Unit
) {
    val lazyListState = rememberLazyListState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState,
            reverseLayout = true
        ) {
            items(pagingItems.itemCount) {
                pagingItems[it]?.let { record ->
                    ChatItem(record)
                }
            }
        }

        HorizontalDivider()
        val canSend = try {
            pagingItems[0]
                ?.let { it.role == IChatRole.Assistant && it.status == IChatRecordStatus.Success }
                ?: true
        } catch (e: Exception) {
            true
        }
        ChatInputLayout(sendMessage, canSend)
    }
}

@Composable
private fun ChatItem(record: ChatRecordUI) {
    Column(modifier = Modifier.fillMaxWidth()) {
        when (record.role) {
            IChatRole.Assistant -> {
                Row(modifier = Modifier.align(Alignment.Start)) {
                    Column(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp,
                                    bottomStart = 3.dp,
                                    bottomEnd = 20.dp
                                )
                            )
                            .padding(16.dp)
                    ) {
                        when (record.status) {
                            IChatRecordStatus.Failure -> {
                                Text(
                                    text = "抱歉，暂时无法回复",
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                )
                            }

                            IChatRecordStatus.Loading -> {
                                Text(
                                    text = "正在努力思考中...",
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            IChatRecordStatus.Success -> {
                                Text(
                                    text = record.msg,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                HorizontalDivider()
                                Row {
                                    Text(
                                        text = record.time,
                                        color = Color.LightGray,
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                    ViewSpacer(size = 16)
                                    AudioButton(record)
                                }
                            }
                        }
                    }
                    ViewSpacer(size = 30)
                }
            }

            IChatRole.User -> {
                Row(modifier = Modifier.align(Alignment.End)) {
                    ViewSpacer(size = 30)
                    Column(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp,
                                    bottomEnd = 3.dp,
                                    bottomStart = 20.dp
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = record.msg,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = record.time,
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AudioButton(record: ChatRecordUI) {
    val coroutineScope = rememberCoroutineScope()
    val clicked = rememberBooleanState()
    IconButton(modifier = Modifier.size(16.dp), onClick = {
        if (clicked.isFalse()) {
            clicked.beTrue()
            coroutineScope.launch(Dispatchers.IO) {
                ChatAudioInstance.play(record.msg)
            }
        }
    }) {
        if (clicked.isTrue()) {

            val audioState = ChatAudioInstance.audioState.collectAsState().value
            Timber.d(" audio state : $audioState ")
            when (audioState) {
                IAudioPlayerState.Completed -> {
                    clicked.beFalse()
                    Icon(
                        painter = painterResource(id = R.drawable.outline_play_circle_outline_24),
                        contentDescription = ""
                    )
                }

                IAudioPlayerState.Error -> {
                    clicked.beFalse()
                    Text(text = "播放失败", fontSize = 12.sp, color = Color.Gray)
                }

                IAudioPlayerState.Idle -> {
                    clicked.beFalse()
                    Icon(
                        painter = painterResource(id = R.drawable.outline_play_circle_outline_24),
                        contentDescription = ""
                    )
                }

                IAudioPlayerState.Loading -> {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_stop_circle_24),
                        contentDescription = ""
                    )
                }

                IAudioPlayerState.Paused -> {
                    clicked.beFalse()
                    Icon(
                        painter = painterResource(id = R.drawable.outline_play_circle_outline_24),
                        contentDescription = ""
                    )
                }

                IAudioPlayerState.Playing -> {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_stop_circle_24),
                        contentDescription = ""
                    )
                }

                IAudioPlayerState.Stopped -> {
                    clicked.beFalse()
                    Icon(
                        painter = painterResource(id = R.drawable.outline_play_circle_outline_24),
                        contentDescription = ""
                    )
                }
            }
        } else {
            Icon(
                painter = painterResource(id = R.drawable.outline_play_circle_outline_24),
                contentDescription = ""
            )
        }
    }


}


@Composable
private fun ChatInputLayout(sendMessage: (String) -> Unit, enableSend: Boolean = true) {
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        val userInput = rememberStringState()
        OutlinedTextField(
            value = userInput.get(),
            onValueChange = { userInput.update(it) },
            modifier = Modifier.weight(1f),
            shape = CircleShape
        )
        ViewSpacer(size = 8)
        Button(onClick = {
            sendMessage(userInput.get())
            userInput.update("")
        }, enabled = userInput.get().isNotEmpty() && enableSend) {
            Text("Send")
        }
    }
}
