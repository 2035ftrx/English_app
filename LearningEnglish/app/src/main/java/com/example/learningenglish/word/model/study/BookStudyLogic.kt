package com.example.learningenglish.word.model.study

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitError
import com.example.learningenglish.ui.base.emitSuccess
import com.example.learningenglish.ui.base.flatMapLoadingSuccess
import com.example.learningenglish.word.model.IStudyType
import com.example.learningenglish.word.model.IWordStatus
import com.example.learningenglish.word.model.record.IWordStudyStateChange
import com.example.learningenglish.word.model.record.IWordStudyStateImpl
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.model.WordUI
import com.example.learningenglish.word.model.word.IWordRepository
import com.example.learningenglish.word.model.wordbook.IWordBookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

class BookStudyLogic(
    private val wordId: Long,
    private val wordRepository: IWordRepository,
    private val wordBookRepository: IWordBookRepository,
    private val iWordStudyStateChange: IWordStudyStateChange = IWordStudyStateImpl()
) : IStudyLogic {

    private val bookUI: MutableState<WordBookUI?> = mutableStateOf(null)

    private val wordUI = mutableStateListOf<WordUI>()

    override fun getInitialData(): Flow<LoadingData<StudyInitData>> {
        return flow {
            wordRepository
                .getWordById(wordId)
                .flatMapLoadingSuccess()
                .flatMapLatest { word ->
                    val bookId = word.bookId
                    wordBookRepository.getWordBookById(bookId)
                        .flatMapLoadingSuccess { Pair(word, it) }
                }
                .catch {
                    it.printStackTrace()
                    it.message?.let { it1 -> emitError(it1) }
                }
                .collect { pair ->
                    val (word, book) = pair
                    bookUI.value = book
                    StudyInitData(
                        pageCount = book.wordNum,
                        initialPage = word.wordRank,
                        book = book,
                        studyType = IStudyType.Learn
                    ).apply {
                        emitSuccess(this)
                    }
                }
        }
    }

    override fun getWordByPage(page: Int): Flow<LoadingData<WordUI>> {
        return bookUI.value?.let { book ->
            wordUI.firstOrNull { it.wordRank == page + 1 }?.let {
                flowOf(LoadingData.Success(it))
            } ?: let {
                wordRepository.getWordByRank(page + 1, book.id)
                    .onEach {
                        when (it) {
                            is LoadingData.Error -> {}
                            is LoadingData.Loading -> {}
                            is LoadingData.Success -> {
                                wordUI.add(it.getValue())
                            }
                        }
                    }
            }
        } ?: let {
            flowOf(LoadingData.Error("Please init bookUI first ."))
        }
    }

    override suspend fun onWordStatusChange(it: WordUI, iWordStatus: IWordStatus) {
        iWordStudyStateChange.onWordStatusChange(it, iWordStatus)
    }

}