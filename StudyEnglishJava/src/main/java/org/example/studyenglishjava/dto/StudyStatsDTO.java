package org.example.studyenglishjava.dto;


import java.time.LocalDate;
import java.util.Map;

public record StudyStatsDTO(Map<LocalDate, DailyStudyStats> dailyStudyStats) {
}
