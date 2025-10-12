package site.addzero.themes

import androidx.compose.ui.graphics.Color

/**
 * Shadcn 主题颜色接口，定义了组件系统中使用的所有颜色常量
 */
interface ShadcnColors {
    /** 应用背景色 */
    val background: Color
    /** 前景色，通常用于文本和图标 */
    val foreground: Color
    /** 卡片背景色 */
    val card: Color
    /** 卡片前景色，用于卡片上的文本和图标 */
    val cardForeground: Color
    /** 弹出框背景色 */
    val popover: Color
    /** 弹出框前景色，用于弹出框上的文本和图标 */
    val popoverForeground: Color
    /** 主要按钮和重要元素的背景色 */
    val primary: Color
    /** 主要按钮和重要元素的前景色 */
    val primaryForeground: Color
    /** 次要按钮和元素的背景色 */
    val secondary: Color
    /** 次要按钮和元素的前景色 */
    val secondaryForeground: Color
    /** 静音/禁用状态元素的背景色 */
    val muted: Color
    /** 静音/禁用状态元素的前景色 */
    val mutedForeground: Color
    /** 强调/突出显示元素的背景色 */
    val accent: Color
    /** 强调/突出显示元素的前景色 */
    val accentForeground: Color
    /** 警告/危险操作的背景色 */
    val destructive: Color
    /** 警告/危险操作的前景色 */
    val destructiveForeground: Color
    /** 边框颜色 */
    val border: Color
    /** 输入框边框颜色 */
    val input: Color
    /** 焦点环颜色，用于表示元素获得焦点 */
    val ring: Color
    /** 图表颜色1 */
    val chart1: Color
    /** 图表颜色2 */
    val chart2: Color
    /** 图表颜色3 */
    val chart3: Color
    /** 图表颜色4 */
    val chart4: Color
    /** 图表颜色5 */
    val chart5: Color
    /** 侧边栏背景色 */
    val sidebar: Color
    /** 侧边栏前景色 */
    val sidebarForeground: Color
    /** 侧边栏主要元素颜色 */
    val sidebarPrimary: Color
    /** 侧边栏主要元素前景色 */
    val sidebarPrimaryForeground: Color
    /** 侧边栏强调元素背景色 */
    val sidebarAccent: Color
    /** 侧边栏强调元素前景色 */
    val sidebarAccentForeground: Color
    /** 侧边栏边框颜色 */
    val sidebarBorder: Color
    /** 侧边栏焦点环颜色 */
    val sidebarRing: Color
    /** 消息提示条背景色 */
    val snackbar: Color
}