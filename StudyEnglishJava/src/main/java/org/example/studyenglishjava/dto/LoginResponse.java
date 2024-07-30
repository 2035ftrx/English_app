package org.example.studyenglishjava.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private final String token;
    private final UserDTO user;

    public LoginResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

}