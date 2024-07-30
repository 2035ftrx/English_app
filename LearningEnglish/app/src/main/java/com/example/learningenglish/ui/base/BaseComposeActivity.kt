package com.example.learningenglish.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.example.learningenglish.ui.theme.AppTheme
import timber.log.Timber


abstract class BaseComposeActivity : ComponentActivity() {

    protected val context by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d(" onCreate ${this.localClassName}")
        enableEdgeToEdge()
        setContent {
            AppTheme {
                RenderContent()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d(" onDestroy ${this.localClassName}")
    }

    @Composable
    abstract fun RenderContent()

}