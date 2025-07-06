package com.addzero.kmp.screens.json

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.addzero.kmp.annotation.Route

import com.addzero.kmp.viewmodel.JsonDesignerViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * JSON设计器主界面
 */
@Composable
@Route("工具", "JSON设计器")
fun JsonDesignerScreen() {
    val viewModel = koinViewModel<JsonDesignerViewModel>()
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部工具栏
        JsonDesignerTopBar(viewModel)
        
        // 主要内容区域
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // 左侧设计区域
            JsonDesignerArea(
                viewModel = viewModel,
                modifier = Modifier.weight(if (viewModel.showJsonPreview) 0.5f else 1f)
            )
            
            // 右侧JSON预览区域
            if (viewModel.showJsonPreview) {
                JsonPreviewArea(
                    viewModel = viewModel,
                    modifier = Modifier.weight(0.5f)
                )
            }
        }
        
        // 底部Excel上传区域
        ExcelUploadArea(
            viewModel = viewModel,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * 顶部工具栏
 */
@Composable
private fun JsonDesignerTopBar(viewModel: JsonDesignerViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 标题
            Text(
                text = "🔧 JSON设计器",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
            
            // 工具按钮
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 添加对象按钮
                IconButton(
                    onClick = { viewModel.addObject() },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "添加对象",
                        tint = Color.White
                    )
                }
                
                // 添加数组按钮
                IconButton(
                    onClick = { viewModel.addArray() },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = "添加数组",
                        tint = Color.White
                    )
                }
                
                // 切换预览按钮
                IconButton(
                    onClick = { viewModel.toggleJsonPreview() },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Icon(
                        if (viewModel.showJsonPreview) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "切换预览",
                        tint = Color.White
                    )
                }
                
                // 清空按钮
                IconButton(
                    onClick = { viewModel.clearAll() },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "清空",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

/**
 * JSON设计区域
 */
@Composable
private fun JsonDesignerArea(
    viewModel: JsonDesignerViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 标题和快速添加按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎨 设计区域",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.addObject() },
                        modifier = Modifier.size(width = 80.dp, height = 32.dp),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text("对象", fontSize = 12.sp)
                    }
                    
                    OutlinedButton(
                        onClick = { viewModel.addArray() },
                        modifier = Modifier.size(width = 80.dp, height = 32.dp),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text("数组", fontSize = 12.sp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 错误信息显示
            viewModel.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "⚠️ $error",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // JSON结构树 - 添加滚动功能
            val scrollState = rememberScrollState()
            JsonElementTree(
                element = viewModel.rootElement,
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            )
        }
    }
}

/**
 * JSON预览区域 - 支持双向编辑
 */
@Composable
private fun JsonPreviewArea(
    viewModel: JsonDesignerViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D3748)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 标题和工具按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📄 JSON编辑器",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { 
                            // TODO: 复制到剪贴板
                        }
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "复制JSON",
                            tint = Color.White
                        )
                    }
                    
                    IconButton(
                        onClick = {
//                            viewModel.importJsonFile()

                        }
                    ) {
                        Icon(
                            Icons.Default.FileOpen,
                            contentDescription = "导入JSON",
                            tint = Color.White
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 可编辑的JSON文本区域
            Card(
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A202C)
                )
            ) {
                OutlinedTextField(
                    value = viewModel.editableJsonText,
                    onValueChange = { viewModel.updateJsonFromEditor(it) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF68D391),
                        unfocusedTextColor = Color(0xFF68D391),
                        focusedBorderColor = Color(0xFF4FD1C7),
                        unfocusedBorderColor = Color(0xFF2D3748),
                        cursorColor = Color(0xFF68D391)
                    ),
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        lineHeight = 18.sp
                    ),
                    placeholder = {
                        Text(
                            "在此编辑JSON...",
                            color = Color(0xFF718096)
                        )
                    }
                )
            }
        }
    }
}
