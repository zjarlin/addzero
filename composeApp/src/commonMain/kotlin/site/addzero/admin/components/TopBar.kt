package site.addzero.admin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import site.addzero.component.Button
import site.addzero.component.ButtonSize
import site.addzero.component.ButtonVariant
import site.addzero.component.sidebar.SidebarTrigger
import site.addzero.themes.colors

@Composable
fun TopBar(
    title: String = "后台管理系统",
    currentRoute: String = "dashboard",
    onMenuClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左侧：菜单按钮 + 页面标题
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SidebarTrigger()

            Text(
                text = when (currentRoute) {
                    "dashboard" -> "仪表盘"
                    "users" -> "用户管理"
                    "permissions" -> "权限管理"
                    "settings" -> "系统设置"
                    "logs" -> "日志管理"
                    "monitoring" -> "数据监控"
                    "profile" -> "用户中心"
                    else -> title
                },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colors.foreground
            )
        }

        // 右侧：通知 + 用户头像
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onNotificationClick,
                size = ButtonSize.Icon,
                variant = ButtonVariant.Ghost
            ) {
                Text("🔔", color = MaterialTheme.colors.foreground)
            }

            Button(
                onClick = onProfileClick,
                size = ButtonSize.Icon,
                variant = ButtonVariant.Ghost
            ) {
                Text("👤", color = MaterialTheme.colors.foreground)
            }
        }
    }
}
