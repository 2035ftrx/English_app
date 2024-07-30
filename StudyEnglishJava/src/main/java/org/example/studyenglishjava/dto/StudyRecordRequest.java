package org.example.studyenglishjava.dto;


/**
 * @param status 1"学会了" 2"模糊" 3"不认识"
 */
public record StudyRecordRequest( Long bookId, Long wordId, Integer status) {
}
