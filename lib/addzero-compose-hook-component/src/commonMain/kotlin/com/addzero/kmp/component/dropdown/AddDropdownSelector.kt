package com.addzero.kmp.component.dropdown

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.button.AddIconButton

/**
 * 通用下拉选择器
 *
 * @param title 标题
 * @param options 选项列表
 * @param getLabel 获取选项显示文本
 * @param value 当前选中项（受控模式）
 * @param onValueChange 选项变更回调（受控模式）
 * @param modifier 修饰符
 * @param isError 是否错误
 * @param initialValue 初始选中项（非受控模式）
 */
@Composable
fun <T> AddDropdownSelector(
    title: String = "下拉",
    options: List<T>,
    getLabel: (T) -> String,
    value: T? = null,
    onValueChange: ((T?) -> Unit)? = null,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    initialValue: T? = null
) {
    // 受控 or 非受控
    val isControlled = value != null && onValueChange != null
    val (internalValue, setInternalValue) = remember { mutableStateOf(initialValue) }
    val selected = if (isControlled) value else internalValue
    val setSelected: (T?) -> Unit = {
        if (isControlled) onValueChange?.invoke(it) else setInternalValue(it)
    }

    var expanded by remember { mutableStateOf(false) }

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 200)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(title)
        Spacer(modifier = Modifier.width(4.dp))

        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(
                width = 1.dp, color = if (isError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
            )
        ) {
            if (selected != null) {
                Text(getLabel(selected))
                Spacer(modifier = Modifier.weight(1f))
            } else {
                Text("请选择")
                Spacer(modifier = Modifier.weight(1f))
            }

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "展开下拉菜单",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.rotate(arrowRotation)
            )

            if (selected != null) {
                AddIconButton(
                    text = "清除选择",
                    imageVector = Icons.Filled.Clear,
                ) { setSelected(null) }
            }
        }

        // 下拉菜单
        AddDropDown(
            options = options,
            expanded = expanded,
            getLabel = getLabel,
            onOptionSelected = {
                setSelected(it)
                expanded = false
            },
            onDismissRequest = { expanded = false }
        )
    }
}
