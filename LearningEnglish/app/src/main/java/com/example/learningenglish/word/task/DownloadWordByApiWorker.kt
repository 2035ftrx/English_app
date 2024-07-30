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
import com.example.learningenglish.http.word.WordRepository
import com.example.learningenglish.http.wordbook.WordBookRepository
import com.example.learningenglish.word.model.toEntity
import com.example.learningenglish.word.model.word.WordRepositoryLocal
import com.example.learningenglish.word.model.wordbook.WordBookRepositoryLocal
import kotlinx.coroutines.delay
import timber.log.Timber


class DownloadWordByApiWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    companion object {
        private const val TAG = "DownloadWordByApiWorker"
        private const val WORD_BOOK_ID = "wordBookId"
        fun action(context: Context, wordBookId: Long) {

            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            val inputData = Data.Builder().putLong(WORD_BOOK_ID, wordBookId).build()

            val workRequest =
                OneTimeWorkRequestBuilder<DownloadWordByApiWorker>().setConstraints(constraints)
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

            downloadWordBook(bookId)

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
        Timber.d(" book complete : $book , $localSaveWordsSize ")
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

    private suspend fun downloadWordBook(bookId: Long, page: Int = 0, size: Int = 200) {
        Timber.d(" download is continue , data progress page : $page ")
        val result = wordRepository.list(bookId, page, size)
        result.getOrThrow().let {
            if (it.isSuccess) {

                val wordBook = wordBookRepositoryLocal.findById(bookId)
                val apiPageResponse = it.data
                Timber.d(" download is success , response page info : ${apiPageResponse.currentPage} / ${apiPageResponse.totalPages} , ${apiPageResponse.totalElements} ")

                apiPageResponse.data
                    ?.mapNotNull { wordBook?.let { it1 -> it.toEntity(it1) } }
                    ?.let { wordRepositoryLocal.save(it) }
                    ?.let { Timber.d(" download is success , save word size : ${it}") }

                // 直到将所有分页跑完
                if (apiPageResponse.currentPage != apiPageResponse.totalPages - 1) {
                    delay(100)
                    downloadWordBook(bookId, page + 1, size)
                } else {
                    val allWordsSize = apiPageResponse.totalElements
                    val localSaveWordsSize = wordRepositoryLocal.queryCountByBookId(bookId)
                    Timber.d(" task is done , data compare : $localSaveWordsSize / $allWordsSize ")
                    if (allWordsSize != localSaveWordsSize.toLong()) {
                        throw DownloadException()
                    }
                }
            }
        }
    }

    private suspend fun cleanWordBook(bookId: Long) {
        // 删除单词
        wordRepositoryLocal.removeWordByBookId(bookId)
    }

}
