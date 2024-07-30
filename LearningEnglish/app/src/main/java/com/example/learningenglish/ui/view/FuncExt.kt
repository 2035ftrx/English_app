package com.example.learningenglish.ui.view

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat

fun ComponentActivity.startActivity(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}

fun Context.shotToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun Context.showShotToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

@Composable
fun rememberDrawablePainter(
    @DrawableRes id: Int,
    @ColorRes tintColorRes: Int? = null,
    context: Context = LocalContext.current
): Painter {
    val tintColor = tintColorRes?.let { ContextCompat.getColor(context, it) }
    val drawable = ContextCompat.getDrawable(context, id)
    tintColor?.let { drawable?.setTint(it) }
    return painterResource(id = id)
}