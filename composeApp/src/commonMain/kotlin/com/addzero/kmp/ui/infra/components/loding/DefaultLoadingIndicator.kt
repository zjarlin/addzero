package com.addzero.kmp.ui.infra.components.loding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DefaultLoadingIndicator() {
    Box(modifier = Modifier.Companion.fillMaxWidth(), contentAlignment = Alignment.Companion.Center) {
        CircularProgressIndicator()
    }
}
