package com.addzero.kmp.viewmodel.infra

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel

// SearchViewModel.kt - 使用 Factory 确保每次都创建新实例
@KoinViewModel
class SearchViewModel : ViewModel() {
    var keyword by mutableStateOf("")
        private set

    fun onKeywordChange(newQuery: String) {
        keyword = newQuery
    }
}
