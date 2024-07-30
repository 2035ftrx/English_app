package com.example.learningenglish.word.model.record

import com.example.learningenglish.word.model.IStudyType
import com.example.learningenglish.word.model.WordUI

interface IWordStudyRecord {
    suspend fun record(wordUI: WordUI, startTime: Long, studyType: IStudyType)
}