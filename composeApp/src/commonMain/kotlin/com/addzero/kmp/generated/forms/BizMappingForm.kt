package com.addzero.kmp.generated.forms

import androidx.compose.runtime.*
import com.addzero.kmp.component.high_level.AddMultiColumnContainer
import com.addzero.kmp.component.drawer.AddDrawer
import com.addzero.kmp.component.form.number.AddIntegerField
import com.addzero.kmp.component.form.text.AddTextField
import com.addzero.kmp.core.ext.parseObjectByKtx
import com.addzero.kmp.generated.isomorphic.*


/**
 * BizMapping 表单属性常量
 */
object BizMappingFormProps {
    const val fromId = "fromId"
    const val toId = "toId"
    const val mappingType = "mappingType"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(fromId, toId, mappingType)
    }
}

@Composable
fun BizMappingForm(
    state: MutableState<BizMappingIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: BizMappingFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        BizMappingFormOriginal(state, dslConfig)
    }
}

@Composable
fun BizMappingFormOriginal(
    state: MutableState<BizMappingIso>,
    dslConfig: BizMappingFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = BizMappingFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        BizMappingFormProps.fromId to {
            AddIntegerField(
                value = state.value.fromId?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(fromId = if (it.isBlank()) 0L else it.parseObjectByKtx())
                },
                label = "fromId",
                isRequired = true
            )
        },
        BizMappingFormProps.toId to {
            AddIntegerField(
                value = state.value.toId?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(toId = if (it.isBlank()) 0L else it.parseObjectByKtx())
                },
                label = "toId",
                isRequired = true
            )
        },
        BizMappingFormProps.mappingType to {
            AddTextField(
                value = state.value.mappingType?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(mappingType = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "mappingType",
                isRequired = true
            )
        }
    )

    // 根据 DSL 配置计算最终要渲染的字段列表（保持顺序）
    val finalItems = remember(renderMap, dsl.hiddenFields, dsl.fieldOrder) {
        // 1. 获取字段顺序
        val orderedFieldNames = if (dsl.fieldOrder.isNotEmpty()) {
            // 使用 DSL 中设置的顺序（可能通过 order() 方法或 order 参数设置）
            dsl.fieldOrder
        } else {
            // 使用默认顺序
            defaultRenderMap.keys.toList()
        }

        // 2. 按顺序构建最终的渲染列表
        orderedFieldNames
            .filter { fieldName -> fieldName !in dsl.hiddenFields } // 过滤隐藏字段
            .mapNotNull { fieldName ->
                // 优先使用自定义渲染，否则使用默认渲染
                when {
                    renderMap.containsKey(fieldName) -> renderMap[fieldName]
                    defaultRenderMap.containsKey(fieldName) -> defaultRenderMap[fieldName]
                    else -> null
                }
            }
    }

    AddMultiColumnContainer(
        howMuchColumn = 2,
        items = finalItems
    )
}

class BizMappingFormDsl(
    val state: MutableState<BizMappingIso>,
    private val renderMap: MutableMap<String, @Composable () -> Unit>
) {
    // 隐藏字段集合
    val hiddenFields = mutableSetOf<String>()

    // 字段显示顺序（如果为空则使用默认顺序）
    val fieldOrder = mutableListOf<String>()

    // 字段排序映射：字段名 -> 排序值
    private val fieldOrderMap = mutableMapOf<String, Int>()

    /**
     * 配置 fromId 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun fromId(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizMappingIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("fromId")
                renderMap.remove("fromId")
            }
            render != null -> {
                hiddenFields.remove("fromId")
                renderMap["fromId"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("fromId")
                renderMap.remove("fromId")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("fromId", orderValue)
        }
    }

    /**
     * 配置 toId 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun toId(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizMappingIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("toId")
                renderMap.remove("toId")
            }
            render != null -> {
                hiddenFields.remove("toId")
                renderMap["toId"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("toId")
                renderMap.remove("toId")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("toId", orderValue)
        }
    }

    /**
     * 配置 mappingType 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun mappingType(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizMappingIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("mappingType")
                renderMap.remove("mappingType")
            }
            render != null -> {
                hiddenFields.remove("mappingType")
                renderMap["mappingType"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("mappingType")
                renderMap.remove("mappingType")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("mappingType", orderValue)
        }
    }

    /**
     * 隐藏指定字段
     */
    fun hide(vararg fields: String) {
        hiddenFields.addAll(fields)
    }

    /**
     * 设置字段显示顺序
     * @param fields 字段名列表，按显示顺序排列
     */
    fun order(vararg fields: String) {
        fieldOrder.clear()
        fieldOrder.addAll(fields)
    }

    /**
     * 在指定字段之前插入字段
     */
    fun insertBefore(targetField: String, vararg newFields: String) {
        if (fieldOrder.isEmpty()) {
            // 如果没有自定义顺序，先初始化为默认顺序
            fieldOrder.addAll(BizMappingFormProps.getAllFields())
        }
        val index = fieldOrder.indexOf(targetField)
        if (index >= 0) {
            fieldOrder.addAll(index, newFields.toList())
        }
    }

    /**
     * 在指定字段之后插入字段
     */
    fun insertAfter(targetField: String, vararg newFields: String) {
        if (fieldOrder.isEmpty()) {
            // 如果没有自定义顺序，先初始化为默认顺序
            fieldOrder.addAll(BizMappingFormProps.getAllFields())
        }
        val index = fieldOrder.indexOf(targetField)
        if (index >= 0) {
            fieldOrder.addAll(index + 1, newFields.toList())
        }
    }

    /**
     * 更新字段排序
     * @param fieldName 字段名
     * @param orderValue 排序值（数值越小越靠前）
     */
    private fun updateFieldOrder(fieldName: String, orderValue: Int) {
        fieldOrderMap[fieldName] = orderValue

        // 重新计算字段顺序
        val allFields = BizMappingFormProps.getAllFields()
        val sortedFields = allFields.sortedWith { field1, field2 ->
            val order1 = fieldOrderMap[field1] ?: Int.MAX_VALUE
            val order2 = fieldOrderMap[field2] ?: Int.MAX_VALUE
            when {
                order1 != Int.MAX_VALUE && order2 != Int.MAX_VALUE -> order1.compareTo(order2)
                order1 != Int.MAX_VALUE -> -1 // field1 有排序值，排在前面
                order2 != Int.MAX_VALUE -> 1  // field2 有排序值，排在前面
                else -> allFields.indexOf(field1).compareTo(allFields.indexOf(field2)) // 都没有排序值，保持原有顺序
            }
        }

        fieldOrder.clear()
        fieldOrder.addAll(sortedFields)
    }
}

/**
 * 记住 BizMapping 表单状态的便捷函数
 */
@Composable
fun rememberBizMappingFormState(current: BizMappingIso? = null): MutableState<BizMappingIso> {
    return remember(current) { mutableStateOf(current ?: BizMappingIso()) }
}