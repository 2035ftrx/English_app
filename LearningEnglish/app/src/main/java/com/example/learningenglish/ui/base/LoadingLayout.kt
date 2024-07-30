package com.example.learningenglish.ui.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun <T> CommonLoadingLayout(
    modifier: Modifier = Modifier,
    data: LoadingData<T>,
    content: @Composable (T) -> Unit
) {
    when (data) {
        is LoadingData.Error -> {
            Box(modifier = modifier , contentAlignment = Alignment.Center){
                Text(text = data.getMessage() ?: "")
            }
        }

        is LoadingData.Loading -> {
            Box(modifier = modifier , contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
        }

        is LoadingData.Success -> {
            content(data.getValue())
        }
    }
}