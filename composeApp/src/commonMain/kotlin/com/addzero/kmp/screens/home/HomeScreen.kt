package com.addzero.kmp.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.addzero.kmp.compose.icons.IconKeys
import com.addzero.kmp.processor.annotation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Route(title = "主页", routePath = "/home", icon = IconKeys.HOME)
fun HomeScreen() {
    // 模拟数据
    val stats = remember {
        listOf(
            StatItem("用户总数", "152", Icons.Default.Person, Color(0xFF1976D2)),
            StatItem("角色数量", "6", Icons.Default.Group, Color(0xFF388E3C)),
            StatItem("部门数量", "12", Icons.Default.Business, Color(0xFFF57C00)),
            StatItem("今日操作", "36", Icons.Default.History, Color(0xFFD32F2F))
        )
    }

    val recentActivities = remember {
        listOf(
            RecentActivity("用户登录", "admin", "系统", "2023-04-05 14:32:15", ActivityType.LOGIN),
            RecentActivity("新增用户", "admin", "用户管理", "2023-04-05 14:25:30", ActivityType.CREATE),
            RecentActivity("修改角色", "admin", "角色管理", "2023-04-05 13:45:22", ActivityType.UPDATE),
            RecentActivity("删除菜单", "admin", "菜单管理", "2023-04-05 11:22:10", ActivityType.DELETE),
            RecentActivity("导出数据", "admin", "部门管理", "2023-04-05 10:18:05", ActivityType.EXPORT)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "系统概览",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 统计卡片行
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            stats.forEach { stat ->
                StatCard(
                    title = stat.title,
                    value = stat.value,
                    icon = stat.icon,
                    color = stat.color,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 图表区域
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 用户趋势图
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "用户增长趋势",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp)
                    ) {
                        // 简单模拟柱状图
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            BarChartBar(height = 0.4f, label = "周一", color = MaterialTheme.colorScheme.primary)
                            BarChartBar(height = 0.6f, label = "周二", color = MaterialTheme.colorScheme.primary)
                            BarChartBar(height = 0.3f, label = "周三", color = MaterialTheme.colorScheme.primary)
                            BarChartBar(height = 0.7f, label = "周四", color = MaterialTheme.colorScheme.primary)
                            BarChartBar(height = 0.5f, label = "周五", color = MaterialTheme.colorScheme.primary)
                            BarChartBar(height = 0.8f, label = "周六", color = MaterialTheme.colorScheme.primary)
                            BarChartBar(height = 0.9f, label = "周日", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            // 系统使用情况
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "系统使用分布",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // 简单模拟饼图
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            PieChartSegment(
                                value = "42%",
                                label = "用户管理",
                                color = Color(0xFF1976D2)
                            )
                            PieChartSegment(
                                value = "28%",
                                label = "角色管理",
                                color = Color(0xFF388E3C)
                            )
                            PieChartSegment(
                                value = "18%",
                                label = "部门管理",
                                color = Color(0xFFF57C00)
                            )
                            PieChartSegment(
                                value = "12%",
                                label = "其他",
                                color = Color(0xFFD32F2F)
                            )
                        }
                    }
                }
            }
        }

        // 最近活动
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "最近活动",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn {
                    items(recentActivities) { activity ->
                        RecentActivityItem(activity = activity)
                        if (activity != recentActivities.last()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = DividerDefaults.Thickness,
                                color = DividerDefaults.color
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(32.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BarChartBar(
    height: Float,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(24.dp)
                .height((height * 160).dp)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun PieChartSegment(
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(50))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

enum class ActivityType {
    LOGIN, CREATE, UPDATE, DELETE, EXPORT
}

@Composable
private fun RecentActivityItem(activity: RecentActivity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(getActivityColor(activity.type).copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getActivityIcon(activity.type),
                contentDescription = null,
                tint = getActivityColor(activity.type)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = activity.action,
                style = MaterialTheme.typography.titleSmall
            )
            Row {
                Text(
                    text = "${activity.user} • ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = activity.module,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = activity.time,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun getActivityIcon(type: ActivityType): ImageVector {
    return when (type) {
        ActivityType.LOGIN -> Icons.AutoMirrored.Filled.Login
        ActivityType.CREATE -> Icons.Default.Add
        ActivityType.UPDATE -> Icons.Default.Edit
        ActivityType.DELETE -> Icons.Default.Delete
        ActivityType.EXPORT -> Icons.Default.Download
    }
}

private fun getActivityColor(type: ActivityType): Color {
    return when (type) {
        ActivityType.LOGIN -> Color(0xFF1976D2)  // 蓝色
        ActivityType.CREATE -> Color(0xFF388E3C) // 绿色
        ActivityType.UPDATE -> Color(0xFFF57C00) // 橙色
        ActivityType.DELETE -> Color(0xFFD32F2F) // 红色
        ActivityType.EXPORT -> Color(0xFF7B1FA2) // 紫色
    }
}

data class StatItem(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val color: Color
)

data class RecentActivity(
    val action: String,
    val user: String,
    val module: String,
    val time: String,
    val type: ActivityType
)
