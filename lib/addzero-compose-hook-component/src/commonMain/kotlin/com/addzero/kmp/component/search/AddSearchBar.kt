package com.addzero.kmp.component.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

/**
 * 🔍 搜索栏组件
 *
 * 基于 Material3 OutlinedTextField 的搜索组件，提供：
 * - 搜索图标和清除按钮
 * - 键盘搜索支持
 * - 自动焦点管理
 * - 搜索建议支持
 * - 搜索历史支持
 *
 * @param keyword 搜索关键词
 * @param onKeyWordChanged 关键词变化回调
 * @param onSearch 搜索回调
 * @param modifier 修饰符
 * @param placeholder 占位符文本
 * @param enabled 是否启用
 * @param autoFocus 是否自动获取焦点
 * @param showClearButton 是否显示清除按钮
 * @param maxLength 最大输入长度
 * @param suggestions 搜索建议列表
 * @param onSuggestionClick 建议点击回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSearchBar(
    keyword: String,
    onKeyWordChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索...",
    enabled: Boolean = true,
    autoFocus: Boolean = false,
    showClearButton: Boolean = true,
    maxLength: Int? = null,
    suggestions: List<String> = emptyList(),
    onSuggestionClick: ((String) -> Unit)? = null
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var showSuggestions by remember { mutableStateOf(false) }

    // 自动获取焦点
    LaunchedEffect(autoFocus) {
        if (autoFocus) {
            focusRequester.requestFocus()
        }
    }

    Column(modifier = modifier) {
        // 搜索输入框
        OutlinedTextField(
            value = keyword,
            onValueChange = { newValue ->
                val finalValue = if (maxLength != null && newValue.length > maxLength) {
                    newValue.take(maxLength)
                } else {
                    newValue
                }
                onKeyWordChanged(finalValue)
                showSuggestions = finalValue.isNotEmpty() && suggestions.isNotEmpty()
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            trailingIcon = if (showClearButton && keyword.isNotEmpty()) {
                {
                    IconButton(
                        onClick = {
                            onKeyWordChanged("")
                            showSuggestions = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "清除",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else null,
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(keyword)
                    keyboardController?.hide()
                    showSuggestions = false
                }
            )
        )

        // 搜索建议
        if (showSuggestions && suggestions.isNotEmpty()) {
            SearchSuggestions(
                suggestions = suggestions,
                onSuggestionClick = { suggestion ->
                    onKeyWordChanged(suggestion)
                    onSuggestionClick?.invoke(suggestion)
                    showSuggestions = false
                },
                onDismiss = { showSuggestions = false }
            )
        }
    }
}

/**
 * 搜索建议组件
 */
@Composable
private fun SearchSuggestions(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            suggestions.take(5).forEach { suggestion -> // 最多显示5个建议
                TextButton(
                    onClick = { onSuggestionClick(suggestion) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = suggestion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 🔍 紧凑搜索栏组件
 */
@Composable
fun AddCompactSearchBar(
    keyword: String,
    onKeyWordChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "搜索...",
    enabled: Boolean = true
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = keyword,
        onValueChange = onKeyWordChanged,
        modifier = modifier,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodySmall
            )
        },
        leadingIcon = {
            IconButton(
                onClick = { onSearch(keyword) }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        trailingIcon = if (keyword.isNotEmpty()) {
            {
                IconButton(
                    onClick = { onKeyWordChanged("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "清除",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        } else null,
        enabled = enabled,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(keyword)
                keyboardController?.hide()
            }
        )
    )
}

/**
 * 🔍 搜索按钮组件
 */
@Composable
fun AddSearchButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String = "搜索"
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )

        if (text.isNotEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text)
        }
    }
}

/**
 * 🔍 搜索图标按钮组件
 */
@Composable
fun AddSearchIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "搜索"
        )
    }
}
