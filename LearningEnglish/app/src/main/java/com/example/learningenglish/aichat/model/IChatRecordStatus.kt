package com.example.learningenglish.aichat.model

sealed interface IChatRecordStatus {

    fun value(): Int

    data object Success : IChatRecordStatus {
        override fun value(): Int {
            return 1
        }
    }

    data object Failure : IChatRecordStatus {
        override fun value(): Int {
            return 2
        }
    }

    data object Loading : IChatRecordStatus {
        override fun value(): Int {
            return 3
        }
    }

}