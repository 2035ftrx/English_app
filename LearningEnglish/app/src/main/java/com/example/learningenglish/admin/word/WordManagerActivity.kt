package com.example.learningenglish.admin.word

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.ui.view.TitleBar

class WordManagerActivity : BaseComposeActivity() {

    companion object {
        private const val WORD_BOOK_ID = "word_book_id"
        fun action(context: Context, wordBookId: Long): Intent {
            return Intent(context, WordManagerActivity::class.java)
                .putExtra(WORD_BOOK_ID, wordBookId)
        }
    }

    @Composable
    override fun RenderContent() {

        EnableEdgeLight()

        Column(modifier = Modifier.systemBarsPadding()) {
            TitleBar(title = "管理单词")
            intent.getLongExtra(WORD_BOOK_ID, 0).let {
                WordListScreen(it)
            }
        }

    }

}