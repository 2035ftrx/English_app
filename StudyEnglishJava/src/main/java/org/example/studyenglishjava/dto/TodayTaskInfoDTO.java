package org.example.studyenglishjava.dto;

public record TodayTaskInfoDTO(
        WordBookDto bookName,
        long learnedWordCount,
        long totalWordCount,
        long todayWordCount,
        long todayReviewWordCount
) {
}
