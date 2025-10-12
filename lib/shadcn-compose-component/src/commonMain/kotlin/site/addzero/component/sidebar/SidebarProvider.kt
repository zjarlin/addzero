package site.addzero.component.sidebar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import site.addzero.util.isMobile

val LocalSidebarState = compositionLocalOf<SidebarState> {
    error("未找到 SidebarProvider")
}

/**
 * 侧边栏状态管理提供者
 */
@Composable
fun SidebarProvider(
    defaultOpen: Boolean = false,
    content: @Composable () -> Unit
) {
    val isMobileDevice = isMobile()

    // 侧边栏状态 - 在移动端和桌面端都遵循 defaultOpen 设置
    var isOpen by rememberSaveable {
        mutableStateOf(if (isMobileDevice) false else defaultOpen)
    }

    // 切换到移动端时自动关闭侧边栏，但保留桌面端状态
    LaunchedEffect(isMobileDevice) {
        if (isMobileDevice) {
            isOpen = false
        }
        // 移除桌面端自动打开功能以允许隐藏
    }

    val sidebarState = remember(isOpen, isMobileDevice) {
        SidebarState(
            isOpen = isOpen,
            isMobile = isMobileDevice,
            toggleSidebar = { isOpen = !isOpen },
            closeSidebar = { isOpen = false }
        )
    }

    CompositionLocalProvider(
        LocalSidebarState provides sidebarState
    ) {
        content()
    }
}
