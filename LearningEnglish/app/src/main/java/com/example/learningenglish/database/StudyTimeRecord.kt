// LearningTimeRecord.kt
package com.example.learningenglish.database

import androidx.room.Entity
import androidx.room.PrimaryKey


// 单词的学习进度记录
@Entity
data class StudyTimeRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,        // user ID
    val wordId: Long,        // 单词 ID

    // 学习时间记录，当用户进入背单词页面时，记录学习开始时间，当用户退出背单词页面时，记录学习结束时间
    // 同一个单词，可能会记录多次学习时间
    val startTime: Long, // 学习开始时间
    val lastTime: Long,   // 学习结束时间
)
