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
 * SysDept 表单属性常量
 */
object SysDeptFormProps {
    const val name = "name"
    const val parent = "parent"
    const val children = "children"
    const val sysUsers = "sysUsers"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(name, parent, children, sysUsers)
    }
}

@Composable
fun SysDeptForm(
    state: MutableState<SysDeptIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: SysDeptFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        SysDeptFormOriginal(state, dslConfig)
    }
}

@Composable
fun SysDeptFormOriginal(
    state: MutableState<SysDeptIso>,
    dslConfig: SysDeptFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = SysDeptFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        SysDeptFormProps.name to {
            AddTextField(
                value = state.value.name?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(name = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "部门名称",
                isRequired = true
            )
        },
        SysDeptFormProps.parent to {
            var dataList by remember { mutableStateOf<List<SysDeptIso>>(emptyList()) }

            LaunchedEffect(Unit) {
                try {
                    val provider = Iso2DataProvider.isoToDataProvider[SysDeptIso::class]
                    dataList = provider?.invoke("") as? List<SysDeptIso> ?: emptyList()
                } catch (e: Exception) {
                    println("加载 parent 数据失败: ${e.message}")
                    dataList = emptyList()
                }
            }

            AddGenericSingleSelector(
                value = state.value.parent,
                onValueChange = { state.value = state.value.copy(parent = it) },
                placeholder = "parent",
                dataProvider = { dataList },
                getId = { it.id ?: 0L },
                getLabel = { it.name ?: "" },
               getChildren = { it.children?:emptyList() } 
            )
        },
        SysDeptFormProps.children to {
            var dataList by remember { mutableStateOf<List<SysDeptIso>>(emptyList()) }

            LaunchedEffect(Unit) {
                try {
                    val provider = Iso2DataProvider.isoToDataProvider[SysDeptIso::class]
                    dataList = provider?.invoke("") as? List<SysDeptIso> ?: emptyList()
                } catch (e: Exception) {
                    println("加载 children 数据失败: ${e.message}")
                    dataList = emptyList()
                }
            }

            AddGenericMultiSelector(
                value = state.value.children ?: emptyList(),
                onValueChange = { state.value = state.value.copy(children = it) },
                placeholder = "children",
                dataProvider = { dataList },
                getId = { it.id ?: 0L },
                getLabel = { it.name ?: "" },
                getChildren = { it.children?:emptyList() }
            )
        },
        SysDeptFormProps.sysUsers to {
            var dataList by remember { mutableStateOf<List<SysUserIso>>(emptyList()) }

            LaunchedEffect(Unit) {
                try {
                    val provider = Iso2DataProvider.isoToDataProvider[SysUserIso::class]
                    dataList = provider?.invoke("") as? List<SysUserIso> ?: emptyList()
                } catch (e: Exception) {
                    println("加载 sysUsers 数据失败: ${e.message}")
                    dataList = emptyList()
                }
            }

            AddGenericMultiSelector(
                value = state.value.sysUsers ?: emptyList(),
                onValueChange = { state.value = state.value.copy(sysUsers = it) },
                placeholder = "部门用户",
                dataProvider = { dataList },
                getId = { it.id ?: 0L },
                getLabel = { it.email ?: "" },
                
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

class SysDeptFormDsl(
    val state: MutableState<SysDeptIso>,
    private val renderMap: MutableMap<String, @Composable () -> Unit>
) {
    // 隐藏字段集合
    val hiddenFields = mutableSetOf<String>()

    // 字段显示顺序（如果为空则使用默认顺序）
    val fieldOrder = mutableListOf<String>()

    // 字段排序映射：字段名 -> 排序值
    private val fieldOrderMap = mutableMapOf<String, Int>()

    /**
     * 配置 name 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun name(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysDeptIso>) -> Unit)? = null
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
     * 配置 parent 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun parent(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysDeptIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("parent")
                renderMap.remove("parent")
            }
            render != null -> {
                hiddenFields.remove("parent")
                renderMap["parent"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("parent")
                renderMap.remove("parent")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("parent", orderValue)
        }
    }

    /**
     * 配置 children 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun children(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysDeptIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("children")
                renderMap.remove("children")
            }
            render != null -> {
                hiddenFields.remove("children")
                renderMap["children"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("children")
                renderMap.remove("children")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("children", orderValue)
        }
    }

    /**
     * 配置 sysUsers 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun sysUsers(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysDeptIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("sysUsers")
                renderMap.remove("sysUsers")
            }
            render != null -> {
                hiddenFields.remove("sysUsers")
                renderMap["sysUsers"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("sysUsers")
                renderMap.remove("sysUsers")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("sysUsers", orderValue)
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
            fieldOrder.addAll(SysDeptFormProps.getAllFields())
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
            fieldOrder.addAll(SysDeptFormProps.getAllFields())
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
        val allFields = SysDeptFormProps.getAllFields()
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
 * 记住 SysDept 表单状态的便捷函数
 */
@Composable
fun rememberSysDeptFormState(current: SysDeptIso? = null): MutableState<SysDeptIso> {
    return remember(current) { mutableStateOf(current ?: SysDeptIso()) }
}