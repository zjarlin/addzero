package com.addzero.kmp.component.table

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.button.AddIconButton
import com.addzero.kmp.component.card.MellumCardType
import com.addzero.kmp.component.table.body.TableContent
import com.addzero.kmp.component.table.header.AddAdvSearchPopBar
import com.addzero.kmp.component.table.header.AddTableHeader
import com.addzero.kmp.component.table.pagination.AddTablePagination
import com.addzero.kmp.component.table.header.AddTableStatsCard
import com.addzero.kmp.component.table.viewmodel.TableCompositeViewModel
import com.addzero.kmp.component.table.config.TableConfig
import org.koin.compose.viewmodel.koinViewModel

/**
 * 🎨 增强版通用表格组件
 *
 * 使用 JetBrains Mellum 风格的卡片来展示表头和分页，
 * 提供更现代化的视觉效果和用户体验
 *
 * @param modifier 修饰符
 * @param config 表格配置
 * @param buttonSlot 自定义按钮插槽
 * @param actionSlot 自定义操作列内容
 * @param headerCardType 表头卡片类型
 * @param paginationCardType 分页卡片类型
 * @param showStatsCard 是否显示统计信息卡片
 * @param statsCardType 统计卡片类型
 * @param compactPagination 是否使用紧凑分页模式
 * @param enableHeaderHover 是否启用表头悬浮效果
 */
@Composable
fun AddGenericTable(
    modifier: Modifier = Modifier,
    config: TableConfig,
    buttonSlot: @Composable (() -> Unit)? = null,
    actionSlot: @Composable () -> Unit = {},
    headerCardType: MellumCardType = MellumCardType.Light,
    paginationCardType: MellumCardType = MellumCardType.Light,
    showStatsCard: Boolean = false,
    statsCardType: MellumCardType = MellumCardType.Blue,
    compactPagination: Boolean = false,
    enableHeaderHover: Boolean = true
) {
    val horizontalScrollState = rememberScrollState()
    val tableViewModel = koinViewModel<TableCompositeViewModel>()
    tableViewModel.initialize(config)

    val renderButtons: @Composable () -> Unit = {
        DefaultTableButtonsWithCards(tableViewModel, buttonSlot)
    }

    Column(modifier = modifier) {
        // 统计信息卡片（可选）
        if (showStatsCard) {
            AddTableStatsCard(
                modifier = Modifier.fillMaxWidth(),
                totalCount = tableViewModel.paginationViewModel.pageState.totalItems,
                selectedCount = tableViewModel.selectionViewModel.selectedItems.size,
                filteredCount = if (tableViewModel.searchViewModel.filterState.isNotEmpty()) {
                    tableViewModel.paginationViewModel.pageState.totalItems
                } else null,
                cardType = statsCardType
            )
        }

        // 表格头部卡片
        AddTableHeader(
            renderButtons = renderButtons,
            keyword = tableViewModel.searchViewModel.keyword,
            onKeyWordChanged = { tableViewModel.searchViewModel.updateKeyword(it) },
            onSearch = { tableViewModel.search() },
            headerHeight = config.uiConfig.headerHeight,
            enableEditMode = tableViewModel.selectionViewModel.enableEditMode,
            isPageAllSelected = tableViewModel.selectionViewModel.isPageAllSelected(
                tableViewModel.dataViewModel.data,
                config.dataConfig.getIdFun
            ),
            onCheckedChange = { tableViewModel.togglePageSelection() },
            columns = tableViewModel.dataViewModel.columns,
            onChangeSorting = { tableViewModel.changeSorting(it) },
            onColumnAdvSearchClick = { column ->
                tableViewModel.showColumnAdvSearch(column)
            },
            stateSorts = tableViewModel.sortViewModel.sortState,
            filterStateMap = tableViewModel.searchViewModel.filterStateMap,
            cardType = headerCardType,
            enableHoverEffect = enableHeaderHover
        )

        // 表格内容
        TableContent(
            getIdFun = config.dataConfig.getIdFun,
            data = tableViewModel.dataViewModel.data,
            selectedItems = tableViewModel.selectionViewModel.selectedItems,
            columns = tableViewModel.dataViewModel.columns,
            pageState = tableViewModel.paginationViewModel.pageState,
            multiSelect = tableViewModel.selectionViewModel.enableEditMode,
            showActions = tableViewModel.uiStateViewModel.showActions,
            rowHeight = config.uiConfig.rowHeight,
            onRowClick = config.dataConfig.onRowClick,
            onDeleteItem = { id ->
                tableViewModel.deleteData(id)
            },
            scrollState = horizontalScrollState,
            modifier = Modifier.weight(1f),
            renderCustomActions = actionSlot,
            onCheckboxClick = {
                tableViewModel.toggleItemSelection(it)
            },
            onBatchDelete = { tableViewModel.batchDelete() },
            onBatchExport = { tableViewModel.batchExport() },
            onEditClick = {
                tableViewModel.showEditForm(it)
            },
        )

        // 分页控件卡片
        if (config.uiConfig.showPagination) {
            AddTablePagination(
                statePagination = tableViewModel.paginationViewModel.pageState,
                enablePagination = true,
                onPageSizeChange = {
                    tableViewModel.changePageSize(it)
                },
                onGoFirstPage = {
                    tableViewModel.paginationViewModel.goToFirstPage()
                    tableViewModel.queryPage()
                },
                onPreviousPage = { tableViewModel.previousPage() },
                onGoToPage = { tableViewModel.goToPage(it) },
                onNextPage = { tableViewModel.nextPage() },
                onGoLastPage = {
                    tableViewModel.paginationViewModel.goToLastPage()
                    tableViewModel.queryPage()
                },
                cardType = paginationCardType,
                compactMode = compactPagination
            )
        }
    }

    // 高级搜索弹窗
    if (tableViewModel.searchViewModel.showAdvancedSearch) {
        AddAdvSearchPopBar(
            column = tableViewModel.searchViewModel.currentAdvSearchColumn,
            showFieldAdvSearch = tableViewModel.searchViewModel.showFieldAdvSearch,
            onAdvSearchSubmit = { tableViewModel.submitAdvSearch() },
            onClose = { tableViewModel.searchViewModel.hideColumnAdvSearch() },
            searchForm = tableViewModel.searchViewModel.currentAdvSearchColumn?.let {
                tableViewModel.searchViewModel.filterStateMap[it.key]
            },
            onAdvSearchCLean = {
                tableViewModel.clearColumnFilter()
            },
            onSearchFormChange = { columnValue, operator, logicType ->
                tableViewModel.updateSearchForm(columnValue, operator, logicType)
            }
        )
    }
}

