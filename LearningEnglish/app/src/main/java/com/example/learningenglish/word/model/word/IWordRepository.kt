package com.example.learningenglish.word.model.word

import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.word.model.WordUI
import kotlinx.coroutines.flow.Flow

interface IWordRepository {
    fun getWordById(id: Long): Flow<LoadingData<WordUI>>
    fun getWordByRank(rank: Int, bookId: Long): Flow<LoadingData<WordUI>>
}