package com.example.learningenglish.word.plan

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.ui.view.TitleBar

class SetPlanningActivity : BaseComposeActivity() {

    companion object {
        fun actionView(context: Context): Intent {
            return Intent(context, SetPlanningActivity::class.java)
        }
    }

    @Composable
    override fun RenderContent() {

        EnableEdgeLight()

        Column(modifier = Modifier.navigationBarsPadding()) {
            TitleBar(title = "学习计划")
            StudyPlanScreen(Modifier.fillMaxSize())
        }

    }


}