package com.example.learningenglish.word.study.backup

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudyProgressRepository(private val context: Context) {

    private val studyPlanPreferences = StudyProgress

    fun getStudyProgress(bookId: Long): Flow<Int> {
        return StudyProgress.getStudyProgress(context, bookId)
    }

    suspend fun saveStudyProgress(bookId: Long, studyIndex: Int) {
        StudyProgress.saveStudyProgress(context, bookId, studyIndex)
    }

}


private object StudyProgress {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "StudyBookProgress")

    suspend fun saveStudyProgress(context: Context, bookId: Long, studyIndex: Int) {
        context.dataStore.edit { preferences ->
            preferences[intPreferencesKey("book_progress_$bookId")] = studyIndex
        }
    }

    fun getStudyProgress(context: Context, bookId: Long): Flow<Int> {
        return context.dataStore.data
            .map { preferences ->
                val studyIndex = preferences[intPreferencesKey("book_progress_$bookId")]
                studyIndex ?: 0
            }
    }

}