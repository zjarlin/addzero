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
 * BizNote 表单属性常量
 */
object BizNoteFormProps {
    const val title = "title"
    const val content = "content"
    const val type = "type"
    const val tags = "tags"
    const val path = "path"
    const val fileUrl = "fileUrl"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(title, content, type, tags, path, fileUrl)
    }
}

@Composable
fun BizNoteForm(
    state: MutableState<BizNoteIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: BizNoteFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        BizNoteFormOriginal(state, dslConfig)
    }
}

@Composable
fun BizNoteFormOriginal(
    state: MutableState<BizNoteIso>,
    dslConfig: BizNoteFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = BizNoteFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        BizNoteFormProps.title to {
            AddTextField(
                value = state.value.title?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(title = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "标题",
                isRequired = true
            )
        },
        BizNoteFormProps.content to {
            AddTextField(
                value = state.value.content?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(content = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "内容",
                isRequired = true
            )
        },
        BizNoteFormProps.type to {
            AddTextField(
                value = state.value.type?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(type = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "类型1=markdown2=pdf3=word4=excel@return笔记类型",
                isRequired = false
            )
        },
        BizNoteFormProps.tags to {
            var dataList by remember { mutableStateOf<List<BizTagIso>>(emptyList()) }

            LaunchedEffect(Unit) {
                try {
                    val provider = Iso2DataProvider.isoToDataProvider[BizTagIso::class]
                    dataList = provider?.invoke("") as? List<BizTagIso> ?: emptyList()
                } catch (e: Exception) {
                    println("加载 tags 数据失败: ${e.message}")
                    dataList = emptyList()
                }
            }

            AddGenericMultiSelector(
                value = state.value.tags ?: emptyList(),
                onValueChange = { state.value = state.value.copy(tags = it) },
                placeholder = "笔记的标签列表，用于分类和检索。通过中间表实现与标签的多对多关系@return标签列表",
                dataProvider = { dataList },
                getId = { it.id ?: 0L },
                getLabel = { it.name ?: "" },
                
            )
        },
        BizNoteFormProps.path to {
            AddTextField(
                value = state.value.path?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(path = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "笔记的路径@return笔记路径",
                isRequired = false
            )
        },
        BizNoteFormProps.fileUrl to {
            AddUrlField(
                value = state.value.fileUrl?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(fileUrl = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "笔记关联的文件链接（可选）。",
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

class BizNoteFormDsl(
    val state: MutableState<BizNoteIso>,
    private val renderMap: MutableMap<String, @Composable () -> Unit>
) {
    // 隐藏字段集合
    val hiddenFields = mutableSetOf<String>()

    // 字段显示顺序（如果为空则使用默认顺序）
    val fieldOrder = mutableListOf<String>()

    // 字段排序映射：字段名 -> 排序值
    private val fieldOrderMap = mutableMapOf<String, Int>()

    /**
     * 配置 title 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun title(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizNoteIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("title")
                renderMap.remove("title")
            }
            render != null -> {
                hiddenFields.remove("title")
                renderMap["title"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("title")
                renderMap.remove("title")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("title", orderValue)
        }
    }

    /**
     * 配置 content 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun content(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizNoteIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("content")
                renderMap.remove("content")
            }
            render != null -> {
                hiddenFields.remove("content")
                renderMap["content"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("content")
                renderMap.remove("content")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("content", orderValue)
        }
    }

    /**
     * 配置 type 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun type(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizNoteIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("type")
                renderMap.remove("type")
            }
            render != null -> {
                hiddenFields.remove("type")
                renderMap["type"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("type")
                renderMap.remove("type")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("type", orderValue)
        }
    }

    /**
     * 配置 tags 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun tags(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizNoteIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("tags")
                renderMap.remove("tags")
            }
            render != null -> {
                hiddenFields.remove("tags")
                renderMap["tags"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("tags")
                renderMap.remove("tags")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("tags", orderValue)
        }
    }

    /**
     * 配置 path 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun path(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<BizNoteIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("path")
                renderMap.remove("path")
            }
            render != null -> {
                hiddenFields.remove("path")
                renderMap["path"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("path")
                renderMap.remove("path")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("path", orderValue)
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
        render: (@Composable (MutableState<BizNoteIso>) -> Unit)? = null
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
            fieldOrder.addAll(BizNoteFormProps.getAllFields())
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
            fieldOrder.addAll(BizNoteFormProps.getAllFields())
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
        val allFields = BizNoteFormProps.getAllFields()
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
 * 记住 BizNote 表单状态的便捷函数
 */
@Composable
fun rememberBizNoteFormState(current: BizNoteIso? = null): MutableState<BizNoteIso> {
    return remember(current) { mutableStateOf(current ?: BizNoteIso()) }
}