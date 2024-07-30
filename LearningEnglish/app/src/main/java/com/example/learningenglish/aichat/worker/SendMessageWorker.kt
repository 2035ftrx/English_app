package com.example.learningenglish.aichat.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.learningenglish.aichat.database.AIChatDatabase
import com.example.learningenglish.aichat.database.AIChatRecord
import com.example.learningenglish.aichat.model.IChatRecordStatus
import com.example.learningenglish.aichat.model.IChatRole
import com.example.learningenglish.aichat.model.toRole
import com.example.learningenglish.http.ai.AIApiRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.util.concurrent.TimeUnit


class SendMessageWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    companion object {
        private const val TAG = "SendMessageWorker"
        private const val CONVERSATION_ID = "CONVERSATION_ID"
        private const val SEND_MESSAGE = "SEND_MESSAGE"
        fun action(context: Context, conversationId: Long, message: String? = null) {

            val constraints =
                Constraints.Builder().build()

            val inputData = Data.Builder()
                .putLong(CONVERSATION_ID, conversationId)
                .putString(SEND_MESSAGE, message)
                .build()

            val workRequest =
                OneTimeWorkRequestBuilder<SendMessageWorker>()
                    .setConstraints(constraints)
                    //.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                    .setInputData(inputData)
                    .addTag(tag = TAG)
                    .build()

            WorkManager.getInstance(context)
                .enqueue(workRequest)
                .state.observeForever {
                    Timber.tag(TAG).i(" enqueueUniqueWork $TAG action: $it")
                }

        }
    }

    private val chatRecordDao by lazy {
        AIChatDatabase.getInstance(context).aiChatRecordDao()
    }

    override suspend fun doWork(): Result {
        val conversationId = inputData.getLong(CONVERSATION_ID, 0)
        val message = inputData.getString(SEND_MESSAGE)
        Timber.d(" doWork conversationId : $conversationId ")
        if (conversationId <= 0) {
            return Result.failure()// input data error
        }

        if (!message.isNullOrEmpty()) {
            chatRecordDao.insert(
                AIChatRecord(
                    cId = conversationId,
                    msg = message,
                    role = IChatRole.User.value(),
                    status = IChatRecordStatus.Success.value(),
                    createdAt = System.currentTimeMillis()
                )
            )
        }

        val allRecordBy20 = chatRecordDao.getAllBy20(conversationId)

        // 插一条，先占位，然后再等接口完成。
        val record = AIChatRecord(
            cId = conversationId,
            msg = "Loading",
            role = IChatRole.Assistant.value(),
            status = IChatRecordStatus.Loading.value(),
            createdAt = System.currentTimeMillis()
        )
        val recordId = chatRecordDao.insert(record)

        try {
            val aiApiRepository = AIApiRepository()
            val tokenResponse = aiApiRepository.aiToken()
            if (tokenResponse.isSuccess) {
                val orThrow = tokenResponse.getOrThrow()
                if (orThrow.isSuccess) {

                    val aiTokenResponse = orThrow.data
                    val accessToken = aiTokenResponse.access_token

                    val url =
                        "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-speed-128k?access_token=$accessToken"

                    val messageJson = buildMessageJson(allRecordBy20)

                    sendMessage(url, messageJson)?.apply {
                        Timber.d(" send message response : $this ")
                    }?.let { JSONObject(it) }?.let {
                        val chatId = it.getString("id")
                        val chatContent = it.getString("result")
                        chatRecordDao.update(
                            record.copy(
                                id = recordId,
                                msg = chatContent,
                                status = IChatRecordStatus.Success.value(),
                                createdAt = System.currentTimeMillis()
                            )
                        )
                    } ?: let {
                        chatRecordDao.update(
                            record.copy(
                                id = recordId,
                                msg = "AI平台接口请求出错",
                                status = IChatRecordStatus.Failure.value()
                            )
                        )
                        return Result.failure()
                    }
                } else {
                    chatRecordDao.update(
                        record.copy(
                            id = recordId,
                            msg = orThrow.message,
                            status = IChatRecordStatus.Failure.value()
                        )
                    )
                    return Result.failure()
                }

            } else {
                chatRecordDao.update(
                    record.copy(
                        id = recordId,
                        status = IChatRecordStatus.Failure.value()
                    )
                )
                return Result.failure()
            }

            return Result.success()
        } catch (e: Exception) {
            Timber.e(" download is fail , error : ${e.message}")
            e.printStackTrace()
            chatRecordDao.update(
                record.copy(
                    id = recordId,
                    msg = e.message ?: "未知错误",
                    status = IChatRecordStatus.Failure.value()
                )
            )
            return Result.failure()
        }
    }

    private fun buildMessageJson(allRecordBy20: List<AIChatRecord>): String {
        val sb = StringBuilder()
        sb.append("{\"messages\":[")
        val conversationContents = StringBuilder()
        allRecordBy20.reversed()//.filter { it.status == IChatRecordStatus.Success.value() }
            .forEach {
                val roleName = it.role.toRole().name()
                conversationContents.append("{\"role\":\"$roleName\",\"content\":\"${it.msg}\"}")
                conversationContents.append(",")
            }
        sb.append(conversationContents.removeSuffix(","))
        sb.append("]}")
        return sb.toString()
    }


    private fun sendMessage(url: String, bodyJson: String): String? {
        val request = Request.Builder()
            .url(url)
            .post(bodyJson.toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()

        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw IOException("Error: ${response.code}")
        }

        return response.body?.string()
    }


}
