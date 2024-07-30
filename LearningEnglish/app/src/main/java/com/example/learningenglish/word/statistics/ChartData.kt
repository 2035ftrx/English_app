package com.example.learningenglish.word.statistics

import com.github.mikephil.charting.data.BarEntry

data class ChartData(
   val timeRange: String,
   val wordCount: Int,
   val dataLearned: List<BarEntry>,
   val dataReview: List<BarEntry>,
   val timeCount: Int,
   val studyTime: List<BarEntry>,
)
