package com.addzero.kmp.screens.role

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.component.button.AddButton
import com.addzero.kmp.compose.icons.IconKeys


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Route("系统管理", "角色管理", routePath = "/system/role", icon = IconKeys.GROUP)
fun RoleListScreen() {
    var roles by remember { mutableStateOf(listOf<Role>()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf<Role?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "角色管理",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AddButton(
            displayName = "添加角色",
            onClick = { showAddDialog = true },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(roles) { role ->
                RoleListItem(
                    role = role,
                    onEdit = { selectedRole = it },
                    onDelete = { /* TODO: 实现删除功能 */ },
                    onPermissions = { /* TODO: 实现权限分配功能 */ }
                )
            }
        }
    }

    if (showAddDialog) {
        RoleDialog(
            role = null,
            onDismiss = { showAddDialog = false },
            onConfirm = { /* TODO: 实现添加功能 */ }
        )
    }

    selectedRole?.let { role ->
        RoleDialog(
            role = role,
            onDismiss = { selectedRole = null },
            onConfirm = { /* TODO: 实现编辑功能 */ }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoleListItem(
    role: Role,
    onEdit: (Role) -> Unit,
    onDelete: (Role) -> Unit,
    onPermissions: (Role) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = role.name, style = MaterialTheme.typography.titleMedium)
                Text(text = role.description, style = MaterialTheme.typography.bodyMedium)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = { onPermissions(role) }) {
                    Icon(Icons.Default.Security, contentDescription = "权限设置")
                }
                IconButton(onClick = { onEdit(role) }) {
                    Icon(Icons.Default.Edit, contentDescription = "编辑")
                }
                IconButton(onClick = { onDelete(role) }) {
                    Icon(Icons.Default.Delete, contentDescription = "删除")
                }
            }
        }
    }
}

@Composable
private fun RoleDialog(
    role: Role?,
    onDismiss: () -> Unit,
    onConfirm: (Role) -> Unit
) {
    var name by remember { mutableStateOf(role?.name ?: "") }
    var description by remember { mutableStateOf(role?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (role == null) "添加角色" else "编辑角色") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("角色名称") }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("角色描述") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(Role(name, description))
                    onDismiss()
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

data class Role(
    val name: String,
    val description: String
)
