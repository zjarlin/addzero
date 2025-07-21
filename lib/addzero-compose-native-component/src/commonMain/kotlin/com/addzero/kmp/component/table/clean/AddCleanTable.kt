package com.addzero.kmp.component.table.clean

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


@Composable
inline fun <C> AddCleanTableHeader(
//    getColumns: (KClass<T>) -> List<C>,
    columns: List<C>,
    getLabel: (C) -> String = { it.toString() },
    rednerChecbox: @Composable () -> Unit = {},
    renderSort: @Composable C.() -> Unit = {},
    renderfilter: @Composable () -> Unit = {},
    renderCaozuo: @Composable () -> Unit = {
        Box(
            modifier = Modifier.width(160.dp)  // 与表格内容的操作列宽度一致
                .fillMaxHeight().padding(horizontal = 16.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "操作", style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ), textAlign = TextAlign.Center
            )
        }

    },
) {

    val horizontalScrollState = rememberScrollState()
//    val columns: List<C> = getColumns(T::class)

    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        // 固定列区域（复选框和行号）- 与表格内容保持一致的宽度
        Row(
            modifier = Modifier.width(120.dp)  // 与表格内容的固定列宽度一致
                .fillMaxHeight().padding(start = 16.dp, end = 8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            // 多选模式下的全选复选框
            rednerChecbox()

            // 行号列标题
            Box(
                modifier = Modifier.padding(horizontal = 4.dp).width(40.dp), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#", style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ), textAlign = TextAlign.Center
                )
            }
        }

        // 可滚动的数据列区域 - 与表格内容保持一致
        Row(
            modifier = Modifier.weight(1f).fillMaxHeight().horizontalScroll(horizontalScrollState)
                .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            // 只显示可见的列
            columns.forEach { column ->
                val label = getLabel(column)
                // 使用Box来确保表头列有正确的宽度和对齐方式
                Box(
                    modifier = Modifier.fillMaxHeight().padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        //渲染标题
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )

                        // 排序图标 - 使用本地状态
                        column.renderSort()

                        // 渲染过滤图标
                        renderfilter()
                    }
                }
            }
        }

        renderCaozuo()
    }

}

