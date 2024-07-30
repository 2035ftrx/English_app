package org.example.studyenglishjava.record;


import lombok.val;
import org.example.studyenglishjava.auth.UserPrincipal;
import org.example.studyenglishjava.dto.*;
import org.example.studyenglishjava.entity.StudyRecord;
import org.example.studyenglishjava.entity.StudySession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/app/words/record")
public class StudyRecordController {

    // logger
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StudyRecordController.class);

    @Autowired
    private StudyRecordRepository studyRecordRepository;
    @Autowired
    private StudySessionRepository studySessionRepository;

    @PostMapping("/update")
    public StudyRecord updateStudyRecord(@RequestBody StudyRecordRequest request) {
        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        val userId = currentUser.userId();
        Optional<StudyRecord> optionalRecord = studyRecordRepository.findByUserIdAndWordId(
                userId, request.wordId()
        );

        StudyRecord record;
        if (optionalRecord.isPresent()) {
            record = optionalRecord.get();
            record.setUpdateTime(System.currentTimeMillis());
        } else {
            record = new StudyRecord();
            record.setUserId(userId);
            record.setBookId(request.bookId());
            record.setWordId(request.wordId());
            record.setCreateTime(System.currentTimeMillis());
            record.setUpdateTime(System.currentTimeMillis());
            record.setStage(0); // 初始阶段
            record.setStrange(0); // 初始陌生度
        }

        // 根据状态更新记录
        switch (request.status()) {
            case 1://"学会了"
                record.setStage(record.getStage() + 1);
                record.setStrange(0);
                break;
            case 2://"模糊"
                record.setStrange(record.getStrange() + 1);
                break;
            case 3://"不认识"
                record.setStage(0); // 重置阶段
                record.setStrange(record.getStrange() + 1);
                break;
        }

        record.setLastReviewTime(System.currentTimeMillis());
        StudyRecord updatedRecord = studyRecordRepository.save(record);
        return updatedRecord;
    }

    @PostMapping("/record")
    public StudySession recordStudySession(@RequestBody StudySessionRequest request) {

        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        val userId = currentUser.userId();
        StudySession session = new StudySession();
        session.setUserId(userId);
        session.setBookId(request.bookId());
        session.setWordId(request.wordId());
        session.setStartTime(request.startTime());
        session.setEndTime(request.endTime());
        session.setType(request.type());

        val save = studySessionRepository.save(session);

        return save;
    }


    @GetMapping("/record/range")
    public StudyStatsDTO getStudyStats(
            @RequestParam Long startDate,
            @RequestParam  Long endDate
    ) {

        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        val userId = currentUser.userId();

        long startTime =  Instant.ofEpochMilli(startDate).toEpochMilli();
        long endTime =  Instant.ofEpochMilli(endDate).toEpochMilli();

        List<StudySession> sessions = studySessionRepository.findAllByUserIdAndStartTimeBetween(userId, startTime, endTime);

        Map<LocalDate, DailyStudyStats> statsMap = new HashMap<>();

        for (StudySession session : sessions) {
            LocalDate date = Instant.ofEpochMilli(session.getStartTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            DailyStudyStats stats = statsMap.getOrDefault(date, new DailyStudyStats(date));

            if (session.getType() == IStudyType.Review.INSTANCE.value()) {
                stats.setWordsReviewed(stats.getWordsReviewed() + 1);
                stats.setReviewDuration(stats.getReviewDuration() + (session.getEndTime() - session.getStartTime()));
            } else {
                stats.setWordsLearned(stats.getWordsLearned() + 1);
                stats.setStudyDuration(stats.getStudyDuration() + (session.getEndTime() - session.getStartTime()));
            }

            statsMap.put(date, stats);
        }

        return new StudyStatsDTO(statsMap);
    }

}
