package com.addzero.kmp.generator

import com.addzero.kmp.strategy.FormStrategyManager
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import java.io.File

/**
 * 表单代码生成器
 *
 * 负责生成表单组件的代码
 */
class FormCodeGenerator(
    private val logger: KSPLogger
) {

    /**
     * 使用策略模式生成表单代码（推荐方法）
     */
    fun generateFormCodeWithStrategy(
        entityDeclaration: KSClassDeclaration,
        packageName: String = "com.addzero.kmp.forms"
    ): String {
        val entityClassName = entityDeclaration.simpleName.asString()
        val isoClassName = "${entityClassName}Iso"

        // 获取所有属性，过滤 BaseEntity 字段
        val properties = entityDeclaration.getAllProperties()
            .filter { !FormStrategyManager.shouldFilterBaseEntity(it) }
            .toList()

        // 使用策略模式生成表单字段
        val formFields = properties.joinToString(",\n") { prop ->
            FormStrategyManager.generateCode(prop)
        }

        // 生成 FormProps 对象
        val formProps = generateFormPropsWithStrategy(entityClassName, properties)

        return """
            |package $packageName
            |
            |import androidx.compose.foundation.layout.*
            |import androidx.compose.material3.*
            |import androidx.compose.runtime.*
            |import androidx.compose.ui.Modifier
            |import androidx.compose.ui.unit.dp
            |import com.addzero.kmp.component.high_level.AddMultiColumnContainer
            |import com.addzero.kmp.component.drawer.AddDrawer
            |import com.addzero.kmp.component.form.*
            |import com.addzero.kmp.component.form.number.AddMoneyField
            |import com.addzero.kmp.component.form.number.AddNumberField
            |import com.addzero.kmp.component.form.number.AddIntegerField
            |import com.addzero.kmp.component.form.number.AddDecimalField
            |import com.addzero.kmp.component.form.number.AddPercentageField
            |import com.addzero.kmp.component.form.text.AddTextField
            |import com.addzero.kmp.component.form.text.AddPasswordField
            |import com.addzero.kmp.component.form.text.AddEmailField
            |import com.addzero.kmp.component.form.text.AddPhoneField
            |import com.addzero.kmp.component.form.text.AddUrlField
            |import com.addzero.kmp.component.form.text.AddUsernameField
            |import com.addzero.kmp.component.form.text.AddIdCardField
            |import com.addzero.kmp.component.form.text.AddBankCardField
            |import com.addzero.kmp.component.form.date.AddDateField
            |import com.addzero.kmp.component.form.date.DateType
            |import com.addzero.kmp.component.form.switch.AddSwitchField
            |import com.addzero.kmp.component.form.selector.AddGenericSingleSelector
            |import com.addzero.kmp.component.form.selector.AddGenericMultiSelector
            |import com.addzero.kmp.core.ext.parseObjectByKtx
            |import com.addzero.kmp.core.validation.RegexEnum
            |import com.addzero.kmp.generated.isomorphic.*
            |import com.addzero.kmp.generated.forms.dataprovider.Iso2DataProvider
            import com.addzero.kmp.generated.enums.*
|
            |
            |$formProps
            |
            |@Composable
            |fun ${entityClassName}Form(
            |    state: MutableState<${isoClassName}>,
            |    visible: Boolean,
            |    title: String,
            |    onClose: () -> Unit,
            |    onSubmit: () -> Unit,
            |    confirmEnabled: Boolean = true,
            |    dslConfig: ${entityClassName}FormDsl.() -> Unit = {}
            |) {
            |    AddDrawer(
            |        visible = visible,
            |        title = title,
            |        onClose = onClose,
            |        onSubmit = onSubmit,
            |        confirmEnabled = confirmEnabled,
            |    ) {
            |        ${entityClassName}FormOriginal(state, dslConfig)
            |    }
            |}
            |
            |@Composable
            |fun ${entityClassName}FormOriginal(
            |    state: MutableState<${isoClassName}>,
            |    dslConfig: ${entityClassName}FormDsl.() -> Unit = {}
            |) {
            |    val renderMap = remember { mutableMapOf<String, @Composable () -> Unit>() }
            |    val dsl = ${entityClassName}FormDsl(state, renderMap).apply(dslConfig)
            |
            |    // 默认字段渲染映射（保持原有顺序）
            |    val defaultRenderMap = linkedMapOf<String, @Composable () -> Unit>(
            |$formFields
            |    )
            |
            |    // 根据 DSL 配置计算最终要渲染的字段列表（保持顺序）
            |    val finalItems = remember(renderMap, dsl.hiddenFields, dsl.fieldOrder) {
            |        // 1. 获取字段顺序
            |        val orderedFieldNames = if (dsl.fieldOrder.isNotEmpty()) {
            |            // 使用 DSL 中设置的顺序（可能通过 order() 方法或 order 参数设置）
            |            dsl.fieldOrder
            |        } else {
            |            // 使用默认顺序
            |            defaultRenderMap.keys.toList()
            |        }
            |
            |        // 2. 按顺序构建最终的渲染列表
            |        orderedFieldNames
            |            .filter { fieldName -> fieldName !in dsl.hiddenFields } // 过滤隐藏字段
            |            .mapNotNull { fieldName ->
            |                // 优先使用自定义渲染，否则使用默认渲染
            |                when {
            |                    renderMap.containsKey(fieldName) -> renderMap[fieldName]
            |                    defaultRenderMap.containsKey(fieldName) -> defaultRenderMap[fieldName]
            |                    else -> null
            |                }
            |            }
            |    }
            |
            |    AddMultiColumnContainer(
            |        howMuchColumn = 2,
            |        items = finalItems
            |    )
            |}
            |
            |class ${entityClassName}FormDsl(
            |    val state: MutableState<${isoClassName}>,
            |    private val renderMap: MutableMap<String, @Composable () -> Unit>
            |) {
            |    // 隐藏字段集合
            |    val hiddenFields = mutableSetOf<String>()
            |
            |    // 字段显示顺序（如果为空则使用默认顺序）
            |    val fieldOrder = mutableListOf<String>()
            |
            |    // 字段排序映射：字段名 -> 排序值
            |    private val fieldOrderMap = mutableMapOf<String, Int>()
            |
            |${generateDslMethodsWithStrategy(entityClassName, properties)}
            |
            |    /**
            |     * 隐藏指定字段
            |     */
            |    fun hide(vararg fields: String) {
            |        hiddenFields.addAll(fields)
            |    }
            |
            |    /**
            |     * 设置字段显示顺序
            |     * @param fields 字段名列表，按显示顺序排列
            |     */
            |    fun order(vararg fields: String) {
            |        fieldOrder.clear()
            |        fieldOrder.addAll(fields)
            |    }
            |
            |    /**
            |     * 在指定字段之前插入字段
            |     */
            |    fun insertBefore(targetField: String, vararg newFields: String) {
            |        if (fieldOrder.isEmpty()) {
            |            // 如果没有自定义顺序，先初始化为默认顺序
            |            fieldOrder.addAll(${entityClassName}FormProps.getAllFields())
            |        }
            |        val index = fieldOrder.indexOf(targetField)
            |        if (index >= 0) {
            |            fieldOrder.addAll(index, newFields.toList())
            |        }
            |    }
            |
            |    /**
            |     * 在指定字段之后插入字段
            |     */
            |    fun insertAfter(targetField: String, vararg newFields: String) {
            |        if (fieldOrder.isEmpty()) {
            |            // 如果没有自定义顺序，先初始化为默认顺序
            |            fieldOrder.addAll(${entityClassName}FormProps.getAllFields())
            |        }
            |        val index = fieldOrder.indexOf(targetField)
            |        if (index >= 0) {
            |            fieldOrder.addAll(index + 1, newFields.toList())
            |        }
            |    }
            |
            |    /**
            |     * 更新字段排序
            |     * @param fieldName 字段名
            |     * @param orderValue 排序值（数值越小越靠前）
            |     */
            |    private fun updateFieldOrder(fieldName: String, orderValue: Int) {
            |        fieldOrderMap[fieldName] = orderValue
            |
            |        // 重新计算字段顺序
            |        val allFields = ${entityClassName}FormProps.getAllFields()
            |        val sortedFields = allFields.sortedWith { field1, field2 ->
            |            val order1 = fieldOrderMap[field1] ?: Int.MAX_VALUE
            |            val order2 = fieldOrderMap[field2] ?: Int.MAX_VALUE
            |            when {
            |                order1 != Int.MAX_VALUE && order2 != Int.MAX_VALUE -> order1.compareTo(order2)
            |                order1 != Int.MAX_VALUE -> -1 // field1 有排序值，排在前面
            |                order2 != Int.MAX_VALUE -> 1  // field2 有排序值，排在前面
            |                else -> allFields.indexOf(field1).compareTo(allFields.indexOf(field2)) // 都没有排序值，保持原有顺序
            |            }
            |        }
            |
            |        fieldOrder.clear()
            |        fieldOrder.addAll(sortedFields)
            |    }
            |}
            |
            |/**
            | * 记住 ${entityClassName} 表单状态的便捷函数
            | */
            |@Composable
            |fun remember${entityClassName}FormState(current: ${isoClassName}? = null): MutableState<${isoClassName}> {
            |    return remember(current) { mutableStateOf(current ?: ${isoClassName}()) }
            |}
        """.trimMargin()
    }

    /**
     * 使用策略模式写入表单文件（推荐方法）
     */
    fun writeFormFileWithStrategy(entityDeclaration: KSClassDeclaration, outputDir: String, packageName: String) {
        val formCode = generateFormCodeWithStrategy(entityDeclaration, packageName)
        val fileName = "${entityDeclaration.simpleName.asString()}Form.kt"
        val file = File("$outputDir/$fileName")
        file.parentFile?.mkdirs()
        file.writeText(formCode)
        logger.info("生成表单文件（策略模式）: ${file.absolutePath}")
    }

    /**
     * 使用策略模式生成 FormProps 对象
     */
    private fun generateFormPropsWithStrategy(
        entityClassName: String,
        properties: List<KSPropertyDeclaration>
    ): String {
        val propConstants = properties.joinToString("\n") { prop ->
            "    const val ${prop.simpleName.asString()} = \"${prop.simpleName.asString()}\""
        }

        val allFieldsList = properties.joinToString(", ") { prop ->
            prop.simpleName.asString()
        }

        return """
            |/**
             | * $entityClassName 表单属性常量
             | */
            |object ${entityClassName}FormProps {
            |$propConstants
            |
            |    /**
            |     * 获取所有字段名列表（按默认顺序）
            |     */
            |    fun getAllFields(): List<String> {
            |        return listOf($allFieldsList)
            |    }
            |}
        """.trimMargin()
    }

    /**
     * 使用策略模式生成 DSL 方法
     * 支持 hidden 和 order 参数
     */
    private fun generateDslMethodsWithStrategy(
        entityClassName: String,
        properties: List<KSPropertyDeclaration>
    ): String {
        return properties.joinToString("\n\n") { prop ->
            val propName = prop.simpleName.asString()
            """
            |    /**
            |     * 配置 $propName 字段
            |     * @param hidden 是否隐藏该字段
            |     * @param order 字段显示顺序（数值越小越靠前）
            |     * @param render 自定义渲染函数
            |     */
            |    fun $propName(
            |        hidden: Boolean = false,
            |        order: Int? = null,
            |        render: (@Composable (MutableState<${entityClassName}Iso>) -> Unit)? = null
            |    ) {
            |        when {
            |            hidden -> {
            |                hiddenFields.add("$propName")
            |                renderMap.remove("$propName")
            |            }
            |            render != null -> {
            |                hiddenFields.remove("$propName")
            |                renderMap["$propName"] = { render(state) }
            |            }
            |            else -> {
            |                hiddenFields.remove("$propName")
            |                renderMap.remove("$propName")
            |            }
            |        }
            |
            |        // 处理排序
            |        order?.let { orderValue ->
            |            updateFieldOrder("$propName", orderValue)
            |        }
            |    }
            """.trimMargin()
        }
    }
}
