package com.example.learningenglish.word.list

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.ui.view.TitleBar

class BookWordListActivity : BaseComposeActivity() {

    companion object {
        private const val BOOK_ID = "book_id"
        fun actionView(context: Context, bookId: Long): Intent {
            return Intent(context, BookWordListActivity::class.java)
                .putExtra(BOOK_ID, bookId)
        }
    }

    @Composable
    override fun RenderContent() {

        EnableEdgeLight()

        Column(modifier = Modifier.navigationBarsPadding()) {
            TitleBar(title = "单词本")
            intent.getLongExtra(BOOK_ID, 0).let {
                AllBookWordsScreen(modifier = Modifier, it)
            }
        }

    }


}