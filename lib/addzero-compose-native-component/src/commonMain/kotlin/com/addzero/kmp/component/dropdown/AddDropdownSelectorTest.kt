package com.addzero.kmp.component.dropdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.generated.enums.EnumSysUserStatus


@Composable
@Route("组件示例", "测试性别下拉选择")
fun AddDropDownSelectorTest(
    modifier: Modifier = Modifier
) {

    AddDropdownSelector(
        options = EnumSysUserStatus.entries,
        getLabel = { it.desc },
        onValueChange = { println(it?.code) }
    )
}

