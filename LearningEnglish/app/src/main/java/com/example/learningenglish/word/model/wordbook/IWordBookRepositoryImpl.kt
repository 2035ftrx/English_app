package com.example.learningenglish.word.model.wordbook

import com.example.learningenglish.http.wordbook.WordBookRepository
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.ResultFlow
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.model.toUI
import kotlinx.coroutines.flow.Flow

class IWordBookRepositoryImpl(private val wordBookRepository: WordBookRepository) :
    IWordBookRepository {
    override fun getWordBookById(id: Long): Flow<LoadingData<WordBookUI>> {
        return ResultFlow({ wordBookRepository.get(id) }) { it.toUI() }
    }
}