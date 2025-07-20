package com.addzero.kmp.component.tree_command

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 命令工具栏
 */
@Composable
fun CommandToolbar(
    commands: Set<TreeCommand>,
    multiSelectMode: Boolean,
    onCommandClick: (TreeCommand) -> Unit
) {
    Surface(
        modifier = Modifier.Companion.fillMaxWidth(),
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            commands.forEach { command ->
                val (icon, tint) = when (command) {
                    TreeCommand.SEARCH -> Icons.Default.Search to MaterialTheme.colorScheme.primary
                    TreeCommand.MULTI_SELECT -> {
                        if (multiSelectMode)
                            Icons.Default.CheckBox to MaterialTheme.colorScheme.primary
                        else
                            Icons.Default.CheckBoxOutlineBlank to MaterialTheme.colorScheme.onSurface
                    }

                    TreeCommand.EXPAND_ALL -> Icons.Default.UnfoldMore to MaterialTheme.colorScheme.onSurface
                    TreeCommand.COLLAPSE_ALL -> Icons.Default.UnfoldLess to MaterialTheme.colorScheme.onSurface
                    TreeCommand.REFRESH -> Icons.Default.Refresh to MaterialTheme.colorScheme.onSurface
                    TreeCommand.FILTER -> Icons.Default.FilterList to MaterialTheme.colorScheme.onSurface
                    TreeCommand.SORT -> Icons.AutoMirrored.Filled.Sort to MaterialTheme.colorScheme.onSurface
                    TreeCommand.ADD_NODE -> Icons.Default.Add to MaterialTheme.colorScheme.onSurface
                    TreeCommand.EDIT_NODE -> Icons.Default.Edit to MaterialTheme.colorScheme.onSurface
                    TreeCommand.DELETE_NODE -> Icons.Default.Delete to MaterialTheme.colorScheme.error
                    TreeCommand.DRAG_DROP -> Icons.Default.DragIndicator to MaterialTheme.colorScheme.onSurface
                    TreeCommand.EXPORT -> Icons.Default.FileDownload to MaterialTheme.colorScheme.onSurface
                    TreeCommand.IMPORT -> Icons.Default.FileUpload to MaterialTheme.colorScheme.onSurface
                }

                IconButton(onClick = { onCommandClick(command) }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = command.name,
                        tint = tint
                    )
                }
            }
        }
    }
}
