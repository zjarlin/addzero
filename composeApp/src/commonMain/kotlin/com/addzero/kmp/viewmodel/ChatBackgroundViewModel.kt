package com.addzero.kmp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.addzero.kmp.component.background.ChatBackgroundConfig
import com.addzero.kmp.component.background.ChatBackgroundPresets
import com.addzero.kmp.component.background.ChatBackgroundType
import org.koin.android.annotation.KoinViewModel

/**
 * 聊天背景管理ViewModel
 * 参考Avatar组件的管理方式，提供背景配置的状态管理
 */
@KoinViewModel
class ChatBackgroundViewModel : ViewModel() {

    /**
     * 当前背景配置
     */
    var currentBackground by mutableStateOf(ChatBackgroundPresets.LabubuDefault)
        private set

    /**
     * 是否显示背景选择器
     */
    var showBackgroundSelector by mutableStateOf(false)
        private set

    /**
     * 预设背景列表
     */
    val presetBackgrounds = listOf(
        "Labubu默认" to ChatBackgroundPresets.LabubuDefault,
        "粉色梦幻" to ChatBackgroundPresets.PinkDream,
        "蓝色清新" to ChatBackgroundPresets.BlueFresh,
        "薄荷绿" to ChatBackgroundPresets.MintGreen,
        "纯白简约" to ChatBackgroundPresets.PureWhite
    )

    /**
     * 自定义背景图片URL
     */
    var customImageUrl by mutableStateOf("")
        private set

    /**
     * 背景透明度
     */
    var backgroundAlpha by mutableStateOf(1f)
        private set

    /**
     * 是否启用覆盖层
     */
    var enableOverlay by mutableStateOf(false)
        private set

    /**
     * 设置背景配置
     */
    fun setBackground(config: ChatBackgroundConfig) {
        currentBackground = config
    }

    /**
     * 设置预设背景
     */
    fun setPresetBackground(preset: ChatBackgroundConfig) {
        currentBackground = preset
    }

    /**
     * 设置自定义图片背景
     */
    fun setCustomImageBackground(imageUrl: String, alpha: Float = 0.8f, overlay: Boolean = true) {
        customImageUrl = imageUrl
        currentBackground = ChatBackgroundPresets.customImage(imageUrl, alpha, overlay)
    }

    /**
     * 更新背景透明度
     */
    fun updateBackgroundAlpha(alpha: Float) {
        backgroundAlpha = alpha
        currentBackground = currentBackground.copy(alpha = alpha)
    }

    /**
     * 切换覆盖层
     */
    fun toggleOverlay() {
        enableOverlay = !enableOverlay
        currentBackground = currentBackground.copy(overlay = enableOverlay)
    }

    /**
     * 显示背景选择器
     */
    fun showSelector() {
        showBackgroundSelector = true
    }

    /**
     * 隐藏背景选择器
     */
    fun hideSelector() {
        showBackgroundSelector = false
    }

    /**
     * 重置为默认背景
     */
    fun resetToDefault() {
        currentBackground = ChatBackgroundPresets.LabubuDefault
        customImageUrl = ""
        backgroundAlpha = 1f
        enableOverlay = false
    }

    /**
     * 获取当前背景类型的显示名称
     */
    fun getCurrentBackgroundName(): String {
        return when (currentBackground.type) {
            ChatBackgroundType.GRADIENT -> {
                presetBackgrounds.find { it.second == currentBackground }?.first ?: "自定义渐变"
            }

            ChatBackgroundType.IMAGE -> "自定义图片"
            ChatBackgroundType.SOLID_COLOR -> "纯色背景"
            ChatBackgroundType.PATTERN -> "图案背景"
            ChatBackgroundType.ANIMATED_GRADIENT -> "动画渐变"
            ChatBackgroundType.CUSTOM -> "自定义背景"
        }
    }
}
