package com.addzero.kmp.component.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.addzero.kmp.component.high_level.AddTooltipBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIconButton(
    text: String,
    imageVector: ImageVector = Icons.Default.Add,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
    content: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {


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

