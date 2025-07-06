package com.addzero.kmp.ui.infra.components.loding

import androidx.compose.runtime.Composable

/**
 * 通用加载内容组件
 */
@Composable
fun <T> LoadingContent(
    state: LoadingState<T>,
    onRetry: () -> Unit = {},
    loadingContent: @Composable () -> Unit = { DefaultLoadingIndicator() },
    errorContent: @Composable (Throwable) -> Unit = { DefaultErrorMessage(it, onRetry) },
    content: @Composable (T) -> Unit
) {
    when {
        state.isLoading -> loadingContent()
        state.isError -> errorContent(state.error!!)
        state.isSuccess -> content(state.data!!)
    }
}
