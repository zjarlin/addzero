package com.addzero.kmp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.util.collections.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel : ViewModel() {
    private val _loadStatePool = MutableStateFlow(ConcurrentMap<String, LoadState>())
    val loadState = _loadStatePool.asStateFlow()

    /**
     * 更新状态
     */
    fun updateLoadState(id: String, state: LoadState) {
        val copy = ConcurrentMap<String, LoadState>()
        copy.putAll(_loadStatePool.value)
        copy[id] = state
        _loadStatePool.value = copy
    }

    fun clearLoadState(id: String) {
        val copy = ConcurrentMap<String, LoadState>()
        copy.putAll(_loadStatePool.value)
        copy.remove(id)
        _loadStatePool.value = copy
    }

    /**
     * 启动一个用于IO操作的协程
     * @param id 协程的ID
     * @param onError Error回调
     * @param block 执行体
     */
    fun suspendLaunch(
        id: String,
        onError: BaseViewModelScope.(Throwable) -> Unit = {},
        onComplete: BaseViewModelScope.() -> Unit = {},
        block: suspend BaseViewModelScope.(CoroutineScope) -> Unit
    ) {
        val scope = object : BaseViewModelScope {
            override val id: String = id
        }
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                onError(scope, throwable)
                onComplete(scope)
            }
        ) {
            withContext(Dispatchers.Main) {
                scope.block(this)
                onComplete(scope)
            }
        }
    }

    interface BaseViewModelScope {
        val id: String
    }

    fun BaseViewModelScope.updateLoadState(state: LoadState) {
        val copy = ConcurrentMap<String, LoadState>()
        copy.putAll(_loadStatePool.value)
        copy[id] = state
        _loadStatePool.value = copy
    }

    fun BaseViewModelScope.clearLoadState() {
        val copy = ConcurrentMap<String, LoadState>()
        copy.putAll(_loadStatePool.value)
        copy.remove(id)
        _loadStatePool.value = copy
    }

    fun BaseViewModelScope.loading() {
        updateLoadState(LoadState.Loading())
    }

    fun BaseViewModelScope.success(
        message: String,
    ) {
        updateLoadState(LoadState.Success(message = message))
    }

    fun BaseViewModelScope.fail(message: String) {
        updateLoadState(LoadState.Fail(message = message))
    }
}