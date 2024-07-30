package com.example.learningenglish.admin.word

import androidx.lifecycle.ViewModel
import com.example.learningenglish.http.admin.AdminWordRepository
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.flatMapResultFlow
import com.example.learningenglish.ui.base.paging.GenericPagingRepository
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.model.toUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AdminBooksViewModel : ViewModel() {
    private val wordRepository = AdminWordRepository() // 假设你有一个UserRepository来处理数据操作

    val bookPagingFlow = GenericPagingRepository { page, size ->
        val all = wordRepository.getAllWordBooks(page, size)
        if (all.isSuccess) {
            if (all.getOrThrow().isSuccess) {
                all.getOrThrow().data.data.map { it.toUI() }
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
    }.getPagingData()

    fun createWordBook(wordBook: WordBookUI): Flow<LoadingData<WordBookUI>> {
        return flow {
            wordRepository.createWordBook(
                wordBook.picUrl,
                wordBook.title,
                wordBook.wordNum,
                wordBook.tags,
            )
                .flatMapResultFlow {
                    it.toUI()
                }
                .collect {
                    emit(it)
                }
        }
    }

    fun updateWordBook(wordBook: WordBookUI): Flow<LoadingData<WordBookUI>> {
        return flow {
            wordRepository.updateWordBook(
                wordBook.id, wordBook.picUrl, wordBook.title, wordBook.wordNum, wordBook.tags,
            )
                .flatMapResultFlow {
                    it.toUI()
                }
                .collect {
                    emit(it)
                }
        }
    }

    fun deleteWordBook(wordBookId: Long): Flow<LoadingData<Long>> {
        return flow {
            wordRepository.deleteWordBook(wordBookId)
                .flatMapResultFlow { it }
                .collect {
                    emit(it)
                }
        }
    }

}
