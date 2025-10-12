package site.addzero.youtrack.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

/**
 * YouTrack 布局插槽接口
 */
interface YouTrackLayoutSlots {

    /**
     * 侧边栏插槽
     */
    @Composable
    fun Sidebar(
        onNavigate: (String) -> Unit,
        selectedRoute: String,
        searchQuery: String,
        onSearchQueryChange: (String) -> Unit
    )

    /**
     * 顶部导航栏插槽
     */
    @Composable
    fun TopBar(
        currentRoute: String,
        currentTitle: String,
        onMenuClick: () -> Unit,
        onThemeToggle: () -> Unit
    )

    /**
     * 侧边栏头部插槽
     */
    @Composable
    fun SidebarHeader(
        searchQuery: String,
        onSearchQueryChange: (String) -> Unit
    )

    /**
     * 侧边栏内容插槽
     */
    @Composable
    fun SidebarContent(
        onNavigate: (String) -> Unit,
        selectedRoute: String,
        searchQuery: String
    )

    /**
     * 侧边栏底部插槽
     */
    @Composable
    fun SidebarFooter(
        selectedRoute: String,
        onNavigate: (String) -> Unit
    )

    /**
     * 主要内容插槽
     */
    @Composable
    fun MainContent(
        paddingValues: PaddingValues
    )
}

/**
 * 默认插槽实现
 */
object DefaultYouTrackLayoutSlots : YouTrackLayoutSlots {

    @Composable
    override fun Sidebar(
        onNavigate: (String) -> Unit,
        selectedRoute: String,
        searchQuery: String,
        onSearchQueryChange: (String) -> Unit
    ) {
        // 默认不提供侧边栏，需要用户自定义
    }

    @Composable
    override fun TopBar(
        currentRoute: String,
        currentTitle: String,
        onMenuClick: () -> Unit,
        onThemeToggle: () -> Unit
    ) {
        // 默认不提供顶部栏，需要用户自定义
    }

    @Composable
    override fun SidebarHeader(
        searchQuery: String,
        onSearchQueryChange: (String) -> Unit
    ) {
        // 默认不提供头部，需要用户自定义
    }

    @Composable
    override fun SidebarContent(
        onNavigate: (String) -> Unit,
        selectedRoute: String,
        searchQuery: String
    ) {
        // 默认不提供内容，需要用户自定义
    }

    @Composable
    override fun SidebarFooter(
        selectedRoute: String,
        onNavigate: (String) -> Unit
    ) {
        // 默认不提供底部，需要用户自定义
    }

    @Composable
    override fun MainContent(
        paddingValues: PaddingValues
    ) {
        // 默认不提供内容，需要用户自定义
    }
}