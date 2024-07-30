package com.example.learningenglish.ui.base

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class LoadingException(msg: String) : Exception(msg)

sealed interface LoadingData<T> {

    class Success<T>(private val value: T) : LoadingData<T> {
        fun getValue(): T {
            return value
        }
    }

    class Loading<T>(
        private val msg: String = "正在加载中...",
        private val value: Boolean = true
    ) : LoadingData<T> {
        fun getValue(): String {
            return msg
        }
    }

    class Error<T>(private val value: LoadingException) : LoadingData<T> {

        constructor(msg: String?) : this(LoadingException(msg ?: " error message null "))
        constructor(msg: Throwable) : this(LoadingException(msg.message ?: " error message null "))

        fun getValue(): LoadingException {
            return value
        }

        fun getMessage(msg: String? = null): String? {
            return value.message ?: msg
        }

        fun getMessageNonNull(msg: String): String {
            return value.message ?: msg
        }

        fun getException(): LoadingException {
            return value
        }

    }

}


suspend fun <D> StateFlow<LoadingData<D>>.collectLoading(
    onSuccess: (D) -> Unit,
    onLoading: () -> Unit = {},
    onError: (LoadingException) -> Unit = {},
) {
    collect {
        when (it) {
            is LoadingData.Error -> onError(it.getException())
            is LoadingData.Loading -> onLoading()
            is LoadingData.Success -> onSuccess(it.getValue())
        }
    }
}

suspend fun <D> MutableStateFlow<LoadingData<D>>.emitLoading() {
    emit(LoadingData.Loading())
}

suspend fun <D> FlowCollector<LoadingData<D>>.emitLoading() {
    emit(LoadingData.Loading())
}

suspend fun <D> MutableStateFlow<LoadingData<D>>.emitSuccess(data: D) {
    emit(LoadingData.Success(data))
}

suspend fun <D> FlowCollector<LoadingData<D>>.emitSuccess(data: D) {
    emit(LoadingData.Success(data))
}

suspend fun <D> MutableStateFlow<LoadingData<D>>.emitError(data: String) {
    emit(LoadingData.Error(data))
}


suspend fun <D> FlowCollector<LoadingData<D>>.emitError(data: String) {
    emit(LoadingData.Error(data))
}

