package com.addzero.kmp.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.api.ApiProvider.fileApi
import com.addzero.kmp.component.addzero_starter.addzero_table.AddGenericTable
import com.addzero.kmp.component.addzero_starter.addzero_table.TableRowType
import com.addzero.kmp.component.form.DynamicFormItem
import com.addzero.kmp.component.table.AddColumn
import com.addzero.kmp.component.table.SysColumnMetaConfig
import com.addzero.kmp.entity.low_table.*
import com.addzero.kmp.annotation.Route

import kotlinx.coroutines.launch


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
    val tableConfig = TableConfig(
        apiConfig = TableApiConfig(
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
            onUpload = { content ->
                rememberCoroutineScope.launch {
                    val uywopload = fileApi.upload(content)
                    println(uywopload)
                }
                "success"
            },
//            onCustomEvent = { event, data ->
//                println("Custom event: $event, data: $data")
//                true
//            }
        ),
        dataConfig = TableDataConfig(
            initData = data,
            columns = columns,
            onRowClick = { item -> println("Row clicked: $item") }
        ),
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

    // 表格组件
    AddGenericTable(
        modifier = Modifier,
        config = tableConfig,
        buttonSlot = {
            // 这里可以添加自定义按钮
        },
        actionSlot = {
            // 定义自定义操作按钮列表
            val customActions = listOf("查看详情", "导出数据", "更多信息")

            // 只有当有自定义操作时才渲染菜单
            if (customActions.isNotEmpty()) {
                var showMenu by remember { mutableStateOf(false) }

                Box {
                    // 三点菜单按钮
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "更多操作",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    )
}

