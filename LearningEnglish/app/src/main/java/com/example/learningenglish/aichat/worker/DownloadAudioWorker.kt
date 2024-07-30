package com.example.learningenglish.aichat.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import timber.log.Timber


class DownloadAudioWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    companion object {
        private const val TAG = "DownloadAudioWorker"
        private const val CHAT_RECORD_ID = "CHAT_RECORD_ID"
        fun action(context: Context, chatRecordId: Long) {

            val constraints =
                Constraints.Builder().build()

            val inputData = Data.Builder()
                .putLong(CHAT_RECORD_ID, chatRecordId)
                .build()

            val workRequest =
                OneTimeWorkRequestBuilder<DownloadAudioWorker>()
                    .setConstraints(constraints)
                    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                    .setInputData(inputData)
                    .build()


            WorkManager.getInstance(context)
                .enqueue(workRequest)
                .state.observeForever {
                    Timber.tag(TAG).i(" enqueueUniqueWork $TAG action: $it")
                }

        }
    }

    override suspend fun doWork(): Result {
        val chatRecordId = inputData.getLong(CHAT_RECORD_ID, 0)
        Timber.d(" doWork chatRecordId : $chatRecordId ")
        if (chatRecordId <= 0) {
            return Result.failure()// input data error
        }
        //val id = id
        return Result.success()
    }

}
