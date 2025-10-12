package site.addzero.component.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay

/**
 * 简单搜索组件 - 常规 Compose 组件设计
 *
 * @param T 搜索结果数据类型
 * @param value 当前搜索值
 * @param onValueChange 值变化回调
 * @param searchItems 所有可搜索的项目
 * @param renderItem 渲染单个项目的函数
 * @param placeholder 搜索框占位符
 * @param modifier 修饰符
 * @param onResultClick 结果点击回调
 * @param minSearchLength 触发搜索的最小字符数
 * @param searchDelay 搜索防抖延迟
 */
@Composable
fun <T> SimpleSearchComponent(
    value: String,
    onValueChange: (String) -> Unit,
    searchItems: List<T>,
    renderItem: @Composable (T, Boolean, () -> Unit) -> Unit,
    placeholder: String = "搜索...",
    modifier: Modifier = Modifier,
    onResultClick: (T) -> Unit = {},
    minSearchLength: Int = 1,
    searchDelay: Long = 300
) {
    val searchResults = remember(value, searchItems) { mutableStateOf<List<T>>(emptyList()) }
    val isLoading = remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableStateOf(-1) }

    // 执行搜索
    LaunchedEffect(value) {
        if (value.length < minSearchLength) {
            searchResults.value = emptyList()
            selectedIndex.value = -1
            return@LaunchedEffect
        }

        isLoading.value = true
        delay(searchDelay)

        // 简单的搜索逻辑
        searchResults.value = searchItems.filter { item ->
            // 默认使用 toString() 进行匹配，用户可以在 renderItem 中自定义显示
            item.toString().contains(value, ignoreCase = true)
        }
        selectedIndex.value = 0
        isLoading.value = false
    }

    Column(modifier = modifier) {
        // 搜索框
        SearchInput(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 搜索结果
        when {
            isLoading.value -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            searchResults.value.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(searchResults.value) { item ->
                        val index = searchResults.value.indexOf(item)
                        val isSelected = index == selectedIndex.value

                        renderItem(
                            item,
                            isSelected
                        ) {
                            onResultClick(item)
                        }
                    }
                }
            }
            value.length >= minSearchLength && searchResults.value.isEmpty() -> {
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
    }
}

/**
 * 搜索弹窗组件
 *
 * @param T 搜索结果数据类型
 * @param isVisible 是否显示弹窗
 * @param onDismiss 关闭弹窗回调
 * @param searchItems 所有可搜索的项目
 * @param renderItem 渲染单个项目的函数
 * @param placeholder 搜索框占位符
 * @param onResultClick 结果点击回调
 * @param modifier 修饰符
 */
@Composable
fun <T> SearchDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    searchItems: List<T>,
    renderItem: @Composable (T, Boolean, () -> Unit) -> Unit,
    placeholder: String = "搜索...",
    onResultClick: (T) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (!isVisible) return

    var searchValue by remember { mutableStateOf("") }

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
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f))
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
                        placeholder = placeholder,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 搜索结果
                    SimpleSearchComponent(
                        value = searchValue,
                        onValueChange = { searchValue = it },
                        searchItems = searchItems,
                        renderItem = renderItem,
                        onResultClick = { item ->
                            onResultClick(item)
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

/**
 * 侧边栏搜索组件 - 过滤式搜索
 *
 * @param T 搜索结果数据类型
 * @param searchValue 搜索值
 * @param onSearchValueChange 搜索值变化回调
 * @param allItems 所有项目
 * @param renderItem 渲染单个项目的函数
 * @param placeholder 搜索框占位符
 * @param modifier 修饰符
 * @param onResultClick 结果点击回调
 * @param filterFunction 自定义过滤函数，默认使用字符串包含匹配
 */
@Composable
fun <T> SidebarSearch(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    allItems: List<T>,
    renderItem: @Composable (T, Boolean, () -> Unit) -> Unit,
    placeholder: String = "搜索...",
    modifier: Modifier = Modifier,
    onResultClick: (T) -> Unit = {},
    filterFunction: ((T, String) -> Boolean)? = null
) {
    // 过滤后的项目
    val filteredItems = remember(searchValue, allItems) {
        if (searchValue.isBlank()) {
            allItems
        } else {
            allItems.filter { item ->
                if (filterFunction != null) {
                    filterFunction(item, searchValue)
                } else {
                    item.toString().contains(searchValue, ignoreCase = true)
                }
            }
        }
    }

    Column(modifier = modifier) {
        // 搜索框
        SearchInput(
            value = searchValue,
            onValueChange = onSearchValueChange,
            placeholder = placeholder,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 过滤结果
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filteredItems) { item ->
                // 高亮匹配的项目
                val isHighlighted = searchValue.isNotBlank() &&
                    item.toString().contains(searchValue, ignoreCase = true)

                renderItem(
                    item,
                    isHighlighted
                ) {
                    onResultClick(item)
                }
            }
        }

        // 搜索统计
        if (searchValue.isNotBlank()) {
            Text(
                text = "找到 ${filteredItems.size} 个结果",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * 简单的组合式函数 - 只管理搜索状态
 *
 * @param searchFunction 搜索函数
 * @param debounceMs 防抖时间
 */
@Composable
fun <T> useSearchState(
    searchFunction: suspend (String) -> List<T>,
    debounceMs: Long = 300
): SearchState<T> {
    var keyword by remember { mutableStateOf("") }
    val results = remember { mutableStateOf<List<T>>(emptyList()) }
    val isLoading = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf<String?>(null) }

    // 执行搜索
    LaunchedEffect(keyword) {
        if (keyword.isBlank()) {
            results.value = emptyList()
            error.value = null
            return@LaunchedEffect
        }

        isLoading.value = true
        error.value = null
        delay(debounceMs)

        try {
            results.value = searchFunction(keyword)
        } catch (e: Exception) {
            error.value = e.message
            results.value = emptyList()
        } finally {
            isLoading.value = false
        }
    }

    return remember {
        object : SearchState<T> {
            override var keyword: String
                get() = keyword
                set(value) { keyword = value }
            override val results: List<T>
                get() = results.value
            override val isLoading: Boolean
                get() = isLoading.value
            override val error: String?
                get() = error.value
            override val resultCount: Int
                get() = results.value.size

            override fun search(query: String) {
                keyword = query
            }

            override fun clear() {
                keyword = ""
                results.value = emptyList()
                error.value = null
            }
        }
    }
}

/**
 * 简单的搜索状态接口
 */
interface SearchState<T> {
    var keyword: String
    val results: List<T>
    val isLoading: Boolean
    val error: String?
    val resultCount: Int

    fun search(query: String)
    fun clear()
}

/**
 * 搜索列表组件 - 使用组合式函数
 *
 * @param T 搜索结果数据类型
 * @param searchState 搜索状态
 * @param renderItem 渲染单个项目的函数
 * @param modifier 修饰符
 * @param onResultClick 结果点击回调
 * @param emptyMessage 空结果提示
 */
@Composable
fun <T> SearchList(
    searchState: SearchState<T>,
    renderItem: @Composable (T, () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    onResultClick: (T) -> Unit = {},
    emptyMessage: String = "没有找到匹配结果"
) {
    Column(modifier = modifier) {
        when {
            searchState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            searchState.error != null -> {
                Text(
                    text = "搜索错误: ${searchState.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            searchState.results.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(searchState.results) { item ->
                        renderItem(item) {
                            onResultClick(item)
                        }
                    }
                }
            }
            searchState.keyword.isNotBlank() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = emptyMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}