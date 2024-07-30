package com.example.learningenglish.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningenglish.R
import com.example.learningenglish.aichat.AIConversationListActivity

@Preview
@Composable
private fun PreviewHomeScreen() {
    HomeScreen(modifier = Modifier.fillMaxSize())
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {

        Column(modifier = modifier) {

            TodayTaskScreen()

            HomeWordBookScreen()

        }

        val context = LocalContext.current

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { context.startActivity(AIConversationListActivity.start(context)) }) {
            Icon(painter = painterResource(id = R.drawable.ic_robot_2_24), contentDescription = "")
        }

    }

}