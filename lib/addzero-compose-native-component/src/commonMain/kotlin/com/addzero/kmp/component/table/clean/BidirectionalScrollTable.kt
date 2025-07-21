package com.addzero.kmp.component.table.clean

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T, C> BidirectionalScrollTable(
    data: List<T>,
    columns: List<C>,
    modifier: Modifier = Modifier,
    getId: (T) -> Any,
    getColumnTitle: (C) -> String,
    getColumnContent: @Composable (C, T) -> Unit,
    getColumnWidth: (C) -> Dp,
    headerHeight: Dp = 56.dp,
    rowHeight: Dp = 48.dp,
) {
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    Column(modifier = modifier) {
        // 表头 - 同步水平滚动
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .horizontalScroll(horizontalScrollState),
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
                        getColumnTitle(column),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Divider()

        // 内容区 - 双向滚动
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(verticalScrollState)
        ) {
            LazyColumn {
                items(data) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(rowHeight)
                            .horizontalScroll(horizontalScrollState)
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
                    Divider()
                }
            }
        }
    }
}
