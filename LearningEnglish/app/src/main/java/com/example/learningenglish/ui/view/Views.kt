package com.example.learningenglish.ui.view

import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.learningenglish.ui.theme.Typography


val errorTipsTextStyle = Typography.bodySmall.copy(color = Color.Red)

@Composable
fun androidx.activity.ComponentActivity.EnableEdgeTopLightBottomDark() {
    SideEffect {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.GRAY
            ),
            navigationBarStyle =
            SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
    }
}


@Composable
fun androidx.activity.ComponentActivity.EnableEdgeTopDarkBottomLight() {
    SideEffect {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle =
            SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.GRAY)
        )
    }
}

@Composable
fun androidx.activity.ComponentActivity.EnableEdgeLight() {
    SideEffect {
        val barStyle =
            SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.GRAY)
        enableEdgeToEdge(
            statusBarStyle = barStyle,
            navigationBarStyle = barStyle
        )
    }
}

@Composable
fun androidx.activity.ComponentActivity.EnableEdgeDark() {
    SideEffect {
        val barStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        enableEdgeToEdge(
            statusBarStyle = barStyle,
            navigationBarStyle = barStyle
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun androidx.activity.ComponentActivity.TitleBar(title: String) {
    TopAppBar(title = { Text(text = title) }, navigationIcon = {
        TitleBackIconButton{
            finish()
        }
    })
}

@Composable
fun TitleBackIconButton(modifier: Modifier = Modifier,onClick:()->Unit) {
    IconButton(modifier=modifier,onClick = { onClick() }) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
    }
}

@Composable
fun KeepDirectionLayout(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        content()
    }
}

