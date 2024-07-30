package org.example.studyenglishjava.dto;

public sealed interface IWordStatus permits IWordStatus.Learned, IWordStatus.Blurred, IWordStatus.Unknown {

    int value();

    final class Learned implements IWordStatus {
        @Override
        public int value() {
            return 1;
        }

    }

    final class Blurred implements IWordStatus {
        @Override
        public int value() {
            return 2;
        }
    }

    final class Unknown implements IWordStatus {
        @Override
        public int value() {
            return 3;
        }
    }
}
