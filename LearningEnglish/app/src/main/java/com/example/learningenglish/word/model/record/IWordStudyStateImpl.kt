package com.example.learningenglish.word.model.record

import com.example.learningenglish.http.record.RecordRepository
import com.example.learningenglish.word.model.IWordStatus
import com.example.learningenglish.word.model.WordUI
import timber.log.Timber

class IWordStudyStateImpl(
    private val recordRepository: RecordRepository = RecordRepository(),
) : IWordStudyStateChange {

    override suspend fun onWordStatusChange(it: WordUI, iWordStatus: IWordStatus) {
        recordRepository.updateStudyRecord(it.bookId, it.wordId, iWordStatus.value())
            .onSuccess {
                Timber.d(" on word status : $it ")
            }
            .onFailure {
                Timber.e(" on word status : $it ")
            }
    }
}