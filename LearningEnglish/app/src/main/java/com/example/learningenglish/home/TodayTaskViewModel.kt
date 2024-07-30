package com.example.learningenglish.home

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningenglish.http.study.StudyRepository
import com.example.learningenglish.ui.base.BaseViewModel
import com.example.learningenglish.ui.base.CommonViewModelFactory
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitError
import com.example.learningenglish.ui.base.emitLoading
import com.example.learningenglish.ui.base.emitSuccess
import com.example.learningenglish.ui.base.flatMapResultFlow
import com.example.learningenglish.word.plan.StudyPlanRepositoryLocal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun TodayTaskViewModelFactory(context: Context): ViewModelProvider.Factory {
    return CommonViewModelFactory(createFunc = {
        TodayTaskViewModel(
            StudyPlanRepositoryLocal(context.applicationContext),
            StudyRepository(),
        )
    })
}

class TodayTaskViewModel(
    private val studyPlanRepositoryLocal: StudyPlanRepositoryLocal,
    private val studyRepository: StudyRepository
) : BaseViewModel() {

    // 界面元素
    private val _todayTask = MutableStateFlow<LoadingData<TodayTaskUI>>(LoadingData.Loading())
    val todayTask: StateFlow<LoadingData<TodayTaskUI>> = _todayTask


    init {
        loadData()
    }

    // 根据记录的学习计划，得到单词本的信息
    // 根据计划内容，以及已学习的记录，计算出待复习的单词数，以及今日待学单词数，以及整本单词的学习进度，还有根据 艾宾斯模型，计算出单词本的复习进度

    fun loadData() {
        viewModelScope.launch {
            studyPlanRepositoryLocal.queryCurrentPlan()
                .onEach {
                    if (it == null) {
                        _todayTask.emitSuccess(TodayTaskUI("", 0, 0, 0, 0, false))
                    }
                }
                .filterNotNull()
                .flatMapLatest { plan ->
                    studyRepository
                        .getTodayTaskInfo(plan.bookId)
                        .flatMapResultFlow { Pair(plan, it) }
                }
                .collect {
                    when (it) {
                        is LoadingData.Error -> {
                            _todayTask.emitError(it.getMessageNonNull("加载失败！"))
                        }

                        is LoadingData.Loading -> {
                            _todayTask.emitLoading()
                        }

                        is LoadingData.Success -> {
                            val (plan, info) = it.getValue()
                            val data = TodayTaskUI(
                                bookName = info.bookName.title,
                                learnedWordCount = info.learnedWordCount,
                                totalWordCount = info.totalWordCount,
                                todayWordCount = plan.dailyWords - info.todayWordCount,
                                todayReviewWordCount = info.todayReviewWordCount,
                                hasPlan = true
                            )
                            _todayTask.emitSuccess(data)
                        }

                    }

                }
        }

    }


}