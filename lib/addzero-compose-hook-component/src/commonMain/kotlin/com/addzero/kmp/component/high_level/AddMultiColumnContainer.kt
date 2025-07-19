package com.addzero.kmp.component.high_level

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 高阶动态显示n 列组件
 * @param [howMuchColumn] 要显示几列
 * @param [modifier]
 * @param [horizontalGap]
 * @param [verticalGap]
 * @param [items]
 */
@Composable
fun AddMultiColumnContainer(
    howMuchColumn: Int = 2,
    modifier: Modifier = Modifier,
    horizontalGap: Int = 16,
    verticalGap: Int = 8,
    items: List<@Composable () -> Unit>
) {
    val filter = items.filter { it != {} }

    LazyVerticalGrid(
        columns = GridCells.Fixed(howMuchColumn),
        horizontalArrangement = Arrangement.spacedBy(horizontalGap.dp),
        verticalArrangement = Arrangement.spacedBy(verticalGap.dp),
        modifier = modifier
    ) {
        items(filter.size) { index ->
            filter[index]()
        }
    }
}