/**
 * 🎨 预设的表格卡片样式组合
 */
object TableCardStyles {

    /**
     * 深色主题样式
     */
    @Composable
    fun DarkTheme(
        modifier: Modifier = Modifier,
        config: TableConfig,
        buttonSlot: @Composable (() -> Unit)? = null,
        actionSlot: @Composable () -> Unit = {}
    ) {
        AddGenericTable(
            modifier = modifier,
            config = config,
            buttonSlot = buttonSlot,
            actionSlot = actionSlot,
            headerCardType = MellumCardType.Dark,
            paginationCardType = MellumCardType.Dark,
            showStatsCard = true,
            statsCardType = MellumCardType.Dark
        )
    }


    /**
     * 浅色主题样式
     */
    @Composable
    fun LightTheme(
        modifier: Modifier = Modifier,
        config: TableConfig,
        buttonSlot: @Composable (() -> Unit)? = null,
        actionSlot: @Composable () -> Unit = {}
    ) {
        AddGenericTable(
            modifier = modifier,
            config = config,
            buttonSlot = buttonSlot,
            actionSlot = actionSlot,
            headerCardType = MellumCardType.Light,
            paginationCardType = MellumCardType.Light,
            showStatsCard = true,
            statsCardType = MellumCardType.Light
        )
    }
}

/**
 * 默认的表格按钮（复用原有逻辑）
 */
@Composable
private fun DefaultTableButtonsWithCards(
    tableViewModel: TableCompositeViewModel,
    buttonSlot: @Composable (() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        buttonSlot?.invoke()
        // 多选模式按钮
        AddIconButton(
            text = if (tableViewModel.selectionViewModel.enableEditMode) "退出多选" else "多选",
            imageVector = if (tableViewModel.selectionViewModel.enableEditMode) Icons.Default.Deselect else Icons.Default.SelectAll,
            onClick = {
                tableViewModel.selectionViewModel.toggleEditMode()
            }
        )

        // 新增按钮
        AddIconButton(
            text = "新增",
            imageVector = Icons.Default.Add,
            onClick = { tableViewModel.showAddForm() }
        )

        // 导入按钮
        AddIconButton(
            text = "导入",
            imageVector = Icons.Default.UploadFile,
            onClick = { tableViewModel.uiStateViewModel.showImportDialog() }
        )

        // 导出按钮
        AddIconButton(
            text = "导出",
            imageVector = Icons.Default.DownloadForOffline,
            onClick = { tableViewModel.uiStateViewModel.showExportDropDown() }
        )
    }
}
