package com.addzero.kmp.demo.clean_table

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.addzero.kmp.annotation.Route

@Composable
@Route
fun LargeTableTestScreen() {
    // 生成100行100列测试数据
    val (testData, columns) = remember { generateLargeTestData(100, 100) }

    // 定义列宽
    val columnWidth = 120.dp

    BidirectionalScrollTable(
        data = testData,
        columns = columns,
        getId = { it.id },
        getColumnTitle = { it },
        getColumnContent = { column, rowData ->
            Text(
                text = rowData.values[column] ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        getColumnWidth = { columnWidth }, // 所有列固定宽度
//        headerHeight = 40.dp,
//        rowHeight = 36.dp,
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .background(MaterialTheme.colorScheme.background)
    )
}

/**
 * 生成大型测试数据
 * @param rowCount 行数
 * @param colCount 列数
 */
fun generateLargeTestData(rowCount: Int, colCount: Int): Pair<List<TestRowData>, List<String>> {
    // 生成列名 (Column1, Column2, ..., ColumnN)
    val columns = (1..colCount).map { "Column$it" }

    // 生成行数据
    val rows = (1..rowCount).map { rowId ->
        // 为每行生成单元格的值
        val cellValues = columns.associate { colName ->
            val colNum = colName.substring(6).toInt()
            colName to "R${rowId}C$colNum" // 格式如: R1C1, R1C2,... RnCn
        }
        TestRowData(id = rowId, values = cellValues)
    }

    return Pair(rows, columns)
}

/**
 * 行数据类型
 */
data class TestRowData(
    val id: Int,
    val values: Map<String, String> // 键为列名，值为单元格内容
)

/**
 * 双向滚动表格组件
 */
@Composable
fun <T, C> BidirectionalScrollTable(
    data: List<T>,
    columns: List<C>,
    modifier: Modifier = Modifier,
    // 核心映射函数
    getId: (T) -> Any,
    getColumnTitle: (C) -> String,
    getColumnContent: @Composable (C, T) -> Unit,
    getColumnWidth: (C) -> Dp,
    // 可选参数
    headerHeight: Dp = 56.dp,
    rowHeight: Dp = 48.dp,
    onRowClick: (T) -> Unit = {},
) {
    // 水平滚动状态（表头和内容共享）
    val horizontalScrollState = rememberScrollState()

    Column(modifier = modifier) {
        // 表头 - 固定高度，横向滚动
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .horizontalScroll(horizontalScrollState)
                .background(MaterialTheme.colorScheme.primaryContainer),
            verticalAlignment = Alignment.CenterVertically
        ) {
            columns.forEach { column ->
                Box(
                    modifier = Modifier
                        .width(getColumnWidth(column))
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getColumnTitle(column),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)

        // 内容区 - 双向滚动
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // 垂直虚拟滚动
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(data) { item ->
                    // 每行横向滚动
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(rowHeight)
                            .horizontalScroll(horizontalScrollState)
                            .clickable { onRowClick(item) }
                            .background(
                                if (getId(item).toString().toInt() % 2 == 0) {
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        columns.forEach { column ->
                            Box(
                                modifier = Modifier
                                    .width(getColumnWidth(column))
                                    .fillMaxHeight()
                                    .padding(horizontal = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                getColumnContent(column, item)
                            }
                        }
                    }

                    // 行分隔线
                    Divider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}
