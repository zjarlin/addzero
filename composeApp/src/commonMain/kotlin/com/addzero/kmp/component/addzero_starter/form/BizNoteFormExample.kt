//package com.addzero.kmp.component.addzero_starter.form
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Card
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.addzero.kmp.forms.BizNoteForm
//import com.addzero.kmp.forms.rememberBizNoteFormState
//import com.addzero.kmp.annotation.Route

//
//@Composable
//@Route
//fun BizNoteFormExample() {
//    val formState = rememberBizNoteFormState()
//
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "BizNote 表单示例",
//            style = MaterialTheme.typography.headlineMedium,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // 显示表单
//        BizNoteForm(state = formState)
//
//        // 显示当前表单值
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp)
//        ) {
//            Column(
//                modifier = Modifier.padding(16.dp)
//            ) {
//                Text(
//                    text = "当前表单值:",
//                    style = MaterialTheme.typography.titleMedium
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = formState.value.toString(),
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
//    }
//}