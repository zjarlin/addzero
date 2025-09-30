package site.addzero.generated.forms

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import site.addzero.component.high_level.AddMultiColumnContainer
import site.addzero.component.drawer.AddDrawer
import site.addzero.component.form.*
import site.addzero.component.form.number.AddMoneyField
import site.addzero.component.form.number.AddNumberField
import site.addzero.component.form.number.AddIntegerField
import site.addzero.component.form.number.AddDecimalField
import site.addzero.component.form.number.AddPercentageField
import site.addzero.component.form.text.AddTextField
import site.addzero.component.form.text.AddPasswordField
import site.addzero.component.form.text.AddEmailField
import site.addzero.component.form.text.AddPhoneField
import site.addzero.component.form.text.AddUrlField
import site.addzero.component.form.text.AddUsernameField
import site.addzero.component.form.text.AddIdCardField
import site.addzero.component.form.text.AddBankCardField
import site.addzero.component.form.date.AddDateField
import site.addzero.component.form.date.DateType
import site.addzero.component.form.switch.AddSwitchField
import site.addzero.component.form.selector.AddGenericSingleSelector
import site.addzero.component.form.selector.AddGenericMultiSelector
import site.addzero.core.ext.parseObjectByKtx
import site.addzero.core.validation.RegexEnum
import site.addzero.generated.isomorphic.*
import site.addzero.generated.forms.dataprovider.Iso2DataProvider
            import site.addzero.generated.enums.*


/**
 * ThingModelProperty 表单属性常量
 */
object ThingModelPropertyFormProps {
    const val thingModel = "thingModel"
    const val identifier = "identifier"
    const val name = "name"
    const val description = "description"
    const val dataType = "dataType"
    const val required = "required"
    const val minNormalValue = "minNormalValue"
    const val maxNormalValue = "maxNormalValue"
    const val minWarningValue = "minWarningValue"
    const val maxWarningValue = "maxWarningValue"
    const val dataPrecision = "dataPrecision"
    const val accessMode = "accessMode"
    const val sort = "sort"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(thingModel, identifier, name, description, dataType, required, minNormalValue, maxNormalValue, minWarningValue, maxWarningValue, dataPrecision, accessMode, sort)
    }
}

@Composable
fun ThingModelPropertyForm(
    state: MutableState<ThingModelPropertyIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: ThingModelPropertyFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        ThingModelPropertyFormOriginal(state, dslConfig)
    }
}

