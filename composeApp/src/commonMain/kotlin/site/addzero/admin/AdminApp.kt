package site.addzero.admin

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import site.addzero.admin.components.AdminSidebar
import site.addzero.admin.components.TopBar
import site.addzero.admin.modules.UserProfileSection
import site.addzero.admin.modules.UserSettingsSection
import site.addzero.component.sidebar.SidebarLayout
import site.addzero.component.sidebar.SidebarProvider
import site.addzero.generated.RouteTable
import site.addzero.themes.ShadcnTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import site.addzero.generated.RouteKeys

@Composable
private fun ProfileModule() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(24.dp)
    ) {
        UserProfileSection(
            onProfileEdit = { /* 处理编辑资料 */ },
            onPasswordChange = { /* 处理修改密码 */ }
        )

        UserSettingsSection(
            onNotificationToggle = { /* 处理通知开关 */ },
            onDarkModeToggle = { /* 处理深色模式 */ },
            onLanguageChange = { /* 处理语言切换 */ }
        )
    }
}

@Composable
private fun AdminPlaceholderContent(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 这里可以放置占位内容，等待具体模块实现
        androidx.compose.material3.Text(
            text = "$title - 模块开发中...",
            style = androidx.compose.material3.MaterialTheme.typography.titleLarge
        )
    }
}
