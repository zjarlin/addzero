package site.addzero.admin.modules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import site.addzero.component.Avatar
import site.addzero.component.Button
import site.addzero.component.ButtonSize
import site.addzero.component.ButtonVariant
import site.addzero.component.Card
import site.addzero.component.Input
import site.addzero.themes.colors
import site.addzero.viewmodel.LoginViewModel

@Composable
fun UserProfileSection(
    userName: String = "管理员",
    userEmail: String = "admin@example.com",
    userRole: String = "超级管理员",
    lastLogin: String = "2024-01-15 14:30",
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = koinViewModel(),
    onLogout: () -> Unit = { loginViewModel.logout() },
    onProfileEdit: () -> Unit = {},
    onPasswordChange: () -> Any? = {}
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 用户头像
            Avatar(
                fallbackText = userName.substring(0, 1).uppercase(),
                size = 80.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 用户信息
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.foreground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = userEmail,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colors.mutedForeground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = userRole,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colors.foreground
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "最后登录: $lastLogin",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colors.mutedForeground
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onProfileEdit,
                    modifier = Modifier.weight(1f),
                    variant = ButtonVariant.Outline
                ) {
                    Text("编辑资料")
                }

                Button(
                    onClick = { onPasswordChange() },
                    modifier = Modifier.weight(1f),
                    variant = ButtonVariant.Outline
                ) {
                    Text("修改密码")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                variant = ButtonVariant.Destructive
            ) {
                Text("退出登录")
            }
        }
    }
}

@Composable
fun UserSettingsSection(
    notifications: Boolean = true,
    darkMode: Boolean = false,
    language: String = "中文",
    modifier: Modifier = Modifier,
    onNotificationToggle: (Boolean) -> Unit = {},
    onDarkModeToggle: (Boolean) -> Unit = {},
    onLanguageChange: (String) -> Unit = {}
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "个人设置",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.foreground
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 通知设置
            SettingItem(
                title = "通知设置",
                description = "接收系统通知和消息",
                isToggle = true,
                toggleValue = notifications,
                onToggle = onNotificationToggle
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 深色模式
            SettingItem(
                title = "深色模式",
                description = "启用深色主题界面",
                isToggle = true,
                toggleValue = darkMode,
                onToggle = onDarkModeToggle
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 语言设置
            SettingItem(
                title = "语言设置",
                description = "选择界面显示语言",
                value = language,
                onClick = { /* TODO: 实现语言选择 */ }
            )
        }
    }
}

@Composable
private fun SettingItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    isToggle: Boolean = false,
    toggleValue: Boolean = false,
    onToggle: (Boolean) -> Unit = {},
    value: String = "",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.foreground
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colors.mutedForeground
            )
        }

        if (isToggle) {
            // TODO: 添加 Toggle 组件
            Text(if (toggleValue) "ON" else "OFF", color = MaterialTheme.colors.foreground)
        } else if (value.isNotEmpty()) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colors.mutedForeground
            )
        }
    }
}
