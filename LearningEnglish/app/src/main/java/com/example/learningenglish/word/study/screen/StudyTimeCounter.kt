package com.example.learningenglish.word.study.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.learningenglish.database.AppDatabase
import com.example.learningenglish.database.StudyTimeRecord
import com.example.learningenglish.user.usermanager.AppUserManager
import kotlinx.coroutines.launch

@Composable
fun StudyTimeCounter(wordId: Long) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(key1 = wordId) {
        val startTime = System.currentTimeMillis()
        onDispose() {
            val endTime = System.currentTimeMillis()
            val dao = AppDatabase.getInstance(context).studyTimeRecordDao()
            val userId = AppUserManager.instance().userId()
            coroutineScope.launch {
                dao.insertReciteRecord(
                    StudyTimeRecord(
                        userId = userId,
                        wordId = wordId,
                        startTime = startTime,
                        lastTime = endTime
                    )
                )
            }
        }
    }
}
