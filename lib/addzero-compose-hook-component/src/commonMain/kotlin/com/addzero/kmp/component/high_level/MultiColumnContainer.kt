package com.addzero.kmp.component.high_level

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 多列布局容器组件
 * @param columnCount 列数
 * @param size 总项目数
 * @param modifier 可选的修饰符
 * @param horizontalSpacing 水平间距
 * @param verticalSpacing 垂直间距
 * @param enableScroll 是否启用滚动
 * @param itemContent 根据索引渲染每个位置的内容
 */
@Composable
@Deprecated("该组件已被弃用，请使用AddMultiColumnContainer")
fun MultiColumnContainer(
    columnCount: Int = 2,
    size: Int,
    modifier: Modifier = Modifier,
    horizontalSpacing: Int = 16,
    verticalSpacing: Int = 8,
    enableScroll: Boolean = true,
    itemContent: @Composable (Int) -> Unit
) {
    // 计算行数
    val rowCount = (size + columnCount - 1) / columnCount

    val baseModifier = if (enableScroll) {
        val scrollState = rememberScrollState()
        modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    } else {
        modifier
    }

    Column(
        modifier = baseModifier
    ) {
        // 按行渲染内容
        for (rowIndex in 0..<rowCount) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = verticalSpacing.dp),
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing.dp)
            ) {
                // 计算当前行的起始和结束索引
                val startIndex = rowIndex * columnCount
                val endIndex = minOf(startIndex + columnCount, size)

                // 渲染当前行的项目
                for (index in startIndex until endIndex) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        itemContent(index)
                    }
                }

                // 如果当前行的列数不足，添加空的占位Box以保持对齐
                if (endIndex - startIndex < columnCount) {
                    repeat(columnCount - (endIndex - startIndex)) {
                        Box(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
