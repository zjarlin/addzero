package com.addzero.kmp.generated.forms

import androidx.compose.runtime.*
import com.addzero.kmp.component.high_level.AddMultiColumnContainer
import com.addzero.kmp.component.drawer.AddDrawer
import com.addzero.kmp.component.form.text.AddTextField
import com.addzero.kmp.core.ext.parseObjectByKtx
import com.addzero.kmp.generated.isomorphic.*


/**
 * Citys 表单属性常量
 */
object CitysFormProps {
    const val areaId = "areaId"
    const val pinyin = "pinyin"
    const val py = "py"
    const val areaName = "areaName"
    const val cityName = "cityName"
    const val provinceName = "provinceName"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(areaId, pinyin, py, areaName, cityName, provinceName)
    }
}

@Composable
fun CitysForm(
    state: MutableState<CitysIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: CitysFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        CitysFormOriginal(state, dslConfig)
    }
}

@Composable
fun CitysFormOriginal(
    state: MutableState<CitysIso>,
    dslConfig: CitysFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = CitysFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        CitysFormProps.areaId to {
            AddTextField(
                value = state.value.areaId?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(areaId = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "🗺️地区ID外部系统（如天气API、地图API等）使用的地区标识符。用于与第三方服务进行数据交互和关联。示例值：-57073(洛阳)-54511(北京)-58367(上海)",
                isRequired = true
            )
        },
        CitysFormProps.pinyin to {
            AddTextField(
                value = state.value.pinyin?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(pinyin = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "🔤完整拼音城市名称的完整拼音表示，用于：-拼音搜索功能-城市列表排序-输入法联想示例值：-luoyang(洛阳)-beijing(北京)-shanghai(上海)",
                isRequired = false
            )
        },
        CitysFormProps.py to {
            AddTextField(
                value = state.value.py?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(py = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "🅰️拼音简写城市名称拼音的首字母缩写，用于：-快速检索和过滤-城市选择器的字母索引-移动端快速定位示例值：-ly(洛阳)-bj(北京)-sh(上海)",
                isRequired = false
            )
        },
        CitysFormProps.areaName to {
            AddTextField(
                value = state.value.areaName?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(areaName = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "🏘️地区名称具体的地区、区县或城市名称，是最精确的地理位置描述。通常用于地址显示和精确定位。示例值：-洛阳(河南省洛阳市)-朝阳区(北京市朝阳区)-浦东新区(上海市浦东新区)",
                isRequired = false
            )
        },
        CitysFormProps.cityName to {
            AddTextField(
                value = state.value.cityName?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(cityName = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "🏙️城市名称所属城市的完整名称，包含市字后缀。用于行政区划层级显示和城市级别的数据统计。示例值：-洛阳市-北京市-上海市",
                isRequired = false
            )
        },
        CitysFormProps.provinceName to {
            AddTextField(
                value = state.value.provinceName?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(provinceName = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "🗾省份名称所属省份、直辖市或自治区的名称。用于省级行政区划显示和地理位置的层级结构。示例值：-河南省-北京市(直辖市)-上海市(直辖市)-广西壮族自治区",
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

class CitysFormDsl(
    val state: MutableState<CitysIso>,
    private val renderMap: MutableMap<String, @Composable () -> Unit>
) {
    // 隐藏字段集合
    val hiddenFields = mutableSetOf<String>()

    // 字段显示顺序（如果为空则使用默认顺序）
    val fieldOrder = mutableListOf<String>()

    // 字段排序映射：字段名 -> 排序值
    private val fieldOrderMap = mutableMapOf<String, Int>()

    /**
     * 配置 areaId 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun areaId(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<CitysIso>) -> Unit)? = null
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
     * 配置 pinyin 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun pinyin(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<CitysIso>) -> Unit)? = null
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
     * 配置 py 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun py(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<CitysIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("py")
                renderMap.remove("py")
            }
            render != null -> {
                hiddenFields.remove("py")
                renderMap["py"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("py")
                renderMap.remove("py")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("py", orderValue)
        }
    }

    /**
     * 配置 areaName 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun areaName(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<CitysIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("areaName")
                renderMap.remove("areaName")
            }
            render != null -> {
                hiddenFields.remove("areaName")
                renderMap["areaName"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("areaName")
                renderMap.remove("areaName")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("areaName", orderValue)
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
        render: (@Composable (MutableState<CitysIso>) -> Unit)? = null
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
     * 配置 provinceName 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun provinceName(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<CitysIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("provinceName")
                renderMap.remove("provinceName")
            }
            render != null -> {
                hiddenFields.remove("provinceName")
                renderMap["provinceName"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("provinceName")
                renderMap.remove("provinceName")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("provinceName", orderValue)
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
            fieldOrder.addAll(CitysFormProps.getAllFields())
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
            fieldOrder.addAll(CitysFormProps.getAllFields())
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
        val allFields = CitysFormProps.getAllFields()
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
 * 记住 Citys 表单状态的便捷函数
 */
@Composable
fun rememberCitysFormState(current: CitysIso? = null): MutableState<CitysIso> {
    return remember(current) { mutableStateOf(current ?: CitysIso()) }
}