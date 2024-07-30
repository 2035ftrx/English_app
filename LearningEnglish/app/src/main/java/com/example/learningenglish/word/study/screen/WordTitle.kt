package com.example.learningenglish.word.study.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningenglish.ui.view.LoopingVibratingSpeakerIcon
import com.example.learningenglish.ui.view.ViewSpacer
import com.example.learningenglish.ui.view.rememberBooleanState
import com.example.learningenglish.word.model.WordUI

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordTitle(modifier: Modifier = Modifier, wordUI: WordUI) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = wordUI.word,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            modifier = Modifier,
            textAlign = TextAlign.Center
        )

        ViewSpacer(size = 16)

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            wordUI.details.phonetic.uk?.let {
                WordPhoneticSymbols(modifier = Modifier, "英 $it") {
                    // todo play sound
                }
            }
            wordUI.details.phonetic.us?.let {
                WordPhoneticSymbols(modifier = Modifier, "美 $it") {
                    // todo play sound
                }
            }
        }

    }
}

@Composable
private fun WordPhoneticSymbols(
    modifier: Modifier = Modifier,
    phonetic: String,
    onPlay: () -> Unit
) {

    val playState = rememberBooleanState()

    Row(
        modifier = modifier
            .clickable { playState.beTrue();onPlay() }
            .background(Color.LightGray.copy(0.3f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 3.dp)
            ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = phonetic, fontSize = 12.sp, color = Color.Gray)
        ViewSpacer(size = 6)
        LoopingVibratingSpeakerIcon(isPlaying = playState.get())
    }

    LaunchedEffect(playState.get()) {
        if (playState.get()) {
            kotlinx.coroutines.delay(2000)
            playState.beFalse()
        }
    }

}