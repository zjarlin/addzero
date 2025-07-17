package com.addzero.kmp.demo

import androidx.compose.runtime.Composable
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.generated.forms.SysUserFormOriginal
import com.addzero.kmp.generated.forms.rememberSysUserFormState


@Composable
@Route
fun TestUserForm( ) {
    SysUserFormOriginal(rememberSysUserFormState())

}