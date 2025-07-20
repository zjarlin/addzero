package com.addzero.kmp.component.tree

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 节点类型枚举
 * 集成了颜色、触发关键字和图标信息
 */
enum class NodeType(
    val keywords: List<String>,
    val getColor: @Composable () -> Color,
    val getIcon: @Composable (isExpanded: Boolean) -> ImageVector
) {
    // 文件系统类型
    FOLDER(
        keywords = listOf("文件夹", "目录", "folder", "directory"),
        getColor = { Color(0xFF1976D2) }, // 蓝色
        getIcon = { isExpanded -> if (isExpanded) Icons.Default.FolderOpen else Icons.Default.Folder }
    ),

    FILE(
        keywords = listOf("文件", "file"),
        getColor = { Color(0xFF607D8B).copy(alpha = 0.8f) }, // 蓝灰色
        getIcon = { Icons.AutoMirrored.Filled.InsertDriveFile }
    ),

    DOCUMENT(
        keywords = listOf("文档", "doc", "docx", "document"),
        getColor = { Color(0xFF1976D2).copy(alpha = 0.8f) }, // 蓝色
        getIcon = { Icons.AutoMirrored.Filled.Article }
    ),

    IMAGE(
        keywords = listOf("图片", "image", "png", "jpg", "jpeg", "gif"),
        getColor = { Color(0xFF4CAF50) }, // 绿色
        getIcon = { Icons.Default.Image }
    ),

    AUDIO(
        keywords = listOf("音频", "audio", "mp3", "wav", "ogg"),
        getColor = { Color(0xFF2196F3) }, // 蓝色
        getIcon = { Icons.Default.AudioFile }
    ),

    VIDEO(
        keywords = listOf("视频", "video", "mp4", "avi", "mov"),
        getColor = { Color(0xFFE91E63) }, // 粉色
        getIcon = { Icons.Default.VideoFile }
    ),

    CODE(
        keywords = listOf("代码", "code", "java", "kotlin", "js", "ts", "py", "cpp", "php"),
        getColor = { Color(0xFF607D8B) }, // 蓝灰色
        getIcon = { Icons.Default.Code }
    ),

    PDF(
        keywords = listOf("pdf"),
        getColor = { Color(0xFFF44336) }, // 红色
        getIcon = { Icons.Default.PictureAsPdf }
    ),

    ARCHIVE(
        keywords = listOf("压缩", "zip", "rar", "7z", "archive"),
        getColor = { Color(0xFF795548) }, // 棕色
        getIcon = { Icons.Default.FolderZip }
    ),

    // 组织结构类型
    PERSON(
        keywords = listOf("人员", "个人", "user", "person"),
        getColor = { Color(0xFF9C27B0) }, // 紫色
        getIcon = { Icons.Default.Person }
    ),

    DEPARTMENT(
        keywords = listOf("部门", "department"),
        getColor = { Color(0xFF1976D2).copy(alpha = 0.8f) }, // 蓝色
        getIcon = { Icons.Default.Business }
    ),

    TEAM(
        keywords = listOf("团队", "team", "测试"),
        getColor = { Color(0xFF9C27B0).copy(alpha = 0.9f) }, // 紫色
        getIcon = { Icons.Default.Group }
    ),

    POSITION(
        keywords = listOf("岗位", "职位", "position"),
        getColor = { Color(0xFFFF5722).copy(alpha = 0.8f) }, // 橙色
        getIcon = { Icons.AutoMirrored.Filled.Assignment }
    ),

    COMPANY(
        keywords = listOf("公司", "总公司", "company"),
        getColor = { Color(0xFF1976D2) }, // 蓝色
        getIcon = { Icons.Default.Domain }
    ),

    ORGANIZATION(
        keywords = listOf("组织", "中心", "organization"),
        getColor = { Color(0xFF1976D2).copy(alpha = 0.9f) }, // 蓝色
        getIcon = { Icons.Default.AccountTree }
    ),

    USER(
        keywords = listOf("用户", "user", "account"),
        getColor = { Color(0xFF9C27B0) }, // 紫色
        getIcon = { Icons.Default.AccountCircle }
    ),

    USER_GROUP(
        keywords = listOf("用户组", "用户群组", "人力", "group"),
        getColor = { Color(0xFF9C27B0).copy(alpha = 0.9f) }, // 紫色
        getIcon = { Icons.Default.People }
    ),

    // 菜单与导航类型
    MENU(
        keywords = listOf("菜单", "menu"),
        getColor = { Color(0xFF1976D2) }, // 蓝色
        getIcon = { Icons.Default.Menu }
    ),

    MENU_GROUP(
        keywords = listOf("菜单组", "menu group"),
        getColor = { Color(0xFF1976D2).copy(alpha = 0.8f) }, // 蓝色
        getIcon = { Icons.AutoMirrored.Filled.ViewList }
    ),

    NAVIGATION(
        keywords = listOf("导航", "nav", "navigation"),
        getColor = { Color(0xFFFF5722) }, // 橙色
        getIcon = { Icons.Default.Navigation }
    ),

    LINK(
        keywords = listOf("链接", "link", "url", "href"),
        getColor = { Color(0xFF2196F3) }, // 蓝色
        getIcon = { Icons.Default.Link }
    ),

    BUTTON(
        keywords = listOf("按钮", "button"),
        getColor = { Color(0xFFFF5722) }, // 橙色
        getIcon = { Icons.Default.SmartButton }
    ),

    // 数据与报表类型
    DATA(
        keywords = listOf("数据", "财务", "data"),
        getColor = { Color(0xFF673AB7) }, // 深紫色
        getIcon = { Icons.Default.Storage }
    ),

    CHART(
        keywords = listOf("图表", "销售", "chart"),
        getColor = { Color(0xFF2196F3) }, // 蓝色
        getIcon = { Icons.Default.BarChart }
    ),

    REPORT(
        keywords = listOf("报表", "report"),
        getColor = { Color(0xFF009688) }, // 青色
        getIcon = { Icons.AutoMirrored.Filled.MenuBook }
    ),

    TABLE(
        keywords = listOf("表格", "table"),
        getColor = { Color(0xFF3F51B5) }, // 靛蓝色
        getIcon = { Icons.Default.TableChart }
    ),

    LIST(
        keywords = listOf("列表", "list"),
        getColor = { Color(0xFF607D8B) }, // 蓝灰色
        getIcon = { Icons.AutoMirrored.Filled.FormatListBulleted }
    ),

    DASHBOARD(
        keywords = listOf("仪表盘", "dashboard", "产品"),
        getColor = { Color(0xFF00BCD4) }, // 青色
        getIcon = { Icons.Default.Dashboard }
    ),

    // 特殊类型
    FAVORITE(
        keywords = listOf("收藏", "favorite", "star"),
        getColor = { Color(0xFFFFEB3B) }, // 黄色
        getIcon = { Icons.Default.Star }
    ),

    SETTING(
        keywords = listOf("设置", "setting", "config", "configuration"),
        getColor = { Color(0xFF607D8B) }, // 蓝灰色
        getIcon = { Icons.Default.Settings }
    ),

    NOTIFICATION(
        keywords = listOf("通知", "notification", "alert", "message"),
        getColor = { Color(0xFFFF9800) }, // 橙色
        getIcon = { Icons.Default.Notifications }
    ),

    WARNING(
        keywords = listOf("警告", "warning", "warn"),
        getColor = { Color(0xFFFF9800) }, // 橙色
        getIcon = { Icons.Default.Warning }
    ),

    ERROR(
        keywords = listOf("错误", "error"),
        getColor = { Color(0xFFF44336) }, // 红色
        getIcon = { Icons.Default.Error }
    ),

    SUCCESS(
        keywords = listOf("成功", "success"),
        getColor = { Color(0xFF4CAF50) }, // 绿色
        getIcon = { Icons.Default.CheckCircle }
    ),

    INFO(
        keywords = listOf("信息", "info", "information"),
        getColor = { Color(0xFF2196F3) }, // 蓝色
        getIcon = { Icons.Default.Info }
    ),

    // 默认类型
    DEFAULT(
        keywords = listOf("默认", "default"),
        getColor = { Color(0xFF1976D2) }, // 蓝色
        getIcon = { isExpanded -> if (isExpanded) Icons.Default.FolderOpen else Icons.Default.Folder }
    );

    companion object {
        /**
         * 根据关键字查找最匹配的节点类型
         * @param text 要匹配的文本
         * @return 匹配的节点类型，如果没有匹配则返回DEFAULT
         */
        fun findByKeyword(text: String): NodeType {
            val lowerText = text.lowercase()

            // 精确匹配
            for (type in entries) {
                if (type.keywords.any { it.equals(lowerText, ignoreCase = true) }) {
                    return type
                }
            }

            // 包含匹配
            for (type in entries) {
                if (type.keywords.any { lowerText.contains(it, ignoreCase = true) }) {
                    return type
                }
            }

            return DEFAULT
        }

        /**
         * 猜测节点类型
         * @param label 节点标签
         * @param hasChildren 是否有子节点
         * @return 猜测的节点类型
         */
        fun guess(label: String, hasChildren: Boolean = false): NodeType {
            // 如果有子节点，优先考虑文件夹或组织结构类型
            if (hasChildren) {
                when {
                    label.contains("公司", ignoreCase = true) -> return COMPANY
                    label.contains("部门", ignoreCase = true) -> return DEPARTMENT
                    label.contains("中心", ignoreCase = true) -> return ORGANIZATION
                    label.contains("团队", ignoreCase = true) -> return TEAM
                    label.contains("菜单", ignoreCase = true) -> return MENU_GROUP
                    else -> return FOLDER
                }
            }

            // 根据关键字匹配
            return findByKeyword(label)
        }

        @Composable
        fun guessColor(label: String, hasChildren: Boolean = false): Color {
            val guess = guess(label, hasChildren)
            return guess.getColor()
        }


        @Composable
        fun guessIcon(label: String, hasChildren: Boolean = false): ImageVector {
            val guess = guess(label, hasChildren)
            return guess.getIcon(hasChildren)
        }

        @Composable
        fun <T> guessIcon(
            getChildren: (T) -> List<T>,
            t: T,
            getLabel: (T) -> String
        ): ImageVector {
            val children = getChildren(t)
            val explanFlag = children.isNotEmpty()
            val guess = NodeType.guess(getLabel(t), explanFlag)
            val icon = guess.getIcon(explanFlag)
            return icon
        }


    }
}