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
 * InternalCitys 表单属性常量
 */
object InternalCitysFormProps {
    const val cityId = "cityId"
    const val cityName = "cityName"
    const val countryName = "countryName"
    const val continents = "continents"
    const val english = "english"
    const val pinyin = "pinyin"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(cityId, cityName, countryName, continents, english, pinyin)
    }
}

@Composable
fun InternalCitysForm(
    state: MutableState<InternalCitysIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: InternalCitysFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        InternalCitysFormOriginal(state, dslConfig)
    }
}

@Composable
fun InternalCitysFormOriginal(
    state: MutableState<InternalCitysIso>,
    dslConfig: InternalCitysFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = InternalCitysFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        InternalCitysFormProps.cityId to {
            AddTextField(
                value = state.value.cityId?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(cityId = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "🏷️城市标识码外部系统或国际标准使用的城市代码，可能包含：-ISO城市代码-天气API城市代码-时区标识符-自定义城市编码示例值：-CN_BJ_001(北京)-US_NY_001(纽约)-JP_TK_001(东京)-GB_LN_001(伦敦)",
                isRequired = false
            )
        },
        InternalCitysFormProps.cityName to {
            AddTextField(
                value = state.value.cityName?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(cityName = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "🏙️城市中文名称城市的中文显示名称，用于：-中文界面显示-中文搜索功能-本地化用户体验示例值：-北京(中国首都)-纽约(美国城市)-东京(日本首都)-伦敦(英国首都)",
                isRequired = false
            )
        },
        InternalCitysFormProps.countryName to {
            AddTextField(
                value = state.value.countryName?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(countryName = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "🌏国家名称城市所属国家的中文名称，用于：-地理位置层级显示-按国家分组查询-国际化地址格式示例值：-中国-美国-日本-英国-法国",
                isRequired = false
            )
        },
        InternalCitysFormProps.continents to {
            AddTextField(
                value = state.value.continents?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(continents = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "🌍所属大洲城市所在的大洲名称，用于：-全球地理位置分类-时区计算辅助-地理统计分析标准大洲名称：-亚洲(Asia)-欧洲(Europe)-北美洲(NorthAmerica)-南美洲(SouthAmerica)-非洲(Africa)-大洋洲(Oceania)-南极洲(Antarctica)",
                isRequired = false
            )
        },
        InternalCitysFormProps.english to {
            AddTextField(
                value = state.value.english?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(english = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "🔤英文名称城市的英文名称，用于：-国际化界面显示-英文搜索功能-API数据交换-多语言支持示例值：-Beijing(北京)-NewYork(纽约)-Tokyo(东京)-London(伦敦)-Paris(巴黎)",
                isRequired = false
            )
        },
        InternalCitysFormProps.pinyin to {
            AddTextField(
                value = state.value.pinyin?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(pinyin = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "🔤拼音中文城市名称的拼音表示，用于：-拼音搜索功能-城市列表排序-输入法联想-音译标准化示例值：-beijing(北京)-shanghai(上海)-guangzhou(广州)-shenzhen(深圳)注意：对于非中文城市，此字段可能为空或包含音译拼音",
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

class InternalCitysFormDsl(
    val state: MutableState<InternalCitysIso>,
    private val renderMap: MutableMap<String, @Composable () -> Unit>
) {
    // 隐藏字段集合
    val hiddenFields = mutableSetOf<String>()

    // 字段显示顺序（如果为空则使用默认顺序）
    val fieldOrder = mutableListOf<String>()

    // 字段排序映射：字段名 -> 排序值
    private val fieldOrderMap = mutableMapOf<String, Int>()

    /**
     * 配置 cityId 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun cityId(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<InternalCitysIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("cityId")
                renderMap.remove("cityId")
            }
            render != null -> {
                hiddenFields.remove("cityId")
                renderMap["cityId"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("cityId")
                renderMap.remove("cityId")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("cityId", orderValue)
        }
    }

    /**
     * 配置 cityName 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun cityName(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<InternalCitysIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("cityName")
                renderMap.remove("cityName")
            }
            render != null -> {
                hiddenFields.remove("cityName")
                renderMap["cityName"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("cityName")
                renderMap.remove("cityName")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("cityName", orderValue)
        }
    }

    /**
     * 配置 countryName 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun countryName(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<InternalCitysIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("countryName")
                renderMap.remove("countryName")
            }
            render != null -> {
                hiddenFields.remove("countryName")
                renderMap["countryName"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("countryName")
                renderMap.remove("countryName")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("countryName", orderValue)
        }
    }

    /**
     * 配置 continents 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun continents(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<InternalCitysIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("continents")
                renderMap.remove("continents")
            }
            render != null -> {
                hiddenFields.remove("continents")
                renderMap["continents"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("continents")
                renderMap.remove("continents")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("continents", orderValue)
        }
    }

    /**
     * 配置 english 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun english(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<InternalCitysIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("english")
                renderMap.remove("english")
            }
            render != null -> {
                hiddenFields.remove("english")
                renderMap["english"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("english")
                renderMap.remove("english")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("english", orderValue)
        }
    }

    /**
     * 配置 pinyin 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun pinyin(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<InternalCitysIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("pinyin")
                renderMap.remove("pinyin")
            }
            render != null -> {
                hiddenFields.remove("pinyin")
                renderMap["pinyin"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("pinyin")
                renderMap.remove("pinyin")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("pinyin", orderValue)
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
            fieldOrder.addAll(InternalCitysFormProps.getAllFields())
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
            fieldOrder.addAll(InternalCitysFormProps.getAllFields())
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
        val allFields = InternalCitysFormProps.getAllFields()
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
 * 记住 InternalCitys 表单状态的便捷函数
 */
@Composable
fun rememberInternalCitysFormState(current: InternalCitysIso? = null): MutableState<InternalCitysIso> {
    return remember(current) { mutableStateOf(current ?: InternalCitysIso()) }
}