package com.example.learningenglish.word.study.backup

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningenglish.database.AppDatabase
import com.example.learningenglish.database.WordEntity
import com.example.learningenglish.database.WordStudyRecord
import com.example.learningenglish.ui.base.BaseViewModel
import com.example.learningenglish.ui.base.CommonViewModelFactory
import com.example.learningenglish.user.usermanager.AppUserManager
import com.example.learningenglish.word.model.LearningPlanEntity
import com.example.learningenglish.word.model.wordbook.WordBookRepositoryLocal
import com.example.learningenglish.word.model.word.WordRepositoryLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun LearningViewModelFactory(context: Context): ViewModelProvider.Factory {
    val database = AppDatabase.getInstance(context)
    return CommonViewModelFactory(createFunc = {
        LearningViewModel(
            wordBookRepositoryLocal = WordBookRepositoryLocal(context),
            wordRepositoryLocal = WordRepositoryLocal(context),
            learningPlanRepository = LearningPlanRepository(),
            studyRecordRepository = StudyRecordRepository(context)
        )
    })
}

// LearningViewModel.kt
class LearningViewModel(
    private val wordBookRepositoryLocal: WordBookRepositoryLocal,
    private val wordRepositoryLocal: WordRepositoryLocal,
    private val learningPlanRepository: LearningPlanRepository,
    private val studyRecordRepository: StudyRecordRepository
) : BaseViewModel() {

    private val _currentWords = MutableStateFlow<List<WordEntity>>(emptyList())
    val currentWords: StateFlow<List<WordEntity>> = _currentWords

    private val _dailyWordCount = MutableStateFlow(0)
    val dailyWordCount: StateFlow<Int> = _dailyWordCount

    fun setDailyWordCount(count: Int) {
        _dailyWordCount.value = count
    }

    fun startLearning(bookId: Long, dailyWordCount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val words = wordRepositoryLocal.getWordsByBookId(bookId)
            val learningPlan = LearningPlanEntity(
                bookId = bookId,
                wordCount = words.size,
                dailyWordCount = dailyWordCount,
                startDate = System.currentTimeMillis()
            )
            learningPlanRepository.saveLearningPlan(learningPlan)
            _currentWords.value = words.take(dailyWordCount)
        }
    }

    fun saveDailyLearningProgress() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentWords = _currentWords.value
            val userId = AppUserManager.instance().userId()
            currentWords.forEach { word ->
                val record = WordStudyRecord(
                    userId = userId,
                    bookId = word.bookId,
                    wordId = word.id,
                    stage = 0,
                    strange = 0,
                    createTime = System.currentTimeMillis(),
                    lastReviewTime = System.currentTimeMillis(),
                    updateTime = System.currentTimeMillis(),
                )
                studyRecordRepository.saveReciteRecord(record)
            }
        }
    }
}
