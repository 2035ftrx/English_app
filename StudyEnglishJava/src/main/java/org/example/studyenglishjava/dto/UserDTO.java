package org.example.studyenglishjava.dto;

import lombok.Data;

@Data
public class UserDTO {
    private final long id;
    private final String username;
    private final int role;

    public UserDTO(long id, String username, int role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // Getters for all fields
}