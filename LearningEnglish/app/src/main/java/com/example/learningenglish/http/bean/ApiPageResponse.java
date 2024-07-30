package com.example.learningenglish.http.bean;

import java.util.List;

public class ApiPageResponse<T> {
    private final int totalPages;
    private final int currentPage;
    private final long totalElements;
    private final List<T> data;

    public ApiPageResponse(int totalPages, int currentPage, long totalElements, List<T> data) {
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.totalElements = totalElements;
        this.data = data;
    }

    public int getTotalPages() {
        return totalPages;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public long getTotalElements() {
        return totalElements;
    }
    public List<T> getData() {
        return data;
    }

}