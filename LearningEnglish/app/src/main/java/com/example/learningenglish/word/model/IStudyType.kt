package com.example.learningenglish.word.model

sealed interface IStudyType {

    fun value(): Int

    data object Learn : IStudyType {
        override fun value(): Int {
            return 1
        }
    }

    data object Review : IStudyType {
        override fun value(): Int {
            return 2
        }
    }

}