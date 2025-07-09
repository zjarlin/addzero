package com.addzero.kmp.component.assist_fun

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.collections.iterator

/**
 * 高性能图标推导函数 - 使用预计算映射表
 */
fun getTitleIcon(title: String): ImageVector {
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

// 高性能图标推导映射表 - 预计算避免运行时字符串匹配
val titleIconMap = mapOf(
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
    "排序" to Icons.AutoMirrored.Filled.Sort,
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
    "标签" to Icons.AutoMirrored.Filled.Label,
    "分类" to Icons.Default.Category,
    "任务" to Icons.AutoMirrored.Filled.Assignment,
    "项目" to Icons.Default.Work,
    "帮助" to Icons.Default.Help,
    "关于" to Icons.Default.Info
)
