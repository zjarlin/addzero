package com.addzero.kmp.ui.infra.model.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.form.text.AddPasswordField
import com.addzero.kmp.ui.infra.model.user_center.UserCenterViewModel
import com.addzero.kmp.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * 右上角个人中心
 */
@Composable
fun SysUserCenterScreen() {
    val viewModel = koinViewModel<UserCenterViewModel>()
    val loginViewModel = koinViewModel<LoginViewModel>()


    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = {
                expanded = true
                viewModel.loadUserData()
            },
            modifier = Modifier.size(40.dp)
        ) {
            // 使用图标代替网络图像
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "用户头像",
                modifier = Modifier.size(32.dp).clip(CircleShape)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Column(
                modifier = Modifier.width(300.dp).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 用户信息部分
                if (viewModel.isLoading) {
                    // 显示加载状态
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else if (viewModel.error != null) {
                    // 显示错误信息
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = viewModel.error ?: "未知错误",
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(
                            onClick = { viewModel.loadUserData() },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "重试"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("重试")
                        }
                    }
                } else {
                    // 显示用户信息
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // todo 使用图标代替网络图像
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "用户头像",
                            modifier = Modifier.size(48.dp).clip(CircleShape)
                        )
                        Column {
                            Text(
                                text = loginViewModel.currentToken?.nickname
                                    ?: "${loginViewModel.currentToken?.username}暂未设置昵称",
                                style = MaterialTheme.typography.titleMedium
                            )
//                            Text(
//                                text = viewModel.role,
//                                style = MaterialTheme.typography.bodySmall,
//                                color = MaterialTheme.colorScheme.onSurfaceVariant
//                            )
                        }
                    }
                }

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                // 密码显示和重置
                if (viewModel.isResettingPassword) {
                    // 重置密码表单
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                        AddPasswordField(
                            value = viewModel.newPassword,
                            onValueChange = { viewModel.newPassword = it },
                            label = "新密码"
                        )
                        AddPasswordField(
                            value = viewModel.confirmPassword,
                            onValueChange = { viewModel.confirmPassword = it },
                            label = "确认密码"
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { viewModel.cancelPasswordReset() }) {
                                Text("取消")
                            }
                            Button(
                                onClick = { viewModel.confirmPasswordReset() },
                                enabled = !viewModel.isLoading
                            ) {
                                if (viewModel.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text("确认")
                            }
                        }
                    }
                } else {
                    // 显示当前密码
                    AddPasswordField(
                        value = loginViewModel.currentToken?.password ?: "",
                        onValueChange = {},
                        enabled = false
                    )
                    Button(
                        onClick = { viewModel.startPasswordReset() },
                        modifier = Modifier.align(Alignment.End),
                        enabled = !viewModel.isLoading
                    ) {
                        Text("重置密码")
                    }
                }

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                // 登出按钮
                Button(
                    onClick = {
                        loginViewModel.logout()
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "登出"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("登出")
                }
            }
        }
    }
}
