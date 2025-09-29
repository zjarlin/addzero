package site.addzero.ui.infra.theme

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel
import site.addzero.generated.enums.EnumSysTheme

/**
 * 主题视图模型
 * @author zjarlin
 * @date 2025/04/11
 */
@KoinViewModel
class ThemeViewModel : ViewModel() {
    /**
     * 是否为暗色主题
     */
    fun EnumSysTheme.isDark(): Boolean {
        return this == EnumSysTheme.DARK_DEFAULT || this == EnumSysTheme
            .DARK_BLUE || this == EnumSysTheme.DARK_GREEN || this == EnumSysTheme.DARK_PURPLE
    }

    // 当前主题类型，默认为蓝色亮色主题
    var currentTheme by mutableStateOf(EnumSysTheme.LIGHT_BLUE)
    val colorScheme by derivedStateOf {
        AppThemes.getColorScheme(currentTheme)
    }


    val isDarkMode: Boolean
        get() = currentTheme.isDark()

    // 切换明暗主题 - 保留此方法以保持兼容性
    fun toggleTheme() {
        currentTheme = if (isDarkMode) {
            // 如果当前是暗色主题，切换为对应的亮色主题
            when (currentTheme) {
                EnumSysTheme.DARK_DEFAULT -> EnumSysTheme.LIGHT_DEFAULT
                EnumSysTheme.DARK_BLUE -> EnumSysTheme.LIGHT_BLUE
                EnumSysTheme.DARK_GREEN -> EnumSysTheme.LIGHT_GREEN
                EnumSysTheme.DARK_PURPLE -> EnumSysTheme.LIGHT_PURPLE
                else -> EnumSysTheme.LIGHT_BLUE
            }
        } else {
            // 如果当前是亮色主题，切换为对应的暗色主题
            when (currentTheme) {
                EnumSysTheme.LIGHT_DEFAULT -> EnumSysTheme.DARK_DEFAULT
                EnumSysTheme.LIGHT_BLUE -> EnumSysTheme.DARK_BLUE
                EnumSysTheme.LIGHT_GREEN -> EnumSysTheme.DARK_GREEN
                EnumSysTheme.LIGHT_PURPLE -> EnumSysTheme.DARK_PURPLE
                else -> EnumSysTheme.DARK_BLUE
            }
        }
    }

    // 获取所有可用主题
    fun getAllThemes(): List<EnumSysTheme> {
        return EnumSysTheme.entries
    }
}
