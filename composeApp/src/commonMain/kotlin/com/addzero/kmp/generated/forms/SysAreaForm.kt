package com.addzero.kmp.generated.forms

import androidx.compose.runtime.*
import com.addzero.kmp.component.high_level.AddMultiColumnContainer
import com.addzero.kmp.component.drawer.AddDrawer
import com.addzero.kmp.component.form.number.AddIntegerField
import com.addzero.kmp.component.form.text.AddTextField
import com.addzero.kmp.core.ext.parseObjectByKtx
import com.addzero.kmp.generated.isomorphic.*


/**
 * SysArea 表单属性常量
 */
object SysAreaFormProps {
    const val parentId = "parentId"
    const val nodeType = "nodeType"
    const val name = "name"
    const val areaCode = "areaCode"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(parentId, nodeType, name, areaCode)
    }
}

@Composable
fun SysAreaForm(
    state: MutableState<SysAreaIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: SysAreaFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        SysAreaFormOriginal(state, dslConfig)
    }
}

@Composable
fun SysAreaFormOriginal(
    state: MutableState<SysAreaIso>,
    dslConfig: SysAreaFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = SysAreaFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        SysAreaFormProps.parentId to {
            AddIntegerField(
                value = state.value.parentId?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(parentId = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "上级数据库列名:parent_id数据类型:int8可空:是",
                isRequired = false
            )
        },
        SysAreaFormProps.nodeType to {
            AddTextField(
                value = state.value.nodeType?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(nodeType = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "1省,2市,3区数据库列名:node_type数据类型:text可空:是默认值:NULL::charactervarying",
                isRequired = false
            )
        },
        SysAreaFormProps.name to {
            AddTextField(
                value = state.value.name?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(name = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "name数据库列名:name数据类型:text可空:是默认值:NULL::charactervarying",
                isRequired = false
            )
        },
        SysAreaFormProps.areaCode to {
            AddTextField(
                value = state.value.areaCode?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(areaCode = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "区域编码数据库列名:area_code数据类型:text可空:是默认值:NULL::charactervarying",
                isRequired = false
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

class SysAreaFormDsl(
    val state: MutableState<SysAreaIso>,
    private val renderMap: MutableMap<String, @Composable () -> Unit>
) {
    // 隐藏字段集合
    val hiddenFields = mutableSetOf<String>()

    // 字段显示顺序（如果为空则使用默认顺序）
    val fieldOrder = mutableListOf<String>()

    // 字段排序映射：字段名 -> 排序值
    private val fieldOrderMap = mutableMapOf<String, Int>()

    /**
     * 配置 parentId 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun parentId(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysAreaIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("parentId")
                renderMap.remove("parentId")
            }
            render != null -> {
                hiddenFields.remove("parentId")
                renderMap["parentId"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("parentId")
                renderMap.remove("parentId")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("parentId", orderValue)
        }
    }

    /**
     * 配置 nodeType 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun nodeType(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysAreaIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("nodeType")
                renderMap.remove("nodeType")
            }
            render != null -> {
                hiddenFields.remove("nodeType")
                renderMap["nodeType"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("nodeType")
                renderMap.remove("nodeType")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("nodeType", orderValue)
        }
    }

    /**
     * 配置 name 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun name(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysAreaIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("name")
                renderMap.remove("name")
            }
            render != null -> {
                hiddenFields.remove("name")
                renderMap["name"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("name")
                renderMap.remove("name")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("name", orderValue)
        }
    }

    /**
     * 配置 areaCode 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun areaCode(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysAreaIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("areaCode")
                renderMap.remove("areaCode")
            }
            render != null -> {
                hiddenFields.remove("areaCode")
                renderMap["areaCode"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("areaCode")
                renderMap.remove("areaCode")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("areaCode", orderValue)
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
            fieldOrder.addAll(SysAreaFormProps.getAllFields())
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
            fieldOrder.addAll(SysAreaFormProps.getAllFields())
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
        val allFields = SysAreaFormProps.getAllFields()
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
 * 记住 SysArea 表单状态的便捷函数
 */
@Composable
fun rememberSysAreaFormState(current: SysAreaIso? = null): MutableState<SysAreaIso> {
    return remember(current) { mutableStateOf(current ?: SysAreaIso()) }
}