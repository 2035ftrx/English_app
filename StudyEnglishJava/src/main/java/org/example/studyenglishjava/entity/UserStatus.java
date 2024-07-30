package org.example.studyenglishjava.entity;

public enum UserStatus {
    ACTIVE(1),
    INACTIVE(0),
    DELETE(-1),
    UNKNOWN(-2),
    ERROR(-3);

    private final int value;

    UserStatus(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
