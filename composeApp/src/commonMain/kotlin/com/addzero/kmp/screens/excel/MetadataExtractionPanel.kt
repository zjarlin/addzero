package com.addzero.kmp.screens.excel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.addzero.kmp.viewmodel.ExcelTemplateDesignerViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * 元数据提取面板
 */
@OptIn(ExperimentalTime::class)
@Composable
fun MetadataExtractionPanel(
    viewModel: ExcelTemplateDesignerViewModel, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxHeight().padding(8.dp), colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FAFC)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            // 标题
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔍 元数据提取", style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ), color = Color(0xFF1E40AF)
                )

                IconButton(
                    onClick = {
                        // 添加示例Excel文件
                        viewModel.addAvailableExcelFile("示例数据${Clock.System.now().toEpochMilliseconds()}.xlsx", "2.5MB")
                    }) {
                    Icon(
                        Icons.Default.Add, contentDescription = "添加Excel", tint = Color(0xFF1E40AF), modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 可选择的Excel文件列表
            Text(
                text = "📁 可选择的Excel文件", style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Medium
                ), color = Color(0xFF374151)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (viewModel.availableExcelFiles.isNotEmpty()) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier.weight(0.4f).verticalScroll(scrollState), verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    viewModel.availableExcelFiles.forEach { excelFile ->
                        AvailableExcelFileCard(excelFile = excelFile, isInCart = viewModel.isInExtractionCart(excelFile), onAddToCart = { viewModel.addToExtractionCart(excelFile) }, onRemove = { viewModel.removeAvailableExcelFile(excelFile) })
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth().height(60.dp), colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "点击+添加Excel文件", style = MaterialTheme.typography.bodySmall, color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 提取购物车
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🛒 提取购物车 (${viewModel.metadataExtractionCart.size})", style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Medium
                    ), color = Color(0xFF374151)
                )

                if (viewModel.metadataExtractionCart.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.clearExtractionCart() }, modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Clear, contentDescription = "清空", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.startMetadataExtraction() }, modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.PlayArrow, contentDescription = "开始提取", tint = Color(0xFF10B981), modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 购物车内容
            if (viewModel.metadataExtractionCart.isNotEmpty()) {
                val cartScrollState = rememberScrollState()
                Column(
                    modifier = Modifier.weight(0.6f).verticalScroll(cartScrollState), verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    viewModel.metadataExtractionCart.forEach { item ->
                        ExtractionCartItem(
                            item = item, onRemove = { viewModel.removeFromExtractionCart(item) })
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth().height(80.dp), colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart, contentDescription = "购物车", tint = Color(0xFF9CA3AF), modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "购物车为空", style = MaterialTheme.typography.bodySmall, color = Color(0xFF9CA3AF)
                            )
                        }
                    }
                }
            }
        }
    }
}
