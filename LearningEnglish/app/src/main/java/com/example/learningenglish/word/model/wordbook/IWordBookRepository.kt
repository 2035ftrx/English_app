package com.example.learningenglish.word.model.wordbook

import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.model.WordUI
import kotlinx.coroutines.flow.Flow

interface IWordBookRepository {
    fun getWordBookById(id: Long): Flow<LoadingData<WordBookUI>>
}