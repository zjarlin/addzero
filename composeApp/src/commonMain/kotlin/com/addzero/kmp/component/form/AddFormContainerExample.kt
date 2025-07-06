//package com.addzero.kmp.component.form
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Email
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Save
//import androidx.compose.material3.Icon
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.addzero.kmp.component.button.AddIconButton
//import com.addzero.kmp.component.high_level.AddFormContainer
//import com.addzero.kmp.processor.annotation.Route
//import kotlinx.coroutines.delay
//
///**
// * 简单的表单容器示例
// * 只使用必需的参数
// */
//@Composable
//@Route
//fun SimpleFormContainerExample() {
//    var showForm by remember { mutableStateOf(false) }
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        AddIconButton("显示美化后的表单", Icons.Default.Save) {
//            showForm = true
//        }
//
//        AddFormContainer(
//            visible = showForm,
//            onClose = { showForm = false },
//            onSubmit = {
//                delay(2000) // Simulate network delay
//                println("表单已提交: Name=$name, Email=$email")
//                showForm = false // Close on success
//            },
//            title = "编辑用户信息"
//        ) {
//            // Form content. Because the lambda is a ColumnScope,
//            // we can place Column children directly here.
//            Text("这是一个可以滚动的表单内容区域。", modifier = Modifier.padding(bottom = 16.dp))
//
//            OutlinedTextField(
//                value = name,
//                onValueChange = { name = it },
//                label = { Text("姓名") },
//                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
//                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
//                singleLine = true
//            )
//
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("邮箱") },
//                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
//                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
//                singleLine = true
//            )
//
//            // Add more fields to demonstrate scrolling
//            (1..15).forEach { index ->
//                 OutlinedTextField(
//                    value = "",
//                    onValueChange = { },
//                    label = { Text("附加字段 $index") },
//                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
//                    singleLine = true,
//                    readOnly = true
//                )
//            }
//        }
//    }
//}