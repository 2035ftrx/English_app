package org.example.studyenglishjava.dto;


import lombok.*;

import java.time.LocalDate;

/**
 * @param date           学习和复习的日期
 * @param wordsLearned   当天学习的单词数量
 * @param wordsReviewed  当天复习的单词数量
 * @param studyDuration  学习花费的时间（毫秒）
 * @param reviewDuration 复习花费的时间（毫秒）
 */
@AllArgsConstructor
@Getter
@Setter
@Data
public class DailyStudyStats {

    private LocalDate date; // 学习和复习的日期
    private Integer wordsLearned = 0; // 当天学习的单词数量
    private Integer wordsReviewed = 0; // 当天复习的单词数量
    private Long studyDuration = 0L; // 学习花费的时间（毫秒）
    private Long reviewDuration = 0L; // 复习花费的时间（毫秒）

    public DailyStudyStats( LocalDate date) {
        this.date = date;
    }
}