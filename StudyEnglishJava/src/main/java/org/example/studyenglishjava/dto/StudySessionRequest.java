package org.example.studyenglishjava.dto;

// type 是否是复习（2表示复习，1表示学习）
public record StudySessionRequest(Long bookId,
                                  Long wordId,
                                  Long startTime, Long endTime,
                                  Integer type
) {
}
