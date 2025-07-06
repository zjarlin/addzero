package com.addzero.kmp.ui.infra.components.loding

/**
 * 数据加载状态包装器
 */
class LoadingState<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
) {
    val isSuccess: Boolean get() = data != null && !isLoading && error == null
    val isError: Boolean get() = error != null && !isLoading

    companion object {
        fun <T> loading(): LoadingState<T> = LoadingState(isLoading = true)
        fun <T> success(data: T): LoadingState<T> = LoadingState(data = data)
        fun <T> error(error: Throwable): LoadingState<T> = LoadingState(error = error)
    }
}