@Composable
fun ThingModelPropertyFormOriginal(
    state: MutableState<ThingModelPropertyIso>,
    dslConfig: ThingModelPropertyFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = ThingModelPropertyFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        ThingModelPropertyFormProps.thingModel to {
            var dataList by remember { mutableStateOf<List<ThingModelIso>>(emptyList()) }

            LaunchedEffect(Unit) {
                try {
                    val provider = Iso2DataProvider.isoToDataProvider[ThingModelIso::class]
                    dataList = provider?.invoke("") as? List<ThingModelIso> ?: emptyList()
                } catch (e: Exception) {
                    println("加载 thingModel 数据失败: ${e.message}")
                    dataList = emptyList()
                }
            }

            AddGenericSingleSelector(
                value = state.value.thingModel,
                onValueChange = { state.value = state.value.copy(thingModel = it) },
                placeholder = "所属物模型",
                dataProvider = { dataList },
                getId = { it.id ?: 0L },
                getLabel = { it.name ?: "" },
                
            )
        },
        ThingModelPropertyFormProps.identifier to {
            AddTextField(
                value = state.value.identifier?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(identifier = if (it.isNullOrEmpty()) "" else it.parseObjectByKtx())
                },
                label = "属性标识",
                isRequired = true
            )
        },
        ThingModelPropertyFormProps.name to {
            AddTextField(
                value = state.value.name?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(name = if (it.isNullOrEmpty()) "" else it.parseObjectByKtx())
                },
                label = "属性名称",
                isRequired = true
            )
        },
        ThingModelPropertyFormProps.description to {
            AddTextField(
                value = state.value.description?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(description = if (it.isNullOrEmpty()) "" else it.parseObjectByKtx())
                },
                label = "属性描述",
                isRequired = true
            )
        },
        ThingModelPropertyFormProps.dataType to {
            AddTextField(
                value = state.value.dataType?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(dataType = if (it.isNullOrEmpty()) "" else it.parseObjectByKtx())
                },
                label = "数据类型，例如：int32,float,double,string,bool,enum等",
                isRequired = true
            )
        },
        ThingModelPropertyFormProps.required to {
            AddSwitchField(
                value = state.value.required ?: false,
                onValueChange = { state.value = state.value.copy(required = it) },
                label = "是否必填"
            )
        },
        ThingModelPropertyFormProps.minNormalValue to {
            AddMoneyField(
                value = state.value.minNormalValue?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(minNormalValue = if (it.isNullOrEmpty()) null else it.parseObjectByKtx())
                },
                label = "正常范围最小值",
                isRequired = false,
                currency = "CNY"
            )
        },
        ThingModelPropertyFormProps.maxNormalValue to {
            AddMoneyField(
                value = state.value.maxNormalValue?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(maxNormalValue = if (it.isNullOrEmpty()) null else it.parseObjectByKtx())
                },
                label = "正常范围最大值",
                isRequired = false,
                currency = "CNY"
            )
        },
        ThingModelPropertyFormProps.minWarningValue to {
            AddMoneyField(
                value = state.value.minWarningValue?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(minWarningValue = if (it.isNullOrEmpty()) null else it.parseObjectByKtx())
                },
                label = "正常范围最小值",
                isRequired = false,
                currency = "CNY"
            )
        },
        ThingModelPropertyFormProps.maxWarningValue to {
            AddMoneyField(
                value = state.value.maxWarningValue?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(maxWarningValue = if (it.isNullOrEmpty()) null else it.parseObjectByKtx())
                },
                label = "正常范围最大值",
                isRequired = false,
                currency = "CNY"
            )
        },
        ThingModelPropertyFormProps.dataPrecision to {
            AddIntegerField(
                value = state.value.dataPrecision?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(dataPrecision = if (it.isNullOrEmpty()) null else it.parseObjectByKtx())
                },
                label = "精度值设置，对数值类型有效",
                isRequired = false
            )
        },
        ThingModelPropertyFormProps.accessMode to {
            AddTextField(
                value = state.value.accessMode?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(accessMode = if (it.isNullOrEmpty()) "" else it.parseObjectByKtx())
                },
                label = "读取方式：读、写、上报",
                isRequired = true
            )
        },
        ThingModelPropertyFormProps.sort to {
            AddIntegerField(
                value = state.value.sort?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(sort = if (it.isNullOrEmpty()) 0 else it.parseObjectByKtx())
                },
                label = "排序",
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

class ThingModelPropertyFormDsl(
    val state: MutableState<ThingModelPropertyIso>,
    private val renderMap: MutableMap<String, @Composable () -> Unit>
) {
    // 隐藏字段集合
    val hiddenFields = mutableSetOf<String>()

    // 字段显示顺序（如果为空则使用默认顺序）
    val fieldOrder = mutableListOf<String>()

    // 字段排序映射：字段名 -> 排序值
    private val fieldOrderMap = mutableMapOf<String, Int>()

    /**
     * 配置 thingModel 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun thingModel(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("thingModel")
                renderMap.remove("thingModel")
            }
            render != null -> {
                hiddenFields.remove("thingModel")
                renderMap["thingModel"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("thingModel")
                renderMap.remove("thingModel")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("thingModel", orderValue)
        }
    }

    /**
     * 配置 identifier 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun identifier(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("identifier")
                renderMap.remove("identifier")
            }
            render != null -> {
                hiddenFields.remove("identifier")
                renderMap["identifier"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("identifier")
                renderMap.remove("identifier")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("identifier", orderValue)
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
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
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
     * 配置 description 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun description(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("description")
                renderMap.remove("description")
            }
            render != null -> {
                hiddenFields.remove("description")
                renderMap["description"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("description")
                renderMap.remove("description")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("description", orderValue)
        }
    }

    /**
     * 配置 dataType 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun dataType(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("dataType")
                renderMap.remove("dataType")
            }
            render != null -> {
                hiddenFields.remove("dataType")
                renderMap["dataType"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("dataType")
                renderMap.remove("dataType")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("dataType", orderValue)
        }
    }

    /**
     * 配置 required 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun required(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("required")
                renderMap.remove("required")
            }
            render != null -> {
                hiddenFields.remove("required")
                renderMap["required"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("required")
                renderMap.remove("required")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("required", orderValue)
        }
    }

    /**
     * 配置 minNormalValue 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun minNormalValue(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("minNormalValue")
                renderMap.remove("minNormalValue")
            }
            render != null -> {
                hiddenFields.remove("minNormalValue")
                renderMap["minNormalValue"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("minNormalValue")
                renderMap.remove("minNormalValue")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("minNormalValue", orderValue)
        }
    }

    /**
     * 配置 maxNormalValue 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun maxNormalValue(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("maxNormalValue")
                renderMap.remove("maxNormalValue")
            }
            render != null -> {
                hiddenFields.remove("maxNormalValue")
                renderMap["maxNormalValue"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("maxNormalValue")
                renderMap.remove("maxNormalValue")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("maxNormalValue", orderValue)
        }
    }

    /**
     * 配置 minWarningValue 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun minWarningValue(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("minWarningValue")
                renderMap.remove("minWarningValue")
            }
            render != null -> {
                hiddenFields.remove("minWarningValue")
                renderMap["minWarningValue"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("minWarningValue")
                renderMap.remove("minWarningValue")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("minWarningValue", orderValue)
        }
    }

    /**
     * 配置 maxWarningValue 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun maxWarningValue(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("maxWarningValue")
                renderMap.remove("maxWarningValue")
            }
            render != null -> {
                hiddenFields.remove("maxWarningValue")
                renderMap["maxWarningValue"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("maxWarningValue")
                renderMap.remove("maxWarningValue")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("maxWarningValue", orderValue)
        }
    }

    /**
     * 配置 dataPrecision 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun dataPrecision(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("dataPrecision")
                renderMap.remove("dataPrecision")
            }
            render != null -> {
                hiddenFields.remove("dataPrecision")
                renderMap["dataPrecision"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("dataPrecision")
                renderMap.remove("dataPrecision")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("dataPrecision", orderValue)
        }
    }

    /**
     * 配置 accessMode 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun accessMode(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("accessMode")
                renderMap.remove("accessMode")
            }
            render != null -> {
                hiddenFields.remove("accessMode")
                renderMap["accessMode"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("accessMode")
                renderMap.remove("accessMode")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("accessMode", orderValue)
        }
    }

    /**
     * 配置 sort 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun sort(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ThingModelPropertyIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("sort")
                renderMap.remove("sort")
            }
            render != null -> {
                hiddenFields.remove("sort")
                renderMap["sort"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("sort")
                renderMap.remove("sort")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("sort", orderValue)
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
            fieldOrder.addAll(ThingModelPropertyFormProps.getAllFields())
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
            fieldOrder.addAll(ThingModelPropertyFormProps.getAllFields())
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
        val allFields = ThingModelPropertyFormProps.getAllFields()
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
 * 记住 ThingModelProperty 表单状态的便捷函数
 */
@Composable
fun rememberThingModelPropertyFormState(current: ThingModelPropertyIso? = null): MutableState<ThingModelPropertyIso> {
    return remember(current) { mutableStateOf(current ?: ThingModelPropertyIso()) }
}