package com.example.learningenglish.word.plan

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningenglish.ui.base.BaseViewModel
import com.example.learningenglish.ui.base.CommonViewModelFactory
import com.example.learningenglish.word.model.StudyPlan
import com.example.learningenglish.word.model.wordbook.WordBookRepositoryLocal
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.model.toUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

fun StudyPlanViewModelFactory(context: Context): ViewModelProvider.Factory {
    return CommonViewModelFactory(createFunc = {
        StudyPlanViewModel(
            StudyPlanRepositoryLocal(context),
            WordBookRepositoryLocal(context),
        )
    })
}

class StudyPlanViewModel(
    private val studyPlanRepositoryLocal: StudyPlanRepositoryLocal,
    private val wordBookRepositoryLocal: WordBookRepositoryLocal
) : BaseViewModel() {

    private val _selectedBook = MutableLiveData<WordBookUI>()
    val selectedBook: LiveData<WordBookUI> = _selectedBook

    private val _studyPlan = MutableLiveData<StudyPlan>()
    val studyPlan: LiveData<StudyPlan> = _studyPlan

    init {
        viewModelScope.launch(Dispatchers.IO) {
            studyPlanRepositoryLocal.getCurrentPlan()?.let {
                _studyPlan.postValue(it)
                wordBookRepositoryLocal.findById(it.bookId)?.let {
                    _selectedBook.postValue(it.toUI())
                }
            }
        }
    }

    fun selectBook(book: WordBookUI) {
        Timber.d(" selectBook : $book ")
        _selectedBook.postValue(book)
        loadPlan(book)
    }

    fun createPlan(book: WordBookUI, dailyWords: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val totalDays = (book.wordNum + dailyWords - 1) / dailyWords // 计算总天数
            val plan = StudyPlan(
                bookId = book.id,
                dailyWords = dailyWords,
                totalDays = totalDays.toInt(),
            )
            Timber.d(" createPlan by book : $book -> $plan ")
            studyPlanRepositoryLocal.saveStudyPlan(plan)
            _studyPlan.postValue(plan)
        }
    }

    private fun loadPlan(book: WordBookUI) {
        viewModelScope.launch(Dispatchers.IO) {
            val plan = studyPlanRepositoryLocal.getPlanByBookId(book.id)
            Timber.d(" plan by book : $book -> $plan ")
            plan?.let {
                _studyPlan.postValue(it)
            } ?: let {
                createPlan(book, 20)  // 默认每天20个单词
            }
        }
    }

}
