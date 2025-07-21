//package com.addzero.kmp.component.table
//
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.Sort
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import com.addzero.kmp.component.button.AddIconButton
//import com.addzero.kmp.component.card.AddCard
//import com.addzero.kmp.component.card.MellumCardType
//import com.addzero.kmp.component.search_bar.AddSearchBar
//import com.addzero.kmp.component.table.body.TableRow
//import com.addzero.kmp.component.table.clean.AddCleanTableHeader
//import com.addzero.kmp.component.table.clean.AddCleanTableViewModel
//import com.addzero.kmp.component.table.config.TableConfig
//import com.addzero.kmp.component.table.header.AddAdvSearchPopBar
//import com.addzero.kmp.component.table.header.column.RenderHeaderColumn
//import com.addzero.kmp.component.table.pagination.AddTablePagination
//import com.addzero.kmp.component.table.viewmodel.TableCompositeViewModel
//import com.addzero.kmp.entity.low_table.EnumSortDirection
//
//
///**
// * 🎨 增强版通用表格组件
// *
// * 使用 JetBrains Mellum 风格的卡片来展示表头和分页，
// * 提供更现代化的视觉效果和用户体验
// *
// * @param modifier 修饰符
// * @param config 表格配置
// * @param buttonSlot 自定义按钮插槽
// * @param actionSlot 自定义操作列内容
// * @param headerCardType 表头卡片类型
// * @param paginationCardType 分页卡片类型
// * @param showStatsCard 是否显示统计信息卡片
// * @param statsCardType 统计卡片类型
// * @param compactPagination 是否使用紧凑分页模式
// * @param enableHeaderHover 是否启用表头悬浮效果
// */
//@Composable
//fun <T, C> AddGenericTable() {
//    val tableViewModel = AddCleanTableViewModel<T, C>()
//
//    val renderButtons: @Composable () -> Unit = {
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            tableViewModel.buttonSlot.invoke()
//            // 多选模式按钮
//            AddIconButton(
//                text = if (tableViewModel.uicondifg.enableEditMode) "退出多选" else
//                    "多选",
//                imageVector = if (tableViewModel.selectionViewModel.enableEditMode) Icons.Default.Deselect else Icons.Default.SelectAll,
//                onClick = {
//                    tableViewModel.selectionViewModel.toggleEditMode()
//                })
//
//            // 新增按钮
//            AddIconButton(
//                text = "新增", imageVector = Icons.Default.Add, onClick = { tableViewModel.showAddForm() })
//
//            // 导入按钮
//            AddIconButton(
//                text = "导入",
//                imageVector = Icons.Default.UploadFile,
//                onClick = { tableViewModel.uiStateViewModel.showImportDialog() })
//
//            // 导出按钮
//            AddIconButton(
//                text = "导出",
//                imageVector = Icons.Default.DownloadForOffline,
//                onClick = { tableViewModel.uiStateViewModel.showExportDropDown() })
//        }
//    }
//
//    Column(modifier = modifier) {
//        // 表格头部卡片
//        tableViewModel.searchViewModel::updateKeyword
//        { tableViewModel.search() }
//        AddCard(
//            onClick = {},
////            modifier = Modifier
////                .fillMaxWidth()
////                .zIndex(2f), // 确保表头在最上层
////            backgroundType = headerCardType,
////            cornerRadius = 12.dp,
////            elevation = 4.dp,
////            padding = 16.dp,
////            animationDuration = 200
//        ) {
//            Column(
////                verticalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                // 第一行：搜索栏和按钮
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // 按钮区域
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(8.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        renderButtons()
//                    }
//
//                    // 搜索栏
//                    AddSearchBar(
//                        keyword = searchViewModel.keyword,
//                        onKeyWordChanged = searchViewModel::onKeywordChange,
//                        onSearch = finalTableViewModel::onSearch,
//                        modifier = Modifier.weight(1f)
//                    )
//
//                }
//
//                // 第二行：表格列头（与表格内容对齐）
//                if (tableViewModel.dataViewModel.columns.isNotEmpty()) {
//
//                    AddCleanTableHeader(
//                        columns = tableViewModel.dataViewModel.columns,
//                        getLabel = { column -> column.title },
//                        rednerChecbox = {
//
//                            tableViewModel.RenderChecbox(
//                                config = config
//                            )
//                        },
//                        renderSort = {
//                            when (getSortDirection(this.key, tableViewModel.sortViewModel.sortState)) {
//                                EnumSortDirection.ASC -> {
//                                    Icon(
//                                        imageVector = Icons.Default.ArrowUpward,
//                                        contentDescription = "升序",
//                                        tint = MaterialTheme.colorScheme.primary,
//                                        modifier = Modifier.size(16.dp)
//                                    )
//                                }
//
//                                EnumSortDirection.DESC -> {
//                                    Icon(
//                                        imageVector = Icons.Default.ArrowDownward,
//                                        contentDescription = "降序",
//                                        tint = MaterialTheme.colorScheme.primary,
//                                        modifier = Modifier.size(16.dp)
//                                    )
//                                }
//
//                                else -> {
//                                    Icon(
//                                        imageVector = Icons.AutoMirrored.Filled.Sort,
//                                        contentDescription = "排序",
//                                        tint = LocalContentColor.current.copy(alpha = 0.5f),
//                                        modifier = Modifier.alpha(0.5f).size(16.dp)
//                                    )
//                                }
//                            }
//                        },
//                        renderfilter = {},
//                        renderCaozuo = {},
//                    )
//                    Row(
//                        modifier = Modifier.fillMaxWidth().height(config.uiConfig.headerHeight.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        // 固定列区域（复选框和行号）- 与表格内容保持一致的宽度
//                        Row(
//                            modifier = Modifier.width(120.dp)  // 与表格内容的固定列宽度一致
//                                .fillMaxHeight().padding(start = 16.dp, end = 8.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            // 多选模式下的全选复选框
//
//                            // 行号列标题
//                            Box(
//                                modifier = Modifier.padding(horizontal = 4.dp).width(40.dp),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Text(
//                                    text = "#", style = MaterialTheme.typography.titleSmall.copy(
//                                        fontWeight = FontWeight.Bold
//                                    ), textAlign = TextAlign.Center
//                                )
//                            }
//                        }
//
//                        // 可滚动的数据列区域 - 与表格内容保持一致
//                        Row(
//                            modifier = Modifier.weight(1f).fillMaxHeight().horizontalScroll(horizontalScrollState)
//                                .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            // 只显示可见的列
//                            val visibleColumns =
//                                tableViewModel.dataViewModel.columns.filter { it.metaconfig.showInList }
//                            visibleColumns.forEach { column ->
//                                RenderHeaderColumn(
//                                    column = column, sortDirection = getSortDirection(
//                                        column.key, stateSorts = tableViewModel.sortViewModel.sortState
//                                    ), onChangeSorting = {
//                                        tableViewModel.changeSorting(column.key)
//                                    }, onColumnAdvSearchClick = {
//                                        tableViewModel.showColumnAdvSearch(column)
//                                    }, isFiltered = getIsFiltered(
//                                        column.key, filterStateMap = tableViewModel.searchViewModel.filterStateMap
//                                    )
//                                )
//                            }
//                        }
//
//                        // 固定操作列区域 - 与表格内容保持一致的宽度
//                        Box(
//                            modifier = Modifier.width(160.dp)  // 与表格内容的操作列宽度一致
//                                .fillMaxHeight().padding(horizontal = 16.dp), contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = "操作", style = MaterialTheme.typography.titleSmall.copy(
//                                    fontWeight = FontWeight.Bold
//                                ), textAlign = TextAlign.Center
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        // 表格内容
//        // 批量操作按钮
//        if (tableViewModel.selectionViewModel.enableEditMode && tableViewModel.selectionViewModel.selectedItems.isNotEmpty()) {
//            Row(
//                modifier = Modifier.fillMaxWidth().padding(8.dp),
//                horizontalArrangement = Arrangement.End,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    "已选择 ${tableViewModel.selectionViewModel.selectedItems.size} 项" + ",已选择的id${tableViewModel.selectionViewModel.selectedItems}",
//                    style = MaterialTheme.typography.bodyMedium,
//                    modifier = Modifier.padding(end = 16.dp)
//                )
//                // @RBAC_PERMISSION: table.batch.delete - 批量删除权限
//                Button(onClick = { tableViewModel.batchDelete() }) {
//                    Text("批量删除")
//                }
//
//                Spacer(modifier = Modifier.width(8.dp))
//
//                // @RBAC_PERMISSION: table.batch.export - 批量导出权限
//                Button(onClick = { tableViewModel.batchExport() }) {
//                    Text("导出选中")
//                }
//            }
//        }
//        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
//            LazyColumn(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                if (tableViewModel.dataViewModel.data.isEmpty()) {
//                    item {
//                        Box(
//                            modifier = Modifier.fillMaxWidth().height(200.dp).padding(16.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = "没有数据",
//                                style = MaterialTheme.typography.bodyLarge,
//                                color = MaterialTheme.colorScheme.onSurfaceVariant
//                            )
//                        }
//                    }
//                } else {
//                    itemsIndexed(items = tableViewModel.dataViewModel.data) { index, item ->
//                        val isSelected =
//                            tableViewModel.selectionViewModel.selectedItems.contains(config.dataConfig.getIdFun(item))
//                        val backgroundColor =
//                            if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
//                            else if (index % 2 == 0) MaterialTheme.colorScheme.surface
//                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
//
//                        val rowNumber =
//                            (tableViewModel.paginationViewModel.pageState.currentPage - 1) * tableViewModel.paginationViewModel.pageState.pageSize + index + 1
//
//                        TableRow(
//                            getidFun = config.dataConfig.getIdFun,
//                            item = item,
//                            rowNumber = rowNumber,
//                            isSelected = isSelected,
//                            multiSelect = tableViewModel.selectionViewModel.enableEditMode,
//                            showActions = tableViewModel.uiStateViewModel.showActions,
//                            rowHeight = config.uiConfig.rowHeight,
//                            backgroundColor = backgroundColor,
//                            onRowClick = config.dataConfig.onRowClick,
//                            onDeleteItem = { id: Any ->
//                                tableViewModel.deleteData(id)
//                            },
//                            onCheckboxClick = { tableViewModel.toggleItemSelection(item) },
//                            horizontalScrollState = horizontalScrollState,
//                            columns = tableViewModel.dataViewModel.columns,
//                            renderCustomActions = actionSlot,
//                            onEditClick = { it: TableRowType ->
//                                tableViewModel.showEditForm(it)
//                            })
//
//                        if (index < tableViewModel.dataViewModel.data.size - 1) {
//                            HorizontalDivider(
//                                Modifier,
//                                DividerDefaults.Thickness,
//                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        // 分页控件卡片
//        if (config.uiConfig.showPagination) {
//            AddTablePagination(
//                statePagination = tableViewModel.paginationViewModel.pageState,
//                enablePagination = true,
//                onPageSizeChange = {
//                    tableViewModel.changePageSize(it)
//                },
//                onGoFirstPage = {
//                    tableViewModel.paginationViewModel.goToFirstPage()
//                    tableViewModel.queryPage()
//                },
//                onPreviousPage = { tableViewModel.previousPage() },
//                onGoToPage = { tableViewModel.goToPage(it) },
//                onNextPage = { tableViewModel.nextPage() },
//                onGoLastPage = {
//                    tableViewModel.paginationViewModel.goToLastPage()
//                    tableViewModel.queryPage()
//                },
//                cardType = paginationCardType,
//                compactMode = compactPagination
//            )
//        }
//    }
//
//    // 高级搜索弹窗
//    if (tableViewModel.searchViewModel.showAdvancedSearch) {
//        AddAdvSearchPopBar(
//            column = tableViewModel.searchViewModel.currentAdvSearchColumn,
//            showFieldAdvSearch = tableViewModel.searchViewModel.showFieldAdvSearch,
//            onAdvSearchSubmit = { tableViewModel.submitAdvSearch() },
//            onClose = { tableViewModel.searchViewModel.hideColumnAdvSearch() },
//            searchForm = tableViewModel.searchViewModel.currentAdvSearchColumn?.let {
//                tableViewModel.searchViewModel.filterStateMap[it.key]
//            },
//            onAdvSearchCLean = {
//                tableViewModel.clearColumnFilter()
//            },
//            onSearchFormChange = { columnValue, operator, logicType ->
//                tableViewModel.updateSearchForm(columnValue, operator, logicType)
//            })
//    }
//}
//
//@Composable
////context()
//fun TableCompositeViewModel.RenderChecbox(
//    config: TableConfig
//) {
//    if (selectionViewModel.enableEditMode) {
//        Box(
//            modifier = Modifier.padding(horizontal = 4.dp).width(40.dp), contentAlignment = Alignment.Center
//        ) {
//            Checkbox(
//                checked = this@RenderChecbox.selectionViewModel.isPageAllSelected(
//                    this@RenderChecbox.dataViewModel.data, config.dataConfig.getIdFun
//                ), onCheckedChange = { it: Boolean -> this@RenderChecbox.togglePageSelection() })
//        }
//    }
//}
//
///**
// * 🎨 预设的表格卡片样式组合
// */
//object TableCardStyles {
//
//    /**
//     * 深色主题样式
//     */
//    @Composable
//    fun DarkTheme(
//        modifier: Modifier = Modifier,
//        config: TableConfig,
//        buttonSlot: @Composable (() -> Unit)? = null,
//        actionSlot: @Composable () -> Unit = {}
//    ) {
//        AddGenericTable(
//            modifier = modifier,
//            config = config,
//            buttonSlot = buttonSlot,
//            actionSlot = actionSlot,
//            headerCardType = MellumCardType.Dark,
//            paginationCardType = MellumCardType.Dark,
//            showStatsCard = true,
//            statsCardType = MellumCardType.Dark
//        )
//    }
//
//
//    /**
//     * 浅色主题样式
//     */
//    @Composable
//    fun LightTheme(
//        modifier: Modifier = Modifier,
//        config: TableConfig,
//        buttonSlot: @Composable (() -> Unit)? = null,
//        actionSlot: @Composable () -> Unit = {}
//    ) {
//        AddGenericTable(
//            modifier = modifier,
//            config = config,
//            buttonSlot = buttonSlot,
//            actionSlot = actionSlot,
//            headerCardType = MellumCardType.Light,
//            paginationCardType = MellumCardType.Light,
//            showStatsCard = true,
//            statsCardType = MellumCardType.Light
//        )
//    }
//}
//
