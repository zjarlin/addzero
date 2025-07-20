package com.addzero.kmp.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.button.AddButton
import com.addzero.kmp.component.high_level.AddLazyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen() {
    var users by remember { mutableStateOf(listOf<User>()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "用户管理",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AddButton(
            displayName = "添加用户",
            onClick = { showAddDialog = true },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AddLazyList(users) {
            UserListItem(
                user = it,
                onDetail = {},
                onEdit = { selectedUser = it },
                onDelete = { /* TODO: 实现删除功能 */ }
            )
        }

    }

    if (showAddDialog) {
        UserDialog(
            user = null,
            onDismiss = { showAddDialog = false },
            onConfirm = { /* TODO: 实现添加功能 */ }
        )
    }

    selectedUser?.let { user ->
        UserDialog(
            user = user,
            onDismiss = { selectedUser = null },
            onConfirm = { /* TODO: 实现编辑功能 */ }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserListItem(
    user: User,
    onEdit: (User) -> Unit,
    onDelete: (User) -> Unit,
    onDetail: () -> Unit
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
                Text(text = user.username, style = MaterialTheme.typography.titleMedium)
                Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                IconButton(onClick = { onEdit(user) }) {
                    Icon(Icons.Default.Edit, contentDescription = "编辑")
                }

                IconButton(onClick = { onDelete(user) }) {
                    Icon(Icons.Default.Delete, contentDescription = "删除")
                }
            }
        }
    }
}

@Composable
private fun UserDialog(
    user: User?,
    onDismiss: () -> Unit,
    onConfirm: (User) -> Unit
) {
    var username by remember { mutableStateOf(user?.username ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (user == null) "添加用户" else "编辑用户") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("用户名") }
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("邮箱") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(User(username, email))
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

data class User(
    val username: String,
    val email: String
)
