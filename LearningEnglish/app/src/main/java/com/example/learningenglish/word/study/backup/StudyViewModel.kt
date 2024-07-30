// StudyViewModel.kt
package com.example.learningenglish.word.study.backup

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningenglish.http.word.WordRepository
import com.example.learningenglish.http.wordbook.WordBookRepository
import com.example.learningenglish.ui.base.BaseViewModel
import com.example.learningenglish.ui.base.CommonViewModelFactory
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitError
import com.example.learningenglish.ui.base.emitLoading
import com.example.learningenglish.ui.base.emitSuccess
import com.example.learningenglish.ui.base.flatMapLoadingSuccess
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.model.word.IWordRepository
import com.example.learningenglish.word.model.word.IWordRepositoryImpl
import com.example.learningenglish.word.model.wordbook.IWordBookRepository
import com.example.learningenglish.word.model.wordbook.IWordBookRepositoryImpl
import com.example.learningenglish.word.plan.StudyPlanRepositoryLocal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

fun StudyViewModelFactory(context: Context): ViewModelProvider.Factory {
    return CommonViewModelFactory(createFunc = {
        StudyViewModel(
            IWordRepositoryImpl(WordRepository()),
            IWordBookRepositoryImpl(WordBookRepository()),
            StudyRecordRepository(context),
            StudyProgressRepository(context),
            StudyPlanRepositoryLocal(context)
        )
    })
}

class StudyViewModel(
    private val wordRepository: IWordRepository,
    private val wordBookRepository: IWordBookRepository,
    private val studyRecordRepository: StudyRecordRepository,
    private val studyProgressRepository: StudyProgressRepository,
    private val studyPlanRepositoryLocal: StudyPlanRepositoryLocal,
) : BaseViewModel() {

    // 单词书相关信息
    private val _wordBookUI = MutableStateFlow<LoadingData<WordBookUI>>(LoadingData.Loading())
    val wordBookUI: StateFlow<LoadingData<WordBookUI>> = _wordBookUI

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage

    fun setupByWordId(wordId: Long) {
        viewModelScope.launch {
            _wordBookUI.emitLoading()
            wordRepository
                .getWordById(wordId)
                .flatMapLoadingSuccess()
                .flatMapLatest { word ->
                    val bookId = word.bookId
                    wordBookRepository.getWordBookById(bookId)
                        .flatMapLoadingSuccess { Pair(word, it) }
                }
                .catch {
                    it.printStackTrace()
                    it.message?.let { it1 -> _wordBookUI.emitError(it1) }
                }
                .collect { pair ->
                    val (word, book) = pair
                    val wordRank = word.wordRank
                    _wordBookUI.emitSuccess(book)
                    _currentPage.emit(wordRank)
                }
        }
    }


}


