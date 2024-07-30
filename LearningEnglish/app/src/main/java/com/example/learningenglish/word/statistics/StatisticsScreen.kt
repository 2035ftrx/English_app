package com.example.learningenglish.word.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningenglish.ui.base.CommonLoadingLayout
import com.example.learningenglish.ui.view.rememberBooleanState


@Preview
@Composable
private fun PreviewStatisticsScreen() {
    StatisticsScreen(modifier = Modifier.fillMaxSize())
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StatisticsScreen(modifier: Modifier = Modifier) {

    val viewModel: StatisticsViewModel =
        viewModel(factory = StatisticsViewModelFactory(LocalContext.current))

    val chartData = viewModel.chartData.collectAsState().value

    Column(modifier = modifier) {
        Text(
            text = "统计",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        val refreshState = rememberBooleanState()
        val rememberPullRefreshState = rememberPullRefreshState(
            refreshing = refreshState.isTrue(),
            onRefresh = { refreshState.beTrue();viewModel.refresh() })
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .pullRefresh(rememberPullRefreshState)
        ) {
            CommonLoadingLayout(data = chartData) { data ->
                StatisticsChartScreen(
                    modifier = Modifier.padding(16.dp),
                    timeRange = data.timeRange,
                    wordCount = data.wordCount,
                    dataLearned = data.dataLearned,
                    dataReview = data.dataReview,
                    timeCount = data.timeCount,
                    studyTime = data.studyTime
                )
            }
        }

    }

}