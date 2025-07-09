package com.addzero.kmp.ui.infra.components.loding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.ui.infra.Colors

@Composable
fun DefaultErrorMessage(error: Throwable, onRetry: () -> Unit) {
    SelectionContainer {

        Column(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "加载失败: ${error.message ?: "未知错误"}", color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.Companion.height(16.dp))
            Button(onClick = onRetry) {
                Text("重试")
            }
        }
    }

}
