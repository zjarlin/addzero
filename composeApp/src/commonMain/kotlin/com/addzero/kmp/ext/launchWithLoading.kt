package com.addzero.kmp.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.addzero.kmp.component.toast.ToastManager
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
//            runCatching {
            ToastManager.error(e.message.toString())
//            }
            onError(e)
        } finally {
            if (loadingState != null) {
                onLodingChange(false)
            }
        }
    }
}
