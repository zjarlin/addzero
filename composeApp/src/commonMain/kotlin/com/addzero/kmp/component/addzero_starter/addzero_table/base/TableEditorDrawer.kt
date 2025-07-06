//package com.addzero.kmp.component.table_base
//
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.zIndex
//import com.addzero.kmp.component.assist_fun.guessFieldType
//import com.addzero.kmp.entity.low_table.EnumColumnAlignment
//import com.addzero.kmp.entity.low_table.StateColumnMetadata
//import com.addzero.kmp.entity.low_table.EnumFieldRenderType
//import com.addzero.kmp.entity.low_table.StateFormField
//import com.addzero.kmp.entity.low_table.DefaultTableConfig
//
///**
// * 表格元数据编辑抽屉
// */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun  TableEditorDrawer(
//    visible: Boolean,
//    metadata: DefaultTableConfig,
//    onClose: () -> Unit,
//    onConfirm: (DefaultTableConfig) -> Unit
//) {
//    // 创建可编辑的元数据副本
//    val editableMetadata = remember(metadata) { metadata.toMutableMetadata() }
//
//    // 标签页选择状态
//    var selectedTabIndex by remember { mutableStateOf(0) }
//
//    // 抽屉宽度动画
//    val drawerWidth by animateDpAsState(
//        targetValue = if (visible) (LocalDensity.current.density * 360).dp else 0.dp,
//        label = "drawerWidth"
//    )
//
//    // 遮罩层
//    if (visible) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Black.copy(alpha = 0.5f))
//                .clickable(onClick = onClose)
//                .zIndex(1f)
//        )
//    }
//
//    // 抽屉内容
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .zIndex(2f),
//        contentAlignment = Alignment.CenterEnd
//    ) {
//        Surface(
//            modifier = Modifier
//                .fillMaxHeight()
//                .width(drawerWidth)
//                .shadow(elevation = 16.dp)
//        ) {
//            Column(
//                modifier = Modifier.fillMaxSize()
//            ) {
//                // 抽屉标题栏
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(MaterialTheme.colorScheme.primary)
//                        .padding(16.dp)
//                ) {
//                    Text(
//                        text = "表格元数据编辑",
//                        style = MaterialTheme.typography.titleLarge,
//                        color = MaterialTheme.colorScheme.onPrimary
//                    )
//
//                    IconButton(
//                        onClick = onClose,
//                        modifier = Modifier.align(Alignment.CenterEnd)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Close,
//                            contentDescription = "关闭",
//                            tint = MaterialTheme.colorScheme.onPrimary
//                        )
//                    }
//                }
//
//                // 标签页
//                TabRow(selectedTabIndex = selectedTabIndex) {
//                    Tab(
//                        selected = selectedTabIndex == 0,
//                        onClick = { selectedTabIndex = 0 },
//                        text = { Text("列配置") }
//                    )
//                    Tab(
//                        selected = selectedTabIndex == 1,
//                        onClick = { selectedTabIndex = 1 },
//                        text = { Text("表格设置") }
//                    )
//                    Tab(
//                        selected = selectedTabIndex == 2,
//                        onClick = { selectedTabIndex = 2 },
//                        text = { Text("表单配置") }
//                    )
//                }
//
//                // 标签页内容
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                        .padding(16.dp)
//                ) {
//                    when (selectedTabIndex) {
//                        0 -> ColumnsEditorTab(editableMetadata)
//                        1 -> TableSettingsTab(editableMetadata)
//                        2 -> FormConfigTab(editableMetadata)
//                    }
//                }
//
//                // 底部按钮区域
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(MaterialTheme.colorScheme.surfaceVariant)
//                        .padding(16.dp)
//                ) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.End,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        TextButton(onClick = onClose) {
//                            Text("取消")
//                        }
//                        Spacer(modifier = Modifier.width(16.dp))
//                        Button(onClick = {
//                            onConfirm(editableMetadata.toImmutableMetadata())
//                            onClose()
//                        }) {
//                            Text("保存")
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
///**
// * 列配置编辑标签页
// */
//@Composable
//private fun <T> ColumnsEditorTab(
//    metadata: MutableTableMetadata<T>
//) {
//    var reorderingEnabled by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        // 工具栏
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "表格列 (${metadata.columns.size})",
//                style = MaterialTheme.typography.titleMedium
//            )
//
//            Row {
//                // 列排序开关
//                Switch(
//                    checked = reorderingEnabled,
//                    onCheckedChange = { reorderingEnabled = it }
//                )
//                Text(
//                    text = "列排序",
//                    modifier = Modifier.padding(start = 8.dp)
//                )
//            }
//        }
//
//        // 列表
//        LazyColumn(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            itemsIndexed(metadata.columns.toList()) { index, column ->
//                ColumnEditorItem(
//                    column = column,
//                    reorderingEnabled = reorderingEnabled,
//                    onMoveUp = {
//                        if (index > 0) {
//                            metadata.swapColumns(index, index - 1)
//                        }
//                    },
//                    onMoveDown = {
//                        if (index < metadata.columns.size - 1) {
//                            metadata.swapColumns(index, index + 1)
//                        }
//                    },
//                    onVisibilityToggle = {
//                        metadata.updateColumn(index, column.copy(visible = !column.visible))
//                    },
//                    onEditColumn = { updatedColumn ->
//                        metadata.updateColumn(index, updatedColumn)
//                    }
//                )
//
//                if (index < metadata.columns.size - 1) {
//                    HorizontalDivider(
//                        modifier = Modifier.padding(vertical = 8.dp),
//                        thickness = DividerDefaults.Thickness,
//                        color = DividerDefaults.color
//                    )
//                }
//            }
//        }
//    }
//}
//
///**
// * 列编辑项
// */
//@Composable
//private fun <T> ColumnEditorItem(
//    column: StateColumnMetadata<T>,
//    reorderingEnabled: Boolean,
//    onMoveUp: () -> Unit,
//    onMoveDown: () -> Unit,
//    onVisibilityToggle: () -> Unit,
//    onEditColumn: (StateColumnMetadata<T>) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        // 列标题行
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // 列排序按钮
//            if (reorderingEnabled) {
//                Row(
//                    modifier = Modifier
//                        .padding(end = 16.dp)
//                        .width(80.dp)
//                ) {
//                    IconButton(onClick = onMoveUp) {
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowUp,
//                            contentDescription = "上移"
//                        )
//                    }
//                    IconButton(onClick = onMoveDown) {
//                        Icon(
//                            imageVector = Icons.Default.KeyboardArrowDown,
//                            contentDescription = "下移"
//                        )
//                    }
//                }
//            }
//
//            // 可见性切换
//            IconButton(onClick = onVisibilityToggle) {
//                Icon(
//                    imageVector = if (column.visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
//                    contentDescription = if (column.visible) "隐藏" else "显示"
//                )
//            }
//
//            // 列标题和信息
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .clickable { expanded = !expanded }
//                    .padding(horizontal = 8.dp)
//            ) {
//                Text(
//                    text = column.title,
//                    style = MaterialTheme.typography.titleSmall
//                )
//                Text(
//                    text = "字段: ${column.key}",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//
//            // 展开/折叠按钮
//            IconButton(onClick = { expanded = !expanded }) {
//                Icon(
//                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
//                    contentDescription = if (expanded) "折叠" else "展开"
//                )
//            }
//        }
//
//        // 展开的编辑区域
//        if (expanded) {
//            ColumnEditorExpandedContent(
//                column = column,
//                onColumnUpdate = onEditColumn
//            )
//        }
//    }
//}
//
///**
// * 列编辑器展开内容
// */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun <T> ColumnEditorExpandedContent(
//    column: StateColumnMetadata<T>,
//    onColumnUpdate: (StateColumnMetadata<T>) -> Unit
//) {
//    var title by remember { mutableStateOf(column.title) }
//    var widthRatio by remember { mutableStateOf(column.widthRatio.toString()) }
//    var alignment by remember { mutableStateOf(column.alignment) }
//    var sortable by remember { mutableStateOf(column.sortable) }
//    var searchable by remember { mutableStateOf(column.searchable) }
//
//    // 更新列配置
//    fun updateColumn() {
//        onColumnUpdate(
//            column.copy(
//                title = title,
//                widthRatio = widthRatio.toFloatOrNull() ?: 1f,
//                alignment = alignment,
//                sortable = sortable,
//                searchable = searchable
//            )
//        )
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
//            .border(
//                width = 1.dp,
//                color = MaterialTheme.colorScheme.outlineVariant,
//                shape = RoundedCornerShape(8.dp)
//            )
//            .padding(16.dp)
//    ) {
//        // 标题输入
//        OutlinedTextField(
//            value = title,
//            onValueChange = {
//                title = it
//                updateColumn()
//            },
//            label = { Text("列标题") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 宽度输入
//        OutlinedTextField(
//            value = widthRatio,
//            onValueChange = {
//                widthRatio = it
//                updateColumn()
//            },
//            label = { Text("列宽度比例 (默认为1)") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 对齐方式选择
//        Text(
//            text = "对齐方式",
//            style = MaterialTheme.typography.bodyMedium
//        )
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            // 左对齐
//            FilterChip(
//                selected = alignment == EnumColumnAlignment.LEFT,
//                onClick = {
//                    alignment = EnumColumnAlignment.LEFT
//                    updateColumn()
//                },
//                label = { Text("左对齐") }
//            )
//
//            // 居中对齐
//            FilterChip(
//                selected = alignment == EnumColumnAlignment.CENTER,
//                onClick = {
//                    alignment = EnumColumnAlignment.CENTER
//                    updateColumn()
//                },
//                label = { Text("居中") }
//            )
//
//            // 右对齐
//            FilterChip(
//                selected = alignment == EnumColumnAlignment.RIGHT,
//                onClick = {
//                    alignment = EnumColumnAlignment.RIGHT
//                    updateColumn()
//                },
//                label = { Text("右对齐") }
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 排序和搜索开关
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Checkbox(
//                    checked = sortable,
//                    onCheckedChange = {
//                        sortable = it
//                        updateColumn()
//                    }
//                )
//                Text("可排序")
//            }
//
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Checkbox(
//                    checked = searchable,
//                    onCheckedChange = {
//                        searchable = it
//                        updateColumn()
//                    }
//                )
//                Text("可搜索")
//            }
//        }
//    }
//}
//
///**
// * 表格设置标签页
// */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun <T> TableSettingsTab(
//    metadata: MutableTableMetadata<T>
//) {
//    var title by remember { mutableStateOf(metadata.title) }
//    var multiSelect by remember { mutableStateOf(metadata.multiSelect) }
//    var pagination by remember { mutableStateOf(metadata.pagination) }
//    var defaultPageSize by remember { mutableStateOf(metadata.defaultPageSize.toString()) }
//    var pageSizeOptions by remember {
//        mutableStateOf(metadata.pageSizeOptions.joinToString(","))
//    }
//    var striped by remember { mutableStateOf(metadata.striped) }
//    var bordered by remember { mutableStateOf(metadata.bordered) }
//    var hoverable by remember { mutableStateOf(metadata.hoverable) }
//    var showActions by remember { mutableStateOf(metadata.showActions) }
//
//    // 更新设置
//    fun updateSettings() {
//        metadata.title = title
//        metadata.multiSelect = multiSelect
//        metadata.pagination = pagination
//        metadata.defaultPageSize = defaultPageSize.toIntOrNull() ?: 10
//        metadata.pageSizeOptions = pageSizeOptions
//            .split(",")
//            .mapNotNull { it.trim().toIntOrNull() }
//        metadata.striped = striped
//        metadata.bordered = bordered
//        metadata.hoverable = hoverable
//        metadata.showActions = showActions
//    }
//
//    val scrollState = rememberScrollState()
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .then(remember<Modifier> {
//                Modifier.verticalScroll(scrollState)
//            })
//    ) {
//        Text(
//            text = "表格基本设置",
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // 表格标题
//        OutlinedTextField(
//            value = title,
//            onValueChange = {
//                title = it
//                updateSettings()
//            },
//            label = { Text("表格标题") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 表格功能开关
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Checkbox(
//                    checked = multiSelect,
//                    onCheckedChange = {
//                        multiSelect = it
//                        updateSettings()
//                    }
//                )
//                Text("允许多选")
//            }
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Checkbox(
//                    checked = pagination,
//                    onCheckedChange = {
//                        pagination = it
//                        updateSettings()
//                    }
//                )
//                Text("启用分页")
//            }
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Checkbox(
//                    checked = striped,
//                    onCheckedChange = {
//                        striped = it
//                        updateSettings()
//                    }
//                )
//                Text("条纹样式")
//            }
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Checkbox(
//                    checked = bordered,
//                    onCheckedChange = {
//                        bordered = it
//                        updateSettings()
//                    }
//                )
//                Text("显示边框")
//            }
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Checkbox(
//                    checked = hoverable,
//                    onCheckedChange = {
//                        hoverable = it
//                        updateSettings()
//                    }
//                )
//                Text("鼠标悬停效果")
//            }
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Checkbox(
//                    checked = showActions,
//                    onCheckedChange = {
//                        showActions = it
//                        updateSettings()
//                    }
//                )
//                Text("显示操作列")
//            }
//        }
//
//        // 仅当启用分页时显示分页设置
//        if (pagination) {
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "分页设置",
//                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
//
//            OutlinedTextField(
//                value = defaultPageSize,
//                onValueChange = {
//                    defaultPageSize = it
//                    updateSettings()
//                },
//                label = { Text("默认每页条数") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            OutlinedTextField(
//                value = pageSizeOptions,
//                onValueChange = {
//                    pageSizeOptions = it
//                    updateSettings()
//                },
//                label = { Text("每页条数选项 (逗号分隔)") },
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }
//}
//
///**
// * 表单配置标签页
// */
//@Composable
//private fun <T> FormConfigTab(
//    metadata: MutableTableMetadata<T>
//) {
//    Column(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Text(
//            text = "表单字段配置",
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // 字段列表
//        LazyColumn(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            items(metadata.columns.toList()) { column ->
//                FormFieldItem(
//                    column = column,
//                    formConfig = metadata.formConfig.find { it.field == column.key },
//                    onToggleFormField = { enabled, config ->
//                        if (enabled) {
//                            // 添加到表单配置
//                            if (config != null) {
//                                metadata.updateFormConfig(config)
//                            } else {
//                                // 创建默认表单配置
//                                metadata.addFormConfig(
//                                    StateFormField(
//                                        field = column.key,
//                                        label = column.title,
//                                        type = guessFieldType(column.key),
//                                        required = false,
//                                        validators = emptyList()
//                                    )
//                                )
//                            }
//                        } else {
//                            // 从表单配置中移除
//                            metadata.removeFormConfig(column.key)
//                        }
//                    },
//                    onUpdateFormConfig = { config ->
//                        metadata.updateFormConfig(config)
//                    }
//                )
//
//                HorizontalDivider(
//                    modifier = Modifier.padding(vertical = 8.dp),
//                    thickness = DividerDefaults.Thickness,
//                    color = DividerDefaults.color
//                )
//            }
//        }
//    }
//}
//
///**
// * 表单字段项
// */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun <T> FormFieldItem(
//    column: StateColumnMetadata<T>,
//    formConfig: StateFormField?,
//    onToggleFormField: (Boolean, StateFormField?) -> Unit,
//    onUpdateFormConfig: (StateFormField) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    val isInForm = formConfig != null
//
//    Column(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        // 字段标题行
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // 表单字段开关
//            Switch(
//                checked = isInForm,
//                onCheckedChange = { onToggleFormField(it, formConfig) }
//            )
//
//            // 字段信息
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .clickable(enabled = isInForm) {
//                        if (isInForm) expanded = !expanded
//                    }
//                    .padding(horizontal = 8.dp)
//            ) {
//                Text(
//                    text = column.title,
//                    style = MaterialTheme.typography.titleSmall
//                )
//                Text(
//                    text = "字段: ${column.key}",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//                if (isInForm) {
//                    Text(
//                        text = "类型: ${formConfig.type.name.lowercase()}",
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//            }
//
//            // 展开/折叠按钮
//            if (isInForm) {
//                IconButton(onClick = { expanded = !expanded }) {
//                    Icon(
//                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
//                        contentDescription = if (expanded) "折叠" else "展开"
//                    )
//                }
//            }
//        }
//
//        // 展开的编辑区域
//        if (expanded && formConfig != null) {
//            FormFieldEditorContent(
//                formConfig = formConfig,
//                onUpdateConfig = onUpdateFormConfig
//            )
//        }
//    }
//}
//
///**
// * 表单字段编辑器内容
// */
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun FormFieldEditorContent(
//    formConfig: StateFormField,
//    onUpdateConfig: (StateFormField) -> Unit
//) {
//    var label by remember { mutableStateOf(formConfig.label) }
//    var type by remember { mutableStateOf(formConfig.type) }
//    var required by remember { mutableStateOf(formConfig.required) }
//
//    // 更新配置
//    fun updateConfig() {
//        onUpdateConfig(
//            formConfig.copy(
//                label = label,
//                type = type,
//                required = required
//            )
//        )
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
//            .border(
//                width = 1.dp,
//                color = MaterialTheme.colorScheme.outlineVariant,
//                shape = RoundedCornerShape(8.dp)
//            )
//            .padding(16.dp)
//    ) {
//        // 标签输入
//        OutlinedTextField(
//            value = label,
//            onValueChange = {
//                label = it
//                updateConfig()
//            },
//            label = { Text("字段标签") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 字段类型选择
//        Text(
//            text = "字段类型",
//            style = MaterialTheme.typography.bodyMedium
//        )
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            // 常见的字段类型选择器
//            Box(modifier = Modifier.fillMaxWidth()) {
//                var fieldTypeMenuExpanded by remember { mutableStateOf(false) }
//
//                OutlinedButton(
//                    onClick = { fieldTypeMenuExpanded = true },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(
//                        text = type.name,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                }
//
//                DropdownMenu(
//                    expanded = fieldTypeMenuExpanded,
//                    onDismissRequest = { fieldTypeMenuExpanded = false },
//                    modifier = Modifier.fillMaxWidth(0.8f)
//                ) {
//                    EnumFieldRenderType.entries.forEach { fieldType ->
//                        DropdownMenuItem(
//                            text = { Text(fieldType.name) },
//                            onClick = {
//                                type = fieldType
//                                fieldTypeMenuExpanded = false
//                                updateConfig()
//                            }
//                        )
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // 是否必填
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Checkbox(
//                checked = required,
//                onCheckedChange = {
//                    required = it
//                    updateConfig()
//                }
//            )
//            Text("必填项")
//        }
//    }
//}
