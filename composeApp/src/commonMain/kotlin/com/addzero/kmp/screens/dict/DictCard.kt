package com.addzero.kmp.screens.dict

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.button.AddEditDeleteButton
import com.addzero.kmp.isomorphic.SysDictIso

/**
 * 字典类型卡片
 */
@Composable
fun DictCard(
    dictType: SysDictIso,
    isSelected: Boolean,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val backgroundColor = if (isSelected)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)

    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimaryContainer
    else
        MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            // 指示器
            Box(
                modifier = Modifier.Companion
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
            )

            Spacer(modifier = Modifier.Companion.width(12.dp))

            Column(modifier = Modifier.Companion.weight(1f)) {
                Text(
                    text = dictType.dictName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Companion.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Companion.Ellipsis
                )

                Text(
                    text = dictType.dictCode,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected)
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )


            }

            // 项目数量指示
            val itemCount = dictType.sysDictItems?.size ?: 0
            if (itemCount > 0) {
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            text = "$itemCount 项",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        labelColor = MaterialTheme.colorScheme.primary
                    ),
                    border = null
                )
            }
            AddEditDeleteButton(onEditClick, onDeleteClick)

            //编辑按钮
//            AddIconButton(
//                "编辑字典",
//                onClick = onEditClick,
//                imageVector = Icons.Default.Edit,
//            )
//            AddDeleteButton { onDeleteClick() }

        }
    }
}
