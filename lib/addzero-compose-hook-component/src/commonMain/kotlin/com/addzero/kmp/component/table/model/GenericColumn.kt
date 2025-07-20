package com.addzero.kmp.component.table.model

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.addzero.kmp.component.form.DynamicFormItem
import com.addzero.kmp.component.table.TableColumnType
import com.addzero.kmp.component.table.TableRowType
import com.addzero.kmp.entity.low_table.EnumColumnAlignment

data class AddColumn(
    override val metaconfig: SysColumnMetaConfig = SysColumnMetaConfig(),
    override val key: String,
    override val title: String,
) : TableColumnType {

    override val get: (TableRowType) -> Any?
        get() = { it[key] }
    override val getFun: (TableRowType?, String) -> Any?
        get() = { rowe, mykey -> rowe?.get(mykey) }
    override val set: (TableRowType?, Any?) -> Unit
        get() = { row, value -> row?.put(key, value) }
    override val setFun: (TableRowType?, String, Any?) -> Unit
        get() = { row, mykey, value -> row?.put(mykey, value) }

    override var customFormRender: @Composable ((TableRowType?) -> Unit) = { item ->
        DynamicFormItem(
            value = getFun(item, key),
            onValueChange = { newValue ->
                // 确保item不为null时才设置值
                if (item != null) {
                    setFun(item, key, newValue)
                }
            },
            title = title,
            kmpType = metaconfig.kmpType
        )
    }

}

interface GenericColumn {
    val metaconfig: SysColumnMetaConfig
    val key: String
    val title: String
    val get: (TableRowType) -> Any?
    val getFun: (TableRowType?, String) -> Any?

    val set: (TableRowType?, Any?) -> Unit
    val setFun: (TableRowType?, String, Any?) -> Unit
    val customCellRender: @Composable ((TableRowType) -> Unit)
        get() = {

            val textAlign = when (metaconfig.alignment) {
                EnumColumnAlignment.LEFT -> TextAlign.Start
                EnumColumnAlignment.CENTER -> TextAlign.Center
                EnumColumnAlignment.RIGHT -> TextAlign.End
            }
            Text(
                text = getFun(it, key).toString(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = textAlign
            )

        }
    val customFormRender: @Composable ((TableRowType?) -> Unit)
        get() = { item ->
            DynamicFormItem(
                value = getFun(item, key),
                onValueChange = { newValue ->
                    // 确保item不为null时才设置值
                    if (item != null) {
                        this.setFun(item, key, newValue)
                    }
                },
                title = this.title,
                kmpType = this.metaconfig.kmpType
            )
        }

//    val widthRatio: Float = 1f,
//    val alignment: EnumColumnAlignment = EnumColumnAlignment.CENTER,
    //低代码元数据配置

    // 是否可排序
//    val sortable: Boolean = true,

    // 是否可搜索
//    val searchable: Boolean = true,

    // 是否在表单中显示
//    val showInForm: Boolean = true,

    // 是否在列表中显示
//    val showInList: Boolean = true,

    // 是否必填
//    val required: Boolean = false,


}
