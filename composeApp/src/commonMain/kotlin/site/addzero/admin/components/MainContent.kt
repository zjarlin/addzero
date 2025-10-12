package site.addzero.admin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import site.addzero.component.Card
import site.addzero.component.Progress
import site.addzero.themes.colors

@Composable
fun DashboardContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 欢迎卡片
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "欢迎使用后台管理系统",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colors.foreground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "请从左侧菜单选择功能模块",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colors.mutedForeground
                )
            }
        }

        // 统计卡片
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatsCard(
                title = "总用户数",
                value = "1,234",
                modifier = Modifier.weight(1f)
            )
            StatsCard(
                title = "今日访问",
                value = "567",
                modifier = Modifier.weight(1f)
            )
        }

        // 系统状态
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "系统状态",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colors.foreground
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Text(
                        text = "CPU 使用率",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colors.mutedForeground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Progress(
                        progress = 0.75f
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Text(
                        text = "内存使用率",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colors.mutedForeground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Progress(
                        progress = 0.45f
                    )
                }
            }
        }
    }
}

@Composable
fun UsersContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "用户管理模块",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colors.foreground
        )
    }
}

@Composable
fun SettingsContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "系统设置模块",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colors.foreground
        )
    }
}

@Composable
fun ProfileContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "用户中心模块",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colors.foreground
        )
    }
}

@Composable
private fun StatsCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colors.mutedForeground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colors.foreground
            )
        }
    }
}
