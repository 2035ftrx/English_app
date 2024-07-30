package org.example.studyenglishjava.record;

import org.example.studyenglishjava.dto.StudyRecordWordDTO;
import org.example.studyenglishjava.entity.StudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long> {

    List<StudyRecord> findByUserIdAndBookId(Long userId, Long bookId);

    // 查询用已学习的单词
    List<StudyRecord> findByUserId(Long userId);

    Optional<StudyRecord> findByUserIdAndWordId(Long userId,  Long wordId);

    @Query("SELECT new org.example.studyenglishjava.dto.StudyRecordWordDTO(sr.id, sr.userId, sr.bookId, sr.wordId, sr.stage, sr.strange, sr.createTime, sr.lastReviewTime, sr.updateTime, w.wordRank, w.headWord, w.word) " +
            "FROM StudyRecord sr " +
            "JOIN word_table w ON sr.wordId = w.id " +
            "WHERE sr.userId = :userId and sr.bookId = :bookId ")
    List<StudyRecordWordDTO> findStudyRecordsWithWordsByUserIdAndBookId(Long userId, Long bookId);


    @Query("SELECT new org.example.studyenglishjava.dto.StudyRecordWordDTO(sr.id, sr.userId, sr.bookId, sr.wordId, sr.stage, sr.strange, sr.createTime, sr.lastReviewTime, sr.updateTime, w.wordRank, w.headWord, w.word) " +
            "FROM StudyRecord sr " +
            "JOIN word_table w ON sr.wordId = w.id " +
            "WHERE sr.userId = :userId")
    List<StudyRecordWordDTO> findStudyRecordsWithWordsByUserId(Long userId);


}