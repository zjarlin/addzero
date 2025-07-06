package com.addzero.kmp.screens.dept

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.addzero.kmp.component.high_level.AddDualCardLayout
import com.addzero.kmp.compose.icons.IconKeys
import com.addzero.kmp.processor.annotation.Route
import com.addzero.kmp.viewmodel.SysDeptViewModel
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Route(
    "系统管理", "部门管理", routePath = "/system/sysDept", icon = IconKeys.BUSINESS
)
fun SysDeptScreen() {

    val vm = koinViewModel<SysDeptViewModel>()
    RenderDeptForm(vm)

    AddDualCardLayout(
        leftContent = {
            LeftCard(vm)

        },
        rightContent = {
            Column(
                modifier = Modifier.fillMaxHeight().padding(16.dp)
            ) {
                DeptDetailCard(vm)
                Spacer(modifier = Modifier.height(16.dp))
                DeptUserList(vm, modifier = Modifier.weight(1f))
            }

        }
    )


}


// 渲染部门树结构的递归函数

@Composable
private fun DeptDetailCard(vm: SysDeptViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth().height(220.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp)
//            , verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 标题行
            Text(
                text = "部门详情",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

//            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            HorizontalDivider()

            // 部门信息
            val selectedDept = vm.currentDeptVO
            if (selectedDept != null) {
                DeptInfoRow("部门名称", selectedDept.name ?: "未知部门")
                DeptInfoRow("部门编码", selectedDept.id.toString())
                DeptInfoRow("成员数量", "${vm.users.size} 人")
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "请选择部门查看详情",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DeptInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun DeptUserList(vm: SysDeptViewModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 标题行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "部门成员 (${vm.users.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (vm.users.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "暂无部门成员",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vm.users.size) { index ->
                        val user = vm.users[index]
                        ListItem(leadingContent = {
                            if (!user.avatar.isNullOrBlank()) {
                                AsyncImage(
                                    model = user.avatar,
                                    contentDescription = "头像",
                                    modifier = Modifier.size(40.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Box(
                                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Gray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = user.nickname?.take(1) ?: "?",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }, headlineContent = {
                            Text(
                                text = user.nickname ?: "未设置昵称", style = MaterialTheme.typography.bodyLarge
                            )
                        }, supportingContent = {
                            Text(
                                text = "用户名: ${user.username}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        })
                        if (index < vm.users.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }
        }
    }
}
