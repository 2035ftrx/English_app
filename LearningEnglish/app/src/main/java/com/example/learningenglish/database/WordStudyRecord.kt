// LearningTimeRecord.kt
package com.example.learningenglish.database

import androidx.room.Entity
import androidx.room.PrimaryKey


// 单词的学习进度记录
@Entity
data class WordStudyRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,        // user ID
    val bookId: Long,        // 书本 ID
    val wordId: Long,        // 单词 ID
    val stage: Int,         // 记忆阶段
    val strange: Int,       // 陌生度
    val createTime: Long,   // 开始学习时间
    val lastReviewTime: Long,   // 上次复习时间，第一次与开始学习时间一致，后续每次复习都会更新
    val updateTime: Long,   // 更新时间
)
