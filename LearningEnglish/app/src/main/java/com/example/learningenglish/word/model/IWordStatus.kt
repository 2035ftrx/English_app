package com.example.learningenglish.word.model

sealed interface IWordStatus {

    fun value(): Int

    data object Learned : IWordStatus {
        override fun value(): Int {
            return 1
        }
    }

    data object Blurred : IWordStatus {
        override fun value(): Int {
            return 2
        }
    }

    data object Unknown : IWordStatus {
        override fun value(): Int {
            return 3
        }
    }

}