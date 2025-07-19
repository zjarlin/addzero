package com.addzero.kmp.component.search_bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.button.AddIconButton

/**
 * 搜索栏组件
 * 支持回车搜索和更友好的提示
 */
@Composable
fun AddSearchBar(
    keyword: String,
    onKeyWordChanged: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "请输入关键词搜索..."
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 使用一个变量来记录文本框的高度
        val textFieldHeight = 56.dp

        OutlinedTextField(
            value = keyword,
            onValueChange = onKeyWordChanged,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                    )
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "搜索",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                if (keyword.isNotEmpty()) {
                    IconButton(
                        onClick = {onKeyWordChanged("")},
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "清除",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .weight(1f)
                .height(textFieldHeight) // 指定高度
                .onPreviewKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyUp &&
                        keyEvent.key == Key.Enter &&
                        keyword.isNotEmpty()
                    ) {
                        onSearch()
                        true
                    } else {
                        false
                    }
                },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            supportingText = null // 移除底部提示文本以保持高度一致
        )

        // 与文本框同高的按钮
        AddIconButton(text = "刷新", imageVector = Icons.Default.Refresh, onClick = onSearch)
//        Button(
//            onClick = onSearch,
////            enabled = keyword.isNotEmpty(),
//            //始终开启
//            enabled = true,
//            shape = RoundedCornerShape(8.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.primary,
//                contentColor = MaterialTheme.colorScheme.onPrimary,
//                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
//                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
//            ),
//            modifier = Modifier
//                .height(textFieldHeight) // 使按钮高度与文本框相同
//                .padding(0.dp) // 移除内边距以避免高度不一致
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                Icon(
//                    Icons.Default.Search,
//                    contentDescription = null,
//                    modifier = Modifier.size(20.dp)
//                )
//                Text(
//                    text = searchButtonText,
//                    style = MaterialTheme.typography.bodyMedium.copy(
//                        fontWeight = FontWeight.Medium
//                    )
//                )
//            }
//        }
    }

    // 如果需要提示文本，可以放在Row下方
//    if (keyword.isEmpty()) {
//        Text(
//            "提示: 输入关键词后按回车键或点击搜索按钮",
//            style = MaterialTheme.typography.bodySmall,
//            textAlign = TextAlign.Start,
//            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
//            color = MaterialTheme.colorScheme.outline
//        )
//    }
}
