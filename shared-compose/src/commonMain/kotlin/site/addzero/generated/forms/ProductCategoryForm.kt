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
 * ProductCategory 表单属性常量
 */
object ProductCategoryFormProps {
    const val name = "name"
    const val description = "description"
    const val parent = "parent"
    const val products = "products"
    const val sort = "sort"
    const val enabled = "enabled"

    /**
     * 获取所有字段名列表（按默认顺序）
     */
    fun getAllFields(): List<String> {
        return listOf(name, description, parent, products, sort, enabled)
    }
}

@Composable
fun ProductCategoryForm(
    state: MutableState<ProductCategoryIso>,
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    dslConfig: ProductCategoryFormDsl.() -> Unit = {}
) {
    AddDrawer(
        visible = visible,
        title = title,
        onClose = onClose,
        onSubmit = onSubmit,
        confirmEnabled = confirmEnabled,
    ) {
        ProductCategoryFormOriginal(state, dslConfig)
    }
}

@Composable
fun ProductCategoryFormOriginal(
    state: MutableState<ProductCategoryIso>,
    dslConfig: ProductCategoryFormDsl.() -> Unit = {}
) {
    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
    val dsl = ProductCategoryFormDsl(state, renderMap).apply(dslConfig)

    // 默认字段渲染映射（保持原有顺序）
    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
        ProductCategoryFormProps.name to {
            AddTextField(
                value = state.value.name?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(name = if (it.isNullOrEmpty()) "" else it.parseObjectByKtx())
                },
                label = "分类名称",
                isRequired = true
            )
        },
        ProductCategoryFormProps.description to {
            AddTextField(
                value = state.value.description?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(description = if (it.isNullOrEmpty()) null else it.parseObjectByKtx())
                },
                label = "分类描述",
                isRequired = false
            )
        },
        ProductCategoryFormProps.parent to {
            var dataList by remember { mutableStateOf<List<ProductCategoryIso>>(emptyList()) }

            LaunchedEffect(Unit) {
                try {
                    val provider = Iso2DataProvider.isoToDataProvider[ProductCategoryIso::class]
                    dataList = provider?.invoke("") as? List<ProductCategoryIso> ?: emptyList()
                } catch (e: Exception) {
                    println("加载 parent 数据失败: ${e.message}")
                    dataList = emptyList()
                }
            }

            AddGenericSingleSelector(
                value = state.value.parent,
                onValueChange = { state.value = state.value.copy(parent = it) },
                placeholder = "父分类",
                dataProvider = { dataList },
                getId = { it.id ?: 0L },
                getLabel = { it.name ?: "" },
                
            )
        },
        ProductCategoryFormProps.products to {
            var dataList by remember { mutableStateOf<List<ProductIso>>(emptyList()) }

            LaunchedEffect(Unit) {
                try {
                    val provider = Iso2DataProvider.isoToDataProvider[ProductIso::class]
                    dataList = provider?.invoke("") as? List<ProductIso> ?: emptyList()
                } catch (e: Exception) {
                    println("加载 products 数据失败: ${e.message}")
                    dataList = emptyList()
                }
            }

            AddGenericMultiSelector(
                value = state.value.products ?: emptyList(),
                onValueChange = { state.value = state.value.copy(products = it) },
                placeholder = "产品",
                dataProvider = { dataList },
                getId = { it.id ?: 0L },
                getLabel = { it.name ?: "" },
                
            )
        },
        ProductCategoryFormProps.sort to {
            AddIntegerField(
                value = state.value.sort?.toString() ?: "",
                onValueChange = {
                    state.value = state.value.copy(sort = if (it.isNullOrEmpty()) null else it.parseObjectByKtx())
                },
                label = "排序",
                isRequired = false
            )
        },
        ProductCategoryFormProps.enabled to {
            AddSwitchField(
                value = state.value.enabled ?: false,
                onValueChange = { state.value = state.value.copy(enabled = it) },
                label = "是否启用"
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

class ProductCategoryFormDsl(
    val state: MutableState<ProductCategoryIso>,
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
        render: (@Composable (MutableState<ProductCategoryIso>) -> Unit)? = null
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
        render: (@Composable (MutableState<ProductCategoryIso>) -> Unit)? = null
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
     * 配置 parent 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun parent(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ProductCategoryIso>) -> Unit)? = null
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
     * 配置 products 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun products(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ProductCategoryIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("products")
                renderMap.remove("products")
            }
            render != null -> {
                hiddenFields.remove("products")
                renderMap["products"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("products")
                renderMap.remove("products")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("products", orderValue)
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
        render: (@Composable (MutableState<ProductCategoryIso>) -> Unit)? = null
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
     * 配置 enabled 字段
     * @param hidden 是否隐藏该字段
     * @param order 字段显示顺序（数值越小越靠前）
     * @param render 自定义渲染函数
     */
    fun enabled(
        hidden: Boolean = false,
        order: Int? = null,
        render: (@Composable (MutableState<ProductCategoryIso>) -> Unit)? = null
    ) {
        when {
            hidden -> {
                hiddenFields.add("enabled")
                renderMap.remove("enabled")
            }
            render != null -> {
                hiddenFields.remove("enabled")
                renderMap["enabled"] = { render(state) }
            }
            else -> {
                hiddenFields.remove("enabled")
                renderMap.remove("enabled")
            }
        }

        // 处理排序
        order?.let { orderValue ->
            updateFieldOrder("enabled", orderValue)
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
            fieldOrder.addAll(ProductCategoryFormProps.getAllFields())
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
            fieldOrder.addAll(ProductCategoryFormProps.getAllFields())
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
        val allFields = ProductCategoryFormProps.getAllFields()
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
 * 记住 ProductCategory 表单状态的便捷函数
 */
@Composable
fun rememberProductCategoryFormState(current: ProductCategoryIso? = null): MutableState<ProductCategoryIso> {
    return remember(current) { mutableStateOf(current ?: ProductCategoryIso()) }
}