package com.addzero.kmp.component.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.addzero.kmp.component.high_level.AddTooltipBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIconButton(
    showFlag: Boolean = true,
    text: String,
    imageVector: ImageVector = Icons.Default.Add,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    content: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    if (!showFlag) {
        return
    }


    val defaultContent: @Composable () -> Unit = content ?: {

        IconButton(
            onClick = onClick,
        ) {
            Icon(
                imageVector = imageVector, contentDescription = text, tint = tint, modifier = modifier
            )
        }


    }


    AddTooltipBox(text) {
        defaultContent()
    }
}

