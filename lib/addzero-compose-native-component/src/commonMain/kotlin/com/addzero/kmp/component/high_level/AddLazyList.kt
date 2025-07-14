package com.addzero.kmp.component.high_level

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * lazylist高阶组件
 * @param [items]
 * @param [key]
 * @param [itemContent]
 */
@Composable
fun <T> AddLazyList(
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable (T) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        this.items(items, key) {
            itemContent(it)
        }
    }
}
