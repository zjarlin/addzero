package com.addzero.kmp.demo

import androidx.compose.runtime.*
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.forms.SysUserForm
import com.addzero.kmp.forms.rememberSysUserFormState

@Composable
@Route
fun dajoisdjoiadi(
) {
    var show by remember { mutableStateOf(false) }
    val rememberSysUserFormState = rememberSysUserFormState()
    SysUserForm(
        state = rememberSysUserFormState,
        visible = true,
        title = "用户表单",
        onClose = { show = false },
        onSubmit = { println(rememberSysUserFormState.value) }
    )

}

