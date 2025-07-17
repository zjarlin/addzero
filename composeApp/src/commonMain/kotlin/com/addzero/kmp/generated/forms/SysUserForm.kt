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
 * SysUser 表单属性常量
 */
object SysUserFormProps {
    const val price = "price"
    const val testInt = "testInt"
    const val phone = "phone"
    const val email = "email"
    const val username = "username"
    const val password = "password"
    const val avatar = "avatar"
    const val nickname = "nickname"
    const val gender = "gender"
    const val depts = "depts"
    const val roles = "roles"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(price, testInt, phone, email, username, password, avatar, nickname, gender, depts, roles)
    }
}

@Composable
fun SysUserForm(
    state: MutableState<SysUserIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: SysUserFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        SysUserFormOriginal(state, dslConfig)
    }
}

@Composable
fun SysUserFormOriginal(
    state: MutableState<SysUserIso>,
    dslConfig: SysUserFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = SysUserFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        SysUserFormProps.price to {
            AddMoneyField(
                value = state.value.price?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(price = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "价格",
                isRequired = true,
                currency = "CNY"
            )
        },
        SysUserFormProps.testInt to {
            AddIntegerField(
                value = state.value.testInt?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(testInt = if (it.isBlank()) 0 else it.parseObjectByKtx())
                },
                label = "整数",
                isRequired = true
            )
        },
        SysUserFormProps.phone to {
            AddPhoneField(
                value = state.value.phone?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(phone = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "手机号",
                isRequired = false
            )
        },
        SysUserFormProps.email to {
            AddEmailField(
                value = state.value.email?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(email = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                showCheckEmail = false,
                label = "电子邮箱",
                isRequired = true
            )
        },
        SysUserFormProps.username to {
            AddUsernameField(
                value = state.value.username?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(username = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "用户名",
                isRequired = true
            )
        },
        SysUserFormProps.password to {
            AddPasswordField(
                value = state.value.password?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(password = if (it.isBlank()) "" else it.parseObjectByKtx())
                },
                label = "密码",
                isRequired = true
            )
        },
        SysUserFormProps.avatar to {
            AddTextField(
                value = state.value.avatar?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(avatar = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "头像",
                isRequired = false
            )
        },
        SysUserFormProps.nickname to {
            AddTextField(
                value = state.value.nickname?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(nickname = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "昵称",
                isRequired = false
            )
        },
        SysUserFormProps.gender to {
            AddTextField(
                value = state.value.gender?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(gender = if (it.isBlank()) null else it.parseObjectByKtx())
                },
                label = "性别",
                isRequired = false
            )
        },
        SysUserFormProps.depts to {
            var dataList by remember { mutableStateOf<List<SysDeptIso>>(emptyList()) }

            LaunchedEffect(Unit) {
                try {
                    val provider = Iso2DataProvider.isoToDataProvider[SysDeptIso::class]
                    dataList = provider?.invoke("") as? List<SysDeptIso> ?: emptyList()
                } catch (e: Exception) {
                    println("加载 depts 数据失败: ${e.message}")
                    dataList = emptyList()
                }
            }

            AddGenericMultiSelector(
                value = state.value.depts ?: emptyList(),
                onValueChange = { state.value = state.value.copy(depts = it) },
                placeholder = "所属部门",
                dataProvider = { dataList },
                getId = { it.id ?: 0L },
                getLabel = { it.name ?: "" },
                getChildren = { it.children?:emptyList() }
            )
        },
        SysUserFormProps.roles to {
            var dataList by remember { mutableStateOf<List<SysRoleIso>>(emptyList()) }

            LaunchedEffect(Unit) {
                try {
                    val provider = Iso2DataProvider.isoToDataProvider[SysRoleIso::class]
                    dataList = provider?.invoke("") as? List<SysRoleIso> ?: emptyList()
                } catch (e: Exception) {
                    println("加载 roles 数据失败: ${e.message}")
                    dataList = emptyList()
                }
            }

            AddGenericMultiSelector(
                value = state.value.roles ?: emptyList(),
                onValueChange = { state.value = state.value.copy(roles = it) },
                placeholder = "角色列表",
                dataProvider = { dataList },
                getId = { it.id ?: 0L },
                getLabel = { it.roleName ?: "" },
                
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

class SysUserFormDsl(
    val state: MutableState<SysUserIso>,
    private val renderMap: MutableMap<String, @Composable () -> Unit>
) {
    // 隐藏字段集合
    val hiddenFields = mutableSetOf<String>()

    // 字段显示顺序（如果为空则使用默认顺序）
    val fieldOrder = mutableListOf<String>()

    // 字段排序映射：字段名 -> 排序值
    private val fieldOrderMap = mutableMapOf<String, Int>()

    /**
     * 配置 price 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun price(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysUserIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("price")
                renderMap.remove("price")
            }
            render != null -> {
                hiddenFields.remove("price")
                renderMap["price"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("price")
                renderMap.remove("price")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("price", orderValue)
        }
    }

    /**
     * 配置 testInt 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun testInt(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysUserIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("testInt")
                renderMap.remove("testInt")
            }
            render != null -> {
                hiddenFields.remove("testInt")
                renderMap["testInt"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("testInt")
                renderMap.remove("testInt")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("testInt", orderValue)
        }
    }

    /**
     * 配置 phone 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun phone(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysUserIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("phone")
                renderMap.remove("phone")
            }
            render != null -> {
                hiddenFields.remove("phone")
                renderMap["phone"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("phone")
                renderMap.remove("phone")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("phone", orderValue)
        }
    }

    /**
     * 配置 email 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun email(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysUserIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("email")
                renderMap.remove("email")
            }
            render != null -> {
                hiddenFields.remove("email")
                renderMap["email"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("email")
                renderMap.remove("email")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("email", orderValue)
        }
    }

    /**
     * 配置 username 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun username(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysUserIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("username")
                renderMap.remove("username")
            }
            render != null -> {
                hiddenFields.remove("username")
                renderMap["username"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("username")
                renderMap.remove("username")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("username", orderValue)
        }
    }

    /**
     * 配置 password 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun password(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysUserIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("password")
                renderMap.remove("password")
            }
            render != null -> {
                hiddenFields.remove("password")
                renderMap["password"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("password")
                renderMap.remove("password")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("password", orderValue)
        }
    }

    /**
     * 配置 avatar 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun avatar(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysUserIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("avatar")
                renderMap.remove("avatar")
            }
            render != null -> {
                hiddenFields.remove("avatar")
                renderMap["avatar"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("avatar")
                renderMap.remove("avatar")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("avatar", orderValue)
        }
    }

    /**
     * 配置 nickname 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun nickname(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysUserIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("nickname")
                renderMap.remove("nickname")
            }
            render != null -> {
                hiddenFields.remove("nickname")
                renderMap["nickname"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("nickname")
                renderMap.remove("nickname")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("nickname", orderValue)
        }
    }

    /**
     * 配置 gender 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun gender(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysUserIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("gender")
                renderMap.remove("gender")
            }
            render != null -> {
                hiddenFields.remove("gender")
                renderMap["gender"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("gender")
                renderMap.remove("gender")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("gender", orderValue)
        }
    }

    /**
     * 配置 depts 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun depts(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysUserIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("depts")
                renderMap.remove("depts")
            }
            render != null -> {
                hiddenFields.remove("depts")
                renderMap["depts"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("depts")
                renderMap.remove("depts")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("depts", orderValue)
        }
    }

    /**
     * 配置 roles 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun roles(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<SysUserIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("roles")
                renderMap.remove("roles")
            }
            render != null -> {
                hiddenFields.remove("roles")
                renderMap["roles"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("roles")
                renderMap.remove("roles")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("roles", orderValue)
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
            fieldOrder.addAll(SysUserFormProps.getAllFields())
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
            fieldOrder.addAll(SysUserFormProps.getAllFields())
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
        val allFields = SysUserFormProps.getAllFields()
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
 * 记住 SysUser 表单状态的便捷函数
 */
@Composable
fun rememberSysUserFormState(current: SysUserIso? = null): MutableState<SysUserIso> {
    return remember(current) { mutableStateOf(current ?: SysUserIso()) }
}