package com.addzero.kmp.screens.excel

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.viewmodel.ExcelTemplateDesignerViewModel

/**
 * 提取购物车项
 */
@Composable
fun ExtractionCartItem(
    item: ExcelTemplateDesignerViewModel.MetadataExtractionItem, onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = when (item.status) {
                ExcelTemplateDesignerViewModel.ExtractionStatus.PENDING -> Color(0xFFFEF3C7)
                ExcelTemplateDesignerViewModel.ExtractionStatus.PROCESSING -> Color(0xFFDCFCE7)
                ExcelTemplateDesignerViewModel.ExtractionStatus.COMPLETED -> Color(0xFFD1FAE5)
                ExcelTemplateDesignerViewModel.ExtractionStatus.FAILED -> Color(0xFFFEE2E2)
            }
        ), border = androidx.compose.foundation.BorderStroke(
            1.dp, when (item.status) {
                ExcelTemplateDesignerViewModel.ExtractionStatus.PENDING -> Color(0xFFD97706)
                ExcelTemplateDesignerViewModel.ExtractionStatus.PROCESSING -> Color(0xFF3B82F6)
                ExcelTemplateDesignerViewModel.ExtractionStatus.COMPLETED -> Color(0xFF10B981)
                ExcelTemplateDesignerViewModel.ExtractionStatus.FAILED -> Color(0xFFEF4444)
            }
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // 状态图标
                Icon(
                    when (item.status) {
                        ExcelTemplateDesignerViewModel.ExtractionStatus.PENDING -> Icons.Default.Schedule
                        ExcelTemplateDesignerViewModel.ExtractionStatus.PROCESSING -> Icons.Default.Sync
                        ExcelTemplateDesignerViewModel.ExtractionStatus.COMPLETED -> Icons.Default.CheckCircle
                        ExcelTemplateDesignerViewModel.ExtractionStatus.FAILED -> Icons.Default.Error
                    }, contentDescription = item.status.name, tint = when (item.status) {
                        ExcelTemplateDesignerViewModel.ExtractionStatus.PENDING -> Color(0xFFD97706)
                        ExcelTemplateDesignerViewModel.ExtractionStatus.PROCESSING -> Color(0xFF3B82F6)
                        ExcelTemplateDesignerViewModel.ExtractionStatus.COMPLETED -> Color(0xFF10B981)
                        ExcelTemplateDesignerViewModel.ExtractionStatus.FAILED -> Color(0xFFEF4444)
                    }, modifier = Modifier.size(12.dp)
                )

                Column {
                    Text(
                        text = item.excelTemplate.name, style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ), fontSize = 10.sp, maxLines = 1
                    )
                    Text(
                        text = when (item.status) {
                            ExcelTemplateDesignerViewModel.ExtractionStatus.PENDING -> "待处理"
                            ExcelTemplateDesignerViewModel.ExtractionStatus.PROCESSING -> "处理中..."
                            ExcelTemplateDesignerViewModel.ExtractionStatus.COMPLETED -> "已完成"
                            ExcelTemplateDesignerViewModel.ExtractionStatus.FAILED -> "处理失败"
                        }, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B7280), fontSize = 8.sp
                    )
                }
            }

            if (item.status == ExcelTemplateDesignerViewModel.ExtractionStatus.PENDING) {
                IconButton(
                    onClick = onRemove, modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        Icons.Default.RemoveShoppingCart, contentDescription = "移除", modifier = Modifier.size(12.dp), tint = Color(0xFFEF4444)
                    )
                }
            }
        }
    }
}
