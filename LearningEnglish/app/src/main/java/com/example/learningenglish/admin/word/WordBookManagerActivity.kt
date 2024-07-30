package com.example.learningenglish.admin.word

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.ui.view.TitleBar

class WordBookManagerActivity : BaseComposeActivity() {

    companion object {
        fun action(context: Context): Intent {
            return Intent(context, WordBookManagerActivity::class.java)
        }
    }

    @Composable
    override fun RenderContent() {

        EnableEdgeLight()

        Column(modifier = Modifier.systemBarsPadding()) {

            TitleBar(title = "管理单词本")
            WordBookListScreen()


        }

    }

}