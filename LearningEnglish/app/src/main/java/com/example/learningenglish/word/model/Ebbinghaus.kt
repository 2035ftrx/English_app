// Ebbinghaus.kt
package com.example.learningenglish.word.model

import com.example.learningenglish.database.WordStudyRecord

class Ebbinghaus {
    // 定义一个私有数组forgettingCurve，用于存储艾宾浩斯遗忘曲线中的各个复习时间节点（以分钟计）。
    // 数组元素依次代表：5分钟后、30分钟后、12小时后、1天后、2天后、4天后、7天后、15天后的复习时间点。
    private val forgettingCurve = arrayOf(
        5,         // 5分钟
        30,        // 30分钟
        12 * 60,   // 12小时
        1 * 24 * 60, // 1天
        2 * 24 * 60, // 2天
        4 * 24 * 60, // 4天
        7 * 24 * 60, // 7天
        15 * 24 * 60 // 15天
    )

    /**
     * 需要复习的判断函数。
     *
     * @param record WordReciteRecord 类型的对象，包含单词学习的阶段信息（stage）及上一次复习的时间戳（lastTime）。
     *
     * @return 如果单词已超过当前学习阶段对应的遗忘时间点，返回 true，表示需要进行复习；反之，返回 false。
     */
    fun needReview(record: WordStudyRecord): Boolean {
        // 1. 检查记录中的学习阶段是否已经超过了遗忘曲线数组的最大索引，若超过则说明该单词已经通过所有预设复习节点，无需再次复习，直接返回 false。
        if (record.stage >= forgettingCurve.size) {
            return false
        }

        // 2. 获取当前系统的毫秒时间戳。
        val currentTime = System.currentTimeMillis()

        // 3. 计算从上次复习到现在的总分钟数差异。
        val timeDiff = ((currentTime - record.lastReviewTime) / (1000 * 60)).toInt()

        // 4. 判断这个时间差是否大于遗忘曲线中当前学习阶段所对应的复习时间点，如果是，则表示需要进行下一轮复习，返回 true；否则，返回 false。
        return timeDiff > forgettingCurve[record.stage]
    }
}

