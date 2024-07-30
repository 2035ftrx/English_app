package org.example.studyenglishjava.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;       // 用户ID
    private Long bookId;       // 书本ID
    private Long wordId;       // 单词ID
    private Long startTime;    // 学习或复习开始时间
    private Long endTime;      // 学习或复习结束时间
    private Integer type;  // 是否是复习（1表示复习，2表示学习）
}