package com.addzero.kmp.demo.testviewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.android.annotation.KoinViewModel

// SearchViewModel.kt
@KoinViewModel
class SearchViewModel : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }
}

// PageAViewModel.kt
@KoinViewModel
class PageAViewModel(private val searchVM: SearchViewModel) : ViewModel() {
    fun onQueryChanged(query: String) {
        searchVM.updateQuery(query)
    }
}

// PageBViewModel.kt
@KoinViewModel
class PageBViewModel(private val searchVM: SearchViewModel) : ViewModel() {
    val query: StateFlow<String> = searchVM.query
}
