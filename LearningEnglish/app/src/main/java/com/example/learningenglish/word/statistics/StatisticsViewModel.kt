package com.example.learningenglish.word.statistics

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningenglish.http.record.DailyStudyStats
import com.example.learningenglish.http.record.RecordRepository
import com.example.learningenglish.http.record.StudyStatsDTO
import com.example.learningenglish.ui.base.BaseViewModel
import com.example.learningenglish.ui.base.CommonViewModelFactory
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitError
import com.example.learningenglish.ui.base.emitLoading
import com.example.learningenglish.ui.base.emitSuccess
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun StatisticsViewModelFactory(context: Context): ViewModelProvider.Factory {
    return CommonViewModelFactory(createFunc = {
        StatisticsViewModel(RecordRepository())
    })
}

class StatisticsViewModel(
    private val recordRepository: RecordRepository
) : BaseViewModel() {

    private val _chartData = MutableStateFlow<LoadingData<ChartData>>(LoadingData.Loading())
    val chartData: StateFlow<LoadingData<ChartData>> = _chartData

    init {
        val currentTime = System.currentTimeMillis()
        val startTime = currentTime - TimeUnit.DAYS.toMillis(7)
        loadData(startTime, currentTime)
    }

    private fun loadData(startTime: Long, endTime: Long) {
        viewModelScope.launch {
            _chartData.emitLoading()
            recordRepository.getStudyStats(startTime, endTime)
                .onSuccess {
                    if (it.isSuccess) {
                        val chartData = responseToDailyData(it.data, startTime, endTime)
                        val simpleDateFormatMD = SimpleDateFormat("MM月dd日", Locale.getDefault())
                        val timeRange = "${
                            simpleDateFormatMD.format(startTime)
                        }-${
                            simpleDateFormatMD.format(endTime)
                        }"
                        _chartData.emitSuccess(chartData.copy(timeRange = timeRange))
                    } else {
                        _chartData.emitError(it.message)
                    }
                }
                .onFailure {
                    it.printStackTrace()
                    it.message?.let { it1 -> _chartData.emitError(it1) }
                }
        }
    }

    private fun responseToDailyData(
        data: StudyStatsDTO, startTime: Long, endTime: Long
    ): ChartData {
        val dailyStudyStats = data.dailyStudyStats
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = startTime
        val end = Calendar.getInstance().apply { timeInMillis = endTime  }

        var totalWordCount = 0
        var totalTimeCount = 0
        val dataLearned = mutableListOf<BarEntry>()
        val dataReview = mutableListOf<BarEntry>()
        val studyTime = mutableListOf<BarEntry>()

        while (!calendar.after(end)) {
            val currentDate = dateFormat.format(calendar.time)

            val stat = dailyStudyStats[currentDate] ?: DailyStudyStats(
                date = currentDate,
                wordsLearned = 0,
                wordsReviewed = 0,
                studyDuration = 0,
                reviewDuration = 0
            )

            totalWordCount += stat.wordsLearned + stat.wordsReviewed
            val totalMinutes = (stat.studyDuration + stat.reviewDuration + 30000) / 60000 // 转换为分钟
            totalTimeCount += totalMinutes

            val xValue = (calendar.time.time - 1719038400000).toFloat()
            dataLearned.add(BarEntry(xValue, stat.wordsLearned.toFloat()))
            dataReview.add(BarEntry(xValue, stat.wordsReviewed.toFloat()))
            studyTime.add(BarEntry(xValue, totalMinutes.toFloat()))

            Timber.d(" $currentDate ${calendar.time.time} $xValue $totalMinutes $stat")

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val timeRange =
            "${dateFormat.format(Date(startTime))} - ${dateFormat.format(Date(endTime))}"

        return ChartData(
            timeRange = timeRange,
            wordCount = totalWordCount,
            dataLearned = dataLearned,
            dataReview = dataReview,
            timeCount = totalTimeCount,
            studyTime = studyTime
        )
    }

    fun refresh() {
        val currentTime = System.currentTimeMillis()
        val startTime = currentTime - 7 * 24 * 60 * 60 * 1000
        loadData(startTime, currentTime)
    }

}
