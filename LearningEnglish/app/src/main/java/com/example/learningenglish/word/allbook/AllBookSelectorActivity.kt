package com.example.learningenglish.word.allbook

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.ui.view.TitleBar

class AllBookSelectorActivity : BaseComposeActivity() {

    companion object {
        fun actionView(context: Context): Intent {
            return Intent(context, AllBookSelectorActivity::class.java)
        }
    }

    @Composable
    override fun RenderContent() {

        EnableEdgeLight()

        Column(modifier = Modifier.navigationBarsPadding()) {

            TitleBar(title = "添加单词本")

            AllBookScreen(modifier = Modifier)

        }

    }


}