package com.example.learningenglish.word.model.word

import com.example.learningenglish.http.word.WordRepository
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.ResultFlow
import com.example.learningenglish.word.model.WordUI
import com.example.learningenglish.word.model.toUI
import kotlinx.coroutines.flow.Flow

class IWordRepositoryImpl(private val wordRepository: WordRepository) : IWordRepository {

    override fun getWordById(id: Long): Flow<LoadingData<WordUI>> {
        return ResultFlow({ wordRepository.getWordById(id) }) { it.toUI() }
    }

    override fun getWordByRank(rank: Int, bookId: Long): Flow<LoadingData<WordUI>> {
        return ResultFlow({ wordRepository.getWordByRank(bookId, rank) }) { it.toUI() }
    }

}