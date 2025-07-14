package com.addzero.kmp.component.table.generictable.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.high_level.AddTooltipBox
import com.addzero.kmp.entity.low_table.EnumExportType
import com.addzero.kmp.entity.low_table.ExportParam
import com.addzero.kmp.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * 导出按钮
 * @param [permissionCode]
 * @param [selectedItems]
 * @param [showExportMenu]
 * @param [onExportButtonClick] 导出按钮点击
 * @param [onExportDropDownItemClick] 导出下拉点击
 * @param [onDismissDropDownMenu]
 */
@Composable
fun ExportButtonContent(
    permissionCode: String,
    selectedItems: Set<Any>,
    showExportMenu: Boolean,
    onExportButtonClick: () -> Unit,
    onExportDropDownItemClick: (ExportParam) -> Unit,
    onDismissDropDownMenu: () -> Unit
) {
    val koinViewModel = koinViewModel<LoginViewModel>()
    val hasPermition = koinViewModel.hasPermission(permissionCode)

    if (!hasPermition) return

    //到处所选
    val notEmpty = selectedItems.isNotEmpty()
    val string1 = if (notEmpty) {
        "导出所选"
    } else {
        "导出全部"
    }
    AddTooltipBox("导出") {
        Box {
            IconButton(onClick = onExportButtonClick) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = "导出数据"
                )
            }


//        UseDropdownSelector(
//            label = string1,
//            options = TODO(),
//            selectedItem = TODO(),
//            onOptionSelected = TODO(),
//            getDisplayText = TODO(),
//            labelWidth = TODO()
//        )

            // 导出下拉菜单
            DropdownMenu(
                expanded = showExportMenu,
                onDismissRequest = onDismissDropDownMenu
            ) {

                EnumExportType.entries.map {
                    val exportParam = ExportParam(it, ids = selectedItems)
                    // 导出所选选项
                    DropdownMenuItem(
                        text = {
                            Text("${string1}为$it")
                        },
                        onClick = { onExportDropDownItemClick(exportParam) },
                    )
                }
            }
        }

    }
    Spacer(modifier = Modifier.Companion.width(8.dp))


}

