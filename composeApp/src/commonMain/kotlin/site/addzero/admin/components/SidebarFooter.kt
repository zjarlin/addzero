package site.addzero.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import site.addzero.component.sidebar.SidebarFooter
import site.addzero.component.sidebar.SidebarMenuButton
import site.addzero.themes.colors
import site.addzero.viewmodel.LoginViewModel

@Composable
fun AdminSidebarFooter(
    selectedRoute: String,
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val loginViewModel = koinViewModel<LoginViewModel>()

    SidebarFooter {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 用户信息区域
            UserProfileSection(
                userName = loginViewModel.currentToken?.nickname ?: loginViewModel.currentToken?.username ?: "用户",
                userEmail = loginViewModel.currentToken?.email ?: "user@example.com",
                onProfileClick = { onNavigate("profile") }
            )

            // 分隔线
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colors.border
            )

            // 功能按钮区域
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // 设置按钮
                SidebarMenuButton(
                    text = "设置",
                    onClick = { onNavigate("settings") },
                    isActive = selectedRoute == "settings"
                )

                // 退出登录按钮
                SidebarMenuButton(
                    text = "退出登录",
                    onClick = onLogout,
                    isActive = false
                )
            }
        }
    }
}

@Composable
private fun UserProfileSection(
    userName: String,
    userEmail: String,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .clickable { onProfileClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 用户头像
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "用户头像",
                tint = MaterialTheme.colors.primaryForeground,
                modifier = Modifier.size(24.dp)
            )
        }

        // 用户信息
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = userName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.foreground,
                maxLines = 1
            )
            Text(
                text = userEmail,
                fontSize = 12.sp,
                color = MaterialTheme.colors.mutedForeground,
                maxLines = 1
            )
        }
    }
}
