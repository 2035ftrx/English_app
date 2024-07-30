package com.example.learningenglish.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.learningenglish.R


@Composable
fun LoadingImage(modifier: Modifier = Modifier, model: Any) {
    AsyncImage(
        model = model,
        contentDescription = "",
        placeholder = painterResource(id = R.drawable.img_placeholder),
        error = painterResource(id = R.drawable.img_placeholder),
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}
