package com.example.learningenglish.aichat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.example.learningenglish.aichat.database.AIChatDatabase
import com.example.learningenglish.aichat.model.ChatConversationUI
import com.example.learningenglish.aichat.model.toUI
import com.example.learningenglish.test.testChatConversationData
import com.example.learningenglish.ui.base.paging.GenericPagingRepositorySource
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


@Preview
@Composable
private fun PreviewAiChat() {


    val collectAsLazyPagingItems =
        flow { emit(PagingData.from(testChatConversationData)) }.collectAsLazyPagingItems()

    ConversationScreen(
        modifier = Modifier.fillMaxSize(),
        pagingItems = collectAsLazyPagingItems,
        onClick = { msg ->

        }
    )
}

@Composable
fun AIChatListScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val paging = remember {
        val conversationDao = AIChatDatabase.getInstance(context).aiChatConversationDao()
        GenericPagingRepositorySource { conversationDao.getAllByPage() }
            .getPagingData()
            .map { it.map { it.toUI() } }
    }

    val pagingItems = paging.collectAsLazyPagingItems()

    ConversationScreen(modifier, pagingItems) {
        context.startActivity(AIChatActivity.start(context, it.id))
    }

}

@Composable
private fun ConversationScreen(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<ChatConversationUI>,
    onClick: (ChatConversationUI) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pagingItems.itemCount) {
            pagingItems[it]?.let { conversation ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onClick(conversation)
                        },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {

                        Text(
                            text = conversation.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(text = conversation.time, color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
    }

}


