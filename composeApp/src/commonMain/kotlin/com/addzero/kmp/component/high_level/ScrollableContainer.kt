package com.addzero.kmp.component.high_level

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 可滚动容器高阶组件
 * 根据参数控制滚动方向（水平或垂直），并在滚动时显示滚动条
 *
 * @param orientation 滚动方向，"horizontal"为水平滚动，"vertical"为垂直滚动
 * @param modifier 可选的修饰符
 * @param showScrollbar 是否显示滚动条
 * @param content 内容组件
 */
@Composable
fun ScrollableContainer(
    orientation: Orientation = Orientation.Vertical, modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()

    // 根据方向应用不同的滚动修饰符
    val contentModifier = when (orientation) {
        Orientation.Horizontal -> Modifier.horizontalScroll(scrollState)
        else -> Modifier.verticalScroll(scrollState)
    }

    Box(modifier = modifier) {
        // 渲染内容 - 移除 fillMaxHeight() 以避免无限高度问题
        // 让内容根据实际需要确定高度，同时支持滚动
        Box(modifier = contentModifier.fillMaxWidth()) {
            content()
        }
    }
}