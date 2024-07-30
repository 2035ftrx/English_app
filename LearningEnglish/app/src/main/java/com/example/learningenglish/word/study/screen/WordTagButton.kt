package com.example.learningenglish.word.study.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.learningenglish.word.model.IWordStatus

@Composable
fun WordTagButton(
    modifier: Modifier = Modifier,
    onResult: (IWordStatus) -> Unit = {},
) {

    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        LargeFloatingActionButton(onClick = {
            onResult(IWordStatus.Learned)
        }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "😀", fontSize = 32.sp)
                Text(text = "学会了", fontSize = 18.sp)
            }
        }
        LargeFloatingActionButton(onClick = {
            onResult(IWordStatus.Blurred)
        }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "❓", fontSize = 32.sp)
                Text(text = "模糊", fontSize = 18.sp)
            }
        }
        LargeFloatingActionButton(onClick = {
            onResult(IWordStatus.Unknown)
        }) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "😳", fontSize = 32.sp)
                Text(text = "不认识", fontSize = 18.sp)
            }
        }
    }

}
