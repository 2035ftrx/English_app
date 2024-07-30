package org.example.studyenglishjava.dto;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final int code;
    private final boolean success;
    private final long time;
    private final String message;
    private final T data;

    public ApiResponse(int code, boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.time = System.currentTimeMillis();
        this.message = message;
        this.data = data;
    }

}