package site.addzero.youtrack.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * 用户信息数据类
 */
data class UserInfo(
    val name: String,
    val email: String = "",
    val avatar: String? = null
)

/**
 * YouTrack 风格的侧边栏底部区域
 */
@Composable
fun YouTrackSidebarFooter(
    @Suppress("UNUSED_PARAMETER") selectedRoute: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit,
    userInfo: UserInfo = UserInfo("用户"),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 分隔线
        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 用户信息卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    spotColor = MaterialTheme.colorScheme.primary
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // 用户信息头部
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 用户头像
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                                    )
                                )
                            )
                            .clickable { onNavigate("profile") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "用户头像",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // 用户信息
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userInfo.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = userInfo.email.ifBlank { "user@example.com" },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // 在线状态指示器
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                Color(0xFF4CAF50),
                                CircleShape
                            )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 快捷操作按钮
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickFooterButton(
                        icon = Icons.Default.Home,
                        contentDescription = "主页",
                        onClick = { onNavigate("/home") }
                    )
                    QuickFooterButton(
                        icon = Icons.Default.Settings,
                        contentDescription = "设置",
                        onClick = { onNavigate("settings") }
                    )
                    QuickFooterButton(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "退出登录",
                        isDestructive = true,
                        onClick = onLogout
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 版本信息
        Text(
            text = "YouTrack Sidebar v1.0.0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

/**
 * 快捷操作按钮
 */
@Composable
private fun QuickFooterButton(
    icon: ImageVector,
    contentDescription: String,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isDestructive && isHovered -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
            isDestructive -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
            isHovered -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(150)
    )

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
        )
    }
}