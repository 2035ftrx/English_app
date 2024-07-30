package com.example.learningenglish.word.model.wordbook

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningenglish.ui.base.BaseViewModel
import com.example.learningenglish.ui.base.CommonViewModelFactory
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitSuccess
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.model.toUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

fun LocalAllWordBookViewModelFactory(context: Context): ViewModelProvider.Factory {
    return CommonViewModelFactory(createFunc = {
        LocalAllWordBookViewModel(
            WordBookRepositoryLocal(
                context
            )
        )
    })
}

class LocalAllWordBookViewModel(private val wordBookRepositoryLocal: WordBookRepositoryLocal) :
    BaseViewModel() {

    private val _wordBookList =
        MutableStateFlow<LoadingData<List<WordBookUI>>>(LoadingData.Loading())
    val wordBookList: StateFlow<LoadingData<List<WordBookUI>>> = _wordBookList

    init {
        loadSelectedBooks()
    }

    private fun loadSelectedBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            wordBookRepositoryLocal.queryAll()
                .map { it.map { it.toUI() } }
                .collect {
                    _wordBookList.emitSuccess(it)
                }
        }
    }

}

