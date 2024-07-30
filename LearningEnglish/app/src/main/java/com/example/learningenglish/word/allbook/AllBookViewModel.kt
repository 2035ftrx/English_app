package com.example.learningenglish.word.allbook

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningenglish.http.wordbook.WordBookRepository
import com.example.learningenglish.ui.base.BaseViewModel
import com.example.learningenglish.ui.base.CommonViewModelFactory
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitError
import com.example.learningenglish.ui.base.emitLoading
import com.example.learningenglish.ui.base.emitSuccess
import com.example.learningenglish.word.model.SelectedBookUI
import com.example.learningenglish.word.model.toEntity
import com.example.learningenglish.word.model.toUI
import com.example.learningenglish.word.model.wordbook.WordBookRepositoryLocal
import com.example.learningenglish.word.task.IAddBookTask
import com.example.learningenglish.word.task.IAddBookTaskImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun AllBookViewModelFactory(context: Context): ViewModelProvider.Factory {
    return CommonViewModelFactory(createFunc = {
        AllBookViewModel(
            WordBookRepository(),
            WordBookRepositoryLocal(context),
            IAddBookTaskImpl(context.applicationContext)
        )
    })
}

class AllBookViewModel(
    private val wordBookRepository: WordBookRepository,
    private val wordBookRepositoryLocal: WordBookRepositoryLocal,
    private val iAddBookTask: IAddBookTask,
) : BaseViewModel() {

    private val _wordBookList =
        MutableStateFlow<LoadingData<List<SelectedBookUI>>>(LoadingData.Loading())
    val wordBookList: StateFlow<LoadingData<List<SelectedBookUI>>> = _wordBookList

    init {
        // load all books from remote
        loadAllBooks()
    }

    private fun loadAllBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            _wordBookList.emitLoading()
            wordBookRepository.list()
                .onSuccess {
                    if (it.isSuccess) {
                        val data = it.data.data
                        val uis = data
                            .map { it.toUI() }
                            .map { ui ->
                                wordBookRepositoryLocal.findById(ui.id)?.let {
                                    SelectedBookUI(ui, true)
                                } ?: let {
                                    SelectedBookUI(ui, false)
                                }
                            }
                        _wordBookList.emitSuccess(uis)
                    } else {
                        _wordBookList.emitError(it.message)
                    }
                }
                .onFailure {
                    it.message?.let { _wordBookList.emitError(it) }
                }
        }

    }

    fun addBook(it: SelectedBookUI) {

        viewModelScope.launch(Dispatchers.IO) {
            wordBookRepositoryLocal.insert(it.book.toEntity())
            loadAllBooks()
        }
        iAddBookTask.onAdd(it.book.id)

    }


}