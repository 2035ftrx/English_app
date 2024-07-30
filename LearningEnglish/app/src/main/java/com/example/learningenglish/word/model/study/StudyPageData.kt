package com.example.learningenglish.word.model.study

import com.example.learningenglish.word.model.IStudyType
import com.example.learningenglish.word.model.WordBookUI

data class StudyInitData(val initialPage: Int, val pageCount: Int, val book: WordBookUI,val studyType:IStudyType)
