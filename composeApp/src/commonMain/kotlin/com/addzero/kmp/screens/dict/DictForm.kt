//package com.addzero.kmp.screens.dict
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.addzero.kmp.component.drawer.AddDrawer
//import com.addzero.kmp.component.form.AddTextField
//import com.addzero.kmp.api.SysDictVO
//
//@Composable
//fun DictForm(
//    showForm: Boolean, currentDict: SysDictVO?, onClose: () -> Unit, onSubmit: (SysDictVO) -> Unit
//) {
//    var dictName by remember { mutableStateOf(currentDict?.dictName ?: "") }
//    var dictCode by remember { mutableStateOf(currentDict?.dictCode ?: "") }
//    var description by remember { mutableStateOf(currentDict?.description ?: "") }
//
//    // 当表单显示时，更新表单数据
//    LaunchedEffect(showForm, currentDict) {
//        if (showForm) {
//            dictName = currentDict?.dictName ?: ""
//            dictCode = currentDict?.dictCode ?: ""
//            description = currentDict?.description ?: ""
//        }
//    }
//
//    AddDrawer(
//        onSubmit = {
//            val id = currentDict?.id ?: 0
//
//
//            val sysDictVO = SysDictVO(
//                id = id, dictName = dictName, dictCode = dictCode, description = description.takeIf { it.isNotBlank() })
//            onSubmit(sysDictVO)
//
//
//        }, onClose = onClose, title = if (currentDict == null) "新增字典类型" else "编辑字典类型", visible = showForm
//    ) {
//        Column(
//            modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            // 字典名称
//
//
//
//
//
//            AddTextField(
//                value = dictName, onValueChange = { dictName = it }, label = "字典名称"
//            )
//
//            // 字典编码
//            AddTextField(
//                value = dictCode,
//                onValueChange = { dictCode = it },
//                label = "字典编码",
//            )
//
//            // 描述
//            AddTextField(
//                value = description, onValueChange = { description = it }, label = "描述", isRequired = false, modifier = Modifier.height(100.dp), maxLines = 3, ,
//            )
//
//        }
//    }
//}
