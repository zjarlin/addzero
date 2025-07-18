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
 * BizDotfiles 表单属性常量
 */
object BizDotfilesFormProps {
    const val osType = "osType"
    const val osStructure = "osStructure"
    const val defType = "defType"
    const val name = "name"
    const val value = "value"
    const val describtion = "describtion"
    const val status = "status"
    const val fileUrl = "fileUrl"
    const val location = "location"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(osType, osStructure, defType, name, value, describtion, status, fileUrl, location)
    }
}

@Composable
fun BizDotfilesForm(
    state: MutableState<BizDotfilesIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: BizDotfilesFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        BizDotfilesFormOriginal(state, dslConfig)
    }
}

@Composable
fun BizDotfilesFormOriginal(
    state: MutableState<BizDotfilesIso>,
    dslConfig: BizDotfilesFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = BizDotfilesFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        BizDotfilesFormProps.osType to {
            AddGenericMultiSelector(
                value = state.value.osType ?: emptyList(),
                onValueChange = { state.value = state.value.copy(osType = it) },
                placeholder = "操作系统win=winlinux=linuxmac=macnull=不限",
                dataProvider = { emptyList<String>() }, // 需要根据具体类型提供数据
                getId = { it.toString() },
                getLabel = { it.toString() },
                
                
            )
        },
        BizDotfilesFormProps.osStructure to {
            AddGenericSingleSelector(
                value = state.value.osStructure,
                onValueChange = { state.value = state.value.copy(osStructure = it) },
                placeholder = "系统架构arm64=arm64x86=x86不限=不限",
                dataProvider = { EnumShellPlatforms.entries },
                getId = { it.name },
                getLabel = { it.name }
            )
        },
        BizDotfilesFormProps.defType to {
            AddTextField(
                value = state.value.defType?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(defType = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "定义类型alias=aliasexport=exportfunction=functionsh=shvar=var",
                isRequired = true
            )
        },
        BizDotfilesFormProps.name to {
            AddTextField(
                value = state.value.name?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(name = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "名称",
                isRequired = true
            )
        },
        BizDotfilesFormProps.value to {
            AddTextField(
                value = state.value.value?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(value = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "值",
                isRequired = true
            )
        },
        BizDotfilesFormProps.describtion to {
            AddTextField(
                value = state.value.describtion?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(describtion = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "注释",
                isRequired = false
            )
        },
        BizDotfilesFormProps.status to {
            AddTextField(
                value = state.value.status?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(status = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "状态1=启用0=未启用",
                isRequired = true
            )
        },
        BizDotfilesFormProps.fileUrl to {
            AddUrlField(
                value = state.value.fileUrl?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(fileUrl = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "文件地址",
                isRequired = false
            )
        },
        BizDotfilesFormProps.location to {
            AddTextField(
                value = state.value.location?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(location = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "文件位置",
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

class BizDotfilesFormDsl(
    val state: MutableState<BizDotfilesIso>,
    private val renderMap: MutableMap<String, @Composable () -> Unit>
) {
    // 隐藏字段集合
    val hiddenFields = mutableSetOf<String>()

    // 字段显示顺序（如果为空则使用默认顺序）
    val fieldOrder = mutableListOf<String>()

    // 字段排序映射：字段名 -> 排序值
    private val fieldOrderMap = mutableMapOf<String, Int>()

    /**
     * 配置 osType 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun osType(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizDotfilesIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("osType")
                renderMap.remove("osType")
            }
            render != null -> {
                hiddenFields.remove("osType")
                renderMap["osType"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("osType")
                renderMap.remove("osType")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("osType", orderValue)
        }
    }

    /**
     * 配置 osStructure 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun osStructure(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizDotfilesIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("osStructure")
                renderMap.remove("osStructure")
            }
            render != null -> {
                hiddenFields.remove("osStructure")
                renderMap["osStructure"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("osStructure")
                renderMap.remove("osStructure")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("osStructure", orderValue)
        }
    }

    /**
     * 配置 defType 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun defType(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizDotfilesIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("defType")
                renderMap.remove("defType")
            }
            render != null -> {
                hiddenFields.remove("defType")
                renderMap["defType"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("defType")
                renderMap.remove("defType")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("defType", orderValue)
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
        render: (@Composable (MutableState<BizDotfilesIso>) -> Unit)? = null
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
     * 配置 value 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun value(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizDotfilesIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("value")
                renderMap.remove("value")
            }
            render != null -> {
                hiddenFields.remove("value")
                renderMap["value"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("value")
                renderMap.remove("value")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("value", orderValue)
        }
    }

    /**
     * 配置 describtion 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun describtion(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizDotfilesIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("describtion")
                renderMap.remove("describtion")
            }
            render != null -> {
                hiddenFields.remove("describtion")
                renderMap["describtion"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("describtion")
                renderMap.remove("describtion")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("describtion", orderValue)
        }
    }

    /**
     * 配置 status 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun status(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizDotfilesIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("status")
                renderMap.remove("status")
            }
            render != null -> {
                hiddenFields.remove("status")
                renderMap["status"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("status")
                renderMap.remove("status")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("status", orderValue)
        }
    }

    /**
     * 配置 fileUrl 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun fileUrl(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizDotfilesIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("fileUrl")
                renderMap.remove("fileUrl")
            }
            render != null -> {
                hiddenFields.remove("fileUrl")
                renderMap["fileUrl"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("fileUrl")
                renderMap.remove("fileUrl")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("fileUrl", orderValue)
        }
    }

    /**
     * 配置 location 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun location(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizDotfilesIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("location")
                renderMap.remove("location")
            }
            render != null -> {
                hiddenFields.remove("location")
                renderMap["location"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("location")
                renderMap.remove("location")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("location", orderValue)
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
            fieldOrder.addAll(BizDotfilesFormProps.getAllFields())
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
            fieldOrder.addAll(BizDotfilesFormProps.getAllFields())
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
        val allFields = BizDotfilesFormProps.getAllFields()
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
 * 记住 BizDotfiles 表单状态的便捷函数
 */
@Composable
fun rememberBizDotfilesFormState(current: BizDotfilesIso? = null): MutableState<BizDotfilesIso> {
    return remember(current) { mutableStateOf(current ?: BizDotfilesIso()) }
}