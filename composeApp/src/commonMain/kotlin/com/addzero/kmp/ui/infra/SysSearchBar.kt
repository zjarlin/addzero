package com.addzero.kmp.ui.infra

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.addzero.kmp.entity.sys.menu.SysMenuVO
import com.addzero.kmp.ui.infra.model.menu.MenuViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 全局搜索栏组件
 *
 * 支持通过Cmd+K快捷键呼出的全局菜单搜索功能
 *
 * @param navController 导航控制器
 * @param modifier 修饰符
 * @param isSearchOpen 搜索框是否打开的外部状态，如果为null则使用内部状态
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddSysRouteSearchBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    isSearchOpen: MutableState<Boolean>? = null
) {
    val searchText = remember { mutableStateOf("") }
    val internalSearchOpen = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(0) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val isNavigatingList = remember { mutableStateOf(false) }

    // 使用外部状态或内部状态
    val searchOpenState = isSearchOpen ?: internalSearchOpen

    // 全局键盘事件监听
    Box(
        modifier = modifier.onPreviewKeyEvent { keyEvent ->
            when {
                // Cmd+K：打开搜索框
                (keyEvent.key == Key.K && keyEvent.isMetaPressed &&
                        keyEvent.type == KeyEventType.KeyDown) -> {
                    searchOpenState.value = true
                    coroutineScope.launch {
                        delay(50) // 延迟以确保UI已更新
                        focusRequester.requestFocus()
                    }
                    true
                }

                else -> false
            }
        }
    ) {
        // 搜索按钮
        IconButton(
            onClick = {
                searchOpenState.value = true
                coroutineScope.launch {
                    delay(50)
                    focusRequester.requestFocus()
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "搜索",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        // 搜索弹窗
        if (searchOpenState.value) {
            // 使用Dialog替代Popup，确保居中显示
            Dialog(
                onDismissRequest = {
                    searchOpenState.value = false
                    searchText.value = ""
                    selectedIndex.value = 0
                    isNavigatingList.value = false
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                    usePlatformDefaultWidth = false // 不使用平台默认宽度，使用我们自定义的宽度
                )
            ) {
                val allMenuItems = flattenMenuItems(MenuViewModel.menuItems)
                val filteredItems = if (searchText.value.isBlank()) {
                    allMenuItems
                } else {
                    allMenuItems.filter {
                        it.title.contains(searchText.value, ignoreCase = true)
                    }
                }

                // 每当过滤结果变化，重置选中项和导航状态
                LaunchedEffect(filteredItems) {
                    selectedIndex.value = 0
                    isNavigatingList.value = false
                }

                // 搜索框打开时自动聚焦
                LaunchedEffect(searchOpenState.value) {
                    if (searchOpenState.value) {
                        delay(50)
                        focusRequester.requestFocus()
                        isNavigatingList.value = false
                    }
                }

                // 为确保视觉效果，添加一个半透明遮罩层
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .zIndex(-1f) // 确保遮罩在内容下方
                        .clickable(onClick = {
                            searchOpenState.value = false
                            searchText.value = ""
                        }),
                    contentAlignment = Alignment.Center
                ) {
                    // 搜索界面框
                    Column(
                        modifier = Modifier
                            .width(400.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                            // 阻止点击事件冒泡到遮罩层
                            .clickable(onClick = {}, enabled = false)
                    ) {
                        // 搜索输入框
                        OutlinedTextField(
                            value = searchText.value,
                            onValueChange = {
                                searchText.value = it
                                isNavigatingList.value = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                                .onPreviewKeyEvent { keyEvent ->
                                    when {
                                        // Escape: 关闭搜索
                                        (keyEvent.key == Key.Escape &&
                                                keyEvent.type == KeyEventType.KeyDown) -> {
                                            searchOpenState.value = false
                                            searchText.value = ""
                                            selectedIndex.value = 0
                                            isNavigatingList.value = false
                                            true
                                        }
                                        // 向下箭头: 选择下一项
                                        (keyEvent.key == Key.DirectionDown &&
                                                keyEvent.type == KeyEventType.KeyDown) -> {
                                            if (filteredItems.isNotEmpty()) {
                                                selectedIndex.value = (selectedIndex.value + 1) % filteredItems.size
                                                isNavigatingList.value = true
                                                coroutineScope.launch {
                                                    lazyListState.animateScrollToItem(selectedIndex.value)
                                                }
                                            }
                                            true
                                        }
                                        // 向上箭头: 选择上一项
                                        (keyEvent.key == Key.DirectionUp &&
                                                keyEvent.type == KeyEventType.KeyDown) -> {
                                            if (filteredItems.isNotEmpty()) {
                                                selectedIndex.value = if (selectedIndex.value > 0)
                                                    selectedIndex.value - 1
                                                else
                                                    filteredItems.size - 1
                                                isNavigatingList.value = true
                                                coroutineScope.launch {
                                                    lazyListState.animateScrollToItem(selectedIndex.value)
                                                }
                                            }
                                            true
                                        }
                                        // Tab键：在列表导航和搜索框之间切换焦点
                                        (keyEvent.key == Key.Tab &&
                                                keyEvent.type == KeyEventType.KeyDown) -> {
                                            isNavigatingList.value = !isNavigatingList.value
                                            true
                                        }
                                        // 回车: 导航到选中项
                                        (keyEvent.key == Key.Enter &&
                                                keyEvent.type == KeyEventType.KeyDown) -> {
                                            if (filteredItems.isNotEmpty() && selectedIndex.value < filteredItems.size) {
                                                val selectedMenu = filteredItems[selectedIndex.value]
                                                navigateToMenu(selectedMenu, navController)
                                                searchOpenState.value = false
                                                searchText.value = ""
                                                selectedIndex.value = 0
                                                isNavigatingList.value = false
                                            }
                                            true
                                        }

                                        else -> false
                                    }
                                },
                            placeholder = { Text("搜索菜单 (Cmd+K)") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "搜索图标"
                                )
                            },
                            trailingIcon = {
                                if (searchText.value.isNotEmpty()) {
                                    IconButton(onClick = {
                                        searchText.value = ""
                                        isNavigatingList.value = false
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "清除"
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (filteredItems.isNotEmpty() && selectedIndex.value < filteredItems.size) {
                                        val selectedMenu = filteredItems[selectedIndex.value]
                                        navigateToMenu(selectedMenu, navController)
                                        searchOpenState.value = false
                                        searchText.value = ""
                                        selectedIndex.value = 0
                                        isNavigatingList.value = false
                                    }
                                    focusManager.clearFocus()
                                }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (isNavigatingList.value)
                                    MaterialTheme.colorScheme.outlineVariant
                                else
                                    MaterialTheme.colorScheme.primary
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 搜索结果列表
                        AnimatedVisibility(
                            visible = filteredItems.isNotEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 300.dp),
                                state = lazyListState
                            ) {
                                items(filteredItems) { menuItem ->
                                    val isSelected = filteredItems.indexOf(menuItem) == selectedIndex.value

                                    ListItem(
                                        headlineContent = {
                                            Text(
                                                text = menuItem.title,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                // 如果当前项被选中且处于列表导航模式，使用不同颜色
                                                color = if (isSelected && isNavigatingList.value)
                                                    MaterialTheme.colorScheme.onPrimaryContainer
                                                else
                                                    LocalContentColor.current
                                            )
                                        },
                                        supportingContent = {
                                            if (menuItem.parentPath != null) {
                                                val parent = allMenuItems.find { it.path == menuItem.parentPath }
                                                if (parent != null) {
                                                    Text(
                                                        text = parent.title,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                            }
                                        },
                                        leadingContent = {
                                            if (menuItem.icon.isNotEmpty()) {
                                                // 此处可以根据实际图标系统实现
                                                Text(menuItem.icon!!, style = MaterialTheme.typography.titleMedium)
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                navigateToMenu(menuItem, navController)
                                                searchOpenState.value = false
                                                searchText.value = ""
                                                selectedIndex.value = 0
                                                isNavigatingList.value = false
                                            }
                                            .background(
                                                if (isSelected)
                                                    if (isNavigatingList.value)
                                                        MaterialTheme.colorScheme.primaryContainer
                                                    else
                                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                                else
                                                    Color.Transparent
                                            )
                                            .let {
                                                // 如果当前项被选中且处于列表导航模式，添加边框高亮
                                                if (isSelected && isNavigatingList.value) {
                                                    it.border(
                                                        width = 1.dp,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        shape = RoundedCornerShape(4.dp)
                                                    )
                                                } else {
                                                    it
                                                }
                                            }
                                            .padding(4.dp)
                                    )

                                    if (filteredItems.indexOf(menuItem) < filteredItems.size - 1) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            thickness = DividerDefaults.Thickness,
                                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        }

                        // 无结果提示
                        AnimatedVisibility(
                            visible = searchText.value.isNotEmpty() && filteredItems.isEmpty(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "没有找到匹配结果",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // 快捷键提示
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            HelpText("↑↓ 选择")
                            HelpText("Tab 切换焦点")
                            HelpText("Enter 跳转")
                            HelpText("Esc 关闭")
                        }
                    }
                }
            }
        }
    }
}

/**
 * 递归展平菜单项，转换为列表
 */
private fun flattenMenuItems(menuItems: List<SysMenuVO>): List<SysMenuVO> {
    val result = mutableListOf<SysMenuVO>()

    fun traverse(items: List<SysMenuVO>) {
        for (item in items) {
            // 排除没有路径的菜单项
            if (item.path.isNotBlank()) {
                result.add(item)
            }

            if (item.children.isNotEmpty()) {
                traverse(item.children)
            }
        }
    }

    traverse(menuItems)
    return result.distinct()
}

/**
 * 导航到指定菜单
 */
private fun navigateToMenu(menuItem: SysMenuVO, navController: NavController) {
    navController.navigate(menuItem.path) {
        launchSingleTop = true
        restoreState = true
    }
}

/**
 * 帮助文本组件
 */
@Composable
private fun HelpText(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}
