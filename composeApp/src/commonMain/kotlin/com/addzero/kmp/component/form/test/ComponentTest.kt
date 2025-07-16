package com.addzero.kmp.component.form.test

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.form.text.*
import com.addzero.kmp.component.form.number.*
import com.addzero.kmp.component.form.date.*
import com.addzero.kmp.component.form.switch.*
import com.addzero.kmp.component.form.selector.*
import kotlinx.datetime.LocalDateTime

/**
 * 测试所有新创建的表单组件
 */
@Composable
fun ComponentTestScreen() {
    var phoneValue by remember { mutableStateOf("") }
    var usernameValue by remember { mutableStateOf("") }
    var urlValue by remember { mutableStateOf("") }
    var idCardValue by remember { mutableStateOf("") }
    var bankCardValue by remember { mutableStateOf("") }
    var numberValue by remember { mutableStateOf("") }
    var dateValue by remember { mutableStateOf<LocalDateTime?>(null) }
    var switchValue by remember { mutableStateOf(false) }
    var enumValue by remember { mutableStateOf<TestEnum?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "表单组件测试",
            style = MaterialTheme.typography.headlineMedium
        )

        // 测试 AddPhoneField
        AddPhoneField(
            value = phoneValue,
            onValueChange = { phoneValue = it },
            label = "手机号",
            isRequired = true
        )

        // 测试 AddUsernameField
        AddUsernameField(
            value = usernameValue,
            onValueChange = { usernameValue = it },
            label = "用户名",
            isRequired = true
        )

        // 测试 AddUrlField
        AddUrlField(
            value = urlValue,
            onValueChange = { urlValue = it },
            label = "网址"
        )

        // 测试 AddIdCardField
        AddIdCardField(
            value = idCardValue,
            onValueChange = { idCardValue = it },
            label = "身份证号"
        )

        // 测试 AddBankCardField
        AddBankCardField(
            value = bankCardValue,
            onValueChange = { bankCardValue = it },
            label = "银行卡号"
        )

        // 测试 AddNumberField
        AddNumberField(
            value = numberValue,
            onValueChange = { numberValue = it },
            label = "数字",
            decimalPlaces = 2
        )

        // 测试 AddDateTimeField
        AddDateTimeField(
            value = dateValue,
            onValueChange = { dateValue = it },
            label = "日期",
            dateType = DateType.DATE
        )

        // 测试 AddSwitchField
        AddSwitchField(
            value = switchValue,
            onValueChange = { switchValue = it },
            label = "开关"
        )

        // 测试 AddEnumSelector
        AddEnumSelector(
            value = enumValue,
            onValueChange = { enumValue = it },
            label = "枚举选择",
            enumClass = TestEnum::class
        )
    }
}

/**
 * 测试用枚举
 */
enum class TestEnum {
    OPTION1,
    OPTION2,
    OPTION3
}
