package com.example.learningenglish.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory<T1 : ViewModel?>(private val initer: () -> T1? = { null }) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return initer() as? T ?: modelClass.newInstance()
    }
}


class CommonViewModelFactory<T : ViewModel>(
    private val createFunc: () -> T? = { null }
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return createFunc() as? T ?: modelClass.newInstance()
    }
}
