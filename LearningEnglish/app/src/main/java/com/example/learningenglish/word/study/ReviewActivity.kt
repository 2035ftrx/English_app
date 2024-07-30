package com.example.learningenglish.word.study

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.learningenglish.http.study.StudyRepository
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.ui.view.TitleBar
import com.example.learningenglish.ui.view.showShotToast
import com.example.learningenglish.word.model.study.ReviewStudyLogic
import com.example.learningenglish.word.plan.StudyPlanRepositoryLocal
import com.example.learningenglish.word.study.screen.StudyPageScreen

class ReviewActivity : BaseComposeActivity() {

    companion object {
        fun actionView(context: Context): Intent {
            return Intent(context, ReviewActivity::class.java)
        }
    }

    @Composable
    override fun RenderContent() {

        EnableEdgeLight()

        Column(modifier = Modifier.navigationBarsPadding()) {
            TitleBar(title = "复习单词")
            val context = LocalContext.current
            val iStudy = remember{
                ReviewStudyLogic(
                    planRepositoryLocal = StudyPlanRepositoryLocal(context),
                    studyRepository = StudyRepository()
                )
            }
            StudyPageScreen(
                modifier = Modifier ,
                iStudyLogic = iStudy,
                onFinish = {
                    showShotToast("恭喜你完成了复习单词")
                    finish()
                }
            )
        }

    }

}