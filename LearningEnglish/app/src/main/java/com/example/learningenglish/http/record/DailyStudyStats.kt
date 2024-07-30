package com.example.learningenglish.http.record

data class DailyStudyStats(
    val date: String, // 学习和复习的日期
    val wordsLearned: Int,// 当天学习的单词数量
    val wordsReviewed: Int,// 当天复习的单词数量
    val studyDuration: Int,// 学习花费的时间（毫秒）
    val reviewDuration: Int,// 复习花费的时间（毫秒）
) {
}
