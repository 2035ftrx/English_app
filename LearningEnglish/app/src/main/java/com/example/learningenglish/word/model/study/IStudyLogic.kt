package com.example.learningenglish.word.model.study

import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.word.model.record.IWordStudyStateChange
import com.example.learningenglish.word.model.WordUI
import kotlinx.coroutines.flow.Flow


interface IStudyLogic : IWordStudyStateChange {

    fun getInitialData(): Flow<LoadingData<StudyInitData>>

    fun getWordByPage(page: Int): Flow<LoadingData<WordUI>>

}