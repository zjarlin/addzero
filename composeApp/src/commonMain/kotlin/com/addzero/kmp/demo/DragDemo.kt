package com.addzero.kmp.demo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.addzero.kmp.annotation.Route
import com.mohamedrejeb.compose.dnd.DragAndDropContainer
import com.mohamedrejeb.compose.dnd.drag.DraggableItem
import com.mohamedrejeb.compose.dnd.drop.dropTarget
import com.mohamedrejeb.compose.dnd.rememberDragAndDropState

data class DragItem(val id: Int, val label: String)

@Composable
@Route("组件示例", "拖拽列表")
fun DragListDemo() {
    // 使用 data class 泛型
    var items by remember {
        mutableStateOf(
            listOf(
                DragItem(1, "A"),
                DragItem(2, "B"),
                DragItem(3, "C"),
                DragItem(4, "D"),
                DragItem(5, "E")
            )
        )
    }
    val dndState = rememberDragAndDropState<DragItem>()

    DragAndDropContainer(state = dndState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            items.forEachIndexed { index, item ->
                val dropModifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .dropTarget(
                        state = dndState,
                        key = item.id, // 用唯一id做key
                        onDrop = { dragState ->
                            val from = items.indexOfFirst { it.id == dragState.data.id }
                            val to = index
                            println("from_id=${dragState.data.id}, to_id=${item.id}")
                            if (from != to && from != -1) {
                                val mutable = items.toMutableList()
                                val moved = mutable.removeAt(from)
                                mutable.add(to, moved)
                                items = mutable
                            }
                        }
                    )

                DraggableItem(
                    state = dndState,
                    key = item.id, // 用唯一id做key
                    data = item,
                    modifier = dropModifier
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (dndState.draggedItem?.data?.id == item.id) Color(0xFF90CAF9) else Color.White
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Surface(
                            //
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item.label, style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

