package com.example.learningenglish.aichat.model

sealed interface IChatRole {
    fun value(): Int
    fun name():String

    data object User : IChatRole {
        override fun value(): Int {
            return 1
        }

        override fun name(): String {
            return "user"
        }
    }

    data object Assistant : IChatRole {
        override fun value(): Int {
            return 2
        }

        override fun name(): String {
            return "assistant"
        }
    }
}
