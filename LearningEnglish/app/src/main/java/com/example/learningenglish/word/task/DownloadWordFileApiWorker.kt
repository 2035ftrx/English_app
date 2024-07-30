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
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream


class DownloadWordFileApiWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    companion object {
        private const val TAG = "DownloadWordFileApiWorker"
        private const val WORD_BOOK_ID = "wordBookId"
        fun action(context: Context, wordBookId: Long) {

            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            val inputData = Data.Builder().putLong(WORD_BOOK_ID, wordBookId).build()

            val workRequest =
                OneTimeWorkRequestBuilder<DownloadWordFileApiWorker>().setConstraints(constraints)
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

            parseWordBook(wordsFile, bookId)

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

    private suspend fun downloadWordBook1(bookId: Long): File {
        val result = wordRepository.allwordsfile(bookId)
        val response = result.getOrThrow()
        val jsonFile = writeResponseBodyToDisk(response)
        Timber.d("File download was a success?  ${jsonFile.length()}  $jsonFile ")
        return jsonFile
    }

    private fun writeResponseBodyToDisk(body: ResponseBody): File {
        val jsonFile = File.createTempFile("words", ".json")
        val fileReader = ByteArray(4096)
        Timber.d(" writeResponseBodyToDisk: ${body.contentLength()} ${jsonFile.length()}")
        body.byteStream().use { inputStream ->
            FileOutputStream(jsonFile).use { outputStream ->
                while (true) {
                    val read = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                }
                outputStream.flush()
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
