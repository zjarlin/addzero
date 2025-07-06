package com.addzero.kmp.component.autocomplet

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.addzero.kmp.processor.annotation.Route


@Composable
@Route("组件示例", "自动完成")
fun AutoCompleteDemo() {
    Column {
        (1..3).map { index ->
            val map = listOf<String>(
                "Apple",
                "Banana",
                "Cherry",
                "Date",
                "Elderberry",
                "Fig",
                "Grape",
                "Honeydew",
                "Iceberg Lettuce",
                "Jackfruit"
            ).map { it + index }
            Text("测试自动完成" + index)
            var selectClause by mutableStateOf("")



            AddAutoComplete(
                title = "水果",
                suggestions = map,
//                maxSuggestions = TODO(),
//                getLabelFun = TODO(),
                onItemSelected = { selectClause = it },
//                modifier = TODO(),
//                initialValue = TODO(),
//                initialSelected = TODO()
            )
//            val useAutoComplet = UseAutoComplet("水果", map)
//            useAutoComplet.render {}
//            useAutoComplet.render()

        }


    }


}
