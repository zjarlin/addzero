@file:OptIn(ExperimentalTime::class)

package com.addzero.kmp.component.ext

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

/**
 * 可滚动容器扩展函数
 *
 * @param orientation 滚动方向
 */
fun Modifier.scrollable(
    orientation: Orientation = Orientation.Vertical
): Modifier = composed {
    val scrollState = rememberScrollState()
    when (orientation) {
        Orientation.Horizontal -> this.horizontalScroll(scrollState)
        else -> this.verticalScroll(scrollState)
    }
}


/**
 * 防抖点击
 * @param [timeout]
 * @param [hapticFeedBack]
 * @param [clickAction]
 */
fun Modifier.debouncedClickable(
    timeout: Duration = 500.milliseconds,
    hapticFeedBack: Boolean = false,
    clickAction: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "debouncedClickable"
        properties["timeout"] = timeout
        properties["hapticFeedBack"] = hapticFeedBack
        properties["onClick"] = clickAction
    }
) {
    var lastClickTime by remember { mutableStateOf(0L) }
    val haptic = LocalHapticFeedback.current
    Modifier.semantics(mergeDescendants = true) {
        role = Role.Button
        onClick(action = {
            val currentTime = kotlin.time.Clock.System.now().toEpochMilliseconds()
            if (currentTime - lastClickTime >= timeout.inWholeMilliseconds) {
                lastClickTime = currentTime
                if (hapticFeedBack) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                clickAction()
                true

            } else {
                false

            }

        })

    }

}
