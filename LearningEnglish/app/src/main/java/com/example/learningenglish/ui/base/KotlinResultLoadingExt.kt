package com.example.learningenglish.ui.base

import com.example.learningenglish.http.bean.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf


fun <T, R> Result<ApiResponse<T>>.flatMapResultFlow(mapFunc: (T) -> R): Flow<LoadingData<R>> {
    val r = this
    return flow {
        r.onSuccess {
            if (it.isSuccess) {
                emitSuccess(mapFunc(it.data))
            } else {
                emitError(it.message)
            }
        }.onFailure {
            it.printStackTrace()
            it.message?.let {
                emitError(it)
            }
        }
    }
}


fun <T, R> ResultFlow(
    resultFunc: suspend () -> Result<ApiResponse<T>>,
    mapFunc: (T) -> R
): Flow<LoadingData<R>> {
    return flow {
        resultFunc().onSuccess {
            if (it.isSuccess) {
                emitSuccess(mapFunc(it.data))
            } else {
                emitError(it.message)
            }
        }.onFailure {
            it.printStackTrace()
            it.message?.let {
                emitError(it)
            }
        }
    }
}

fun <T, R> Flow<LoadingData<T>>.flatMapLoadingSuccess(mapFunc: suspend (T) -> R ): Flow<R> {
    return this.flatMapLatest {
        when (it) {
            is LoadingData.Error -> {
                throw Exception(it.getException())
            }

            is LoadingData.Loading -> {
                emptyFlow()
            }

            is LoadingData.Success -> {
                flowOf(mapFunc(it.getValue()))
            }
        }
    }
}

fun <T > Flow<LoadingData<T>>.flatMapLoadingSuccess(): Flow<T> {
    return this.flatMapLatest {
        when (it) {
            is LoadingData.Error -> {
                throw Exception(it.getException())
            }

            is LoadingData.Loading -> {
                emptyFlow()
            }

            is LoadingData.Success -> {
                flowOf(it.getValue())
            }
        }
    }
}


