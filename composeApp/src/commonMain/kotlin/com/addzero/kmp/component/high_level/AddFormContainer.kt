package com.addzero.kmp.component.high_level

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.addzero.kmp.component.button.AddLoadingButton
import com.addzero.kmp.ui.infra.theme.AppThemes
import com.addzero.kmp.ui.infra.theme.ThemeViewModel
import kotlinx.coroutines.launch

/**
 * 高性能的标题图标推导 - 使用预计算的映射表
 */
private val titleIconMap = mapOf(
    // 操作类
    "添加" to Icons.Default.Add, "新增" to Icons.Default.Add, "创建" to Icons.Default.Add,
    "编辑" to Icons.Default.Edit, "修改" to Icons.Default.Edit, "更新" to Icons.Default.Edit,
    "删除" to Icons.Default.Delete, "移除" to Icons.Default.Delete,

    // 用户相关
    "用户" to Icons.Default.Person, "账户" to Icons.Default.Person,
    "设置" to Icons.Default.Settings, "配置" to Icons.Default.Settings,

    // 搜索和导入导出
    "搜索" to Icons.Default.Search, "查找" to Icons.Default.Search,
    "上传" to Icons.Default.Upload, "导入" to Icons.Default.Upload,
    "下载" to Icons.Default.Download, "导出" to Icons.Default.Download,

    // 通信相关
    "邮件" to Icons.Default.Email, "邮箱" to Icons.Default.Email,
    "密码" to Icons.Default.Lock, "登录" to Icons.Default.Lock,
    "电话" to Icons.Default.Phone, "联系" to Icons.Default.Phone,
    "消息" to Icons.Default.Notifications, "通知" to Icons.Default.Notifications,

    // 文件相关
    "文件" to Icons.Default.Description, "文档" to Icons.Default.Description,
    "图片" to Icons.Default.Image, "图像" to Icons.Default.Image,
    "视频" to Icons.Default.VideoLibrary,
    "音频" to Icons.Default.AudioFile, "音乐" to Icons.Default.AudioFile,
    "文件夹" to Icons.Default.Folder,

    // 时间和位置
    "日期" to Icons.Default.DateRange, "时间" to Icons.Default.DateRange,
    "位置" to Icons.Default.LocationOn, "地址" to Icons.Default.LocationOn,

    // 状态和操作
    "收藏" to Icons.Default.Favorite, "喜欢" to Icons.Default.Favorite,
    "分享" to Icons.Default.Share,
    "帮助" to Icons.Default.Help, "支持" to Icons.Default.Help,
    "信息" to Icons.Default.Info, "详情" to Icons.Default.Info,
    "警告" to Icons.Default.Warning,
    "错误" to Icons.Default.Error,
    "成功" to Icons.Default.CheckCircle, "完成" to Icons.Default.CheckCircle,
    "标签" to Icons.Default.Label,
    "链接" to Icons.Default.Link,
    "打印" to Icons.Default.Print,
    "刷新" to Icons.Default.Refresh,
    "复制" to Icons.Default.ContentCopy,
    "粘贴" to Icons.Default.ContentPaste,
    "剪切" to Icons.Default.ContentCut,
    "保存" to Icons.Default.Save,
    "表单" to Icons.Default.Assignment
)

/**
 * 高性能的标题图标推导函数
 */
private fun getTitleIcon(title: String): ImageVector {
    // 首先尝试精确匹配
    titleIconMap[title]?.let { return it }

    // 然后尝试包含匹配（转换为小写进行比较）
    val lowerTitle = title.lowercase()
    for ((keyword, icon) in titleIconMap) {
        if (lowerTitle.contains(keyword.lowercase())) {
            return icon
        }
    }

    // 默认图标
    return Icons.Default.Edit
}

/**
 * 美观大方的表单容器组件
 * 以 Dialog 形式置顶显示
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
    titleIcon: ImageVector? = null, // 如果为null，将自动根据title推导
    submitText: String = "提交",
    cancelText: String = "取消",
    submitIcon: ImageVector = Icons.Default.Save,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit

) {
    if (!visible) return

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false, // Allow custom width
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        var isLoading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val currentTheme = ThemeViewModel.currentTheme
        val gradientConfig = AppThemes.getGradientConfig(currentTheme)

        // 使用计算属性优化图标推导性能 - 只有在title或titleIcon变化时才重新计算
        val resolvedTitleIcon by remember(title, titleIcon) {
            derivedStateOf {
                titleIcon ?: getTitleIcon(title)
            }
        }

        Card(
            modifier = modifier
                .fillMaxWidth(0.8f) // 缩小到80%宽度，提升易用性
                .fillMaxHeight(0.75f), // 缩小到75%高度
            shape = RoundedCornerShape(20.dp), // 稍微减小圆角
            elevation = CardDefaults.cardElevation(12.dp), // 增加阴影深度
            colors = CardDefaults.cardColors(
                containerColor = if (currentTheme.isGradient()) {
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
        ) {
            // 添加渐变背景层
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // 渐变背景（仅在渐变主题时显示）
                if (currentTheme.isGradient() && gradientConfig != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        gradientConfig.colors.first().copy(alpha = 0.08f),
                                        gradientConfig.colors[1].copy(alpha = 0.05f),
                                        gradientConfig.colors.getOrNull(2)?.copy(alpha = 0.03f) ?: Color.Transparent,
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header with Title and Close button - 美化头部，标题居中显示
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 左侧占位，保持居中平衡
                        Spacer(modifier = Modifier.width(44.dp))

                        // 居中的标题区域（包含图标和文本）
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // 标题图标（自动推导）
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (currentTheme.isGradient() && gradientConfig != null) {
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    gradientConfig.colors.first().copy(alpha = 0.15f),
                                                    gradientConfig.colors[1].copy(alpha = 0.1f)
                                                )
                                            )
                                        } else {
                                            Brush.linearGradient(
                                                colors = listOf(
                                                    MaterialTheme.colorScheme.primaryContainer,
                                                    MaterialTheme.colorScheme.primaryContainer
                                                )
                                            )
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = resolvedTitleIcon,
                                    contentDescription = null,
                                    tint = if (currentTheme.isGradient() && gradientConfig != null) {
                                        gradientConfig.colors.first()
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    },
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // 标题文本
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (currentTheme.isGradient() && gradientConfig != null) {
                                        gradientConfig.colors.first()
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    }
                                )
                            )
                        }

                        // 美化关闭按钮
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
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

                    // 美化分割线
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 28.dp),
                        color = if (currentTheme.isGradient() && gradientConfig != null) {
                            gradientConfig.colors.first().copy(alpha = 0.2f)
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        }
                    )

                    // Content area - 优化内容区域间距
                    Column(
                        modifier = Modifier
                            .weight(1f) // Occupy remaining space
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp) // 统一间距
                    ) {
                        content()
                    }

                    // Footer with buttons - 美化按钮区域
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 28.dp, end = 28.dp, top = 16.dp, bottom = 24.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 美化取消按钮
                        OutlinedButton(
                            onClick = onClose,
                            modifier = Modifier
                                .height(52.dp)
                                .widthIn(min = 100.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = if (currentTheme.isGradient() && gradientConfig != null) {
                                ButtonDefaults.outlinedButtonColors(
                                    contentColor = gradientConfig.colors.first()
                                )
                            } else {
                                ButtonDefaults.outlinedButtonColors()
                            }
                        ) {
                            Text(
                                cancelText,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        // 美化提交按钮
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
        }
    }
}

// 假设 AddLoadingButton 已经存在
