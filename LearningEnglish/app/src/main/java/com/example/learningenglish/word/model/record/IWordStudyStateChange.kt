package com.example.learningenglish.word.model.record

import com.example.learningenglish.word.model.IWordStatus
import com.example.learningenglish.word.model.WordUI

interface IWordStudyStateChange {

    suspend fun onWordStatusChange(it: WordUI, iWordStatus: IWordStatus)

}