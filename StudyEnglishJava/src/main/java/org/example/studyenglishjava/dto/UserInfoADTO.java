package org.example.studyenglishjava.dto;


public record UserInfoADTO(
        long id,
        String username,
        String nickname,
        int gender,
        long lastLoginTime,
        String school,
        String grade
) {
}
