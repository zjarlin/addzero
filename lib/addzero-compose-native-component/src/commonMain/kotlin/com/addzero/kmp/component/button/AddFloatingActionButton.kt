package com.addzero.kmp.component.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.addzero.kmp.component.high_level.AddTooltipBox

/**
 * 高阶浮动操作按钮组件
 * 仿照 AddIconButton 的设计模式，提供 Tooltip 支持和自定义内容
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFloatingActionButton(
    text: String,
    imageVector: ImageVector = Icons.Default.Add,
    modifier: Modifier = Modifier.Companion,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    content: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    val defaultContent: @Composable () -> Unit = content ?: {
        Icon(
            imageVector = imageVector,
            contentDescription = text,
            tint = contentColor
        )
    }

    AddTooltipBox(text) {

        FloatingActionButton(
            onClick = onClick,
            modifier = modifier,
            containerColor = containerColor,
            contentColor = contentColor
        ) {
            defaultContent()

        }


    }
}