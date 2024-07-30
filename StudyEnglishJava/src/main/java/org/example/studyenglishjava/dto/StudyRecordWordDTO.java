package org.example.studyenglishjava.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StudyRecordWordDTO {
    private Long studyRecordId;
    private Long userId;
    private Long bookId;
    private Long wordId;
    private Integer stage;
    private Integer strange;
    private Long createTime;
    private Long lastReviewTime;
    private Long updateTime;
    private Integer wordRank;
    private String headWord;
    private String word;
}