package site.addzero.youtrack.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues

/**
 * YouTrack 风格的插槽化布局组件
 *
 * 这个组件只负责布局结构，具体内容通过插槽提供
 *
 * @param isSidebarOpen 侧边栏是否打开
 * @param onSidebarToggle 侧边栏切换回调
 * @param slots 插槽实现
 * @param modifier 修饰符
 */
@Composable
fun YouTrackLayout(
    isSidebarOpen: Boolean = true,
    onSidebarToggle: () -> Unit = {},
    slots: YouTrackLayoutSlots,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        // 侧边栏区域
        if (isSidebarOpen) {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                slots.Sidebar(
                    onNavigate = { /* 在插槽中处理 */ },
                    selectedRoute = "", // 在插槽中传入
                    searchQuery = "", // 在插槽中传入
                    onSearchQueryChange = { /* 在插槽中处理 */ }
                )
            }
        }

        // 主要内容区域
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            // 顶部导航栏
            slots.TopBar(
                currentRoute = "", // 在插槽中传入
                currentTitle = "YouTrack Admin", // 在插槽中传入
                onMenuClick = onSidebarToggle,
                onThemeToggle = { /* 在插槽中处理 */ }
            )

            // 内容区域
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                slots.MainContent(PaddingValues(16.dp))
            }
        }
    }
}

