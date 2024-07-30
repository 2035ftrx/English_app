package com.example.learningenglish.word.task

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.learningenglish.http.word.Word
import com.example.learningenglish.http.word.WordRepository
import com.example.learningenglish.http.wordbook.WordBookRepository
import com.example.learningenglish.utils.JsonParse
import com.example.learningenglish.word.model.toEntity
import com.example.learningenglish.word.model.wordbook.WordBookRepositoryLocal
import com.example.learningenglish.word.model.word.WordRepositoryLocal
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DownloadWordWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    companion object {
        private const val TAG = "DownloadWordWorker"
        private const val WORD_BOOK_ID = "wordBookId"
        fun action(context: Context, wordBookId: Long) {

            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            val inputData = Data.Builder().putLong(WORD_BOOK_ID, wordBookId).build()

            val workRequest =
                OneTimeWorkRequestBuilder<DownloadWordWorker>().setConstraints(constraints)
                    .setInputData(inputData).build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                TAG + wordBookId, ExistingWorkPolicy.REPLACE, workRequest
            ).state.observeForever {
                Timber.tag(TAG).i(" enqueueUniqueWork $TAG action: $it")
            }

        }
    }

    private val wordBookRepositoryLocal by lazy {
        WordBookRepositoryLocal(applicationContext)
    }

    private val wordRepositoryLocal by lazy {
        WordRepositoryLocal(applicationContext)
    }

    private val wordRepository by lazy { WordRepository() }
    private val wordBookRepository by lazy { WordBookRepository() }

    override suspend fun doWork(): Result {
        val bookId = inputData.getLong(WORD_BOOK_ID, 0)
        Timber.d(" doWork bookId : $bookId ")
        if (bookId <= 0) {
            return Result.failure()// input data error
        }
        try {
            if (checkBookComplete(bookId)) {
                return Result.success()
            }

            // 先清除掉旧数据，以免影响
            cleanWordBook(bookId)

            val wordsFile = downloadWordBook1(bookId)

            wordsFile?.let { parseWordBook(it, bookId) }

            if (!checkBookComplete(bookId)) {
                return Result.retry()
            }

            return Result.success()
        } catch (e: Exception) {
            Timber.e(" download is fail , error : ${e.message}")
            e.printStackTrace()
            delay(1000)
            return Result.retry()
        }
    }

    private suspend fun checkBookComplete(bookId: Long): Boolean {
        val book = wordBookRepositoryLocal.findById(bookId)
        val localSaveWordsSize = wordRepositoryLocal.queryCountByBookId(bookId)
        Timber.d(" book complete :  $localSaveWordsSize , $book ")
        book?.let {
            return book.wordNum == localSaveWordsSize
        } ?: let {
            val bookRemote = wordBookRepository.get(bookId)
            if (bookRemote.isSuccess) {
                val wordBookEntity = bookRemote.getOrThrow().data.toEntity()
                wordBookRepositoryLocal.insert(wordBookEntity)
                return wordBookEntity.wordNum == localSaveWordsSize
            } else {
                return checkBookComplete(bookId)
            }
        }
    }

    private suspend fun downloadWordBook1(bookId: Long): File? {
        val request: Request = Request.Builder()
            .url("http://192.168.3.4:8099/app/words/allwordsfile?bookId=$bookId")
            .build()
        try {
            val okHttpClient = OkHttpClient()
            val newCall = okHttpClient.newCall(request)
            newCall.execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }
                val body: ResponseBody? = response.body
                if (body != null) {
                    val jsonFile = writeResponseBodyToDisk(body)
                    Timber.d("File download was a success?  ${jsonFile.length()}  $jsonFile ")
                    return jsonFile
                } else {
                    println("Response body is null")
                    return null
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    private fun writeResponseBodyToDisk(body: ResponseBody): File {
        val jsonFile = File.createTempFile("words", ".json")
        Timber.d(" writeResponseBodyToDisk: ${body.contentLength()} ${jsonFile.length()}")
        body.byteStream().use { inputStream ->
            FileOutputStream(jsonFile).use { outputStream ->
                inputStream.copyTo(outputStream, bufferSize = 8192)
            }
        }
        Timber.d(" writeResponseBodyToDisk: ${body.contentLength()} ${jsonFile.length()}")
        return jsonFile
    }

    private suspend fun parseWordBook(wordsFile: File, bookId: Long) {

        val readText = wordsFile.readText()
        val wordBook = wordBookRepositoryLocal.findById(bookId)

        val words: List<Word> =
            JsonParse.fromJson(readText, object : TypeToken<List<Word>>() {}.type)

        wordBook?.let {
            words.map { it.toEntity(wordBook) }.let {
                wordRepositoryLocal.save(it)
            }
        }

    }

    private suspend fun cleanWordBook(bookId: Long) {
        // 删除单词
        wordRepositoryLocal.removeWordByBookId(bookId)
    }

}
