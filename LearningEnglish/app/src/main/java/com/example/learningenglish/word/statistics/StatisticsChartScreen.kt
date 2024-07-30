package com.example.learningenglish.word.statistics

import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningenglish.ui.view.ViewSpacer
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import timber.log.Timber


@Preview
@Composable
private fun PreviewChartScreen() {
    StatisticsChartScreen(
        modifier = Modifier.fillMaxSize(),
        wordCount = 23,
        timeRange = "7月1日-7月7日",
        dataLearned = listOf(
            BarEntry(504087f, 0.0f),
            BarEntry(536076f, 5.0f),
            BarEntry(582476f, 0.0f),
            BarEntry(608876f, 0.0f),
            BarEntry(658876f, 0.0f),
            BarEntry(695276f, 0.0f),
            BarEntry(781676f, 8f),
        ),
        dataReview = listOf(
            BarEntry(504087f, 0.0f),
            BarEntry(536076f, 0.0f),
            BarEntry(582476f, 3.0f),
            BarEntry(608876f, 0.0f),
            BarEntry(658876f, 8.0f),
            BarEntry(695276f, 0.0f),
            BarEntry(781676f, 3f),
        ),
        timeCount = 654,
        studyTime = listOf(
            BarEntry(504087f, 11f),
            BarEntry(536076f, 0.0f),
            BarEntry(582476f, 0.0f),
            BarEntry(608876f, 0.0f),
            BarEntry(658876f, 6.0f),
            BarEntry(695276f, 0.0f),
            BarEntry(781676f, 0.0f),
        )
    )
}


@Composable
fun StatisticsChartScreen(
    modifier: Modifier = Modifier,
    timeRange: String,
    wordCount: Int,
    dataLearned: List<BarEntry>,
    dataReview: List<BarEntry>,
    timeCount: Int,
    studyTime: List<BarEntry>,
) {

    Column(modifier = modifier) {
        Text(text = timeRange, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        ViewSpacer(size = 16)

        Text(
            modifier = Modifier,
            text = "单词学习量", fontSize = 16.sp, fontWeight = FontWeight.SemiBold
        )
        ViewSpacer(size = 8)

        Text(text = "共" + wordCount + "词", fontSize = 12.sp)

        WordChartGraph(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            dataLearned = dataLearned,
            dataReview = dataReview
        )
        ViewSpacer(size = 16)

        HorizontalDivider(
            thickness = 16.dp,
            color = androidx.compose.ui.graphics.Color.LightGray.copy(alpha = 0.2f)
        )

        ViewSpacer(size = 16)

        Text(
            modifier = Modifier,
            text = "学习时长", fontSize = 16.sp, fontWeight = FontWeight.SemiBold
        )
        ViewSpacer(size = 8)

        Text(text = "共" + timeCount + "分钟（单位：分钟）", fontSize = 12.sp)

        StudyTimeGraph(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
            studyTime = studyTime
        )

    }

}


@Composable
fun WordChartGraph(
    modifier: Modifier = Modifier,
    dataLearned: List<BarEntry>,
    dataReview: List<BarEntry>,
) {
    BarGraph(modifier = modifier, onUpdate = { chart ->

        Timber.d(" dataLearned : $dataLearned ")
        val learnedDataSet = BarDataSet(dataLearned, "学习单词量")
        learnedDataSet.setColor(Color.parseColor("#00ee00"))

        Timber.d(" dataReview : $dataReview ")
        val reviewDataSet = BarDataSet(dataReview, "复习单词量")
        reviewDataSet.setColor(Color.parseColor("#eeee00"))

        chart.data = BarData(learnedDataSet, reviewDataSet).apply {
            barWidth = 10f
        }

        chart.groupBars(0f, 0.1f, 0f);

        chart.invalidate()

    })
}

@Composable
fun StudyTimeGraph(
    modifier: Modifier = Modifier,
    studyTime: List<BarEntry>,
) {
    BarGraph(modifier = modifier, onUpdate = { chart ->

        Timber.d(" studyTime : $studyTime ")
        val learnedDataSet = BarDataSet(studyTime, "学习时间")
        learnedDataSet.setColor(Color.parseColor("#eeee00"))

        chart.data = BarData(learnedDataSet).apply {
            barWidth = 5f
        }
        chart.invalidate()

    })
}
