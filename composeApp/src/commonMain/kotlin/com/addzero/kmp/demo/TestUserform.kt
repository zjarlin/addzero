//package com.addzero.kmp.demo
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import com.addzero.kmp.annotation.Route
//import com.addzero.kmp.component.button.AddIconButton
//import com.addzero.kmp.component.form.text.AddTextField
//import com.addzero.kmp.generated.forms.SysUserFormOriginal
//import com.addzero.kmp.generated.forms.rememberSysUserFormState
//import kotlinx.coroutines.launch
//
//
//@Composable
//@Route("组件示例", "测试用户表单")
//fun TestUserForm() {
//    val state = rememberSysUserFormState()
//
//    //由于状态里本身没有这俩字段,单独定义
//    var firstName by mutableStateOf("")
//    var lastName by mutableStateOf("")
//
//    val rememberCoroutineScope = rememberCoroutineScope()
//    val onSubMit: suspend () -> Unit = {
//        println(firstName)
//        println(lastName)
//    }
//
//    SysUserFormOriginal(state) {
//        //隐藏掉username
//
//        username() {
//            AddIconButton("假装是个提交按钮") {
//                rememberCoroutineScope.launch {
//                    onSubMit()
//                }
//
//            }
//        }
//        if (state.value.nickname == "张三") {
//            username(
//                hidden = true
//            )
//
//        }
//
//        password {
//            val state = it
//            Text("我是密码值${state.value.password}")
//        }
//        nickname() {
//            val state = it
//            //这里用输入框示范一下
//            Column {
//                AddTextField(
//                    value = state.value.nickname ?: "",
//                    onValueChange = {
//                        state.value = state.value.copy(nickname = it)
//                        firstName = it.take(1)
//                        lastName = it.takeLast(1)
//                    }
//                )
//                Text("昵称为${state.value.username}")
//                Text("A${firstName}")
//                Text("B${lastName}")
//
//            }
//
//        }
//    }
//
//
//}