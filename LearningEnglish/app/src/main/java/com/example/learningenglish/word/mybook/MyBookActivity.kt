package com.example.learningenglish.word.mybook

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.ui.view.TitleBar

class MyBookActivity : BaseComposeActivity() {

    companion object {
        fun actionView(context: Context): Intent {
            return Intent(context, MyBookActivity::class.java)
        }
    }

    @Composable
    override fun RenderContent() {

        EnableEdgeLight()

        Scaffold(
            topBar = {
                TitleBar(title = "我的单词本")
            }
        ) {
            MyBookScreen(modifier = Modifier.padding(it))
        }

    }


}