package org.example.studyenglishjava.word;


import lombok.val;
import org.example.studyenglishjava.auth.UserPrincipal;
import org.example.studyenglishjava.dto.*;
import org.example.studyenglishjava.entity.StudyRecord;
import org.example.studyenglishjava.entity.StudySession;
import org.example.studyenglishjava.entity.Word;
import org.example.studyenglishjava.record.StudyRecordRepository;
import org.example.studyenglishjava.record.StudySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


@RestController
@RequestMapping("/app/words/study")
public class StudyController {

    // logger
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StudyController.class);

    @Autowired
    private StudyRecordRepository studyRecordRepository;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private WordBookRepository wordBookRepository;

    @GetMapping("plan")
    public PlanTaskDTO getTaskByPlan(
            @RequestParam(value = "bookId") long bookId,
            @RequestParam(value = "planWordCount") int planWordCount
    ) {
        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        val book = wordBookRepository.findById(bookId);
        logger.info("getTaskByPlan bookId:{} book:{}", bookId, book.orElseThrow());
        val learnedWords = studyRecordRepository.findByUserIdAndBookId(currentUser.userId(), bookId);
        val todayLearnedWords = learnedWords.stream().filter(w -> isToday(w.getLastReviewTime())).toList();
        logger.info("getTaskByPlan learnedWords:{}", learnedWords);
        val learnedWordIds = learnedWords.stream().map(StudyRecord::getWordId).distinct().toList();
        logger.info("getTaskByPlan learnedWordIds:{}", learnedWordIds);
        Pageable pageable = PageRequest.of(0, planWordCount - todayLearnedWords.size());
        val ids = new ArrayList<Long>();
        if (learnedWordIds.isEmpty()) {
            ids.add(0L);
        } else {
            ids.addAll(learnedWordIds);
        }
        val byBookIdAndIdNotInOrderByWordRank = wordRepository.findByBookIdAndIdNotInOrderByWordRank(bookId, ids, pageable);
        return new PlanTaskDTO(book.orElseThrow().toDTO(), byBookIdAndIdNotInOrderByWordRank.stream().map(Word::toDTO).toList());
    }

    @GetMapping("review")
    public ReviewTaskDTO getReviewTask(@RequestParam(value = "bookId") long bookId) {
        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        val book = wordBookRepository.findById(bookId);
        val learnedWords = studyRecordRepository.findStudyRecordsWithWordsByUserId(currentUser.userId());
        val ebbinghaus = new Ebbinghaus();
        return new ReviewTaskDTO(book.orElseThrow().toDTO(), learnedWords.stream().filter(ebbinghaus::needReview).toList());
    }


    @GetMapping("today")
    public TodayTaskInfoDTO getTodayTaskInfo(@RequestParam(value = "bookId") long bookId) {
        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        val book = wordBookRepository.findById(bookId);
        val learnedWords = studyRecordRepository.findStudyRecordsWithWordsByUserId(currentUser.userId());
        val todayLearnedWords = learnedWords.stream().filter(w -> isToday(w.getLastReviewTime())).toList();
        val ebbinghaus = new Ebbinghaus();
        val needReview = learnedWords.stream().filter(ebbinghaus::needReview).toList();
        return new TodayTaskInfoDTO(
                book.orElseThrow().toDTO(),
                learnedWords.size(),
                book.orElseThrow().getWordNum(),
                todayLearnedWords.size(),
                needReview.size()
        );
    }

    // 判断时间戳是否在今天，将时间戳转字符串再判断是否同一天
    public boolean isToday(long timestamp) {
        val date = new Date(timestamp);
        val format = new SimpleDateFormat("yyyy-MM-dd");
        val today = format.format(new Date());
        return format.format(date).equals(today);
    }


}
