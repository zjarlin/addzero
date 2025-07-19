package com.addzero.kmp.component.table

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.button.AddIconButton
import com.addzero.kmp.component.table.config.TableApiConfig
import com.addzero.kmp.component.table.config.TableConfig
import com.addzero.kmp.component.table.config.TableDataConfig
import com.addzero.kmp.component.table.config.TableImportConfig
import com.addzero.kmp.component.table.config.TableUiConfig
import com.addzero.kmp.component.table.model.AddColumn
import com.addzero.kmp.entity.low_table.SpecPageResult


@Composable
fun AddTable(data: List<TableRowType>, columns: List<AddColumn>, buttonSloat: () -> Unit) {

    // 使用新API创建表格配置
    var apiConfig = TableApiConfig(
        onLoadData = { input ->
            println("Load data with input: $input")
            val specPageResult = SpecPageResult(
                rows = data,
                totalRowCount = data.size.toLong(),
                totalPageCount = 1
            )
            specPageResult

        },
        onEdit = { item ->
            println("Edit item: $item")
            true
        },
        onDelete = { id ->
            println("Delete item with id: $id")
            true
        },
        onSave = { item ->
            println("Save item: $item")
            true
        },
        onExport = { param ->
            println("Export data with param: $param")
            true
        },
//        onUpload = { content ->
//            rememberCoroutineScope.launch {
//                val uywopload = fileApi.upload(content)
//                println(uywopload)
//            }
//            "success"
//        },
//            onCustomEvent = { event, data ->
//                println("Custom event: $event, data: $data")
//                true
//            }
    )
    var dataConfig = TableDataConfig(
        initData = data,
        columns = columns,
    )


    val tableConfig = TableConfig(
        apiConfig = apiConfig,
        dataConfig = dataConfig,
        uiConfig = TableUiConfig(
            rowHeight = 56,
            headerHeight = 56,
            showActions = true,
            showPagination = true,
            showBatchOperations = true,
            showAdvancedSearch = true
        ),
        importConfig = TableImportConfig(
            title = "Excel文件上传",
            description = "请上传Excel文件(.xlsx)，文件大小不超过10MB",
            showDescription = true,
            acceptedFileTypes = listOf("xlsx", "csv"),
            maxFileSize = 10
        )
    )

    // 表格组件选择器
    var useCardStyle by remember { mutableStateOf(false) }
//    var koinViewModel = koinViewModel<ThemeViewModel>()
    var selectedTheme by remember { mutableStateOf("Light") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 样式选择器
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "表格样式选择",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = useCardStyle,
                            onCheckedChange = { useCardStyle = it }
                        )
                        Text("使用卡片风格", style = MaterialTheme.typography.bodyMedium)
                    }

                    if (useCardStyle) {
                        Text("主题:", style = MaterialTheme.typography.bodyMedium)
                        val themes = listOf("Light", "Dark")
                        themes.forEach { theme ->
                            FilterChip(
                                onClick = { selectedTheme = theme },
                                label = { Text(theme, style = MaterialTheme.typography.bodySmall) },
                                selected = selectedTheme == theme
                            )
                        }
                    }
                }
            }
        }

        // 表格组件
        // 使用卡片风格的表格
        when (selectedTheme) {
            "Light" -> {
                TableCardStyles.LightTheme(
                    modifier = Modifier.fillMaxWidth(),
                    config = tableConfig,
                    buttonSlot = buttonSloat
                )
            }

            "Dark" -> {
                TableCardStyles.DarkTheme(
                    modifier = Modifier.fillMaxWidth(),
                    config = tableConfig,
                    buttonSlot = buttonSloat
                )
            }

        }

    }
}


