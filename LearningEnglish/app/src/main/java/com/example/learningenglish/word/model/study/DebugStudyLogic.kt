package com.example.learningenglish.word.model.study

import com.example.learningenglish.test.testBookData
import com.example.learningenglish.test.testWordData
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitSuccess
import com.example.learningenglish.word.model.IStudyType
import com.example.learningenglish.word.model.IWordStatus
import com.example.learningenglish.word.model.WordUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class DebugStudyLogic : IStudyLogic {

    override fun getInitialData(): Flow<LoadingData<StudyInitData>> {
        val pageCount = Random.nextInt()
        val initialPage = Random.nextInt(pageCount)
        return flow {
            emitSuccess(
                StudyInitData(
                    pageCount = pageCount,
                    initialPage = initialPage,
                    book = testBookData.random(),
                    studyType = IStudyType.Learn
                )
            )
        }
    }

    override suspend fun onWordStatusChange(it: WordUI, iWordStatus: IWordStatus) {

    }

    override fun getWordByPage(page: Int): Flow<LoadingData<WordUI>> {
        return flow {
            emitSuccess(testWordData.random())
        }
    }
}