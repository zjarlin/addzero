package com.addzero.kmp.strategy.impl

import com.addzero.kmp.strategy.FormStrategy
import com.addzero.kmp.util.*
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

/**
 * 🎯 单个对象选择策略
 *
 * 处理单个对象类型字段，生成单选选择器
 */
object GenericSingleStrategy : FormStrategy {

    override val name: String = "GenericSingleStrategy"

    override fun calculateWeight(prop: KSPropertyDeclaration): Int {
        val typeName = prop.typeName
        val declaration = prop.type.resolve().declaration

        // 检查是否为 Jimmer 实体
        val isJimmerEntityType = isJimmerEntity(declaration)

        // 检查是否为枚举类型
        val isEnumType = isEnum(declaration)

        // 检查是否为集合类型
        val isCollectionType = prop.isCollectionType()

        println("GenericSingleStrategy: ${declaration.simpleName.asString()}, isJimmerEntity: $isJimmerEntityType, isEnum: $isEnumType, isCollection: $isCollectionType")

        // 如果是基础类型，直接返回 0，不处理
        if (isBasicType(typeName)) {
            return 0
        }

        // 使用布尔值 + 操作符累加计算权重
        return isJimmerEntityType +
                (!isEnumType) +  // 排除枚举类型
                (!isCollectionType)  // 排除集合类型
    }

    private fun isBasicType(typeName: String): Boolean {
        val basicTypes = setOf(
            "String", "Long", "Int", "Boolean", "Double", "Float",
            "BigDecimal", "LocalDate", "LocalDateTime", "Instant"
        )
        return basicTypes.any { typeName.contains(it) }
    }

    /**
     * 在类型的属性中查找带有 @LabelProp 注解的属性
     * 如果有多个 @LabelProp 属性，返回第一个非空的那个
     */
    private fun findLabelPropInType(classDeclaration: KSClassDeclaration): String {
        try {
            // 获取类型的所有属性
            val properties = classDeclaration.getAllProperties()

            // 查找所有带有 @LabelProp 注解的属性
            val labelProperties = properties.filter { property ->
                property.annotations.any { annotation ->
                    annotation.shortName.asString() == "LabelProp"
                }
            }.toList()

            if (labelProperties.isNotEmpty()) {
                // 如果有多个 @LabelProp 属性，选择第一个非空的
                val selectedProperty = labelProperties.firstOrNull { property ->
                    val propertyName = property.simpleName.asString()
                    // 这里可以添加更复杂的非空检查逻辑
                    // 目前简单返回第一个找到的
                    propertyName.isNotBlank()
                } ?: labelProperties.first()

                val labelFieldName = selectedProperty.simpleName.asString()
                if (labelProperties.size > 1) {
                    println("找到多个 @LabelProp 标记的属性: ${classDeclaration.simpleName.asString()}, 选择: ${labelFieldName}")
                } else {
                    println("找到 @LabelProp 标记的属性: ${classDeclaration.simpleName.asString()}.${labelFieldName}")
                }
                return labelFieldName
            } else {
                println("在 ${classDeclaration.simpleName.asString()} 中未找到 @LabelProp 标记的属性，使用默认值 'name'")
                return "name"  // 默认使用 name 字段
            }
        } catch (e: Exception) {
            println("查找 @LabelProp 属性时发生错误: ${e.message}")
            return "name"  // 出错时使用默认值
        }
    }

    override fun genCode(prop: KSPropertyDeclaration): String {
        val name = prop.name
        val label = prop.label
        val isRequired = prop.isRequired
        val defaultValue = prop.defaultValue
        val typeName = prop.typeName.replace("?", "").trim()
        val declaration = prop.type.resolve().declaration
        val typeOrGenericClassDeclaration = declaration as KSClassDeclaration

        val entityClassName = (prop.parentDeclaration as? KSClassDeclaration)?.simpleName?.asString()
            ?: throw IllegalStateException("无法获取实体类名")

        // 新逻辑：查找字段类型的属性中带有 @LabelProp 注解的属性
        val labelField = findLabelPropInType(typeOrGenericClassDeclaration)
        val simpleName = typeOrGenericClassDeclaration.simpleName.asString()
        val isoTypeName = "${simpleName}Iso"

        val istree = typeOrGenericClassDeclaration.hasProperty("children")
        val treedsl = if (istree) """getChildren = { it.children?:emptyList() }""" else ""

        return """
            |        ${entityClassName}FormProps.$name to {
            |            var dataList by remember { mutableStateOf<List<${isoTypeName}>>(emptyList()) }
            |
            |            LaunchedEffect(Unit) {
            |                try {
            |                    val provider = Iso2DataProvider.isoToDataProvider[${isoTypeName}::class]
            |                    dataList = provider?.invoke("") as? List<${isoTypeName}> ?: emptyList()
            |                } catch (e: Exception) {
            |                    println("加载 $name 数据失败: ${'$'}{e.message}")
            |                    dataList = emptyList()
            |                }
            |            }
            |
            |            AddGenericSingleSelector(
            |                value = state.value.$name,
            |                onValueChange = { state.value = state.value.copy($name = it) },
            |                placeholder = $label,
            |                dataProvider = { dataList },
            |                getId = { it.id ?: 0L },
            |                getLabel = { it.$labelField ?: "" },
            |               $treedsl 
            |            )
            |        }
        """.trimMargin()
    }

}
