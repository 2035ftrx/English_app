package com.example.learningenglish.home

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningenglish.test.testTaskData
import com.example.learningenglish.ui.base.CommonLoadingLayout
import com.example.learningenglish.ui.theme.AppTheme
import com.example.learningenglish.ui.view.ViewSpacer
import com.example.learningenglish.word.plan.SetPlanningActivity
import com.example.learningenglish.word.study.ReviewActivity
import com.example.learningenglish.word.study.StudyPlanActivity

@Preview
@Composable
private fun PreviewTodayTaskScreen() {
    AppTheme {
        TaskDetailsCard(modifier = Modifier.fillMaxSize(), testTaskData.random()){

        }
    }
}

@Composable
fun TodayTaskScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val viewModel: TodayTaskViewModel =
        viewModel(factory = TodayTaskViewModelFactory(context))

    val task = viewModel.todayTask.collectAsState().value

    CommonLoadingLayout(modifier = modifier, data = task) { ui ->
        TaskDetailsCard(modifier, ui) {
            viewModel.loadData()
        }
    }

}

@Composable
private fun TaskDetailsCard(modifier: Modifier, ui: TodayTaskUI, onRefresh: () -> Unit) {

    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            onRefresh()
        }
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (ui.hasPlan) {
                TaskInfoDetails(ui, launcher, context)
            } else {
                Button(onClick = { launcher.launch(SetPlanningActivity.actionView(context = context)) }) {
                    Text(text = "制定学习计划")
                }
            }
        }
    }
}

@Composable
private fun TaskInfoDetails(
    ui: TodayTaskUI,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    context: Context
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = ui.bookName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        TextButton(onClick = { launcher.launch(SetPlanningActivity.actionView(context)) }) {
            Text(text = "修改计划")
        }
    }
    ViewSpacer(size = 10)
    TaskDetails(ui, {
        launcher.launch(StudyPlanActivity.actionView(context = context))
    }, {
        launcher.launch(ReviewActivity.actionView(context = context))
    })
}

@Composable
private fun TaskDetails(
    ui: TodayTaskUI,
    onStudy: () -> Unit,
    onReview: () -> Unit
) {
    Text(text = "已背${ui.learnedWordCount}/${ui.totalWordCount}", fontSize = 12.sp)
    ViewSpacer(size = 20)
    val todayWordCount = ui.todayWordCount
    val todayReviewWordCount = ui.todayReviewWordCount
    Row {
        BigCountBtnLayout(
            modifier = Modifier.weight(1f),
            title = "今日待学习",
            count = todayWordCount,
            buttonText = "学习",
            onClick = {
                onStudy()
            }
        )
        BigCountBtnLayout(
            modifier = Modifier.weight(1f),
            title = "今日待复习",
            count = todayReviewWordCount,
            buttonText = "复习",
            onClick = {
                onReview()
            }
        )
    }
}

@Composable
private fun BigCountBtnLayout(
    modifier: Modifier = Modifier,
    title: String,
    count: Int,
    buttonText: String,
    onClick: () -> Unit,
) {

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = title, fontSize = 12.sp)
        ViewSpacer(size = 10)

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = count.toString(),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                maxLines = 1,
                modifier = Modifier
            )
            Text(text = "词")
        }
        ViewSpacer(size = 10)

        Button(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            onClick = { onClick() },
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(text = buttonText)
        }

    }


}
