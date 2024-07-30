package org.example.studyenglishjava.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PlanTaskDTO {
    private WordBookDto book;
    private List<WordDTO> list;
}