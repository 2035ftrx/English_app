package com.example.learningenglish.ui.base.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

// 应该用不到，作为模板示例
class GenericViewModel<T : Any>(
    private val repository: GenericPagingRepository<T>
) : ViewModel() {

    val items: StateFlow<PagingData<T>> = repository.getPagingData()
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

}
