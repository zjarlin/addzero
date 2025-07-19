package com.addzero.kmp.assist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun ViewModel.api(
    loadingState: Boolean? = null,
    crossinline onLodingChange: (Boolean) -> Unit = {},
    crossinline onError: (Throwable) -> Unit = {},
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    this.viewModelScope.api(loadingState, onLodingChange, onError, block)

}

/**
 * api调用
 * @param [loadingState]
 * @param [block]
 * @param [onError]
 */
inline fun CoroutineScope.api(
    loadingState: Boolean? = null,
    crossinline onLodingChange: (Boolean) -> Unit = {},
    crossinline onError: (Throwable) -> Unit = {},
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    this.launch {
        if (loadingState != null) {
            onLodingChange(true)
        }
        try {
            block()
        } catch (e: Throwable) {
            e.printStackTrace()
            onError(e)
        } finally {
            if (loadingState != null) {
                onLodingChange(false)
            }
        }
    }
}
