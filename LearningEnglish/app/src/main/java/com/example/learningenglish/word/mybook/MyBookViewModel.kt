package com.example.learningenglish.word.mybook

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningenglish.ui.base.BaseViewModel
import com.example.learningenglish.ui.base.CommonViewModelFactory
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitError
import com.example.learningenglish.ui.base.emitLoading
import com.example.learningenglish.ui.base.emitSuccess
import com.example.learningenglish.word.model.wordbook.WordBookRepositoryLocal
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.model.toEntity
import com.example.learningenglish.word.model.word.WordRepositoryLocal
import com.example.learningenglish.word.model.toUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun MyBookViewModelFactory(context: Context): ViewModelProvider.Factory {
    return CommonViewModelFactory(createFunc = {
        MyBookViewModel(
            WordBookRepositoryLocal(context), WordRepositoryLocal(context)
        )
    })
}

class MyBookViewModel(
    private val wordBookRepositoryLocal: WordBookRepositoryLocal,
    private val wordRepositoryLocal: WordRepositoryLocal,
) : BaseViewModel() {

    private val _wordBookList =
        MutableStateFlow<LoadingData<List<WordBookUI>>>(LoadingData.Loading())
    val wordBookList: StateFlow<LoadingData<List<WordBookUI>>> = _wordBookList

    init {
        // load all books from remote
        loadAllBooks()
    }

    private fun loadAllBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            _wordBookList.emitLoading()
            wordBookRepositoryLocal.queryAll()
                .collect {
                    if (it.isEmpty()) {
                        _wordBookList.emitError(" Empty Data ! ")
                    } else {
                        _wordBookList.emitSuccess(it.map { it.toUI() })
                    }
                }
        }

    }

    fun removeBook(it: WordBookUI) {

        viewModelScope.launch {
            wordBookRepositoryLocal.remove(it.toEntity())
            // todo remove this code .
            wordRepositoryLocal.removeWordByBookId(it.id)
        }

    }


}