package com.example.learningenglish.word.plan

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.learningenglish.word.model.StudyPlan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class StudyPlanRepositoryLocal(private val context: Context) {
    private val studyPlanPreferences = StudyPlanPreferences

    suspend fun getPlanByBookId(bookId: Long): StudyPlan? {
        val first = studyPlanPreferences.getStudyPlan(context).first()
        return first?.takeIf { it.bookId == bookId }
    }

    suspend fun saveStudyPlan(studyPlan: StudyPlan) {
        studyPlanPreferences.saveStudyPlan(context, studyPlan)
    }

    suspend fun getCurrentPlan(): StudyPlan? {
        return studyPlanPreferences.getStudyPlan(context).firstOrNull()
    }

    fun queryCurrentPlan(): Flow<StudyPlan?> {
        return studyPlanPreferences.getStudyPlan(context)
    }
}


private object StudyPlanPreferences {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "study_plan")

    private val BOOK_ID = longPreferencesKey("book_id")
    private val DAILY_WORDS = intPreferencesKey("daily_words")
    private val TOTAL_DAYS = intPreferencesKey("total_days")

    suspend fun saveStudyPlan(context: Context, studyPlan: StudyPlan) {
        context.dataStore.edit { preferences ->
            preferences[BOOK_ID] = studyPlan.bookId
            preferences[DAILY_WORDS] = studyPlan.dailyWords
            preferences[TOTAL_DAYS] = studyPlan.totalDays
        }
    }

    fun getStudyPlan(context: Context): Flow<StudyPlan?> {
        return context.dataStore.data
            .map { preferences ->
                val bookId = preferences[BOOK_ID]
                val dailyWords = preferences[DAILY_WORDS]
                val totalDays = preferences[TOTAL_DAYS]

                if (bookId != null && dailyWords != null && totalDays != null) {
                    StudyPlan(bookId, dailyWords, totalDays)
                } else {
                    null
                }
            }
    }

}
