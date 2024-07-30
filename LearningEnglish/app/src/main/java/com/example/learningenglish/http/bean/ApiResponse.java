package com.example.learningenglish.http.bean;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<D> {

    @SerializedName("data")
    private D data;

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message = "";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return code == 1;
    }

    @Override
    public String toString() {
        return "DataResponse{" +
                "data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
