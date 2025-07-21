package com.addzero.kmp.generated.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.high_level.AddMultiColumnContainer
import com.addzero.kmp.component.drawer.AddDrawer
import com.addzero.kmp.component.form.*
import com.addzero.kmp.component.form.number.AddMoneyField
import com.addzero.kmp.component.form.number.AddNumberField
import com.addzero.kmp.component.form.number.AddIntegerField
import com.addzero.kmp.component.form.number.AddDecimalField
import com.addzero.kmp.component.form.number.AddPercentageField
import com.addzero.kmp.component.form.text.AddTextField
import com.addzero.kmp.component.form.text.AddPasswordField
import com.addzero.kmp.component.form.text.AddEmailField
import com.addzero.kmp.component.form.text.AddPhoneField
import com.addzero.kmp.component.form.text.AddUrlField
import com.addzero.kmp.component.form.text.AddUsernameField
import com.addzero.kmp.component.form.text.AddIdCardField
import com.addzero.kmp.component.form.text.AddBankCardField
import com.addzero.kmp.component.form.date.AddDateField
import com.addzero.kmp.component.form.date.DateType
import com.addzero.kmp.component.form.switch.AddSwitchField
import com.addzero.kmp.component.form.selector.AddGenericSingleSelector
import com.addzero.kmp.component.form.selector.AddGenericMultiSelector
import com.addzero.kmp.core.ext.parseObjectByKtx
import com.addzero.kmp.core.validation.RegexEnum
import com.addzero.kmp.generated.isomorphic.*
import com.addzero.kmp.generated.forms.dataprovider.Iso2DataProvider
            import com.addzero.kmp.generated.enums.*


/**
 * JdbcColumnMetadata 表单属性常量
 */
object JdbcColumnMetadataFormProps {
    const val tableName = "tableName"
    const val columnName = "columnName"
    const val jdbcType = "jdbcType"
    const val columnType = "columnType"
    const val columnLength = "columnLength"
    const val nullableBoolean = "nullableBoolean"
    const val nullableFlag = "nullableFlag"
    const val remarks = "remarks"
    const val defaultValue = "defaultValue"
    const val primaryKeyFlag = "primaryKeyFlag"
    const val tableId = "tableId"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(tableName, columnName, jdbcType, columnType, columnLength, nullableBoolean, nullableFlag, remarks, defaultValue, primaryKeyFlag, tableId)
    }
}

@Composable
fun JdbcColumnMetadataForm(
    state: MutableState<JdbcColumnMetadataIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: JdbcColumnMetadataFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        JdbcColumnMetadataFormOriginal(state, dslConfig)
    }
}

