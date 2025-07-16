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
 * SysWeather 表单属性常量
 */
object SysWeatherFormProps {
    const val date = "date"
    const val highTemp = "highTemp"
    const val lowTemp = "lowTemp"
    const val amCondition = "amCondition"
    const val pmCondition = "pmCondition"
    const val wind = "wind"
    const val aqi = "aqi"
    const val areaId = "areaId"
    const val areaType = "areaType"
    const val week = "week"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(date, highTemp, lowTemp, amCondition, pmCondition, wind, aqi, areaId, areaType, week)
    }
}

@Composable
fun SysWeatherForm(
    state: MutableState<SysWeatherIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: SysWeatherFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        SysWeatherFormOriginal(state, dslConfig)
    }
}

@Composable
fun SysWeatherFormOriginal(
    state: MutableState<SysWeatherIso>,
    dslConfig: SysWeatherFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = SysWeatherFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        SysWeatherFormProps.date to {
            AddDateField(
                value = state.value.date,
                onValueChange = {
                if(it==null){
                    state.value = state.value
                }else{
                    state.value = state.value.copy(date = it!!)
                
                }
                },
                label = "日期",
                isRequired = true,
                dateType = DateType.DATE
            )
        },
        SysWeatherFormProps.highTemp to {
            AddIntegerField(
                value = state.value.highTemp?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(highTemp = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "最高温度(摄氏度)",
                isRequired = false
            )
        },
        SysWeatherFormProps.lowTemp to {
            AddIntegerField(
                value = state.value.lowTemp?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(lowTemp = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "最低温度(摄氏度)",
                isRequired = false
            )
        },
        SysWeatherFormProps.amCondition to {
            AddTextField(
                value = state.value.amCondition?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(amCondition = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "上午天气状况",
                isRequired = false
            )
        },
        SysWeatherFormProps.pmCondition to {
            AddTextField(
                value = state.value.pmCondition?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(pmCondition = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "下午天气状况",
                isRequired = false
            )
        },
        SysWeatherFormProps.wind to {
            AddTextField(
                value = state.value.wind?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(wind = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "风力风向信息",
                isRequired = false
            )
        },
        SysWeatherFormProps.aqi to {
            AddIntegerField(
                value = state.value.aqi?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(aqi = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "空气质量指数",
                isRequired = false
            )
        },
        SysWeatherFormProps.areaId to {
            AddTextField(
                value = state.value.areaId?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(areaId = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "地区ID",
                isRequired = false
            )
        },
        SysWeatherFormProps.areaType to {
            AddTextField(
                value = state.value.areaType?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(areaType = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "地区类型",
                isRequired = false
            )
        },
        SysWeatherFormProps.week to {
            AddTextField(
                value = state.value.week?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(week = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "星期信息(格式:YYYY-MM-DD周X)",
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

class SysWeatherFormDsl(
    val state: MutableState<SysWeatherIso>,
    private val renderMap: MutableMap<String, @Composable () -> Unit>
) {
    // 隐藏字段集合
    val hiddenFields = mutableSetOf<String>()

    // 字段显示顺序（如果为空则使用默认顺序）
    val fieldOrder = mutableListOf<String>()

    // 字段排序映射：字段名 -> 排序值
    private val fieldOrderMap = mutableMapOf<String, Int>()

    /**
     * 配置 date 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun date(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysWeatherIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("date")
                renderMap.remove("date")
            }
            render != null -> {
                hiddenFields.remove("date")
                renderMap["date"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("date")
                renderMap.remove("date")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("date", orderValue)
        }
    }

    /**
     * 配置 highTemp 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun highTemp(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysWeatherIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("highTemp")
                renderMap.remove("highTemp")
            }
            render != null -> {
                hiddenFields.remove("highTemp")
                renderMap["highTemp"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("highTemp")
                renderMap.remove("highTemp")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("highTemp", orderValue)
        }
    }

    /**
     * 配置 lowTemp 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun lowTemp(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysWeatherIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("lowTemp")
                renderMap.remove("lowTemp")
            }
            render != null -> {
                hiddenFields.remove("lowTemp")
                renderMap["lowTemp"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("lowTemp")
                renderMap.remove("lowTemp")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("lowTemp", orderValue)
        }
    }

    /**
     * 配置 amCondition 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun amCondition(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysWeatherIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("amCondition")
                renderMap.remove("amCondition")
            }
            render != null -> {
                hiddenFields.remove("amCondition")
                renderMap["amCondition"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("amCondition")
                renderMap.remove("amCondition")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("amCondition", orderValue)
        }
    }

    /**
     * 配置 pmCondition 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun pmCondition(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysWeatherIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("pmCondition")
                renderMap.remove("pmCondition")
            }
            render != null -> {
                hiddenFields.remove("pmCondition")
                renderMap["pmCondition"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("pmCondition")
                renderMap.remove("pmCondition")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("pmCondition", orderValue)
        }
    }

    /**
     * 配置 wind 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun wind(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysWeatherIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("wind")
                renderMap.remove("wind")
            }
            render != null -> {
                hiddenFields.remove("wind")
                renderMap["wind"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("wind")
                renderMap.remove("wind")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("wind", orderValue)
        }
    }

    /**
     * 配置 aqi 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun aqi(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysWeatherIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("aqi")
                renderMap.remove("aqi")
            }
            render != null -> {
                hiddenFields.remove("aqi")
                renderMap["aqi"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("aqi")
                renderMap.remove("aqi")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("aqi", orderValue)
        }
    }

    /**
     * 配置 areaId 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun areaId(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysWeatherIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("areaId")
                renderMap.remove("areaId")
            }
            render != null -> {
                hiddenFields.remove("areaId")
                renderMap["areaId"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("areaId")
                renderMap.remove("areaId")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("areaId", orderValue)
        }
    }

    /**
     * 配置 areaType 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun areaType(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysWeatherIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("areaType")
                renderMap.remove("areaType")
            }
            render != null -> {
                hiddenFields.remove("areaType")
                renderMap["areaType"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("areaType")
                renderMap.remove("areaType")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("areaType", orderValue)
        }
    }

    /**
     * 配置 week 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun week(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysWeatherIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("week")
                renderMap.remove("week")
            }
            render != null -> {
                hiddenFields.remove("week")
                renderMap["week"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("week")
                renderMap.remove("week")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("week", orderValue)
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
            fieldOrder.addAll(SysWeatherFormProps.getAllFields())
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
            fieldOrder.addAll(SysWeatherFormProps.getAllFields())
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
        val allFields = SysWeatherFormProps.getAllFields()
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
 * 记住 SysWeather 表单状态的便捷函数
 */
@Composable
fun rememberSysWeatherFormState(current: SysWeatherIso? = null): MutableState<SysWeatherIso> {
    return remember(current) { mutableStateOf(current ?: SysWeatherIso()) }
}