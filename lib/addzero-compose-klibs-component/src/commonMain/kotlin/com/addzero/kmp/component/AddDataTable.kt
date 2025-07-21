package com.addzero.kmp.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import com.seanproctor.datatable.DataColumn
import com.seanproctor.datatable.TableRowScope
import com.seanproctor.datatable.material3.DataTable

@Composable
fun <R, C> AddDataTable(
    data: List<R>,
    columns: List<C>,
    getLabel: (C) -> String,
    getValue: R.(C) -> Any?,
    customRender4Column: @Composable ((C) -> Unit)? = null,
    customRender4Cell: (@Composable (R, C) -> Unit)? = null,
    selectedRowColor: Color = Blue,
    onRowClick: (() -> Unit)?
) {
    val _customRender4Column = customRender4Column ?: {
        val label = getLabel(it)
        Text(label)
    }
    val _customRender4Cell = customRender4Cell ?: { row, column ->
        val value1 = row.getValue(column)
        val value = value1 ?: value1.toString()
        Text(
            text = value.toString(),
//            color = White
        )
    }


    // 跟踪选中的行
    var selectedRow by remember { mutableStateOf<R?>(null) }

    val dataColumns = columns.map {
        DataColumn(
            alignment = Alignment.Center
        ) {
            _customRender4Column(it)
        }
    }

    DataTable(
        columns = dataColumns
    ) {
        data.forEachIndexed { rowIndex, it ->
            val content: TableRowScope.() -> Unit = {
                onClick = onRowClick
                // 设置行背景色，根据选中状态和行号交替
                backgroundColor = when {
                    selectedRow == rowIndex -> selectedRowColor // 选中行
                    rowIndex % 2 == 0 -> LightGray // 偶数行
                    else -> White // 奇数行
                }
                columns.forEachIndexed { columnIndex, dataColumn ->
                    cell {
                        _customRender4Cell(it, dataColumn)
                    }


                }

            }

        }
        // 生成100行
    }
}

// 定义颜色常量
//private val LightBlue = androidx.compose.ui.graphics.Color(0xFFE3F2FD)
//private val LightGray = androidx.compose.ui.graphics.Color(0xFFF5F5F5)
//private val White = androidx.compose.ui.graphics.Color.White
