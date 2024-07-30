package com.example.learningenglish.word.study

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.learningenglish.http.word.WordRepository
import com.example.learningenglish.http.wordbook.WordBookRepository
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.ui.view.TitleBar
import com.example.learningenglish.ui.view.showShotToast
import com.example.learningenglish.word.model.study.BookStudyLogic
import com.example.learningenglish.word.model.word.IWordRepositoryImpl
import com.example.learningenglish.word.model.wordbook.IWordBookRepositoryImpl
import com.example.learningenglish.word.study.screen.StudyPageScreen

class StudyBookActivity : BaseComposeActivity() {

    companion object {
        private const val WORD_ID = "word_id"
        fun actionView(context: Context, wordId: Long): Intent {
            return Intent(context, StudyBookActivity::class.java)
                .putExtra(WORD_ID, wordId)
        }
    }

    @Composable
    override fun RenderContent() {

        EnableEdgeLight()

        Column(modifier = Modifier.navigationBarsPadding()) {
            TitleBar(title = "学习单词")
            val iStudy = remember {
                BookStudyLogic(
                    wordId = intent.getLongExtra(WORD_ID, 0),
                    wordRepository = IWordRepositoryImpl(WordRepository()),
                    wordBookRepository = IWordBookRepositoryImpl(WordBookRepository()),
                )
            }
            StudyPageScreen(
                modifier = Modifier.fillMaxSize(),
                iStudyLogic = iStudy,
                onFinish = {
                    showShotToast("恭喜你完成了单词本学习")
                    finish()
                }
            )
        }

    }

}