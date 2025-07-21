package com.addzero.kmp.component.table.clean

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddCleanTableViewModel<T, C>(
 val buttonSlot:@Composable  ()->Unit={}
) : ViewModel() {
    var data by mutableStateOf(emptyList<T>())
    var columns by mutableStateOf(emptyList<C>())
//    class TaleSloat(
//        val title: String,
//        val content: @Composable () -> Unit
//    )
}
