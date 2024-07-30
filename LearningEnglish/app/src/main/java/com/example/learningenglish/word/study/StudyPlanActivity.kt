package com.example.learningenglish.word.study

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.learningenglish.word.model.study.PlanStudyLogic
import com.example.learningenglish.word.plan.StudyPlanRepositoryLocal
import com.example.learningenglish.word.study.screen.StudyPageScreen

class StudyPlanActivity : BaseComposeActivity() {

    companion object {
        fun actionView(context: Context): Intent {
            return Intent(context, StudyPlanActivity::class.java)
        }
    }

    @Composable
    override fun RenderContent() {

        EnableEdgeLight()

        Column(modifier = Modifier.navigationBarsPadding()) {
            TitleBar(title = "学习单词")
            val context = LocalContext.current
            val iStudy = remember {
                PlanStudyLogic(
                    planRepositoryLocal = StudyPlanRepositoryLocal(context),
                    studyRepository = StudyRepository(),
                )
            }
            StudyPageScreen(
                modifier = Modifier.fillMaxSize(),
                iStudyLogic = iStudy,
                onFinish = {
                    showShotToast("恭喜你完成了今天的学习计划")
                    finish()
                }
            )
        }

    }

}