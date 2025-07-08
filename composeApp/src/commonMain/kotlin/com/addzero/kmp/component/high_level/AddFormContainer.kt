package com.addzero.kmp.component.high_level

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.addzero.kmp.component.button.AddLoadingButton
import com.addzero.kmp.ui.infra.theme.AppThemes
import com.addzero.kmp.ui.infra.theme.ThemeViewModel
import kotlinx.coroutines.launch

// 高性能图标推导映射表 - 预计算避免运行时字符串匹配
private val titleIconMap = mapOf(
    "用户" to Icons.Default.Person,
    "角色" to Icons.Default.AccountBox,
    "权限" to Icons.Default.Security,
    "设置" to Icons.Default.Settings,
    "配置" to Icons.Default.Settings,
    "系统" to Icons.Default.Computer,
    "数据" to Icons.Default.Storage,
    "文件" to Icons.Default.Folder,
    "上传" to Icons.Default.Upload,
    "下载" to Icons.Default.Download,
    "导入" to Icons.Default.GetApp,
    "导出" to Icons.Default.Share,
    "编辑" to Icons.Default.Edit,
    "修改" to Icons.Default.Edit,
    "新增" to Icons.Default.Add,
    "添加" to Icons.Default.Add,
    "创建" to Icons.Default.Add,
    "删除" to Icons.Default.Delete,
    "移除" to Icons.Default.Remove,
    "查看" to Icons.Default.Visibility,
    "详情" to Icons.Default.Info,
    "信息" to Icons.Default.Info,
    "消息" to Icons.Default.Message,
    "通知" to Icons.Default.Notifications,
    "邮件" to Icons.Default.Email,
    "搜索" to Icons.Default.Search,
    "筛选" to Icons.Default.FilterList,
    "排序" to Icons.Default.Sort,
    "统计" to Icons.Default.BarChart,
    "报表" to Icons.Default.Assessment,
    "分析" to Icons.Default.Analytics,
    "监控" to Icons.Default.Monitor,
    "日志" to Icons.Default.History,
    "备份" to Icons.Default.Backup,
    "恢复" to Icons.Default.Restore,
    "同步" to Icons.Default.Sync,
    "刷新" to Icons.Default.Refresh,
    "重置" to Icons.Default.RestartAlt,
    "清空" to Icons.Default.Clear,
    "保存" to Icons.Default.Save,
    "提交" to Icons.Default.Send,
    "发布" to Icons.Default.Publish,
    "审核" to Icons.Default.Gavel,
    "标签" to Icons.Default.Label,
    "分类" to Icons.Default.Category,
    "任务" to Icons.Default.Assignment,
    "项目" to Icons.Default.Work,
    "帮助" to Icons.Default.Help,
    "关于" to Icons.Default.Info
)

/**
 * 高性能图标推导函数 - 使用预计算映射表
 */
private fun getTitleIcon(title: String): ImageVector {
    // 直接查找完全匹配
    titleIconMap[title]?.let { return it }
    
    // 查找包含关系
    for ((keyword, icon) in titleIconMap) {
        if (title.contains(keyword)) {
            return icon
        }
    }
    
    // 默认图标
    return Icons.Default.Description
}

/**
 * 微软云母效果表单容器组件
 * 清晰简洁的半透明设计，确保输入框完全可读
 *
 * @param visible 是否显示表单
 * @param onClose 关闭回调
 * @param onSubmit 提交回调
 * @param title 表单标题
 * @param titleIcon 标题图标
 * @param submitText 提交按钮文本
 * @param cancelText 取消按钮文本
 * @param submitIcon 提交按钮图标
 * @param modifier 修饰符
 * @param content 表单内容
 */
@Composable
fun AddFormContainer(
    visible: Boolean,
    onClose: () -> Unit,
    onSubmit: suspend () -> Unit,
    confirmEnabled: Boolean = true,
    title: String = "表单",
    titleIcon: ImageVector? = null,
    submitText: String = "提交",
    cancelText: String = "取消",
    submitIcon: ImageVector = Icons.Default.Save,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    if (!visible) return

    // 使用 Popup 替代 Dialog，避免背景变暗
    Popup(
        onDismissRequest = onClose,
        properties = PopupProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            focusable = true
        )
    ) {
        var isLoading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        // 简化的全屏居中容器，直接点击关闭
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)) // 轻微遮罩提升层次感
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClose() },
            contentAlignment = Alignment.Center
        ) {

            // 高性能图标推导
            val resolvedTitleIcon: ImageVector = remember(title, titleIcon) {
                titleIcon ?: getTitleIcon(title)
            }

            // 精心设计的表单容器 - 固定底色，良好对比度
            Card(
                modifier = modifier
                    .fillMaxWidth(0.85f)
                    .fillMaxHeight(0.8f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { /* 阻止事件冒泡 */ },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 24.dp,
                    pressedElevation = 32.dp
                ),
                colors = CardDefaults.cardColors(
                    // 固定的现代化底色设计 - 不随主题变化
                    containerColor = Color(0xFFFAFAFA), // 温暖的浅灰白色
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFFE0E0E0) // 精致的边框
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 标题栏
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(44.dp))

                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Color(0xFF2196F3).copy(alpha = 0.1f) // 固定的蓝色背景
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = resolvedTitleIcon,
                                    contentDescription = null,
                                    tint = Color(0xFF2196F3), // 固定的蓝色图标
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1976D2) // 固定的深蓝色标题
                                )
                            )
                        }

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Color(0xFFF5F5F5) // 固定的浅灰色背景
                                )
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close dialog",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // 分割线
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 28.dp),
                        color = Color(0xFFE0E0E0) // 固定的浅灰色分割线
                    )

                    // 内容区域
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        content()
                    }

                    // 按钮区域
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 28.dp, end = 28.dp, top = 16.dp, bottom = 24.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onClose,
                            modifier = Modifier
                                .height(52.dp)
                                .widthIn(min = 100.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF1976D2) // 固定的深蓝色按钮文字
                            )
                        ) {
                            Text(
                                cancelText,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        AddLoadingButton(
                            enabled = confirmEnabled,
                            text = submitText,
                            onClick = {
                                if (!isLoading) {
                                    isLoading = true
                                    scope.launch {
                                        try {
                                            onSubmit()
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            },
                            icon = submitIcon,
                            loading = isLoading
                        )
                    }
                }
            }
        } // Card 表单容器闭合括号
    } // Box 居中容器闭合括号
} // Popup 闭合括号
