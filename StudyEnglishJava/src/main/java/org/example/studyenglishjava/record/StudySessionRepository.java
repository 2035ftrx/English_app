package org.example.studyenglishjava.record;

import org.example.studyenglishjava.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
    List<StudySession> findAllByUserIdAndStartTimeBetween(Long userId, Long startTime, Long endTime);
}