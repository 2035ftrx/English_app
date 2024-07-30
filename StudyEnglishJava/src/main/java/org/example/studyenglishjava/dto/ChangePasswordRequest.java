package org.example.studyenglishjava.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private final String username;
    private final String oldPassword;
    private final String newPassword;
    private final String confirmPassword;

    public ChangePasswordRequest(String username, String oldPassword, String newPassword, String confirmPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

}