@Composable
fun JdbcColumnMetadataFormOriginal(
    state: MutableState<JdbcColumnMetadataIso>,
    dslConfig: JdbcColumnMetadataFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = JdbcColumnMetadataFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        JdbcColumnMetadataFormProps.tableName to {
            AddTextField(
                value = state.value.tableName?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(tableName = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "tableName",
                isRequired = false
            )
        },
        JdbcColumnMetadataFormProps.columnName to {
            AddTextField(
                value = state.value.columnName?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(columnName = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "columnName",
                isRequired = false
            )
        },
        JdbcColumnMetadataFormProps.jdbcType to {
            AddIntegerField(
                value = state.value.jdbcType?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(jdbcType = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "jdbcType",
                isRequired = false
            )
        },
        JdbcColumnMetadataFormProps.columnType to {
            AddTextField(
                value = state.value.columnType?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(columnType = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "columnType",
                isRequired = false
            )
        },
        JdbcColumnMetadataFormProps.columnLength to {
            AddIntegerField(
                value = state.value.columnLength?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(columnLength = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "columnLength",
                isRequired = false
            )
        },
        JdbcColumnMetadataFormProps.nullableBoolean to {
            AddSwitchField(
                value = state.value.nullableBoolean ?: false,
                onValueChange = { state.value = state.value.copy(nullableBoolean = it) },
                label = "nullable"
            )
        },
        JdbcColumnMetadataFormProps.nullableFlag to {
            AddTextField(
                value = state.value.nullableFlag?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(nullableFlag = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "nullableFlag",
                isRequired = false
            )
        },
        JdbcColumnMetadataFormProps.remarks to {
            AddTextField(
                value = state.value.remarks?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(remarks = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "remarks",
                isRequired = false
            )
        },
        JdbcColumnMetadataFormProps.defaultValue to {
            AddTextField(
                value = state.value.defaultValue?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(defaultValue = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "defaultValue",
                isRequired = false
            )
        },
        JdbcColumnMetadataFormProps.primaryKeyFlag to {
            AddTextField(
                value = state.value.primaryKeyFlag?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(primaryKeyFlag = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "primaryKey",
                isRequired = false
            )
        },
        JdbcColumnMetadataFormProps.tableId to {
            AddIntegerField(
                value = state.value.tableId?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(tableId = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "tableId",
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

class JdbcColumnMetadataFormDsl(
    val state: MutableState<JdbcColumnMetadataIso>,
    private val renderMap: MutableMap<String, @Composable () -> Unit>
) {
    // 隐藏字段集合
    val hiddenFields = mutableSetOf<String>()

    // 字段显示顺序（如果为空则使用默认顺序）
    val fieldOrder = mutableListOf<String>()

    // 字段排序映射：字段名 -> 排序值
    private val fieldOrderMap = mutableMapOf<String, Int>()

    /**
     * 配置 tableName 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun tableName(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<JdbcColumnMetadataIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("tableName")
                renderMap.remove("tableName")
            }
            render != null -> {
                hiddenFields.remove("tableName")
                renderMap["tableName"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("tableName")
                renderMap.remove("tableName")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("tableName", orderValue)
        }
    }

    /**
     * 配置 columnName 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun columnName(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<JdbcColumnMetadataIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("columnName")
                renderMap.remove("columnName")
            }
            render != null -> {
                hiddenFields.remove("columnName")
                renderMap["columnName"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("columnName")
                renderMap.remove("columnName")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("columnName", orderValue)
        }
    }

    /**
     * 配置 jdbcType 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun jdbcType(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<JdbcColumnMetadataIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("jdbcType")
                renderMap.remove("jdbcType")
            }
            render != null -> {
                hiddenFields.remove("jdbcType")
                renderMap["jdbcType"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("jdbcType")
                renderMap.remove("jdbcType")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("jdbcType", orderValue)
        }
    }

    /**
     * 配置 columnType 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun columnType(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<JdbcColumnMetadataIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("columnType")
                renderMap.remove("columnType")
            }
            render != null -> {
                hiddenFields.remove("columnType")
                renderMap["columnType"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("columnType")
                renderMap.remove("columnType")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("columnType", orderValue)
        }
    }

    /**
     * 配置 columnLength 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun columnLength(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<JdbcColumnMetadataIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("columnLength")
                renderMap.remove("columnLength")
            }
            render != null -> {
                hiddenFields.remove("columnLength")
                renderMap["columnLength"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("columnLength")
                renderMap.remove("columnLength")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("columnLength", orderValue)
        }
    }

    /**
     * 配置 nullableBoolean 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun nullableBoolean(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<JdbcColumnMetadataIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("nullableBoolean")
                renderMap.remove("nullableBoolean")
            }
            render != null -> {
                hiddenFields.remove("nullableBoolean")
                renderMap["nullableBoolean"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("nullableBoolean")
                renderMap.remove("nullableBoolean")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("nullableBoolean", orderValue)
        }
    }

    /**
     * 配置 nullableFlag 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun nullableFlag(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<JdbcColumnMetadataIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("nullableFlag")
                renderMap.remove("nullableFlag")
            }
            render != null -> {
                hiddenFields.remove("nullableFlag")
                renderMap["nullableFlag"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("nullableFlag")
                renderMap.remove("nullableFlag")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("nullableFlag", orderValue)
        }
    }

    /**
     * 配置 remarks 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun remarks(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<JdbcColumnMetadataIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("remarks")
                renderMap.remove("remarks")
            }
            render != null -> {
                hiddenFields.remove("remarks")
                renderMap["remarks"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("remarks")
                renderMap.remove("remarks")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("remarks", orderValue)
        }
    }

    /**
     * 配置 defaultValue 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun defaultValue(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<JdbcColumnMetadataIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("defaultValue")
                renderMap.remove("defaultValue")
            }
            render != null -> {
                hiddenFields.remove("defaultValue")
                renderMap["defaultValue"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("defaultValue")
                renderMap.remove("defaultValue")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("defaultValue", orderValue)
        }
    }

    /**
     * 配置 primaryKeyFlag 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun primaryKeyFlag(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<JdbcColumnMetadataIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("primaryKeyFlag")
                renderMap.remove("primaryKeyFlag")
            }
            render != null -> {
                hiddenFields.remove("primaryKeyFlag")
                renderMap["primaryKeyFlag"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("primaryKeyFlag")
                renderMap.remove("primaryKeyFlag")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("primaryKeyFlag", orderValue)
        }
    }

    /**
     * 配置 tableId 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun tableId(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<JdbcColumnMetadataIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("tableId")
                renderMap.remove("tableId")
            }
            render != null -> {
                hiddenFields.remove("tableId")
                renderMap["tableId"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("tableId")
                renderMap.remove("tableId")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("tableId", orderValue)
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
            fieldOrder.addAll(JdbcColumnMetadataFormProps.getAllFields())
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
            fieldOrder.addAll(JdbcColumnMetadataFormProps.getAllFields())
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
        val allFields = JdbcColumnMetadataFormProps.getAllFields()
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
 * 记住 JdbcColumnMetadata 表单状态的便捷函数
 */
@Composable
fun rememberJdbcColumnMetadataFormState(current: JdbcColumnMetadataIso? = null): MutableState<JdbcColumnMetadataIso> {
    return remember(current) { mutableStateOf(current ?: JdbcColumnMetadataIso()) }
}