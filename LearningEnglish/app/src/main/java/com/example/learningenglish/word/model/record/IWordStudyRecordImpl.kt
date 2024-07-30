package com.example.learningenglish.word.model.record

import com.example.learningenglish.http.record.RecordRepository
import com.example.learningenglish.word.model.IStudyType
import com.example.learningenglish.word.model.WordUI
import timber.log.Timber

class IWordStudyRecordImpl(private val recordRepository: RecordRepository = RecordRepository()) :
    IWordStudyRecord {
    override suspend fun record(wordUI: WordUI, startTime: Long, studyType: IStudyType) {
        recordRepository.recordStudySession(
            wordUI.bookId,
            wordUI.wordId,
            startTime,
            System.currentTimeMillis(),
            studyType.value()
        )
            .onSuccess {
                Timber.d(" on word status : $it ")
            }
            .onFailure {
                Timber.e(" on word status : $it ")
            }
    }
}