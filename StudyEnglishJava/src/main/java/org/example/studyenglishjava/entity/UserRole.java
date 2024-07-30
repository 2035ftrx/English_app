package org.example.studyenglishjava.entity;

public enum UserRole {
    SUPER(1),
    ADMIN(2),
    USER(3),
    GUEST(4),
    UNKNOWN(5);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static UserRole valueOf(int value) {
        for (UserRole role : values()) {
            if (role.value == value) {
                return role;
            }
        }
        return UNKNOWN;
    }
}