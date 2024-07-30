package com.example.learningenglish.word.model.study

import com.example.learningenglish.http.study.StudyRepository
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitError
import com.example.learningenglish.ui.base.emitLoading
import com.example.learningenglish.ui.base.emitSuccess
import com.example.learningenglish.ui.base.flatMapResultFlow
import com.example.learningenglish.word.model.IStudyType
import com.example.learningenglish.word.model.IWordStatus
import com.example.learningenglish.word.model.record.IWordStudyStateChange
import com.example.learningenglish.word.model.record.IWordStudyStateImpl
import com.example.learningenglish.word.model.WordUI
import com.example.learningenglish.word.model.toUI
import com.example.learningenglish.word.plan.StudyPlanRepositoryLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class ReviewStudyLogic(
    private val planRepositoryLocal: StudyPlanRepositoryLocal,
    private val studyRepository: StudyRepository,
    private val iWordStudyStateChange: IWordStudyStateChange = IWordStudyStateImpl()
) : IStudyLogic {

    private val words = mutableListOf<WordUI>()

    override fun getInitialData(): Flow<LoadingData<StudyInitData>> {
        return flow {
            planRepositoryLocal.queryCurrentPlan()
                .filterNotNull()
                .flatMapLatest {
                    studyRepository
                        .getReviewTask(it.bookId)
                        .flatMapResultFlow { it }
                }
                .collect { w ->
                    when (w) {
                        is LoadingData.Error -> emitError(w.getMessageNonNull(""))
                        is LoadingData.Loading -> emitLoading()
                        is LoadingData.Success -> {
                            words.clear()
                            words.addAll(w.getValue().list.map { it.toUI() })
                            emitSuccess(
                                StudyInitData(
                                    initialPage = 0,
                                    pageCount = w.getValue().list.size,
                                    book = w.getValue().book.toUI(),
                                    studyType = IStudyType.Review
                                )
                            )
                        }
                    }

                }
        }
    }

    override fun getWordByPage(page: Int): Flow<LoadingData<WordUI>> {
        return flow {
            emitLoading()
            words.getOrElse(page) {
                words.firstOrNull()
            }?.let {
                emitSuccess(it)
            }
        }
    }

    override suspend fun onWordStatusChange(it: WordUI, iWordStatus: IWordStatus) {
        iWordStudyStateChange.onWordStatusChange(it, iWordStatus)
    }
}