package com.addzero.kmp.demo.testviewmodel

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.generated.forms.SysWeatherForm
import com.addzero.kmp.generated.forms.SysWeatherFormOriginal
import com.addzero.kmp.generated.forms.rememberSysWeatherFormState


@Composable
@Route
fun djaoisdjo(
    modifier: Modifier = Modifier,

) {
    val rememberSysWeatherFormState = rememberSysWeatherFormState()

    SysWeatherFormOriginal(rememberSysWeatherFormState)
//    SysWeatherForm()
}