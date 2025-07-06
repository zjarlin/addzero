package com.addzero.kmp.component.text

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier

/**
 * 修复SelectionContainer层次结构问题的工具
 */

/**
 * 安全的SelectionContainer包装器
 * 避免嵌套SelectionContainer导致的层次结构冲突
 */
@Composable
fun SafeSelectionContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // 使用CompositionLocal来跟踪是否已经在SelectionContainer中
    val isInSelectionContainer = LocalSelectionContainerState.current
    
    if (isInSelectionContainer) {
        // 如果已经在SelectionContainer中，直接渲染内容
        content()
    } else {
        // 如果不在SelectionContainer中，创建新的
        CompositionLocalProvider(LocalSelectionContainerState provides true) {
            SelectionContainer(modifier = modifier) {
                content()
            }
        }
    }
}

/**
 * 用于跟踪SelectionContainer状态的CompositionLocal
 */
private val LocalSelectionContainerState = compositionLocalOf { false }

/**
 * 检查当前是否在SelectionContainer中
 */
@Composable
fun isInSelectionContainer(): Boolean {
    return LocalSelectionContainerState.current
}

/**
 * 条件性的SelectionContainer
 * 只有在需要时才创建SelectionContainer
 */
@Composable
fun ConditionalSelectionContainer(
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (enabled && !isInSelectionContainer()) {
        SafeSelectionContainer(modifier = modifier) {
            content()
        }
    } else {
        content()
    }
}

/**
 * 重置SelectionContainer状态的组件
 * 用于在新的组合层次中重新开始
 */
@Composable
fun ResetSelectionContainer(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalSelectionContainerState provides false) {
        content()
    }
}
