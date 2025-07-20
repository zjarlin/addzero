package com.addzero.kmp.ui.infra.responsive

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 🚀 响应式布局工具类
 *
 * 提供屏幕尺寸检测和响应式布局支持
 */

/**
 * 屏幕尺寸类型枚举
 */
enum class ScreenSize {
    MOBILE,    // 移动端：< 768dp
    TABLET,    // 平板：768dp - 1024dp
    DESKTOP    // 桌面端：> 1024dp
}

/**
 * 布局模式枚举
 */
enum class LayoutMode {
    SIDEBAR,   // 侧边栏模式（桌面端）
    TOPBAR     // 顶部导航栏模式（移动端）
}

/**
 * 响应式布局配置
 */
data class ResponsiveConfig(
    val screenSize: ScreenSize,
    val layoutMode: LayoutMode,
    val sidebarWidth: Dp,
    val compactSidebarWidth: Dp,
    val topbarHeight: Dp,
    val showSidebar: Boolean,
    val showTopNavigation: Boolean
)

/**
 * 🔍 检测当前屏幕尺寸
 */
@Composable
fun rememberScreenSize(): ScreenSize {
    val density = LocalDensity.current
    var screenSize by remember { mutableStateOf(ScreenSize.DESKTOP) }

    // 这里需要根据实际的窗口尺寸来判断
    // 由于 Compose Multiplatform 的限制，我们使用一个简化的检测方法
    LaunchedEffect(density) {
        // 在实际项目中，这里应该监听窗口尺寸变化
        // 目前先使用默认值，后续可以通过平台特定的实现来获取真实的屏幕尺寸
        screenSize = ScreenSize.DESKTOP
    }

    return screenSize
}

/**
 * 🎯 获取响应式布局配置
 */
@Composable
fun rememberResponsiveConfig(
    forceLayoutMode: LayoutMode? = null
): ResponsiveConfig {
    val screenSize = rememberScreenSize()

    return remember(screenSize, forceLayoutMode) {
        val layoutMode = forceLayoutMode ?: when (screenSize) {
            ScreenSize.MOBILE -> LayoutMode.TOPBAR
            ScreenSize.TABLET -> LayoutMode.TOPBAR
            ScreenSize.DESKTOP -> LayoutMode.SIDEBAR
        }

        ResponsiveConfig(
            screenSize = screenSize,
            layoutMode = layoutMode,
            sidebarWidth = when (screenSize) {
                ScreenSize.MOBILE -> 280.dp
                ScreenSize.TABLET -> 320.dp
                ScreenSize.DESKTOP -> 240.dp
            },
            compactSidebarWidth = 56.dp,
            topbarHeight = when (screenSize) {
                ScreenSize.MOBILE -> 56.dp
                ScreenSize.TABLET -> 64.dp
                ScreenSize.DESKTOP -> 72.dp
            },
            showSidebar = layoutMode == LayoutMode.SIDEBAR,
            showTopNavigation = layoutMode == LayoutMode.TOPBAR
        )
    }
}

/**
 * 🎨 响应式间距
 */
@Composable
fun responsivePadding(
    mobile: Dp = 8.dp,
    tablet: Dp = 12.dp,
    desktop: Dp = 16.dp
): Dp {
    val screenSize = rememberScreenSize()
    return when (screenSize) {
        ScreenSize.MOBILE -> mobile
        ScreenSize.TABLET -> tablet
        ScreenSize.DESKTOP -> desktop
    }
}

/**
 * 🎨 响应式字体大小缩放
 */
@Composable
fun responsiveTextScale(): Float {
    val screenSize = rememberScreenSize()
    return when (screenSize) {
        ScreenSize.MOBILE -> 0.9f
        ScreenSize.TABLET -> 1.0f
        ScreenSize.DESKTOP -> 1.0f
    }
}

/**
 * 🔧 响应式列数
 */
@Composable
fun responsiveColumns(
    mobile: Int = 1,
    tablet: Int = 2,
    desktop: Int = 3
): Int {
    val screenSize = rememberScreenSize()
    return when (screenSize) {
        ScreenSize.MOBILE -> mobile
        ScreenSize.TABLET -> tablet
        ScreenSize.DESKTOP -> desktop
    }
}
