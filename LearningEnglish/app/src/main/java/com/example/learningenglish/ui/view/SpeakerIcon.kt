package com.example.learningenglish.ui.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.learningenglish.R

@Composable
fun LoopingVibratingSpeakerIcon(isPlaying: Boolean) {
    val scaleAnim by remember {
        mutableStateOf(Animatable(1f))
    }

    LaunchedEffect(key1 = isPlaying) {
        if (isPlaying) {
            scaleAnim.animateTo(
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 600),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            scaleAnim.snapTo(1f) // 当播放停止时，迅速回到原始大小
        }
    }

    Image(
        painter = painterResource(id = R.drawable.baseline_campaign_24),
        contentDescription = "Speaker Icon",
        modifier = Modifier
            .scale(scaleAnim.value)
    )
}

@Composable
private fun VibratingSpeakerIcon(isPlaying: Boolean) {
    val scale by animateFloatAsState(
        targetValue = if (isPlaying) 1.2f else 1f,
        animationSpec = SpringSpec(dampingRatio = 0.5f, stiffness = 500f)
    )

    Image(
        painter = painterResource(id = R.drawable.baseline_campaign_24),
        contentDescription = "Speaker Icon",
        modifier = Modifier
            .size(64.dp)
            .scale(scale)
    )
}

@Composable
fun SpeakerIcon() {
    var isPlaying by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        LoopingVibratingSpeakerIcon(isPlaying)

        Button(onClick = { isPlaying = !isPlaying }) {
            Text(text = if (isPlaying) "停止播放" else "开始播放")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSpeakerControl() {
    SpeakerIcon()
}