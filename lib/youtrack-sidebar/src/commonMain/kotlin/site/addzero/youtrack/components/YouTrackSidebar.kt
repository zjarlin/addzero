package site.addzero.youtrack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * YouTrack 风格的完整侧边栏组件
 *
 * @param navigationMetadataProvider 导航元数据提供者
 * @param selectedRoute 当前选中的路由
 * @param onNavigate 路由导航回调
 * @param searchQuery 搜索查询文本
 * @param onSearchQueryChange 搜索查询变更回调
 * @param onLogout 退出登录回调
 * @param userInfo 用户信息
 * @param appTitle 应用标题
 * @param appSubtitle 应用副标题
 * @param modifier 修饰符
 */
@Composable
fun YouTrackSidebar(
    navigationMetadataProvider: NavigationMetadataProvider,
    selectedRoute: String,
    onNavigate: (String) -> Unit,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    userInfo: UserInfo = UserInfo("用户"),
    appTitle: String = "AddZero",
    appSubtitle: String = "智能管理平台",
    modifier: Modifier = Modifier
) {
    // 构建树形菜单数据
    val menuTree = remember(searchQuery) {
        MenuTreeBuilder.buildFromNavigationMetadata(
            navigationItems = navigationMetadataProvider.getAllNavigationItems(),
            searchQuery = searchQuery
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // 侧边栏头部
        YouTrackSidebarHeader(
            searchQuery = searchQuery,
            onSearchQueryChange = onSearchQueryChange,
            appTitle = appTitle,
            appSubtitle = appSubtitle
        )

        // 菜单内容区域
        Box(modifier = Modifier.weight(1f)) {
            if (menuTree.isNotEmpty()) {
                YouTrackMenuTree(
                    nodes = menuTree,
                    selectedRoute = selectedRoute,
                    onNavigate = onNavigate,
                    searchQuery = searchQuery,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // 无搜索结果时的占位内容
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = "无搜索结果",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "未找到匹配的菜单项",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "尝试其他关键词",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 侧边栏底部
        YouTrackSidebarFooter(
            selectedRoute = selectedRoute,
            onNavigate = onNavigate,
            onLogout = onLogout,
            userInfo = userInfo
        )
    }
}
