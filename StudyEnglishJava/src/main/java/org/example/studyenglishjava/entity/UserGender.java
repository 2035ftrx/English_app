package org.example.studyenglishjava.entity;

// Enums
public enum UserGender {
    MALE(1),
    FEMALE(2),
    UNKNOWN(0);

    private final int value;

    UserGender(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public String valueString() {
        return "" + this.value;
    }
}
