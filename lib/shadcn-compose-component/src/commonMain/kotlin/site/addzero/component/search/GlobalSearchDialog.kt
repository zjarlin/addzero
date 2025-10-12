package site.addzero.component.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import site.addzero.themes.colors

/**
 * 简化的搜索结果项数据类
 */
data class SimpleSearchResult<T>(
    val data: T,
    val title: String,
    val subtitle: String? = null,
    val icon: String? = null
)

/**
 * 全局搜索弹窗组件
 *
 * @param T 搜索结果数据类型
 * @param isVisible 是否显示搜索弹窗
 * @param onDismiss 关闭弹窗回调
 * @param searchItems 所有可搜索的项目
 * @param renderFunction 渲染函数，将数据转换为显示格式
 * @param onResultClick 结果点击回调
 * @param placeholder 搜索框占位符
 * @param modifier 修饰符
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun <T> GlobalSearchDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    searchItems: List<T>,
    renderFunction: (T) -> SimpleSearchResult<T>,
    onResultClick: (T) -> Unit,
    placeholder: String = "搜索...",
    modifier: Modifier = Modifier
) {
    if (!isVisible) return

    var searchValue by remember { mutableStateOf("") }
    val searchResults = remember(searchValue, searchItems) {
        if (searchValue.isBlank()) {
            emptyList()
        } else {
            searchItems.filter { item ->
                val result = renderFunction(item)
                result.title.contains(searchValue, ignoreCase = true) ||
                        result.subtitle?.contains(searchValue, ignoreCase = true) == true
            }.map { renderFunction(it) }
        }
    }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val lazyListState = rememberLazyListState()
    val selectedIndex = remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    // 搜索框打开时自动聚焦
    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(50)
            focusRequester.requestFocus()
        }
    }

    // 搜索结果变化时重置选中项
    LaunchedEffect(searchResults) {
        selectedIndex.value = 0
        if (searchResults.isNotEmpty()) {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(0)
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        // 半透明遮罩层
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .zIndex(-1f)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            // 搜索界面框
            Card(
                modifier = modifier
                    .width(400.dp)
                    .heightIn(max = 500.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // 搜索输入框
                    SearchInput(
                        value = searchValue,
                        onValueChange = { searchValue = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onPreviewKeyEvent { keyEvent ->
                                when {
                                    // Escape: 关闭搜索
                                    (keyEvent.key == Key.Escape && keyEvent.type == KeyEventType.KeyDown) -> {
                                        onDismiss()
                                        true
                                    }
                                    // 向下箭头: 选择下一项
                                    (keyEvent.key == Key.DirectionDown && keyEvent.type == KeyEventType.KeyDown) -> {
                                        if (searchResults.isNotEmpty()) {
                                            selectedIndex.value = (selectedIndex.value + 1) % searchResults.size
                                            coroutineScope.launch {
                                                lazyListState.animateScrollToItem(selectedIndex.value)
                                            }
                                        }
                                        true
                                    }
                                    // 向上箭头: 选择上一项
                                    (keyEvent.key == Key.DirectionUp && keyEvent.type == KeyEventType.KeyDown) -> {
                                        if (searchResults.isNotEmpty()) {
                                            selectedIndex.value = if (selectedIndex.value > 0)
                                                selectedIndex.value - 1
                                            else
                                                searchResults.size - 1
                                            coroutineScope.launch {
                                                lazyListState.animateScrollToItem(selectedIndex.value)
                                            }
                                        }
                                        true
                                    }
                                    // 回车: 选择当前项
                                    (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyDown) -> {
                                        if (searchResults.isNotEmpty() && selectedIndex.value < searchResults.size) {
                                            onResultClick(searchResults[selectedIndex.value].data)
                                            onDismiss()
                                        }
                                        true
                                    }

                                    else -> false
                                }
                            },
                        placeholder = placeholder,
                        variant = SearchInputVariant.Outline,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                            }
                        ),
                        onClearSearch = {
                            searchValue = ""
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 搜索结果列表
                    when {
                        searchValue.isBlank() -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "输入关键词开始搜索",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        searchResults.isNotEmpty() -> {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 300.dp),
                                state = lazyListState
                            ) {
                                items(searchResults) { item ->
                                    val index = searchResults.indexOf(item)
                                    val isSelected = index == selectedIndex.value

                                    SearchResultItemRow(
                                        item = item,
                                        isSelected = isSelected,
                                        onClick = {
                                            onResultClick(item.data)
                                            onDismiss()
                                        }
                                    )

                                    if (index < searchResults.size - 1) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            thickness = DividerDefaults.Thickness,
                                            color = colors.border.copy(alpha = 0.3f)
                                        )
                                    }
                                }
                            }
                        }

                        else -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "没有找到匹配结果",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // 快捷键提示
                    if (searchResults.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "↑↓ 选择",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Enter 选择",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Esc 关闭",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 搜索结果项行组件
 */
@Composable
private fun <T> SearchResultItemRow(
    item: SimpleSearchResult<T>,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colors

    ListItem(
        headlineContent = {
            Text(
                text = item.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (isSelected) colors.primary else LocalContentColor.current,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        },
        supportingContent = item.subtitle?.let { subtitle ->
            {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = colors.mutedForeground
                )
            }
        },
        leadingContent = item.icon?.let { icon ->
            {
                Text(
                    text = icon,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.mutedForeground
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else Color.Transparent
            )
            .padding(4.dp)
    )
}

/**
 * 全局搜索按钮组件
 */
@Composable
fun GlobalSearchButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "搜索",
            tint = colors.mutedForeground
        )
    }
}

/**
 * 带快捷键的全局搜索按钮组件
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GlobalSearchButtonWithShortcut(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onKeyPressed: ((KeyEvent) -> Boolean)? = null
) {
    Box(
        modifier = modifier.onPreviewKeyEvent { keyEvent ->
            // 处理快捷键
            when {
                (keyEvent.key == Key.K && keyEvent.isMetaPressed && keyEvent.type == KeyEventType.KeyDown) -> {
                    onClick()
                    true
                }

                else -> onKeyPressed?.invoke(keyEvent) ?: false
            }
        }
    ) {
        GlobalSearchButton(onClick = onClick)
    }
}
