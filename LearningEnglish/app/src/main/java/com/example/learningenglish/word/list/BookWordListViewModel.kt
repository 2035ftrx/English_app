package com.example.learningenglish.word.list

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningenglish.http.word.WordRepository
import com.example.learningenglish.ui.base.BaseViewModel
import com.example.learningenglish.ui.base.CommonViewModelFactory
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.base.emitError
import com.example.learningenglish.ui.base.emitLoading
import com.example.learningenglish.ui.base.emitSuccess
import com.example.learningenglish.word.model.SelectedBookUI
import com.example.learningenglish.word.model.toEntity
import com.example.learningenglish.word.model.toUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun BookWordListViewModelFactory(context: Context): ViewModelProvider.Factory {
    return CommonViewModelFactory(createFunc = {
        BookWordListViewModel(
            WordRepository(),
        )
    })
}

class BookWordListViewModel(
    private val wordRepository: WordRepository,
) : BaseViewModel() {

    private val _wordsList =
        MutableStateFlow<LoadingData<List<SelectedBookUI>>>(LoadingData.Loading())
    val wordsList: StateFlow<LoadingData<List<SelectedBookUI>>> = _wordsList

    init {
        // load all books from remote
        loadAllBooks()
    }

    private fun loadAllBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            _wordsList.emitLoading()

        }

    }


}