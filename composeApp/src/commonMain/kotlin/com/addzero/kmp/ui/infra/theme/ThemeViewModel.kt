package com.addzero.kmp.ui.infra.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * 主题视图可以定义单例的
 * @author zjarlin
 * @date 2025/04/11
 */
object ThemeViewModel {
    // 当前主题类型，默认为淡蓝色浅色主题
    var currentTheme by mutableStateOf(AppThemeType.LIGHT_BLUE)
        private set

    // 是否为暗色主题 - 保留此属性以保持兼容性
    val isDarkMode: Boolean
        get() = currentTheme.isDark()

    // 切换明暗主题 - 保留此方法以保持兼容性
    fun toggleTheme() {
        currentTheme = if (isDarkMode) {
            // 如果当前是暗色主题，切换为对应的亮色主题
            when (currentTheme) {
                AppThemeType.DARK_DEFAULT -> AppThemeType.LIGHT_DEFAULT
                AppThemeType.DARK_BLUE -> AppThemeType.LIGHT_BLUE
                AppThemeType.DARK_GREEN -> AppThemeType.LIGHT_GREEN
                AppThemeType.DARK_PURPLE -> AppThemeType.LIGHT_PURPLE
                else -> AppThemeType.LIGHT_BLUE
            }
        } else {
            // 如果当前是亮色主题，切换为对应的暗色主题
            when (currentTheme) {
                AppThemeType.LIGHT_DEFAULT -> AppThemeType.DARK_DEFAULT
                AppThemeType.LIGHT_BLUE -> AppThemeType.DARK_BLUE
                AppThemeType.LIGHT_GREEN -> AppThemeType.DARK_GREEN
                AppThemeType.LIGHT_PURPLE -> AppThemeType.DARK_PURPLE
                else -> AppThemeType.DARK_BLUE
            }
        }
    }

    // 设置特定主题
    fun setTheme(themeType: AppThemeType) {
        currentTheme = themeType
    }

    // 获取所有可用主题
    fun getAllThemes(): List<AppThemeType> {
        return AppThemeType.entries
    }
}
