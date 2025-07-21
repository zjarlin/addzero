package com.addzero.kmp.demo

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
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.component.button.AddIconButton
import com.addzero.kmp.component.form.DynamicFormItem
import com.addzero.kmp.component.table.TableRowType
import com.addzero.kmp.component.table.config.*
import com.addzero.kmp.component.table.model.AddColumn
import com.addzero.kmp.component.table.model.SysColumnMetaConfig
import com.addzero.kmp.entity.low_table.SpecPageResult


// 示例数据类
data class SampleData(
    val id: Int, var name: String, var age: Int
)

@Composable
@Route("组件示例", "测试表格")
fun AddGenericTableExample() {
    // 示例数据
    val data: List<TableRowType> = listOf(
        mutableMapOf("id" to 1, "name" to "Alice", "age" to 25),
        mutableMapOf("id" to 2, "name" to "Bob", "age" to 30),
        mutableMapOf("id" to 3, "name" to "Charlie", "age" to 35)

    )
//    val koinViewModel = koinViewModel<GenericTableViewModel<SampleData>>()
//    GenericTableViewModel<SampleData>(tableConfig)
    val rememberCoroutineScope = rememberCoroutineScope()

    // 示例列配置
    val columns = listOf(
        AddColumn(
            key = "name",
            title = "姓名",
        ).apply {
            customFormRender = { item ->
                // 即使item为null也能正确处理
                DynamicFormItem(
                    value = getFun(item, key),
                    onValueChange = { newValue ->
                        // 确保item不为null时才设置值
                        if (item != null) {
                            setFun(item, key, newValue)
                        }
                    },
                    title = title,
                    kmpType = metaconfig.kmpType
                )
            }
        },
        AddColumn(
            metaconfig = SysColumnMetaConfig().copy(kmpType = "Int"),
            key = "age",
            title = "年龄",
        )
    )

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
//        onRowClick = { item -> println("Row clicked: $item") }
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
//        when (selectedTheme) {
//            "Light" -> {
//                TableCardStyles.LightTheme(
//                    modifier = Modifier.fillMaxWidth(),
//                    config = tableConfig,
//                    buttonSlot = { SampleCustomButtons() }
//                )
//            }
//
//            "Dark" -> {
//                TableCardStyles.DarkTheme(
//                    modifier = Modifier.fillMaxWidth(),
//                    config = tableConfig,
//                    buttonSlot = { SampleCustomButtons() }
//                )
//            }
//
//        }
    }
}

/**
 * 示例自定义按钮
 */
@Composable
private fun SampleCustomButtons() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AddIconButton(
            text = "刷新",
            imageVector = Icons.Default.Refresh,
            onClick = { println("刷新数据") }
        )

        AddIconButton(
            text = "设置",
            imageVector = Icons.Default.Settings,
            onClick = { println("打开设置") }
        )
    }
}

