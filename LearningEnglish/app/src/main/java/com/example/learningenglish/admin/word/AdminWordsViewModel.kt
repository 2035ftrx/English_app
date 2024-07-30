package com.example.learningenglish.admin.word

import com.example.learningenglish.http.admin.AdminWordRepository
import com.example.learningenglish.ui.base.BaseViewModel
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.flatMapResultFlow
import com.example.learningenglish.ui.base.paging.GenericPagingRepository
import com.example.learningenglish.word.model.WordEdit
import com.example.learningenglish.word.model.toEdit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AdminWordsViewModel : BaseViewModel() {
    private val wordRepository = AdminWordRepository() // 假设你有一个WordRepository来处理数据操作

    fun wordPagingFlow(wordBookId: Long) = GenericPagingRepository { page, size ->
        val all = wordRepository.getAllWords(wordBookId, page, size)
        if (all.isSuccess) {
            if (all.getOrThrow().isSuccess) {
                all.getOrThrow().data.data.map { it.toEdit() }
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
    }.getPagingData()

    fun createWord(wordBookId: Long, headWord: String, word: String): Flow<LoadingData<WordEdit>> {
        return flow {
            wordRepository.createWord(wordBookId, headWord, word)
                .flatMapResultFlow { it.toEdit() }
                .collect { emit(it) }
        }
    }

    fun updateWord(id: Long, headWord: String, word: String): Flow<LoadingData<WordEdit>> {
        return flow {
            wordRepository.updateWord(id, headWord, word)
                .flatMapResultFlow { it.toEdit() }
                .collect { emit(it) }
        }
    }

    fun deleteWord(wordId: Long): Flow<LoadingData<Long>> {
        return flow {
            wordRepository.deleteWord(wordId)
                .flatMapResultFlow { it }
                .collect { emit(it) }
        }
    }
}
