package com.addzero.kmp.demo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.generated.forms.SysUserFormOriginal
import com.addzero.kmp.generated.forms.rememberSysUserFormState


@Composable
@Route("组件示例", "测试用户表单")
fun TestUserForm() {
    val state = rememberSysUserFormState()
    SysUserFormOriginal(state) {
        //隐藏掉username

        username()

        password {
            val state = it
            Text("我是密码值${state.value.password}")
        }
        nickname() {
            val state = it
            Text("我是有自定义逻辑的属性(显示username的值, 昵称为${state.value.username}")

        }
    }

}