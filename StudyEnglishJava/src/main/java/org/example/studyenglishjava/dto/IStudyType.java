package org.example.studyenglishjava.dto;

public interface IStudyType {

    int value();

    public enum Learn implements IStudyType {
        INSTANCE;

        @Override
        public int value() {
            return 1;
        }
    }

    public enum Review implements IStudyType {
        INSTANCE;

        @Override
        public int value() {
            return 2;
        }
    }
}
