package org.example.studyenglishjava.dto;


import org.example.studyenglishjava.entity.WordBook;

/**
 * DTO for {@link WordBook}
 */
public record WordBookDto(Long id, String bookId, String picUrl, String title, Integer wordNum, String tags) {
}