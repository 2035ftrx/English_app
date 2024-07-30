package org.example.studyenglishjava.entity;

public interface DefaultStringValue {
    String value();

    public enum Impl implements DefaultStringValue {
        UNKNOWN {
            @Override
            public String value() {
                return "";
            }
        }
    }
}