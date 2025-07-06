package com.addzero.kmp.demo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.high_level.AddTabs
import com.addzero.kmp.component.high_level.CustomTabRow
import com.addzero.kmp.component.high_level.TabItem
import com.addzero.kmp.compose.icons.IconKeys
import com.addzero.kmp.processor.annotation.Route

/**
 * 标签页示例屏幕
 */
@Route(
    value = "组件示例",
    title = "多标签页组件",
    routePath = "examples/tabs",
    icon = IconKeys.TYPE_SHARP,
    order = 3.0
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabsExampleScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("标签页组件示例") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 基本示例
            Text(
                "基本标签页示例",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            // 创建标签项
            val tabs = remember {
                listOf(
                    TabItem("搜索结果") {
                        SearchResultsContent()
                    },
                    TabItem("收藏夹") {
                        FavoritesContent()
                    }
                )
            }

            // 使用 AddTabs 组件
            AddTabs(
                tabs = tabs,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 自定义标签行示例
            Text(
                "自定义标签示例",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            // 使用自定义标签行
            var selectedTabIndex by remember { mutableIntStateOf(0) }
            val tabTitles = remember { listOf("最新", "热门", "推荐") }

            CustomTabRow(
                tabs = tabTitles,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )

            // 自定义标签内容
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("显示「${tabTitles[selectedTabIndex]}」内容")
            }
        }
    }
}

/**
 * 搜索结果内容
 */
@Composable
private fun SearchResultsContent() {
    val searchResults = remember {
        List(20) { index ->
            "搜索结果 ${index + 1}"
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(searchResults) { result ->
            ListItem(
                headlineContent = { Text(result) },
                leadingContent = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null
                    )
                }
            )
        }
    }
}

/**
 * 收藏夹内容
 */
@Composable
private fun FavoritesContent() {
    val favorites = remember {
        List(10) { index ->
            "收藏项 ${index + 1}"
        }
    }

    if (favorites.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("暂无收藏内容")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            items(favorites) { favorite ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    ListItem(
                        headlineContent = { Text(favorite) },
                        leadingContent = {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }
            }
        }
    }
} 