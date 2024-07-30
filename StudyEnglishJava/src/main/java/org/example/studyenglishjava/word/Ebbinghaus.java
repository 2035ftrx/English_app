package org.example.studyenglishjava.word;

import org.example.studyenglishjava.dto.StudyRecordWordDTO;

public class Ebbinghaus {
    private static final int[] forgettingCurve = {
            5,         // 5分钟
            30,        // 30分钟
            12 * 60,   // 12小时
            1 * 24 * 60, // 1天
            2 * 24 * 60, // 2天
            4 * 24 * 60, // 4天
            7 * 24 * 60, // 7天
            15 * 24 * 60 // 15天
    };

    public   boolean needReview(StudyRecordWordDTO record) {
        // 1. 检查记录中的学习阶段是否已经超过了遗忘曲线数组的最大索引，若超过则说明该单词已经通过所有预设复习节点，无需再次复习，直接返回 false。
        if (record.getStage() >= forgettingCurve.length) {
            return false;
        }

        // 2. 获取当前系统的毫秒时间戳。
        long currentTime = System.currentTimeMillis();

        // 3. 计算从上次复习到现在的总分钟数差异
        long timeDiff = (currentTime - record.getLastReviewTime()) / (1000 * 60);

        // 4. 判断这个时间差是否大于遗忘曲线中当前学习阶段所对应的复习时间点，如果是，则表示需要进行下一轮复习，返回 true；否则，返回 false。
        return timeDiff > forgettingCurve[record.getStage()];
    }
}
