package org.example.studyenglishjava.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ReviewTaskDTO {
    private WordBookDto book;
    private List<StudyRecordWordDTO> list;
}
