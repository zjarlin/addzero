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
 * SysRole 表单属性常量
 */
object SysRoleFormProps {
    const val roleCode = "roleCode"
    const val roleName = "roleName"
    const val systemFlag = "systemFlag"
    const val status = "status"
    const val sysUsers = "sysUsers"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(roleCode, roleName, systemFlag, status, sysUsers)
    }
}

@Composable
fun SysRoleForm(
    state: MutableState<SysRoleIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: SysRoleFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        SysRoleFormOriginal(state, dslConfig)
    }
}

@Composable
fun SysRoleFormOriginal(
    state: MutableState<SysRoleIso>,
    dslConfig: SysRoleFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = SysRoleFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        SysRoleFormProps.roleCode to {
            AddTextField(
                value = state.value.roleCode?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(roleCode = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "角色编码",
                isRequired = true
            )
        },
        SysRoleFormProps.roleName to {
            AddTextField(
                value = state.value.roleName?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(roleName = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "角色名称",
                isRequired = true
            )
        },
        SysRoleFormProps.systemFlag to {
            AddSwitchField(
                value = state.value.systemFlag ?: false,
                onValueChange = { state.value = state.value.copy(systemFlag = it) },
                label = "是否为系统角色"
            )
        },
        SysRoleFormProps.status to {
            AddTextField(
                value = state.value.status?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(status = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "角色状态",
                isRequired = true
            )
        },
        SysRoleFormProps.sysUsers to {
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
                placeholder = "sysUsers",
                dataProvider = { dataList },
                getId = { it.id ?: 0L },
                getLabel = { it.email ?: "" }
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

class SysRoleFormDsl(
    val state: MutableState<SysRoleIso>,
    private val renderMap: MutableMap<String, @Composable () -> Unit>
) {
    // 隐藏字段集合
    val hiddenFields = mutableSetOf<String>()

    // 字段显示顺序（如果为空则使用默认顺序）
    val fieldOrder = mutableListOf<String>()

    // 字段排序映射：字段名 -> 排序值
    private val fieldOrderMap = mutableMapOf<String, Int>()

    /**
     * 配置 roleCode 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun roleCode(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysRoleIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("roleCode")
                renderMap.remove("roleCode")
            }
            render != null -> {
                hiddenFields.remove("roleCode")
                renderMap["roleCode"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("roleCode")
                renderMap.remove("roleCode")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("roleCode", orderValue)
        }
    }

    /**
     * 配置 roleName 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun roleName(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysRoleIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("roleName")
                renderMap.remove("roleName")
            }
            render != null -> {
                hiddenFields.remove("roleName")
                renderMap["roleName"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("roleName")
                renderMap.remove("roleName")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("roleName", orderValue)
        }
    }

    /**
     * 配置 systemFlag 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun systemFlag(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysRoleIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("systemFlag")
                renderMap.remove("systemFlag")
            }
            render != null -> {
                hiddenFields.remove("systemFlag")
                renderMap["systemFlag"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("systemFlag")
                renderMap.remove("systemFlag")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("systemFlag", orderValue)
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
        render: (@Composable (MutableState<SysRoleIso>) -> Unit)? = null
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
     * 配置 sysUsers 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun sysUsers(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysRoleIso>) -> Unit)? = null
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
            fieldOrder.addAll(SysRoleFormProps.getAllFields())
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
            fieldOrder.addAll(SysRoleFormProps.getAllFields())
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
        val allFields = SysRoleFormProps.getAllFields()
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
 * 记住 SysRole 表单状态的便捷函数
 */
@Composable
fun rememberSysRoleFormState(current: SysRoleIso? = null): MutableState<SysRoleIso> {
    return remember(current) { mutableStateOf(current ?: SysRoleIso()) }
}