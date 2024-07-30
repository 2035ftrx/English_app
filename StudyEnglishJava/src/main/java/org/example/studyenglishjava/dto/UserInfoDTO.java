package org.example.studyenglishjava.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfoDTO {
//    private final long userId;
    private String nickname;
//    private String phone;
//    private String email;
//    private String avatar;
//    private String description;
    private int gender;
    private long lastLoginTime;
    private String school;
    private String grade;
    // Getters for all fields

}