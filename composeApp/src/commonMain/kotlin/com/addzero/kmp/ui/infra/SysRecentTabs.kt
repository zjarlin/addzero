package com.addzero.kmp.ui.infra

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.addzero.kmp.ui.infra.model.navigation.RecentTabsManager
import com.addzero.kmp.ui.infra.model.navigation.TabInfo

/**
 * 最近访问标签页组件
 * 
 * 显示用户最近访问的页面，以标签页形式展示，支持切换和关闭
 * 支持键盘快捷键:
 * - Cmd+W: 关闭当前标签页
 * - Cmd+Shift+T: 恢复最近关闭的标签页
 * 
 * @param navController 导航控制器
 * @param modifier 修饰符
 * @param listenShortcuts 是否监听快捷键事件，默认为true
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddRecentTabs(
    navController: NavController,
    modifier: Modifier = Modifier,
    listenShortcuts: Boolean = true,
    recentViewModel: RecentTabsManager
) {
    val tabs = recentViewModel.tabs
    val currentTabIndex = recentViewModel.currentTabIndex

    if (tabs.isEmpty()) {
        return
    }

    // 准备键盘事件修饰符
    var boxModifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)) // 使用带有轻微海拔的表面颜色，增加层次感

    // 只有当listenShortcuts为true时，才添加键盘事件监听
    if (listenShortcuts) {
        boxModifier = boxModifier.onPreviewKeyEvent { keyEvent ->
            when {
                // Cmd+W: 关闭当前标签页
                (keyEvent.key == Key.W && keyEvent.isMetaPressed && 
                        keyEvent.type == KeyEventType.KeyDown) -> {
                    if (currentTabIndex >= 0 && currentTabIndex < tabs.size) {
                        recentViewModel.closeTab(currentTabIndex, navController)
                        true
                    } else {
                        false
                    }
                }
                // Cmd+Shift+T: 恢复最近关闭的标签页
                (keyEvent.key == Key.T && keyEvent.isMetaPressed && 
                        keyEvent.isShiftPressed && keyEvent.type == KeyEventType.KeyDown) -> {
                    recentViewModel.reopenLastClosedTab(navController)
                    true
                }
                else -> false
            }
        }
    }

    // 使用准备好的修饰符
    Box(modifier = boxModifier) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp) // 调整内边距，使其更宽松
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp) // 进一步增大标签间距，提升呼吸感
        ) {
            tabs.forEachIndexed { index, tab ->
                TabItem(
                    tab = tab,
                    isActive = index == currentTabIndex,
                    onActivate = { recentViewModel.activateTab(index, navController) },
                    onClose = { recentViewModel.closeTab(index, navController) }
                )
            }
        }
    }
}

/**
 * 单个标签页项目
 * 
 * @param tab 标签页信息
 * @param isActive 是否是当前激活的标签页
 * @param onActivate 激活标签页的回调
 * @param onClose 关闭标签页的回调
 */
@Composable
private fun TabItem(
    tab: TabInfo,
    isActive: Boolean,
    onActivate: () -> Unit,
    onClose: () -> Unit
) {
    val backgroundColor = if (isActive)
        MaterialTheme.colorScheme.secondaryContainer // 激活标签使用更柔和的强调色
    else
        MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp) // 未激活标签使用略微抬高的表面色

    val contentColor = if (isActive)
        MaterialTheme.colorScheme.onSecondaryContainer // 激活标签文本颜色
    else
        MaterialTheme.colorScheme.onSurfaceVariant // 未激活标签文本颜色，更柔和

    val shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp) // 调整圆角，使其更像标签
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current, // 可以考虑自定义 Indication 效果
                onClick = onActivate
            )
            .height(40.dp), // 略微增加标签高度
        color = backgroundColor,
        tonalElevation = if (isActive) 2.dp else 1.dp, // 调整阴影效果，使其更细腻
        shape = shape,
        border = if (isActive) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)) else null // 激活状态添加细边框
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), // 调整内部元素间距
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp) // 文本和关闭按钮之间的间距
        ) {
            Text(
                text = tab.title,
                color = contentColor,
                style = MaterialTheme.typography.labelLarge, // 使用更适合标签的字体样式
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 仅当标签被激活时显示关闭按钮，或者鼠标悬停时（Compose Web/Desktop）
            // 为了简化，这里我们仅在激活时显示
            // if (isActive) { // 暂时移除此条件，保持关闭按钮一直可见，后续可根据交互需求调整
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(24.dp) // 稍微增大关闭按钮的点击区域
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "关闭标签页",
                    tint = contentColor,
                    modifier = Modifier.size(18.dp) // 稍微增大图标尺寸
                )
                // }
            }
        }
    }
}

