package com.addzero.kmp.component.table.header

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.card.AddCard
import com.addzero.kmp.component.card.MellumCardType

/**
 * 🎨 表格头部统计信息卡片
 *
 * 用于展示表格的统计信息，如总数、选中数量等
 */
@Composable
fun AddTableStatsCard(
    modifier: Modifier = Modifier,
    totalCount: Int,
    selectedCount: Int = 0,
    filteredCount: Int? = null,
    cardType: MellumCardType = MellumCardType.Blue
) {
    AddCard(
        modifier = modifier,
        backgroundType = cardType,
        cornerRadius = 8.dp,
        elevation = 2.dp,
        padding = 12.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 总数
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = totalCount.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "总计",
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalContentColor.current.copy(alpha = 0.7f)
                )
            }

            // 选中数量（如果有选中项）
            if (selectedCount > 0) {
                HorizontalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp),
                     color = LocalContentColor.current.copy(alpha = 0.3f)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = selectedCount.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "已选",
                        style = MaterialTheme.typography.bodySmall,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
            }

            // 过滤后数量（如果有过滤）
            if (filteredCount != null && filteredCount != totalCount) {
                HorizontalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp),
                    color = LocalContentColor.current.copy(alpha = 0.3f)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = filteredCount.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "筛选",
                        style = MaterialTheme.typography.bodySmall,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
