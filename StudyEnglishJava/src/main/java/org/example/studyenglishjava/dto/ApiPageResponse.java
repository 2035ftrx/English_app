package org.example.studyenglishjava.dto;


import java.util.List;

public record ApiPageResponse<T>(int totalPages, int currentPage, long totalElements, List<T> data) {


}